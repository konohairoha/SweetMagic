package sweetmagic.init.render.projectile;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;

@SideOnly(Side.CLIENT)
public class RenderLightMagic<T extends Entity> extends Render<T> {

	private static final ResourceLocation tex = new ResourceLocation(SweetMagicCore.MODID, "textures/particle/notex.png");

	public RenderLightMagic(RenderManager renderManager) {
		super(renderManager);
	}

	public void renderBullet(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		this.bindEntityTexture(entity);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		int i = 0;
		float f = 0.0F;
		float f1 = 0.5F;
		float f2 = (float) (0 + i * 10) / 32.0F;
		float f3 = (float) (5 + i * 10) / 32.0F;
		float f4 = 0.0F;
		float f5 = 0.15625F;
		float f6 = (float) (5 + i * 10) / 32.0F;
		float f7 = (float) (10 + i * 10) / 32.0F;
		float f8 = 0.15625F;
		GlStateManager.enableRescaleNormal();
		GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(f8, f8, f8);
		GlStateManager.translate(-4.0F, 0.0F, 0.0F);
		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}
		GlStateManager.glNormal3f(f8, 0.0F, 0.0F);
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(-7.0D, -2.0D, -2.0D).tex(f4, f6).endVertex();
		vertexbuffer.pos(-7.0D, -2.0D, 2.0D).tex(f5, f6).endVertex();
		vertexbuffer.pos(-7.0D, 2.0D, 2.0D).tex(f5, f7).endVertex();
		vertexbuffer.pos(-7.0D, 2.0D, -2.0D).tex(f4, f7).endVertex();
		tessellator.draw();
		GlStateManager.glNormal3f(-f8, 0.0F, 0.0F);
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(-7.0D, 2.0D, -2.0D).tex(f4, f6).endVertex();
		vertexbuffer.pos(-7.0D, 2.0D, 2.0D).tex(f5, f6).endVertex();
		vertexbuffer.pos(-7.0D, -2.0D, 2.0D).tex(f5, f7).endVertex();
		vertexbuffer.pos(-7.0D, -2.0D, -2.0D).tex(f4, f7).endVertex();
		tessellator.draw();
		for (int j = 0; j < 4; ++j) {
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.glNormal3f(0.0F, 0.0F, f8);
			vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
			vertexbuffer.pos(-8.0D, -2.0D, 0.0D).tex(f, f2).endVertex();
			vertexbuffer.pos(8.0D, -2.0D, 0.0D).tex(f1, f2).endVertex();
			vertexbuffer.pos(8.0D, 2.0D, 0.0D).tex(f1, f3).endVertex();
			vertexbuffer.pos(-8.0D, 2.0D, 0.0D).tex(f, f3).endVertex();
			tessellator.draw();
		}
		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}
		GlStateManager.disableRescaleNormal();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	public void doRender(T var1, double var2, double var4, double var6, float var8, float var9) {
		this.renderBullet((T) var1, var2, var4, var6, var8, var9);
		this.renderAnimetion((EntityBaseMagicShot) var1, var2, var4, var6, var8, var9);
	}

	public void renderAnimetion (EntityBaseMagicShot entity, double x, double y, double z, float entityYaw, float partialTicks) {
	}

	protected ResourceLocation getEntityTexture(T entity) {
		return tex;
	}
}
