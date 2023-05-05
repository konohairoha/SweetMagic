package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.particle.Particle;
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
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import sweetmagic.client.particle.ParticleTwilightlight;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBlastBomb;
import sweetmagic.init.entity.projectile.EntityElectricMagic;
import sweetmagic.init.entity.projectile.EntityLightMagic;

public class EntityElshariaCurious extends EntityMob implements ISMMob, IRangedAttackMob {

	private int coolTime = 0;
	private int spellTicks = 0;
	private int deathTicks = 0;
	private float damage = 0F;
	private static final float REQUIR_DAMAGE = 64F;
	private BlockPos boundOrigin;
	private int pX = (int) this.posX;
	private int pY = (int) this.posY;
	private int pZ = (int) this.posZ;
	private final BossInfoServer bossInfo = new BossInfoServer(this.getBossName(), BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_10);
    private static final DataParameter<Integer> ARMPROPERTY = EntityDataManager.<Integer>createKey(EntityElshariaCurious.class, DataSerializers.VARINT);

	public EntityElshariaCurious(World world) {
		super(world);
		this.experienceValue = 1400;
		this.isImmuneToFire = true;
		this.moveHelper = new AIMoveControl(this);
		this.setSize(0.75F, 2.3F);
		this.setNoGravity(true);
	}

	public static void registerFixesWitch(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityElshariaCurious.class);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ARMPROPERTY, 0);
	}

	public boolean getSpecial () {
		return this.getARMPro() == 1;
	}

	public void setSpecial (boolean isSpecial) {
		this.setARM(isSpecial ? 1 : 0);
	}

	// ボスゲージの名前を日本語表記に
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation("entity.elsharia_curious.name", new Object[0]);
	}

	public ITextComponent getBossName () {
		return new TextComponentTranslation("entity.elsharia_curious.name", new Object[0]);
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackRanged(this, 2D, 100, 32F));
		this.tasks.addTask(3, new AIMoveRandom());
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.tasks.addTask(9, new EntityAISacredArrow(this));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	protected SoundEvent getAmbientSound() {
		return SMSoundEvent.EC_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource dame) {
		return SMSoundEvent.EC_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SMSoundEvent.EC_DEATH;
	}

	protected float getSoundVolume() {
		return 0.67F;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(640D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
	}

	// スポーン時の設定
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance dif, IEntityLivingData living) {
		this.pX = (int) this.posX;
		this.pY = (int) this.posY;
		this.pZ = (int) this.posZ;
		this.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 99999, 1));
		this.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 0));
		this.setHardHealth(this);
		this.motionY += 0.25D;
		this.coolTime = 600;
    	return super.onInitialSpawn(dif, living);
	}

	public int getARMPro () {
		return this.dataManager.get(ARMPROPERTY);
	}

	public void setARM (int i) {
        this.dataManager.set(ARMPROPERTY, i);
	}

	@Override
	public ArmMode getArm() {

		switch (this.getARMPro()) {
		case 1:
			return ArmMode.RIGHT_MAGIC;
		case 2:
			return ArmMode.RIGHT_SHOT;
		}

		return ArmMode.NONE;
	}

	public void onLivingUpdate() {

		// パーティクルスポーン
		if (this.world.isRemote && this.isRender()) {
			this.spawnParticle(1);
		}

		super.onLivingUpdate();
	}

	public void spawnParticle (int value) {

		Random rand = this.world.rand;
		float mY = (float) (this.motionY <= 0F ? 1F : this.motionY);

		for (int i = 0; i < value; ++i) {
			float f1 = (float) (this.posX - 0.5F + rand.nextFloat() + this.motionX * i / 4.0F);
			float f2 = (float) (this.posY + 0.5F + rand.nextFloat() * 0.5 + mY * i / 4.0D);
			float f3 = (float) (this.posZ - 0.5F + rand.nextFloat() + this.motionZ * i / 4.0D);
			this.world.spawnParticle(EnumParticleTypes.END_ROD, f1, f2, f3, 0, 0, 0);
		}

		if (this.getARMPro() == 1) {

			float moX = this.getRandFloat() - this.getRandFloat() * 1.25F;
			float moY = this.getRandFloat() - this.getRandFloat();
			float moZ = this.getRandFloat() - this.getRandFloat() * 1.25F;

			float x = -moX / 12F;
			float y = -moY / 12F;
			float z = -moZ / 12F;

			for (int i = 0; i < 8; i++) {
				float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + moX * i * 0.25F);
				float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5F + moY * i * 0.25F) + 1.5F;
				float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + moZ * i * 0.25F);
				Particle effect = ParticleTwilightlight.create(this.world, f1, f2, f3, x, y, z);
				this.getParticle().addEffect(effect);
			}
		}
	}

	public float getRandFloat () {
		return this.rand.nextFloat() * 1.5F;
	}

	@Override
	protected void updateAITasks() {

		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());

		if (this.isHalfHelth()) {
			this.bossInfo.setColor(this.getColor());
		}

		EntityLivingBase target = this.getAttackTarget();
		if (target == null) { return; }

		if (this.posY - target.posY > 5.5D) {
			this.motionY -= 0.004D;
		}

		if (this.coolTime > 0) {
			this.coolTime--;
		}

		// クールタイムが終わってないなら
		if (this.coolTime > 0 || this.world.isRemote) { return; }

		int range = 16;
		Random rand = world.rand;
		int count = Math.max(1, (int) (this.getMaxHealth() - this.getHealth()) / 64);
		this.coolTime = 500;
        int posY = this.world.canSeeSky(this.getPosition()) ? 16 : 7;

		for (int i = 0; i <= count; i++) {

			double x = (rand.nextDouble() * range) - rand.nextDouble() * range;
			double y = (rand.nextDouble() * range) - rand.nextDouble() * range;
			double z = (rand.nextDouble() * range) - rand.nextDouble() * range;
	        BlockPos pos = new BlockPos(target.posX + x, target.posY + y, target.posZ + z);

	        EntityElectricMagic entity = new EntityElectricMagic(this.world, this, 1);
			entity.shoot(0, entity.motionY - 0.33F, 0, 0, 0);
			entity.motionY -= 3;
			entity.setPosition(pos.getX(), pos.getY() + posY, pos.getZ());
			entity.setDamage(entity.getDamage() + 6 + this.getAddPower(100D));
			this.world.spawnEntity(entity);
		}
	}

	public float getHealthPercent () {
		return this.getHealth() / this.getMaxHealth();
	}

	public void attackEntityWithRangedAttack(EntityLivingBase target, float dis) {

		EntityBlastBomb entity = new EntityBlastBomb(this.world, this, ItemStack.EMPTY, 1);
		double x = target.posX - this.posX;
		double y = target.getEntityBoundingBox().minY - target.height / 2 - this.posY;
		double z = target.posZ - this.posZ;
		double xz = (double) MathHelper.sqrt(x * x + z * z);
		entity.shoot(x, y - xz * 0.015D, z, 2.5F, 0); // 射撃速度

		entity.setDamage(entity.getDamage() + 4.5D + this.getAddPower(80D));
		this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
		this.world.spawnEntity(entity);
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

	@Nullable
	public BlockPos getBoundOrigin() {
		return this.boundOrigin;
	}

	public void setBoundOrigin(@Nullable BlockPos boundOrig) {
		this.boundOrigin = boundOrig;
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
			this.bossInfo.setName(this.getDisplayName());
		}

		if (tags.hasKey("BoundX")) {
			this.boundOrigin = new BlockPos(tags.getInteger("BoundX"), tags.getInteger("BoundY"), tags.getInteger("BoundZ"));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);

		if (this.boundOrigin != null) {
			tags.setInteger("BoundX", this.boundOrigin.getX());
			tags.setInteger("BoundY", this.boundOrigin.getY());
			tags.setInteger("BoundZ", this.boundOrigin.getZ());
		}
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_GHAST;
	}

	// 体力半分かどうか
	public boolean isHalfHelth () {
		return this.getHealth() < this.getMaxHealth() / 2;
	}

	// エーテルバリアーなどのエフェクトを表示するかどうか
	public boolean isRenderEffect () {
		return false;
	}

	public Color getColor () {
		return BossInfo.Color.RED;
	}

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if ( this.checkBossDamage(src) && !this.isMindControl(this) ) {
    		if (!this.isSMDamage(src) && src.getImmediateSource() instanceof EntityLivingBase) {
    			EntityLivingBase entity = (EntityLivingBase) src.getImmediateSource();
    			entity.attackEntityFrom(DamageSource.MAGIC, amount);
    		}

			this.teleportRandomly(this.rand);
    		return false;
		}

    	// 風魔法チェック
    	if (this.checkMagicCyclone(src)) {
    		amount *= 0.075F;
    	}

    	// 光魔法チェック
    	if (this.checkMagicLight(src)) {
    		amount *= 0.3F;
    	}

    	// 爆発魔法チェック
    	if (this.checkMagicExplosion(src)) {
    		amount *= 0.3F;
    	}

		amount = Math.max(0, amount - (float) this.getAddPower(160D));
		if (amount <= 0) { return false; }

		// 体力が半分でダメージカット
		if (this.isHalfHelth()) {
    		amount *= 0.75F;
		}

    	// ガストを代わりに殺す
    	List<EntitySilderGhast> ghastList = this.getEntityList(EntitySilderGhast.class, this, 32D, 32D, 32D);
    	if (!ghastList.isEmpty()) {

    		for (EntitySilderGhast entity : ghastList) {

    			if (!entity.isEntityAlive()) { continue; }

        		entity.setHealth(entity.getHealth() - amount);
        		amount = entity.getHealth();
        		entity.playSound(SoundEvents.ENTITY_GHAST_DEATH, 2F, 1F);

        		if (amount <= 0) {
            		this.playSound(SoundEvents.ENTITY_BLAZE_HURT, 0.5F, 1F);
            		return false;
        		}
    		}
    	}

		if (this.rand.nextInt(4) == 0 && amount >= 1.5F && this.getARMPro() == 0) {
			this.teleportRandomly(this.rand);
		}

		amount = Math.min(amount, this.getCap());
		this.damage += amount;

		if (this.damage >= REQUIR_DAMAGE) {
			if (!this.world.isRemote) {

				for (int i = 0; i < 2; i++) {
					EntitySilderGhast entity = new EntitySilderGhast(this.world);
					double x = this.posX + this.rand.nextDouble() * 4 - this.rand.nextDouble() * 4;
					double z = this.posZ + this.rand.nextDouble() * 4 - this.rand.nextDouble() * 4;

					entity.setPosition(x, this.posY + this.rand.nextDouble() * 2, z);
					entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 2400, 0, true, false));
					entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
					this.world.spawnEntity(entity);
					entity.motionY += 0.1D;
				}
			}

			this.damage -= REQUIR_DAMAGE;
		}

		// 火力キャップを返す
		return super.attackEntityFrom(src, amount);
	}

    // 火力キャップの取得
    public float getCap () {
    	return this.isHalfHelth() ? 15F : 22.5F;
    }

    // 体力の減り具合に応じて火力を上げる
    public double getAddPower (double rate) {
    	return  (this.getMaxHealth() - this.getHealth()) / rate;
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
			this.motionY = 0.115D;
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
			this.dropItem(this.world, this, ItemInit.divine_crystal, this.rand.nextInt(7) + 1);
			this.dropItem(this.world, this, ItemInit.pure_crystal, this.rand.nextInt(3) + 1);
			this.dropItem(this.world, this, ItemInit.cosmic_crystal_shard, this.rand.nextInt(8) + 8);
			this.dropItem(this.world, this, ItemInit.cosmic_crystal, this.rand.nextInt(2));
			this.dropItem(this.world, this, ItemInit.angel_flugel, 1);
			this.dropItem(this.world, this, ItemInit.accebag, 1);
			this.dropItem(this.world, this, ItemInit.mf_magiabottle, this.rand.nextInt(8) + 4);
			this.dropItem(this.world, this, new ItemStack(BlockInit.figurine_ec));
			this.dropItem(this.world, this, ItemInit.magic_deus_force, this.rand.nextInt(6) + 3);
			this.dropItem(this.world, this, ItemInit.devil_cake, this.rand.nextInt(3) + 2);
		}

		super.onDeath(cause);
	}


	public class AIMoveControl extends EntityMoveHelper {

		private EntityElshariaCurious ec;

		public AIMoveControl(EntityElshariaCurious ec) {
			super(ec);
			this.ec = ec;
		}

		public void onUpdateMoveHelper() {

			if (this.action != EntityMoveHelper.Action.MOVE_TO) { return; }

			double d0 = this.posX - this.ec.posX;
			double d1 = this.posY - this.ec.posY;
			double d2 = this.posZ - this.ec.posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			d3 = (double) MathHelper.sqrt(d3);

			if (d3 < this.ec.getEntityBoundingBox().getAverageEdgeLength()) {
				this.action = EntityMoveHelper.Action.WAIT;
				this.ec.motionX *= 0.5D;
				this.ec.motionY *= 0.5D;
				this.ec.motionZ *= 0.5D;
			}

			else {
				this.ec.motionX += d0 / d3 * 0.05D * this.speed;
				this.ec.motionY += d1 / d3 * 0.05D * this.speed;
				this.ec.motionZ += d2 / d3 * 0.05D * this.speed;

				if (this.ec.getAttackTarget() == null) {
					this.ec.rotationYaw = -((float) MathHelper.atan2(this.ec.motionX,
							this.ec.motionZ)) * (180F / (float) Math.PI);
					this.ec.renderYawOffset = this.ec.rotationYaw;
				}

				else {
					double d4 = this.ec.getAttackTarget().posX - this.ec.posX;
					double d5 = this.ec.getAttackTarget().posZ - this.ec.posZ;
					this.ec.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
					this.ec.renderYawOffset = this.ec.rotationYaw;
				}
			}
		}
	}

	public class AIMoveRandom extends EntityAIBase {

		private EntityElshariaCurious entity = EntityElshariaCurious.this;

		public AIMoveRandom() {
			this.setMutexBits(1);
		}

		public boolean shouldExecute() {
			return !this.entity.getMoveHelper().isUpdating() && this.entity.rand.nextFloat() > 0.3F;
		}

		public boolean shouldContinueExecuting() {
			return false;
		}

		public void updateTask() {

			Random rand = this.entity.rand;
			BlockPos pos = this.entity.getBoundOrigin();

			if (pos == null) {
				pos = new BlockPos(this.entity);
			}

			for (int i = 0; i < 3; ++i) {

				BlockPos pos1 = pos.add(rand.nextInt(15) - 7, rand.nextInt(11) - 5, rand.nextInt(15) - 7);

				if (!this.entity.world.isAirBlock(pos1)) { continue; }

				this.entity.moveHelper.setMoveTo((double) pos1.getX() + 0.5D, (double) pos1.getY() + 0.5D, (double) pos1.getZ() + 0.5D, 0.25D);

				if (this.entity.getAttackTarget() == null) {
					this.entity.getLookHelper().setLookPosition((double) pos1.getX() + 0.5D, (double) pos1.getY() + 0.5D, (double) pos1.getZ() + 0.5D, 180.0F, 20.0F);
				}

				break;
			}
		}
	}

	public class EntityAISacredArrow extends EntityAIBase {

		protected int spellWarmup;
		protected int spellCooldown;
		public World world;
		public EntityElshariaCurious entity;

		public EntityAISacredArrow (EntityElshariaCurious entity) {
			this.entity = entity;
			this.world = this.entity.world;
		}

		// AIを実行できるか
		public boolean shouldExecute() {
			return this.getTarget() != null && this.entity.ticksExisted >= this.spellCooldown && this.entity.isHalfHelth();
		}

		// 実行できるか
		public boolean shouldContinueExecuting() {
			return this.entity.getAttackTarget() != null && this.spellWarmup > 0;
		}

		public void startExecuting() {
			this.spellWarmup = this.getCastWarmupTime();
			this.entity.spellTicks = this.getCastingTime();
			this.spellCooldown = this.entity.ticksExisted + this.getCastingInterval();
			this.entity.setARM(1);
		}

		// タスク処理
		public void updateTask() {

			--this.spellWarmup;

			EntityLivingBase target = this.entity.getAttackTarget();
			if (target == null || this.spellWarmup > 130) { return; }

			if (this.spellWarmup == 130) {
				this.entity.setARM(2);
			}

			int range = 16;
			Random rand = this.world.rand;

			for (int i = 0; i < 2; i++) {
				double x = (rand.nextDouble() * range) - rand.nextDouble() * range;
				double y = (rand.nextDouble() * range) - rand.nextDouble() * range;
				double z = (rand.nextDouble() * range) - rand.nextDouble() * range;
				BlockPos pos = new BlockPos(target.posX + x, target.posY + y, target.posZ + z);
				int posY = this.world.canSeeSky(pos) ? 16 : 5;
				this.magicAttack(pos, posY);
			}

			if (this.spellWarmup == 0) {
				this.castSpell();
			}
		}

		public void magicAttack (BlockPos pos, int posY) {
			if (!this.world.isRemote) {

				EntityLightMagic entity = new EntityLightMagic(this.world, this.entity, 1);
				entity.shoot(0, entity.motionY - 0.33F, 0, 0, 0);
				entity.motionY -= 3;
				entity.setPosition(pos.getX(), pos.getY() + posY, pos.getZ());
				entity.setDamage(entity.getDamage() + 6 +  + this.entity.getAddPower(64D));
				entity.isHitDead = true;
				this.world.spawnEntity(entity);
				entity.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.25F, 0.67F);
			}
		}

		// 特殊行動開始
		protected void castSpell() {
			this.entity.setARM(0);
		}

		// キャストタイム
		protected int getCastingTime() {
			return this.spellCooldown;
		}

		// ウォームアップタイム
		protected int getCastWarmupTime() {
			return 200;
		}

		// インターバル
		protected int getCastingInterval() {
			return 1200;
		}

		// ターゲット取得
		public EntityLivingBase getTarget () {
			return this.entity.getAttackTarget();
		}
	}
}
