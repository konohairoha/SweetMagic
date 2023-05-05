package sweetmagic.init.render.projectile;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.entity.projectile.EntityDragonBullet;

@SideOnly(Side.CLIENT)
public class RenderDragonBullet extends Render<EntityDragonBullet> {

    private static final ResourceLocation TEX = new ResourceLocation("textures/entity/enderdragon/dragon_fireball.png");

	public RenderDragonBullet(RenderManager manager) {
		super(manager);
	}

	public void doRender(EntityDragonBullet entity, double x, double y, double z, float entityYaw, float parTick) {

        GlStateManager.pushMatrix();
		this.bindEntityTexture(entity);
		GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buf = tes.getBuffer();
        GlStateManager.rotate(180F - this.renderManager.playerViewY, 0F, 1F, 0F);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1F, 0F, 0F);

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		buf.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		buf.pos(-0.5D, -0.25D, 0D).tex(0D, 1D).normal(0F, 1F, 0F).endVertex();
		buf.pos(0.5D, -0.25D, 0D).tex(1D, 1D).normal(0F, 1F, 0F).endVertex();
		buf.pos(0.5D, 0.75D, 0D).tex(1D, 0D).normal(0F, 1F, 0F).endVertex();
		buf.pos(-0.5D, 0.75D, 0D).tex(0D, 0D).normal(0F, 1F, 0F).endVertex();
		tes.draw();

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, parTick);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDragonBullet entity) {
		return TEX;
	}
}
