package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.magic.TileToolRepair;
import sweetmagic.util.RenderUtils;

public class RenderToolRepair extends TileEntitySpecialRenderer<TileToolRepair> {

	private static final float size = 0.25F;
	private static final ItemStack stack = new ItemStack(ItemInit.aether_crystal);

	@Override
	public void render(TileToolRepair te, double x, double y, double z, float parTick, int stage, float alpha) {
		if (!te.isMfEmpty()) {
			this.rebderEffect(te, x, y, z, parTick);
		}
	}

	public void rebderEffect (TileToolRepair te, double x, double y, double z, float parTick) {
        this.renderItem(te, 0, x + 0.25, y + 0.125, z, parTick);
        this.renderItem(te, 1, x, y + 0.125, z + 0.25, parTick);
        this.renderItem(te, 2, x - 0.25, y + 0.125, z, parTick);
        this.renderItem(te, 3, x, y + 0.125, z - 0.25, parTick);
        this.renderAether(te, x, y + 0.025, z, parTick);
	}

	protected void renderItem(TileToolRepair te, int slot, double x, double y, double z, float parTick) {

		ItemStack stack = te.getWandItem(slot);
		if (stack.isEmpty()) { return; }

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) * 0.1F) * 0.1F + 0.2F, 0);
		GlStateManager.scale(this.size, this.size, this.size);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		RenderUtils.renderItem(render, stack, 0, 2.55F, 0, 0, 1, 0, 0);
        GlStateManager.popMatrix();
	}

	protected void renderAether(TileToolRepair te, double x, double y, double z, float parTick) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 360;

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) / 8F) * 0.05F + 0.1F, 0);
		GlStateManager.scale(0.2F, 0.2F, 0.2F);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		RenderUtils.renderItem(render, this.stack, 0, 2.55F, 0, 0, 1, 0, 0);
        GlStateManager.popMatrix();
	}
}
