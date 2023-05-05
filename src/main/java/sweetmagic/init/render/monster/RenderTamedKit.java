package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.model.ModelRobe;

public class RenderTamedKit extends RenderLiving<EntityOcelot> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/repair_kitt.png");

	public RenderTamedKit(RenderManager render) {
        super(render, new ModelRobe(10), 0.5F);
	}

	protected ResourceLocation getEntityTexture(EntityOcelot entity) {
		return TEX;
	}

	protected void preRenderCallback(EntityOcelot entity, float parTick) {
		super.preRenderCallback(entity, parTick);
		GlStateManager.scale(0.7F, 0.7F, 0.7F);
	}
}
