package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import sweetmagic.init.LootTableInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBlastBomb;

public class EntitySilderGhast extends EntityGhast implements ISMMob {

	private int tickTime = 0;
	private BlockPos boundOrigin;
    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.<Boolean>createKey(EntitySilderGhast.class, DataSerializers.BOOLEAN);

	public EntitySilderGhast(World world) {
		super(world);
        this.setSize(1.5F, 1.5F);
        this.isImmuneToFire = true;
        this.experienceValue = 80;
		this.moveHelper = new EntitySilderGhast.AIMoveControl(this);
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ATTACKING, Boolean.valueOf(false));
	}

	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(8, new EntitySilderGhast.AIMoveRandom());
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 24F));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
	}

	// スポーン時の設定
	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance dif, IEntityLivingData living) {
		this.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 99999, 1));
		this.setHardHealth(this);
    	return super.onInitialSpawn(dif, living);
	}

	@Override
	protected void updateAITasks() {

		this.tickTime++;
		super.updateAITasks();

		EntityLivingBase target = this.getAttackTarget();
		if (target == null) {

			if (this.tickTime % 80 == 0) {
				this.tickTime = 0;
				List<EntityPlayer> playerList = this.getEntityList(EntityPlayer.class, this, 32D, 32D, 32D);

				for (EntityPlayer player : playerList) {

					if (player.isCreative() || player.isSpectator()) { continue; }

					this.setAttackTarget(player);
				}
			}

			return;
		}

		if (this.posY - target.posY > 7) {
			this.motionY -= 0.005D;
		}

		else if (this.posY - target.posY <= 7) {
			this.motionY += 0.005D;
		}


		if (this.tickTime >= 90 && !this.isAttacking()) {
			this.setAttacking(true);
			this.playSound(SoundEvents.ENTITY_GHAST_WARN, 2F, 1.125F);
		}

		if (this.tickTime % 100 != 0) { return; }

		this.setAttacking(false);
		this.tickTime = 0;

		EntityBlastBomb entity = new EntityBlastBomb(this.world, this, ItemStack.EMPTY, 0);
		double x = target.posX - this.posX;
		double y = target.getEntityBoundingBox().minY - target.height / 2 - this.posY;
		double z = target.posZ - this.posZ;
		double xz = (double) MathHelper.sqrt(x * x + z * z);

		entity.shoot(x, y - xz * 0.015D, z, 2F, 0); // 射撃速度
		entity.setDamage(entity.getDamage() + 6);
		this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
		this.world.spawnEntity(entity);
	}

	@Override
	protected float getSoundVolume() {
		return 0.25F;
	}

	@Override
	protected float getSoundPitch() {
		return 1.125F;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 4;
	}

	// エーテルバリアーなどのエフェクトを表示するかどうか
	public boolean isRenderEffect () {
		return false;
	}

	public boolean isAttacking() {
		return ((Boolean) this.dataManager.get(ATTACKING)).booleanValue();
	}

	public void setAttacking(boolean attack) {
		this.dataManager.set(ATTACKING, Boolean.valueOf(attack));
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableInit.SILDER_GHAST;
	}

    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src) && !this.isMindControl(this)) {
    		return false;
		}

		// ダメージ倍処理
		return super.attackEntityFrom(src, this.getDamageAmount(this.world , src, amount));
	}

	@Nullable
	public BlockPos getBoundOrigin() {
		return this.boundOrigin;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {
		super.readEntityFromNBT(tags);
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

	public class AIMoveControl extends EntityMoveHelper {

		private EntitySilderGhast entity;

		public AIMoveControl(EntitySilderGhast entity) {
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

		private EntitySilderGhast entity = EntitySilderGhast.this;

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
}
