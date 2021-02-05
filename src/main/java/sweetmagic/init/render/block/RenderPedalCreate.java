package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sweetmagic.init.tile.magic.TilePedalCreate;
import sweetmagic.util.RenderUtils;

public class RenderPedalCreate extends TileEntitySpecialRenderer<TilePedalCreate> {

	private final float size = 0.5F;

	@Override
	public void render(TilePedalCreate te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableLighting();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y + 0.5, z, partialTicks);
        GlStateManager.popMatrix();
	}

	// アイテムのレンダー
	protected void renderItem(TilePedalCreate te, double x, double y, double z, float partialTicks) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;
		GlStateManager.translate(0, MathHelper.sin((worldTime + partialTicks) / 10F) * 0.15F + 0.2F, 0);
		GlStateManager.scale(this.size, this.size, this.size);
		GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);
		ItemStack hand = te.getHandItem();
		if (hand.isEmpty()) { return; }

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
        float posY = 2.55F + te.nowTick * 0.008F;
		RenderUtils.renderItem(render, hand, 0, posY, 0, 0, 1, 0, 0);

		rot *= 1F + (te.nowTick * 0.0075F);
		GlStateManager.rotate(rot, 0F, 0.25F, 0F);
		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.scale(0.7F, 0.7F, 0.7F);

        // 投入アイテムの個数取得
//        int maxAmount = te.getInputCount();
//		System.out.println("========AMO" + amount);
//		System.out.println("========MAX" + maxAmount);

		for (int i = 0; i < 8; i++) {

			ItemStack input = ItemStack.EMPTY;

			try {
				input = te.getInputItem(i);
			}

			catch (Throwable e) { }

			if (input.isEmpty()) { return; }

			float pos = i * (1F + te.nowTick);
	        GlStateManager.rotate(44, 0.0F, pos, 0F);
	        float posY2 = 5F + te.nowTick * 0.0175F;
	        float posX = 2F - te.nowTick * 0.0105F;
			RenderUtils.renderItem(itemRenderer, input, posX, posY2, 0, 0, 0, 0, 0F);

		}
	}
}
