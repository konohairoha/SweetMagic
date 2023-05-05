package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;

@SideOnly(Side.CLIENT)
public class RenderCreeperCal extends RenderCreeper {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/creepercal.png");

	public RenderCreeperCal(RenderManager render) {
		super(render);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCreeper entity) {
		return TEX;
	}
}
