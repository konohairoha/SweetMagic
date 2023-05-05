package sweetmagic.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;

public class ParticleOrb extends ParticleBase {

	private static final ResourceLocation TEX =  new ResourceLocation(SweetMagicCore.MODID, "textures/particle/particle_orb.png");

	public ParticleOrb(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		this.motionX = xSpeed;
		this.motionY = ySpeed;
		this.motionZ = zSpeed;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleAlpha = 1F;
		this.particleScale = 0.1F;
		this.particleGravity = 0F;
		this.particleMaxAge = (int) (3D / (Math.random() * 4D + 0.2D)) + 64;
	}

	public static Particle create(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... array) {
		return new Factory().createParticle(0, world, x, y, z, xSpeed, ySpeed, zSpeed, array);
	}

	public void renderParticle(BufferBuilder buf, Entity entity, float parTick, float rotX, float rotZ, float rotYZ, float rotXY, float rotXZ) {

		int i = (int) ((this.particleAge + parTick));

		if (i < 32) {
			this.textureManager.bindTexture(TEX);
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

	public void onUpdate() {

		super.onUpdate();

		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9D;
		this.motionY *= 0.9D;
		this.motionZ *= 0.9D;
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {

		@Override
		public Particle createParticle(int id, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... par1) {

			ParticleOrb particle = new ParticleOrb(world, x, y, z, xSpeed, ySpeed, zSpeed);

			if (par1 != null && par1.length > 0) {

				if (par1.length == 1) {
					particle.setColor(par1[0]);
				}

				else if (par1.length == 3) {
					particle.setColor(par1);
				}
			}
			return particle;
		}
	}
}
