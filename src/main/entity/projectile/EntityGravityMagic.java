package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.util.PlayerHelper;

public class EntityGravityMagic extends EntityBaseMagicShot {


	public EntityGravityMagic(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	public EntityGravityMagic(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityGravityMagic(World world, EntityLivingBase thrower, ItemStack stack) {
		super(world, thrower, stack);

		if (thrower instanceof EntityPlayer) {
			this.isRange = this.hasAcce((EntityPlayer) thrower, ItemInit.extension_ring);
		}
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		// 貫通時
		if (this.range > 0D) {

			this.ticksInAir = 0;
			this.tickTime++;
			double range = this.isRange ? this.range * 1.33D : this.range;

			List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, range, range, range);

			for (EntityLivingBase ent : list) {

				// カラミティまたは重力加速があるなら次へ
				if (!this.checkThrower(ent)) { continue; }

				// 吸い込み
				this.gravitySuction(ent);
			}

			// 一定時間経つとダメージ
			if (this.tickTime > 12) {

				float power = this.range == 3 ? 0.5F : 0.825F;
				float dame =  power * this.getWandLevel() + (this.isRange ? 8F : 1F);
				List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, range, range, range);

				for (EntityLivingBase ent : entityList) {

					if (PlayerHelper.isPlayer(ent)) { continue; }
					this.attackDamage(ent, dame);
					ent.hurtResistantTime = 0;
					ent.addPotionEffect(new PotionEffect(PotionInit.gravity, 40 * (level + 1), 2));
					this.checkShadow(ent);
				}

				// 経験値追加処理
				this.addExp();
				this.setEntityDead();
			}
		}

		else {
			super.inGround(result);
		}
	}

	public void gravitySuction (EntityLivingBase entity) {

		double dX = this.posX - entity.posX;
		double dY = this.posY - entity.posY;
		double dZ = this.posZ - entity.posZ;
		double dist = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
		double vel = 1D - dist / 15D;

		if (vel > 0D) {
			vel *= dist * vel;
			entity.motionX += dX / vel * 0.0775;
			entity.motionY += dY / vel * 0.135;
			entity.motionZ += dZ / vel * 0.0775;
		}
	}

	public void onUpdate() {

		super.onUpdate();

		// 貫通時
		if (this.inGround && this.range > 0D && this.world.isRemote) {

			for(int k = 0; k <= 4; k++) {
				float randX = (this.rand.nextFloat() - this.rand.nextFloat()) * 2.5F;
				float randY = (this.rand.nextFloat() - this.rand.nextFloat()) * 1.5F;
				float randZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 2.5F;

				float x = (float) (this.posX - 0.5F + randX);
				float y = (float) (this.posY + 2F + randY);
				float z = (float) (this.posZ - 0.5F + randZ);
				float xSpeed = -randX * 0.075F;
				float ySpeed = -randY * 0.075F;
				float zSpeed = -randZ * 0.075F;

				Particle effect = ParticleNomal.create(this.world, x, y, z, xSpeed, ySpeed, zSpeed);
				this.getParticle().addEffect(effect);
			}
		}
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		for (int i = 0; i < 6; i++) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
			float f2 = (float) (this.posY - 0.75F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
			float x = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
			float y = (this.rand.nextFloat() + this.rand.nextFloat()) * 0.15F;
			float z = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
			Particle effect = ParticleNomal.create(this.world, f1, f2, f3, x, y, z);
			this.getParticle().addEffect(effect);
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		// 貫通しないとき
		if (!this.isHit) {
			int level = this.getWandLevel();

			// 経験値追加処理
			this.addExp();
			living.addPotionEffect(new PotionEffect(PotionInit.gravity, 40 * (level + 1), 2));
			this.setEntityDead();
		}
	}
}
