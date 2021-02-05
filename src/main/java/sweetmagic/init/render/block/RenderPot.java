package sweetmagic.init.render.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import sweetmagic.init.BlockInit;
import sweetmagic.init.tile.cook.TilePot;
import sweetmagic.util.RenderUtils;

public class RenderPot extends TileEntitySpecialRenderer<TilePot> {

	@Override
	public void render(TilePot te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		IBlockState state =te.getWorld().getBlockState(te.getPos());
		Block block = state.getBlock();

		if (block == BlockInit.pot_re || block == BlockInit.frypan_on || block == BlockInit.frypan_re) {
	        GlStateManager.pushMatrix();
	        GlStateManager.color(1f, 1f, 1f, 1f);
	        GlStateManager.translate(x + 0.5F, y + 0.3F, z + 0.5F);
	        GlStateManager.rotate(RenderUtils.getFacingAngle(state) + 180f, 0f, 1f, 0f);
	        this.renderItem(te, block, x, y - 0.5, z, partialTicks);
	        GlStateManager.popMatrix();
		}
	}

	protected void renderItem(TilePot te, Block block, double x, double y, double z, float partialTicks) {

		List<ItemStack> stackList = new ArrayList();

		if (block == BlockInit.pot_re || block == BlockInit.frypan_re) {
			stackList.addAll(te.outPutList);
		} else {
			stackList.add(te.handItem);
			stackList.addAll(te.inPutList);
		}

		if (stackList.isEmpty()) { return; }

		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(0, 0.0F, 1.0F, 0.0F);

        int count = 0;
        float pos = 0;

        for (ItemStack s : stackList) {

        	pos = count != 0 ? pos *= -1 : pos;
        	pos = count % 2 == 1 ? pos + 0.175F : pos;

			RenderUtils.renderItem(itemRenderer, s, 0, 0, pos, 0, 1, 0, 0f);
			count++;
        }
	}
}
