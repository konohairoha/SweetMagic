package sweetmagic.init.entity.monster;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.util.ParticleHelper;

public class EntityEvilCrystal extends EntityMob  implements IRangedAttackMob, ISMMob {

	private static final DataParameter<Integer> DATA = EntityDataManager.<Integer>createKey(EntityEvilCrystal.class, DataSerializers.VARINT);
	private int tickTime = 0;
	private int taskTime = 0;
	private int randTime = 0;

	public EntityEvilCrystal(World world) {
		super(world);
        this.experienceValue = 100;
        this.isImmuneToFire = true;
		this.setSize(0.4F, 1.5F);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(DATA, Integer.valueOf((int) 0));
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(4, new EntityAILookIdle(this));
		this.tasks.addTask(5, new EntityAIAttackRanged(this, 1.0D, 60, 9F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.375D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.setHardHealth(this);
		this.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 99999, 9));
		this.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 0));
		return livingdata;
	}

	public void setData (int data) {
		this.dataManager.set(DATA, data);
	}

	public int getData () {
		return this.dataManager.get(DATA);
	}

	protected SoundEvent getAmbientSound() {
		return null;
	}

	protected SoundEvent getHurtSound(DamageSource dame) {
		return SoundEvents.BLOCK_GLASS_FALL;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.BLOCK_GLASS_BREAK;
	}

	public void readEntityFromNBT(NBTTagCompound tags) {
		super.readEntityFromNBT(tags);
		this.setData(tags.getInteger("data"));
	}

	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);
		tags.setInteger("data", this.getData());
	}

	public void onLivingUpdate() {

		if (!this.isEntityAlive()) { return; }

		super.onLivingUpdate();
		this.tickTime++;

		EntityLivingBase entity = this.getAttackTarget();

		if (entity == null) {

			List<EntityPlayer> entityList = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(24D, 6D, 24D));
			if (entityList.isEmpty()) { return; }

			for (EntityPlayer player : entityList) {

				if (player.isCreative()) { continue; }
				this.setAttackTarget(player);
				entity = player;
			}

			if (entity == null) {
				this.taskTime = 0;
				return;
			}
		}

		// 敵が見えないなら終了
		if (!this.canEntityBeSeen(entity) || !entity.isEntityAlive()) { return; }

		if (this.randTime == 0) {
			this.randTime = this.rand.nextInt(100) - this.rand.nextInt(100);
		}

		this.taskTime++;
		this.getNavigator().clearPath();
		this.getLookHelper().setLookPositionWithEntity(entity, 90F, 90F);
		if (this.taskTime % (160 + this.randTime) != 0) { return; }

		entity.attackEntityFrom(DamageSource.MAGIC, (this.getData() + 1F) * 1.5F + 3F);
		entity.hurtResistantTime = 0;
		this.createExplo(entity, (this.getData() + 1F) * 2.5F);
		this.setAttackTarget(null);
		this.taskTime = 0;
	}

	public void createExplo (EntityLivingBase entity, float explo) {

		List<EntityPlayer> entityList = this.world.getEntitiesWithinAABB(EntityPlayer.class, entity.getEntityBoundingBox().grow(explo, explo, explo));
		if (entityList.isEmpty()) { return; }

		for (EntityPlayer player : entityList ) {

			if ( player.isCreative() ) { continue; }

			float dame = explo;
			double distance = 2 - player.getDistance(this.posX, this.posY, this.posZ) / dame;
			dame *= distance * 1.825F;
			player.attackEntityFrom(DamageSource.MAGIC, dame);
			player.hurtResistantTime = 0;
		}

		this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 3F, 1F / (this.rand.nextFloat() * 0.2F + 0.9F));
		ParticleHelper.spawnParticle(this.world, new BlockPos(entity.posX, entity.posY + 0.5, entity.posZ), EnumParticleTypes.EXPLOSION_HUGE);
	}

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.checkBossDamage(src) && !this.isMindControl(this)) {
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

    	// 爆発魔法チェック
    	if (this.checkMagicExplosion(src)) {
    		amount = Math.min(5F, amount);
    	}

    	return super.attackEntityFrom(src, Math.min(10F, amount));
    }

	public void onDeath(DamageSource cause) {

		if (!this.world.isRemote) {
			this.entityDropItem(new ItemStack(this.getItem(), 1), 0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(12) + 1), 0F);
		}
		super.onDeath(cause);
	}

	public Item getItem () {

		Item item = null;
		int data = this.getData();

		switch (data) {
		case 0:
			item = ItemInit.aether_crystal;
			break;
		case 1:
			item = ItemInit.divine_crystal;
			break;
		case 2:
			item = ItemInit.pure_crystal;
			break;
		case 3:
			item = ItemInit.deus_crystal;
			break;
		}

		return item;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distance) { }

	@Override
	public void setSwingingArms(boolean swinging) { }

	// エーテルバリアーなどのエフェクトを表示するかどうか
	public boolean isRenderEffect () {
		return false;
	}
}
