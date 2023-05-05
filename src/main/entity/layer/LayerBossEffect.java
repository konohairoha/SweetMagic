package sweetmagic.init.entity.layer;

import java.util.Random;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import sweetmagic.init.entity.monster.EntityFairySkyDwarfKronos;

public class LayerBossEffect implements LayerRenderer<EntityFairySkyDwarfKronos> {

	public void doRenderLayer(EntityFairySkyDwarfKronos entity, float limbSwing, float swingAmount, float parTick, float ageTick, float headYaw, float headPitch, float scale) {

		if (entity.deathTicks <= 0) { return; }

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		RenderHelper.disableStandardItemLighting();
		float f = ((float) entity.deathTicks + parTick) / 200.0F;
		float f1 = 0F;

		if (f > 0.8F) {
			f1 = (f - 0.8F) / 0.2F;
		}

		Random rand = new Random(432L);
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(7425);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.disableAlpha();
		GlStateManager.enableCull();
		GlStateManager.depthMask(false);
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, -1F, 0F);

		for (int i = 0; (float) i < (f + f * f) / 2.0F * 60.0F; ++i) {
			GlStateManager.rotate(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(rand.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(rand.nextFloat() * 360.0F + f * 90.0F, 0.0F, 0.0F, 1.0F);
			float f2 = rand.nextFloat() * 20.0F + 5.0F + f1 * 10.0F;
			float f3 = rand.nextFloat() * 2.0F + 1.0F + f1 * 2.0F;
			buf.begin(6, DefaultVertexFormats.POSITION_COLOR);
			buf.pos(0.0D, 0.0D, 0.0D).color(255, 255, 255, (int) (255.0F * (1.0F - f1))).endVertex();
			buf.pos(-0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(255, 0, 255, 0).endVertex();
			buf.pos(0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(255, 0, 255, 0).endVertex();
			buf.pos(0.0D, (double) f2, (double) (1.0F * f3)).color(255, 0, 255, 0).endVertex();
			buf.pos(-0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(255, 0, 255, 0).endVertex();
			tes.draw();
		}

		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(7424);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		RenderHelper.enableStandardItemLighting();
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}
