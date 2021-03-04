package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.util.ResourceLocation;

public class RenderElectricCube extends RenderSlime {

    private static final ResourceLocation TEX = new ResourceLocation("sweetmagic:textures/entity/electriccube.png");

	public RenderElectricCube(RenderManager rendermanagerIn) {
        super(rendermanagerIn);
	}

//	protected ResourceLocation getEntityTexture(EntitySlime entity) {
//		return TEX;
//	}
}
