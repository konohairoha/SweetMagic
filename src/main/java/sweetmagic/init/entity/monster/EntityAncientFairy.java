package sweetmagic.init.entity.monster;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.util.WorldHelper;

public class EntityAncientFairy extends EntityMob implements ISMMob {

	private final BossInfoServer bossInfo = new BossInfoServer(this.getDisplayName(), this.getColor(), BossInfo.Overlay.NOTCHED_12);
	public int deathTicks = 0;
	public BlockPos boundOrigin;
	public int pX = (int) this.posX;
	public int pY = (int) this.posY;
	public int pZ = (int) this.posZ;
	public int spellTick = 0;
	public int randTick = 0;
	public int tickTime = 0;
	public int attractTime = 0;

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

	public static void registerFixesVex(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityAncientFairy.class);
	}

	public void onUpdate() {

		super.onUpdate();

		if (this.attractTime  <= 0) {
			this.attractTime  = 200 + this.world.rand.nextInt(200);
		}

		this.tickTime ++;

		if (this.attractTime <= this.tickTime) {

			if (this.world.isRemote) {
				for(int k = 0; k <= 3; k++) {
					float f1 = (float) this.posX - 0.5F + this.rand.nextFloat();
					float f2 = (float) (this.posY + 0.25F + this.rand.nextFloat() * 1.5);
					float f3 = (float) this.posZ - 0.5F + this.rand.nextFloat();
					FMLClientHandler.instance().getClient().effectRenderer.addEffect(new ParticleNomal.Factory().createParticle(0, this.world, f1, f2, f3, 0, 0, 0));
				}
			}

			WorldHelper.suctionPlayer(this.world, this.getEntityBoundingBox().grow(32), this.posX, this.posY, this.posZ, this, 0.0775D);

			if (this.attractTime + 4 <= this.tickTime) {
				this.attractTime  = 200 + this.world.rand.nextInt(200);
				this.tickTime = 0;
			}
		}
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
			entity.data = this.rand.nextInt(3);
			entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 2400, 0, true, false));
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

			target.attackEntityFrom(DamageSource.MAGIC, 6F);
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

				target.attackEntityFrom(DamageSource.MAGIC, 3F);
			}

		}
	}

	public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src) || !this.isSMDamage(src)) {

    		if (!this.isSMDamage(src) && src.getImmediateSource() instanceof EntityLivingBase) {
    			EntityLivingBase entity = (EntityLivingBase) src.getImmediateSource();
    			entity.attackEntityFrom(DamageSource.MAGIC, amount);
    		}


    		return false;
		}

    	if (this.isHalfHelth()) {

    		amount =  Math.min(amount, 15);

    		if (this.rand.nextBoolean()) {
				this.teleportRandomly(this.rand);
    		}
    	}

    	else {
    		amount =  Math.min(amount, 30);
    	}

		return super.attackEntityFrom(src, amount);
	}

	public boolean teleportRandomly(Random rand) {
		double x = this.pX + (rand.nextDouble() - 0.5) * 8;
		double y = this.pY + (double) (rand.nextInt(4));
		double z = this.pZ + (rand.nextDouble() - 0.5) * 8;
		this.spawnParticle();
		return teleportTo(x, y, z);
	}

	// エンティティに対してテレポート
	public boolean teleportToEntity (Entity entity ) {
		Vec3d vec3d = new Vec3d(this.posX - entity.posX, getEntityBoundingBox().minY + this.height / 2.0F - entity.posY + entity.getEyeHeight( ), this.posZ - entity.posZ);
		vec3d = vec3d.normalize();
		double targetX = this.posX + (this.rand.nextDouble() - 0.5) * 8.0 - vec3d.x * 16.0;
		double targetY = this.posY + (this.rand.nextInt(8) - 2) - vec3d.y * 16.0;
		double targetZ = this.posZ + (this.rand.nextDouble() - 0.5) * 8.0 - vec3d.z * 16.0;
		this.spawnParticle();
		return this.teleportTo(targetX, targetY, targetZ);
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

	// 死亡時
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		this.deathTicks++;
		if (!this.world.isRemote) {
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(16) + 48), 0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal, this.rand.nextInt(24) + 24), 0F);
			this.entityDropItem(new ItemStack(ItemInit.divine_crystal, this.rand.nextInt(8) + 4), 0F);
			this.entityDropItem(new ItemStack(ItemInit.pure_crystal, this.rand.nextInt(7) + 1), 0F);
			this.entityDropItem(new ItemStack(ItemInit.mf_sbottle, this.rand.nextInt(32) + 12), 0F);
			this.entityDropItem(new ItemStack(ItemInit.mf_bottle, this.rand.nextInt(18) + 6), 0F);
			this.entityDropItem(new ItemStack(ItemInit.cosmic_crystal_shard, 6), 0F);

			if (this.rand.nextFloat() <= 0.5F) {
				this.entityDropItem(new ItemStack(ItemInit.varrier_pendant, 1), 0F);
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

		public EntityAncientFairy vex;

		public AIMoveControl(EntityAncientFairy vex) {
			super(vex);
			this.vex = vex;
		}

		public void onUpdateMoveHelper() {

			if (this.action != EntityMoveHelper.Action.MOVE_TO) { return; }

			double d0 = this.posX - this.vex.posX;
			double d1 = this.posY - this.vex.posY;
			double d2 = this.posZ - this.vex.posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			d3 = (double) MathHelper.sqrt(d3);

			if (d3 < this.vex.getEntityBoundingBox().getAverageEdgeLength()) {
				this.action = EntityMoveHelper.Action.WAIT;
				this.vex.motionX *= 0.5D;
				this.vex.motionY *= 0.5D;
				this.vex.motionZ *= 0.5D;
			}

			else {
				this.vex.motionX += d0 / d3 * 0.05D * this.speed;
				this.vex.motionY += d1 / d3 * 0.05D * this.speed;
				this.vex.motionZ += d2 / d3 * 0.05D * this.speed;

				if (this.vex.getAttackTarget() == null) {
					this.vex.rotationYaw = -((float) MathHelper.atan2(this.vex.motionX,
							this.vex.motionZ)) * (180F / (float) Math.PI);
					this.vex.renderYawOffset = this.vex.rotationYaw;
				}

				else {
					double d4 = this.vex.getAttackTarget().posX - this.vex.posX;
					double d5 = this.vex.getAttackTarget().posZ - this.vex.posZ;
					this.vex.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
					this.vex.renderYawOffset = this.vex.rotationYaw;
				}
			}
		}
	}

	public class AIMoveRandom extends EntityAIBase {

		public EntityAncientFairy vex = EntityAncientFairy.this;

		public AIMoveRandom() {
			this.setMutexBits(1);
		}

		public boolean shouldExecute() {
			return !this.vex.getMoveHelper().isUpdating() && this.vex.rand.nextInt(7) == 0;
		}

		public boolean shouldContinueExecuting() {
			return false;
		}

		public void updateTask() {

			BlockPos pos = this.vex.getBoundOrigin();

			if (pos == null) {
				pos = new BlockPos(this.vex);
			}

			for (int i = 0; i < 3; ++i) {

				BlockPos pos1 = pos.add(this.vex.rand.nextInt(15) - 7, this.vex.rand.nextInt(11) - 5, this.vex.rand.nextInt(15) - 7);

				if (this.vex.world.isAirBlock(pos1)) {
					this.vex.moveHelper.setMoveTo((double) pos1.getX() + 0.5D, (double) pos1.getY() + 0.5D, (double) pos1.getZ() + 0.5D, 0.25D);

					if (this.vex.getAttackTarget() == null) {
						this.vex.getLookHelper().setLookPosition((double) pos1.getX() + 0.5D, (double) pos1.getY() + 0.5D, (double) pos1.getZ() + 0.5D, 180.0F, 20.0F);
					}

					break;
				}
			}
		}
	}

}
