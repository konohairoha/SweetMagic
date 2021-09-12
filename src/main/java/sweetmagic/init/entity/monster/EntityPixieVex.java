package sweetmagic.init.entity.monster;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.particle.Particle;
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
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.client.particle.ParticleMagicFrost;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.packet.RemovePotion;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.SoundHelper;

public class EntityPixieVex extends EntityMob  implements ISMMob {

	public static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.<Byte> createKey(EntityPixieVex.class, DataSerializers.BYTE);
    public static final DataParameter<Integer> DATA = EntityDataManager.<Integer>createKey(EntityPixieVex.class, DataSerializers.VARINT);
	@Nullable
	public BlockPos boundOrigin;
	public EntityLivingBase targetEntity;
	public SMElement ele;
	public int tickTime = 0;
	public int taskTIme = 0;
	public boolean isVex = true;

	public EntityPixieVex(World world) {
		super(world);
		this.isImmuneToFire = true;
		this.moveHelper = new EntityPixieVex.AIMoveControl(this);
		this.setSize(0.4F, 0.8F);
		this.experienceValue = 100;
	}

	public void move(MoverType type, double x, double y, double z) {
		super.move(type, x, y, z);
	}

	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(8, new EntityPixieVex.AIMoveRandom());
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityPixieVex.class }));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(VEX_FLAGS, Byte.valueOf((byte) 0));
		this.dataManager.register(DATA, Integer.valueOf((int) 0));
	}

	public static void registerFixesVex(DataFixer fixer) {
		EntityLiving.registerFixesMob(fixer, EntityPixieVex.class);
	}

	public void onUpdate() {

		super.onUpdate();
		this.setNoGravity(true);

		if (!this.isVex || !this.isRender()) { return; }

		if (this.world.isRemote) {

			this.tickTime++;
			if (this.tickTime % 14 == 0) {
				this.tickTime = 0;
				this.spawnParticle();
			}
		}

		EntityLivingBase entity = this.getAttackTarget();

		if (entity != null) {
			this.targetEntity = entity;
		}

		else {
			this.targetEntity = null;
		}

		if (entity != null && this.ticksExisted % 300 != 0) { return; }

		List<EntityPlayer> entityList = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(24D, 6D, 24D));
		if (entityList.isEmpty()) { return; }

		for (EntityPlayer player : entityList) {

			if (player.isCreative()) { continue; }
			this.setAttackTarget(player);
			this.targetEntity = player;
		}
	}

	@Override
	protected void updateAITasks() {

		// ターゲットの取得していないなら終了
		EntityLivingBase entity = this.getAttackTarget();
		if (entity == null) { return; }

		// ターゲットが見えないなら終了
		if (!this.canEntityBeSeen(entity) || this.getDistance(entity) > 24D) {
			this.setAttackTarget(null);
			this.targetEntity = null;
			this.taskTIme = 0;
			return;
		}

		this.taskTIme++;
		this.getNavigator().clearPath();
		this.getLookHelper().setLookPositionWithEntity(entity, 90F, 90F);
		if (this.taskTIme % 160 != 0) { return; }

		// リフレッシュエフェクトが付いてるなら剥がす
		if (entity.isPotionActive(PotionInit.refresh_effect) && entity instanceof EntityPlayer &&
				entity.getActivePotionEffect(PotionInit.refresh_effect).getAmplifier() == 0) {

			entity.removeActivePotionEffect(PotionInit.refresh_effect);
			PacketHandler.sendToClient(new RemovePotion(3));

			// クライアント（プレイヤー）へ送りつける
			if (entity instanceof EntityPlayerMP) {
				PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_REMOVE, 1F, 0.33F), (EntityPlayerMP) entity);
			}
		}

		// リフレッシュエフェクトが付いてないならデバフ付与
		else {

			switch (this.getData()) {
			case 0:
				PlayerHelper.addPotion(entity, PotionInit.frosty, 300, 0);
				break;
			case 1:
				PlayerHelper.addPotion(entity, PotionInit.gravity, 300, 0);
				break;
			case 2:
				PlayerHelper.addPotion(entity, MobEffects.BLINDNESS, 300, 0);
				break;
			}
		}

		entity.attackEntityFrom(DamageSource.MAGIC, 2);
		this.setAttackTarget(null);
		this.taskTIme = 0;
	}

	public void spawnParticle() {

		float x = (float) (-this.motionX / 80);
		float y = (float) (-this.motionY / 80);
		float z = (float) (-this.motionZ / 80);

		int data = 0;

		try {
			data = this.getData();
		} catch (Throwable e) { }

		switch (data) {
		case 0:
			for (int i = 0; i < 3; i++) {
				float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
				float f2 = (float) (this.posY + 0.5F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
				float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
				Particle effect = new ParticleMagicFrost.Factory().createParticle(0, this.world, f1, f2, f3, x, y, z);
				FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
			}
			break;
		case 1:
			for (int i = 0; i < 3; i++) {
				float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
				float f2 = (float) (this.posY + 0.5F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
				float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
				Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, f1, f2, f3, x, y, z);
				FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
			}
			break;
		case 2:

			for (int i = 0; i < 3; i++) {
				float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
				float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
				float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
				Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, f1, f2, f3, x, y, z, 48);
				FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
			}
			break;
		}
	}

	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
			this.entityDropItem(new ItemStack(ItemInit.prizmium, this.rand.nextInt(2) + 1), 0.0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(5)), 0F);

			if (this.rand.nextFloat() <= 0.01F) {
				this.entityDropItem(new ItemStack(ItemInit.varrier_pendant, 1), 0F);
			}
		}
    }

	public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src) && !this.isMindControl(this)) {
    		return false;
		}

		// ダメージ倍処理
		amount = this.getDamageAmount(this.world , src, amount);
		return super.attackEntityFrom(src, amount);
	}

	@Nullable@Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance dif, @Nullable IEntityLivingData living) {
		this.setEquipmentBasedOnDifficulty(dif);
		this.setEnchantmentBasedOnDifficulty(dif);
		this.setData(this.world.rand.nextInt(3));
		this.setElement();
		this.setHardHealth(this);
		return super.onInitialSpawn(dif, living);
	}

	public void readEntityFromNBT(NBTTagCompound tags) {
		super.readEntityFromNBT(tags);

		if (tags.hasKey("BoundX")) {
			this.boundOrigin = new BlockPos(tags.getInteger("BoundX"), tags.getInteger("BoundY"), tags.getInteger("BoundZ"));
		}

		this.setData(tags.getInteger("data"));
		this.setElement();
	}

	// 属性設定
	public void setElement () {
		switch (this.getData()) {
		case 0:
			this.ele = SMElement.FROST;
			break;
		case 1:
			this.ele = SMElement.GRAVITY;
			break;
		case 2:
			this.ele = SMElement.DARK;
			break;
		}
	}

	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);

		if (this.boundOrigin != null) {
			tags.setInteger("BoundX", this.boundOrigin.getX());
			tags.setInteger("BoundY", this.boundOrigin.getY());
			tags.setInteger("BoundZ", this.boundOrigin.getZ());
		}

		tags.setInteger("data", this.getData());
	}

	@Nullable
	public BlockPos getBoundOrigin() {
		return this.boundOrigin;
	}

	public void setBoundOrigin(@Nullable BlockPos boundOriginIn) {
		this.boundOrigin = boundOriginIn;
	}

	private boolean getVexFlag(int mask) {
		int i = ((Byte) this.dataManager.get(VEX_FLAGS)).byteValue();
		return (i & mask) != 0;
	}

	private void setVexFlag(int mask, boolean value) {

		int i = ((Byte) this.dataManager.get(VEX_FLAGS)).byteValue();
		i = value ? (i | mask) : (i & ~mask);
		this.dataManager.set(VEX_FLAGS, Byte.valueOf((byte) (i & 255)));
	}

	public void setCharging(boolean charging) {
		this.setVexFlag(1, charging);
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_VEX_AMBIENT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_VEX_DEATH;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_VEX_HURT;
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_VEX;
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender() {
		return 15728880;
	}

	public float getBrightness() {
		return 1.0F;
	}

	public int getAttackDuration() {
		return 80;
	}

	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
		this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
	}

	public float getAttackAnimationScale(float par1) {
		return ((float) 100 + par1) / (float) this.getAttackDuration();
	}

	public int getData () {
		return this.dataManager.get(DATA);
	}

	public void setData (int data) {
		this.dataManager.set(DATA, data);
	}

	public void setElement(Element ele) {
		this.ele = ele.ele;
		this.setData(ele.data);
	}

    public void fall(float dis, float dama) { }

	public class AIMoveControl extends EntityMoveHelper {

		public EntityPixieVex vex;

		public AIMoveControl(EntityPixieVex vex) {
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

		public EntityPixieVex vex = EntityPixieVex.this;

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

	public enum Element {

		FROST(SMElement.FLAME, 0),
		GRAVITY(SMElement.GRAVITY, 1),
		DARK(SMElement.DARK, 2);

		public SMElement ele;
		public int data;

		Element (SMElement ele, int data) {
			this.ele = ele;
			this.data = data;
		}
	}
}
