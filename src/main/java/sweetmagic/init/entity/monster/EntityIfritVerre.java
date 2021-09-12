package sweetmagic.init.entity.monster;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityExplosionMagic;
import sweetmagic.init.entity.projectile.EntityFireMagic;
import sweetmagic.init.entity.projectile.EntityFlameNova;
import sweetmagic.init.entity.projectile.EntityGravityMagic;
import sweetmagic.init.entity.projectile.EntityMeteorMagic;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.SMUtil;

public class EntityIfritVerre extends EntityMob implements IRangedAttackMob, ISMMob {

	public BlockPos tpPos = null;
    public int spellTicks = 0;
	public boolean isCharge = false;
	private int coolTime = 0;
	private int damageCoolTime = 0;
	private int tickTime = 0;
	private final BossInfoServer bossInfo = new BossInfoServer(this.getBossName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_6);

	public EntityIfritVerre(World world) {
		super(world);
		this.experienceValue = 120;
		this.setSize(0.6F, 1.75F);
	}

	public static void registerFixesWitch(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityIfritVerre.class);
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
		this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(4, new EntityAIMeteoRain(this, true));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
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
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.setHardHealth(this);
		return livingdata;
	}

	public void onLivingUpdate() {

		this.tickTime++;
		if (this.damageCoolTime > 0) { this.damageCoolTime--; }

		if (this.tickTime % 5 == 0 && this.isInLava() && this.tpPos != null) {

			if (this.attemptTeleport(this.tpPos.getX(), this.tpPos.getY(), this.tpPos.getZ())) {
				this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
			}
		}

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
				this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
				this.coolTime += 200;
				return;
			}

			// switch文で回復量を変える予定
			this.setHealth(this.getHealth() + this.getHealValue());
			this.coolTime += 200;

			// パーティクルスポーン
			ParticleHelper.spawnHeal(this, EnumParticleTypes.VILLAGER_HAPPY, 16, 1, 4);
			this.playSound(SMSoundEvent.HEAL, 0.35F, 1F);
			return;
		}

		// 体力半分以上
		else {

			switch (this.rand.nextInt(4)) {
			case 0:
				// リフレッシュ・エフェクト
				if (!this.isPotionActive(PotionInit.regene)) {
					this.addPotionEffect(new PotionEffect(PotionInit.regene, 400, 3));
					this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
					this.coolTime += 300;
				}
				return;
			case 1:
				// ルナッティクムーン
				if (!this.isPotionActive(PotionInit.shadow)) {
					this.addPotionEffect(new PotionEffect(PotionInit.shadow, 400, 4));
					this.playSound(SMSoundEvent.MAGICSTART, 1F, 1F);
					this.coolTime += 300;
				}
				return;
			case 2:
				// エーテルバリアー
				if (!this.isPotionActive(PotionInit.aether_barrier)) {
					this.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 400, 1));
					this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
					this.coolTime += 300;
				}
				return;
			case 3:
				// 攻撃力強化
				if (!this.isPotionActive(MobEffects.STRENGTH)) {
					this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 400, 1));
					this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
					this.coolTime += 300;
				}
				return;
			}
		}
	}

	public float getHealValue () {
		return this.isUnique() ? this.getMaxHealth() * 0.15F : this.getMaxHealth() / 3;
	}

	// モブスポーン条件
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.isSMDimension(this.world);
	}

	@Override
	protected void updateAITasks() {

		super.updateAITasks();
		if (!this.isUnique()) { return; }

		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
	}

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src) && !this.isMindControl(this)) {
    		return false;
		}

    	// ダメージソースチェック
    	if (this.isUnique() && this.checkDamageSrc(src)) {
    		this.heal(amount / 5);
			ParticleHelper.spawnHeal(this, EnumParticleTypes.VILLAGER_HAPPY, 16, 1, 4);
    		return false;
    	}

		// ダメージ倍処理
    	if (!this.isUnique()) {
    		amount = this.getDamageAmount(this.world , src, amount);
    	}

    	// ボス
    	else {

        	// 風魔法チェック
        	if (this.checkMagicCyclone(src)) {
        		amount *= 0.1F;
        	}

        	// 光魔法チェック
        	if (this.checkMagicLight(src)) {
        		amount *= 0.5F;
        	}

    		amount =  Math.min(amount, 15);
    	}

    	// ダメージを跳ね返す
    	if (src.getImmediateSource() instanceof EntityLivingBase) {
    		EntityLivingBase entity = (EntityLivingBase) src.getImmediateSource();
			entity.attackEntityFrom(DamageSource.MAGIC, amount / 2);
    	}

		if (!this.isSMDamage(src)) {
			amount *= this.isUnique() ? 0.25 : 0.75;
		}

		this.damageCoolTime = 400;

		return super.attackEntityFrom(src, amount);
	}

    // ダメージソースチェック
    public boolean checkDamageSrc (DamageSource src) {

    	if (src == DamageSource.IN_FIRE || src == DamageSource.ON_FIRE || src.isExplosion()) {
    		return true;
    	}

    	Entity entity = src.getImmediateSource();
    	return entity instanceof EntityFireMagic || entity instanceof EntityExplosionMagic;
    }

    // 死亡時
	public void onDeath(DamageSource cause) {

		if (!this.world.isRemote && !this.isDethCancel(this)) {

			this.entityDropItem(new ItemStack(ItemInit.mysterious_page, this.rand.nextInt(2) + 1), 0.0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal, this.rand.nextInt(7) + 1), 0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(16)), 0F);
			this.entityDropItem(new ItemStack(ItemInit.blank_magic, this.rand.nextInt(2)), 0F);
			this.entityDropItem(new ItemStack(ItemInit.blank_page, this.rand.nextInt(4) + 1), 0F);
			this.entityDropItem(new ItemStack(ItemInit.witch_tears, this.rand.nextInt(2) + 1), 0F);
			this.entityDropItem(new ItemStack(ItemInit.b_mf_bottle, this.rand.nextInt(2) + 1), 0F);

			if (this.isUnique()) {
				this.dropItem(this.world, this, ItemInit.cosmic_crystal_shard, this.rand.nextInt(8) + 4);
				this.dropItem(this.world, this, ItemInit.mf_magiabottle, 1);
				this.dropItem(this.world, this, ItemInit.b_mf_magiabottle, this.rand.nextInt(8) + 1);

				if (this.rand.nextBoolean()) {
					this.dropItem(this.world, this, ItemInit.scorching_jewel, 1);
				}
			}

			else if (this.rand.nextFloat() <= 0.01F) {
				this.entityDropItem(new ItemStack(ItemInit.scorching_jewel, 1), 0F);
			}
		}

		super.onDeath(cause);
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_WITCH;
	}

	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {

		EntityBaseMagicShot entity = this.getShot();
		if (entity == null) { return; }

        double x = target.posX - this.posX;
        double y = target.getEntityBoundingBox().minY - target.height / 2  - this.posY;
        double z = target.posZ - this.posZ;
        double xz = (double)MathHelper.sqrt(x * x + z * z);
		entity.shoot(x, y - xz * 0.015D, z, this.isUnique() ? 3F : 2F, 0);	// 射撃速度

		int dame = this.isUnique() ? 8 : 4;
		entity.setDamage(entity.getDamage() + dame);
		this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
        this.world.spawnEntity(entity);
	}

	public EntityBaseMagicShot getShot () {

		EntityBaseMagicShot entity = null;
		int rand = this.rand.nextInt(this.isUnique() ? 5 : 4);

		switch (rand) {
		case 0:
			entity = new EntityFlameNova(this.world, this, ItemStack.EMPTY);
			break;
		case 1:
			entity = new EntityFireMagic(this.world, this, ItemStack.EMPTY);
			break;
		case 2:
			entity = new EntityExplosionMagic(this.world, this, ItemStack.EMPTY, 1);
			break;
		case 3:
			entity = new EntityGravityMagic(this.world, this, ItemStack.EMPTY);
			entity.range = 6D;
			entity.isHitDead = true;
			break;
		case 4:
			EntityBlaze blaze = new EntityBlaze(this.world);
			blaze.setHealth(40F);
			blaze.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 400, 3, true, false));

			double xRand = this.posX + (this.rand.nextDouble() - this.rand.nextDouble() - 0.5D) * 8D;
			double zRand = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble() - 0.5D) * 8D;
			blaze.setLocationAndAngles(xRand, this.posY, zRand, this.rotationYaw, 0.0F);
			this.world.spawnEntity(blaze);

			// タゲをnullに書き換え
			SMUtil.tameAIAnger((EntityLiving) blaze, this.getAttackTarget());
			this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
			break;
		}

		return entity;
	}

	public float getEyeHeight() {
		return 1.62F;
	}

	// 二つ名かどうか
    public boolean isUnique () {
    	return this.getMaxHealth() >= 256F;
    }

	// 体力半分かどうか
	public boolean isHalfHelth () {
		return this.getHealth() < this.getMaxHealth() / 2;
	}

	@Override
	protected boolean canDespawn() {
		return !this.isUnique();
	}

	@Override
	public void setInWeb() { }

	@Override
	public boolean isNonBoss() {
		return !this.isUnique();
	}

	public void setSwingingArms(boolean swingingArms) { }

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		if (this.isUnique()) {
			this.bossInfo.addPlayer(player);
		}
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		if (this.isUnique()) {
			this.bossInfo.removePlayer(player);
		}
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getBossName());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {
		super.readEntityFromNBT(tags);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getBossName());
		}
	}

	public ITextComponent getBossName () {
		return new TextComponentTranslation("entity.spirit_fire_ifrite.name", new Object[0]);
	}

	public class EntityAIMeteoRain extends EntityAIBase {

		protected int spellWarmup;
		protected int spellCooldown;
		public World world;
		public EntityIfritVerre ifrite;
		public final boolean isStop;

		public EntityAIMeteoRain (EntityIfritVerre entity, boolean isStop) {
			this.ifrite = entity;
			this.world = this.ifrite.world;
			this.isStop = isStop;
		}

		// AIを実行できるか
		public boolean shouldExecute() {
			if (this.getTarget() == null) {
				return false;
			} else {
				return this.ifrite.ticksExisted >= this.spellCooldown && this.ifrite.isHalfHelth() && this.ifrite.isUnique();
			}
		}

		// 実行できるか
		public boolean shouldContinueExecuting() {
			return this.ifrite.getAttackTarget() != null && this.spellWarmup > 0;
		}

		public void startExecuting() {
			this.spellWarmup = this.getCastWarmupTime();
			this.ifrite.spellTicks = this.getCastingTime();
			this.spellCooldown = this.ifrite.ticksExisted + this.getCastingInterval();
			this.setCharge(true);
		}

		// タスク処理
		public void updateTask() {

			--this.spellWarmup;
			Random rand = world.rand;
			int range = 16;
			EntityLivingBase target = this.ifrite.getAttackTarget();
			if (target == null) { return; }

			double x = (rand.nextDouble() * range) - rand.nextDouble() * range;
			double y = (rand.nextDouble() * range) - rand.nextDouble() * range;
			double z = (rand.nextDouble() * range) - rand.nextDouble() * range;
	        BlockPos pos = new BlockPos(target.posX + x, target.posY + y, target.posZ + z);
	        int posY = this.world.canSeeSky(pos) ? 20 : 7;

			if (!this.world.isRemote) {
				EntityMeteorMagic entity = new EntityMeteorMagic(this.world, this.ifrite, ItemStack.EMPTY);
				entity.shoot(0, entity.motionY - 0.33F, 0, 0, 0);
				entity.motionY -= 1;
				entity.setPosition(pos.getX(), pos.getY() + posY, pos.getZ());
				entity.setDamage(entity.getDamage() + 6);
				this.world.spawnEntity(entity);
			}

			this.ifrite.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.33F, 0.67F);

			if (this.spellWarmup == 0) {
				this.castSpell();
				this.setCharge(false);
			}
		}

		// 特殊行動開始
		protected void castSpell() {
		}

		// ウォームアップタイム
		protected int getCastWarmupTime() {
			return 180;
		}

		// キャストタイム
		protected int getCastingTime() {
			return this.spellCooldown;
		}

		// インターバル
		protected int getCastingInterval() {
			return 700;
		}

		// ターゲット取得
		public EntityLivingBase getTarget () {
			return this.ifrite.getAttackTarget();
		}

		// チャージの設定
		public void setCharge (boolean charge) {
			if (this.isStop) {
				this.ifrite.isCharge = charge;
			}
		}
	}
}
