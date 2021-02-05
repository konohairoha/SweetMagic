package sweetmagic.init.render.monster;

import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;

@SideOnly(Side.CLIENT)
public class RenderWitchMadameVerre extends RenderLiving<EntityWitchMadameVerre> {

	private static final ResourceLocation WITCH_TEXTURES = new ResourceLocation("sweetmagic:textures/entity/witchmadameverre.png");

	public RenderWitchMadameVerre(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelWitch(0.0F), 0.5F);
		//        this.addLayer(new LayerHeldItemWitch(this));
	}

	public ModelWitch getMainModel() {
		return (ModelWitch) super.getMainModel();
	}

	public void doRender(EntityWitchMadameVerre entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		((ModelWitch) this.mainModel).holdingItem = !entity.getHeldItemMainhand().isEmpty();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	protected ResourceLocation getEntityTexture(EntityWitchMadameVerre entity) {
		return WITCH_TEXTURES;
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	protected void preRenderCallback(EntityWitchMadameVerre entitylivingbaseIn, float partialTickTime) {
		float size = 0.67F;
		GlStateManager.scale(size, size, size);
	}
}