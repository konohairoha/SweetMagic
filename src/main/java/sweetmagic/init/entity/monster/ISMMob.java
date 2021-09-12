package sweetmagic.init.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.config.SMConfig;
import sweetmagic.init.DimensionInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityCyclonMagic;
import sweetmagic.init.entity.projectile.EntityLightMagic;
import sweetmagic.init.entity.projectile.EntityMagicItem;
import sweetmagic.util.SMDamage;

public interface ISMMob {

	// 魔法攻撃なら2倍
	default float getDamageAmount (World world, DamageSource src, float dame) {

		// スイートマジックディメンションなら
		if (this.isSMDimension(world)) {
			float rate = this.isHard() ? 0.75F : 1F;
			return this.isSMDamage(src) ? dame * 0.75F * rate : dame * 0.25F * rate;
		}

		// それ以外
		else {
			return this.isSMDamage(src) ? dame * 2 : dame;
		}
	}

	// 魔法によるダメージか？
	default boolean isSMDamage (DamageSource src) {
    	Entity entity = src.getImmediateSource();
    	return src instanceof SMDamage || entity instanceof EntityBaseMagicShot;
	}

	// ボスのダメージチェック
	default boolean checkBossDamage (DamageSource src) {
		return this.isAtterckerSMMob(src) || !this.isSMDamage(src);
	}

	// 攻撃したのがSMMobかどうか
	default boolean isAtterckerSMMob (DamageSource src) {
		Entity entity = src.getImmediateSource();
		Entity source = src.getTrueSource();
		return this.checkAttacker(entity) || this.checkAttacker(source);
	}

	// 攻撃した相手を確認
	default boolean checkAttacker (Entity entity) {
		return ( entity instanceof ISMMob && !this.isMindControl((EntityLivingBase) entity) ) || entity == this;
	}

	// 経過日数を満たしていないなら
	default boolean isDayElapse (World world, int day) {
		return world.getTotalWorldTime() < (day * 24000);
	}

	// スポーン条件
	default boolean canSpawn (World world, Entity entity, int day) {
		return ( world.canSeeSky(new BlockPos(entity)) && !this.isDayElapse(world, day) ) ||
				this.isSMDimension(world);
	}

	// SMディメンションかどうか
	default boolean isSMDimension (World world) {
		return world.provider.getDimension() == DimensionInit.dimID;
	}

    // 風魔法かどうか
	default boolean checkMagicCyclone (DamageSource src) {
    	return src.getImmediateSource() instanceof EntityCyclonMagic;
    }

    // 光魔法かどうか
	default boolean checkMagicLight (DamageSource src) {
    	return src.getImmediateSource() instanceof EntityLightMagic;
    }

	// マインドコントロールされていないかどうか
	default boolean isMindControl (EntityLivingBase entity) {
		return entity.isPotionActive(PotionInit.mind_control);
	}

	// 死亡キャンセル付いてるなら
	default boolean isDethCancel (EntityLivingBase entity) {
		return entity.isPotionActive(PotionInit.refresh_effect) && entity.getActivePotionEffect(PotionInit.refresh_effect).getAmplifier() >= 1;
	}

	default void dropItem (World world, EntityLivingBase entity, Item item, int amount) {
		this.dropItem(world, entity, new ItemStack(item, amount));
	}

	default void dropItem (World world, EntityLivingBase entity, ItemStack stack) {
		EntityMagicItem entityItem = new EntityMagicItem(world, entity.posX, entity.posY, entity.posZ, stack);
        entityItem.setDefaultPickupDelay();
        world.spawnEntity(entityItem);
	}

	// 高難易度か
	default boolean isHard () {
		return SMConfig.isHard;
	}

	default void setHardHealth (EntityLivingBase entity) {

		// ハード以外なら終了
		if (!this.isHard()) { return; }

		float maxHealth = Math.max(entity.getMaxHealth() * 1.25F, 10F);
		entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
		entity.setHealth(maxHealth);
	}

	// レンダーを軽減かどうか
	default boolean isRender () {
		return SMConfig.isRender;
	}
}
