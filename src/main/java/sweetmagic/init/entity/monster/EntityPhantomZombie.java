package sweetmagic.init.entity.monster;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.DimensionInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityRockBlast;

public class EntityPhantomZombie extends EntityZombie implements ISMMob {

	public int tickTime = 0;
	public int aiTickTime = 0;
	public int randTime = 0;

	public EntityPhantomZombie(World worldIn) {
		super(worldIn);
        this.experienceValue = 100;
        this.setPathPriority(PathNodeType.DAMAGE_OTHER, 0.0F);
		this.isImmuneToFire = true;
        this.moveHelper = new PhantomMoveHelper(this);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(10, new EntityAILookIdle(this));
		this.applyEntityAI();
	}

	@Override
	public void onLivingUpdate() {

		super.onLivingUpdate();

		if (!this.world.isRemote) { return; }

		this.tickTime++;
		if (this.tickTime % 16 != 0) { return; }

		this.tickTime = 0;
		this.spawnParticle();
	}

    public void fall(float dis, float dama) { }

	public void spawnParticle() {

		for (int i = 0; i < 3; i++) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
			float x = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
			float y = (this.rand.nextFloat() + this.rand.nextFloat()) * 0.0825F;
			float z = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;

			Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, f1, f2, f3, x, y, z, 48);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

	@Override
	protected void updateAITasks() {

		// ターゲットの取得していないなら終了
		EntityLivingBase entity = this.getAttackTarget();
		if (entity == null) { return; }

		// 自分とターゲットの高さが一定以下なら
		if (this.posY <= entity.posY + 1.75D) {
			if (this.motionY < 0D) { this.motionY = 0D; }
			this.motionY += (0.1D - this.motionY) * 0.25D;
		}

		double d0 = entity.posX - this.posX;
		double d1 = entity.posZ - this.posZ;
		double d3 = d0 * d0 + d1 * d1;

		// 離れすぎてたら近づく
		if (d3 > 256D) {
			double d5 = (double) MathHelper.sqrt(d3);
			this.motionX += (d0 / d5 * 0.2D - this.motionX) * 0.6000000238418579D;
			this.motionZ += (d1 / d5 * 0.2D - this.motionZ) * 0.6000000238418579D;
		}

		// 近すぎたら離れる
		else if (d3 <= 48D) {
			double move = this.isUnique() ? 0.2D : 0.1D;
			double d5 = (double) MathHelper.sqrt(d3);
			this.motionX -= (d0 / d5 * 0.1D - this.motionX) * move;
			this.motionZ -= (d1 / d5 * 0.1D - this.motionZ) * move;
		}

		// 一定距離なら適当に動く
		else {
			Random rand = this.getRNG();
			double d4 = this.posX + (double) ((rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d5 = this.posY + (double) ((rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d6 = this.posZ + (double) ((rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.getMoveHelper().setMoveTo(d4, d5, d6, 1.0D);
		}

		// 自分の向きを調整
		if (entity.getDistanceSq(this) < 4096.0D) {
			double d4 = entity.posX - this.posX;
			double d5 = entity.posZ - this.posZ;
			this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
			this.renderYawOffset = this.rotationYaw;
		}

		if (this.world.isRemote) { return; }

		this.aiTickTime++;

		// ランダム時間を取得してない９なら
		if (this.randTime <= 0) {
			this.randTime = this.rand.nextInt(50);
		}

		// 一定時間を超えたら魔法攻撃
		if (this.aiTickTime >= this.randTime + 150) {

			this.aiTickTime = 0;

			int coolTime = this.isUnique() ? 35 : 50;
			this.randTime = this.rand.nextInt(coolTime);

			EntityBaseMagicShot magic = new EntityRockBlast(this.world, this, ItemStack.EMPTY);
	        double x = entity.posX - this.posX;
	        double y = entity.getEntityBoundingBox().minY - entity.height / 2  - this.posY;
	        double z = entity.posZ - this.posZ;
	        double xz = (double)MathHelper.sqrt(x * x + z * z);
	        magic.shoot(x, y - xz * 0.015D, z, 1.75F, 0);	// 射撃速度
	        magic.setDamage(magic.getDamage() + 6);
			this.world.playSound(null, new BlockPos(this), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.67F);
	        this.world.spawnEntity(magic);
		}
	}

	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(2D);
    }

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.setHardHealth(this);
		return livingdata;
	}

	// モブスポーン条件
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.world.provider.getDimension() == DimensionInit.dimID;
	}

	// 二つ名かどうか
    public boolean isUnique () {
    	return this.getMaxHealth() >= 40F;
    }

	@Override
	public void setInWeb() {}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);

		if (!this.world.isRemote) {
			this.entityDropItem(new ItemStack(ItemInit.sugarbell, this.rand.nextInt(2) + 1), 0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(8) + 1), 0F);
		}
    }

	public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src) && !this.isMindControl(this)) {
    		return false;
		}

		// ダメージ倍処理
		amount = this.getDamageAmount(this.world , src, amount);

		return super.attackEntityFrom(src, amount);
	}

	public boolean attackEntityAsMob(Entity entity) {

		if (!super.attackEntityAsMob(entity)) { return false; }

		if (entity instanceof EntityLivingBase) {

			EntityLivingBase living = (EntityLivingBase) entity;
			living.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 100));

			if (living.isPotionActive(PotionInit.aether_barrier)) {
				PotionEffect potion = living.getActivePotionEffect(PotionInit.aether_barrier);
				int level = potion.getAmplifier();
				this.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 200, level));
			}
		}

		return true;
	}

	public class PhantomMoveHelper extends EntityMoveHelper {

		public final EntityPhantomZombie entity;
		public int changeCoolTime;

		public PhantomMoveHelper(EntityPhantomZombie ghast) {
			super(ghast);
			this.entity = ghast;
		}

		public void onUpdateMoveHelper() {

			if (this.action == EntityMoveHelper.Action.MOVE_TO) {
				double d0 = this.posX - this.entity.posX;
				double d1 = this.posY - this.entity.posY;
				double d2 = this.posZ - this.entity.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;

				if (this.changeCoolTime-- <= 0) {

					this.changeCoolTime += this.entity.getRNG().nextInt(5) + 2;
					d3 = (double) MathHelper.sqrt(d3);

					if (this.isNotColliding(this.posX, this.posY, this.posZ, d3)) {
						this.entity.motionX += d0 / d3 * 0.1D;
						this.entity.motionY += d1 / d3 * 0.1D;
						this.entity.motionZ += d2 / d3 * 0.1D;
					} else {
						this.action = EntityMoveHelper.Action.WAIT;
					}
				}
			}
		}

		public boolean isNotColliding(double x, double y, double z, double par1) {

			double d0 = (x - this.entity.posX) / par1;
			double d1 = (y - this.entity.posY) / par1;
			double d2 = (z - this.entity.posZ) / par1;
			AxisAlignedBB aabb = this.entity.getEntityBoundingBox();

			for (int i = 1; (double) i < par1; ++i) {

				aabb = aabb.offset(d0, d1, d2);

				if (!this.entity.world.getCollisionBoxes(this.entity, aabb).isEmpty()) {
					return false;
				}
			}

			return true;
		}
	}
}
