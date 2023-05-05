package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.util.ParticleHelper;

public class EntityAncientFairy extends EntityMob implements ISMMob {

	private int deathTicks = 0;
	private int spellTicks = 0;
	private int spellTick = 0;
	private int randTick = 0;
	private int tickTime = 0;
	private int attractTime = 0;
	private BlockPos boundOrigin;
	private int pX = (int) this.posX;
	private int pY = (int) this.posY;
	private int pZ = (int) this.posZ;
	private final BossInfoServer bossInfo = new BossInfoServer(this.getDisplayName(), this.getColor(), BossInfo.Overlay.NOTCHED_12);

	public EntityAncientFairy(World world) {
		super(world);
		this.isImmuneToFire = true;
		this.moveHelper = new EntityAncientFairy.AIMoveControl(this);
		this.setSize(1.0F, 3F);
		this.experienceValue = 500;
	}

	public void move(MoverType type, double x, double y, double z) {
		super.move(type, x, y, z);
	}

	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIPoisonFog(this));
		this.tasks.addTask(8, new EntityAncientFairy.AIMoveRandom());
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityAncientFairy.class }));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(256.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(15.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance dif, @Nullable IEntityLivingData livingdata) {
		this.setHardHealth(this);
		this.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 99999, 1, true, false));
		return livingdata;
	}

	public static void registerFixesentity(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityAncientFairy.class);
	}

	@Override
	protected void updateAITasks() {

		if (this.deathTicks > 0) { return; }

		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
		this.bossInfo.setColor(this.getColor());

		// ターゲットの取得していないなら終了
		EntityLivingBase target = this.getAttackTarget();
		if (target == null) { return; }

		this.spellTick++;
		boolean isHalf = this.isHalfHelth();
		this.motionY *= 0.25D;

		if (this.posY < target.posY || this.posY < target.posY + 2.0D) {
			if (this.motionY < 0.0D) { this.motionY = 0.0D; }
			this.motionY += (0.5D - this.motionY) * 0.25D;
		}

		for (Potion potion : PotionInit.deBuffList) {
			if (this.isPotionActive(potion)) {
				this.removePotionEffect(potion);
			}
		}

		if (this.randTick <= 0) {
			this.randTick = this.rand.nextInt(isHalf ? 30 : 150);
		}

		if (this.spellTick < 250 + this.randTick) { return; }

		this.spellTick = 0;
		this.randTick = this.rand.nextInt(150);

		for (int i = 0; i < 2; i++) {
			EntityPixieVex entity = new EntityPixieVex(this.world);
			double x = this.posX + this.rand.nextDouble() * 4 - this.rand.nextDouble() * 4;
			double z = this.posZ + this.rand.nextDouble() * 4 - this.rand.nextDouble() * 4;

			entity.setPosition(x, this.posY + this.rand.nextDouble() * 2, z);
			entity.setData(this.rand.nextInt(3));
			entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 1600, 0, true, false));
			entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
			this.world.spawnEntity(entity);
		}

		// 体力半分のとき
		if (isHalf) {

			switch (this.rand.nextInt(3)) {
			case 0:

				for (PotionEffect effect : target.getActivePotionEffects()) {

					// バフならすべてコピー
					Potion potion = effect.getPotion();
					if (!potion.isBadEffect()) {
						this.addPotionEffect(new PotionEffect(potion, effect.getDuration(), effect.getAmplifier(), true, false));
					}
				}
				break;

			case 1:
				this.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 400, 0, true, false));
				target.addPotionEffect(new PotionEffect(MobEffects.SPEED, 400, 2, true, false));
				target.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 400, 1, true, false));
				break;
			case 2:
				target.addPotionEffect(new PotionEffect(MobEffects.POISON, 160, 0));
				target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 160, 2));
				target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 160, 0));
				break;
			}

			target.attackEntityFrom(DamageSource.MAGIC, 10F);
		}

		else {

			// リフレッシュエフェクトがかかっているか
			boolean isEffect = target.isPotionActive(PotionInit.refresh_effect);

			if (isEffect) {

				switch (this.rand.nextInt(3)) {
				case 0:
					this.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 400, 0, true, false));
					break;
				case 1:
					this.addPotionEffect(new PotionEffect(MobEffects.SPEED, 400, 2, true, false));
					break;
				case 2:
					this.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 400, 2, true, false));
					break;
				}
			}

			// リフレッシュエフェクトがかかってないなら
			else {
				switch (this.rand.nextInt(3)) {
				case 0:
					target.addPotionEffect(new PotionEffect(MobEffects.POISON, 400, 0));
					break;
				case 1:
					target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 2));
					break;
				case 2:
					target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 400, 0));
					break;
				}

				target.attackEntityFrom(DamageSource.MAGIC, 6F);
			}
		}
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
    		amount *= 0.03F;
    	}

    	// 光魔法チェック
    	if (this.checkMagicLight(src)) {
    		amount *= 0.3F;
			this.teleportRandomly(this.rand);
    	}

    	if (this.isHalfHelth()) {
    		amount = Math.min(amount, 15);
			this.teleportRandomly(this.rand);
    	}

    	else {
    		amount = Math.min(amount, 25);
    	}

		return super.attackEntityFrom(src, amount);
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
			float f2 = (float) this.posY + 0.25F + this.rand.nextFloat() * 1.5F;
			float f3 = (float) this.posZ - 0.5F + this.rand.nextFloat();
			this.world.spawnParticle(EnumParticleTypes.FLAME, f1, f2, f3, 0, 0, 0);
		}
	}

	// 死亡時
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		this.deathTicks++;
		if (!this.world.isRemote) {

			this.dropItem(this.world, this, ItemInit.aether_crystal, this.rand.nextInt(24) + 24);
			this.dropItem(this.world, this, ItemInit.divine_crystal, this.rand.nextInt(8) + 5);
			this.dropItem(this.world, this, ItemInit.pure_crystal, this.rand.nextInt(7) + 1);
			this.dropItem(this.world, this, ItemInit.mf_sbottle, this.rand.nextInt(32) + 12);
			this.dropItem(this.world, this, ItemInit.mf_bottle, this.rand.nextInt(18) + 6);
			this.dropItem(this.world, this, ItemInit.cosmic_crystal_shard, 6);
			this.dropItem(this.world, this, ItemInit.mf_magiabottle, this.rand.nextInt(3) + 1);
			this.dropItem(this.world, this, new ItemStack(BlockInit.figurine_af));
			this.dropItem(this.world, this, ItemInit.magic_pure_force, this.rand.nextInt(6) + 1);
			this.dropItem(this.world, this, ItemInit.devil_cake, this.rand.nextInt(2) + 1);

			if (this.rand.nextFloat() <= 0.5F) {
				this.dropItem(this.world, this, ItemInit.varrier_pendant, 1);
			}
		}
    }

	@Nullable
	public BlockPos getBoundOrigin() {
		return this.boundOrigin;
	}

	// 体力半分かどうか
	public boolean isHalfHelth () {
		return this.getHealth() < this.getMaxHealth() / 2;
	}

	public ITextComponent getDisplayName() {
		return new TextComponentTranslation("entity.ancientfairy.name", new Object[0]);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public void setInWeb() { }

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
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

	// ゲージの色の取得
	public Color getColor () {
		return this.isHalfHelth() ? BossInfo.Color.RED : BossInfo.Color.GREEN;
	}

	public class AIMoveControl extends EntityMoveHelper {

		private EntityAncientFairy entity;

		public AIMoveControl(EntityAncientFairy entity) {
			super(entity);
			this.entity = entity;
		}

		public void onUpdateMoveHelper() {

			if (this.action != EntityMoveHelper.Action.MOVE_TO) { return; }

			double d0 = this.posX - this.entity.posX;
			double d1 = this.posY - this.entity.posY;
			double d2 = this.posZ - this.entity.posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			d3 = (double) MathHelper.sqrt(d3);

			if (d3 < this.entity.getEntityBoundingBox().getAverageEdgeLength()) {
				this.action = EntityMoveHelper.Action.WAIT;
				this.entity.motionX *= 0.5D;
				this.entity.motionY *= 0.5D;
				this.entity.motionZ *= 0.5D;
			}

			else {
				this.entity.motionX += d0 / d3 * 0.05D * this.speed;
				this.entity.motionY += d1 / d3 * 0.05D * this.speed;
				this.entity.motionZ += d2 / d3 * 0.05D * this.speed;
				EntityLivingBase target = this.entity.getAttackTarget();

				if (target == null) {
					this.entity.rotationYaw = -((float) MathHelper.atan2(this.entity.motionX, this.entity.motionZ)) * (180F / (float) Math.PI);
					this.entity.renderYawOffset = this.entity.rotationYaw;
				}

				else {
					double d4 = target.posX - this.entity.posX;
					double d5 = target.posZ - this.entity.posZ;
					this.entity.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
					this.entity.renderYawOffset = this.entity.rotationYaw;
				}
			}
		}
	}

	public class AIMoveRandom extends EntityAIBase {

		private EntityAncientFairy entity = EntityAncientFairy.this;

		public AIMoveRandom() {
			this.setMutexBits(1);
		}

		public boolean shouldExecute() {
			return !this.entity.getMoveHelper().isUpdating() && this.entity.rand.nextInt(7) == 0;
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

	public class EntityAIPoisonFog extends EntityAIBase {

		protected int spellWarmup;
		protected int spellCooldown;
		public World world;
		public EntityAncientFairy entity;

		public EntityAIPoisonFog (EntityAncientFairy entity) {
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
		}

		// タスク処理
		public void updateTask() {

			--this.spellWarmup;

			if (this.spellWarmup % 4 == 0) {

				BlockPos pos = new BlockPos(this.entity);
				Random rand = this.world.rand;
				float chance = this.entity.isRender() ? 0.1F : 0.02F;

				for (BlockPos p : BlockPos.getAllInBox(pos.add(-16, -1, -16), pos.add(16, 4, 16))) {
					if (rand.nextFloat() > chance) { continue; }

					ParticleHelper.spawnParticle(this.world, p, EnumParticleTypes.SPELL_WITCH, 1, 0.075D);
				}
			}

			if (this.spellWarmup == 0) {
				this.castSpell();
			}
		}

		// 特殊行動開始
		protected void castSpell() {

			List<EntityPlayer> playerList = this.entity.getEntityList(EntityPlayer.class, this.entity, 16D, 16D, 16);

			for (EntityPlayer player : playerList) {

				if (player.isCreative() || player.isSpectator() || player.getHealth() <= 1F) { continue; }

				float health = player.getHealth() * ( player.isPotionActive(PotionInit.refresh_effect) ? 0.5F : 0.75F ) ;
				player.setHealth(health);
				player.attackEntityFrom(DamageSource.MAGIC, 1F);
			}
		}

		// キャストタイム
		protected int getCastingTime() {
			return this.spellCooldown;
		}

		// ウォームアップタイム
		protected int getCastWarmupTime() {
			return 160;
		}

		// インターバル
		protected int getCastingInterval() {
			return 700;
		}

		// ターゲット取得
		public EntityLivingBase getTarget () {
			return this.entity.getAttackTarget();
		}
	}
}
