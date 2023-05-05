package sweetmagic.init.entity.monster;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.config.SMConfig;
import sweetmagic.init.DimensionInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityCyclonMagic;
import sweetmagic.init.entity.projectile.EntityExplosionMagic;
import sweetmagic.init.entity.projectile.EntityLightMagic;
import sweetmagic.init.entity.projectile.EntityMagicItem;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.SMDamage;

public interface ISMMob {

	// 魔法攻撃なら2倍
	default float getDamageAmount (World world, DamageSource src, float dame) {

		// スイートマジックディメンションなら
		if (this.isSMDimension(world)) {
			float rate = 1F;

			switch (this.getStrength()) {
			case 0: rate = 1.25F;
			break;
			case 2: rate = 0.75F;
			break;
			case 3: rate = 0.65F;
			break;
			}

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

	// えんちちーリスト取得
	default <T extends Entity> List<T> getEntityList (Class <? extends T > classEntity, Entity entity, double x, double  y, double  z) {
		return entity.world.getEntitiesWithinAABB(classEntity, this.getAABB(entity, x, y, z));
	}

	// 範囲の取得
	default AxisAlignedBB getAABB (Entity entity, double x, double  y, double  z) {
		return entity.getEntityBoundingBox().grow(x, y, z);
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

    // 爆発魔法かどうか
	default boolean checkMagicExplosion(DamageSource src) {
    	return src.getImmediateSource() instanceof EntityExplosionMagic;
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

	default void dropItem (World world, EntityLivingBase entity, Block block, int amount) {
		this.dropItem(world, entity, new ItemStack(block, amount));
	}

	default void dropItem (World world, EntityLivingBase entity, ItemStack stack) {
		EntityMagicItem entityItem = new EntityMagicItem(world, entity.posX, entity.posY, entity.posZ, stack);
        entityItem.setDefaultPickupDelay();
        world.spawnEntity(entityItem);
	}

	// 高難易度か
	default int getStrength() {
		return SMConfig.isHard;
	}

	default void setHardHealth (EntityLivingBase entity) {

		// 通常の難易度なら終了
		if (this.getStrength() != 1) { return; }

		float maxHealth = entity.getMaxHealth();

		switch (this.getStrength()) {
		case 0: maxHealth = Math.max(entity.getMaxHealth() * 0.75F, 1.5F);
		break;
		case 2: maxHealth = Math.max(entity.getMaxHealth() * 1.25F, 1.5F);
		break;
		case 3: maxHealth = Math.max(entity.getMaxHealth() * 1.5F, 1.5F);
		break;
		}

		entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
		entity.setHealth(maxHealth);
	}

	// レンダーを軽減かどうか
	default boolean isRender () {
		return !SMConfig.isRender;
	}

	// 杖を返す
	default ItemStack getWandHand () {
		return ItemStack.EMPTY;
	}

	// 背中のアイテムを返す
	default ItemStack getBackPack () {
		return ItemStack.EMPTY;
	}

	// 防具のタイプ
	default int getArmorType () {
		return 0;
	}

	default ArmMode getArm () {
		return ArmMode.NONE;
	}

	default float swingAmout () {
		return 0F;
	}

	// エーテルバリアーなどのエフェクトを表示するかどうか
	default boolean isRenderEffect () {
		return true;
	}

	default ParticleManager getParticle () {
		return ParticleHelper.spawnParticl();
	}

	default boolean getSpecial () {
		return false;
	}

	default void setSpecial (boolean isSpecial) { }

	public enum ArmMode {

		NONE,
		MAGIC,
		SPECIAL_MAGIC,
		RIGHT_MAGIC,
		LEFT_MAGIC,
		RIGHT_SHOT,
		LEFT_SHOT,
		RIGHT_SET,
		LEFT_SET,
		BOTH_SET,
		RIGHT_SWING;

		public boolean isSpecial () {
			return this == SPECIAL_MAGIC;
		}
	}
}
