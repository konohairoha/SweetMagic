package sweetmagic.init.entity.monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MoverType;
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
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.client.particle.ParticleMagicDig;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityCyclonMagic;
import sweetmagic.init.entity.projectile.EntityDigMagic;
import sweetmagic.init.entity.projectile.EntityElectricMagic;
import sweetmagic.init.entity.projectile.EntityFireMagic;
import sweetmagic.init.entity.projectile.EntityFrostMagic;
import sweetmagic.init.entity.projectile.EntityGravityMagic;
import sweetmagic.util.SMUtil;

public class EntityZombieKronos extends EntitySpellcasterIllager implements IRangedAttackMob, ISMMob {

	public int deathTicks = 0;
	public int tickTime = 0;
	private final BossInfoServer bossInfo = new BossInfoServer(this.getDisplayName(), BossInfo.Color.PINK, BossInfo.Overlay.NOTCHED_10);
	public EntityLivingBase suportEntity1;
	public EntityLivingBase suportEntity2;
	public List<EntityBaseMagicShot> magicList = new ArrayList<>();
	public float oldHealth;
	public float newHealth;

	// えんちちーの炎ダメージを減らしたかったらここで
	public EntityZombieKronos(World world) {
		super(world);
		this.setSize(0.6F, 1.95F);
		this.experienceValue = 250;
		this.isImmuneToFire = true;
		this.setPathPriority(PathNodeType.WATER, 8.0F);
		this.setPathPriority(PathNodeType.LAVA, 8.0F);
		this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
		this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
	}

	//えーあい
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityZombieKronos.AICastingSpell());
		this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 1.0D));
		this.tasks.addTask(3, new EntityZombieKronos.AIMagicSlow());
		this.tasks.addTask(4, new EntityZombieKronos.AIAttackSpell());
		this.tasks.addTask(6, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityZombieKronos.class }));
		this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
	}

	// えんちちーのステータス設定
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.65D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(512D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4D);
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	public static void registerFixesEvoker(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityZombieKronos.class);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {
		super.readEntityFromNBT(tags);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}

		this.oldHealth = tags.getFloat("oldHealth");
		this.newHealth = tags.getFloat("newHealth");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);
		tags.setFloat("oldHealth", this.oldHealth);
		tags.setFloat("newHealth", this.newHealth);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_EVOCATION_ILLAGER;
	}

//	@Override
//	protected void updateAITasks() {
//
//		super.updateAITasks();
//		if (this.deathTicks > 0) { return; }
//
//		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
//		this.motionY *= 0.2D;
//
//		EntityLivingBase entity = this.getAttackTarget();
//		if (entity == null) { return; }
//
//		if (this.posY < entity.posY || this.posY < entity.posY + 2.0D) {
//
//			if (this.motionY >= 0D) { return; }
//
//			this.motionY = 0D;
//			this.motionY += (0.5D - this.motionY) * 0.25D;
//		}
//	}

	@Override
	protected void onDeathUpdate() {

		++this.deathTicks;
		this.motionY -= 1;

		if (this.deathTicks >= 10 && this.deathTicks <= 300) {

			this.world.setWorldTime((this.world.getWorldTime() + 36));

			if (this.deathTicks % 11 == 0) {
			    this.world.playSound(null, new BlockPos(this), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 0.5F, 1.0F);
			}

			if (this.deathTicks % 5 == 0) {
			float f = (this.rand.nextFloat() - 0.5F) * 3.0F;
			float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F;
			float f2 = (this.rand.nextFloat() - 0.5F) * 3.0F;
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f,
					this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
			}
		}
		this.move(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
		if (this.deathTicks >= 300 && !this.world.isRemote) {
			this.setDead();
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
		}
    }

	// 体力が半分以下かどうか
	public boolean isArmor () {
		return this.getHealth() < this.getMaxHealth() / 2;
	}

	@Override
	public void onLivingUpdate() {

		super.onLivingUpdate();
		if (this.deathTicks > 0) { return; }

		this.tickTime++;

		// パーティクルスポーン
		if (this.tickTime % 7 == 0) {
			this.spawnParticle();
		}

		// サポートえんちちーのチェック
		if (this.tickTime % 20 == 0) {
			this.checkSuportEntity();
			this.oldHealth = this.newHealth;
			this.newHealth = this.getHealth();

			// 体力が半分なら
			if (this.newHealth < this.halfHealth() && this.oldHealth > this.halfHealth()) {
				this.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 10000, 1));
			}
		}

		// 魔法攻撃
//		if (!this.magicList.isEmpty() && this.tickTime % 10 == 0 && !this.isArmor() && this.getAttackTarget() != null && !world.isRemote) {
//			this.magicAttack();
//		}

		// 範囲内のモブにバフ
		if (this.tickTime % 300 == 0) {
			this.rangeMobBuf();
			this.tickTime = 0;
		}

	}

	// 体力が半分
	public float halfHealth () {
		return this.getHealth() / 2;
	}

	// パーティクルスポーン
	public void spawnParticle () {

		if (this.world.isRemote) {
			float f1 = (float) this.posX - 0.5F + this.rand.nextFloat();
			float f2 = (float) (this.posY + 0.25F + this.rand.nextFloat() * 1.5);
			float f3 = (float) this.posZ - 0.5F + this.rand.nextFloat();

			FMLClientHandler.instance().getClient().effectRenderer.addEffect(new ParticleMagicDig.Factory().createParticle(0, this.world, f1, f2, f3, 0, 0, 0));
		}

		// 時間固定
		this.world.setWorldTime(12500);
		WorldInfo info = world.getWorldInfo();

		// 雨降っていたら晴れに
		if (info.isRaining()) {
			info.setRainTime(0);
			info.setThunderTime(0);
			info.setRaining(false);
		}
	}

	// サポートえんちちーのチェック
	public void checkSuportEntity () {
		if (this.suportEntity1 == null || this.suportEntity1.getHealth() <= 0) {
			this.suportEntity1 = null;
		}

		if (this.suportEntity2 == null || this.suportEntity2.getHealth() <= 0) {
			this.suportEntity2 = null;
		}
	}

	public void magicAttack () {

		// 攻撃対象の取得
		EntityLivingBase target = this.getAttackTarget();

		// 魔法の取得
		EntityBaseMagicShot shot = this.magicList.get(0);
		shot.thrower = this;

        double x = target.posX - this.posX;
        double y = target.getEntityBoundingBox().minY - target.height / 2  - this.posY;
		double z = target.posZ - this.posZ;
		double xz = (double) MathHelper.sqrt(x * x + z * z);
        shot.shoot(x, y + xz * 0.05D, z, 2F, 0);	// 射撃速度
        shot.setDamage(shot.getDamage() + 6);
		this.world.playSound(null, new BlockPos(this), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.67F);
        this.world.spawnEntity(shot);
//		System.out.println("==============" + shot);

        // リストの削除
        this.magicList.remove(0);

        if (this.magicList.isEmpty()) {
        	this.magicList.clear();
        }
	}

	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {

		int count = 16;
        World world = this.world;
        Random rand = world.rand;
		int value = rand.nextInt(6);

		for (int i = 0; i < count; i++) {

			EntityBaseMagicShot shot = this.getShot(world, value);
			shot.setDamage(shot.getDamage() + 6);
			shot.setHeadingFromThrower(this, this.rotationPitch, this.rotationYaw, 0F, 2.5F, 0F);	//　弾の初期弾速と弾のばらつき
			shot.shoot(shot.motionX, shot.motionY, shot.motionZ, 0, 0);									// 射撃速度
			shot.motionY -= rand.nextDouble() * 2;
			double x = target.posX - 32 + rand.nextDouble() * 64;
			double y = target.posY + 6 + rand.nextDouble() * 8;
			double z = target.posZ - 32 + rand.nextDouble() * 64;
			shot.setPosition(x, y + 2, z);
			this.world.spawnEntity(shot);
        }
	}

	public EntityBaseMagicShot getShot (World world, int value) {

		EntityBaseMagicShot entity = null;

		switch (value) {
		case 0:
			entity = new EntityFireMagic(world, this, ItemStack.EMPTY);
			break;
		case 1:
			entity = new EntityFrostMagic(world, this, ItemStack.EMPTY, false);
			break;
		case 2:
			entity = new EntityCyclonMagic(world, this, ItemStack.EMPTY);
			break;
		case 3:
			entity = new EntityGravityMagic(world, this, ItemStack.EMPTY);
			break;
		case 4:
			entity = new EntityDigMagic(world, this, ItemStack.EMPTY, 0);
			break;
		case 5:
			entity = new EntityElectricMagic(world, this, ItemStack.EMPTY);
			break;
		}

		return entity;
	}


	// 範囲内のモブにバフ
	public void rangeMobBuf () {

		EntityZombieKronos liv = this;
		liv.suportEntity1 = null;
		liv.suportEntity2 = null;

		List<EntityLivingBase> list = SMUtil.getAABB(EntityLivingBase.class, liv.world, liv, 15, 7.5, 15);
		int count = 0;

		for (EntityLivingBase entity : list) {

			if (!(entity instanceof ISMMob) || entity == this) { continue; }

			entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 400, 2));
			entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 400, 1));
			count++;

			switch (count) {
			case 1:
				liv.suportEntity1 = entity;
				break;
			case 2:
				liv.suportEntity2 = entity;
				break;
			}

			if (count >= 2) { break; }
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src)) {
    		return false;
		}

//    	// ダメージ倍処理
    	Entity entity = src.getImmediateSource();
//		if (entity instanceof EntityBaseMagicShot) {
//			amount *= 3F;
//		}

		if (!(entity instanceof EntityBaseMagicShot)) {
			amount /= 3;
		}

		super.attackEntityFrom(src, amount);
		return true;
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
	public void setSwingingArms(boolean swingingArms) {
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	public class AIAttackSpell extends EntitySpellcasterIllager.AIUseSpell {

		private AIAttackSpell() {
			super();
		}

		@Override
		protected int getCastingTime() {
			return 50;
		}

		@Override
		protected int getCastingInterval() {
			return 400;
		}

		@Override
		protected void castSpell() {

			EntityLivingBase living = EntityZombieKronos.this.getAttackTarget();

			if (living == null) { return; }

			double d0 = Math.min(living.posY, living.posY);
			double d1 = Math.max(living.posY, living.posY) + 1.0D;
			float f = (float) MathHelper.atan2(living.posZ - living.posZ, living.posX - living.posX);

			for (int i = 0; i < living.world.rand.nextInt(3) + 1; ++i) {
				float f1 = f + i * (float) Math.PI * 0.4F;
				this.spawnFangs(living, living.posX + MathHelper.cos(f1) * 1.5D,
						living.posZ + MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
			}
		}

		private void spawnFangs(EntityLivingBase liv, double par1, double par2, double par3, double par4, float par5, int par6) {

			boolean flag = false;
			BlockPos pos = new BlockPos(par1, par4, par2);
			EntityZombieKronos entity = EntityZombieKronos.this;
			World world = entity.world;

			while (true) {

				if (!world.isBlockNormalCube(pos, true) && world.isBlockNormalCube(pos.down(), true)) {
					flag = true;
					break;
				}

				pos = pos.down();

				if (pos.getY() < MathHelper.floor(par3) - 1) { break; }
			}

			if (flag) {

				Random rand = world.rand;
				int value = rand.nextInt(3);
				EntityLivingBase living = null;
				EntityLivingBase rider = null;

				switch (value) {
				case 0:
					living = new EntityArchSpider(world);
					rider = new EntitySkullFrost(world);
					rider.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));
					break;
				case 1:
					living = new EntityEnderShadow(world);
					rider = new EntityBlazeTempest(world);
					((EntityEnderShadow)living).canSpawnShadow = false;
					living.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
					living.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
					break;
				case 2:
					living = new EntityElectricCube(world);
					rider = new EntityCreeperCal(world);
					break;
				}

				double xRand = entity.posX + (rand.nextDouble() - 0.5) * 20.0;
				double zRand = entity.posZ + (rand.nextDouble() - 0.5) * 20.0;

				living.setLocationAndAngles(xRand, entity.posY, zRand, entity.rotationYaw, 0.0F);
				world.spawnEntity(living);

				rider.setLocationAndAngles(xRand, entity.posY, zRand, entity.rotationYaw, 0.0F);
				world.spawnEntity(rider);

				rider.startRiding(living);
				SMUtil.tameAIAnger((EntityLiving) living, liv); // タゲをnullに書き換え
				SMUtil.tameAIAnger((EntityLiving) rider, liv); // タゲをnullに書き換え
			}
		}

		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
		}

		@Override
		protected EntitySpellcasterIllager.SpellType getSpellType() {
			return EntitySpellcasterIllager.SpellType.FANGS;
		}
	}

	public class AICastingSpell extends EntitySpellcasterIllager.AICastingApell {

		private AICastingSpell() {
			super();
		}

		@Override
		public void updateTask() {

			EntityZombieKronos entity = EntityZombieKronos.this;

			if (entity.getAttackTarget() != null) {

				entity.getLookHelper().setLookPositionWithEntity(entity.getAttackTarget(),
						entity.getHorizontalFaceSpeed(), entity.getVerticalFaceSpeed());
			}
		}
	}

	public class AIMagicSlow extends EntitySpellcasterIllager.AIUseSpell {

		private AIMagicSlow() {
			super();
		}

		@Override
		public boolean shouldExecute() {
			return !super.shouldExecute() ? false : EntityZombieKronos.this.rand.nextInt(8) + 1 > 4;
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

			EntityZombieKronos entity = EntityZombieKronos.this;
			World world = entity.world;

			List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(15D, 6D, 15D));

			for (EntityLivingBase liv : list) {
				if (liv instanceof ISMMob) { continue; }
				liv.addPotionEffect(new PotionEffect(PotionInit.slow, 300, 1));
			}
		}

		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SMSoundEvent.REVERTIME;
		}

		@Override
		protected EntitySpellcasterIllager.SpellType getSpellType() {
			return EntitySpellcasterIllager.SpellType.SUMMON_VEX;
		}
	}

	public class AISMMobBuf extends EntitySpellcasterIllager.AIUseSpell {

		private AISMMobBuf() {
			super();
		}

		@Override
		public boolean shouldExecute() {

			if (!super.shouldExecute() ? false : EntityZombieKronos.this.rand.nextInt(8) + 1 > 4) {

				EntityZombieKronos liv = EntityZombieKronos.this;
				World world = liv.world;

				List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, liv.getEntityBoundingBox().grow(15D, 6D, 15D));
				int count = 0;

				for (EntityLivingBase entity : list) {

					if (!(entity instanceof ISMMob)) { continue; }

					entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 300, 1));
					count++;

					switch (count) {
					case 1:
						liv.suportEntity1 = entity;
						break;
					case 2:
						liv.suportEntity2 = entity;
						break;
					}

					if (count >= 2) { break; }

				}
			}

			return false;
		}

		@Override
		protected int getCastingTime() {
			return 100;
		}

		@Override
		protected int getCastingInterval() {
			return 340;
		}

		@Override
		protected void castSpell() {

			EntityZombieKronos liv = EntityZombieKronos.this;
			World world = liv.world;

			List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, liv.getEntityBoundingBox().grow(15D, 6D, 15D));
			int count = 0;

			for (EntityLivingBase entity : list) {

				if (!(entity instanceof ISMMob)) { continue; }

				entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 300, 1));
				count++;

				switch (count) {
				case 1:
					liv.suportEntity1 = entity;
					break;
				case 2:
					liv.suportEntity2 = entity;
					break;
				}

				if (count >= 2) { break; }
			}
		}

		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SMSoundEvent.HORAMAGIC;
		}

		@Override
		protected EntitySpellcasterIllager.SpellType getSpellType() {
			return EntitySpellcasterIllager.SpellType.SUMMON_VEX;
		}
	}
}
