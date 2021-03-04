package sweetmagic.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleLay extends Particle {

	public static final String LTEX = new String("sweetmagic:textures/particle/particle_lay.png");
	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F)
			.addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB)
			.addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B)
			.addElement(DefaultVertexFormats.PADDING_1B);
	private TextureManager textureManager;

	public double speedX, speedY, speedZ;

	public ParticleLay(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed) {
		super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);

		this.speedX = xSpeed;
		this.speedY = ySpeed;
		this.speedZ = zSpeed;
		this.motionX = xSpeed;
		this.motionY = ySpeed;
		this.motionZ = zSpeed;
//		this.posX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
//		this.posY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
//		this.posZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleAlpha = 0.75F;
		this.particleScale = 0.2F;
		this.particleGravity = 0.0F;
		this.particleMaxAge = (int) (3.0D / (Math.random() * 4.0D + 0.2D)) + 32;
		this.textureManager = Minecraft.getMinecraft().getTextureManager();
	}

	protected void setColor(int color[]) {
//		int[] colors = this.getColor(color);
		this.particleRed = color[0] / 255.0F;
		this.particleGreen = color[1] / 255.0F;
		this.particleBlue = color[2] / 255.0F;
	}

	public int[] getColor(int color) {
		int r = (color >> 16) & 255;
		int g = (color >> 8) & 255;
		int b = color & 255;
		return new int[] { r, g, b };
	}

	@Override
	public void move(double x, double y, double z) {
		this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		this.resetPositionToBB();
	}

	// Renders the particle
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX,
			float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

		int i = (int) (((this.particleAge + partialTicks) * 16F / this.particleMaxAge) % 16);

		if (i <= 16) {
			this.textureManager.bindTexture(new ResourceLocation(LTEX));
			float fu = 0.0F;
			float fU = 1.0F;
			float fv = i / 16.0F;
			float fV = fv + 0.05125F;
			float scale = this.particleScale;
			float fx = (float) (this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
			float fy = (float) (this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
			float fz = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
			GlStateManager.disableLighting();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			buffer.begin(7, VERTEX_FORMAT);
			buffer.pos(fx - rotationX * scale - rotationXY * scale, fy - rotationZ * scale * 1.0F,
					fz - rotationYZ * scale - rotationXZ * scale)
					.tex(fU, fV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(fx - rotationX * scale + rotationXY * scale, fy + rotationZ * scale * 1.0F,
					fz - rotationYZ * scale + rotationXZ * scale)
					.tex(fU, fv).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(fx + rotationX * scale + rotationXY * scale, fy + rotationZ * scale * 1.0F,
					fz + rotationYZ * scale + rotationXZ * scale)
					.tex(fu, fv).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(fx + rotationX * scale - rotationXY * scale, fy - rotationZ * scale * 1.0F,
					fz + rotationYZ * scale - rotationXZ * scale)
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
		int k = i >> 32 & 255;
		j = j + (int) (f * 15.0F * 32.0F);
		if (j > 240) { j = 240;  }

		return j | k << 32;
	}

	@Override
	public void onUpdate() {

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

//		this.motionY += 0.1;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}

//		this.move(this.motionX, this.motionY, this.motionZ);
//		this.motionX *= 0.9D;
//		this.motionY *= 0.9D;
//		this.motionZ *= 0.9D;
//		this.move(this.motionX, this.motionY, this.motionZ);
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {

		@Override
		public Particle createParticle(int id, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... par1) {
			ParticleLay particle = new ParticleLay(world, x, y, z, xSpeed, ySpeed, zSpeed);
			if (par1 != null && par1.length > 0) {
				particle.setColor(par1);
			}
			return particle;
		}
	}

	@Override //基本的に自作パーティクルは3を渡すっぽい
	public int getFXLayer() {
		return 3;
	}
}
