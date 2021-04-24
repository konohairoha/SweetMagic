package sweetmagic.init.render.projectile;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCardNormal extends RenderLightMagic {

	public RenderCardNormal(RenderManager render) {
		super(render);
	}
}
