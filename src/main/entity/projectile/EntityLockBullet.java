package sweetmagic.init.entity.projectile;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import sweetmagic.client.particle.ParticleOrbShort;

public class EntityLockBullet extends EntityBaseMagicShot {

    private EnumFacing direction;
    private EntityLivingBase target;
    private UUID targetUniqueId;
    private double targetDeltaX;
    private double targetDeltaY;
    private double targetDeltaZ;
    private int steps;

	public EntityLockBullet(World world) {
		super(world);
		this.setSize(0.35F, 0.35F);
	}

	public EntityLockBullet(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityLockBullet(World world, EntityLivingBase thrower, EntityLivingBase target, ItemStack stack) {
		super(world, thrower, stack);
		this.target = target;
		this.targetUniqueId = target.getUniqueID();
        this.direction = EnumFacing.UP;
        EnumFacing face = thrower.getHorizontalFacing();
        boolean isZ = face == EnumFacing.NORTH || face == EnumFacing.SOUTH;
        this.selectNextMoveDirection(( isZ ? EnumFacing.Axis.Z : EnumFacing.Axis.X ));
	}

	public void onUpdate() {

		super.onUpdate();

		if (!this.world.isRemote) {

			if (this.target == null || !this.target.isEntityAlive() ||
					this.target instanceof EntityPlayer && ((EntityPlayer) this.target).isSpectator()) {

				if (!this.hasNoGravity()) {
					this.motionY -= 0.04D;
				}
			}

			else {
				this.targetDeltaX = MathHelper.clamp(this.targetDeltaX * 1.025D, -1D, 1D);
				this.targetDeltaY = MathHelper.clamp(this.targetDeltaY * 1.025D, -1D, 1D);
				this.targetDeltaZ = MathHelper.clamp(this.targetDeltaZ * 1.025D, -1D, 1D);
				this.motionX += (this.targetDeltaX - this.motionX) * this.getSpeed();
				this.motionY += (this.targetDeltaY - this.motionY) * this.getSpeed();
				this.motionZ += (this.targetDeltaZ - this.motionZ) * this.getSpeed();
			}
		}

		this.setPosition(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		ProjectileHelper.rotateTowardsMovement(this, 0.5F);

		if (!this.world.isRemote && this.target != null && !this.target.isDead) {

			if (this.steps > 0) {
				--this.steps;

				if (this.steps == 0) {
					this.selectNextMoveDirection(this.direction == null ? null : this.direction.getAxis());
				}
			}

			if (this.direction != null) {
				BlockPos pos = new BlockPos(this);
				EnumFacing.Axis face = this.direction.getAxis();

				if (this.world.isBlockNormalCube(pos.offset(this.direction), false)) {
					this.selectNextMoveDirection(face);
				}

				else {

					BlockPos pos1 = new BlockPos(this.target);

					if (face == EnumFacing.Axis.X && pos.getX() == pos1.getX() ||
						face == EnumFacing.Axis.Z && pos.getZ() == pos1.getZ() ||
						face == EnumFacing.Axis.Y && pos.getY() == pos1.getY()) {
						this.selectNextMoveDirection(face);
					}
				}
			}
		}
	}

	public double getSpeed () {
		return 0.825D;
	}

	private void selectNextMoveDirection(@Nullable EnumFacing.Axis baseFace) {

		double d0 = 0.5D;
		BlockPos pos = (new BlockPos(this)).down();

		if (this.target != null) {
			d0 = (double) this.target.height * 0.5D;
			pos = new BlockPos(this.target.posX, this.target.posY + d0, this.target.posZ);
		}

		double d1 = (double) pos.getX() + 0.5D;
		double d2 = (double) pos.getY() + d0;
		double d3 = (double) pos.getZ() + 0.5D;
		EnumFacing face = null;

		if (pos.distanceSqToCenter(this.posX, this.posY, this.posZ) >= 4D) {

			BlockPos pos1 = new BlockPos(this);
			List<EnumFacing> list = Lists.<EnumFacing> newArrayList();

			if (baseFace != EnumFacing.Axis.X) {

				if (pos1.getX() < pos.getX() && this.world.isAirBlock(pos1.east())) {
					list.add(EnumFacing.EAST);
				}

				else if (pos1.getX() > pos.getX() && this.world.isAirBlock(pos1.west())) {
					list.add(EnumFacing.WEST);
				}
			}

			if (baseFace != EnumFacing.Axis.Y) {

				if (pos1.getY() < pos.getY() && this.world.isAirBlock(pos1.up())) {
					list.add(EnumFacing.UP);
				}

				else if (pos1.getY() > pos.getY() && this.world.isAirBlock(pos1.down())) {
					list.add(EnumFacing.DOWN);
				}
			}

			if (baseFace != EnumFacing.Axis.Z) {

				if (pos1.getZ() < pos.getZ() && this.world.isAirBlock(pos1.south())) {
					list.add(EnumFacing.SOUTH);
				}

				else if (pos1.getZ() > pos.getZ() && this.world.isAirBlock(pos1.north())) {
					list.add(EnumFacing.NORTH);
				}
			}

			face = EnumFacing.random(this.rand);

			if (list.isEmpty()) {
				for (int i = 5; !this.world.isAirBlock(pos1.offset(face)) && i > 0; --i) {
					face = EnumFacing.random(this.rand);
				}
			}

			else {
				face = list.get(this.rand.nextInt(list.size()));
			}

			d1 = this.posX + (double) face.getFrontOffsetX();
			d2 = this.posY + (double) face.getFrontOffsetY();
			d3 = this.posZ + (double) face.getFrontOffsetZ();
		}

		this.setDirection(face);
		double d6 = d1 - this.posX;
		double d7 = d2 - this.posY;
		double d4 = d3 - this.posZ;
		double d5 = (double) MathHelper.sqrt(d6 * d6 + d7 * d7 + d4 * d4);

		this.targetDeltaX = d5 == 0D ? 0D : d6 / d5 * 0.15D;
		this.targetDeltaY = d5 == 0D ? 0D : d7 / d5 * 0.15D;
		this.targetDeltaZ = d5 == 0D ? 0D : d4 / d5 * 0.15D;

		this.isAirBorne = true;
		this.steps = 10 + this.rand.nextInt(5) * 10;
	}

	private void setDirection(@Nullable EnumFacing direction) {
		this.direction = direction;
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {

		if (this.isEntityInvulnerable(source)) { return false; }

		this.markVelocityChanged();
		return false;
	}

	// 自然消滅までの時間 30tick + this.plusTickAir(増やしたい場合は-10とか付ければおっけー)
	protected int plusTickAir() {
		return 400;
	}

	protected void writeEntityToNBT(NBTTagCompound tags) {

		super.writeEntityToNBT(tags);

		if (this.target != null) {
			BlockPos pos = new BlockPos(this.target);
			NBTTagCompound targetTags = NBTUtil.createUUIDTag(this.target.getUniqueID());
			targetTags.setInteger("X", pos.getX());
			targetTags.setInteger("Y", pos.getY());
			targetTags.setInteger("Z", pos.getZ());
			tags.setTag("Target", targetTags);
		}

		if (this.direction != null) {
			tags.setInteger("Dir", this.direction.getIndex());
		}

		tags.setInteger("Steps", this.steps);
		tags.setDouble("TXD", this.targetDeltaX);
		tags.setDouble("TYD", this.targetDeltaY);
		tags.setDouble("TZD", this.targetDeltaZ);
	}

	protected void readEntityFromNBT(NBTTagCompound tags) {

		super.readEntityFromNBT(tags);

		this.steps = tags.getInteger("Steps");
		this.targetDeltaX = tags.getDouble("TXD");
		this.targetDeltaY = tags.getDouble("TYD");
		this.targetDeltaZ = tags.getDouble("TZD");

		if (tags.hasKey("Dir", 99)) {
			this.direction = EnumFacing.getFront(tags.getInteger("Dir"));
		}

		if (tags.hasKey("Target", 10)) {
			NBTTagCompound tags1 = tags.getCompoundTag("Target");
			this.targetUniqueId = NBTUtil.getUUIDFromTag(tags1);
		}
	}

	public boolean isBurning() {
		return false;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {
//		for (int i = 0; i < 3; i++) {
//			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 8F);
//			float f2 = (float) (this.posY - 0.75F + this.rand.nextFloat() * 0.5 + this.motionY * i / 8D);
//			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 8D);
//			float x = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
//			float y = (this.rand.nextFloat() + this.rand.nextFloat()) * 0.15F;
//			float z = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
//			Particle effect = ParticleNomal.create(this.world, f1, f2, f3, x, y, z);
//			this.getParticle().addEffect(effect);
//		}
		Particle effect = ParticleOrbShort.create(this.world, this.posX, this.posY + 0.25F, this.posZ, 0, 0, 0, 43, 33, 168);
		this.getParticle().addEffect(effect);
	}
}
