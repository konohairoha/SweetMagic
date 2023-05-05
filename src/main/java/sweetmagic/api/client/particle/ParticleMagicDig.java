package sweetmagic.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;

@SideOnly(Side.CLIENT)
public class ParticleMagicDig extends ParticleBase {

	private static final ResourceLocation M_TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/particle/particle_dig.png");

	public ParticleMagicDig(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		this.motionX = (world.rand.nextDouble() - 0.5) / 2;
		this.motionY = (world.rand.nextDouble() - 0.5) / 2;
		this.motionZ = (world.rand.nextDouble() - 0.5) / 2;
		this.posX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.posY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.posZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleAlpha = 1.0F;
		this.particleScale = 0.2F;
		this.particleGravity = 0.0F;
		this.particleMaxAge = (int) (3.0D / (Math.random() * 4.0D + 0.2D)) + 64;
	}

	public static Particle create(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... array) {
		return new Factory().createParticle(0, world, x, y, z, xSpeed, ySpeed, zSpeed, array);
	}

	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotX,
			float rotZ, float rotYZ, float rotXY, float rotXZ) {
		int i = (int) ((this.particleAge + partialTicks));

		if (i < 32) {
			this.textureManager.bindTexture(M_TEX);
			float fu = 0.0F;
			float fU = 1.0F;
			float fv = i / 32.0F;
			float fV = fv + 0.03125F;
			float scale = this.particleScale;
			float fx = (float) (this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
			float fy = (float) (this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
			float fz = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			buffer.begin(7, VERTEX_FORMAT);
			buffer.pos(fx - rotX * scale - rotXY * scale, fy - rotZ * scale * 1.0F, fz - rotYZ * scale - rotXZ * scale)
					.tex(fU, fV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(fx - rotX * scale + rotXY * scale, fy + rotZ * scale * 1.0F, fz - rotYZ * scale + rotXZ * scale)
					.tex(fU, fv).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(fx + rotX * scale + rotXY * scale, fy + rotZ * scale * 1.0F, fz + rotYZ * scale + rotXZ * scale)
					.tex(fu, fv).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(fx + rotX * scale - rotXY * scale, fy - rotZ * scale * 1.0F, fz + rotYZ * scale - rotXZ * scale)
					.tex(fu, fV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			Tessellator.getInstance().draw();
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
		}
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

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {
		@Override
		public Particle createParticle(int particleID, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... array) {
			return new ParticleMagicDig(world, x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}
}
