package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.plant.TileAlstroemeria;
import sweetmagic.util.RenderUtils;

public class RenderTileAlstroemeria extends TileEntitySpecialRenderer<TileAlstroemeria> {

	private static final float size = 0.5F;
	private static final ItemStack hand = new ItemStack(ItemInit.veil_darkness);

	@Override
	public void render(TileAlstroemeria te, double x, double y, double z, float parTick, int destroyStage, float alpha) {

		if (!te.isSummon) { return; }
        GlStateManager.pushMatrix();
        GlStateManager.enableLighting();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y + 0.5, z, parTick);
        GlStateManager.popMatrix();
	}

	// アイテムのレンダー
	protected void renderItem(TileAlstroemeria te, double x, double y, double z, float parTick) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;
		GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) * 0.1F) * 0.15F + 0.2F, 0);
		GlStateManager.scale(this.size, this.size, this.size);
		GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
        float posY = 2.55F + te.nowTick * 0.01225F;
		RenderUtils.renderItem(render, this.hand, 0, posY, 0, 0, 1, 0, 0);
	}
}
