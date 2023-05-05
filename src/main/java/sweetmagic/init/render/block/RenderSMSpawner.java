package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import sweetmagic.init.tile.magic.TileSMSpaner;

public class RenderSMSpawner extends TileEntitySpecialRenderer<TileSMSpaner> {

	@Override
	public void render(TileSMSpaner te, double x, double y, double z, float parTick, int stage, float alpha) {

		if (!te.findPlayer && te.tickTime >= 30) { return; }

		EntityLivingBase entity = te.getRenderEntity();
		if (entity == null) { return; }

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		this.renderMob(te, entity, x, y, z, parTick);
		GlStateManager.popMatrix();
	}

	public void renderMob(TileSMSpaner te, EntityLivingBase entity, double posX, double posY, double posZ, float parTick) {

		float f = 0.75F;
		float f1 = Math.max(entity.width, entity.height);

		if ((double) f1 > 1D) {
			f /= f1;
		}

		Long worldTime = te.getWorld().getTotalWorldTime();
        float rot = worldTime % 360 * 40;
        GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) * 0.1F) * 0.075F + 0.05F, 0);
        GlStateManager.rotate(rot, 0F, 1F, 0F);
        GlStateManager.rotate(-30, 1F, 0F, 0F);
		GlStateManager.scale(f, f, f);
		entity.setLocationAndAngles(posX, posY, posZ, 0F, 0F);
		Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0D, 0D, 0D, 0F, 0, false);
	}
}
