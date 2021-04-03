package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.model.ModelAoi;
import sweetmagic.init.entity.monster.EntityWindineVerre;

public class RenderWindineVerre extends RenderLiving<EntityWindineVerre> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/aoi.png");

	public RenderWindineVerre(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelAoi(0.0F), 0.5F);
	}

	public void doRender(EntityWindineVerre entity, double x, double y, double z, float entityYaw, float partialTicks) {
//		((ModelAoi) this.mainModel).holdingItem = !entity.getHeldItemMainhand().isEmpty();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
//        this.addLayer(new LayerWing(this));
	}

	protected ResourceLocation getEntityTexture(EntityWindineVerre entity) {
		return TEX;
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	protected void preRenderCallback(EntityWindineVerre entitylivingbaseIn, float partialTickTime) {
		float size = 0.75F;
		GlStateManager.scale(size, size, size);
	}
}