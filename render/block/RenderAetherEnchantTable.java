package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import sweetmagic.init.tile.magic.TileAetherEnchantTable;
import sweetmagic.util.RenderUtils;

public class RenderAetherEnchantTable extends TileEntitySpecialRenderer<TileAetherEnchantTable> {

	private static final float size = 0.5F;

	@Override
	public void render(TileAetherEnchantTable te, double x, double y, double z, float parTick, int stege, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te);
        GlStateManager.popMatrix();
	}

	protected void renderItem(TileAetherEnchantTable te) {

        float rot = 0;

        // ブロックの向きでアイテムの向きも変える
        switch (te.getFace()) {
		case NORTH:
			rot = 180F;
			break;
		case SOUTH:
			rot = 0F;
			break;
		case EAST:
			rot = 90F;
			break;
		case WEST:
			rot = 270F;
			break;
        }

        GlStateManager.scale(size, size, size);
        GlStateManager.rotate(rot, 0F, 1F, 0F);
		RenderItem render = Minecraft.getMinecraft().getRenderItem();

		ItemStack stack = te.getWandItem(0);
		if (stack.isEmpty()) { return; }

        RenderUtils.renderItem(render, stack, 0F, 2.35F, 0F, 0, 1, 0, 0, false);
	}
}
