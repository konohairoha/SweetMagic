package sweetmagic.init.entity.monster;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.client.particle.ParticleMagicFrost;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityFrostMagic;

public class EntitySkullFrost extends EntitySkeleton implements ISMMob {

	int tickTime = 0;
	private int teleportDelay;
	Random rand = new Random();
    public boolean chickenJockey;

	public EntitySkullFrost(World world) {
		super(world);
		this.experienceValue = 40;
		this.isImmuneToFire = true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.225D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE)
				.applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
		this.setLeftHanded(this.rand.nextFloat() < 0.05F);
		this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());
		if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) {
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
		}
		return livingdata;
	}

	@Override
	protected EntityArrow getArrow(float par1) {
		EntityArrow arrow = super.getArrow(par1);
		if (arrow instanceof EntityTippedArrow) {
			int time = !this.isUnique() ? 100 : 400;
			int level = this.isUnique() ? 3 : 1;
			((EntityTippedArrow) arrow).addEffect(new PotionEffect(PotionInit.frosty, time, level));
		}
		return arrow;
	}

	public void attackEntityWithRangedAttack(EntityLivingBase target, float par1) {

		// ユニーク状態なら
		if (this.isUnique()) {

			EntityBaseMagicShot entity = new EntityFrostMagic(this.world, this, ItemStack.EMPTY, false);
	        double x = target.posX - this.posX;
	        double y = target.getEntityBoundingBox().minY - target.height / 2  - this.posY;
	        double z = target.posZ - this.posZ;
	        double xz = (double)MathHelper.sqrt(x * x + z * z);
			entity.shoot(x, y - xz * 0.015D, z, 2F, 0);	// 射撃速度
			entity.setDamage(entity.getDamage() + 6);
			this.world.playSound(null, new BlockPos(this), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.67F);
	        this.world.spawnEntity(entity);
		}

		else {

			EntityArrow entity = this.getArrow(par1);
			if (this.getHeldItemMainhand().getItem() instanceof ItemBow) {
				entity = ((ItemBow) this.getHeldItemMainhand().getItem()).customizeArrow(entity);
			}
			double d0 = target.posX - this.posX;
			double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - entity.posY;
			double d2 = target.posZ - this.posZ;
			double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
			entity.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 0);
			entity.setDamage(entity.getDamage() + (this.isDayElapse(this.world, 5) ? 0 : 2));
			this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1F, 1F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			this.world.spawnEntity(entity);
		}

	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
	}

	// モブスポーン条件
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.canSpawn(world, this, 3);
	}

	// 二つ名かどうか
    public boolean isUnique () {
    	return this.getMaxHealth() >= 40F;
    }

	@Override
	public void onLivingUpdate() {

		super.onLivingUpdate();
		if (!this.world.isRemote) { return; }

		this.tickTime++;
		if (this.tickTime % 60 != 0) { return; }

		this.tickTime = 0;
		this.spawnParticle();
	}

	public void spawnParticle() {

		float x = (float) (-this.motionX / 80);
		float y = (float) (-this.motionY / 80);
		float z = (float) (-this.motionZ / 80);

		for (int i = 0; i < 6; i++) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
			float f2 = (float) (this.posY + 0.5F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
			Particle effect = new ParticleMagicFrost.Factory().createParticle(0, this.world, f1, f2, f3, x, y, z);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

	// えんちちーの光の大きさ
	public float getBrightness() {
		return 1.0F;
	}

	public boolean isChickenJockey() {
		return this.chickenJockey;
	}

	public void setChickenJockey(boolean jockey) {
		this.chickenJockey = jockey;
	}

    public void readEntityFromNBT(NBTTagCompound tags) {
        super.readEntityFromNBT(tags);
		this.chickenJockey = tags.getBoolean("IsChickenJockey");
    }

    public void writeEntityToNBT(NBTTagCompound tags) {
        super.writeEntityToNBT(tags);
        tags.setBoolean("IsChickenJockey", this.chickenJockey);
    }

	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(5)), 0F);
			this.entityDropItem(new ItemStack(ItemInit.unmeltable_ice, this.rand.nextInt(3) + 1), 0F);
		}
	}

	public boolean attackEntityFrom(DamageSource src, float amount) {

    	if (this.isAtterckerSMMob(src)) {
    		return false;
		}

		// ダメージ倍処理
		amount = this.getDamageAmount(this.world , src, amount);

		return super.attackEntityFrom(src, amount);
	}
}
