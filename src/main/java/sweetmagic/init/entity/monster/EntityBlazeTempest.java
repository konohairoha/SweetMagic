package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.projectile.EntityBlazeCyclone;

public class EntityBlazeTempest extends EntityMob implements ISMMob {

    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
    private static final DataParameter<Byte> ON_FIRE = EntityDataManager.<Byte>createKey(EntityBlazeTempest.class, DataSerializers.BYTE);
    public int tickTime = 0;
    public int randTime = 0;
    public int fixedTime = 800;
    public boolean tempest = false;

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

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ON_FIRE, Byte.valueOf((byte)0));
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

			if (this.isDayElapse(this.world, 5)) { return; }
			this.randTime = this.rand.nextInt(this.fixedTime);

			// 暴風攻撃
			this.tempestGun();
		}

		if (this.world.isRemote) {

			// パーティクルスポーン
			this.spawnParticle(this, this.world, 1);
		}
		super.onLivingUpdate();
	}

	// 暴風攻撃
	public void tempestGun () {

		float dame = 3F;
		if (this.isTempest()) { dame *= 1.5F; }

		List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, this.getEntityBoundingBox().grow(10D, 6D, 10D));
		Vec3d p = new Vec3d(this.posX, this.posY, this.posZ);

		for (Entity entity : list ) {

			if (entity instanceof ISMMob || entity instanceof EntityBlazeCyclone) { continue; }

			Vec3d t = new Vec3d(entity.posX, entity.posY, entity.posZ);
			Vec3d r = new Vec3d(t.x - p.x, t.y - p.y, t.z - p.z);

			entity.motionX += r.x ;
			entity.motionY += r.y ;
			entity.motionZ += r.z ;

			if (!(entity instanceof IMob) && entity instanceof EntityLivingBase) {

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
				entity.posZ + (rand.nextDouble() - 0.5D) * entity.width, 0.0D, 0.0D, 0.0D);
		}
	}

    public boolean isTempest () {
    	return this.getMaxHealth() >= 30F;
    }

	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
			this.entityDropItem(new ItemStack(ItemInit.tiny_feather, this.rand.nextInt(2) + 1), 0.0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(5)), 0F);
		}
	}

	protected void updateAITasks() {

		--this.heightOffsetUpdateTime;

		if (this.heightOffsetUpdateTime <= 0) {
			this.heightOffsetUpdateTime = 100;
			this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * 3.0F;
		}

		EntityLivingBase entity = this.getAttackTarget();

		if (entity != null && entity.posY + (double) entity.getEyeHeight() > this.posY
				+ (double) this.getEyeHeight() + (double) this.heightOffset) {
			this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
			this.isAirBorne = true;
		}

		super.updateAITasks();
	}

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src)) {
    		return false;
		}

		// ダメージ倍処理
		amount = this.getDamageAmount(this.world , src, amount);

		return super.attackEntityFrom(src, amount);
	}

	public void fall(float distance, float damageMultiplier) {
	}

	public boolean isBurning() {
		return this.isCharged();
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_BLAZE;
	}

	public boolean isCharged() {
		return false;
	}

	// モブスポーン条件
	@Override
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.canSpawn(this.world, this, 3);
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

		public final EntityBlazeTempest blaze;
		public int attackStep;
		public int attackTime;
		public World world;
		public Random rand;

		public AIFireballAttack(EntityBlazeTempest blazeIn) {
			this.blaze = blazeIn;
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

			if (d0 < 4.0D) {
				if (this.attackTime <= 0) {
					this.attackTime = 16;
					this.blaze.attackEntityAsMob(entity);
				}

				this.blaze.getMoveHelper().setMoveTo(entity.posX, entity.posY, entity.posZ, 1.0D);
			} else if (d0 < this.getFollowDistance() * this.getFollowDistance()) {
				double d1 = entity.posX - this.blaze.posX;
				double d2 = entity.getEntityBoundingBox().minY + (double) (entity.height / 2.0F)
						- (this.blaze.posY + (double) (this.blaze.height / 2.0F));
				double d3 = entity.posZ - this.blaze.posZ;

				if (this.attackTime <= 0) {
					++this.attackStep;

					if (this.attackStep == 1) {
						this.attackTime = 40;
					} else if (this.attackStep <= 5) {
						this.attackTime = 6;
					} else {
						this.attackTime = 70;
						this.attackStep = 0;
					}

					if (this.attackStep > 1) {
						float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
						this.blaze.world.playEvent((EntityPlayer) null, 1018,
								new BlockPos((int) this.blaze.posX, (int) this.blaze.posY, (int) this.blaze.posZ), 0);

						if (this.blaze.isTempest() ) {

							EntityBlazeCyclone fireball[] = new EntityBlazeCyclone[4];
                            for (int i = 0; i < fireball.length; ++i) {

                				d1 *= rand.nextFloat() * 1.15;
                				d2 *= rand.nextFloat() * 1.15;
                				d3 *= rand.nextFloat() * 1.15;
                            	fireball[i] = new EntityBlazeCyclone(this.world, this.blaze, d1 + this.blaze.getRNG().nextGaussian() * (double)f, d2, d3 + this.blaze.getRNG().nextGaussian() * (double)f);
                            	fireball[i].posY = this.blaze.posY + (double)(this.blaze.height / 2.0F) + 0.5D;
                                this.blaze.world.spawnEntity(fireball[i]);
                            }
						} else {
							for (int i = 0; i < 1; ++i) {
								EntityBlazeCyclone cyclone = new EntityBlazeCyclone(this.world, this.blaze, d1 + this.blaze.getRNG().nextGaussian() * (double)f, d2, d3 + this.blaze.getRNG().nextGaussian() * (double)f);
	                            cyclone.posY = this.blaze.posY + (double)(this.blaze.height / 2.0F) + 0.5D;
	                            this.world.spawnEntity(cyclone);
							}
						}
					}
				}

				this.blaze.getLookHelper().setLookPositionWithEntity(entity, 10.0F, 10.0F);
			} else {
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
