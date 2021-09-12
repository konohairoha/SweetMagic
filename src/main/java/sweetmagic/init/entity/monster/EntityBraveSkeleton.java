package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.ai.BraveBaseAI;
import sweetmagic.init.entity.projectile.EntityFlameNova;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.SMDamage;
import sweetmagic.util.WorldHelper;

public class EntityBraveSkeleton  extends EntityWitherSkeleton implements ISMMob {

	public int deathTicks = 0;
    public int spellTicks = 0;
    protected int teleportDelay = 0;
	private final BossInfoServer bossInfo = new BossInfoServer(this.getDisplayName(), this.getColor(), BossInfo.Overlay.NOTCHED_10);
	public boolean isCharge = false;
	public int pX = (int) this.posX;
	public int pY = (int) this.posY;
	public int pZ = (int) this.posZ;

	public EntityBraveSkeleton(World world) {
		super(world);
        this.setSize(0.7F, 2.55F);
		this.experienceValue = 600;
		this.isImmuneToFire = true;
	}

	// ボスゲージの名前を日本語表記に
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation("entity.braveskeleton.name", new Object[0]);
	}

	public static void registerFixesWitherSkeleton(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityBraveSkeleton.class);
	}

	// えーあいの登録
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
		this.tasks.addTask(4, new FlamNovaSpell(this));
		this.tasks.addTask(5, new ExplosionSpell(this));
		this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
	}

	// スポーンした時にデフォルトで持たせる情報
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.BOW));
	}

	// ステータス設定
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(512.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.333D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(22.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(16.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
	}

	// スポーン時の設定
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance dif, @Nullable IEntityLivingData living) {

		living = super.onInitialSpawn(dif, living);

		// 馬に乗せる
		EntitySkeletonHorse horse = new EntitySkeletonHorse(this.world);
		horse.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
		horse.onInitialSpawn(dif, (IEntityLivingData) null);
		horse.setOwnerUniqueId(this.entityUniqueID);
		horse.setTemper(100);
		horse.setHorseTamed(true);
		horse.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(128D);
		horse.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10D);
		horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		horse.setHealth(horse.getMaxHealth());
		horse.setGrowingAge(0);
		this.world.spawnEntity(horse);
		this.startRiding(horse, true);
		this.pX = (int) this.posX;
		this.pY = (int) this.posY;
		this.pZ = (int) this.posZ;
		this.setHardHealth(this);
		this.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 99999, 0));

    	return super.onInitialSpawn(dif, living);
	}

	@Override
	public void onLivingUpdate() {

		super.onLivingUpdate();

		// 氷結状態なら氷結解除
		if (this.ticksExisted % 10 == 0 && this.isPotionActive(PotionInit.frosty)) {
			this.removePotionEffect(PotionInit.frosty);
		}

		// 15秒に一回
		if (this.ticksExisted % 300 == 0) {

			// ターゲットの取得していないなら終了
			EntityLivingBase entity = this.getAttackTarget();
			if (entity == null) { return; }

			// 覇気
			this.heroAttack();

			// ブロック破壊
			this.breakBlock();
		}

		// 体力半分切ってないなら終了
		if (!this.isHalfHelth()) { return; }

		// テレポート
		this.teleportCheck();

		// 30秒に一回
		if (this.ticksExisted % 600 == 0) {

			// バフ付与
			this.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 600, 0, true, false));
			this.addPotionEffect(new PotionEffect(PotionInit.shadow, 600, 0, true, false));
			this.playSound(SMSoundEvent.MAGICSTART, 0.35F, 1F);

			int power = 0;

			if (this.getHeldItemMainhand().isEmpty()) {
				power = 3;
			}

			// 弓なら剣に切り替え
			if (this.getMainHand() == Items.BOW) {
				this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
				this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.DIAMOND_SWORD));
			}

			// 剣なら弓に切り替え
			else if (this.getMainHand() == Items.DIAMOND_SWORD) {
				this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
				this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
			}

			// どっちもないなら装備しつつ移動速度アップ
			else {
				this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
				this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.BOW));
				this.addPotionEffect(new PotionEffect(MobEffects.SPEED, 800, 1, true, false));
			}

			this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 600, power, true, false));
		}
	}

	// 覇気
	public void heroAttack() {

		Vec3d p = new Vec3d(this.posX, this.posY, this.posZ);
		AxisAlignedBB aabb = this.getEntityBoundingBox().grow(8F);
		List<Entity> toAttack = this.world.getEntitiesWithinAABBExcludingEntity(this, aabb);

		for (Entity target : toAttack) {

			if ((target instanceof IMob) || !(target instanceof EntityLivingBase) || target instanceof EntitySkeletonHorse) { continue; }
			if (!this.checkDifHeight((EntityLivingBase) target)) { continue; }

			target.motionY += 0.3;
			this.heroicSpirit(p, (EntityLivingBase) target);
		}
	}

	// ブロック破壊
	public void breakBlock () {

        int x = MathHelper.floor(this.posX);
        int y = MathHelper.floor(this.posY);
        int z = MathHelper.floor(this.posZ);

		for (int y2 = 0; y2 <= 3; ++y2) {
			for (int z2 = -2; z2 <= 2; ++z2) {
				for (int x2 = -2; x2 <= 2; ++x2) {

					BlockPos pos = new BlockPos(x + x2, y + y2, z + z2);
					IBlockState state = this.world.getBlockState(pos);
					Block block = state.getBlock();

					if (block != Blocks.AIR && block.canEntityDestroy(state, this.world, pos, this) &&
							ForgeEventFactory.onEntityDestroyBlock(this, pos, state)) {
						this.world.destroyBlock(pos, true);
					}
				}
			}
		}
	}

	// テレポート
	public void teleportCheck () {

		if (!this.world.isRemote && this.isEntityAlive() && this.teleportDelay-- <= 0 && this.rand.nextInt(30) == 0) {

			// ターゲットの取得していないなら終了
			EntityLivingBase entity = this.getAttackTarget();
			if (entity == null || entity.getDistanceSq(this) < 36) { return; }

			for (int i = 0; i < 16; i++) {
				if (this.teleportToEntity(entity)) {
					this.teleportDelay = 90;
					break;
				}
			}
		}
	}

	public boolean teleportRandomly(Random rand) {
		double x = this.pX + (rand.nextDouble() - 0.5) * 16;
		double y = this.pY + (double) (rand.nextInt(12) - 4);
		double z = this.pZ + (rand.nextDouble() - 0.5) * 16;
		this.spawnParticle();
		return this.teleportTo(x, y, z);
	}

	// エンティティに対してテレポート
	public boolean teleportToEntity (Entity entity ) {
		Vec3d vec3d = new Vec3d(this.posX - entity.posX, getEntityBoundingBox().minY + this.height / 2.0F - entity.posY + entity.getEyeHeight( ), this.posZ - entity.posZ);
		vec3d = vec3d.normalize();
		double targetX = this.posX + (this.rand.nextDouble() - 0.5) * 8.0 - vec3d.x * 16.0;
		double targetY = this.posY + (this.rand.nextInt(8) + 2) - vec3d.y * 16.0;
		double targetZ = this.posZ + (this.rand.nextDouble() - 0.5) * 8.0 - vec3d.z * 16.0;
		this.spawnParticle();
		return this.teleportTo(targetX, targetY, targetZ);
	}

	// テレポート実施
	public boolean teleportTo(double x, double y, double z) {

		if (this.isCharge) { return false; }

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

	// 射撃攻撃時
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distance) {

		EntityArrow arrow = this.getArrow(distance);
		if (this.getMainHand() instanceof ItemBow) {
			arrow = ((ItemBow) this.getMainHand()) .customizeArrow(arrow);
		}

		double d0 = target.posX - this.posX;
		double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - arrow.posY;
		double d2 = target.posZ - this.posZ;
		double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
		arrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 0);
		arrow.setDamage(10);
		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(arrow);
	}

	// 矢の能力設定
	@Override
	protected EntityArrow getArrow(float par1) {
		EntityArrow arrow = super.getArrow(par1);
		arrow = super.getArrow(par1);
	    arrow.setFire(200);
        return arrow;
    }

	// メインハンドアイテムの取得
	public Item getMainHand () {
		return this.getHeldItemMainhand().getItem();
	}

	// 死亡時
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		this.deathTicks++;
		if (!this.world.isRemote) {

			this.entityDropItem(new ItemStack(ItemInit.aether_crystal, this.rand.nextInt(24) + 24), 0F);
			this.dropItem(this.world, this, ItemInit.divine_crystal, this.rand.nextInt(8) + 6);
			this.dropItem(this.world, this, ItemInit.pure_crystal, this.rand.nextInt(7) + 1);
			this.dropItem(this.world, this, ItemInit.mf_sbottle, this.rand.nextInt(32) + 12);
			this.dropItem(this.world, this, ItemInit.mf_bottle, this.rand.nextInt(18) + 6);
			this.dropItem(this.world, this, ItemInit.cosmic_crystal_shard, 16);
			this.dropItem(this.world, this, ItemInit.mf_magiabottle, this.rand.nextInt(3) + 1);

			if (this.rand.nextFloat() <= 0.5F) {
				this.dropItem(this.world, this, ItemInit.warrior_bracelet, 1);
			}
		}
    }

	public int getSpellTicks() {
		return this.spellTicks;
	}

	public boolean canSpell () {
		return this.spellTicks <= 0;
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {
		super.readEntityFromNBT(tags);
        this.spellTicks = tags.getInteger("SpellTicks");
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);
		tags.setInteger("SpellTicks", this.spellTicks);
	}

	@Override
	protected void updateAITasks() {

		if (this.deathTicks > 0) { return; }

		super.updateAITasks();

		// 馬に乗ってるなら馬のゲージ表示
		if (this.isRiding() && this.getRidingEntity() instanceof EntitySkeletonHorse) {
			EntitySkeletonHorse entity = (EntitySkeletonHorse) this.getRidingEntity();
			this.bossInfo.setPercent(entity.getHealth() / entity.getMaxHealth());
		}

		// 馬に乗ってないなら自分のゲージ
		else {
			this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
		}

		this.bossInfo.setColor(this.getColor());

		if (this.spellTicks > 0) {
			--this.spellTicks;
		}

		// ターゲットの取得していないなら終了
		EntityLivingBase entity = this.getAttackTarget();
		if (entity == null) { return; }


		double d0 = entity.posX - this.posX;
		double d1 = entity.posZ - this.posZ;
		double d3 = d0 * d0 + d1 * d1;

		// 離れすぎてたら近づく
		if (d3 > 128D) {
			double speed = this.isRiding() ? 2D : 3.5D;
			Vec3d vec = this.findChargePoint(this, entity, 2.1);
			this.limbSwingAmount += 1.5;
			double x = vec.x, y = vec.y, z = vec.z;
			this.getLookHelper().setLookPosition(x, y - 1, z, 10.0F, this.getVerticalFaceSpeed());
			this.getNavigator().tryMoveToXYZ(x, y, z, speed);
		}
	}

	// ターゲットと高低差を確認
	public boolean checkDifHeight (EntityLivingBase entity) {
		return entity.posY - this.posY > 1;
	}

	public float getEyeHeight() {
		return 2.1F;
	}

	public void travel(float strafe, float vertical, float forward) {
		if (!this.isCharge) {
			super.travel(strafe, vertical, forward);
		}
	}

	public boolean attackEntityAsMob(Entity entity) {

		if (!super.attackEntityAsMob(entity)) { return false; }

		if (entity instanceof EntityLivingBase) {

			Vec3d p = new Vec3d(this.posX, this.posY, this.posZ);
			DamageSource src = SMDamage.MagicDamage(this, this);
			AxisAlignedBB aabb = this.getEntityBoundingBox().grow(7.5F);
			List<Entity> toAttack = this.world.getEntitiesWithinAABBExcludingEntity(this, aabb);

			for (Entity target : toAttack) {

				if ((target instanceof IMob) || !(target instanceof EntityLivingBase) || target instanceof EntitySkeletonHorse) { continue; }

				target.attackEntityFrom(src, 6F);
				target.hurtResistantTime = 0;
				target.motionY += 0.2D;
				this.heroicSpirit(p, target);

				if (target instanceof EntityPlayer && this.world.rand.nextBoolean()) {
					EntityPlayer player = (EntityPlayer) target;
					player.getFoodStats().addStats(-2, 1F);
				}
			}
		}

		return true;
	}

	public boolean attackEntityFrom(DamageSource src, float amount) {

    	if ( ( this.checkBossDamage(src) && !this.isMindControl(this) ) || this.isRiding()) {
    		return false;
		}

    	// 風魔法チェック
    	if (this.checkMagicCyclone(src)) {
    		amount *= 0.1F;
    	}

    	// 光魔法チェック
    	if (this.checkMagicLight(src)) {
    		amount *= 0.5F;
    	}

    	if (this.isHalfHelth()) {

    		amount *= 0.75;
        	amount = Math.min(amount, 15);

    		if (this.rand.nextBoolean()) {
				this.teleportRandomly(this.rand);
    		}
    	}

    	else {
        	amount = Math.min(amount, 20);
    	}

		return super.attackEntityFrom(src, amount);
	}

	// 覇気
	public void heroicSpirit (Vec3d p, Entity entity) {
		Vec3d t = new Vec3d(entity.posX, entity.posY, entity.posZ);
		Vec3d r = new Vec3d(t.x - p.x, t.y - p.y, t.z - p.z);
		entity.motionX += r.x ;
		entity.motionZ += r.z ;
	}

	// ゲージの色の取得
	public Color getColor () {

		// 乗ってるときは緑ゲージに
		if (this.isRiding()) {
			return BossInfo.Color.GREEN;
		}

		return this.isHalfHelth() ? BossInfo.Color.RED : BossInfo.Color.BLUE;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public void setInWeb() { }

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

	protected Vec3d findChargePoint(Entity attacker, Entity target, double overshoot) {

		double vecx = target.posX - attacker.posX;
		double vecz = target.posZ - attacker.posZ;
		float rangle = (float) (Math.atan2(vecz, vecx));
		double distance = MathHelper.sqrt(vecx * vecx + vecz * vecz);
		double dx = MathHelper.cos(rangle) * (distance + overshoot);
		double dz = MathHelper.sin(rangle) * (distance + overshoot);

		return new Vec3d(attacker.posX + dx, target.posY, attacker.posZ + dz);
	}


	//==========================================
	//				えーあい
	//==========================================

	public class FlamNovaSpell extends BraveBaseAI {

		public FlamNovaSpell (EntityBraveSkeleton entity) {
			super(entity, false);
		}

		// ウォームアップタイム
		@Override
		protected int getCastingTime() {
			return 100;
		}

		// インターバル
		protected int getCastingInterval() {
			return 300;
		}

		protected void castSpell() {

			if (this.world.isRemote) { return; }

			// ターゲットの取得していないなら終了
			EntityLivingBase entity = this.getTarget();
			if (entity == null) { return; }

			EntityFlameNova magic = new EntityFlameNova(this.world, this.brave, ItemStack.EMPTY);
			double x = entity.posX - this.brave.posX;
			double y = entity.getEntityBoundingBox().minY - entity.height / 2 - this.brave.posY;
			double z = entity.posZ - this.brave.posZ;
			double xz = (double) MathHelper.sqrt(x * x + z * z);
			magic.shoot(x, y - xz * 0.015D, z, 1.75F, 0); // 射撃速度
			magic.setDamage(magic.getDamage() + 8);
			this.brave.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
			this.world.spawnEntity(magic);
		}
	}

	public class ExplosionSpell extends BraveBaseAI {

		public ExplosionSpell (EntityBraveSkeleton entity) {
			super(entity, true);
		}

		// AIを実行できるか
		public boolean shouldExecute() {
			return super.shouldExecute() && this.brave.isHalfHelth();
		}

		// インターバル
		protected int getCastingInterval() {
			return 800;
		}

		// ウォームアップタイム
		protected int getCastWarmupTime() {
			return 120;
		}

		// タスク処理
		public void updateTask() {

			--this.spellWarmup;

			if (this.spellWarmup % 4 == 0) {

				BlockPos pos = new BlockPos(this.brave);
				ParticleHelper.spawnBoneMeal(this.world, pos.up(), EnumParticleTypes.VILLAGER_ANGRY);
				Random rand = this.world.rand;
				float chance = this.brave.isRender() ? 0.083F : 0.03F;

				for (BlockPos p : BlockPos.getAllInBox(pos.add(-8, 0, -8), pos.add(8, 5, 8))) {
					if (rand.nextFloat() > chance) { continue; }

					ParticleHelper.spawnParticle(this.world, p, EnumParticleTypes.FLAME, 1, 0.075D);
				}

				if (this.spellWarmup % 16 == 0) {
					this.brave.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.75F, 1F);
				}
			}

			if (this.spellWarmup < 40) {

				double x = this.brave.posX;
				double y = this.brave.posY;
				double z = this.brave.posZ;

				WorldHelper.suctionPlayer(this.world, this.brave.getEntityBoundingBox().grow(16), x, y, z, this.brave, 0.85D);
			}

			if (this.spellWarmup == 0) {
				this.castSpell();
				this.setCharge(false);
			}
		}

		protected void castSpell() {
			this.world.createExplosion(this.brave, this.brave.posX, this.brave.posY, this.brave.posZ, 8F, false);
			BlockPos pos = new BlockPos(this.brave);

			for (BlockPos p : BlockPos.getAllInBox(pos.add(-7, 0, -7), pos.add(7, 0, 7))) {
				if (this.world.rand.nextInt(32) != 0) { continue; }
				ParticleHelper.spawnParticle(this.world, p, EnumParticleTypes.EXPLOSION_HUGE, 1, 0.075D);
			}
		}
	}
}
