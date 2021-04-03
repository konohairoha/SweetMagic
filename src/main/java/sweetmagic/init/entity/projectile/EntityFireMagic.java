package sweetmagic.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.init.PotionInit;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.PlayerHelper;

public class EntityFireMagic extends EntityBaseMagicShot {

	public boolean isFire;

	public EntityFireMagic(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	public EntityFireMagic(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityFireMagic(World world, EntityLivingBase thrower, ItemStack stack) {
		super(world, thrower, stack);
		this.isFire = true;
	}

	public void onUpdate() {
		super.onUpdate();
		if (this.isFire) {
			this.setFire(1);
		}
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {
		this.setEntityDead();
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		float x = (float) (-this.motionX / 16);
		float y = (float) (-this.motionY / 16);
		float z = (float) (-this.motionZ / 16);

		for (int i = 0; i < 4; i++) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
			this.world.spawnParticle(EnumParticleTypes.FLAME, f1, f2, f3, x, y, z);
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		int level = this.getWandLevel();

		if (PlayerHelper.isThowerPlayer(living, this.getThrower())) {
			living.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 60 * (level + 1), 0));
		} else {
			living.setFire(3 * (level + 1));
		}

		// 相手が氷状態なら
		if (living.isPotionActive(PotionInit.frosty)) {

			// ポーション時間の取得
			float dame = living.getActivePotionEffect(PotionInit.frosty).getDuration() / 80 + 1;
			living.removePotionEffect(PotionInit.frosty);
			this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 1F, 1F);
			ParticleHelper.spawnBoneMeal(this.world, this.getPosition(), EnumParticleTypes.CLOUD);

			// 射撃者がプレイヤーなら
			if (PlayerHelper.isPlayer(this.getThrower())) {
				living.attackEntityFrom(DamageSource.causePlayerDamage(this.getPlayer()), dame);
			}

			// それ以外
			else {
				living.attackEntityFrom(DamageSource.causeMobDamage(this.getThrower()), dame);
			}
		}

		// 経験値追加処理
		this.addExp();
	}
}
