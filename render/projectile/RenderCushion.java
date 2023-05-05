package sweetmagic.init.render.projectile;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.model.ModelCushion;
import sweetmagic.init.entity.projectile.EntityCushion;

public class RenderCushion extends Render<EntityCushion> {

	private static final ResourceLocation TEX_O = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/cushion_o.png");
	private static final ResourceLocation TEX_S = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/cushion_s.png");
	private static final ResourceLocation TEX_Y = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/cushion_y.png");
	private static final ResourceLocation TEX_B = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/cushion_b.png");
	private static final ModelCushion MODEL = new ModelCushion(false);

	public RenderCushion(RenderManager render) {
		super(render);
		this.shadowSize = 0.4F;
		this.shadowOpaque = 0.4F;
	}

	@Override
	public void doRender(EntityCushion entity, double x, double y, double z, float yaw, float parTick) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x - 0.1F, (float) y, (float) z + 0.1F);
		GlStateManager.scale(-1F, -1F, 1F);
		this.bindTexture(this.getEntityTexture(entity));
		GlStateManager.rotate(- entity.rotationYaw * 0.75F, 0F, 1F, 0F);
		MODEL.render(0.055F, null);
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, yaw, parTick);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCushion entity) {
		switch (entity.getData()) {
		case 0: return TEX_O;
		case 1: return TEX_S;
		case 2: return TEX_Y;
		case 3: return TEX_B;
		}
		return TEX_O;
	}
}
