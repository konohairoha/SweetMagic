package sweetmagic.init.render.monster;

import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.entity.layer.LayerManHandItem;
import sweetmagic.init.entity.monster.EntityEnderShadow;

@SideOnly(Side.CLIENT)
public class RenderEnderShadow extends RenderLiving<EntityEnderShadow> {

    private static final ResourceLocation TEX = new ResourceLocation("sweetmagic:textures/entity/ender_shadow.png");

	public RenderEnderShadow(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelEnderman(0.0F), 0.5F);
        this.addLayer(new LayerManHandItem(this));
//        this.addLayer(new LayerEndermanEyes(new Render));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityEnderShadow entity) {
		return TEX;
	}
}
