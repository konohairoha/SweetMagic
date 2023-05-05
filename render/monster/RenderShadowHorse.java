package sweetmagic.init.render.monster;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityHorse;
import sweetmagic.init.entity.monster.EntityShadowHorse;

public class RenderShadowHorse extends RenderHorse {

	public RenderShadowHorse(RenderManager render) {
		super(render);
	}

	public void doRender(EntityShadowHorse entity, double x, double y, double z, float entityYaw, float parTick) {
		super.doRender(entity, x, y, z, entityYaw, parTick);

		GlStateManager.color(1F, 1F, 1F, 0.5F);
	}

	@Override
	protected void preRenderCallback(EntityHorse entity, float parTick) {
		super.preRenderCallback(entity, parTick);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1, 1, 1, 0.875F);
	}
}
