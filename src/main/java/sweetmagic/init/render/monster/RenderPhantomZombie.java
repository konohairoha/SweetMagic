package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPhantomZombie extends RenderZombie {

	public static final ResourceLocation TEX = new ResourceLocation("sweetmagic:textures/entity/phantom_zombie.png");

	public RenderPhantomZombie(RenderManager render) {
		super(render);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityZombie entity) {
		return TEX;
	}
}
