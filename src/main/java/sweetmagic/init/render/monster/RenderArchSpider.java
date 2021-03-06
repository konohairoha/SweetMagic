package sweetmagic.init.render.monster;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import sweetmagic.init.entity.monster.EntityArchSpider;

public class RenderArchSpider extends RenderLiving<EntityArchSpider> {

	public static final ResourceLocation TEXTURES = new ResourceLocation("sweetmagic:textures/entity/archspider.png");

	public RenderArchSpider(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelSpider(), 0.3F);
	}

	@Override
	protected void preRenderCallback(EntityArchSpider entity, float partialTickTime) {
		float size = entity.isUnique() ? 1.65F : 1.33F;
		GlStateManager.scale(size, size, size);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityArchSpider entity) {
		return TEXTURES;
	}
}
