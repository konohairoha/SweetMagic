package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.util.SMUtil;

public class EntityZombieHora extends EntitySpellcasterIllager implements ISMMob {

	public int deathTicks = 0;
	public int tickTime = 0;
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

	// えーあい
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityZombieHora.AICastingSpell());
		this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 1.0D));
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

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	protected void entityInit() {
		super.entityInit();
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
		this.motionY -= 1;

		if (this.deathTicks >= 10 && this.deathTicks <= 300) {

			this.world.setWorldTime((this.world.getWorldTime() + 36));
			this.bossInfo.setPercent(0);

			if (this.deathTicks % 11 == 0) {
			    this.world.playSound(null, new BlockPos(this), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 0.5F, 1.0F);
			}

			if (this.deathTicks % 5 == 0) {
			float f = (this.rand.nextFloat() - 0.5F) * 3.0F;
			float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F;
			float f2 = (this.rand.nextFloat() - 0.5F) * 3.0F;
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f,
					this.posY + 2D + f1, this.posZ + f2, 0D, 0D, 0D);
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
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(32) + 32), 0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(32) + 32), 0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal, this.rand.nextInt(36) + 12), 0F);
			this.entityDropItem(new ItemStack(ItemInit.divine_crystal, this.rand.nextInt(7) + 2), 0F);
			this.entityDropItem(new ItemStack(ItemInit.pure_crystal, 4), 0F);
			this.entityDropItem(new ItemStack(ItemInit.mf_sbottle, this.rand.nextInt(40) + 8), 0F);
			this.entityDropItem(new ItemStack(ItemInit.mf_bottle, this.rand.nextInt(20) + 4), 0F);
			this.entityDropItem(new ItemStack(BlockInit.sturdust_crystal_bot, 1), 0F);
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
				float f2 = (float) (this.posY + 0.25F + this.rand.nextFloat() * 1.5);
				float f3 = (float) this.posZ - 0.5F + this.rand.nextFloat();
				FMLClientHandler.instance().getClient().effectRenderer.addEffect(new ParticleNomal.Factory().createParticle(0, this.world, f1, f2, f3, 0, 0, 0));
			}

			if (world instanceof WorldServer) {
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
			World world = liv.world;
			liv.suportEntity1 = null;
			liv.suportEntity2 = null;

			List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, liv.getEntityBoundingBox().grow(15D, 6D, 15D));
			int count = 0;

			for (EntityLivingBase entity : list) {

				if (!(entity instanceof ISMMob) || entity == this) { continue; }

				int level = this.isHalfHelth() ? 2 : 0;
				int time = this.isHalfHelth() ? 400 : 300;
				int maxCount = 2;
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

	@Override
	public void onLivingUpdate() {

		super.onLivingUpdate();
		this.fallDistance = 0;
	}

	@Override
	public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src)) { return false; }

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

		super.attackEntityFrom(src, amount);
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
		boolean success = attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());
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
			return 200;
		}

		@Override
		protected void castSpell() {

			EntityZombieHora entity = EntityZombieHora.this;
			EntityLivingBase living = entity.getAttackTarget();
			if (living == null) { return; }

			List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(24D, 4D, 24D));
			int count = 0;

			for (EntityLivingBase e : list) {

				if (!(e instanceof ISMMob) || e instanceof EntityZombieHora) { continue; }
				count++;
			}

			if (count > 15) { return; }


			double d0 = Math.min(living.posY, living.posY);
			double d1 = Math.max(living.posY, living.posY) + 1.0D;
			float f = (float) MathHelper.atan2(living.posZ - living.posZ, living.posX - living.posX);
			int summonCount = EntityZombieHora.this.isHalfHelth() ? 3 : 2;

			for (int i = 0; i < summonCount; ++i) {
				float f1 = f + i * (float) Math.PI * 0.4F;
				this.spawnFangs(living, living.posX + MathHelper.cos(f1) * 1.5D,
						living.posZ + MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
			}
		}

		private void spawnFangs(EntityLivingBase liv, double par1, double par2, double par3, double par4, float par5, int par6) {

			boolean flag = false;
			BlockPos pos = new BlockPos(par1, par4, par2);
			EntityZombieHora entity = EntityZombieHora.this;
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
				int value = rand.nextInt(7);
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
					living.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
					living.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
					break;
				case 5:
					living = new EntitySkullFrost(world);
					living.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));
					break;
				case 6:
					living = new EntityWitchMadameVerre(world);
					break;
				}

				double xRand = entity.posX + (rand.nextDouble() - 0.5) * 20.0;
				double zRand = entity.posZ + (rand.nextDouble() - 0.5) * 20.0;
				living.setLocationAndAngles(xRand, entity.posY, zRand, entity.rotationYaw, 0.0F);
				world.spawnEntity(living);
				SMUtil.tameAIAnger((EntityLiving) living, liv); // タゲをnullに書き換え

				if (world.isRemote) {
					this.spawnParticl(world, pos);
				}
			}
		}

		public void spawnParticl (World world, BlockPos pos) {

			Random rand = world.rand;

			for (int i = 0; i < 8; i++) {
				float f1 = (float) pos.getX() - 0.5F + rand.nextFloat();
				float f2 = (float) (pos.getY() + 0.25F + rand.nextFloat() * 1.5);
				float f3 = (float) pos.getZ() - 0.5F + rand.nextFloat();
				Particle particl = new ParticleNomal.Factory().createParticle(0, world, f1, f2, f3, 0, 0, 0);
				FMLClientHandler.instance().getClient().effectRenderer.addEffect(particl);
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

	class AICastingSpell extends EntitySpellcasterIllager.AICastingApell {

		private AICastingSpell() {
			super();
		}

		@Override
		public void updateTask() {

			EntityZombieHora entity = EntityZombieHora.this;
			if (entity.getAttackTarget() != null) {
				entity.getLookHelper().setLookPositionWithEntity(entity.getAttackTarget(),
						entity.getHorizontalFaceSpeed(), entity.getVerticalFaceSpeed());
			}
		}
	}

	public class AISummonSpell extends EntitySpellcasterIllager.AIUseSpell {

		private AISummonSpell() {
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
			World world = entity.world;
			List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(15D, 6D, 15D));

			for (EntityLivingBase liv : list) {

				if (liv instanceof ISMMob) { continue; }
				liv.addPotionEffect(new PotionEffect(PotionInit.slow, 200, 0));


				if (entity.isHalfHelth() && !liv.isPotionActive(PotionInit.refresh_effect)) {
					for (Potion potion : PotionInit.buffList) {
						liv.removePotionEffect(potion);
					}
				}
			}

			if (entity.isHalfHelth()) {
				entity.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 600, 0, true, false));
			}

			else {
				for (Potion potion : PotionInit.deBuffList) {
					entity.removePotionEffect(potion);
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
