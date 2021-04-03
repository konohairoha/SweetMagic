package sweetmagic.init.render.monster;

import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.layer.LayerBlazeTempest;
import sweetmagic.init.entity.monster.EntityBlazeTempest;

@SideOnly(Side.CLIENT)
public class RenderBlazeTempest extends RenderLiving<EntityBlazeTempest> {

	public static final ResourceLocation TEXTURES = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/blazetempest.png");

	public RenderBlazeTempest(RenderManager render) {
		super(render, new ModelBlaze(), 0.3F);
        this.addLayer(new LayerBlazeTempest(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBlazeTempest entity) {
		return TEXTURES;
	}

	protected void preRenderCallback(EntityMob entity, float tickTime) {
		GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
	}
}
