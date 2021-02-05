package sweetmagic.init.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;

public class EntityArchSpider extends EntitySpider implements ISMMob {

	public EntityArchSpider(World world) {
		super(world);
        this.experienceValue = 70;
	}

	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(48.0D);
    }

	public boolean attackEntityAsMob(Entity entity) {
		if (super.attackEntityAsMob(entity)) {
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase living = (EntityLivingBase) entity;
				int time = this.isUnique() ? 300: 200;
				int level = this.isUnique() ? 1: 0;
				living.addPotionEffect(new PotionEffect(PotionInit.deadly_poison, time, level));
			}
		}
		return true;
	}

	// 二つ名かどうか
    public boolean isUnique () {
    	return this.getMaxHealth() >= 40F;
    }

	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
			this.entityDropItem(new ItemStack(ItemInit.poison_bottle, this.rand.nextInt(2) + 1), 0F);
			this.entityDropItem(new ItemStack(ItemInit.aether_crystal_shard, this.rand.nextInt(5)), 0F);
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

	@Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData living) {

		living = super.onInitialSpawn(difficulty, living);
		float f = difficulty.getClampedAdditionalDifficulty();
		this.setCanPickUpLoot(this.rand.nextFloat() < 0.35F * f);

		if (this.rand.nextFloat() < 0.03F) {
			EntitySkullFrost chicken = new EntitySkullFrost(this.world);
			chicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
			chicken.onInitialSpawn(difficulty, (IEntityLivingData) null);
			chicken.setChickenJockey(true);
			this.world.spawnEntity(chicken);
			chicken.startRiding(this);
		}

    	return super.onInitialSpawn(difficulty, living);
	}

	// モブスポーン条件
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && this.canSpawn(this.world, this, 3);
	}
}
