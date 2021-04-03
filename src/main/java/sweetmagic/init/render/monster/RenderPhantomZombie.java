package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;

@SideOnly(Side.CLIENT)
public class RenderPhantomZombie extends RenderZombie {

	public static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/phantom_zombie.png");

	public RenderPhantomZombie(RenderManager render) {
		super(render);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityZombie entity) {
		return TEX;
	}
}
