package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import sweetmagic.init.tile.magic.TileSMSpaner;

public class RenderSMSpawner extends TileEntitySpecialRenderer<TileSMSpaner> {

	@Override
	public void render(TileSMSpaner te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		this.renderMob(te, x, y, z, partialTicks);
		GlStateManager.popMatrix();
	}

	public void renderMob(TileSMSpaner te, double posX, double posY, double posZ, float parTick) {

		Entity entity = te.getEntity();

		if (entity != null) {
			float f = 0.53125F;
			float f1 = Math.max(entity.width, entity.height);

			if ((double) f1 > 1.0D) {
				f /= f1;
			}

			Long worldTime = te.getWorld().getTotalWorldTime();
	        float rot = worldTime % 360;
	        GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) / 10.0F) * 0.15F + 0.2F, 0);
	        GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(f, f, f);
			entity.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
			Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, parTick, false);
		}
	}
}
