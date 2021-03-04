package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import sweetmagic.init.entity.model.ModelAoi;
import sweetmagic.init.entity.monster.EntityIfritVerre;

public class RenderIfrit extends RenderLiving<EntityIfritVerre> {

	private static final ResourceLocation WITCH_TEXTURES = new ResourceLocation("sweetmagic:textures/entity/ifrit.png");

	public RenderIfrit(RenderManager render) {
		super(render, new ModelAoi(0.0F), 0.5F);
		//        this.addLayer(new LayerHeldItemWitch(this));
	}

	public void doRender(EntityIfritVerre entity, double x, double y, double z, float entityYaw, float parTick) {
//		((ModelAoi) this.mainModel).holdingItem = !entity.getHeldItemMainhand().isEmpty();
		super.doRender(entity, x, y, z, entityYaw, parTick);
	}

	protected ResourceLocation getEntityTexture(EntityIfritVerre entity) {
		return WITCH_TEXTURES;
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	protected void preRenderCallback(EntityIfritVerre entity, float parTick) {
		float size = 1F;
		GlStateManager.scale(size, size, size);
	}
}