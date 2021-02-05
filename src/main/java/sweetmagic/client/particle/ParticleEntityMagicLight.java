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

@SideOnly(Side.CLIENT)
public class ParticleEntityMagicLight extends Particle {

	public static final String M_TEX = new String("sweetmagic:textures/particle/particle_mlight.png");
	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F)
			.addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB)
			.addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B)
			.addElement(DefaultVertexFormats.PADDING_1B);
	private TextureManager textureManager;

	public double moveX, moveY, moveZ;
	public boolean initP;
	public int tIdx;

	public ParticleEntityMagicLight(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
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

		this.textureManager = Minecraft.getMinecraft().getTextureManager();
		this.initP = false;
		this.tIdx = 0;
	}

	public void setColor(float red, float green, float blue) {
		this.particleRed = red;
		this.particleGreen = green;
		this.particleBlue = blue;
	}

	@Override
	public void move(double x, double y, double z) {
		this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		this.resetPositionToBB();
	}

	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX,
			float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		int i = (int) ((this.particleAge + partialTicks));

		if (i < 32) {
			this.textureManager.bindTexture(new ResourceLocation(M_TEX));
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
		int k = i >> 16 & 255;
		j = j + (int) (f * 15.0F * 16.0F);

		if (j > 240) {
			j = 240;
		}

		return j | k << 16;
	}

	@Override
	public void onUpdate() {
		if (initP) {
			this.moveX = (world.rand.nextDouble() - 0.5) / 3;
			this.moveY = (world.rand.nextDouble() - 0.5) / 3;
			this.moveZ = (world.rand.nextDouble() - 0.5) / 3;
			initP = false;
		}
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}

		//		this.move(this.motionX, this.motionY, this.motionZ);
		this.posX = this.moveX + this.posX;
		this.posY = this.moveY + this.posY;
		this.posZ = this.moveZ + this.posZ;
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {
		@Override
		public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
				double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
			return new ParticleEntityMagicLight(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		}
	}

	@Override //基本的に自作パーティクルは3を渡すっぽい
	public int getFXLayer() {
		return 3;
	}

	//	@Override		//実際にパーティクルを置いてみるときの参考
	//	@SideOnly(Side.CLIENT)
	//	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	//	{
	//		double d0 = (double) pos.getX() + 0.1D + rand.nextDouble() * 0.8D;
	//		double d1 = (double) pos.getY() + 1D + rand.nextDouble() * 0.3D;
	//		double d2 = (double) pos.getZ() + 0.1D + rand.nextDouble() * 0.8D;
	//
	//		if (worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && !worldIn.getBlockState(pos.up()).isOpaqueCube())
	//		{
	//			if (rand.nextInt(5) == 0)
	//			{
	//				Particle newEffect = new ParticleHotSpring.Factory().createParticle(0, worldIn, d0, d1, d2, 0, 0D, 0);
	//				FMLClientHandler.instance().getClient().effectRenderer.addEffect(newEffect);
	//			}
	//		}
	//	}
}
