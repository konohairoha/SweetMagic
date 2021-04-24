package sweetmagic.init.render.monster;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.monster.EntityArchSpider;

public class RenderArchSpider extends RenderLiving<EntityArchSpider> {

	private static final ResourceLocation TEXTURES = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/archspider.png");

	public RenderArchSpider(RenderManager render) {
		super(render, new ModelSpider(), 0.3F);
	}

	@Override
	protected void preRenderCallback(EntityArchSpider entity, float parTick) {
		float size = entity.isUnique() ? 1.65F : 1.33F;
		GlStateManager.scale(size, size, size);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityArchSpider entity) {
		return TEXTURES;
	}
}
