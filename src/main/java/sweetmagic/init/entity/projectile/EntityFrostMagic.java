package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.client.particle.ParticleIceCrystal;
import sweetmagic.client.particle.ParticleMagicFrost;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.PotionInit;

public class EntityFrostMagic extends EntityBaseMagicShot {

	public EntityFrostMagic(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	public EntityFrostMagic(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityFrostMagic(World world, EntityLivingBase thrower, ItemStack stack, boolean isHitDead) {
		super(world, thrower, stack);
		this.isHitDead = isHitDead;
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		if (this.motionX == 0 || this.motionZ == 0) {
			this.rangeAttack(1.5D);
		}

		this.setEntityDead();
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		if (this.motionX != 0 && this.motionZ != 0) {

			float x = (float) (-this.motionX / 240);
			float y = (float) (-this.motionY / 240);
			float z = (float) (-this.motionZ / 240);

			int count = this.isHitDead ? 12 : 6;

			for (int i = 0; i < count; i++) {
				float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
				float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
				float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
				Particle effect = new ParticleMagicFrost.Factory().createParticle(0, this.world, f1, f2, f3, x, y, z);
				FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
			}
		}

		else if (this.rand.nextInt(4) == 0) {

			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat());
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat());
			Particle effect = new ParticleIceCrystal.Factory().createParticle(0, this.world, f1, f2, f3, 0, 0, 0);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		int level = this.getWandLevel();
		int addTime = living.isBurning() ? 2 : 0;

		// 経験値追加処理
		this.addExp();
		this.playSound(living, SMSoundEvent.FROST, 0.25F, 1F);
		living.addPotionEffect(new PotionEffect(PotionInit.frosty, 40 * (level + 1 + addTime), this.isHitDead ? 3 : 2));
		living.hurtResistantTime = 0;

		if (this.motionX == 0 || this.motionZ == 0) {
			this.rangeAttack(0.75D);
		}
	}

	public void rangeAttack (double range) {

		List<EntityLivingBase> list = this.getEntityList(range, range / 2, range);
		float dame = (float) range;

		for (EntityLivingBase entity : list ) {

			if (!this.checkThrower(entity)) { continue; }

			this.attackDamage(entity, dame);
			entity.hurtResistantTime = 0;
			this.checkShadow(entity);
			entity.addPotionEffect(new PotionEffect(PotionInit.frosty, 100, 4));
		}
	}
}
