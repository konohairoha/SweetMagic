package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.magic.TileMFTableAdvanced;
import sweetmagic.util.RenderUtils;

public class RenderMFTable extends TileEntitySpecialRenderer<TileMFTable> {

	private final float size = 0.5F;

	@Override
	public void render(TileMFTable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y + 0.5, z, partialTicks);
        GlStateManager.popMatrix();
	}

	protected void renderItem(TileMFTable te, double x, double y, double z, float partialTicks) {

		if (te instanceof TileMFTableAdvanced) {

			Long worldTime = te.getWorld().getTotalWorldTime();
			float rot = worldTime % 720;
			GlStateManager.rotate(rot, 0F, 48F, 0F);
			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
	        GlStateManager.scale(0.7F, 0.7F, 0.7F);
			float rotY = (te.getTileWorld().getTotalWorldTime() + partialTicks) / 90F;

			for (int i = 0; i < 4; i++) {

				ItemStack stack = te.getWandItem(i);
				if (stack.isEmpty()) { continue; }
				GlStateManager.rotate(rotY, 0F, (180F / (float) Math.PI) + (i * (360F / 4)), 0F);
				RenderUtils.renderItem(itemRenderer, stack, 1, 2F, 0, 0, 0, 0, 0F);
			}

			ItemStack stack = te.getInputItem();
			if (stack.isEmpty()) { return; }

			rot = worldTime % 360;
			RenderItem render = Minecraft.getMinecraft().getRenderItem();
			GlStateManager.translate(0, MathHelper.sin((worldTime + partialTicks) / 10F) * 0.15F + 0.2F, 0);
			GlStateManager.scale(0.6F, 0.6F, 0.6F);
			GlStateManager.rotate(rot, 0F, 1F, 0F);
			RenderUtils.renderItem(render, stack, 0, 3F, 0, 0, 1, 0, 0f);

		}

		else {

			ItemStack stack = te.getWandItem(0);
			if (stack.isEmpty()) { return; }

			Long worldTime = te.getWorld().getTotalWorldTime();
			float rot = worldTime % 720;

			RenderItem render = Minecraft.getMinecraft().getRenderItem();
			GlStateManager.translate(0, MathHelper.sin((worldTime + partialTicks) / 10F) * 0.15F + 0.2F, 0);
			GlStateManager.scale(this.size, this.size, this.size);
			GlStateManager.rotate(rot, 0F, 1F, 0F);
			RenderUtils.renderItem(render, stack, 0, 2.55F, 0, 0, 1, 0, 0f);
		}
	}
}
