package sweetmagic.init.entity.projectile;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.client.particle.ParticleNomal;

public class EntityRockBlast extends EntityBaseMagicShot {

	public EntityRockBlast(World world) {
		super(world);
		this.setSize(0.75F, 0.75F);
	}

	public EntityRockBlast(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityRockBlast(World world, EntityLivingBase thrower, ItemStack stack) {
		super(world, thrower, stack);
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {
		this.setEntityDead();
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		for(int k = 0; k <= 4; k++) {
			float f1 = (float) this.posX - 0.5F + this.rand.nextFloat();
			float f2 = (float) (this.posY + 0.5F + this.rand.nextFloat() * 1.5);
			float f3 = (float) this.posZ - 0.5F + this.rand.nextFloat();
			Particle particle = new ParticleNomal.Factory().createParticle(0, this.world, f1, f2, f3, 0, 0, 0, 48);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {
		living.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 50, 1));
		living.hurtResistantTime = 0;
	}
}
