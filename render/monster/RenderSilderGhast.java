package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderGhast;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;

public class RenderSilderGhast extends RenderGhast {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/shilder_ghast.png");
	private static final ResourceLocation SHOT = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/shilder_ghast_shooting.png");

	public RenderSilderGhast(RenderManager render) {
		super(render);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGhast entity) {
		return entity.isAttacking() ? SHOT : TEX;
	}

	protected void preRenderCallback(EntityGhast entity, float parTick) {
		float size = 1.5F;
		GlStateManager.scale(size, size, size);
	}
}
