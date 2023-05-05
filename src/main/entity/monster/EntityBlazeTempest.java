package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.config.SMConfig;
import sweetmagic.init.LootTableInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBlazeCyclone;

public class EntityBlazeTempest extends EntityMob implements ISMMob {

    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
    private int tickTime = 0;
    private int randTime = 0;
    private int fixedTime = 800;
    private boolean tempest = false;

	public EntityBlazeTempest(World world) {
		super(world);
        this.setPathPriority(PathNodeType.WATER, 8.0F);
        this.setPathPriority(PathNodeType.LAVA, 8.0F);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
		this.isImmuneToFire = true;
		this.experienceValue = 100;
	}

	public static void registerFixesBlaze(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityBlazeTempest.class);
	}

	protected void initEntityAI() {
		this.tasks.addTask(4, new EntityBlazeTempest.AIFireballAttack(this));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.225D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance dif, @Nullable IEntityLivingData entity) {
		this.setHardHealth(this);
		return super.onInitialSpawn(dif, entity);
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_BLAZE_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_BLAZE_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_BLAZE_DEATH;
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender() {
		return 15728880;
	}

	public float getBrightness() {
		return 1.0F;
	}

	public void onLivingUpdate() {

		if (!this.onGround && this.motionY < 0.0D) {
			this.motionY *= 0.6D;
		}

		this.tickTime++;

		if (this.randTime <= 0) {
			this.randTime = this.rand.nextInt(this.fixedTime);
		}

		if (this.tickTime >= (400 + this.randTime)) {

			this.tickTime = 0;

			if (!this.isDayElapse(this.world, 10) && this.getStrength() != 0) { return; }
			this.randTime = this.rand.nextInt(this.fixedTime);

			// 暴風攻撃
			this.tempestGun();
		}

		// パーティクルスポーン
		if (this.world.isRemote && this.isRender()) {
			this.spawnParticle(this, this.world, 1);
		}

		super.onLivingUpdate();
	}

	// 暴風攻撃
	public void tempestGun () {

		float dame = 3F;
		if (this.isTempest()) { dame *= 1.5F; }

		Vec3d p = new Vec3d(this.posX, this.posY, this.posZ);
		List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, this, 7.5D, 4.5D, 7.5D);

		for (EntityLivingBase entity : list) {

			// 吹き飛ばし耐性が付いていたら飛ばさない
			if (entity instanceof ISMMob || entity.isPotionActive(PotionInit.resistance_blow)) { continue; }

			Vec3d t = new Vec3d(entity.posX, entity.posY, entity.posZ);
			Vec3d r = new Vec3d(t.x - p.x, t.y - p.y, t.z - p.z);

			entity.motionX += r.x ;
			entity.motionY += r.y ;
			entity.motionZ += r.z ;

			if (!(entity instanceof IMob)) {

				entity.attackEntityFrom(DamageSource.MAGIC, dame);
				entity.hurtResistantTime = 0;

				// パーティクルスポーン
				if (this.world.isRemote) {
					this.spawnParticle((EntityLivingBase)entity, this.world, 8);
				}
			}
		}
	}

	public void spawnParticle (EntityLivingBase entity, World world, int value) {

		Random rand = world.rand;

		for (int i = 0; i < value; ++i) {
			world.spawnParticle(EnumParticleTypes.SWEEP_ATTACK,
				entity.posX + (rand.nextDouble() - 0.5D) * entity.width,
				entity.posY + rand.nextDouble() * entity.height,
				entity.posZ + (rand.nextDouble() - 0.5D) * entity.width, 0D, 0D, 0D);
		}
	}

    public boolean isTempest () {
    	return this.getMaxHealth() >= 30F;
    }


	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableInit.BLAZETEMPEST;
	}

	protected void updateAITasks() {

		--this.heightOffsetUpdateTime;

		if (this.heightOffsetUpdateTime <= 0) {
			this.heightOffsetUpdateTime = 100;
			this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * 3F;
		}

		EntityLivingBase entity = this.getAttackTarget();

		if (entity != null && entity.posY + (double) entity.getEyeHeight() > this.posY + (double) this.getEyeHeight() + (double) this.heightOffset) {
			this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
			this.isAirBorne = true;
		}

		super.updateAITasks();
	}

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src) && !this.isMindControl(this)) { return false; }

		// ダメージ倍処理
		amount = this.getDamageAmount(this.world , src, amount);
		return super.attackEntityFrom(src, amount);
	}

	public void fall(float dis, float damage) { }

	public boolean isBurning() {
		return this.isCharged();
	}

	public boolean isCharged() {
		return false;
	}

	// モブスポーン条件
	@Override
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.canSpawn(this.world, this, SMConfig.spawnDay);
	}

	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);
		if (this.isTempest()) { tags.setBoolean("tempest", this.isTempest()); }
	}

	public void readEntityFromNBT(NBTTagCompound tags) {
		super.readEntityFromNBT(tags);
		if (tags.getBoolean("tempest")) { this.tempest = tags.getBoolean("tempest"); }
	}

	public class AIFireballAttack extends EntityAIBase {

		private final EntityBlazeTempest blaze;
		private int attackStep;
		private int attackTime;
		private World world;
		private Random rand;

		public AIFireballAttack(EntityBlazeTempest blaze) {
			this.blaze = blaze;
			this.world = this.blaze.world;
			this.rand = this.world.rand;
			this.setMutexBits(3);
		}

		public boolean shouldExecute() {
			EntityLivingBase living = this.blaze.getAttackTarget();
			return living != null && living.isEntityAlive();
		}

		public void startExecuting() {
			this.attackStep = 0;
		}

		public void resetTask() { }

		public void updateTask() {

			--this.attackTime;
			EntityLivingBase entity = this.blaze.getAttackTarget();
			double d0 = this.blaze.getDistanceSq(entity);

			if (d0 < 4D) {
				if (this.attackTime <= 0) {
					this.attackTime = 16;
					this.blaze.attackEntityAsMob(entity);
				}

				this.blaze.getMoveHelper().setMoveTo(entity.posX, entity.posY, entity.posZ, 1D);
			}

			else if (d0 < this.getFollowDistance() * this.getFollowDistance()) {

				double d1 = entity.posX - this.blaze.posX;
				double d2 = entity.getEntityBoundingBox().minY + (double) (entity.height / 2F) - (this.blaze.posY + (double) (this.blaze.height / 2F));
				double d3 = entity.posZ - this.blaze.posZ;

				if (this.attackTime <= 0) {

					++this.attackStep;

					if (this.attackStep == 1) {
						this.attackTime = 40;
					}

					else if (this.attackStep <= 5) {
						this.attackTime = 6;
					}

					else {
						this.attackTime = 70;
						this.attackStep = 0;
					}

					if (this.attackStep > 1) {

						float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
						this.world.playEvent(null, 1018, this.blaze.getPosition(), 0);

						if (this.blaze.isTempest() ) {

							EntityBlazeCyclone fireball[] = new EntityBlazeCyclone[4];
                            for (int i = 0; i < fireball.length; ++i) {

                				d1 *= this.rand.nextFloat() * 1.15F;
                				d2 *= this.rand.nextFloat() * 1.15F;
                				d3 *= this.rand.nextFloat() * 1.15F;
                            	fireball[i] = new EntityBlazeCyclone(this.world, this.blaze, d1 + this.blaze.getRNG().nextGaussian() * (double)f, d2, d3 + this.blaze.getRNG().nextGaussian() * (double) f);
                            	fireball[i].posY = this.blaze.posY + (double)(this.blaze.height / 2.0F) + 0.5D;
                                this.blaze.world.spawnEntity(fireball[i]);
                            }
						}

						else {
							for (int i = 0; i < 1; ++i) {
								EntityBlazeCyclone cyclone = new EntityBlazeCyclone(this.world, this.blaze, d1 + this.blaze.getRNG().nextGaussian() * (double)f, d2, d3 + this.blaze.getRNG().nextGaussian() * (double) f);
	                            cyclone.posY = this.blaze.posY + (double)(this.blaze.height / 2.0F) + 0.5D;
	                            this.world.spawnEntity(cyclone);
							}
						}
					}
				}

				this.blaze.getLookHelper().setLookPositionWithEntity(entity, 10.0F, 10.0F);
			}

			else {
				this.blaze.getNavigator().clearPath();
				this.blaze.getMoveHelper().setMoveTo(entity.posX, entity.posY, entity.posZ, 1.0D);
			}

			super.updateTask();
		}

		public double getFollowDistance() {
			IAttributeInstance range = this.blaze.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
			return range == null ? 16.0D : range.getAttributeValue();
		}
	}
}
