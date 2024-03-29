package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sweetmagic.init.tile.cook.TileFermenter;
import sweetmagic.util.RenderUtils;

public class RenderFermenter extends TileEntitySpecialRenderer<TileFermenter> {

	private static final float size = 0.5F;

	@Override
	public void render(TileFermenter te, double x, double y, double z, float parTick, int stage, float alpha) {

		// 料理中以外かつ完了以外なら終了
        if (!te.isWorking && !te.isFinish) { return; }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y - 0.5, z, parTick);
        GlStateManager.popMatrix();
	}

	protected void renderItem(TileFermenter te, double x, double y, double z, float parTick) {

        ItemStack stack = ItemStack.EMPTY;

        // 料理中
        if (te.isWorking && !te.handItem.isEmpty()) {
        	stack = te.handItem;
        }

        // 完成時
        else if (te.isFinish && !te.getOutItem().isEmpty()) {
        	stack = te.getOutItem();
        }

        // 空なら終了
        if (stack.isEmpty()) { return; }

		Long worldTime = te.getWorld().getTotalWorldTime();
        float rot = worldTime % 720;
		RenderItem render = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) / 16F) * 0.075F + 0.1F, 0);
        GlStateManager.scale(this.size, this.size, this.size);
        GlStateManager.rotate(rot, 0.0F, 1F, 0.0F);
        RenderUtils.renderItem(render, stack, 0, 0.55F, 0, 0, 1, 0, 0F);
	}
}
