package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.model.ModelAoi;
import sweetmagic.init.entity.monster.EntityIfritVerre;

public class RenderIfritVerre extends RenderLiving<EntityIfritVerre> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/ifrit.png");

	public RenderIfritVerre(RenderManager render) {
		super(render, new ModelAoi(0.0F), 0.5F);
	}

	protected ResourceLocation getEntityTexture(EntityIfritVerre entity) {
		return TEX;
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	protected void preRenderCallback(EntityIfritVerre entity, float parTick) {
		float size = 0.875F;
		GlStateManager.scale(size, size, size);
	}
}