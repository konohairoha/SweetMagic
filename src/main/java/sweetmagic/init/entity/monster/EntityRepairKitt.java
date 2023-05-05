package sweetmagic.init.entity.monster;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
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
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityCyclonMagic;
import sweetmagic.init.entity.projectile.EntityDigMagic;
import sweetmagic.init.entity.projectile.EntityFireMagic;
import sweetmagic.init.entity.projectile.EntityFrostMagic;
import sweetmagic.init.entity.projectile.EntityGravityMagic;
import sweetmagic.util.EventUtil;
import sweetmagic.util.ParticleHelper;

public class EntityRepairKitt extends EntityMob implements IRangedAttackMob, ISMMob {

	private int tickTime = 0;
	private int needTick = 0;
	private int coolTime = 0;
	private int deathTicks = 0;
	private int pX = (int) this.posX;
	private int pY = (int) this.posY;
	private int pZ = (int) this.posZ;
    private static final DataParameter<Integer> ARMORTYPE = EntityDataManager.<Integer>createKey(EntityRepairKitt.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> GADGETTIME = EntityDataManager.<Integer>createKey(EntityRepairKitt.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SWING = EntityDataManager.<Float>createKey(EntityRepairKitt.class, DataSerializers.FLOAT);
	private final BossInfoServer bossInfo = new BossInfoServer(this.getBossName(), BossInfo.Color.YELLOW, BossInfo.Overlay.NOTCHED_6);
	private final BossInfoServer armorBar = new BossInfoServer(this.getArmorName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_20);
	private static final ItemStack HAMMER = new ItemStack(ItemInit.aether_hammer);
	private static final ItemStack RIFLE = new ItemStack(ItemInit.aether_rifle);
	private static final ItemStack WAND = new ItemStack(ItemInit.kitt_gadget);
	private static final ItemStack BACK = new ItemStack(ItemInit.kitt_jet);

	public EntityRepairKitt(World world) {
		super(world);
		this.experienceValue = 120;
		this.setSize(0.5F, 1.5F);
	}

	// ボスゲージの名前を日本語表記に
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation("entity.repairkitt.name", new Object[0]);
	}

	public ITextComponent getBossName () {
		return new TextComponentTranslation("entity.repairkitt.name", new Object[0]);
	}

	private ITextComponent getArmorName () {
		return new TextComponentTranslation("item.kitt_gadget.name", new Object[0]);
	}

	public static void registerFixesWitch(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityRepairKitt.class);
	}

	public boolean getSpecial () {
		return this.getArmorType() == 2;
	}

	public void setSpecial (boolean isSpecial) {
		this.setArmorType(isSpecial ? 2 : 0);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(GADGETTIME, 0);
		this.dataManager.register(ARMORTYPE, 0);
		this.dataManager.register(SWING, 0F);
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
		this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	@Override
	protected float getSoundPitch() {
		return 0.875F;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_CAT_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource dame) {
		return SoundEvents.ENTITY_CAT_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_CAT_DEATH;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(288D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.pX = (int) this.posX;
		this.pY = (int) this.posY;
		this.pZ = (int) this.posZ;
		this.setHardHealth(this);
		this.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 9999, 0));
		return livingdata;
	}

	// モブスポーン条件
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.isSMDimension(this.world);
	}

	public void onLivingUpdate() {

		super.onLivingUpdate();
		int armorType = this.getArmorType();

		// 20秒に一回
		if (this.ticksExisted % 400 == 0 && this.getAttackTarget() != null) {

			boolean isHalf = this.isHalfHelth();

			if (!isHalf) {
				this.setArmorType(armorType == 0 ? 1 : 0 );
			}

			// 体力半分なら
			else if (armorType != 2) {

				// クールタイムが無くなっているなら
				if (this.coolTime <= 0) {
					this.dataManager.set(GADGETTIME, 160);
					this.setArmorType(2);

					List<EntityPlayerMP> list = this.getEntityList(EntityPlayerMP.class, this, 24F, 24F, 24F);

					for (EntityPlayerMP player : list) {
						this.armorBar.addPlayer(player);
					}
				}

				// クールタイムが消費しきっていないくてガジェッドモードでないなら
				else {
					this.setArmorType(armorType == 0 ? 1 : 0 );
				}
			}
		}

		if (armorType == 2) {
			if (!this.world.isRemote) { return; }
			Random rand = this.world.rand;
			double randDouble = rand.nextDouble();
			double d0 = this.posX - 0.25D + randDouble * 0.25D;
			double d1 = this.posY - 0.5D + randDouble * 0.3D;
			double d2 = this.posZ - 0.25D  + randDouble * 0.25D;
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, d0, d1, d2, 0, -0.425D, 0);
		}

		else {
			this.removeActivePotionEffect(PotionInit.aether_barrier);
			this.removeActivePotionEffect(PotionInit.cyclone);
			this.removeActivePotionEffect(PotionInit.shadow);

			if (armorType == 0) {

				float swingTick = this.swingAmout();

				if (this.swingAmout() >= 0) {
					this.dataManager.set(SWING, Math.max(0, swingTick - 0.1F));
				}
			}
		}
	}

	@Override
	protected void updateAITasks() {

		super.updateAITasks();

		boolean isHalf = this.isHalfHelth();
		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());

		if (this.getArmorType() == 2) {
			this.armorBar.setPercent((float) this.getGadgetTime() / 160F);

			// ターゲットの取得していないなら終了
			EntityLivingBase entity = this.getAttackTarget();

			// 自分とターゲットの高さが一定以下なら
			if (entity != null && this.posY <= entity.posY + 2D) {
				if (this.motionY < 0D) { this.motionY = 0D; }
				this.motionY += (0.125D - this.motionY) * 0.25D;
			}
		}

		if (isHalf) {
			this.bossInfo.setColor(this.getColor());

			if (this.coolTime > 0) {
				this.coolTime--;
			}

			if (this.isCool()) {
				ParticleHelper.spawnParticle(this.world, this.getPosition(), EnumParticleTypes.EXPLOSION_NORMAL);
				return;
			}
		}

		// ターゲットの取得していないなら終了
		EntityLivingBase entity = this.getAttackTarget();
		if (entity == null) { return; }

		// 1秒に一回実行
		this.tickTime++;
		if (this.tickTime % 5 != 0) { return; }

		int gadgetTime = this.getGadgetTime();
		if (gadgetTime > 0) {
			this.dataManager.set(GADGETTIME, gadgetTime - 1);
			this.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 40, 0));
			this.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 999, 10));
			this.addPotionEffect(new PotionEffect(PotionInit.cyclone, 40, 2));
			this.addPotionEffect(new PotionEffect(PotionInit.shadow, 40, 4));
		}

		if (this.tickTime % 20 != 0) { return; }

		int armorType = this.getArmorType();

		// 必要時間が取れていないなら
		if(this.needTick <= 0) {

			switch (armorType) {
			case 0:
				this.needTick = 100;
				break;
			case 1:
				this.needTick = 80;
				break;
			case 2:
				this.needTick = 100;
				break;
			}
		}

		if (this.getGadgetTime() == 0 && armorType == 2) {

			try {

				Collection<EntityPlayerMP> list = this.armorBar.getPlayers();

				if (list != null) {
					for (EntityPlayerMP player : list) {
						this.armorBar.removePlayer(player);
					}
				}
			}

			catch (Throwable e) { }

			// ガジェッド攻撃
			this.gadgetAttack();

			float dame = 7.5F;
			DamageSource src = DamageSource.MAGIC;
			List<EntityPlayer> playerList = this.getEntityList(EntityPlayer.class, this, 10D, 10D, 10D);
			if (playerList.isEmpty()) { return; }

			// プレイヤー分攻撃
			for (EntityPlayer player : playerList) {

				if (player.isCreative()) {
					continue;
				}

				float rangeDame = dame;
				double dis = 2 - player.getDistance(this.posX, this.posY, this.posZ) / rangeDame;
				rangeDame *= dis * 1.825F;
				player.attackEntityFrom(src, rangeDame);
				player.hurtResistantTime = 0;
			}

			// 6秒間動けなくさせる
			EventUtil.tameAIDonmov(this, 6);

			this.coolTime = 1000;
			this.setArmorType(this.rand.nextInt(2));
			this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 3F, 1F / (this.rand.nextFloat() * 0.2F + 0.9F));
			ParticleHelper.spawnParticle(this.world, this.getPosition().up(), EnumParticleTypes.EXPLOSION_HUGE);

			return;
		}

		// 時間に達してないなら
		if (this.tickTime < this.needTick) { return; }

		switch (this.getArmorType()) {
		case 0:
			// ハンマー攻撃
			this.hammerAttack();
			break;
		case 1:
			// ライフル攻撃
			this.lifleAttack(false);
			break;
		case 2:
			// ガジェッド攻撃
			this.gadgetAttack();
			break;
		}

		this.tickTime = 0;
	}

	public boolean isCool () {
		return this.coolTime <= 1000 && this.coolTime >= 976;
	}

	// ゲージの色の取得
	public Color getColor () {
		return BossInfo.Color.RED;
	}

	// ハンマー攻撃
	public void hammerAttack () {

		float dame = 7.5F;
		DamageSource src = DamageSource.MAGIC;
		List<EntityPlayer> playerList = this.getEntityList(EntityPlayer.class, this, 15D, 15D, 15D);
		if(playerList.isEmpty()) { return; }

		// プレイヤー分攻撃
		for (EntityPlayer player : playerList) {

			if (player.isCreative()) { continue; }

			player.attackEntityFrom(src, dame);
			player.motionY -= 1F;
			player.hurtResistantTime = 0;

			BlockPos pos = player.getPosition();
			for (BlockPos p : BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, -1, 1))) {

				if (this.world.getBlockState(p).getBlock() == Blocks.AIR) { continue; }

				ParticleHelper.spawnParticle(this.world, p.up(), EnumParticleTypes.FALLING_DUST);
			}
		}

		this.dataManager.set(SWING, 2F);
		this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.5F, 0.25F);

		BlockPos pos = this.getPosition();
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-2, -1, -2), pos.add(2, -1, 2))) {

			if (this.world.getBlockState(p).getBlock() == Blocks.AIR) { continue; }

			ParticleHelper.spawnParticle(this.world, p.up(), EnumParticleTypes.FALLING_DUST);
		}
	}

	// ライフル攻撃
	public void lifleAttack (boolean isGadget) {

		EntityBaseMagicShot entity = this.getShot();
		if (entity == null) { return; }

		EntityLivingBase target = this.getAttackTarget();

        double x = target.posX - this.posX;
        double y = target.getEntityBoundingBox().minY - target.height / 2  - this.posY;
		double z = target.posZ - this.posZ;
		double xz = (double) MathHelper.sqrt(x * x + z * z);
		entity.shoot(x, y + (isGadget ? xz * 0.05825D : y + xz * 0.075D) , z, 5.5F, isGadget ? 9 : 0);	// 射撃速度
		entity.setDamage(entity.getDamage() + (isGadget ? 7 : 16) );
		this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
        this.world.spawnEntity(entity);
	}

	// ガジェッド攻撃
	public void gadgetAttack () {

		for (int i = 0; i < 6; i++) {
			this.lifleAttack(true);
		}
	}

	public EntityBaseMagicShot getShot () {

		EntityBaseMagicShot entity = null;

		switch (this.rand.nextInt(5)) {
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

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if ( this.checkBossDamage(src) && !this.isMindControl(this) ) {

    		if (!this.isSMDamage(src) && src.getImmediateSource() instanceof EntityLivingBase) {
    			EntityLivingBase entity = (EntityLivingBase) src.getImmediateSource();
    			entity.attackEntityFrom(DamageSource.MAGIC, amount);
    		}

    		return false;
		}

    	// 風魔法チェック
    	if (this.checkMagicCyclone(src)) {
			this.teleportRandomly(this.rand);
    		amount *= 0.05F;
    	}

    	// 光魔法チェック
    	if (this.checkMagicLight(src)) {
    		amount *= 0.35F;
			this.teleportRandomly(this.rand);
    	}

    	// 爆発魔法チェック
    	if (this.checkMagicExplosion(src)) {
    		amount *= 0.3F;
			this.teleportRandomly(this.rand);
    	}

    	boolean isHalf = this.isHalfHelth();

    	if (isHalf && amount >= 1 && this.getArmorType() != 2) {
			this.teleportRandomly(this.rand);
    	}

		return super.attackEntityFrom(src, Math.min(amount, isHalf ? 12.5F : 17.5F));
	}

	public boolean teleportRandomly(Random rand) {
		double x = this.pX + (rand.nextDouble() - 0.5) * 4;
		double y = this.pY;
		double z = this.pZ + (rand.nextDouble() - 0.5) * 4;
		this.spawnParticle();
		return teleportTo(x, y, z);
	}

	// テレポート実施
	public boolean teleportTo(double x, double y, double z) {

		EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0);
		if (MinecraftForge.EVENT_BUS.post(event)) { return false; }

		boolean success = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());
		if (success) {
			this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
		}
		return success;
	}

	// パーティクルすぽーん
	public void spawnParticle() {
		for (int i = 0; i < 16; i++) {
			float f1 = (float) this.posX - 0.5F + this.rand.nextFloat();
			float f2 = (float) ((float) this.posY + 0.25F + this.rand.nextFloat() * 1.5);
			float f3 = (float) this.posZ - 0.5F + this.rand.nextFloat();
			this.world.spawnParticle(EnumParticleTypes.FLAME, f1, f2, f3, 0, 0, 0);
		}
	}

	@Override
	protected void onDeathUpdate() {

		++this.deathTicks;

		if (this.deathTicks >= 5 && this.deathTicks <= 40) {

			this.bossInfo.setPercent(0);

			if (this.deathTicks % 11 == 0) {
			    this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.65F, 1F);
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
			this.dropItem(this.world, this, ItemInit.cosmos_light_ingot, this.rand.nextInt(8) + 4);
			this.dropItem(this.world, this, ItemInit.pure_crystal, this.rand.nextInt(5) + 3);
			this.dropItem(this.world, this, ItemInit.deus_crystal, this.rand.nextInt(2));
			this.dropItem(this.world, this, ItemInit.cosmic_crystal_shard, this.rand.nextInt(11) + 6);
			this.dropItem(this.world, this, ItemInit.mf_magiabottle, this.rand.nextInt(6) + 2);
			this.dropItem(this.world, this, ItemInit.magicians_grobe, 1);
			this.dropItem(this.world, this, new ItemStack(BlockInit.figurine_rk));
			this.dropItem(this.world, this, ItemInit.magic_pure_force, this.rand.nextInt(4) + 1);
			this.dropItem(this.world, this, ItemInit.devil_cake, this.rand.nextInt(2) + 1);
		}

		super.onDeath(cause);
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_WITCH;
	}

	public float getEyeHeight() {
		return 1.62F;
	}

	// 体力半分かどうか
	public boolean isHalfHelth () {
		return this.getHealth() < this.getMaxHealth() / 2;
	}

	// 杖を返す
	public ItemStack getWandHand () {

		switch (this.getArmorType()) {
		case 0: return HAMMER;
		case 1: return RIFLE;
		case 2: return WAND;
		}

		return ItemStack.EMPTY;
	}

	// 杖を返す
	public ItemStack getBackPack () {
		return this.getArmorType() == 2 ? BACK : ItemStack.EMPTY;
	}

	public ArmMode getArm () {

		switch (this.getArmorType()) {
		case 0: return ArmMode.RIGHT_SWING;
		case 1: return ArmMode.RIGHT_SET;
		case 2: return ArmMode.BOTH_SET;
		}

		return ArmMode.NONE;
	}

	public float swingAmout () {
		return this.dataManager.get(SWING);
	}

	// 状態取得
	public int getArmorType () {
		return this.dataManager.get(ARMORTYPE);
	}

	// 状態の設定
	public void setArmorType (int type) {
		this.dataManager.set(ARMORTYPE, type);
	}

	// ガジェットの残り時間取得
	public int getGadgetTime () {
		return this.dataManager.get(GADGETTIME);
	}

	// エーテルバリアーなどのエフェクトを表示するかどうか
	public boolean isRenderEffect () {
		return false;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public void setInWeb() {}

    public void fall(float dis, float dama) { }

	@Override
	public boolean isNonBoss() {
		return false;
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
		this.armorBar.removePlayer(player);
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getBossName());
		this.armorBar.setName(this.getArmorName());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {
		super.readEntityFromNBT(tags);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getBossName());
			this.armorBar.setName(this.getArmorName());
		}
		this.setArmorType(tags.getInteger("armorType"));
	}

    public void writeEntityToNBT(NBTTagCompound tags) {
        super.writeEntityToNBT(tags);
        tags.setInteger("armorType", this.getArmorType());
    }

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float dist) { }

	@Override
	public void setSwingingArms(boolean swingingArms) { }
}
