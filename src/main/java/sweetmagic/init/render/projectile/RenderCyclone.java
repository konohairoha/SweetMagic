package sweetmagic.init.render.projectile;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCyclone <T extends Entity> extends Render<T> {

    public RenderCyclone(RenderManager render) {
        super(render);
    }

	public void doRender(T var1, double var2, double var4, double var6, float var8, float var9) {

	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return null;
	}
}