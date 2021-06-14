package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.PotionInit;
import sweetmagic.util.EventUtil;
import sweetmagic.util.SMDamage;

public class EntityBlackHole extends EntityGravityMagic {

	private boolean isFix = false;

	public EntityBlackHole(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
		this.isHit = true;
		this.plusTick = -200;
	}

	public EntityBlackHole(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityBlackHole(World world, EntityLivingBase thrower, ItemStack stack) {
		super(world, thrower, stack);
		this.isHitDead = true;
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		float moX = (float) (this.isFix ? ( this.getRandFloat() - this.getRandFloat() ) * 1.25F: this.motionX);
		float moY = (float) (this.isFix ? ( this.getRandFloat() - this.getRandFloat() ) * 1.25F : this.motionY);
		float moZ = (float) (this.isFix ? ( this.getRandFloat() - this.getRandFloat() ) * 1.25F : this.motionZ);

		float x = -moX / 12F;
		float y = -moY / 12F;
		float z = -moZ / 12F;

		for (int i = 0; i < 8; i++) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + moX * i * 0.25F);
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5F + moY * i * 0.25F);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + moZ * i * 0.25F);
			Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, f1, f2, f3, x, y, z, 48);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

	public float getRandFloat () {
		return this.rand.nextFloat() * 1.5F;
	}

	public void onUpdate() {

		super.onUpdate();

		//
		if (this.ticksInAir >= 5 && !this.isFix) {
			this.ticksInAir = 0;
			this.tickTime++;
			this.isFix = true;
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
		}

		// 一定時間が経ったら
		if (this.isFix) {

			this.ticksInAir = 0;
			this.tickTime++;
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;

			DamageSource src = SMDamage.flostDamage;
			double range = Math.min(16D, 2.5D + this.getWandLevel() * 0.5D);
			List<EntityLivingBase> entityList = this.getEntityList(range, range, range);

			for (EntityLivingBase entity : entityList) {

				if ( !(entity instanceof IMob)) { continue; }

				if (this.ticksExisted % 20 == 0) {
					entity.attackEntityFrom(src, 1F);
					entity.hurtResistantTime = 0;
					this.checkShadow(entity);

					if (entity.isPotionActive(PotionInit.gravity)) {
						entity.removeActivePotionEffect(PotionInit.gravity);
					}

					// 敵を動かなくさせる
					if (entity instanceof EntityLiving && entity.isNonBoss()) {
						EventUtil.tameAIDonmov((EntityLiving) entity, 1);
					}
				}

				// 吸い込み
				if (entity.canEntityBeSeen(this)) {
					this.gravitySuction(entity);
				}
			}

			if (this.tickTime > 120) {

				// パーティクルをスポーンするメソッド
				if (this.world.isRemote) {

					for (int i = 0; i < 64; i++) {
						float f1 = (float) (this.posX + 0.5F);
						float f2 = (float) (this.posY + this.rand.nextFloat() * 0.5F);
						float f3 = (float) (this.posZ + 0.5F);
						float x = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.15F;
						float y = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.15F;
						float z = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.15F;

						Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, f1, f2, f3, x, y, z);
						FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
					}
				}

				this.setEntityDead();
			}
		}
	}

	public void gravitySuction (EntityLivingBase entity) {

		double dX = this.posX - entity.posX;
		double dY = this.posY - entity.posY;
		double dZ = this.posZ - entity.posZ;
		double dist = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
		double vel = 1D - dist / 16;

		if (vel > 0D) {
			vel *= dist * vel;
			entity.motionX += Math.max(-1, Math.min(1, dX / vel * 0.075D));
			entity.motionY += Math.max(-1, Math.min(1, dY / vel * 0.075D));
			entity.motionZ += Math.max(-1, Math.min(1, dZ / vel * 0.075D));
		}
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		if (this.tickTime == 0) {
			this.setPosition(this.posX, this.posY + 1.25D, this.posZ);
		}

		this.ticksInAir = 0;
		this.tickTime++;
		this.isFix = true;
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {
		living.hurtResistantTime = 0;
	}

	// 死亡時
	public void setDead() {

		super.setDead();

		int level = this.getWandLevel();
		double range = Math.min(16D, 2.5D + this.getWandLevel() * 0.35D);
		List<EntityLivingBase> entityList = this.getEntityList(range, range, range);

		for (EntityLivingBase entity : entityList) {

			if (!(entity instanceof IMob)) { continue; }

			this.attackDamage(entity, (float) (range + this.getDamage()) );
			entity.hurtResistantTime = 0;
			this.checkShadow(entity);
			entity.addPotionEffect(new PotionEffect(PotionInit.gravity, 40 * (level + 1), 2));
		}

		this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
	}
}
