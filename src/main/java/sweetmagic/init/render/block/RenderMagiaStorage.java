package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sweetmagic.init.tile.chest.TileMagiaStorage;
import sweetmagic.util.RenderUtils;

public class RenderMagiaStorage extends TileEntitySpecialRenderer<TileMagiaStorage> {

	private static final float size = 1.5F;

	@Override
	public void render(TileMagiaStorage te, double x, double y, double z, float parTick, int stage, float alpha) {

		ItemStack stack = new ItemStack(te.getBlock(te.getPos()));
		if (stack.isEmpty() || ( !te.findPlayer && te.renderTick > 32 )) { return; }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y - 3.5F, (float) z + 0.5F);

        this.renderItem(te, x, y, z, parTick, stack);

        GlStateManager.popMatrix();
	}

	protected void renderItem(TileMagiaStorage te, double x, double y, double z, float parTick, ItemStack stack) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = (worldTime + te.tickTime) % 360;

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
		GlStateManager.translate(0F, MathHelper.sin((worldTime + te.tickTime) * 0.1F) * 0.1F + 0.25F, 0F);
		GlStateManager.scale(this.size, this.size, this.size);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		RenderUtils.renderItem(render, stack, 0F, 2.55F, 0F, 0F, 1F, 0F, 0F);
	}
}
