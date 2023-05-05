package sweetmagic.init.render.projectile;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderShinigFlare extends RenderLightMagic {

	public RenderShinigFlare(RenderManager render) {
		super(render);
	}
}
