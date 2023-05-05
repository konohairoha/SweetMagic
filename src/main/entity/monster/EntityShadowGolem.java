package sweetmagic.init.entity.monster;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.SweetMagicCore;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.item.sm.sweetmagic.SMBook;
import sweetmagic.util.EventUtil;
import sweetmagic.util.SMDamage;

public class EntityShadowGolem extends EntityIronGolem {

	public int tickTime = 0;
	public float momentum;
	protected int lerpSteps;
	public float headYaw;
	public float prevHeadYaw;
	protected double lastX;
	protected double lastY;
	protected double lastZ;
	protected double lastYaw;
	protected double lastRot;
	public double lastMoveY;
	private static final DataParameter<Boolean> ISPROTECT = EntityDataManager.<Boolean>createKey(EntityShadowGolem.class, DataSerializers.BOOLEAN);

	public EntityShadowGolem (World world) {
		super(world);
        this.moveHelper = new GolemMoveHelper(this);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ISPROTECT, false);
	}

	public void setProtect(boolean isProtect) {
		this.dataManager.set(ISPROTECT, isProtect);
	}

	public boolean getProtect() {
		return this.dataManager.get(ISPROTECT);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.setProtect(tag.getBoolean("isProtect"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setBoolean("isProtect", this.getProtect());
	}

	protected void collideWithEntity(Entity entity) {

		if (entity instanceof IMob && this.getRNG().nextInt(10) == 0) {
			this.setAttackTarget((EntityLivingBase) entity);
		}

		entity.applyEntityCollision(this);
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		if (this.world.isRemote) {
			this.spawnAppearParticles();
		}
		this.setPlayerCreated(true);
		return livingdata;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.5D);
	}

	// パーティクルスポーン
	private void spawnAppearParticles() {

		for (int i = 0; i < 4; i++) {

			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
			float f2 = (float) (this.posY + 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
			float x = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
			float y = (this.rand.nextFloat() + this.rand.nextFloat()) * 0.0825F;
			float z = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;

			Particle effect = ParticleNomal.create(this.world, f1, f2, f3, x, y, z, 48);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {

		if (!player.isSneaking()) {
			player.startRiding(this);
		}

		else {
			if (player.getHeldItem(hand).getItem() instanceof SMBook) {
				this.isDead = true;
			}
		}

		return super.processInteract(player, hand);
	}

	public boolean attackEntityAsMob(Entity entity) {

		if (!super.attackEntityAsMob(entity)) { return false; }
		entity.hurtResistantTime = 0;

		if (entity instanceof EntityLivingBase) {

			DamageSource src = SMDamage.MagicDamage(this, this);
			AxisAlignedBB aabb = this.getEntityBoundingBox().grow(5F);
			List<Entity> toAttack = this.world.getEntitiesWithinAABBExcludingEntity(this, aabb);

			for (Entity target : toAttack) {

				if (!(target instanceof IMob)) { continue; }

				target.attackEntityFrom(src, 6F);
				target.hurtResistantTime = 0;
				target.motionY += 0.5D;

				if (target instanceof EntityCreeper) {
					// 敵を動かなくさせる
					EventUtil.tameAIDonmov((EntityLiving) target, 2);
				}
			}
		}

		return true;
	}

	public boolean attackEntityFrom(DamageSource src, float amount) {

		if (this.getProtect()) {
			return false;
		}

    	if (src.getImmediateSource() instanceof ISMMob) {
    		amount *= 0.5F;
		}

    	else if (src.getImmediateSource() instanceof EntityBaseMagicShot || src == DamageSource.MAGIC) {
    		amount *= 0.25F;
    	}

		return super.attackEntityFrom(src, amount);
	}

	@Override
	public void onUpdate() {

		// 壁に当たったら
		if (this.collidedHorizontally) {
			this.setPosition(this.posX, this.posY + 1D, this.posZ);
		}

		this.prevHeadYaw = this.headYaw;
//		this.prevPosX = this.posX;
//		this.prevPosY = this.posY;
//		this.prevPosZ = this.posZ;
//		this.setPosition(this.prevPosX, this.prevPosY, this.prevPosZ);
		super.onUpdate();
		this.tickLerp();

		EntityPlayer player = null;
		if (this.isRading()) {
			player = (EntityPlayer) this.getPassengers().get(0);
		}

		if (player != null) {

			this.updateYaw(player);

			while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
				this.prevRotationYaw -= 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
				this.prevRotationYaw += 360.0F;
			}

			while (this.headYaw - this.prevHeadYaw < -180.0F) {
				this.prevHeadYaw -= 360.0F;
			}

			while (this.headYaw - this.prevHeadYaw >= 180.0F) {
				this.prevHeadYaw += 360.0F;
			}

			if (!this.world.isRemote) {

				this.updateSpeed(player);

				this.motionX *= 0.99D;
				this.motionZ *= 0.99D;

				float speed = (float) Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
				if (speed < 0.01D) {
					this.motionX = 0.0D;
					this.motionZ = 0.0D;
				}
			}

			this.move(MoverType.PLAYER, this.motionX, this.motionY, this.motionZ);

			this.isJump();
		}

		if (!this.world.isRemote) { return; }

		this.tickTime++;
		if (this.tickTime % 16 != 0) { return; }

		this.tickTime = 0;
		this.spawnAppearParticles();
	}

	protected void updateYaw(EntityPlayer rider) {

		if (rider == null) { return; }

		float f1 = this.rotationYaw;
		double d3 = MathHelper.wrapDegrees(rider.rotationYaw - (double) this.rotationYaw);
		this.rotationYaw = (float) (this.rotationYaw + d3 / 2.0D);

		if ((this.rotationYaw - f1) > 5F) {
			this.headYaw = 45F;
		}

		else if ((this.rotationYaw - f1) < -5F) {
			this.headYaw = -45F;
		}

		else {
			this.headYaw = 0F;
		}
	}

	protected void updateSpeed(EntityPlayer rider) {

		float yaw = this.rotationYaw * 0.017453292F;

		if (rider != null && rider.moveForward > 0F) {

			double d1 = -Math.sin(yaw);
			double d2 = Math.cos(yaw);
			double d9 = this.motionX * this.motionX + this.motionZ * this.motionZ;
			double d10 = Math.sqrt(d9);

			this.motionX += d1 * 0.2D;
			this.motionZ += d2 * 0.2D;

			if (d10 > this.getMaxSpeed()) {
				this.motionX = d1 * this.getMaxSpeed();
				this.motionZ = d2 * this.getMaxSpeed();
			}

			rider.moveForward = 0F;
		}

		else {
			this.motionX *= 0.5D;
			this.motionZ *= 0.5D;
		}
	}

	public void isJump () {

		if (this.onGround && SweetMagicCore.proxy.isJumpPressed()) {
			this.motionY += 0.45D;
			this.motionX *= 1.05D;
			this.motionZ *= 1.05D;
		}
	}

	public boolean isRading () {
		return !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof EntityPlayer;
	}

	public boolean checkMove () {
		return this.motionX < 0.1D && this.motionZ < 0.1D;
	}

	protected float getMaxSpeed() {
		return 0.5F;
	}

	// ガクガク対策
	protected void tickLerp() {
		if (this.lerpSteps > 0 && !this.canPassengerSteer()) {
			double d0 = this.posX + (this.lastX - this.posX);
			double d1 = this.posY + (this.lastY - this.posY);
			double d2 = this.posZ + (this.lastZ - this.posZ);
			double d3 = MathHelper.wrapDegrees(this.lastYaw - this.rotationYaw);
			this.rotationYaw = (float) (this.rotationYaw + d3);
			this.rotationPitch = (float) (this.rotationPitch + (this.lastRot - this.rotationPitch));
			this.setPosition(d0, d1, d2);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}
	}

	@Override
	public void updatePassenger(Entity entity) {

		if (this.isPassenger(entity) && entity != this.getControllingPassenger()) {
			float f = 0.0F;
			float f1 = (float) ((this.isDead ? 0.01D : this.getMountedYOffset()) + entity.getYOffset());

			if (this.getPassengers().size() > 1) {
				int i = this.getPassengers().indexOf(entity);
				f = i == 0 ? 0.2F : -0.6F;
			}

			Vec3d vec3d = (new Vec3d(f, 0.0D, 0.0D)).rotateYaw(-this.rotationYaw * 0.017453292F - ((float) Math.PI / 2F));
			entity.setPosition(this.posX + vec3d.x, this.posY + f1, this.posZ + vec3d.z);
			entity.rotationYaw = this.rotationYaw;
			entity.setRotationYawHead(this.rotationYaw);
			this.applyYawToEntity(entity);
		}

		else {
			super.updatePassenger(entity);
		}
	}

	@Override
	@Nullable
	public Entity getControllingPassenger() {
		List<Entity> list = this.getPassengers();
		return list.isEmpty() ? null : (Entity) list.get(0);
	}

	protected void applyYawToEntity(Entity entity) {
		entity.setRenderYawOffset(this.rotationYaw);
		float f = MathHelper.wrapDegrees(entity.rotationYaw - this.rotationYaw);
		float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
		entity.prevRotationYaw += f1 - f;
		entity.rotationYaw += f1 - f;
		entity.setRotationYawHead(entity.rotationYaw);
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return 0;
	}

	@Override
	protected boolean canDropLoot() {
		return false;
	}

	@Override
	protected Item getDropItem() {
		return null;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	public class GolemMoveHelper extends EntityMoveHelper {

		public final EntityShadowGolem entity;
		public int changeCoolTime;

		public GolemMoveHelper(EntityShadowGolem ghast) {
			super(ghast);
			this.entity = ghast;
		}

		public void onUpdateMoveHelper() {
			if (!this.entity.isRading() && this.entity.checkMove()) {
				super.onUpdateMoveHelper();
			}
		}
	}
}
