package sweetmagic.init.entity.projectile;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.client.particle.ParticleNomal;

public class EntityNomal extends EntityBaseMagicShot {

	public EntityNomal(World world) {
		super(world);
		this.setSize(0.5F, 0.5F);
	}

	public EntityNomal(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityNomal(World world, EntityLivingBase thrower) {
		this(world, thrower.posX, thrower.posY + (double) thrower.getEyeHeight() - 0.10000000149011612D, thrower.posZ);
		this.thrower = thrower;
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {
		Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, this.posX, this.posY, this.posZ, 0, 0, 0);
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
	}
}
