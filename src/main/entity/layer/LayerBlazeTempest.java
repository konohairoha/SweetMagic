package sweetmagic.init.entity.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.render.monster.RenderBlazeTempest;

public class LayerBlazeTempest implements LayerRenderer<EntityBlazeTempest> {

    private static final ResourceLocation TEX_0 = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final RenderBlazeTempest renderer;
    private final ModelBlaze model = new ModelBlaze();

	public LayerBlazeTempest(RenderBlazeTempest renderer) {
		this.renderer = renderer;
	}

	public void doRenderLayer(EntityBlazeTempest entity, float swing, float amount, float tick, float age, float yaw,	float pitch, float scale) {

		if (entity.isTempest()) {
			boolean flag = entity.isInvisible();
			GlStateManager.depthMask(!flag);
			this.renderer.bindTexture(TEX_0);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			float f = (float) (entity.ticksExisted + tick) * 2.25F;
			GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
			GlStateManager.matrixMode(5888);
			GlStateManager.enableBlend();
			GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
			GlStateManager.disableLighting();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			this.model.setModelAttributes(this.renderer.getMainModel());
			Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
			this.model.render(entity, swing, amount, age, yaw, pitch, scale);
			Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			GlStateManager.matrixMode(5888);
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.depthMask(flag);
		}
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}
