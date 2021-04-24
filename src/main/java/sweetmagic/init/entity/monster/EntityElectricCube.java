package sweetmagic.init.entity.monster;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import sweetmagic.api.iitem.IWand;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.util.PlayerHelper;

public class EntityElectricCube extends EntitySlime implements ISMMob {

    private static final DataParameter<Integer> SLIME_SIZE = EntityDataManager.<Integer>createKey(EntityElectricCube.class, DataSerializers.VARINT);

	public EntityElectricCube (World world) {
		super(world);
        this.experienceValue = 30;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(SLIME_SIZE, Integer.valueOf(1));
	}

	@Override
	public void setSlimeSize(int size, boolean resetHealth) {
		this.dataManager.set(SLIME_SIZE, Integer.valueOf(size));
		this.setSize(0.51000005F * (float) size, 0.51000005F * (float) size);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getHealth(size));
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED) .setBaseValue(this.getSpeed(size));

		if (resetHealth) {
			this.setHealth(this.getMaxHealth());
		}

		this.experienceValue = size * size;
	}

	@Override
	public int getSlimeSize() {
		return ((Integer) this.dataManager.get(SLIME_SIZE)).intValue();
	}

	// サイズに合わして体力を増やす
	public double getHealth (int size) {
		Double health = 1D;
		switch (size) {
		case 1:
			health = 4D;
			break;
		case 2:
			health = 8D;
			break;
		case 4:
			health = 12D;
			break;
		case 8:
			health = 20D;
			break;
		}
		return health;
	}

	// サイズに合わして移動速度を増やす
	public double getSpeed (int size) {
		Double speed = 1D;
		switch (size) {
		case 1:
			speed = 0.65D;
			break;
		case 2:
			speed = 0.6D;
			break;
		case 4:
			speed = 0.55D;
			break;
		case 8:
			speed = 0.5D;
			break;
		}
		return speed;
	}

	// サイズに合わして攻撃力を増やす
	public int getDamage (int size) {
		int damage = 1;
		switch (size) {
		case 1:
			damage = 1;
			break;
		case 2:
			damage = 2;
			break;
		case 4:
			damage = 3;
			break;
		case 8:
			damage = 4;
			break;
		}
		return damage;
	}

	// モブスポーン条件
	public boolean getCanSpawnHere() {
		return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() &&
				this.world.getBlockState((new BlockPos(this)).down()).canEntitySpawn(this) && this.canSpawn(this.world, this, 3);
	}

	// 光レベル
	protected boolean isValidLightLevel() {

		BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

		if (this.world.getLightFor(EnumSkyBlock.SKY, pos) > this.rand.nextInt(32)) {
			return false;
		}

		int i = this.world.getLightFromNeighbors(pos);

		if (this.world.isThundering()) {
			int j = this.world.getSkylightSubtracted();
			this.world.setSkylightSubtracted(10);
			i = this.world.getLightFromNeighbors(pos);
			this.world.setSkylightSubtracted(j);
		}
		return i <= this.rand.nextInt(8);
	}

	@Override
	protected int getAttackStrength() {
		return 0;
	}

	protected void dealDamage(EntityLivingBase entity) {

		int i = this.getSlimeSize();
		if (this.getDistanceSq(entity) >= (0.6D * (double) i * 0.6D * (double) i)) { return; }

		EntityLivingBase living = (EntityLivingBase) entity;

		// メインハンドのアイテムを飛ばす
		if (this.rand.nextInt(8) <= i - 1) {
			this.outStack(living);
		}

		// ダメージを与える
		this.electricAttack(living, this.getDamage(i));

		// 近くにいるプレイヤーダメージ
		if ((this.world.isRaining() || living.isInWater()) && living instanceof EntityPlayer) {
			this.rangeElectricDamage((EntityPlayer) living);
		}
	}

	// 近くにいるプレイヤーダメージ
	public void rangeElectricDamage (EntityPlayer player) {

		BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
		List<EntityPlayer> playerList = this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-5, -2, -5), pos.add(5, 2, 5)));

		for (EntityPlayer pl : playerList) {

			if (pl == player) { continue; }

			// ダメージを与える
			this.electricAttack(player, 1);
		}
	}

	// メインハンドのアイテムを飛ばす
	public void outStack (EntityLivingBase living) {

		if (living.isPotionActive(PotionInit.refresh_effect) || PlayerHelper.isPlayer(living) && PlayerHelper.isCleative((EntityPlayer) living)) { return; }

		ItemStack stack = living.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

		if (!stack.isEmpty() && !(stack.getItem() instanceof IWand) && !this.world.isRemote) {

			Random rand = this.world.rand;
			Double x = this.posX + rand.nextDouble() * 2 - 1D;
			Double z = this.posZ + rand.nextDouble() * 2 - 1D;
			Double moX = (rand.nextDouble() - 0.5D) * 0.75D;
			Double moZ = (rand.nextDouble() - 0.5D) * 0.75D;

			ItemStack copyStack = stack.copy();
			copyStack.setCount(1);
			EntityItem item = new EntityItem(this.world, x + 0.5, this.posY + 1.5, z + 0.5, copyStack);
			item.motionX += moX;
			item.motionY += 0.125;
			item.motionZ += moZ;
			this.world.spawnEntity(item);
			stack.shrink(1);

			this.world.playSound(null, new BlockPos(living) , SMSoundEvent.ELECTRIC, SoundCategory.NEUTRAL, 0.175F, 1F);
		}
	}

	// ダメージを与える
	public void electricAttack (EntityLivingBase entitiy, int dama) {
		entitiy.attackEntityFrom(DamageSource.causeMobDamage(this), dama);
	}

	@Override
	protected boolean canDamagePlayer() {
		return true;
	}

	@Override
	protected EntitySlime createInstance() {
		return new EntityElectricCube(this.world);
	}

	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
			this.entityDropItem(new ItemStack(ItemInit.electronic_orb, 1), 0.0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(2)), 0F);
		}
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {

		// スポーンする最大段階
		int i = this.rand.nextInt(this.isDayElapse(this.world, 10) ? 2 : 3);

		if (i < 2 && this.rand.nextFloat() < 0.5F * difficulty.getClampedAdditionalDifficulty()) {
			++i;
		}

		int j = 1 << i;
		this.setSlimeSize(j, true);

		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE)
				.applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));

		if (this.rand.nextFloat() < 0.05F) {
			this.setLeftHanded(true);
		} else {
			this.setLeftHanded(false);
		}

		return livingdata;
	}

	@Override
    public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src)) {
    		return false;
		}

		// ダメージ倍処理
		amount = this.getDamageAmount(this.world , src, amount);

		return super.attackEntityFrom(src, amount);
	}
}
