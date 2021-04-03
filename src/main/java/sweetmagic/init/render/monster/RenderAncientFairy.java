package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.model.ModelPixelVex;
import sweetmagic.init.entity.monster.EntityAncientFairy;

public class RenderAncientFairy extends RenderLiving<EntityAncientFairy> {

    private static final ResourceLocation VEX_TEXTURE = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/ancientfairy.png");
    private int modelVersion;

	public RenderAncientFairy(RenderManager render) {
		super(render, new ModelPixelVex(), 0.3F);
		this.modelVersion = ((ModelPixelVex) this.mainModel).getModelVersion();
	}

	protected ResourceLocation getEntityTexture(EntityAncientFairy entity) {
		return VEX_TEXTURE;
	}

	protected void preRenderCallback(EntityAncientFairy entity, float parTick) {
		GlStateManager.scale(1.5F, 1.5F, 1.5F);
	}
}
