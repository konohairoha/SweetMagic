package sweetmagic.init.render.monster;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityIronGolem;

public class RenderShadowGolem extends RenderIronGolem {

	public RenderShadowGolem(RenderManager render) {
		super(render);
	}

	@Override
	protected void preRenderCallback(EntityIronGolem entity, float parTick) {
		super.preRenderCallback(entity, parTick);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1, 1, 1, 0.875F);
	}
}
