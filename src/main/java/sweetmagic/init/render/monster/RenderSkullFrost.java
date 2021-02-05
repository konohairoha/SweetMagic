package sweetmagic.init.render.monster;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSkullFrost extends RenderLiving<EntitySkeleton> {

	public static final ResourceLocation TEXTURES = new ResourceLocation("sweetmagic:textures/entity/skullflost.png");

	public RenderSkullFrost(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelSkeleton(), 0.3F);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this) {
            protected void initArmor() {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        });
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySkeleton entity) {
		return TEXTURES;
	}
}
