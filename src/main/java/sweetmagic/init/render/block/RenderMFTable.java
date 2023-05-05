package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sweetmagic.init.tile.magic.TileMFMMTable;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.magic.TileMFTableAdvanced;
import sweetmagic.util.RenderUtils;

public class RenderMFTable extends TileEntitySpecialRenderer<TileMFTable> {

	private static final float size = 0.5F;

	@Override
	public void render(TileMFTable te, double x, double y, double z, float parTick, int stage, float alpha) {

		if (te instanceof TileMFTableAdvanced || te instanceof TileMFMMTable) {
			this.renderAdvanced(te, x, y, z, parTick);
		}

		else {

			ItemStack stack = te.getWandItem(0);
			if (stack.isEmpty()) { return; }

	        GlStateManager.pushMatrix();
	        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
	        this.renderItem(te, x, y + 0.5, z, parTick, stack);
	        GlStateManager.popMatrix();
		}
	}

	protected void renderItem(TileMFTable te, double x, double y, double z, float parTick, ItemStack stack) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
		GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) * 0.1F) * 0.15F + 0.2F, 0);
		GlStateManager.scale(this.size, this.size, this.size);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		RenderUtils.renderItem(render, stack, 0F, 2.55F, 0F, 0F, 1F, 0F, 0F);
	}

	public void renderAdvanced (TileMFTable te, double x, double y, double z, float parTick) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;
		float rotY = (te.getTileWorld().getTotalWorldTime() + parTick) / 90F;
		int count = te.getInvSize();
		float pi = 180F / (float) Math.PI;
		RenderItem render = Minecraft.getMinecraft().getRenderItem();

		for (int i = 0; i < count; i++) {

			ItemStack stack = te.getWandItem(i);
			if (stack.isEmpty()) { continue; }

			GlStateManager.pushMatrix();
	        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
			GlStateManager.rotate(rotY * pi + (i * (360 / count)), 0F, 1F, 0F);
	        GlStateManager.scale(0.6F, 0.6F, 0.6F);
			GlStateManager.translate(0.75F, 0F, 0F);
			RenderUtils.renderItem(render, stack, 0F, 2.2F, 0F, 0F, 0F, 0F, 0F);
			GlStateManager.popMatrix();
		}

		ItemStack stack = te.getInputItem();
		if (stack.isEmpty()) { return; }

		rot = worldTime % 360;
		RenderItem renderCrystal = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) * 0.1F) * 0.15F + 0.2F, 0);
		GlStateManager.scale(0.4F, 0.4F, 0.4F);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		RenderUtils.renderItem(renderCrystal, stack, 0F, 3F, 0F, 0F, 1F, 0F, 0F);
        GlStateManager.popMatrix();
	}
}
