package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;

public class RenderElectricCube extends RenderSlime {

    private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/electriccube.png");

	public RenderElectricCube(RenderManager render) {
        super(render);
	}

	protected ResourceLocation getEntityTexture(EntitySlime entity) {
		return TEX;
	}
}
