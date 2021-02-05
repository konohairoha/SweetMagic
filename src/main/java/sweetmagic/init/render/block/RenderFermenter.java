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

	float size = 0.5F;

	@Override
	public void render(TileFermenter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y - 0.5, z, partialTicks);
        GlStateManager.popMatrix();
	}

	protected void renderItem(TileFermenter te, double x, double y, double z, float partialTicks) {

		Long worldTime = te.getWorld().getTotalWorldTime();
        float rot = worldTime % 720;

        ItemStack stack = ItemStack.EMPTY;

        if (!te.isWorking && !te.isFinish) { return; }

        if (te.isWorking && !te.handItem.isEmpty()) {
        	stack = te.handItem;
        }

        else if (te.isFinish && !te.getOutItem().isEmpty()) {
        	stack = te.getOutItem();
        }

        if (stack.isEmpty()) { return; }

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.translate(0, MathHelper.sin((worldTime + partialTicks) / 16F) * 0.075F + 0.1F, 0);
        GlStateManager.scale(this.size, this.size, this.size);
        GlStateManager.rotate(rot, 0.0F, 1F, 0.0F);
        RenderUtils.renderItem(render, stack, 0, 0.55F, 0, 0, 1, 0, 0f);
	}
}
