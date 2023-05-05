package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.layer.LayerKittEquipment;
import sweetmagic.init.entity.model.ModelRobe;
import sweetmagic.init.entity.monster.EntityRepairKitt;

public class RenderRepairKitt extends RenderLiving<EntityRepairKitt> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/repair_kitt.png");
	private static final ResourceLocation FA = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/repair_kitt_fa.png");

	public RenderRepairKitt(RenderManager render) {
		super(render, new ModelRobe(10), 0.5F);
        this.addLayer(new LayerKittEquipment(this));
	}

	public ModelRobe getMainModel() {
		return (ModelRobe) super.getMainModel();
	}

	protected ResourceLocation getEntityTexture(EntityRepairKitt entity) {
		return entity.getArmorType() == 2 ? FA : TEX;
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	protected void preRenderCallback(EntityRepairKitt entity, float parTick) {
		float size = 0.7F;
		GlStateManager.scale(size, size, size);
	}
}