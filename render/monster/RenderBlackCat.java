package sweetmagic.init.render.monster;

import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;

public class RenderBlackCat extends RenderLiving<EntityOcelot> {

    private static final ResourceLocation BLACK_OCELOT_TEXTURES = new ResourceLocation("textures/entity/cat/black.png");

	public RenderBlackCat(RenderManager render) {
        super(render, new ModelOcelot(), 0.4F);
	}

	protected ResourceLocation getEntityTexture(EntityOcelot entity) {
		return BLACK_OCELOT_TEXTURES;
	}

	protected void preRenderCallback(EntityOcelot entity, float parTick) {
		super.preRenderCallback(entity, parTick);
		GlStateManager.scale(0.8F, 0.8F, 0.8F);
	}
}
