package sweetmagic.init.render.monster;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

public class RenderShadowWolf extends RenderWolf {

	private static final ResourceLocation TEX = new ResourceLocation("textures/entity/wolf/wolf_tame.png");

	public RenderShadowWolf(RenderManager render) {
		super(render);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityWolf entity) {
		return TEX;
	}

	@Override
	protected void preRenderCallback(EntityWolf entity, float parTick) {
		super.preRenderCallback(entity, parTick);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1, 1, 1, 0.875F);
	}
}
