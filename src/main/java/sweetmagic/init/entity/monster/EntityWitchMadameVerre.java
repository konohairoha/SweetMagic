package sweetmagic.init.entity.monster;

import java.util.Random;

import javax.annotation.Nullable;

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
import sweetmagic.init.entity.projectile.EntityCyclonMagic;
import sweetmagic.init.entity.projectile.EntityDigMagic;
import sweetmagic.init.entity.projectile.EntityElectricMagic;
import sweetmagic.init.entity.projectile.EntityFireMagic;
import sweetmagic.init.entity.projectile.EntityFlameNova;
import sweetmagic.init.entity.projectile.EntityFrostMagic;
import sweetmagic.init.entity.projectile.EntityGravityMagic;
import sweetmagic.util.ParticleHelper;

public class EntityWitchMadameVerre extends EntityMob implements IRangedAttackMob, ISMMob {

    public int spellTicks = 0;
	public boolean isCharge = false;
	private int coolTime = 0;
	private int damageCoolTime = 0;
	private int tickTime = 0;
	private final BossInfoServer bossInfo = new BossInfoServer(this.getBossName(), BossInfo.Color.YELLOW, BossInfo.Overlay.NOTCHED_6);

	public EntityWitchMadameVerre(World world) {
		super(world);
		this.experienceValue = 120;
		this.setSize(0.4F, 1.45F);
	}

	public static void registerFixesWitch(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityWitchMadameVerre.class);
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
		this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(4, new EntityAIThunderbolt(this, true));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.setHardHealth(this);
		return livingdata;
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

			// クールタイムが終わってないなら
			if (this.coolTime > 0) { return; }
		}

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

			boolean isUnique = this.isUnique();
			int time = isUnique ? 800 : 400;
			int coolTime = isUnique ? 200 : 300;

			switch (this.rand.nextInt(4)) {
			case 0:
				// リフレッシュ・エフェクト
				if (!this.isPotionActive(PotionInit.refresh_effect)) {
					this.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, time, 0));
					this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
					this.coolTime += coolTime * 1.25;
				}
				return;
			case 1:
				// ルナッティクムーン
				if (!this.isPotionActive(PotionInit.shadow)) {
					this.addPotionEffect(new PotionEffect(PotionInit.shadow, time, isUnique ? 9 : 4));
					this.playSound(SMSoundEvent.MAGICSTART, 1F, 1F);
					this.coolTime += coolTime;
				}
				return;
			case 2:
				// エーテルバリアー
				if (!this.isPotionActive(PotionInit.aether_barrier)) {
					this.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, time, 1));
					this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
					this.coolTime += coolTime;
				}
				return;
			case 3:
				// 攻撃力強化
				if (!this.isPotionActive(MobEffects.STRENGTH)) {
					this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, time, isUnique ? 4 : 1));
					this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
					this.coolTime += coolTime;
				}
				return;
			}
		}
	}

	public float getHealValue () {
		return this.isUnique() ? this.getMaxHealth() * 0.2F : this.getMaxHealth() / 2;
	}

	// モブスポーン条件
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.canSpawn(this.world, this, 15);
	}

	@Override
	protected void updateAITasks() {

		if (!this.isUnique()) { return; }

		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());

	}

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src) && !this.isMindControl(this)) {
    		return false;
		}

		// ダメージ倍処理
    	if (!this.isUnique()) {
    		amount = this.getDamageAmount(this.world , src, amount);
    	}

    	else {

        	// 風魔法チェック
        	if (this.checkMagicCyclone(src)) {
        		amount *= 0.1F;
        	}

        	// 光魔法チェック
        	if (this.checkMagicLight(src)) {
        		amount *= 0.5F;
        	}

    		amount = Math.min(amount, 15);
    	}

		if (!this.isSMDamage(src)) {
			amount *= this.isUnique() ? 0.25 : 0.75;
		}

		this.damageCoolTime = 400;

		return super.attackEntityFrom(src, amount);
	}

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
					this.dropItem(this.world, this, ItemInit.witch_scroll, 1);
				}
			}

			else if (this.rand.nextFloat() <= 0.01F) {
				this.entityDropItem(new ItemStack(ItemInit.witch_scroll, 1), 0F);
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
        double x = target.posX - this.posX;
        double y = target.getEntityBoundingBox().minY - target.height / 2  - this.posY;
        double z = target.posZ - this.posZ;
        double xz = (double)MathHelper.sqrt(x * x + z * z);
		entity.shoot(x, y - xz * 0.015D, z, this.isUnique() ? 3F : 2F, 0);	// 射撃速度

		int dame = this.isUnique() ? 10 : 4;
		entity.setDamage(entity.getDamage() + dame);
		this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
        this.world.spawnEntity(entity);
	}

	public EntityBaseMagicShot getShot () {

		EntityBaseMagicShot entity = null;
		int rand = this.rand.nextInt(5);

		switch (rand) {
		case 0:
			if (this.isUnique()) {
				entity = new EntityFlameNova(this.world, this, ItemStack.EMPTY);
			} else {
				entity = new EntityFireMagic(this.world, this, ItemStack.EMPTY);
			}
			break;
		case 1:
			entity = new EntityFrostMagic(this.world, this, ItemStack.EMPTY, this.isUnique());
			break;
		case 2:
			entity = new EntityCyclonMagic(this.world, this, ItemStack.EMPTY);
			break;
		case 3:
			entity = new EntityGravityMagic(this.world, this, ItemStack.EMPTY);
			if (this.isUnique()) {
				entity.range = 6D;
				entity.isHitDead = true;
			}
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

	public void setSwingingArms(boolean swingingArms) {
	}

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
		return new TextComponentTranslation("entity.witchcmadame_verre.name", new Object[0]);
	}

	public class EntityAIThunderbolt extends EntityAIBase {

		protected int spellWarmup;
		protected int spellCooldown;
		public World world;
		public EntityWitchMadameVerre witch;
		public final boolean isStop;

		public EntityAIThunderbolt (EntityWitchMadameVerre entity, boolean isStop) {
			this.witch = entity;
			this.world = this.witch.world;
			this.isStop = isStop;
		}

		// AIを実行できるか
		public boolean shouldExecute() {
			if (this.getTarget() == null) {
				return false;
			} else {
				return this.witch.ticksExisted >= this.spellCooldown && this.witch.isHalfHelth() && this.witch.isUnique();
			}
		}

		// 実行できるか
		public boolean shouldContinueExecuting() {
			return this.witch.getAttackTarget() != null && this.spellWarmup > 0;
		}

		public void startExecuting() {
			this.spellWarmup = this.getCastWarmupTime();
			this.witch.spellTicks = this.getCastingTime();
			this.spellCooldown = this.witch.ticksExisted + this.getCastingInterval();
			this.setCharge(true);
		}

		// タスク処理
		public void updateTask() {

			--this.spellWarmup;
			Random rand = world.rand;
			int range = 16;
			EntityLivingBase target = this.witch.getAttackTarget();
			if (target == null) { return; }

			double x = (rand.nextDouble() * range) - rand.nextDouble() * range;
			double y = (rand.nextDouble() * range) - rand.nextDouble() * range;
			double z = (rand.nextDouble() * range) - rand.nextDouble() * range;
	        BlockPos pos = new BlockPos(target.posX + x, target.posY + y, target.posZ + z);
	        int posY = this.world.canSeeSky(pos) ? 20 : 7;

			if (!this.world.isRemote) {
				EntityElectricMagic entity = new EntityElectricMagic(this.world, this.witch, ItemStack.EMPTY);
				entity.shoot(0, entity.motionY - 0.33F, 0, 0, 0);
				entity.motionY -= 3;
				entity.setPosition(pos.getX(), pos.getY() + posY, pos.getZ());
				entity.setDamage(entity.getDamage() + 5);
				this.world.spawnEntity(entity);
			}

			this.witch.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.33F, 0.67F);

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
			return 120;
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
			return this.witch.getAttackTarget();
		}

		// チャージの設定
		public void setCharge (boolean charge) {
			if (this.isStop) {
				this.witch.isCharge = charge;
			}
		}
	}
}
