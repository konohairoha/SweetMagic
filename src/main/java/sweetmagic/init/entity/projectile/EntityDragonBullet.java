package sweetmagic.init.entity.projectile;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import sweetmagic.client.particle.ParticleNomal;

public class EntityDragonBullet extends EntityLockBullet {

	public EntityDragonBullet(World world) {
		super(world);
		this.setSize(0.5F, 0.5F);
	}

	public EntityDragonBullet(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityDragonBullet(World world, EntityLivingBase thrower, EntityLivingBase target, ItemStack stack) {
		super(world, thrower, target, stack);
	}

	public double getSpeed () {
		return 1.5D;
	}

	// 自然消滅までの時間 30tick + this.plusTickAir(増やしたい場合は-10とか付ければおっけー)
	protected int plusTickAir() {
		return 600;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {
		for (int i = 0; i < 3; i++) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 8F);
			float f2 = (float) (this.posY - 0.75F + this.rand.nextFloat() * 0.5 + this.motionY * i / 8D);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 8D);
			float x = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
			float y = (this.rand.nextFloat() + this.rand.nextFloat()) * 0.15F;
			float z = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
			Particle effect = ParticleNomal.create(this.world, f1, f2, f3, x, y, z, 48);
			this.getParticle().addEffect(effect);
		}
	}
}
