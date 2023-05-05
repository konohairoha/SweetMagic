package sweetmagic.init.render.projectile;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.projectile.EntityMagicCycle;

public class RenderMagicCycle extends Render<EntityMagicCycle> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/block/mahoujin3.png");

	public RenderMagicCycle(RenderManager render) {
		super(render);
		this.shadowSize = 0F;
		this.shadowOpaque = 0F;
	}

	@Override
	public void doRender(EntityMagicCycle entity, double x, double y, double z, float yaw, float parTick) {

		if (entity.getData() == 8) { return; }

		GlStateManager.pushMatrix();

		GlStateManager.translate((float) x, (float) y - 0.25D, (float) z );
		float alpha = Math.min(1F, ((float) entity.ticksExisted / 30F));
		float rgb = 1F / 255F;
		GlStateManager.color(entity.getRed() * rgb, entity.getGreen() * rgb, entity.getBlue() * rgb, alpha);

		float rot = entity.ticksExisted * 2F;
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		bindTexture(this.getEntityTexture(entity));

		float scale = entity.getScale();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-scale, 0F, -scale).tex(0D, 0D).endVertex();
		buffer.pos(scale, 0F, -scale).tex(1D, 0D).endVertex();
		buffer.pos(scale, 0F, scale).tex(1D, 1D).endVertex();
		buffer.pos(-scale, 0F, scale).tex(0D, 1D).endVertex();

		tessellator.draw();

		GlStateManager.rotate(180F, 0F, 0F, 1F);
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(scale, 0F, scale).tex(0D, 0D).endVertex();
		buffer.pos(-scale, 0F, scale).tex(1D, 0D).endVertex();
		buffer.pos(-scale, 0F, -scale).tex(1D, 1D).endVertex();
		buffer.pos(scale, 0F, -scale).tex(0D, 1D).endVertex();

		tessellator.draw();

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();

		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMagicCycle entity) {
		return TEX;
	}
}
