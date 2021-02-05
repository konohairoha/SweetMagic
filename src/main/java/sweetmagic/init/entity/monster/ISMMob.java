package sweetmagic.init.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.init.DimensionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.util.SMDamage;

public interface ISMMob {

	// 魔法攻撃なら2倍
	default float getDamageAmount (World world, DamageSource src, float dame) {

		if (this.isSMDimension(world)) {
			return this.isSMDamage(src) ? dame * 0.85F : dame * 0.25F;
		}

		else {
			return this.isSMDamage(src) ? dame * 2 : dame;
		}

	}

	// 魔法によるダメージか？
	default boolean isSMDamage (DamageSource src) {
    	Entity entity = src.getImmediateSource();
    	return src instanceof SMDamage || entity instanceof EntityBaseMagicShot;
	}

	// 攻撃したのがSMMobかどうか
	default boolean isAtterckerSMMob (DamageSource src) {
		return src.getImmediateSource() instanceof ISMMob || src.getTrueSource() instanceof ISMMob;
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
}
