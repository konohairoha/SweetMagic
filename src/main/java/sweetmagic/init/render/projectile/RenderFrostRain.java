package sweetmagic.init.render.projectile;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFrostRain extends RenderLightMagic {

	public RenderFrostRain(RenderManager renderManager) {
		super(renderManager);
	}
}