package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.layer.LayerBossEffect;
import sweetmagic.init.entity.layer.LayerFairySkyDwarfKronos;
import sweetmagic.init.entity.model.ModelHora;
import sweetmagic.init.entity.monster.EntityFairySkyDwarfKronos;

public class RenderFairySkyDwarfKronos extends RenderLiving<EntityFairySkyDwarfKronos> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/fairyskydwarfkronos.png");

	public RenderFairySkyDwarfKronos(RenderManager render) {
		super(render, new ModelHora(), 0.3F);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this) {
            @Override
			protected void initArmor() {
                this.modelLeggings = new ModelHora(0.5F, true);
                this.modelArmor = new ModelHora(1.0F, true);
            }
        });
        this.addLayer(new LayerFairySkyDwarfKronos(this));
        this.addLayer(new LayerBossEffect());
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFairySkyDwarfKronos entity) {
		return TEX;
	}

	@Override
	public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
    }

	@Override
	protected void preRenderCallback(EntityFairySkyDwarfKronos entity, float parTick) {
		float size = 1.5F;
		GlStateManager.scale(size, size, size);
	}
}
