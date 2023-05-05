package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.layer.LayerVerreWand;
import sweetmagic.init.entity.model.ModelRobe;
import sweetmagic.init.entity.monster.EntityIfritVerre;

public class RenderIfritVerre extends RenderLiving<EntityIfritVerre> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/ifrit.png");

	public RenderIfritVerre(RenderManager render) {
		super(render, new ModelRobe(0), 0.5F);
        this.addLayer(new LayerVerreWand(this));
	}

	public ModelRobe getMainModel() {
		return (ModelRobe) super.getMainModel();
	}

	protected ResourceLocation getEntityTexture(EntityIfritVerre entity) {
		return TEX;
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	@Override
	protected void preRenderCallback(EntityIfritVerre entity, float parTick) {
		float size = 0.8F;
		GlStateManager.scale(size, size, size);
	}
}