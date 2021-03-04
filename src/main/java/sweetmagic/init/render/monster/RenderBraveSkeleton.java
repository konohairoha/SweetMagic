package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBraveSkeleton extends RenderSkeleton {

	public static final ResourceLocation TEX = new ResourceLocation("sweetmagic:textures/entity/brave_skeleton.png");

	public RenderBraveSkeleton(RenderManager render) {
		super(render);
	}

	@Override
	protected ResourceLocation getEntityTexture(AbstractSkeleton entity) {
		return TEX;
	}

	protected void preRenderCallback(AbstractSkeleton entitylivingbaseIn, float partialTickTime) {
		GlStateManager.scale(1.25F, 1.25F, 1.25F);
	}
}
