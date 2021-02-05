package sweetmagic.init.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityCyclonMagic;
import sweetmagic.init.entity.projectile.EntityDigMagic;
import sweetmagic.init.entity.projectile.EntityFireMagic;
import sweetmagic.init.entity.projectile.EntityFrostMagic;
import sweetmagic.init.entity.projectile.EntityGravityMagic;
import sweetmagic.util.ParticleHelper;

public class EntityWitchMadameVerre extends EntityMob implements IRangedAttackMob, ISMMob {

	private int coolTime = 0;
	private int damageCoolTime = 0;
	private int tickTime = 0;

	public EntityWitchMadameVerre(World world) {
		super(world);
		this.experienceValue = 120;
		this.setSize(0.4F, 1.4F);
	}

	public static void registerFixesWitch(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityWitchMadameVerre.class);
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
		this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	protected void entityInit() {
		super.entityInit();
	}

	@Override
	protected float getSoundPitch() {
		return 1.125F;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_WITCH_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource dame) {
		return SoundEvents.ENTITY_WITCH_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WITCH_DEATH;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
	}

	public void onLivingUpdate() {

		this.tickTime++;
		if (this.damageCoolTime > 0) { this.damageCoolTime--; }

		// 5秒経てば
		if (this.tickTime % 80 == 0) {

			this.tickTime = 0;

			// 体力最大以外かつ最後にダメージを受けたのが一定時間たてば
			if (this.getMaxHealth() > this.getHealth() && this.damageCoolTime == 0) {
				this.setHealth(this.getHealth() + 2);
			}
		}

		this.addPotion();
		super.onLivingUpdate();
	}

	public void addPotion () {

		if (this.world.isRemote || this.getAttackTarget() == null) { return; }

		if (this.coolTime > 0) {
			this.coolTime--;
		}

		// クールタイムが終わってないなら
		if (this.coolTime > 0) { return; }

		// 体力半分以下なら
		if (this.getHealth() <= this.getMaxHealth() / 2) {

			// エーテルバリアー
			if (!this.isPotionActive(PotionInit.aether_barrier)) {
				this.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 400, 2));
				this.world.playSound(null, new BlockPos(this), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.NEUTRAL, 1F, 1.175F);
				this.coolTime += 200;
				return;
			}

			// switch文で回復量を変える予定
			this.setHealth(this.getHealth() + (this.getMaxHealth() / 2));
			this.coolTime += 200;

			// パーティクルスポーン
			ParticleHelper.spawnHeal(this, EnumParticleTypes.VILLAGER_HAPPY, 16, 1, 4);
			this.world.playSound(null, new BlockPos(this), SMSoundEvent.HEAL, SoundCategory.NEUTRAL, 0.175F, 1F);
			return;
		}

		// 体力半分以上
		else {

			switch (this.rand.nextInt(4)) {
			case 0:
				// リフレッシュ・エフェクト
				if (!this.isPotionActive(PotionInit.refresh_effect)) {
					this.addPotionEffect(new PotionEffect(PotionInit.electric_armor, 400, 1));
					this.world.playSound(null, new BlockPos(this), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.NEUTRAL, 1F, 1.175F);
					this.coolTime += 300;
				}
				return;
			case 1:
				// 毒エンチャ
				if (!this.isPotionActive(PotionInit.grant_poison)) {
					this.addPotionEffect(new PotionEffect(PotionInit.grant_poison, 400, 1));
					this.world.playSound(null, new BlockPos(this), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.NEUTRAL, 1F, 1.175F);
					this.coolTime += 300;
				}
				return;
			case 2:
				// エーテルバリアー
				if (!this.isPotionActive(PotionInit.aether_barrier)) {
					this.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 400, 1));
					this.world.playSound(null, new BlockPos(this), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.NEUTRAL, 1F, 1.175F);
					this.coolTime += 300;
				}
				return;
			case 3:
				// 攻撃力強化
				if (!this.isPotionActive(MobEffects.STRENGTH)) {
					this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 400, 1));
					this.world.playSound(null, new BlockPos(this), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.NEUTRAL, 1F, 1.175F);
					this.coolTime += 300;
				}
				return;
			}
		}
	}

	// モブスポーン条件
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.canSpawn(this.world, this, 15);
	}

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 15) {
			for (int i = 0; i < this.rand.nextInt(35) + 10; ++i) {
				this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH,
						this.posX + this.rand.nextGaussian() * 0.12999999523162842D,
						this.getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D,
						this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src)) { return false; }

		// ダメージ倍処理
		amount = this.getDamageAmount(this.world , src, amount);

		if (!this.isSMDamage(src)) {
			amount *= 0.75;
		}

		this.damageCoolTime = 400;

		return super.attackEntityFrom(src, amount);
	}

	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
			this.entityDropItem(new ItemStack(ItemInit.mysterious_page, this.rand.nextInt(2) + 1), 0.0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal, this.rand.nextInt(7) + 1), 0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(16)), 0F);
			this.entityDropItem(new ItemStack(ItemInit.blank_magic, this.rand.nextInt(2)), 0F);
			this.entityDropItem(new ItemStack(ItemInit.blank_page, this.rand.nextInt(4) + 1), 0F);
			this.entityDropItem(new ItemStack(ItemInit.witch_tears, this.rand.nextInt(2) + 1), 0F);
		}
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_WITCH;
	}

	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		EntityBaseMagicShot entity = this.getShot();
        double x = target.posX - this.posX;
        double y = target.getEntityBoundingBox().minY - target.height / 2  - this.posY;
        double z = target.posZ - this.posZ;
        double xz = (double)MathHelper.sqrt(x * x + z * z);
		entity.shoot(x, y - xz * 0.015D, z, 2F, 0);	// 射撃速度

		int dame = this.isUnique() ? 7 : 4;
		entity.setDamage(entity.getDamage() + dame);
		this.world.playSound(null, new BlockPos(this), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.67F);
        this.world.spawnEntity(entity);
	}

	public EntityBaseMagicShot getShot () {

		EntityBaseMagicShot entity = null;
		int rand = this.rand.nextInt(5);

		switch (rand) {
		case 0:
			entity = new EntityFireMagic(this.world, this, ItemStack.EMPTY);
			break;
		case 1:
			entity = new EntityFrostMagic(this.world, this, ItemStack.EMPTY, false);
			break;
		case 2:
			entity = new EntityCyclonMagic(this.world, this, ItemStack.EMPTY);
			break;
		case 3:
			entity = new EntityGravityMagic(this.world, this, ItemStack.EMPTY);
			break;
		case 4:
			entity = new EntityDigMagic(this.world, this, ItemStack.EMPTY, 0);
			break;
		}

		return entity;
	}

	public float getEyeHeight() {
		return 1.62F;
	}

	// 二つ名かどうか
    public boolean isUnique () {
    	return this.getMaxHealth() >= 40F;
    }

	public void setSwingingArms(boolean swingingArms) {
	}
}
