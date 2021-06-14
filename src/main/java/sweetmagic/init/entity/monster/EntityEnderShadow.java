package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import sweetmagic.init.ItemInit;

public class EntityEnderShadow extends EntityZombie implements ISMMob {

	public boolean isShadow = false;
	public boolean canSpawnShadow = true;
	private EntityLiving owner;

	public EntityEnderShadow(World world) {
		super(world);
        this.experienceValue = 80;
        this.setSize(0.6F, 2.9F);
        this.stepHeight = 2.0F;
	}

	public float getEyeHeight() {
		return 2.65F;
	}

	// えーあい
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(10, new EntityAILookIdle(this));
        this.targetTasks.addTask(7, new EntityEnderShadow.AICopyOwnerTarget(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
	}

	// アップデート処理
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!this.world.isRemote) { return; }

		for (int i = 0; i < 2; ++i) {
			double f1 = this.posX + (this.rand.nextDouble() - 0.5D) * this.width;
			double f2 = this.posY + this.rand.nextDouble() * this.height - 0.25D;
			double f3 = this.posZ + (this.rand.nextDouble() - 0.5D) * this.width;
			double f4 = (this.rand.nextDouble() - 0.5D) * 2D;
			double f6 = (this.rand.nextDouble() - 0.5D) * 2D;
			this.world.spawnParticle(EnumParticleTypes.PORTAL, f1, f2, f3, f4, - this.rand.nextDouble(), f6);
		}
	}

	// 初期ステータス
	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.225D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
    }

	// スポーンした時にデフォルトで持たせる情報
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    	this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
    	this.setChild(false);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.setHardHealth(this);
		return livingdata;
	}

	// 死んだとき
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		List<EntityEnderShadow> list = this.world.getEntitiesWithinAABB(EntityEnderShadow.class, this.getEntityBoundingBox().grow(100D, 32D, 100D));

		for (EntityEnderShadow ender : list) {
			if (ender.getOwner() != null && ender.getOwner() == this) {
				ender.setDead();
			}
		}

		if (!this.world.isRemote) {
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(5)), 0F);
			this.entityDropItem(new ItemStack(ItemInit.ender_shard, this.rand.nextInt(3) + 1), 0F);
			this.entityDropItem(new ItemStack(ItemInit.stray_soul, this.rand.nextInt(3) + 1), 0F);
		}
    }

	// 攻撃されたとき
	public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src) && !this.isMindControl(this)) {
    		return false;
		}

		if (this.isShadow) {

			if (this.isSMDamage(src)) {
				this.setHealth(0);
				this.playSound(this.getDeathSound(), 1F, 1F);
				return true;
			}
			return false;
		}

		// ダメージ倍処理
		if (this.isSMDamage(src)) {

			// ダメージ倍処理
			amount = this.getDamageAmount(this.world , src, amount);
			return super.attackEntityFrom(src, amount);
		}

		if (this.isEntityInvulnerable(src)) {
			return false;

		} else if (src instanceof EntityDamageSourceIndirect) {

			for (int i = 0; i < 64; ++i) {
				if (this.teleportRandomly(this.rand)) {
					this.spawnShadow();
					return true;
				}
			}
			return false;


		} else {

			boolean flag = super.attackEntityFrom(src, amount);

			if (src.isUnblockable() && this.rand.nextInt(10) != 0) {
				this.teleportRandomly(this.rand);
			}

			if (flag) {
				this.spawnShadow();
			}

			return flag;
		}
	}

	// 影をスポーン
	public void spawnShadow () {

		if (!this.world.isRemote && this.canSpawnShadow && this.rand.nextDouble() < 0.33D && this.isSMDimension(this.world)) {

			for (int i = 0; i < 2; i++) {

				Random rand = new Random();
				double xRand = this.posX + (rand.nextDouble() - 0.5) * 20.0;
				double zRand = this.posZ + (rand.nextDouble() - 0.5) * 20.0;

				EntityEnderShadow ender = new EntityEnderShadow(world);
				ender.setLocationAndAngles(xRand, this.posY, zRand, this.rotationYaw, 0.0F);
				this.world.spawnEntity(ender);
				this.teleportRandomly(rand);
				ender.isShadow = true;
				ender.setOwner(this);
				ender.setHealth(this.getHealth());
				ender.canSpawnShadow = false;
				ender.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.WOODEN_SWORD));
				ender.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.0D);
				ender.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
			}

			this.teleportRandomly(this.rand);
			this.canSpawnShadow = false;
		}
	}

	private boolean teleportRandomly(Random rand) {
		double targetX = this.posX + (rand.nextDouble() - 0.5) * 20.0;
		double targetY = this.posY + (double) (rand.nextInt(12) - 4);
		double targetZ = this.posZ + (rand.nextDouble() - 0.5) * 20.0;
		this.spawnParticle();
		return teleportTo(targetX, targetY, targetZ);
	}

	private boolean teleportTo(double x, double y, double z) {
		EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0);
		if (MinecraftForge.EVENT_BUS.post(event)) return false;
		boolean success = attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());
		if (success) {
			this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
		}
		return success;
	}

	public void spawnParticle() {
		for (int i = 0; i < 8; i++) {
			float f1 = (float) this.posX - 0.5F + this.rand.nextFloat();
			float f2 = (float) ((float) this.posY + 0.25F + this.rand.nextFloat() * 1.5);
			float f3 = (float) this.posZ - 0.5F + this.rand.nextFloat();
			this.world.spawnParticle(EnumParticleTypes.PORTAL, f1, f2, f3, 0, 0, 0);
		}
	}

	// モブスポーン条件
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.canSpawn(this.world, this, 3);
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ENDERMEN_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ENDERMEN_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ENDERMEN_DEATH;
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_ENDERMAN;
	}

	public EntityLiving getOwner() {
		return this.owner;
	}

	public void setOwner(EntityLiving owner) {
		this.owner = owner;
	}

    public void readEntityFromNBT(NBTTagCompound tags) {
        super.readEntityFromNBT(tags);
		this.isShadow = tags.getBoolean("isShaodw");
		this.canSpawnShadow = tags.getBoolean("canSpawnShadow");
    }

    public void writeEntityToNBT(NBTTagCompound tags) {
        super.writeEntityToNBT(tags);
        tags.setBoolean("isShaodw", this.isShadow);
        tags.setBoolean("canSpawnShadow", this.canSpawnShadow);
    }

	public class AICopyOwnerTarget extends EntityAITarget {

		EntityEnderShadow entity = EntityEnderShadow.this;
		EntityLiving owner = this.entity.owner;

		public AICopyOwnerTarget(EntityCreature creature) {
			super(creature, false);
		}

		public boolean shouldExecute() {
			return this.owner != null && this.owner.getAttackTarget() != null && this.isSuitableTarget(this.owner.getAttackTarget(), false);
		}

		public void startExecuting() {
			this.entity.setAttackTarget(this.owner.getAttackTarget());
			super.startExecuting();
		}
	}
}
