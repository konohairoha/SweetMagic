package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.client.particle.ParticleNomal;
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
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		// 貫通時
		if (this.range > 0D) {

			this.ticksInAir = 0;
			this.tickTime++;
			List<EntityLivingBase> list = this.getEntityList(this.range, this.range * 0.5D, this.range);

			for (EntityLivingBase ent : list) {

				// カラミティまたは重力加速があるなら次へ
				if (!(ent instanceof IMob)) { continue; }

				double dX = this.posX - ent.posX;
				double dY = this.posY - ent.posY;
				double dZ = this.posZ - ent.posZ;
				double dist = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
				double vel = 1D - dist / 15D;

				if (vel > 0.0D) {
					vel *= dist * vel;
					ent.motionX += dX / vel * 0.0775;
					ent.motionY += dY / vel * 0.135;
					ent.motionZ += dZ / vel * 0.0775;
				}
			}

			if (this.world.isRemote) {
				for(int k = 0; k <= 4; k++) {
					float f1 = (float) this.posX - 0.5F + this.rand.nextFloat();
					float f2 = (float) (this.posY + 0.5F + this.rand.nextFloat() * 1.5);
					float f3 = (float) this.posZ - 0.5F + this.rand.nextFloat();
					FMLClientHandler.instance().getClient().effectRenderer.addEffect(new ParticleNomal.Factory().createParticle(0, this.world, f1, f2, f3, 0, 0, 0));
				}
			}

			// 一定時間経つとダメージ
			if (this.tickTime > 12) {

				float power = this.range == 3 ? 0.5F : 0.825F;
				float dame = 1F + power * this.getWandLevel();
				List<EntityLivingBase> entityList = this.getEntityList(this.range, this.range * 0.5D, this.range);

				for (EntityLivingBase ent : entityList) {

					if (PlayerHelper.isPlayer(ent)) { continue; }
					this.attackDamage(ent, dame);
					ent.hurtResistantTime = 0;
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

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		for (int i = 0; i < 6; i++) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
			float x = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
			float y = (this.rand.nextFloat() + this.rand.nextFloat()) * 0.15F;
			float z = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
			Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, f1, f2, f3, x, y, z);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
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
			living.addPotionEffect(new PotionEffect(PotionInit.gravity, 300 * level, 2));
			this.setEntityDead();
		}
	}
}
