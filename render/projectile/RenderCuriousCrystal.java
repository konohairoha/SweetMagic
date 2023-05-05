package sweetmagic.init.render.projectile;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.projectile.EntityCuriousCrystal;

public class RenderCuriousCrystal extends Render<EntityCuriousCrystal> {

    private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/curious_crystal.png");
    private final ModelBase modelEnderCrystalNoBase = new ModelEnderCrystal(0.0F, false);

	public RenderCuriousCrystal(RenderManager render) {
		super(render);
		this.shadowSize = 0.5F;
	}

	@Override
	public void doRender(EntityCuriousCrystal entity, double x, double y, double z, float entityYaw, float parTick) {
		float f = entity.ticksExisted + parTick;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		this.bindTexture(TEX);
		float f1 = MathHelper.sin(f * 0.2F) / 2F + 0.5F;
		f1 = f1 * f1 + f1;

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

			this.modelEnderCrystalNoBase.render(entity, 0.0F, f * 3.0F, f1 * 0.2F, 0.0F, 0.0F, 0.0625F);

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, parTick);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCuriousCrystal entity) {
		return TEX;
	}

	@Override
	public boolean shouldRender(EntityCuriousCrystal entity, ICamera camera, double camX, double camY, double camZ) {
		return super.shouldRender(entity, camera, camX, camY, camZ);
	}
}
