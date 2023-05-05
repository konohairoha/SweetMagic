package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import sweetmagic.init.tile.magic.TileAetherCover;
import sweetmagic.util.RenderUtils;

public class RenderAetherCover extends TileEntitySpecialRenderer<TileAetherCover> {

	private static final float size = 2F;

	@Override
	public void render(TileAetherCover te, double x, double y, double z, float parTick, int stege, float alpha) {

		if (!te.findPlayer && te.renderTime >= 30) { return; }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        this.renderItem(te);
        GlStateManager.popMatrix();
	}

	protected void renderItem(TileAetherCover te) {

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
		ItemStack stack = te.isSlotEmpty() ? te.getDefaultStack() : te.getChestItem();
        RenderUtils.renderItem(render, stack, 0F, 0F, 0F, 0, 0, 0, 0, false);
	}
}
