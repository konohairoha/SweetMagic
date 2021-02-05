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

public class ParticleNomal extends Particle {

	public static final String TEX = new String("sweetmagic:textures/particle/particle_normal.png");
	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F)
			.addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB)
			.addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B)
			.addElement(DefaultVertexFormats.PADDING_1B);
	private TextureManager textureManager;

	public double moveX, moveY, moveZ;

	public ParticleNomal(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		this.motionX = xSpeed;
		this.motionY = ySpeed;
		this.motionZ = zSpeed;
//		this.posX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
//		this.posY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
//		this.posZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleAlpha = 1F;
		this.particleScale = 0.2F;
		this.particleGravity = 0F;
		this.particleMaxAge = (int) (3D / (Math.random() * 4D + 0.2D)) + 64;

		this.textureManager = Minecraft.getMinecraft().getTextureManager();
	}

	protected void setColor(int color) {
		int[] colors = this.getColor(color);
		this.particleRed = colors[0] / 255.0F;
		this.particleGreen = colors[1] / 255.0F;
		this.particleBlue = colors[2] / 255.0F;
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

	/**
	 * Renders the particle
	 */
	public void renderParticle(BufferBuilder buf, Entity entity, float parTick, float rotX, float rotZ, float rotYZ, float rotXY, float rotXZ) {

		int i = (int) ((this.particleAge + parTick));

		if (i < 32) {
			this.textureManager.bindTexture(new ResourceLocation(TEX));
			float fu = 0.0F;
			float fU = 1.0F;
			float fv = i / 32.0F;
			float fV = fv + 0.03125F;
			float scale = this.particleScale;
			float fx = (float) (this.prevPosX + (this.posX - this.prevPosX) * parTick - interpPosX);
			float fy = (float) (this.prevPosY + (this.posY - this.prevPosY) * parTick - interpPosY);
			float fz = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * parTick - interpPosZ);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			buf.begin(7, VERTEX_FORMAT);
			buf.pos(fx - rotX * scale - rotXY * scale, fy - rotZ * scale * 1.0F, fz - rotYZ * scale - rotXZ * scale)
					.tex(fU, fV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			buf.pos(fx - rotX * scale + rotXY * scale, fy + rotZ * scale * 1.0F, fz - rotYZ * scale + rotXZ * scale)
					.tex(fU, fv).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			buf.pos(fx + rotX * scale + rotXY * scale, fy + rotZ * scale * 1.0F, fz + rotYZ * scale + rotXZ * scale)
					.tex(fu, fv).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
			buf.pos(fx + rotX * scale - rotXY * scale, fy - rotZ * scale * 1.0F, fz + rotYZ * scale - rotXZ * scale)
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

	public void onUpdate() {

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}

		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9D;
		this.motionY *= 0.9D;
		this.motionZ *= 0.9D;
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {

		@Override
		public Particle createParticle(int id, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... par1) {
			ParticleNomal particle = new ParticleNomal(world, x, y, z, xSpeed, ySpeed, zSpeed);
			if (par1 != null && par1.length > 0) {
				particle.setColor(par1[0]);
			}
			return particle;
		}
	}

	@Override //基本的に自作パーティクルは3を渡すっぽい
	public int getFXLayer() {
		return 3;
	}
}
