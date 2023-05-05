package sweetmagic.init.render.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import sweetmagic.api.enumblock.EnumCook;
import sweetmagic.init.block.blocks.BlockPot;
import sweetmagic.init.tile.cook.TilePot;
import sweetmagic.util.RenderUtils;

public class RenderPot extends TileEntitySpecialRenderer<TilePot> {

	@Override
	public void render(TilePot te, double x, double y, double z, float parTick, int stage, float alpha) {

		IBlockState state = te.getWorld().getBlockState(te.getPos());
		EnumCook cook = te.getCook(te.getWorld().getBlockState(te.getPos()));
		if (cook == null || cook.isOFF() || (cook.isON() && state.getBlock() instanceof BlockPot)) { return; }

		GlStateManager.pushMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(x + 0.5F, y + 0.3F, z + 0.5F);
		GlStateManager.rotate(RenderUtils.getFacingAngle(state) + 180F, 0F, 1F, 0F);
		this.renderItem(te, cook, x, y - 0.5, z, parTick);
		GlStateManager.popMatrix();
	}

	protected void renderItem(TilePot te, EnumCook cook, double x, double y, double z, float parTick) {

		List<ItemStack> stackList = new ArrayList();

		// 料理が終了状態なら
		if (cook == EnumCook.FIN) {
			stackList.addAll(te.outPutList);
		}

		// 料理中状態なら
		else {
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

			RenderUtils.renderItem(itemRenderer, s, 0, 0, pos, 0, 1, 0, 0F);
			count++;
        }
	}
}
