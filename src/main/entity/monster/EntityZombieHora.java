package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
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
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityCyclonMagic;
import sweetmagic.init.entity.projectile.EntityRockBlast;
import sweetmagic.util.SMUtil;

public class EntityZombieHora extends EntitySpellcasterIllager implements ISMMob, IRangedAttackMob {

	private int deathTicks = 0;
	private int tickTime = 0;
	private final BossInfoServer bossInfo = new BossInfoServer(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_10);
	public EntityLivingBase suportEntity1;
	public EntityLivingBase suportEntity2;
	public int pX = (int) this.posX;
	public int pY = (int) this.posY;
	public int pZ = (int) this.posZ;

	// えんちちーの炎ダメージを減らしたかったらここで
	public EntityZombieHora(World world) {
		super(world);
		this.setSize(0.6F, 1.95F);
		this.experienceValue = 250;
		this.isImmuneToFire = true;
		this.setPathPriority(PathNodeType.WATER, 8.0F);
		this.setPathPriority(PathNodeType.LAVA, 8.0F);
		this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
		this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
	}

	public boolean getSpecial () {
		return this.getSpellType() == SpellType.SUMMON_VEX;
	}

	public void setSpecial (boolean isSpecial) {
		this.setSpellType(isSpecial ? SpellType.SUMMON_VEX : SpellType.NONE);
	}

	// えーあい
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityZombieHora.AICastingSpell());
		this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 1.0D));
		this.tasks.addTask(3, new EntityAIAttackRanged(this, 1.0D, 100, 10.0F));
		this.tasks.addTask(4, new EntityZombieHora.AISummonSpell());
		this.tasks.addTask(5, new EntityZombieHora.AIAttackSpell());
		this.tasks.addTask(6, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityZombieHora.class }));
		this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
	}

	// えんちちーのステータス設定
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.8D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(512D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(48.0D);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.setHardHealth(this);
		this.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 99999, 1));
		return livingdata;
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public ITextComponent getDisplayName() {
		String name = this.isUnique() ? "entity.sorcererhora.name" : "entity.zombiehora.name" ;
		return new TextComponentTranslation(name, new Object[0]);
	}

	public Color getColor () {
		return this.isHalfHelth() ? BossInfo.Color.RED : BossInfo.Color.BLUE;
	}

	public static void registerFixesEvoker(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityZombieHora.class);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {
		super.readEntityFromNBT(tags);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_EVOCATION_ILLAGER;
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

		if (entity != null && ( this.posY < entity.posY || this.posY < entity.posY + 2.0D ) ) {
			if (this.motionY < 0.0D) { this.motionY = 0.0D; }
			this.motionY += (0.5D - this.motionY) * 0.25D;
		}
	}

	@Override
	protected void onDeathUpdate() {

		++this.deathTicks;
		this.motionY = -1D;

		if (!this.isUnique()) {

			if (this.deathTicks >= 10 && this.deathTicks <= 300) {

				this.world.setWorldTime((this.world.getWorldTime() + 36));
				this.bossInfo.setPercent(0);

				if (this.deathTicks % 11 == 0) {
				    this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5F, 1F);
				}

				if (this.deathTicks % 5 == 0) {
					float f = (this.rand.nextFloat() - 0.5F) * 3F;
					float f1 = (this.rand.nextFloat() - 0.5F) * 2F;
					float f2 = (this.rand.nextFloat() - 0.5F) * 3F;
					this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f, this.posY + 2D + f1, this.posZ + f2, 0D, 0D, 0D);
				}
			}

			if (this.deathTicks >= 300 && !this.world.isRemote) {
				this.setDead();
			}
		}

		else {

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
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {

			if (!this.isUnique()) {
				this.entityDropItem(new ItemStack(ItemInit.aether_crystal, this.rand.nextInt(36) + 16), 0F);
				this.dropItem(this.world, this, ItemInit.divine_crystal, this.rand.nextInt(7) + 3);
				this.dropItem(this.world, this, ItemInit.pure_crystal, 4);
				this.dropItem(this.world, this, ItemInit.mf_sbottle, this.rand.nextInt(40) + 8);
				this.dropItem(this.world, this, ItemInit.mf_bottle, this.rand.nextInt(20) + 4);
				this.dropItem(this.world, this, ItemInit.mf_magiabottle, 1);
				this.dropItem(this.world, this, ItemInit.b_mf_magiabottle, this.rand.nextInt(8) + 1);
				this.dropItem(this.world, this, ItemInit.magic_divine_force, this.rand.nextInt(5) + 2);
			}

			else {
				this.dropItem(this.world, this, ItemInit.cosmic_crystal_shard, this.rand.nextInt(10) + 12);
				this.dropItem(this.world, this, ItemInit.pure_crystal, 4);
				this.dropItem(this.world, this, ItemInit.deus_crystal, this.rand.nextInt(3));
				this.dropItem(this.world, this, ItemInit.mf_bottle, this.rand.nextInt(20) + 8);
				this.dropItem(this.world, this, ItemInit.mf_magiabottle, this.rand.nextInt(8) + 3);
				this.dropItem(this.world, this, ItemInit.accebag, 1);
				this.dropItem(this.world, this, ItemInit.magic_deus_force, this.rand.nextInt(4) + 1);
			}

			this.dropItem(this.world, this, new ItemStack(BlockInit.figurine_zh));
			this.dropItem(this.world, this, new ItemStack(BlockInit.sturdust_crystal_bot));
		}
    }

	@Override
	public void onUpdate() {

		super.onUpdate();
		if (this.deathTicks > 0) { return; }

		this.tickTime++;
		if (this.tickTime % 7 == 0) {

			if (this.world.isRemote) {
				float f1 = (float) this.posX - 0.5F + this.rand.nextFloat();
				float f2 = (float) this.posY + 0.25F + this.rand.nextFloat() * 1.5F;
				float f3 = (float) this.posZ - 0.5F + this.rand.nextFloat();
				this.getParticle().addEffect(ParticleNomal.create(this.world, f1, f2, f3, 0, 0, 0));
			}

			if (!this.isUnique()) {

				if (this.world instanceof WorldServer) {
					int dayTime = 24000;
		            WorldServer sever = world.getMinecraftServer().getWorld(0);
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
		}

		if (this.tickTime % 20 == 0) {

			if (this.suportEntity1 == null || this.suportEntity1.getHealth() <= 0) {
				this.suportEntity1 = null;
			}

			if (this.suportEntity2 == null || this.suportEntity2.getHealth() <= 0) {
				this.suportEntity2 = null;
			}
		}

		// 範囲内のモブにバフ
		if (this.tickTime % 400 == 0) {

			EntityZombieHora liv = this;
			liv.suportEntity1 = null;
			liv.suportEntity2 = null;

			int level = this.isHalfHelth() ? 2 : 0;
			int time = this.isHalfHelth() ? 400 : 300;
			int maxCount = 2;

			int count = 0;
			List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, this, 15D, 6D, 15);

			for (EntityLivingBase entity : list) {

				if (!(entity instanceof ISMMob) || entity == this) { continue; }

				entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, time, level, true, false));
				count++;

				switch (count) {
				case 1:
					liv.suportEntity1 = entity;
					break;
				case 2:
					liv.suportEntity2 = entity;
					break;
				}

				if (count >= maxCount) { break; }
			}
		}
	}

	public void fall(float dis, float dama) { }

	@Override
	public boolean attackEntityFrom(DamageSource src, float amount) {

		if (this.checkBossDamage(src) && this.isMindControl(this)) {

    		if (this.isUnique() && !this.isSMDamage(src) && src.getImmediateSource() instanceof EntityLivingBase) {
    			EntityLivingBase entity = (EntityLivingBase) src.getImmediateSource();
    			entity.attackEntityFrom(DamageSource.MAGIC, amount);
    		}

			return false;
		}

		if (this.isUnique()) {

	    	// 風魔法チェック
	    	if (this.checkMagicCyclone(src)) {
				this.teleportRandomly(this.rand);
	    		amount *= 0.03F;
	    	}

	    	// 光魔法チェック
	    	if (this.checkMagicLight(src)) {
	    		amount *= 0.3F;
				this.teleportRandomly(this.rand);
	    	}

	    	amount = Math.min(amount, this.isHalfHelth() ? 15F : 22.5F);
		}

		else {

	    	// ダメージ倍処理
			if (this.isSMDamage(src)) {

				if (!this.isHalfHelth()) {
					amount *= 1.5F;
				}

				if (this.rand.nextInt(2) == 0) {
					this.teleportRandomly(this.rand);
				}
			}

			else {
				amount = 0.0001F;
				this.teleportRandomly(this.rand);
			}
		}

		return super.attackEntityFrom(src, amount);
	}

	private boolean teleportRandomly(Random rand) {
		if (this.isUnique()) { return false; }
		double x = this.pX + (rand.nextDouble() - 0.5D) * 8D;
		double y = this.pY;
		double z = this.pZ + (rand.nextDouble() - 0.5) * 8D;
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

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_EVOCATION_ILLAGER_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.EVOCATION_ILLAGER_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource src) {
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

	// エーテルバリアーなどのエフェクトを表示するかどうか
	public boolean isRenderEffect () {
		return false;
	}

	public boolean isUnique () {
		return this.getMaxHealth() >= 600D;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float dis) { }

	@Override
	public void setSwingingArms(boolean swing) { }

	public class AIAttackSpell extends EntitySpellcasterIllager.AIUseSpell {

		private AIAttackSpell() {
			super();
		}

		@Override
		protected int getCastingTime() {
			return 20;
		}

		@Override
		protected int getCastingInterval() {
			return 300;
		}

		@Override
		protected void castSpell() {

			EntityZombieHora entity = EntityZombieHora.this;
			EntityLivingBase target = entity.getAttackTarget();
			if (target == null) { return; }

			int count = 0;
			List<EntityLivingBase> list = entity.getEntityList(EntityLivingBase.class, entity, 24D, 4D, 24);

			for (EntityLivingBase e : list) {

				if (!(e instanceof ISMMob) || e instanceof EntityZombieHora) { continue; }
				count++;
			}

			if (count > 15) { return; }

			int summonCount = EntityZombieHora.this.isHalfHelth() ? 3 : 2;

			for (int i = 0; i < summonCount; ++i) {
				this.spawnMob(target);
			}
		}

		private void spawnMob(EntityLivingBase target) {

			EntityZombieHora entity = EntityZombieHora.this;
			boolean isUnique = entity.isUnique();
			World world = entity.world;
			Random rand = world.rand;
			int value = rand.nextInt(isUnique ? 11 : 7);
			EntityLivingBase living = null;
			boolean isHalf = entity.isHalfHelth();

			switch (value) {
			case 0:
				living = new EntityArchSpider(world);
				break;
			case 1:
				living = new EntityBlazeTempest(world);
				if (isHalf) {
					living.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
					living.setHealth(30F);
				}
				break;
			case 2:
				living = new EntityPhantomZombie(world);
				if (isHalf) {
					living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
					living.setHealth(40);
				}
				break;
			case 3:
				living = new EntityElectricCube(world);
				if (isHalf) {
					living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
				}
				break;
			case 4:
				living = new EntityEnderShadow(world);
				((EntityEnderShadow)living).canSpawnShadow = false;
				living.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(isUnique ? Items.DIAMOND_AXE : Items.IRON_AXE));
				living.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				break;
			case 5:
				living = new EntitySkullFrost(world);
				living.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));
				break;
			case 6:
				living = new EntityWitchMadameVerre(world);
				break;
			case 7:
				living = new EntityWindineVerre(world);
				break;
			case 8:
				living = new EntityIfritVerre(world);
				break;
			case 9:
				living = new EntityPixieVex(world);
				break;
			case 10:
				living = new EntitySilderGhast(world);
				break;
			}

			if (isUnique) {
				float health = living.getMaxHealth() * 2F;
				living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
				living.setHealth(health);
				living.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 600, 1));
				living.addPotionEffect(new PotionEffect(PotionInit.shadow, 2400, 4));
			}

			double xRand = entity.posX + (rand.nextDouble() - 0.5) * 20.0;
			double zRand = entity.posZ + (rand.nextDouble() - 0.5) * 20.0;
			living.setLocationAndAngles(xRand, entity.posY, zRand, entity.rotationYaw, 0.0F);
			living.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
			world.spawnEntity(living);
			SMUtil.tameAIAnger((EntityLiving) living, target); // タゲをnullに書き換え

		}

		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SMSoundEvent.HORAMAGIC;
		}

		@Override
		protected EntitySpellcasterIllager.SpellType getSpellType() {
			return EntitySpellcasterIllager.SpellType.FANGS;
		}
	}

	public class AICastingSpell extends EntitySpellcasterIllager.AICastingApell {

		public AICastingSpell() {
			super();
		}

		@Override
		public void updateTask() {

			EntityZombieHora entity = EntityZombieHora.this;
			if (entity.getAttackTarget() != null) {
				entity.getLookHelper().setLookPositionWithEntity(entity.getAttackTarget(), entity.getHorizontalFaceSpeed(), entity.getVerticalFaceSpeed());
			}
		}
	}

	public class AISummonSpell extends EntitySpellcasterIllager.AIUseSpell {

		public AISummonSpell() {
			super();
		}

		@Override
		public boolean shouldExecute() {
			return !super.shouldExecute() ? false : EntityZombieHora.this.rand.nextInt(8) + 1 > 4;
		}

		@Override
		protected int getCastingTime() {
			return 100;
		}

		@Override
		protected int getCastingInterval() {
			return 400;
		}

		@Override
		protected void castSpell() {

			EntityZombieHora entity = EntityZombieHora.this;
			List<EntityLivingBase> list = entity.getEntityList(EntityLivingBase.class, entity, 15D, 6D, 15D);
			boolean isUnique = entity.isUnique();

			for (EntityLivingBase liv : list) {

				if (liv instanceof ISMMob) { continue; }

				if (!isUnique) {

					liv.addPotionEffect(new PotionEffect(PotionInit.slow, 200, 0));
					if (!entity.isHalfHelth() || liv.isPotionActive(PotionInit.refresh_effect)) { continue; }

					for (Potion potion : PotionInit.buffList) {
						if (liv.isPotionActive(potion)) {
							liv.removePotionEffect(potion);
						}
					}
				}

				else {

					World world = entity.world;
					EntityLivingBase target = entity.getAttackTarget();
					if (target == null || world.isRemote) { return; }

					boolean flag = world.rand.nextBoolean();

					for (int i = 0; i < 6; i++) {

						EntityBaseMagicShot magic = flag ? new EntityCyclonMagic(world, entity, ItemStack.EMPTY) : new EntityRockBlast(world, entity, ItemStack.EMPTY, 2);
				        double x = target.posX - entity.posX;
				        double y = target.getEntityBoundingBox().minY - target.height / 2  - entity.posY;
				        double z = target.posZ - entity.posZ;
				        double xz = (double)MathHelper.sqrt(x * x + z * z);
				        magic.shoot(x, y - xz * 0.015D, z, 1.75F, 24);	// 射撃速度

						magic.setDamage(magic.getDamage() + 8);
						entity.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
						world.spawnEntity(magic);
					}
				}
			}

			if (isUnique) { return; }

			// 体力が半分以下なら
			if (entity.isHalfHelth()) {
				entity.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 600, 0, true, false));
			}

			// 半分以上なら自身のデバフ解除
			else {
				for (Potion potion : PotionInit.deBuffList) {

					// デバフなら
					if (potion.isBadEffect()) {
						entity.removePotionEffect(potion);
					}
				}
			}
		}

		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SMSoundEvent.REVERTIME;
		}

		@Override
		protected EntitySpellcasterIllager.SpellType getSpellType() {
			return EntitySpellcasterIllager.SpellType.DISAPPEAR;
		}
	}
}
