package sweetmagic.init.render.monster;

import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;

@SideOnly(Side.CLIENT)
public class RenderWitchMadameVerre extends RenderLiving<EntityWitchMadameVerre> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/witchmadameverre.png");

	public RenderWitchMadameVerre(RenderManager render) {
		super(render, new ModelWitch(0.0F), 0.5F);
	}

	public ModelWitch getMainModel() {
		return (ModelWitch) super.getMainModel();
	}

	protected ResourceLocation getEntityTexture(EntityWitchMadameVerre entity) {
		return TEX;
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	protected void preRenderCallback(EntityWitchMadameVerre entitylivingbaseIn, float parTick) {
		float size = 0.67F;
		GlStateManager.scale(size, size, size);
	}
}
