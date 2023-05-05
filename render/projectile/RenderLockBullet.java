package sweetmagic.init.render.projectile;

import net.minecraft.client.model.ModelShulkerBullet;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.projectile.EntityLockBullet;

@SideOnly(Side.CLIENT)
public class RenderLockBullet extends Render<EntityLockBullet>
{
    private static final ResourceLocation SHULKER_SPARK_TEXTURE = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/spark.png");
    private final ModelShulkerBullet model = new ModelShulkerBullet();

	public RenderLockBullet(RenderManager manager) {
		super(manager);
	}

	private float rotLerp(float prevRot, float rotYaw, float parTick) {
		float f;

		for (f = rotYaw - prevRot; f < -180.0F; f += 360.0F) { ; }

		while (f >= 180.0F) {
			f -= 360.0F;
		}

		return prevRot + parTick * f;
	}

	public void doRender(EntityLockBullet entity, double x, double y, double z, float entityYaw, float parTick) {

		GlStateManager.pushMatrix();
		float f = this.rotLerp(entity.prevRotationYaw, entity.rotationYaw, parTick);
		float f1 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * parTick;
		float f2 = (float) entity.ticksExisted + parTick;
		GlStateManager.translate((float) x, (float) y + 0.15F, (float) z);
		GlStateManager.rotate(MathHelper.sin(f2 * 0.1F) * 180.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(MathHelper.cos(f2 * 0.1F) * 180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(MathHelper.sin(f2 * 0.15F) * 360.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		this.bindEntityTexture(entity);
		this.model.render(entity, 0.0F, 0.0F, 0.0F, f, f1, 0.03125F);
		GlStateManager.enableBlend();
		GlStateManager.color(0.25F, 0.25F, 0.25F, 0.5F);
		GlStateManager.scale(1.5F, 1.5F, 1.5F);
		this.model.render(entity, 0.0F, 0.0F, 0.0F, f, f1, 0.03125F);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, parTick);
	}

	protected ResourceLocation getEntityTexture(EntityLockBullet entity) {
		return SHULKER_SPARK_TEXTURE;
	}
}
