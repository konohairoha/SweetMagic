package sweetmagic.init.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.config.SMConfig;
import sweetmagic.init.LootTableInit;
import sweetmagic.init.PotionInit;
import sweetmagic.util.WorldHelper;

public class EntityCreeperCal extends EntityCreeper implements ISMMob {

	private int lastActiveTime;
	private int timeSinceIgnited;
	private int fuseTime = 25; // 爆発までの時間
	private static final DataParameter<Boolean> POWERED = EntityDataManager.<Boolean> createKey(EntityCreeperCal.class, DataSerializers.BOOLEAN);

	public EntityCreeperCal(World world) {
		super(world);
        this.experienceValue = 80;
	}

	// えんちちーのステータス設定
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAICreeperSwell(this));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 12.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
	}

	public void onUpdate() {
		if (this.isEntityAlive()) {

			this.lastActiveTime = this.timeSinceIgnited;
			int i = this.getCreeperState();

			if (this.hasIgnited()) {
				this.setCreeperState(1);
			}

			if (i > 0 && this.timeSinceIgnited == 0) {
				this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
			}

			this.timeSinceIgnited += i;

			if (this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0;
			}

			else if (this.timeSinceIgnited > 0) {

				double range = this.getStrength() == 0 ? 0.9D : 1.5D;
				WorldHelper.suctionPlayer(world, this.getEntityBoundingBox().grow(range), this.posX, this.posY, this.posZ, this, 0.0775D);

				if (this.world.isRemote && this.isRender()) {
					for(int k = 0; k <= (this.timeSinceIgnited / 4); k++) {

						float randX = (this.rand.nextFloat() - this.rand.nextFloat()) * 2.5F;
						float randY = (this.rand.nextFloat() - this.rand.nextFloat()) * 1.5F;
						float randZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 2.5F;

						float x = (float) (this.posX - 0.5F + randX);
						float y = (float) (this.posY + 0.25F + randY);
						float z = (float) (this.posZ - 0.5F + randZ);
						float xSpeed = -randX * 0.075F;
						float ySpeed = -randY * 0.075F;
						float zSpeed = -randZ * 0.075F;

						Particle effect = ParticleNomal.create(this.world, x, y, z, xSpeed, ySpeed, zSpeed);
						this.getParticle().addEffect(effect);
					}
				}
			}

			if (this.timeSinceIgnited >= this.fuseTime) {
				this.timeSinceIgnited = this.fuseTime;
				this.explodeCal();
			}

			this.getAmbientSound();
		}
		super.onUpdate();
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance dif, @Nullable IEntityLivingData livingdata) {
		this.setHardHealth(this);
		return livingdata;
	}

	// モブスポーン条件
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.canSpawn(this.world, this, SMConfig.spawnDay);
	}

	public void explodeCal() {
		if (!this.world.isRemote) {

			boolean isEasy = this.getStrength() == 0;

			if (!isEasy) {
				EntityAreaEffectCloud entity = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
				entity.setRadiusOnUse(-0.5F);
				entity.setWaitTime(10);
		        entity.setParticle(EnumParticleTypes.EXPLOSION_NORMAL);
		        entity.setRadius(3);
				entity.setDuration(100);
				entity.setRadiusPerTick((7F - entity.getRadius()) / (float) entity.getDuration());
		        entity.addEffect(new PotionEffect(PotionInit.gravity, 201, 0));
		        this.world.spawnEntity(entity);
			}

	        float explo = isEasy ? 1F : 1.5F;
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? explo * 3F : explo, false);
			this.setDead();
			this.dead = true;
		}
	}

	public void setPower (boolean flag) {
		this.dataManager.register(POWERED, Boolean.valueOf(flag));
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableInit.CREEPERCALAMITY;
	}

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src) && !this.isMindControl(this)) {
    		return false;
		}

		// ダメージ倍処理
		amount = this.getDamageAmount(this.world , src, amount);

		if (this.getPowered() && !this.isSMDamage(src)) {
			amount *= 0.25;
		}

		super.attackEntityFrom(src, amount);
		return true;
	}
}
