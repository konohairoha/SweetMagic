package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
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
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityDragonBullet;
import sweetmagic.init.entity.projectile.EntityFlameNova;
import sweetmagic.init.entity.projectile.EntityFrostMagic;
import sweetmagic.init.entity.projectile.EntityLightMagic;
import sweetmagic.init.entity.projectile.EntityLockBullet;
import sweetmagic.init.entity.projectile.EntityMagicCycle;
import sweetmagic.util.SMUtil;

public class EntityZombieKronos extends EntitySpellcasterIllager implements ISMMob {

	public int deathTicks = 0;
	public int tickTime = 0;
	private final BossInfoServer bossInfo = new BossInfoServer(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_12);
	private static final DataParameter<Integer> ACCESIZE = EntityDataManager.<Integer>createKey(EntityZombieKronos.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> ISSPECIAL = EntityDataManager.<Boolean>createKey(EntityZombieKronos.class, DataSerializers.BOOLEAN);
	public int pX = (int) this.posX;
	public int pY = (int) this.posY;
	public int pZ = (int) this.posZ;
	private int normalAttackCoolTime = 0;
	private int specialAttackCoolTime = 0;
	private int chargeTime = 300;
	private int healthCount = 3;
	private int invincibleTime = 0;
	private int shotCount = 0;
	private int specialShotCount = 0;
	private double lastHealth = 1024D;

	// えんちちーの炎ダメージを減らしたかったらここで
	public EntityZombieKronos(World world) {
		super(world);
		this.setSize(0.6F, 2F);
		this.experienceValue = 2000;
		this.isImmuneToFire = true;
		this.setPathPriority(PathNodeType.WATER, 8.0F);
		this.setPathPriority(PathNodeType.LAVA, 8.0F);
		this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
		this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
	}

	// えーあい
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 1.0D));
		this.tasks.addTask(2, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityZombieKronos.class }));
		this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ACCESIZE, Integer.valueOf((int) 0));
		this.dataManager.register(ISSPECIAL, false);
	}

	// えんちちーのステータス設定
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.5D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1024D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(48.0D);
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public ITextComponent getDisplayName() {
		return new TextComponentTranslation("entity.zombiekronos.name");
	}

	public Color getColor () {

		// 体力半分以上
		if (!this.isHalfHelth()) {
			return BossInfo.Color.GREEN;
		}

		// 体力1/4以上
		else if (!this.isQuarterHelth()) {
			return BossInfo.Color.YELLOW;
		}

		// 体力1/4未満
		else {
			return BossInfo.Color.RED;
		}
	}

	public static void registerFixesEvoker(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityZombieKronos.class);
	}

	public void setAcceSize (int size) {
		this.dataManager.set(ACCESIZE, size);
	}

	public int getAcceSize () {
		return this.dataManager.get(ACCESIZE);
	}

	public void setIsSpecial (boolean isSpecial) {
		this.dataManager.set(ISSPECIAL, isSpecial);
	}

	public boolean getIsSpecial () {
		return this.dataManager.get(ISSPECIAL);
	}

	// スポーン時の設定
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance dif, IEntityLivingData living) {
		this.pX = (int) this.posX;
		this.pY = (int) this.posY;
		this.pZ = (int) this.posZ;
		this.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 0));
		this.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 99999, 9));
		this.setHardHealth(this);
		this.setAcceSize(3);
		this.setIsSpecial(false);
    	return super.onInitialSpawn(dif, living);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {
		super.readEntityFromNBT(tags);
		this.bossInfo.setName(this.getDisplayName());
		this.setAcceSize(tags.getInteger("acceSize"));
		this.setIsSpecial(tags.getBoolean("IsSpecial"));
		this.pX = tags.getInteger("pX");
		this.pY = tags.getInteger("pY");
		this.pZ = tags.getInteger("pZ");
		this.deathTicks = tags.getInteger("deathTicks");
		this.tickTime = tags.getInteger("tickTime");
		this.normalAttackCoolTime = tags.getInteger("normalAttackCoolTime");
		this.specialAttackCoolTime = tags.getInteger("specialAttackCoolTime");
		this.invincibleTime = tags.getInteger("invincibleTime");
		this.shotCount = tags.getInteger("shotCount");
		this.lastHealth = tags.getDouble("lastHealth");
		this.chargeTime = tags.getInteger("chargeTime");
		this.healthCount = tags.getInteger("healthCount");
		this.specialShotCount = tags.getInteger("specialShotCount");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);
		tags.setInteger("acceSize", this.getAcceSize());
		tags.setBoolean("IsSpecial", this.getSpecial());
		tags.setInteger("pX", this.pX);
		tags.setInteger("pY", this.pY);
		tags.setInteger("pZ", this.pZ);
		tags.setInteger("deathTicks", this.deathTicks);
		tags.setInteger("tickTime", this.tickTime);
		tags.setInteger("normalAttackCoolTime", this.normalAttackCoolTime);
		tags.setInteger("specialAttackCoolTime", this.specialAttackCoolTime);
		tags.setInteger("invincibleTime", this.invincibleTime);
		tags.setInteger("shotCount", this.shotCount);
		tags.setDouble("lastHealth", this.lastHealth);
		tags.setInteger("chargeTime", this.chargeTime);
		tags.setInteger("healthCount", this.healthCount);
		tags.setInteger("specialShotCount", this.specialShotCount);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
		if (this.deathTicks > 0) { return; }

		if (this.invincibleTime > 0) {
			this.invincibleTime--;
		}

		this.tickTime++;
		if (this.tickTime % 2 != 0) { return; }

		if (this.tickTime % 8 != 0) {
			if (this.world instanceof WorldServer) {
				int dayTime = 24000;
	            WorldServer sever = this.world.getMinecraftServer().getWorld(0);
	            long day = (sever.getWorldTime() / dayTime);
				this.world.setWorldTime(12500 + (day * dayTime));
			}

			WorldInfo info = world.getWorldInfo();

			if (info.isRaining()) {
				info.setRainTime(0);
				info.setThunderTime(0);
				info.setRaining(false);
			}
		}

		double health = this.getHealth();

		// 体力に変化があった場合
		if (health != this.lastHealth) {

			this.lastHealth = health;

			int acceSize = this.getAcceSize();

			if (acceSize > 0) {

				EntityLivingBase entity = null;

				// 854以下になった場合
				if (health <= 854D && acceSize == 3) {
					this.setAcceSize(2);
					EntityWitchMadameVerre witch = new EntityWitchMadameVerre(this.world);
					witch.setAlly(true);
					entity = witch;
					this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1F, 1.2F);
				}

				// 684以下になった場合
				else if (health <= 684D && acceSize == 2) {
					this.setAcceSize(1);
					EntityIfritVerre witch = new EntityIfritVerre(this.world);
					witch.setAlly(true);
					entity = witch;
					this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1F, 1.2F);
				}

				// 684以下になった場合
				else if (health <= 514D && acceSize == 1) {
					this.setAcceSize(0);
					EntityWindineVerre witch = new EntityWindineVerre(this.world);
					witch.setAlly(true);
					entity = witch;
					this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1F, 1.2F);
				}

				// ボス召喚
				if (entity != null && !this.world.isRemote) {

					int x = this.pX + this.rand.nextInt(24) - this.rand.nextInt(24);
					int y = this.pY + 2;
					int z = this.pZ + this.rand.nextInt(24) - this.rand.nextInt(24);
					BlockPos pos = new BlockPos(x, y, z);
					Block block = this.world.getBlockState(pos).getBlock();
					Block upBlock = this.world.getBlockState(pos.up()).getBlock();

					if (block != Blocks.AIR || upBlock != Blocks.AIR) {
						x = this.pX;
						z = this.pZ;
					}

					entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(256D);
					entity.setHealth(64F);
					entity.setPosition(x, y, z);
					entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
					entity.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 9999, 0));
					entity.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 99999, 9));
					this.world.spawnEntity(entity);
					SMUtil.tameAIAnger((EntityLiving) entity, this);
				}
			}
		}
	}

	@Override
	protected void updateAITasks() {

		if (this.deathTicks > 0) { return; }

		super.updateAITasks();
		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());

		if (this.isHalfHelth()) {
			this.bossInfo.setColor(this.getColor());
		}

		this.motionY *= 0.25D;
		EntityLivingBase entity = this.getAttackTarget();

		if (entity != null) {

			double d4 = entity.posX - this.posX;
			double d5 = entity.posZ - this.posZ;
			this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
			this.renderYawOffset = this.rotationYaw;

			if (this.posY < entity.posY || this.posY < entity.posY + 2D) {
				if (this.motionY < 0D) { this.motionY = 0D; }
				this.motionY += (0.5D - this.motionY) * 0.25D;
			}
		}

		// 通常攻撃
		this.normalAttackCoolTime--;
		if (this.normalAttackCoolTime <= 0) {
			this.nomalAttack();
		}

		// 特殊攻撃
		this.specialAttackCoolTime--;
		if (this.specialAttackCoolTime <= 0) {
			this.specialAttack();
		}
	}

	// 通常攻撃
	public void nomalAttack () {

		// 体力半分以上なら
		if (!this.isHalfHelth()) {

			this.shotMagic(this.getAcceSize());

			if (this.shotCount >= 8) {
				this.shotCount = 0;
				this.normalAttackCoolTime = 260;
			}
		}

		// 体力1/4以上なら
		else if (!this.isQuarterHelth()) {
			this.shotLockBullet();

			if (this.shotCount >= 6) {
				this.shotCount = 0;
				this.normalAttackCoolTime = 360;
			}
		}

		// 体力1/4未満でリフレッシュエフェクトが発動中
		else {
			this.shotLockBullet();

			if (this.shotCount >= 10) {
				this.shotCount = 0;
				this.normalAttackCoolTime = 300;
			}
		}
	}

	// 特殊攻撃
	public void specialAttack () {

		// 体力半分以上なら
		if (!this.isHalfHelth()) { }

		// 体力1/4以上なら
		else if (!this.isQuarterHelth()) {
			this.summonMagicCycle();
		}

		// 体力1/4未満でリフレッシュエフェクトが発動中
		else {

			this.setIsSpecial(true);

			if (this.chargeTime >= 0) {
				this.chargeTime--;
				return;
			}

			this.shotDragonBullet();

			if (this.specialShotCount >= 10) {
				this.specialShotCount = 0;
				this.specialAttackCoolTime = 400;
				this.chargeTime = 300;
				this.setIsSpecial(false);
			}
		}
	}

	// 射撃魔法
	public void shotMagic (int acceSize) {

		// 誰もいないなら終了
		List<EntityPlayer> entityList = this.getPlayerList();
		if (entityList.isEmpty()) { return; }

		// プレイヤー分回す
		for (EntityPlayer player : entityList) {

			// ターゲットがターゲットを同じかクリエイティブなら次へ
			if (player == this.getAttackTarget() || player.isCreative()) { continue; }

			EntityBaseMagicShot magic = this.getMagic(acceSize);

			if (magic != null) {
		        double x = player.posX - this.posX;
		        double y = player.getEntityBoundingBox().minY - player.height / 2  - this.posY + 1D;
				double z = player.posZ - this.posZ;
				double xz = (double) MathHelper.sqrt(x * x + z * z);
		        magic.mobPower = 5;
		        magic.shoot(x, y - xz * 0.015D, z, 3.5F, 0);	// 射撃速度
		        magic.setDamage(magic.getDamage() + 8);
				this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
		        this.world.spawnEntity(magic);
			}
		}

		this.normalAttackCoolTime += 15;
		this.shotCount++;
	}

	// 魔法の取得
	public EntityBaseMagicShot getMagic (int acceSize) {

		EntityBaseMagicShot entity = null;

		switch (acceSize) {
		case 1:
			entity = new EntityFrostMagic(this.world, this, ItemStack.EMPTY, true);
			break;
		case 2:
			entity = new EntityFlameNova(this.world, this, ItemStack.EMPTY);
			break;
		case 3:
			entity = new EntityLightMagic(this.world, this, ItemStack.EMPTY);
			entity.setDamage(entity.getDamage() + 8);
			break;
		}

		return entity;
	}

	// プレイヤーリストの取得
	public List<EntityPlayer> getPlayerList () {
		return this.getEntityList(EntityPlayer.class, this, 64D, 64D, 64D);
	}

	// 自動追尾攻撃
	public void shotLockBullet () {

		// 誰もいないなら終了
		List<EntityPlayer> entityList = this.getPlayerList();
		if (entityList.isEmpty()) { return; }

		for (EntityPlayer player : entityList) {

			if (player.isCreative()) { continue; }

			EntityLockBullet entity = new EntityLockBullet(this.world, this, player, ItemStack.EMPTY);
	        double x = player.posX - this.posX;
	        double y = player.getEntityBoundingBox().minY - player.height / 2  - this.posY + 1D;
			double z = player.posZ - this.posZ;
			double xz = (double) MathHelper.sqrt(x * x + z * z);
			entity.shoot(x, y - xz * 0.015D, z, 3F, 0);	// 射撃速度
			entity.setDamage(entity.getDamage() + 15);
			this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
	        this.world.spawnEntity(entity);
		}

		this.normalAttackCoolTime += 16;
		this.shotCount++;
	}

	// 魔法陣展開
	public void summonMagicCycle () {

		// 誰もいないなら終了
		List<EntityPlayer> entityList = this.getPlayerList();
		if (entityList.isEmpty()) { return; }

		for (EntityPlayer player : entityList) {

			if (player.isCreative()) { continue; }

			EntityMagicCycle entity = new EntityMagicCycle(this, 90, 132, 255, 7.5F, 0, 600, 5);
			entity.setPosition(player.posX + 0.5D, player.posY + 0.5D, player.posZ + 0.5D);
			this.world.spawnEntity(entity);

			for (int i = 0; i < 2; i++) {
				EntityEvilCrystal crystal = new EntityEvilCrystal(this.world);
				crystal.setData(this.rand.nextInt(4) + 1);
				double xRand = this.rand.nextInt(20) - this.rand.nextInt(20) + 0.5D;
				double zRand = this.rand.nextInt(20) - this.rand.nextInt(20) + 0.5D;
				crystal.setPosition(player.posX + xRand, player.posY + 0.5D, player.posZ + zRand);
				crystal.setAttackTarget(player);
				this.world.spawnEntity(crystal);
			}
		}

		this.specialAttackCoolTime = 500 + this.rand.nextInt(200) - this.rand.nextInt(200);
	}

	// 焼却砲
	public void shotDragonBullet () {

		// 誰もいないなら終了
		List<EntityPlayer> entityList = this.getPlayerList();
		if (entityList.isEmpty()) { return; }

		for (EntityPlayer player : entityList) {

			if (player.isCreative()) { continue; }

			EntityDragonBullet entity = new EntityDragonBullet(this.world, this, player, ItemStack.EMPTY);
	        double x = player.posX - this.posX;
	        double y = player.getEntityBoundingBox().minY - player.height / 2  - this.posY + 1D;
			double z = player.posZ - this.posZ;
			double xz = (double) MathHelper.sqrt(x * x + z * z);
			entity.shoot(x, y - xz * 0.015D, z, 3F, 0);	// 射撃速度
			entity.setDamage(entity.getDamage() + 30);
			entity.setPosition(entity.posX, entity.posY + 1.25D, entity.posZ);
			this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
	        this.world.spawnEntity(entity);
		}

		this.specialAttackCoolTime = 8;
		this.specialShotCount++;
	}

	@Override
	protected void onDeathUpdate() {

		++this.deathTicks;
		this.motionY = -1D;

		if (this.deathTicks <= 1) {

			List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, this, 32D, 32D, 32D);

			for (EntityLivingBase entity : entityList) {

				if (entity.isNonBoss() || !(entity instanceof ISMMob) || entity == this) { continue; }
				entity.isDead = true;
			}
		}

		else if (this.deathTicks >= 10 && this.deathTicks < 290) {

			this.world.setWorldTime((this.world.getWorldTime() + 36));
			this.bossInfo.setPercent(0);

			if (this.deathTicks % 11 == 0) {
			    this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5F, 1F);
			}

			if (this.deathTicks % 5 == 0) {
				float f = (this.rand.nextFloat() - 0.5F) * 3.0F;
				float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F;
				float f2 = (this.rand.nextFloat() - 0.5F) * 3.0F;
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f, this.posY + 2D + f1, this.posZ + f2, 0D, 0D, 0D);
			}
		}

		else if (this.deathTicks >= 300) {

		    this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5F, 1F);
			float f = (this.rand.nextFloat() - 0.5F) * 3.0F;
			float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F;
			float f2 = (this.rand.nextFloat() - 0.5F) * 3.0F;
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f, this.posY + 2D + f1, this.posZ + f2, 0D, 0D, 0D);

			if (!this.world.isRemote) {

				this.setDead();

				EntityFairySkyDwarfKronos entity = new EntityFairySkyDwarfKronos(this.world);
				entity.setPosition(this.posX, this.posY, this.posZ);
				entity.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 0));
				entity.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 99999, 9));
				this.world.spawnEntity(entity);
			}
		}
	}

	// エーテルバリアーなどのエフェクトを表示するかどうか
	public boolean isRenderEffect () {
		return false;
	}

	public void fall(float dis, float dama) { }

	@Override
	public boolean attackEntityFrom(DamageSource src, float amount) {

		if ( ( this.checkBossDamage(src) && this.isMindControl(this) ) || this.invincibleTime > 0) {
			return false;
		}

		if (!this.isSMDamage(src) && src.getImmediateSource() instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase) src.getImmediateSource();
			entity.attackEntityFrom(DamageSource.MAGIC, amount);
			return false;
		}

    	// 風魔法チェック
    	if (this.checkMagicCyclone(src)) {
    		amount = Math.min(1F, amount);
    	}

    	// 光魔法チェック
    	if (this.checkMagicLight(src)) {
    		amount *= 0.3F;
    	}

		float cap = 15F;

		if (this.isQuarterHelth()) {
			cap = 8F;
		}

		else if (this.isHalfHelth()) {
			cap = 12F;
		}

		// 体力半分以下からテレポート
		if (this.isHalfHelth()) {
			this.teleportRandomly(this.rand);
		}

		// 体力が1/4以下なら一時的に無敵
		if (this.isQuarterHelth()) {
			this.invincibleTime = 3;
		}

		super.attackEntityFrom(src, Math.min(amount, cap));
		return true;
	}

	private boolean teleportRandomly(Random rand) {
		double x = this.pX + (rand.nextDouble() - 0.5) * 8;
		double y = this.pY + (double) (rand.nextInt(12) - 4);
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
			float f2 = (float) this.posY + 0.25F + this.rand.nextFloat() * 1.5F;
			float f3 = (float) this.posZ - 0.5F + this.rand.nextFloat();
			this.world.spawnParticle(EnumParticleTypes.PORTAL, f1, f2, f3, 0, 0, 0);
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_EVOCATION_ILLAGER_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.EVOCATION_ILLAGER_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damage) {
		return SoundEvents.ENTITY_EVOCATION_ILLAGER_HURT;
	}

	@Override
	protected SoundEvent getSpellSound() {
		return SMSoundEvent.HORAMAGIC;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public void setInWeb() {}

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
	public boolean isNonBoss() {
		return false;
	}

	// 体力半分かどうか
	public boolean isHalfHelth () {
		return this.getHealth() < this.getMaxHealth() / 2;
	}

	// 体力4分の1かどうか
	public boolean isQuarterHelth () {
		return this.getHealth() < this.getMaxHealth() / 4;
	}
}
