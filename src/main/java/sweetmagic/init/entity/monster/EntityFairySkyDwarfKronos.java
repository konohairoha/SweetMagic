package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.particle.Particle;
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
import sweetmagic.client.particle.ParticleTwilightlight;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityCyclonMagic;
import sweetmagic.init.entity.projectile.EntityDigMagic;
import sweetmagic.init.entity.projectile.EntityDragonBullet;
import sweetmagic.init.entity.projectile.EntityFireMagic;
import sweetmagic.init.entity.projectile.EntityFrostMagic;
import sweetmagic.init.entity.projectile.EntityGravityMagic;
import sweetmagic.init.entity.projectile.EntityLightMagic;
import sweetmagic.init.entity.projectile.EntityLockBullet;
import sweetmagic.util.ParticleHelper;

public class EntityFairySkyDwarfKronos extends EntitySpellcasterIllager implements ISMMob {

	public int deathTicks = 0;
	public int tickTime = 0;
	private int invincibleTime = 0;
	private int normalFairykCoolTime = 450;
	private int specialFairykCoolTime = 0;
	private int normalSkyCoolTime = 525;
	private int specialSkyCoolTime = 0;
	private int normalDwarfCoolTime = 300;
	private int specialDwarfCoolTime = 0;
	private int normalKronosCoolTime = 50;
	private int specialKronosCoolTime = 0;

	private int normalDwarfShotCount = 0;
	private int normalKronosShotCount = 0;
	private int specialKronosShotCount = 0;

	public int pX = (int) this.posX;
	public int pY = (int) this.posY;
	public int pZ = (int) this.posZ;
	private double lastHealth = 512D;
	private final BossInfoServer bossInfo = new BossInfoServer(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_6);
	private static final DataParameter<Boolean> ISSPECIAL = EntityDataManager.<Boolean>createKey(EntityFairySkyDwarfKronos.class, DataSerializers.BOOLEAN);

	// えんちちーの炎ダメージを減らしたかったらここで
	public EntityFairySkyDwarfKronos(World world) {
		super(world);
		this.setSize(0.9F, 3F);
		this.experienceValue = 4000;
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
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityFairySkyDwarfKronos.class }));
		this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ISSPECIAL, false);
	}

	// えんちちーのステータス設定
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(512D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(48D);
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
    	return super.onInitialSpawn(dif, living);
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public ITextComponent getDisplayName() {
		return new TextComponentTranslation("entity.fairyskydwarfkronos.name");
	}

	public Color getColor () {
		return !this.isHalfHelth() ? BossInfo.Color.BLUE : BossInfo.Color.RED;
	}

	public static void registerFixesEvoker(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityFairySkyDwarfKronos.class);
	}

	@Override
	protected SoundEvent getSpellSound() {
		return null;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	// エーテルバリアーなどのエフェクトを表示するかどうか
	public boolean isRenderEffect () {
		return false;
	}

	public void setSpecial (boolean isSpecial) {
		this.dataManager.set(ISSPECIAL, isSpecial);
	}

	public boolean getSpecial () {
		return this.dataManager.get(ISSPECIAL);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {
		super.readEntityFromNBT(tags);
		this.bossInfo.setName(this.getDisplayName());
		this.pX = tags.getInteger("pX");
		this.pY = tags.getInteger("pY");
		this.pZ = tags.getInteger("pZ");
		this.tickTime = tags.getInteger("tickTime");
		this.invincibleTime = tags.getInteger("invincibleTime");
		this.lastHealth = tags.getDouble("lastHealth");
		this.normalFairykCoolTime = tags.getInteger("normalFairykCoolTime");
		this.specialFairykCoolTime = tags.getInteger("specialFairykCoolTime");
		this.normalSkyCoolTime = tags.getInteger("normalSkyCoolTime");
		this.specialSkyCoolTime = tags.getInteger("specialSkyCoolTime");
		this.normalDwarfCoolTime = tags.getInteger("normalDwarfCoolTime");
		this.specialDwarfCoolTime = tags.getInteger("specialDwarfCoolTime");
		this.normalKronosCoolTime = tags.getInteger("normalKronosCoolTime");
		this.normalKronosCoolTime = tags.getInteger("normalKronosCoolTime");
		this.specialKronosCoolTime = tags.getInteger("specialKronosCoolTime");
		this.normalDwarfShotCount = tags.getInteger("normalDwarfShotCount");
		this.normalKronosShotCount = tags.getInteger("normalKronosShotCount");
		this.specialKronosShotCount = tags.getInteger("specialKronosShotCount");
		this.setSpecial(tags.getBoolean("IsSpecial"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);
		tags.setInteger("pX", this.pX);
		tags.setInteger("pY", this.pY);
		tags.setInteger("pZ", this.pZ);
		tags.setInteger("tickTime", this.tickTime);
		tags.setInteger("invincibleTime", this.invincibleTime);
		tags.setDouble("lastHealth", this.lastHealth);
		tags.setInteger("normalFairykCoolTime", this.normalFairykCoolTime);
		tags.setInteger("specialFairykCoolTime", this.specialFairykCoolTime);
		tags.setInteger("normalSkyCoolTime", this.normalSkyCoolTime);
		tags.setInteger("specialSkyCoolTime", this.specialSkyCoolTime);
		tags.setInteger("normalDwarfCoolTime", this.normalDwarfCoolTime);
		tags.setInteger("specialDwarfCoolTime", this.specialDwarfCoolTime);
		tags.setInteger("normalKronosCoolTime", this.normalKronosCoolTime);
		tags.setInteger("specialKronosCoolTime", this.specialKronosCoolTime);
		tags.setInteger("normalDwarfShotCount", this.normalDwarfShotCount);
		tags.setInteger("normalKronosShotCount", this.normalKronosShotCount);
		tags.setInteger("specialKronosShotCount", this.specialKronosShotCount);
		tags.setBoolean("IsSpecial", this.getSpecial());
	}

	@Override
	public void onUpdate() {

		super.onUpdate();

		this.tickTime++;

		if (this.ticksExisted == 1) {
			this.specialFairykCoolTime = this.setCoolTime(760 + this.rand.nextInt(200));
			this.specialSkyCoolTime = this.setCoolTime(1100 + this.rand.nextInt(400));
			this.specialDwarfCoolTime = this.setCoolTime(800 + this.rand.nextInt(500));
			this.specialKronosCoolTime = this.setCoolTime(400 + this.rand.nextInt(200));
		}

		if (this.invincibleTime > 0) {
			this.invincibleTime--;
		}

		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());

		if (this.isHalfHelth()) {
			this.bossInfo.setColor(this.getColor());
		}

		if (this.tickTime % 2 != 0) { return; }

		if (this.world instanceof WorldServer) {
			WorldServer sever = this.world.getMinecraftServer().getWorld(0);
			this.world.setWorldTime(sever.getWorldTime() + 16);
		}

		if (this.tickTime % 20 != 0) {

			WorldInfo info = world.getWorldInfo();

			if (info.isRaining()) {
				info.setRainTime(0);
				info.setThunderTime(0);
				info.setRaining(false);
			}
		}

		if (this.tickTime % 100 != 0) {

			long worldTime = this.world.getWorldTime() % 24000;

			// 昼の場合
			if (worldTime < 10000) {
				this.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 500, 3));
			}

			// 夕方の場合
			else if (worldTime >= 10000 && worldTime < 14000) {
				this.addPotionEffect(new PotionEffect(PotionInit.increased_cool_time, 500, 3));
			}

			// 夜の場合
			else {
				this.addPotionEffect(new PotionEffect(PotionInit.shadow, 500, 3));
			}

			// 一定距離離れているなら初期座標へテレポート
			if (this.checkDistance()) {
				this.teleportRandomly(this.rand);
			}
		}
	}

	public boolean checkDistance () {
		return this.getDistance(this.pX, this.pY, this.pZ) > 48D;
	}

	private boolean teleportRandomly(Random rand) {
		double x = this.pX + (rand.nextDouble() - 0.5D) * 8D;
		double y = this.pY;
		double z = this.pZ + (rand.nextDouble() - 0.5) * 8D;
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

	@Override
	protected void updateAITasks() {

		if (this.deathTicks > 0) { return; }

		super.updateAITasks();
		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());

		if (this.isHalfHelth()) {
			this.bossInfo.setColor(this.getColor());
		}

		EntityLivingBase entity = this.getAttackTarget();
		if (entity == null) { return; }

		// 通常攻撃
		this.normalAttack(this.isHalfHelth());

		// 特殊攻撃
		this.specialAttack();
	}

	// 通常攻撃
	public void normalAttack (boolean isHalf) {

		// エンシェントフェアリー通常攻撃
		if (this.normalFairykCoolTime-- <= 0) {
			this.summonPixieVex(isHalf);
			this.normalFairykCoolTime = this.setCoolTime(400 + this.rand.nextInt(250) + this.rand.nextInt(250));
		}

		// エルシャリアキュリオス通常攻撃
		if (this.normalSkyCoolTime-- <= 0) {
			this.summonSilderGhast(isHalf);
			this.normalSkyCoolTime = this.setCoolTime(600 + this.rand.nextInt(300) + this.rand.nextInt(300));
		}

		// リペアキット通常攻撃
		if (this.normalDwarfCoolTime-- <= 0) {
			this.gadgetAttack(isHalf);

			this.normalDwarfCoolTime = 16;
			this.normalDwarfShotCount--;

			if (this.normalDwarfShotCount >= 6) {
				this.normalDwarfShotCount = 0;
				this.normalDwarfCoolTime = this.setCoolTime(250 + this.rand.nextInt(200) + this.rand.nextInt(200));
			}
		}

		// クロノス通常攻撃
		if (this.normalKronosCoolTime-- <= 0) {
			this.shotLockBullet(isHalf);
			this.normalKronosCoolTime = 14;
			this.normalKronosShotCount++;

			if (this.normalKronosShotCount >= 8) {
				this.normalKronosShotCount = 0;
				this.normalKronosCoolTime = this.setCoolTime(300 + this.rand.nextInt(200) + this.rand.nextInt(200));
			}
		}
	}

	// 特殊攻撃
	public void specialAttack() {

		// エンシェントフェアリー通常攻撃
		if (this.specialFairykCoolTime-- <= 240) {

			// チャージ中
			boolean isCharge = this.specialFairykCoolTime > 0;

			// 毒攻撃
			this.poisonFog(isCharge);

			if (!isCharge) {
				this.specialFairykCoolTime = this.setCoolTime(760 + this.rand.nextInt(200));
			}
		}

		// エルシャリアキュリオス通常攻撃
		if (this.specialSkyCoolTime-- <= 200) {

			// チャージ中
			boolean isCharge = this.specialFairykCoolTime > 0;

			// セイクリッドアロー
			this.sacredArrow(isCharge);

			if (!isCharge) {
				this.specialSkyCoolTime = this.setCoolTime(1100 + this.rand.nextInt(400));
			}
		}

		// リペアキット通常攻撃
		if (this.specialDwarfCoolTime-- <= 0) {
			this.summonEvilCrystal();
			this.specialDwarfCoolTime = this.setCoolTime(800 + this.rand.nextInt(500));
		}

		// クロノス通常攻撃
		if (this.specialKronosCoolTime-- <= 300) {

			// チャージ中
			boolean isCharge = this.specialKronosCoolTime > 0;

			if (isCharge) {
				if (!this.getSpecial()) {
					this.setSpecial(true);
				}
				return;
			}

			// ドラゴンバレット砲
			this.shotDragonBullet();
			this.specialKronosShotCount++;

			this.specialKronosCoolTime = 10;

			if (this.specialKronosShotCount >= 12) {
				this.setSpecial(false);
				this.specialKronosShotCount = 0;
				this.specialKronosCoolTime = this.setCoolTime(500 + this.rand.nextInt(300));
			}
		}
	}

	public void summonPixieVex (boolean isHalf) {

		// 誰もいないなら終了
		List<EntityPlayer> entityList = this.getPlayerList();
		if (entityList.isEmpty()) { return; }

		int count = isHalf ? (this.rand.nextInt(2) + 1) : 1;

		for (EntityPlayer player : entityList) {

			for (int i = 0; i < count; i++) {
				EntityPixieVex entity = new EntityPixieVex(this.world);
				double x = this.posX + this.rand.nextDouble() * 4 - this.rand.nextDouble() * 4;
				double z = this.posZ + this.rand.nextDouble() * 4 - this.rand.nextDouble() * 4;

				entity.setPosition(x, this.posY + this.rand.nextDouble() * 2, z);
				entity.setData(this.rand.nextInt(3));
				entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 1600, 0, true, false));
				entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
				this.world.spawnEntity(entity);
				entity.setAttackTarget(player);
			}
		}
	}

	public void summonSilderGhast (boolean isHalf) {

		// 誰もいないなら終了
		List<EntityPlayer> entityList = this.getPlayerList();
		if (entityList.isEmpty()) { return; }

		int count = isHalf ? (this.rand.nextInt(2) + 1) : 1;

		for (EntityPlayer player : entityList) {

			for (int i = 0; i < count; i++) {
				EntitySilderGhast entity = new EntitySilderGhast(this.world);
				double x = this.posX + this.rand.nextDouble() * 4 - this.rand.nextDouble() * 4;
				double z = this.posZ + this.rand.nextDouble() * 4 - this.rand.nextDouble() * 4;

				entity.setPosition(x, this.posY + this.rand.nextDouble() * 2, z);
				entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 2400, 0, true, false));
				entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
				this.world.spawnEntity(entity);
				entity.motionY += 0.1D;
				entity.setAttackTarget(player);
			}
		}
	}

	// ライフル攻撃
	public void gadgetAttack (boolean isHalf) {

		// 誰もいないなら終了
		List<EntityPlayer> entityList = this.getPlayerList();
		if (entityList.isEmpty()) { return; }

		int count = isHalf ? 2 : 1;

		for (EntityPlayer player : entityList) {

			if (player.isCreative()) { continue; }

			for (int i = 0; i < count; i++) {
				EntityBaseMagicShot magic = this.getShot();
				if (magic == null) { continue; }

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

	// ロックバレット射撃
	public void shotLockBullet (boolean isHalf) {

		// 誰もいないなら終了
		List<EntityPlayer> entityList = this.getPlayerList();
		if (entityList.isEmpty()) { return; }

		int dame = isHalf ? 20 : 10;
		float speed = isHalf ? 4.25F : 2.5F;

		for (EntityPlayer player : entityList) {

			if (player.isCreative()) { continue; }

			EntityLockBullet entity = new EntityLockBullet(this.world, this, player, ItemStack.EMPTY);
	        double x = player.posX - this.posX;
	        double y = player.getEntityBoundingBox().minY - player.height / 2  - this.posY + 1D;
			double z = player.posZ - this.posZ;
			double xz = (double) MathHelper.sqrt(x * x + z * z);
			entity.shoot(x, y - xz * 0.015D, z, speed, 0);	// 射撃速度
			entity.setDamage(entity.getDamage() + dame);
			this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
	        this.world.spawnEntity(entity);
		}
	}

	// 毒攻撃
	public void poisonFog (boolean isCharge) {

		if (isCharge) {
			BlockPos pos = this.getPosition();
			Random rand = this.world.rand;
			float chance = this.isRender() ? 0.075F : 0.025F;

			for (BlockPos p : BlockPos.getAllInBox(pos.add(-24, 0, -24), pos.add(24, 6, 24))) {
				if (rand.nextFloat() > chance) { continue; }

				ParticleHelper.spawnParticle(this.world, p, EnumParticleTypes.SPELL_WITCH, 1, 0.075D);
			}
		}

		else {

			List<EntityPlayer> playerList = this.getEntityList(EntityPlayer.class, this, 24D, 6D, 24D);

			for (EntityPlayer player : playerList) {

				if (player.isCreative() || player.isSpectator() || player.getHealth() <= 1F) { continue; }

				float health = player.getHealth() * ( player.isPotionActive(PotionInit.refresh_effect) ? 0.33F : 0.5F ) ;
				player.setHealth(health);
				player.attackEntityFrom(DamageSource.MAGIC, 1F);
			}
		}
	}

	// セイクリッドアロー
	public void sacredArrow (boolean isCharge) {

		if (isCharge) {

			if (this.world.isRemote) {

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

		else {

			EntityLivingBase target = this.getAttackTarget();
			if (target == null) { return; }

			int range = 24;
			Random rand = this.world.rand;

			for (int i = 0; i < 3; i++) {
				double x = (rand.nextDouble() * range) - rand.nextDouble() * range;
				double y = (rand.nextDouble() * range) - rand.nextDouble() * range;
				double z = (rand.nextDouble() * range) - rand.nextDouble() * range;
				BlockPos pos = new BlockPos(target.posX + x, target.posY + y, target.posZ + z);
				this.magicAttack(pos, 24);
			}
		}
	}

	// 光魔法攻撃
	public void magicAttack (BlockPos pos, int posY) {
		if (!this.world.isRemote) {

			EntityLightMagic entity = new EntityLightMagic(this.world, this, 1);
			entity.shoot(0, entity.motionY - 0.33F, 0, 0, 0);
			entity.motionY -= 3;
			entity.setPosition(pos.getX(), pos.getY() + posY, pos.getZ());
			entity.setDamage(entity.getDamage() + 20);
			entity.isHitDead = true;
			this.world.spawnEntity(entity);
			entity.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.25F, 0.67F);
		}
	}

	// クリスタル召喚
	public void summonEvilCrystal () {

		// 誰もいないなら終了
		List<EntityPlayer> entityList = this.getPlayerList();
		if (entityList.isEmpty()) { return; }

		for (EntityPlayer player : entityList) {

			if (player.isCreative()) { continue; }

			int count = this.rand.nextInt(2) + 1;

			for (int i = 0; i < count; i++) {
				EntityEvilCrystal crystal = new EntityEvilCrystal(this.world);
				crystal.setData(this.rand.nextInt(4) + 1);
				double xRand = this.rand.nextInt(20) - this.rand.nextInt(20) + 0.5D;
				double zRand = this.rand.nextInt(20) - this.rand.nextInt(20) + 0.5D;
				crystal.setPosition(player.posX + xRand, player.posY + 0.5D, player.posZ + zRand);
				crystal.setAttackTarget(player);
				this.world.spawnEntity(crystal);
			}
		}
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
			entity.setPosition(entity.posX, entity.posY + 4D, entity.posZ);
			this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
	        this.world.spawnEntity(entity);
		}
	}

	public float getRandFloat () {
		return this.rand.nextFloat() * 1.5F;
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
			this.dropItem(this.world, this, BlockInit.figurine_zk, 1);
			this.dropItem(this.world, this, ItemInit.extension_ring, 1);
			this.dropItem(this.world, this, ItemInit.mf_magiabottle, this.rand.nextInt(10) + 6);
			this.dropItem(this.world, this, BlockInit.magiaflux_block, this.rand.nextInt(4) + 2);
			this.dropItem(this.world, this, ItemInit.cosmic_crystal_shard, this.rand.nextInt(10) + 16);
			this.dropItem(this.world, this, ItemInit.cosmic_crystal, this.rand.nextInt(2) + 1);
			this.dropItem(this.world, this, ItemInit.magic_deus_force, this.rand.nextInt(16) + 8);
			this.dropItem(this.world, this, ItemInit.magic_cosmic_force, this.rand.nextInt(8) + 3);
			this.dropItem(this.world, this, ItemInit.magic_creative, this.rand.nextInt(4) + 2);
		}
    }

	@Override
	protected void onDeathUpdate() {

		++this.deathTicks;
		this.motionY = -1D;

		if (this.deathTicks >= 10 && this.deathTicks < 200) {

			this.world.setWorldTime((this.world.getWorldTime() + 45));
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

		else if (this.deathTicks >= 200) {

		    this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5F, 1F);
			float f = (this.rand.nextFloat() - 0.5F) * 3.0F;
			float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F;
			float f2 = (this.rand.nextFloat() - 0.5F) * 3.0F;
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f, this.posY + 2D + f1, this.posZ + f2, 0D, 0D, 0D);

			if (!this.world.isRemote) {
				this.setDead();
			}
		}
	}

	public void fall(float dis, float dama) { }

	@Override
	public boolean attackEntityFrom(DamageSource src, float amount) {

		if ( ( this.checkBossDamage(src) && this.isMindControl(this) ) || this.invincibleTime > 0) {
			return false;
		}

		if (amount <= 2F) { return super.attackEntityFrom(src, 0); }

    	// 風魔法チェック
    	if (this.checkMagicCyclone(src)) {
    		amount = Math.min(0.25F, amount);
    	}

    	// 光魔法チェック
    	if (this.checkMagicLight(src)) {
    		amount *= 0.3F;
    	}

		float cap = this.isHalfHelth() ? 8F : 10F;
		this.invincibleTime = 1;

		return super.attackEntityFrom(src, Math.min(amount, cap));
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
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public void setInWeb() {}

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
	}

	// 体力半分かどうか
	public boolean isHalfHelth () {
		return this.getHealth() < this.getMaxHealth() / 2;
	}

	// プレイヤーリストの取得
	public List<EntityPlayer> getPlayerList () {
		return this.getEntityList(EntityPlayer.class, this, 64D, 64D, 64D);
	}

	// クールタイムの計算
	public int setCoolTime (int time) {
		return (int) (time * ( this.isPotionActive(PotionInit.increased_cool_time) ? 0.6F : 1F ));
	}
}
