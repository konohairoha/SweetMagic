package sweetmagic.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleIceCrystal extends Particle {

	public static final String ORB_TEX = new String("sweetmagic:particle/particle_icecrystal");
	private float flameScale;

	protected ParticleIceCrystal(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		this.motionX = xSpeed;
		this.motionY = ySpeed - 0.75F;
		this.motionZ = zSpeed;
		this.flameScale = this.particleScale = 4F;
		this.particleAlpha = 0.85F;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 8;

		this.setParticleTexture(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(ORB_TEX));
	}

	public static Particle create(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... array) {
		return new Factory().createParticle(0, world, x, y, z, xSpeed, ySpeed, zSpeed, array);
	}

	@Override
	public void move(double x, double y, double z) {
		this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		this.resetPositionToBB();
	}

	@Override
	public void renderParticle(BufferBuilder buf, Entity entity, float parTick, float rotX, float rotZ, float rotYZ, float rotXY, float rotXZ) {

		float f = (this.particleAge + parTick) / this.particleMaxAge;
		this.particleScale = this.flameScale * (1.0F - f * f * 0.5F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
			GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		super.renderParticle(buf, entity, parTick, rotX, rotZ, rotYZ, rotXY, rotXZ);
		GlStateManager.disableBlend();
	}

	@Override
	public int getBrightnessForRender(float p_189214_1_) {
		float f = (this.particleAge + p_189214_1_) / this.particleMaxAge;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		int i = super.getBrightnessForRender(p_189214_1_);
		int j = i & 255;
		int k = i >> 16 & 255;
		j = j + (int) (f * 15.0F * 16.0F);

		if (j > 240) {
			j = 240;
		}

		return j | k << 16;
	}

	@Override
	public void onUpdate() {

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}

		if (this.onGround) {
			this.setExpired();
		}

		this.move(this.motionX, this.motionY, this.motionZ);
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {

		@Override
		public Particle createParticle(int id, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... array) {
			return new ParticleIceCrystal(world, x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}

	@Override
	public int getFXLayer() {
		return 1;
	}
}
