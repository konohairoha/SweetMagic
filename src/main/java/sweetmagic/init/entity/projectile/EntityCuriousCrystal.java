package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.SMDamage;

public class EntityCuriousCrystal extends Entity {

	public EntityCuriousCrystal(World world) {
		super(world);
		this.preventEntitySpawning = true;
		this.setSize(1.25F, 1F);
	}

	public EntityCuriousCrystal(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() { }

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		this.setDead();
		return true;
	}

	public void setDead() {

		this.isDead = true;
		List<Entity> list = this.getEntityList(5D, 5D, 5D);

		for (Entity entity : list) {

			if (!entity.isEntityAlive() || entity instanceof IMob || (!(entity instanceof EntityPlayer) && !(entity instanceof EntityCuriousCrystal) )) { continue; }

			entity.attackEntityFrom(SMDamage.exploDamage, 4F);
			entity.hurtResistantTime = 0;
		}

		this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 3F, 1F / (this.rand.nextFloat() * 0.2F + 0.9F));
		ParticleHelper.spawnParticle(this.world, new BlockPos(this.posX, this.posY + 0.5, this.posZ), EnumParticleTypes.EXPLOSION_HUGE);
	}

	// プレイヤーリスト取得
	public List<Entity> getEntityList (double x, double  y, double  z) {
		return this.world.getEntitiesWithinAABB(Entity.class, this.getAABB(x, y, z));
	}

	// 範囲の取得
	public AxisAlignedBB getAABB (double x, double  y, double  z) {
		return this.getEntityBoundingBox().grow(x, y, z);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) { }
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) { }
}
