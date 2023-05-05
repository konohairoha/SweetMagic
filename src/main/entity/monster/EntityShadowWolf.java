package sweetmagic.init.entity.monster;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.item.sm.sweetmagic.SMBook;

public class EntityShadowWolf extends EntityWolf {

	private static final int DISPEL_TIME = 10;
	private static final DataParameter<Boolean> ISPROTECT = EntityDataManager.<Boolean>createKey(EntityShadowWolf.class, DataSerializers.BOOLEAN);

	public EntityShadowWolf(World world) {
		super(world);
		this.experienceValue = 0;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ISPROTECT, false);
	}

	public void setProtect(boolean isProtect) {
		this.dataManager.set(ISPROTECT, isProtect);
	}

	public boolean getProtect() {
		return this.dataManager.get(ISPROTECT);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.setProtect(tag.getBoolean("isProtect"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setBoolean("isProtect", this.getProtect());
	}

	@Override
	protected void initEntityAI() {
		this.aiSit = new EntityAISit(this);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(9, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		if (this.world.isRemote && this.world.rand.nextBoolean()) {
			this.spawnAppearParticles();
		}
		return livingdata;
	}

	// パーティクルスポーン
	private void spawnAppearParticles() {
		for (int k = 0; k < 1; ++k) {
			this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH,
					this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
					this.posY + this.rand.nextDouble() * (double) this.height - 0.25D,
					this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width,
					(this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
					(this.rand.nextDouble() - 0.5D) * 2.0D);
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!this.world.isRemote) { return; }
		this.spawnAppearParticles();
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (this.isTamed()) {

			if (player.isSneaking() && player.getHeldItem(hand).getItem() instanceof SMBook) {
				this.isDead = true;
			}

			ItemStack stack = player.getHeldItem(hand);
			if (stack.getItem() instanceof IWand && this.getOwner() == player && player.isSneaking()) {
				if (this.ticksExisted > 20) { return true; }
			}
		}

		return super.processInteract(player, hand);
	}

	public boolean attackEntityFrom(DamageSource src, float amount) {

		if (this.getProtect()) {
			return false;
		}

    	if (src.getImmediateSource() instanceof ISMMob) {
    		amount *= 0.5F;
		}

    	else if (src.getImmediateSource() instanceof EntityBaseMagicShot || src == DamageSource.MAGIC) {
    		amount *= 0.25F;
    	}

		return super.attackEntityFrom(src, amount);
	}

	@Override
	public void onDeath(DamageSource src) {
        this.dead = true;
        this.getCombatTracker().reset();
	}

	@Override
	public EntityWolf createChild(EntityAgeable par1EntityAgeable) {
		return null;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return 0;
	}

	@Override
	protected boolean canDropLoot() {
		return false;
	}

	@Override
	protected Item getDropItem() {
		return null;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}
}
