package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.layer.LayerAngelWings;
import sweetmagic.init.entity.model.ModelRobe;
import sweetmagic.init.entity.monster.EntityElshariaCurious;

public class RenderElshariaCurious extends RenderLiving<EntityElshariaCurious> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/elsharia_curious.png");

	public RenderElshariaCurious(RenderManager render) {
		super(render, new ModelRobe(0), 0.5F);
        this.addLayer(new LayerAngelWings(this));
	}

	public ModelRobe getMainModel() {
		return (ModelRobe) super.getMainModel();
	}

	protected ResourceLocation getEntityTexture(EntityElshariaCurious entity) {
		return TEX;
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0F, 0.1875F, 0F);
	}

	protected void preRenderCallback(EntityElshariaCurious entity, float parTick) {
		float size = 1.125F;
		GlStateManager.scale(size, size, size);
	}
}