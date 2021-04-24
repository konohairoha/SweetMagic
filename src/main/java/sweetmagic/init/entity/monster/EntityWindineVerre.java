package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBabuleMagic;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityElectricMagic;
import sweetmagic.init.entity.projectile.EntityFrostMagic;
import sweetmagic.init.entity.projectile.EntityLightMagic;
import sweetmagic.init.entity.projectile.EntityPoisonMagic;
import sweetmagic.packet.EntityRemovePKT;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.SMDamage;

public class EntityWindineVerre extends EntityMob implements IRangedAttackMob, ISMMob {

	private int coolTime = 0;
    public int spellTicks = 0;
	public boolean isCharge = false;
	private int damageCoolTime = 0;
	private int tickTime = 0;
	public float capaDame = 0;
	private final BossInfoServer bossInfo = new BossInfoServer(this.getBossName(), BossInfo.Color.YELLOW, BossInfo.Overlay.NOTCHED_6);

	public EntityWindineVerre(World world) {
		super(world);
		this.experienceValue = 120;
		this.setSize(0.5F, 1.5F);
	}

	public static void registerFixesWitch(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityWitchMadameVerre.class);
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
		this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(4, new EntityAIFrostRain(this, true));
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
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
	}

	public void onLivingUpdate() {

		this.tickTime++;
		if (this.damageCoolTime > 0) { this.damageCoolTime--; }

		if (this.tickTime % 5 == 0 && !this.isInWater()) {

			int range = 2;

			for (int x = -range; x <= range; x++) {
				for (int z = -range; z <= range; z++) {

					BlockPos pos = new BlockPos(this.posX + x, this.posY - 1, this.posZ + z);
					Block block = this.world.getBlockState(pos).getBlock();
					if (block != Blocks.WATER && block != Blocks.FROSTED_ICE) { continue; }

					this.world.setBlockState(pos, Blocks.FROSTED_ICE.getDefaultState(), 2);
				}
			}
		}

		// 3.5秒経てば
		if (this.tickTime % 70 == 0) {

			this.tickTime = 0;

			// 体力最大以外かつ最後にダメージを受けたのが一定時間たてば
			if (this.getMaxHealth() > this.getHealth() && this.damageCoolTime == 0) {
				this.setHealth(this.getHealth() + 2);
			}
		}

		super.onLivingUpdate();
	}


	public float getHealValue () {
		return this.isUnique() ? this.getMaxHealth() * 0.25F : this.getMaxHealth() / 2;
	}

	// モブスポーン条件
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.isSMDimension(this.world);
	}

	@Override
	protected void updateAITasks() {

		super.updateAITasks();

		// ユニーク状態ならボスゲージ更新
		if (this.isUnique()) {
			this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
			this.bossInfo.setColor(this.getColor());
		}

		// ターゲットの取得していないなら終了
		EntityLivingBase entity = this.getAttackTarget();
		if (entity == null) { return; }

		// バフ付与
		this.addPotion();

	}

	// バフ付与
	public void addPotion () {

		if (this.world.isRemote) { return; }

		if (this.coolTime > 0) {
			this.coolTime--;
		}

		// クールタイムが終わってないなら
		if (this.coolTime > 0) { return; }

		// 体力半分以下なら
		if (this.getHealth() <= this.getMaxHealth() / 2) {

			// エーテルバリアー
			if (!this.isPotionActive(PotionInit.aether_barrier)) {
				this.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 400, 2, true, false));
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
					this.addPotionEffect(new PotionEffect(PotionInit.regene, 400, 0, true, false));
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
				// 重力加速
				if (!this.isPotionActive(PotionInit.gravity_accele)) {
					this.addPotionEffect(new PotionEffect(PotionInit.gravity_accele, 400, 0));
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

	// ゲージの色の取得
	public Color getColor () {
		return this.isPotionActive(PotionInit.refresh_effect) ? BossInfo.Color.BLUE : BossInfo.Color.PURPLE;
	}

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src) ) { return false; }

    	// 光か雷ならリジェネ解除
    	Entity entity = src.getImmediateSource();
    	if (entity instanceof EntityLightMagic || entity instanceof EntityElectricMagic) {
			this.removePotionEffect(PotionInit.regene);
			this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.5F, 1F);
			PacketHandler.sendToClient(new EntityRemovePKT(this, 0, 0, 0, false));
    	}

    	boolean isRegene = this.isPotionActive(PotionInit.regene);

		// ダメージ倍処理
    	if (!this.isUnique() && !isRegene) {
    		amount = this.getDamageAmount(this.world , src, amount);
    	}

		if (isRegene && this.isUnique() || !this.isSMDamage(src)) {

			// リジェネがついてるなら
			if (isRegene && this.isUnique()) {

				this.capaDame += amount;

				// キャパが100を超えたらリジェネ解除
				if (this.capaDame >= 100) {
					this.removePotionEffect(PotionInit.regene);
					this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.25F, 1F);
					PacketHandler.sendToClient(new EntityRemovePKT(this, 0, 0, 0, false));
				}
			}

			amount *= 0.375F;
		}

		this.damageCoolTime = 400;

		return super.attackEntityFrom(src, amount);
	}

	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
			this.entityDropItem(new ItemStack(ItemInit.mysterious_page, this.rand.nextInt(2) + 1), 0.0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal, this.rand.nextInt(7) + 1), 0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(16)), 0F);
			this.entityDropItem(new ItemStack(ItemInit.blank_magic, this.rand.nextInt(2)), 0F);
			this.entityDropItem(new ItemStack(ItemInit.blank_page, this.rand.nextInt(4) + 1), 0F);
			this.entityDropItem(new ItemStack(ItemInit.witch_tears, this.rand.nextInt(2) + 1), 0F);

			if (this.isUnique()) {
				this.entityDropItem(new ItemStack(ItemInit.cosmic_crystal_shard, this.rand.nextInt(8) + 4), 0F);

				if (this.rand.nextBoolean()) {
					this.entityDropItem(new ItemStack(ItemInit.mermaid_veil, 1), 0F);
				}
			}

			else if (this.rand.nextFloat() <= 0.01F) {
				this.entityDropItem(new ItemStack(ItemInit.mermaid_veil, 1), 0F);
			}
		}
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
		double xz = (double) MathHelper.sqrt(x * x + z * z);
		entity.shoot(x, y - xz * 0.015D, z, this.isUnique() ? 3F : 2F, 0);	// 射撃速度

		int dame = this.isUnique() ? 8 : 4;
		entity.setDamage(entity.getDamage() + dame);
		this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
        this.world.spawnEntity(entity);
	}

	public EntityBaseMagicShot getShot () {

		EntityBaseMagicShot entity = null;
		int rand = this.rand.nextInt(5);

		switch (rand) {
		case 0:
			entity = new EntityBabuleMagic(this.world, this, ItemStack.EMPTY, 0);
			break;
		case 1:
			entity = new EntityPoisonMagic(this.world, this, ItemStack.EMPTY, 0);
			break;
		case 2:
			entity = new EntityFrostMagic(this.world, this, ItemStack.EMPTY, true);
			break;
		case 3:
			entity = new EntityBabuleMagic(this.world, this, ItemStack.EMPTY, 1);
			break;
		case 4:
			entity = null;

			Vec3d p = new Vec3d(this.posX, this.posY, this.posZ);
			DamageSource src = SMDamage.MagicDamage(this, this);
			AxisAlignedBB aabb = this.getEntityBoundingBox().grow(7.5F);
			List<Entity> toAttack = this.world.getEntitiesWithinAABBExcludingEntity(this, aabb);

			for (Entity target : toAttack) {

				if ((target instanceof IMob) || !(target instanceof EntityLivingBase)) { continue; }

				target.attackEntityFrom(src, 6F);
				target.hurtResistantTime = 0;
				target.motionY += 0.3D;

				Vec3d t = new Vec3d(target.posX, target.posY, target.posZ);
				Vec3d r = new Vec3d(t.x - p.x, t.y - p.y, t.z - p.z);
				target.motionX += r.x ;
				target.motionZ += r.z ;
				ParticleHelper.spawnHeal(target, EnumParticleTypes.WATER_SPLASH, 16, 1, 4);
				target.playSound(SoundEvents.ENTITY_GENERIC_SPLASH, 0.35F, 1F);

			}

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
		return new TextComponentTranslation("entity.spirit_water_windine.name", new Object[0]);
	}

	public class EntityAIFrostRain extends EntityAIBase {

		protected int spellWarmup;
		protected int spellCooldown;
		public World world;
		public EntityWindineVerre windine;
		public final boolean isStop;

		public EntityAIFrostRain (EntityWindineVerre entity, boolean isStop) {
			this.windine = entity;
			this.world = this.windine.world;
			this.isStop = isStop;
		}

		// AIを実行できるか
		public boolean shouldExecute() {
			if (this.getTarget() == null) {
				return false;
			} else {
				return this.windine.ticksExisted >= this.spellCooldown && this.windine.isHalfHelth() && this.windine.isUnique();
			}
		}

		// 実行できるか
		public boolean shouldContinueExecuting() {
			return this.windine.getAttackTarget() != null && this.spellWarmup > 0;
		}

		public void startExecuting() {
			this.spellWarmup = this.getCastWarmupTime();
			this.windine.spellTicks = this.getCastingTime();
			this.spellCooldown = this.windine.ticksExisted + this.getCastingInterval();
			this.setCharge(true);
		}

		// タスク処理
		public void updateTask() {

			--this.spellWarmup;
			Random rand = world.rand;
			int range = 16;
			EntityLivingBase target = this.windine.getAttackTarget();
			if (target == null) { return; }

			for (int i = 0; i < 3; i++) {

				double x = (rand.nextDouble() * range) - rand.nextDouble() * range;
				double y = (rand.nextDouble() * range) - rand.nextDouble() * range;
				double z = (rand.nextDouble() * range) - rand.nextDouble() * range;
		        BlockPos pos = new BlockPos(target.posX + x, target.posY + y, target.posZ + z);
		        int posY = this.world.canSeeSky(pos) ? 20 : 7;

				if (!this.world.isRemote) {
					EntityFrostMagic entity = new EntityFrostMagic(this.world, this.windine, ItemStack.EMPTY, true);
					entity.shoot(0, entity.motionY - 0.33F, 0, 0, 0);
					entity.motionY -= 1;
					entity.setPosition(pos.getX(), pos.getY() + posY, pos.getZ());
					entity.setDamage(entity.getDamage() + 6);
					this.world.spawnEntity(entity);
				}
			}

			this.windine.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.33F, 0.67F);

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
			return this.windine.getAttackTarget();
		}

		// チャージの設定
		public void setCharge (boolean charge) {
			if (this.isStop) {
				this.windine.isCharge = charge;
			}
		}
	}
}
