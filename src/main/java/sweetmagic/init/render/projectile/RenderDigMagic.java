package sweetmagic.init.render.projectile;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDigMagic extends RenderLightMagic {

	public RenderDigMagic(RenderManager renderManager) {
		super(renderManager);
	}
}
