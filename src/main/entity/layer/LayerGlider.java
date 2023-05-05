package sweetmagic.init.entity.layer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.PotionInit;

public class LayerGlider extends LayerEffectBase<EntityLivingBase> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/angel_wing.png");

	public LayerGlider(RenderLivingBase<?> render) {
		super(render);
	}

	@Override
	public ResourceLocation getTexture(EntityLivingBase entity, float parTick) {
		return null;
	}

	@Override
	public boolean shouldRender(EntityLivingBase entity, float parTick) {
		return entity.isPotionActive(PotionInit.glider) && entity.isEntityAlive() && !entity.isPotionActive(PotionInit.breakblock);
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float swingAmount, float parTick, float ageTick, float headYaw, float headPitch, float scale) {
		if (this.shouldRender(entity, parTick)) {
		     this.renderEffect(scale, entity, parTick);
		}
	}

	private void renderEffect(float scale, EntityLivingBase entity, float parTick) {

		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

		GlStateManager.rotate(180, 1F, 0F, 0F);

		if (entity.isSneaking()) {
			GlStateManager.rotate(30F, 1F, 0F, 0F);
			GlStateManager.translate(0F, -1.2F, 0.125F);
		}

		else {
			GlStateManager.translate(0, -1F, 0);
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(TEX);
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();

		GlStateManager.pushMatrix();

		GlStateManager.translate(0.1F, 0.4F, -0.15F);
		GlStateManager.rotate(20 + 20 * MathHelper.sin((entity.ticksExisted + parTick) * 0.3F), 0, 1, 0);

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buf.pos(0, 2, 0).tex(0, 0).endVertex();
		buf.pos(2, 2, 0).tex(1, 0).endVertex();
		buf.pos(2, 0, 0).tex(1, 1).endVertex();
		buf.pos(0, 0, 0).tex(0, 1).endVertex();
		tes.draw();

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buf.pos(0, 2, 0).tex(0, 0).endVertex();
		buf.pos(0, 0, 0).tex(0, 1).endVertex();
		buf.pos(2, 0, 0).tex(1, 1).endVertex();
		buf.pos(2, 2, 0).tex(1, 0).endVertex();
		tes.draw();

		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();

		GlStateManager.translate(-0.1F, 0.4F, -0.15F);
		GlStateManager.rotate(-200 - 20 * MathHelper.sin((entity.ticksExisted + parTick) * 0.3F), 0, 1F, 0);

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buf.pos(0, 2, 0).tex(0, 0).endVertex();
		buf.pos(2, 2, 0).tex(1, 0).endVertex();
		buf.pos(2, 0, 0).tex(1, 1).endVertex();
		buf.pos(0, 0, 0).tex(0, 1).endVertex();
		tes.draw();

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buf.pos(0, 2, 0).tex(0, 0).endVertex();
		buf.pos(0, 0, 0).tex(0, 1).endVertex();
		buf.pos(2, 0, 0).tex(1, 1).endVertex();
		buf.pos(2, 2, 0).tex(1, 0).endVertex();
		tes.draw();

		GlStateManager.popMatrix();

		GlStateManager.enableLighting();
		GlStateManager.disableBlend();

		GlStateManager.popMatrix();
	}
}

