package sweetmagic.init.entity.monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBabuleMagic;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityCyclonMagic;
import sweetmagic.init.entity.projectile.EntityDigMagic;
import sweetmagic.init.entity.projectile.EntityElectricMagic;
import sweetmagic.init.entity.projectile.EntityExplosionMagic;
import sweetmagic.init.entity.projectile.EntityFireMagic;
import sweetmagic.init.entity.projectile.EntityFlameNova;
import sweetmagic.init.entity.projectile.EntityFrostMagic;
import sweetmagic.init.entity.projectile.EntityGravityMagic;
import sweetmagic.init.entity.projectile.EntityMeteorMagic;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.SMDamage;
import sweetmagic.util.SMUtil;

public class EntitySandryon extends EntityMob implements ISMMob {

	private int deathTicks = 0;
	private int spellTicks = 0;
	private int damageCoolTime = 0;
	private int tickTime = 0;
	private final BossInfoServer bossInfo = new BossInfoServer(this.getBossName(), BossInfo.Color.YELLOW, BossInfo.Overlay.NOTCHED_6);
	private List<ItemStack> wandList = new ArrayList<>();
	private List<EntityPlayer> playerList = new ArrayList<>();
	private static final DataParameter<Boolean> ISWAND = EntityDataManager.<Boolean>createKey(EntitySandryon.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> WANDSIZE = EntityDataManager.<Integer>createKey(EntitySandryon.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> STARTMAGIC = EntityDataManager.<Boolean>createKey(EntitySandryon.class, DataSerializers.BOOLEAN);

	private int chargeTime = 0;
	private int coolTime = 0;
	private boolean attackTime = false;
	private int pX = (int) this.posX;
	private int pY = (int) this.posY;
	private int pZ = (int) this.posZ;

	public EntitySandryon(World world) {
		super(world);
		this.experienceValue = 2000;
		this.setSize(0.4F, 1.45F);
		this.isImmuneToFire = true;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ISWAND, Boolean.valueOf(false));
		this.dataManager.register(WANDSIZE, Integer.valueOf((int) 0));
		this.dataManager.register(STARTMAGIC, Boolean.valueOf(false));
	}

	public boolean getSpecial () {
		return this.dataManager.get(STARTMAGIC);
	}

	public void setSpecial (boolean isSpecial) {
		this.dataManager.set(STARTMAGIC, isSpecial);
	}

	public static void registerFixesWitch(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityWitchMadameVerre.class);
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(3, new EntityAITier3Magic(this));
		this.tasks.addTask(4, new EntityAIInfiniteWand(this));
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
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(512D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
	}

	// スポーン時の設定
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance dif, IEntityLivingData living) {
		this.pX = (int) this.posX;
		this.pY = (int) this.posY;
		this.pZ = (int) this.posZ;
		this.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 1));
		this.setHardHealth(this);
    	return super.onInitialSpawn(dif, living);
	}

	// インフィニティワンドを使ってないか
	public boolean isInfiniteWand () {
		return this.dataManager.get(this.STARTMAGIC);
	}

	// 魔法の貯め中か
	public boolean isWand () {
		return this.dataManager.get(this.ISWAND);
	}

	public void onLivingUpdate() {

		this.tickTime++;
		if (this.damageCoolTime > 0) { this.damageCoolTime--; }

		if (this.tickTime % 20 == 0) {

			// 4秒経てば
			if (this.tickTime % 80 == 0) {

				this.tickTime = 0;

				// 体力最大以外かつ最後にダメージを受けたのが一定時間たてば
				if (this.getMaxHealth() > this.getHealth() && this.damageCoolTime == 0) {
					this.setHealth(this.getHealth() + 2);
				}
			}

			this.playerList = this.getPlayerList();

			for (EntityPlayer player : this.playerList) {
				if (player.isPotionActive(PotionInit.shadow)) {
					player.removePotionEffect(PotionInit.shadow);
				}
			}
		}

		for (Potion potion : PotionInit.deBuffList) {
			if (this.isPotionActive(potion)) {
				this.removePotionEffect(potion);
			}
		}

		if (this.isPotionActive(MobEffects.GLOWING)) {
			this.removePotionEffect(MobEffects.GLOWING);
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
		if (this.isHalfHelth() && this.rand.nextFloat() <= 0.3F) {

			// リジェネ
			if (!this.isPotionActive(PotionInit.regene)) {
				this.addPotionEffect(new PotionEffect(PotionInit.regene, 1200, 0));
				this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
				this.coolTime += 300;
				return;
			}

			// switch文で回復量を変える予定
			this.setHealth(this.getHealth() + this.getHealValue());
			this.coolTime += 300;

			// パーティクルスポーン
			ParticleHelper.spawnHeal(this, EnumParticleTypes.VILLAGER_HAPPY, 16, 1, 4);
			this.playSound(SMSoundEvent.HEAL, 0.35F, 1F);
			return;
		}

		// 体力半分以上
		else {

			int time = 1200;
			int coolTime = 200;

			switch (this.rand.nextInt(5)) {
			case 0:
				// switch文で回復量を変える予定
				this.setHealth(this.getHealth() + this.getHealValue() * 0.5F);

				// パーティクルスポーン
				ParticleHelper.spawnHeal(this, EnumParticleTypes.VILLAGER_HAPPY, 16, 1, 4);
				this.playSound(SMSoundEvent.HEAL, 0.35F, 1F);
				this.coolTime += coolTime;

				return;
			case 1:
				// ルナッティクムーン
				if (!this.isPotionActive(PotionInit.shadow)) {
					this.addPotionEffect(new PotionEffect(PotionInit.shadow, time, 9));
					this.playSound(SMSoundEvent.MAGICSTART, 1F, 1F);
					this.coolTime += coolTime;
				}
				return;
			case 2:
				// エーテルバリアー
				if (!this.isPotionActive(PotionInit.aether_barrier)) {
					this.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, time * 2, 3));
					this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
					this.coolTime += coolTime;
				}
				return;
			case 3:
				// 攻撃力強化
				if (!this.isPotionActive(MobEffects.STRENGTH)) {
					this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, time, 5));
					this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
					this.coolTime += coolTime;
				}
				return;
			case 4:

				List<EntityPlayer> list = this.getPlayerList();
				double health = 40 + (list.size() - 1) * 20;

				for (int i = 0; i < 3; i++) {

					EntityWitchMadameVerre entity = new EntityWitchMadameVerre(this.world);

					int x = this.pX + this.rand.nextInt(16) - this.rand.nextInt(16);
					int y = this.pY + 2;
					int z = this.pZ + this.rand.nextInt(16) - this.rand.nextInt(16);
					BlockPos pos = new BlockPos(x, y, z);
					Block block = this.world.getBlockState(pos).getBlock();
					Block upBlock = this.world.getBlockState(pos.up()).getBlock();

					if (block != Blocks.AIR || upBlock != Blocks.AIR) {
						x = this.pX;
						z = this.pZ;
					}

					entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
					entity.setHealth(entity.getMaxHealth());
					entity.setPosition(x, y, z);
					entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
					this.world.spawnEntity(entity);
					SMUtil.tameAIAnger(entity, this.getAttackTarget());
				}

				this.coolTime += coolTime * 2;
				return;
			}
		}
	}

	// プレイヤーリストの取得
	public List<EntityPlayer> getPlayerList () {
		return this.getEntityList(EntityPlayer.class, this, 64D, 64D, 64D);
	}

	public float getHealValue () {
		return 20F;
	}

	// モブスポーン条件
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.canSpawn(this.world, this, 15);
	}

	@Override
	protected void updateAITasks() {

		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());

		if (this.isHalfHelth()) {
			this.bossInfo.setColor(this.getColor());
		}

		if ( this.checkTarget() || this.isInfiniteWand()) {
			this.resetAI();
			return;
		}

		if (this.chargeTime <= 0 && !this.attackTime) {
			this.chargeTime = this.rand.nextInt(100) + 50;
		}

		// クールタイムがあるなら減らす
		this.chargeTime--;
		if (this.chargeTime > 0) { return; }

		if (this.chargeTime <= 0 && !this.attackTime) {
			this.attackTime = true;
		}

		// 杖を持ってないなら
		if (this.wandList.isEmpty()) {
			this.setWand();
	        this.dataManager.set(ISWAND, Boolean.valueOf(true));
	        this.chargeTime = 100;
	        this.dataManager.set(WANDSIZE, 5);
		}

		else {

			// チャージ中なら
			if (this.chargeTime >= 0) {
				this.chargeTime--;
			}

			// チャージが終わってるなら
			else {

				int num = this.rand.nextInt(this.wandList.size());
				this.shotMagic(num, this.wandList.get(num).getItem());

				if (this.wandList.isEmpty()) {
					this.resetAI();
				}
			}
		}
	}

	public boolean checkTarget () {
		return this.getAttackTarget() == null || !this.getAttackTarget().isEntityAlive();
	}

	public Color getColor () {
		return this.isPotionActive(PotionInit.refresh_effect) ? BossInfo.Color.BLUE : BossInfo.Color.RED;
	}

	public void resetAI () {
        this.dataManager.set(ISWAND, Boolean.valueOf(false));
        this.dataManager.set(WANDSIZE, 0);
		this.attackTime = false;
		this.chargeTime = this.rand.nextInt(100) + 50;
		this.wandList = new ArrayList();
	}

	public void setWand () {

		switch (this.rand.nextInt(2)) {
		case 0:
			this.wandList.add(new ItemStack(ItemInit.aether_wand));
			this.wandList.add(new ItemStack(ItemInit.aether_wand_r));
			this.wandList.add(new ItemStack(ItemInit.aether_wand_g));
			this.wandList.add(new ItemStack(ItemInit.aether_wand_b));
			this.wandList.add(new ItemStack(ItemInit.aether_wand_p));
			this.wandList.add(new ItemStack(ItemInit.aether_wand_y));
			break;
		case 1:
			this.wandList.add(new ItemStack(ItemInit.divine_wand));
			this.wandList.add(new ItemStack(ItemInit.divine_wand_r));
			this.wandList.add(new ItemStack(ItemInit.divine_wand_g));
			this.wandList.add(new ItemStack(ItemInit.divine_wand_b));
			this.wandList.add(new ItemStack(ItemInit.divine_wand_p));
			this.wandList.add(new ItemStack(ItemInit.divine_wand_y));
			break;
		}
	}

	public void shotMagic (int i, Item item) {

		// 魔法の取得
		EntityBaseMagicShot entity = this.getMagic(item);

		if (entity != null) {
			EntityLivingBase target = this.getAttackTarget();
			boolean outLange = this.getDistance(target) > 48D;
	        double x = target.posX - this.posX;
	        double y = target.getEntityBoundingBox().minY - target.height * 0.5D  - this.posY;
	        double z = target.posZ - this.posZ;
	        double xz = (double)MathHelper.sqrt(x * x + z * z);
			entity.mobPower = 7;
			entity.shoot(x, y + (outLange ? xz * 0.025D : xz * 0.05D) , z, 5.5F, 0);	// 射撃速度
			entity.setDamage(entity.getDamage() + 10);
			this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
	        this.world.spawnEntity(entity);

	    	// マルチなら対象を増やす
	        this.multiAttack(target, entity, item);
		}

		this.chargeTime += 14;
		this.wandList.remove(i);
        this.setWandSize(this.getWandSize() - 1);
	}

	public int getWandSize () {
		return this.dataManager.get(WANDSIZE);
	}

	public void setWandSize (int i) {
        this.dataManager.set(WANDSIZE, i);
	}

	// 魔法の取得
	public EntityBaseMagicShot getMagic (Item item) {

		EntityBaseMagicShot entity = null;

		//=========================
		//		tier1
		//=========================


		if (item == ItemInit.aether_wand) {
			entity = new EntityDigMagic(this.world, this, ItemStack.EMPTY, 0);
		}

		else if (item == ItemInit.aether_wand_r) {
			entity = new EntityFireMagic(this.world, this, ItemStack.EMPTY);
		}

		else if (item == ItemInit.aether_wand_g) {
			entity = new EntityCyclonMagic(this.world, this, ItemStack.EMPTY);
		}

		else if (item == ItemInit.aether_wand_b) {
			entity = new EntityBabuleMagic(this.world, this, ItemStack.EMPTY, 0);
		}

		else if (item == ItemInit.aether_wand_y) {
			entity = new EntityFrostMagic(this.world, this, ItemStack.EMPTY, false);
		}

		else if (item == ItemInit.aether_wand_p) {
			entity = new EntityGravityMagic(this.world, this, ItemStack.EMPTY);
		}


		//=========================
		//		tier2
		//=========================

		else if (item == ItemInit.divine_wand) {
			entity = new EntityExplosionMagic(this.world, this, ItemStack.EMPTY, 2);
			entity.setDamage(5);
			entity.mobPower = 12;
		}

		else if (item == ItemInit.divine_wand_r) {
			entity = new EntityFlameNova(this.world, this, ItemStack.EMPTY);
			entity.setDamage(3);
		}

		else if (item == ItemInit.divine_wand_g) {

			entity = new EntityCyclonMagic(this.world, this, ItemStack.EMPTY);

			EntityLivingBase target = this.getAttackTarget();

			for (int k = 0; k < 2; k++) {
				EntityCyclonMagic magic = new EntityCyclonMagic(this.world, this, ItemStack.EMPTY);

				boolean outLange = this.getDistance(target) > 48D;
		        double x = target.posX - this.posX;
		        double y = target.getEntityBoundingBox().minY - target.height * 0.5D  - this.posY;
		        double z = target.posZ - this.posZ;
		        double xz = (double)MathHelper.sqrt(x * x + z * z);
		        magic.shoot(x, y + (outLange ? xz * 0.025D : xz * 0.05D) , z, 5.5F, 6);	// 射撃速度
		        magic.setDamage(magic.getDamage() + 10);
		        this.world.spawnEntity(magic);
			}
		}

		else if (item == ItemInit.divine_wand_b) {
			entity = new EntityFrostMagic(this.world, this, ItemStack.EMPTY, true);
			entity.setDamage(3);
		}

		else if (item == ItemInit.divine_wand_y) {
			entity = new EntityBabuleMagic(this.world, this, ItemStack.EMPTY, 2);
			entity.setDamage(3);
		}

		else if (item == ItemInit.divine_wand_p) {
			entity = new EntityGravityMagic(this.world, this, ItemStack.EMPTY);
			entity.range = 6D;
			entity.isHitDead = true;
		}

		return entity;
	}

	// マルチなら対象を増やす
	public void multiAttack (EntityLivingBase target, EntityBaseMagicShot entity, Item item) {

		// 一人しかいないなら終了
		List<EntityPlayer> entityList = this.getPlayerList();
		if (entityList.size() <= 1) { return; }

		// プレイヤー分回す
		for (EntityPlayer player : entityList) {

			// ターゲットがターゲットを同じかクリエイティブなら次へ
			if (player == target || player.isCreative()) { continue; }

			EntityBaseMagicShot magic = this.getMagic(item);

			if (entity != null) {
				boolean outLange = this.getDistance(player) > 48D;
		        double x = player.posX - this.posX;
		        double y = player.getEntityBoundingBox().minY - player.height * 0.5D  - this.posY;
		        double z = player.posZ - this.posZ;
		        double xz = (double)MathHelper.sqrt(x * x + z * z);
		        magic.mobPower = 7;
		        magic.shoot(x, y + (outLange ? xz * 0.025D : xz * 0.05D) , z, 5.5F, 0);	// 射撃速度
		        magic.setDamage(magic.getDamage() + 10);
				this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
		        this.world.spawnEntity(magic);
			}
		}
	}

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if ( this.checkBossDamage(src) && !this.isMindControl(this) ) {

    		if (!this.isSMDamage(src) && src.getImmediateSource() instanceof EntityLivingBase) {
    			EntityLivingBase entity = (EntityLivingBase) src.getImmediateSource();
    			entity.attackEntityFrom(DamageSource.MAGIC, amount * 2F);
    			this.addPotionEffect(new PotionEffect(PotionInit.wind_relief, 30, 0));
    		}

    		return false;
		}

    	// 風魔法チェック
    	if (this.checkMagicCyclone(src)) {
    		amount *= 0.05F;
    	}

    	// 光魔法チェック
    	if (this.checkMagicLight(src)) {
    		amount *= 0.3F;
    	}

    	// 魔法チャージ中
    	if (this.isWand()) {
    		amount *= 0.75F;
    		this.playSound(SoundEvents.ENTITY_BLAZE_HURT, 1F, 1F);
    	}

    	// インフィニティワンドチャージ中
    	else if (this.isInfiniteWand()) {
    		amount *= 0.375F;
    		this.playSound(SoundEvents.ENTITY_BLAZE_HURT, 1F, 1F);
    	}

		// 体力が半分でダメージカット
		if (this.isHalfHelth()) {

    		if (this.isPotionActive(PotionInit.refresh_effect)) {
    			amount *= 0.875F;
    		}

    		else {
    			amount *= 0.775F;
    		}
		}

		this.damageCoolTime = 300;
		this.teleportRandomly(this.rand);
		amount /= this.getMaltiCap();

		// 火力キャップを返す
		return super.attackEntityFrom(src, Math.min(amount, this.getCap()));
	}

    // 火力キャップの取得
    public float getCap () {
    	return this.isHalfHelth() ? 25F : 30F;
    }

    // マルチ時の補正値
    public float getMaltiCap () {

    	// プレイヤーリストが空ならプレイヤーリストを入れる
    	if (this.playerList.isEmpty()) {
    		this.playerList = this.getPlayerList();
    	}

    	// それでもプレイヤーがいないなら補正なし
    	if (this.playerList.isEmpty() || this.playerList.size() <= 1) {
    		return 1F;
    	}

    	// 補正値を返す
    	return this.playerList.size() * 1.5F;
    }

	private boolean teleportRandomly(Random rand) {
		double x = this.pX + (rand.nextDouble() - 0.5) * 8;
		double y = this.pY + rand.nextInt(4);
		double z = this.pZ + (rand.nextDouble() - 0.5) * 8;
		this.spawnParticle();
		return teleportTo(x, y, z);
	}

	private boolean teleportTo(double x, double y, double z) {
		EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0);
		if (MinecraftForge.EVENT_BUS.post(event)) { return false; }
		boolean success = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());
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

	// 腕の状態を返す
	public ArmMode getArm () {
		return this.getSpecial() ? ArmMode.SPECIAL_MAGIC : ArmMode.NONE;
	}

	@Override
	protected void onDeathUpdate() {

		++this.deathTicks;

		if (this.deathTicks >= 5 && this.deathTicks <= 40) {

			this.bossInfo.setPercent(0);

			if (this.deathTicks % 11 == 0) {
			    this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5F, 1F);
			}

			if (this.deathTicks % 5 == 0) {
				float f = (this.rand.nextFloat() - 0.5F) * 3.0F;
				float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F;
				float f2 = (this.rand.nextFloat() - 0.5F) * 3.0F;
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f, this.posY + 1D + f1, this.posZ + f2, 0D, 0D, 0D);
			}
		}

		if (this.deathTicks > 40 && !this.world.isRemote) {
			this.setDead();
		}
	}

	public void onDeath(DamageSource cause) {

		if (!this.world.isRemote && !this.isDethCancel(this)) {
			this.dropItem(this.world, this, ItemInit.mystical_page, this.rand.nextInt(4) + 1);
			this.dropItem(this.world, this, ItemInit.divine_crystal, this.rand.nextInt(7) + 1);
			this.dropItem(this.world, this, ItemInit.pure_crystal, this.rand.nextInt(3) + 1);
			this.dropItem(this.world, this, ItemInit.witch_tears, this.rand.nextInt(2) + 1);
			this.dropItem(this.world, this, ItemInit.cosmic_crystal_shard, this.rand.nextInt(8) + 8);
			this.dropItem(this.world, this, ItemInit.cosmic_crystal, this.rand.nextInt(2));
			this.dropItem(this.world, this, ItemInit.magician_quillpen, 1);
			this.dropItem(this.world, this, ItemInit.accebag, 3);
			this.dropItem(this.world, this, ItemInit.mf_magiabottle, this.rand.nextInt(4) + 2);
			this.dropItem(this.world, this, new ItemStack(BlockInit.figurine_sa));
			this.dropItem(this.world, this, ItemInit.magic_cosmic_force, this.rand.nextInt(3) + 1);
			this.dropItem(this.world, this, ItemInit.magic_creative, this.rand.nextInt(2));
			this.entityDropItem(new ItemStack(ItemInit.witch_cake, this.rand.nextInt(4) + 2), 0F);
		}

		super.onDeath(cause);
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_WITCH;
	}

	// 体力半分かどうか
	public boolean isHalfHelth () {
		return this.getHealth() < this.getMaxHealth() / 2;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public void setInWeb() { }

	@Override
	public boolean isNonBoss() {
		return false;
	}

	public void setSwingingArms(boolean swing) { }

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
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
		return new TextComponentTranslation("entity.sandryon.name", new Object[0]);
	}


	/*=======================================
	 * 				必殺技
	 *=======================================
	 */


	public class EntityAITier3Magic extends EntityAIBase {

		protected int spellWarmup;
		protected int spellCooldown;
		public World world;
		public EntitySandryon entity;
		public int data = 0;

		public EntityAITier3Magic (EntitySandryon entity) {
			this.entity = entity;
			this.world = this.entity.world;
		}

		// AIを実行できるか
		public boolean shouldExecute() {
			return this.getTarget() != null && this.entity.ticksExisted >= this.spellCooldown && this.entity.isHalfHelth() && this.entity.isPotionActive(PotionInit.refresh_effect);
		}

		// 実行できるか
		public boolean shouldContinueExecuting() {
			return this.entity.getAttackTarget() != null && this.spellWarmup > 0;
		}

		public void startExecuting() {
			this.spellWarmup = this.getCastWarmupTime();
			this.entity.spellTicks = this.getCastingTime();
			this.spellCooldown = this.entity.ticksExisted + this.getCastingInterval();
			this.data = this.world.rand.nextInt(3);
		}

		// タスク処理
		public void updateTask() {

			--this.spellWarmup;
			Random rand = world.rand;
			int range = 16;
			EntityLivingBase target = this.entity.getAttackTarget();
			if (target == null) { return; }

			double x = (rand.nextDouble() * range) - rand.nextDouble() * range;
			double y = (rand.nextDouble() * range) - rand.nextDouble() * range;
			double z = (rand.nextDouble() * range) - rand.nextDouble() * range;
	        BlockPos pos = new BlockPos(target.posX + x, target.posY + y, target.posZ + z);
	        int posY = this.world.canSeeSky(pos) ? 20 : 5;
	        this.magicAttack(pos, posY);

			if (this.spellWarmup == 0) {
				this.castSpell();
			}
		}

		public void magicAttack (BlockPos pos, int posY) {
			if (!this.world.isRemote) {
				EntityBaseMagicShot entity = this.getMagic(this.data);
				entity.shoot(0, entity.motionY - 0.33F, 0, 0, 0);
				entity.motionY -= 3;
				entity.setPosition(pos.getX(), pos.getY() + posY, pos.getZ());
				entity.setDamage(entity.getDamage() + 8);
				entity.isHitDead = true;
				this.world.spawnEntity(entity);
			}

			this.entity.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.33F, 0.67F);
		}

		public EntityBaseMagicShot getMagic (int i) {

			EntityBaseMagicShot magic = null;

			switch (i) {
			case 0:
				magic = new EntityElectricMagic(this.world, this.entity, ItemStack.EMPTY);
				break;
			case 1:
				magic = new EntityMeteorMagic(this.world, this.entity, ItemStack.EMPTY);
				break;
			case 2:
				magic = new EntityFrostMagic(this.world, this.entity, ItemStack.EMPTY, true);
				break;
			}

			return magic;
		}

		// 特殊行動開始
		protected void castSpell() { }

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
			return 480;
		}

		// ターゲット取得
		public EntityLivingBase getTarget () {
			return this.entity.getAttackTarget();
		}
	}

	public class EntityAIInfiniteWand extends EntityAITier3Magic {

		public EntityAIInfiniteWand (EntitySandryon entity) {
			super(entity);
		}

		// AIを実行できるか
		@Override
		public boolean shouldExecute() {
			return this.getTarget() != null && this.entity.ticksExisted >= this.spellCooldown && !this.entity.isPotionActive(PotionInit.refresh_effect);
		}

		// ウォームアップタイム
		protected int getCastWarmupTime() {
			return 240;
		}

		// インターバル
		protected int getCastingInterval() {
			return 800;
		}

		@Override
		public void startExecuting() {
			this.spellWarmup = this.getCastWarmupTime();
			this.entity.spellTicks = this.getCastingTime();
			this.spellCooldown = this.entity.ticksExisted + this.getCastingInterval();
			this.setMagicData(true);
		}

		// タスク処理
		@Override
		public void updateTask() {

			--this.spellWarmup;

			if (this.spellWarmup == 0) {
				this.castSpell();
				this.setMagicData(false);
			}
		}

		// 特殊行動開始
		@Override
		protected void castSpell() {

			// えんちちーのリストを取得
			AxisAlignedBB aabb = this.entity.getEntityBoundingBox().grow(32D);
			List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
			float explo = 30F;
			DamageSource damage = SMDamage.MagicDamage(this.entity, this.entity);

			for (EntityLivingBase entity : list) {

				if (entity instanceof IMob) { continue; }

				float dame = explo;
				double distance = 2 - entity.getDistance(this.entity.posX, this.entity.posY, this.entity.posZ) / dame;
				dame *= distance * 1.825F;
				entity.attackEntityFrom(damage, dame);
				entity.hurtResistantTime = 0;
				ParticleHelper.spawnHeal(entity, EnumParticleTypes.FIREWORKS_SPARK, 64, this.world.rand.nextGaussian() * 0.1, 4);

				for (Potion potion : PotionInit.buffList) {
					if (entity.isPotionActive(potion)) {
						entity.removePotionEffect(potion);
					}
				}
			}

			this.entity.playSound(SMSoundEvent.HORAMAGIC, 1F, 1F);
		}

		public void setMagicData (boolean flag) {
			this.entity.setSpecial(flag);
		}
	}
}
