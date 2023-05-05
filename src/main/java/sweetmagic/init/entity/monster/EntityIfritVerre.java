package sweetmagic.init.entity.monster;

import java.util.Calendar;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.LootTableInit;
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
	private static final DataParameter<Boolean> ISSPECIAL = EntityDataManager.<Boolean>createKey(EntityIfritVerre.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> ISALLY = EntityDataManager.<Boolean>createKey(EntityIfritVerre.class, DataSerializers.BOOLEAN);
	private final BossInfoServer bossInfo = new BossInfoServer(this.getBossName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_6);
	private static final ItemStack WAND = new ItemStack(ItemInit.deuscrystal_wand_r);

	public EntityIfritVerre(World world) {
		super(world);
		this.experienceValue = 120;
		this.isImmuneToFire = true;
		this.setSize(0.6F, 1.75F);
	}

	public static void registerFixesWitch(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityIfritVerre.class);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ISSPECIAL, Boolean.valueOf(false));
		this.dataManager.register(ISALLY, Boolean.valueOf(false));
	}

	public void travel(float strafe, float vertical, float forward) {
		if (!this.dataManager.get(this.ISSPECIAL)) {
			super.travel(strafe, vertical, forward);
		}
	}

	public boolean getSpecial () {
		return this.dataManager.get(ISSPECIAL);
	}

	public void setSpecial (boolean isSpecial) {
		this.dataManager.set(ISSPECIAL, isSpecial);
	}

	public boolean getAlly () {
		return this.dataManager.get(ISALLY);
	}

	public void setAlly (boolean isAlly) {
		this.dataManager.set(ISALLY, isAlly);
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, this.isUnique() ? 7.5F : 10F));
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

		if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
			Calendar calendar = this.world.getCurrentDate();

			if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
				this.setItemStackToSlot(EntityEquipmentSlot.HEAD,
						new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
				this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
			}
		}

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
		if (!this.isUnique() || this.getAlly()) { return; }

		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
	}

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src) && !this.isMindControl(this)) {
    		return false;
		}

    	Entity srcEntity = src.getImmediateSource();

    	if (this.getAlly() && srcEntity instanceof EntityPlayer) {
    		return false;
    	}

    	boolean isUnique = this.isUnique() && !this.getAlly();

    	// ボスかつ魔法ダメージ以外なら反射
		if (isUnique && !this.isSMDamage(src) && srcEntity instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase) srcEntity;
			entity.attackEntityFrom(DamageSource.MAGIC, amount * 1.5F);
			this.addPotionEffect(new PotionEffect(PotionInit.wind_relief, 30, 0));
			return false;
		}

		// ダメージ倍処理
    	if (!isUnique) {
    		amount = this.getDamageAmount(this.world , src, amount);

    		if (!this.isSMDamage(src)) {
    			amount *= 0.75F;
    		}
    	}

    	// ボス
    	else {

			float health = this.getHealth();
			float maxHealth = this.getMaxHealth();
	    	boolean isRefresh = this.isPotionActive(PotionInit.refresh_effect);
    		amount =  Math.min(amount, 15);

        	// 風魔法チェック
        	if (this.checkMagicCyclone(src)) {
        		amount *= 0.05F;
        	}

        	// 光魔法チェック
        	if (this.checkMagicLight(src)) {
        		amount *= 0.5F;
        	}

        	// 体力50%以上かつリフレッシュエフェクト中なら
        	if (health >= (maxHealth * 0.5F) && isRefresh) {
    			amount *= 0.85F;
        	}

        	// 体力50%未満かつリフレッシュエフェクト中なら
        	else if (isRefresh) {
    			amount *= 0.67F;
        	}

        	// リフレッシュエフェクトが付いていないなら
        	else {
    			amount *= 0.5F;
        	}
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

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableInit.WITCHMADAMEVERRE;
	}

    // 死亡時
	public void onDeath(DamageSource cause) {

		if (!this.world.isRemote && !this.isDethCancel(this)) {

			if (this.isUnique() && !this.getAlly()) {
				this.dropItem(this.world, this, ItemInit.cosmic_crystal_shard, this.rand.nextInt(8) + 4);
				this.dropItem(this.world, this, ItemInit.mf_magiabottle, 1);
				this.dropItem(this.world, this, ItemInit.b_mf_magiabottle, this.rand.nextInt(8) + 1);
				this.dropItem(this.world, this, new ItemStack(BlockInit.figurine_if));
				this.dropItem(this.world, this, ItemInit.scorching_jewel, 1);
				this.dropItem(this.world, this, ItemInit.magic_pure_force, this.rand.nextInt(4) + 1);
				this.entityDropItem(new ItemStack(ItemInit.witch_cake, 1), 0F);
			}

			else if (this.rand.nextFloat() <= 0.01F) {
				this.entityDropItem(new ItemStack(ItemInit.scorching_jewel, 1), 0F);
			}

			else if (this.rand.nextFloat() <= 0.05F) {
				this.entityDropItem(new ItemStack(ItemInit.witch_cake, 1), 0F);
			}
		}

		super.onDeath(cause);
	}

	public void attackEntityWithRangedAttack(EntityLivingBase target, float dis) {

		EntityBaseMagicShot entity = this.getShot();
		if (entity == null) { return; }

        double x = target.posX - this.posX;
        double y = target.getEntityBoundingBox().minY - target.height / 2  - this.posY + 1D;
        double z = target.posZ - this.posZ;
        double xz = (double)MathHelper.sqrt(x * x + z * z);
		entity.shoot(x, y - xz * 0.015D, z, this.isUnique() ? 3F : 2F, 0);	// 射撃速度

		entity.setDamage(entity.getDamage() + this.getDamage());
		this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
        this.world.spawnEntity(entity);
	}

	public EntityBaseMagicShot getShot () {

		EntityBaseMagicShot entity = null;
		int rand = this.rand.nextInt(this.isUnique() && !this.getAlly() ? 5 : 4);

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
			blaze.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 600, 3, true, false));

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

	public float getDamage () {

		float health = this.getHealth();
		float maxHealth = this.getMaxHealth();
    	boolean isRefresh = this.isPotionActive(PotionInit.refresh_effect);

    	// ボス以外なら
		if (!this.isUnique()) {
			return 4;
		}

    	// 体力50%以上かつリフレッシュエフェクト中なら
		else if ((health >= maxHealth * 0.5F) && isRefresh) {
			return 8;
		}

    	// 体力50%未満かつリフレッシュエフェクト中なら
		else if (isRefresh) {
			return 12;
		}

    	// リフレッシュエフェクトが付いていないなら
		else {
			return 16;
		}
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

	// 杖を返す
	public ItemStack getWandHand () {
		return WAND;
	}

	// 腕の状態を返す
	public ArmMode getArm () {
		return this.dataManager.get(this.ISSPECIAL) ? ArmMode.SPECIAL_MAGIC : ArmMode.NONE;
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

	public void setSwingingArms(boolean swing) { }

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		if (this.isUnique() && !this.getAlly()) {
			this.bossInfo.addPlayer(player);
		}
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		if (this.isUnique() && !this.getAlly()) {
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

		this.setAlly(tags.getBoolean("isAlly"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);
		tags.setBoolean("isAlly", this.getAlly());
	}

	public ITextComponent getBossName () {
		return new TextComponentTranslation("entity.spirit_fire_ifrite.name", new Object[0]);
	}

	public class EntityAIMeteoRain extends EntityAIBase {

		private int spellWarmup;
		private int spellCooldown;
		private World world;
		private EntityIfritVerre ifrite;
		private final boolean isStop;

		public EntityAIMeteoRain (EntityIfritVerre entity, boolean isStop) {
			this.ifrite = entity;
			this.world = this.ifrite.world;
			this.isStop = isStop;
		}

		// AIを実行できるか
		public boolean shouldExecute() {
			return this.getTarget() != null && this.ifrite.ticksExisted >= this.spellCooldown && this.ifrite.isHalfHelth() && this.ifrite.isUnique() && !this.ifrite.getAlly();
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
			this.ifrite.dataManager.set(ISSPECIAL, Boolean.valueOf(true));
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
				this.ifrite.dataManager.set(ISSPECIAL, Boolean.valueOf(false));
			}
		}

		// 特殊行動開始
		protected void castSpell() { }

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
