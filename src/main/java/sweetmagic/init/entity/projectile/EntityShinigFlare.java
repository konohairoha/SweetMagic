package sweetmagic.init.entity.projectile;

import java.util.List;
import java.util.Random;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.api.iitem.IWand;
import sweetmagic.client.particle.ParticleEntityMagicLight;
import sweetmagic.init.PotionInit;
import sweetmagic.util.ParticleHelper;

public class EntityShinigFlare extends EntityBaseMagicShot {

	public EntityShinigFlare(World world) {
		super(world);
		this.setSize(0.5F, 0.5F);
	}

	public EntityShinigFlare(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityShinigFlare(World world, EntityLivingBase thrower, ItemStack stack) {
		super(world, thrower, stack);
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		if (this.world.isRemote) { return; }

		// 経験値追加処理
		this.addExp();
		this.rangeAttack(this.getWandLevel() / 2);
		this.setEntityDead();
	}

	protected void onHit(RayTraceResult result) {

		if (this.world.isRemote) {

			Random rand = this.world.rand;

			for (int i = 0; i < 16; i++) {

				double x = this.posX + rand.nextFloat() - 0.5F;
				double z = this.posZ + rand.nextFloat() - 0.5F;
				double f1 = rand.nextDouble() - 0.5D;
				double f2 = rand.nextDouble() * 0.25D;
				double f3 = rand.nextDouble() - 0.5D;

				this.world.spawnParticle(EnumParticleTypes.FLAME, x, this.posY, z, f1, f2, f3);

				Particle effect = new ParticleEntityMagicLight.Factory().createParticle(0, this.world, x, this.posY, z, f1, f2, f3);
				ParticleHelper.spawnParticl().addEffect(effect);
			}
		}

		// 経験値追加処理
//		this.addExp();
		super.onHit(result);
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		float x = (float) (-this.motionX / 80);
		float y = (float) (-this.motionY / 80);
		float z = (float) (-this.motionZ / 80);
		float xf = (float) (-this.motionX / 16);
		float yf = (float) (-this.motionY / 16);
		float zf = (float) (-this.motionZ / 16);

		for (int i = 0; i < 8; i++) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
			Particle effect = new ParticleEntityMagicLight.Factory().createParticle(0, this.world, f1, f2, f3, x, y, z);
			ParticleHelper.spawnParticl().addEffect(effect);
		}

		for (int i = 0; i < 4; i++) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
			this.world.spawnParticle(EnumParticleTypes.FLAME, f1, f2, f3, xf, yf, zf);
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		this.rangeAttack(1F);

		// 経験値追加処理
		this.addExp();
		this.setEntityDead();
	}

	public void rangeAttack (float addDamage) {

		addDamage += this.getDamage() / 2;
		int level = this.getWandLevel();
		double range = 5D + IWand.getWand(this.stack).getLevel(this.stack) * 0.75;
		List<EntityLivingBase> entityList = this.getEntityList(range, range * 0.5, range);

		for (EntityLivingBase entity : entityList) {

			if (!(entity instanceof IMob)) { continue; }

			this.attackDamage(entity, (float) range + addDamage);
			entity.hurtResistantTime = 0;
			this.checkShadow(entity);

			BlockPos pos = new BlockPos(entity);
			ParticleHelper.spawnBoneMeal(this.world, pos, EnumParticleTypes.END_ROD);
			ParticleHelper.spawnBoneMeal(this.world, pos.up(), EnumParticleTypes.END_ROD);

			// バフなら
			if (entity.isNonBoss()) {
				for (Potion potion : PotionInit.getBuffPotionList()) {
					entity.removePotionEffect(potion);
				}
			}

			entity.addPotionEffect(new PotionEffect(PotionInit.flame, 40 * (level + 1), 1));
			entity.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 40 * level, 0));
		}
	}
}
