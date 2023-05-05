package sweetmagic.init.entity.projectile;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.client.particle.ParticleBabule;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.PotionInit;

public class EntityBabuleMagic extends EntityBaseMagicShot {

	public int potionnLevel;

	public EntityBabuleMagic(World world) {
		super(world);
		this.setSize(0.5F, 0.5F);
	}

	public EntityBabuleMagic(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityBabuleMagic(World world, EntityLivingBase thrower, ItemStack stack, int level) {
		super(world, thrower, stack);
		this.potionnLevel = level;
	}

	// 水中での速度減衰
	protected float inWaterSpeed(RayTraceResult raytraceresult) {
		return 1F;
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {
		this.setEntityDead();
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		float x = (float) (-this.motionX / 240);
		float y = (float) (-this.motionY / 240);
		float z = (float) (-this.motionZ / 240);

		int count = this.isHitDead ? 12 : 6;

		for (int i = 0; i < count; i++) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i * 0.25F);
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
			Particle effect = ParticleBabule.create(this.world, f1, f2, f3, x, y, z);
			this.getParticle().addEffect(effect);
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		int level = this.getWandLevel();
		this.playSound(SMSoundEvent.BABULE, 1F, 1F);


		// プレイヤーが攻撃した場合
		if (this.isPlayerThrower) {

			// 経験値追加処理
			this.addExp();

			int value = 1 + (this.potionnLevel - 1);
			for (int i = 0; i < value ; i++) {
				this.attackDamage(living, (float) this.getDamage());
			}
		}


		this.playSound(living, SoundEvents.ENTITY_PLAYER_SPLASH, 0.5F, 1.25F);
		int time = 10 * level;

		if (this.potionnLevel > 3) {
			time = Math.min(100, time);
		}

		living.addPotionEffect(new PotionEffect(PotionInit.babule, time, this.potionnLevel));
		living.hurtResistantTime = 0;

		if (this.potionnLevel >= 2 && living.isPotionActive(PotionInit.refresh_effect) && living.isNonBoss()) {
			living.removePotionEffect(PotionInit.refresh_effect);
		}

	}
}
