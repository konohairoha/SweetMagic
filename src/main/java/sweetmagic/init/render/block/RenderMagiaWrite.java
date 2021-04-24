package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.magic.TileMagiaWrite;
import sweetmagic.util.RenderUtils;

public class RenderMagiaWrite extends TileEntitySpecialRenderer<TileMagiaWrite> {

	private static final float size = 0.35F;
	private static final ItemStack stack = new ItemStack(ItemInit.aether_crystal_shard);

	@Override
	public void render(TileMagiaWrite te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y + 0.5, z, partialTicks);
        GlStateManager.popMatrix();

        this.renderEffect(te, x, y, z, partialTicks);
	}

	// スロットのアイテム描画
	protected void renderItem(TileMagiaWrite te, double x, double y, double z, float partialTicks) {

		ItemStack stack = te.getToolItem();
		if (stack.isEmpty()) { return; }

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
		GlStateManager.translate(0, MathHelper.sin((worldTime + partialTicks) / 10F) * 0.1F + 0.2F, 0);
		GlStateManager.scale(this.size, this.size, this.size);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		RenderUtils.renderItem(render, stack, 0, 1.6F, 0, 0, 1, 0, 0f);
	}

	public void renderEffect (TileMagiaWrite te, double x, double y, double z, float parTick) {

		if (te.isSmelt && te.tickTime > 0) {

			float posY = 0.5F + 0.001375F * te.tickTime;
	        GlStateManager.pushMatrix();
	        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);

	        this.renderAether(te, 0.3375F, posY, 0.3375F, parTick);
	        this.renderAether(te, -0.3375F, posY, 0.3375F, parTick);
	        this.renderAether(te, 0.3375F, posY, -0.3375F, parTick);
	        this.renderAether(te, -0.3375F, posY, -0.3375F, parTick);

	        GlStateManager.popMatrix();
		}
	}

	// 柱の周りのかけらの描画
	public void renderAether (TileMagiaWrite te, float x, float y, float z, float parTick) {

		float rotY = (te.getTime() + parTick + te.tickTime) / 10F;
		float rotX = -0.125F;
		float rotZ = 0;
		float scale = 0.25F;
		int count = 4;
		float pi = 180F / (float) Math.PI;
		RenderItem render = Minecraft.getMinecraft().getRenderItem();

		for (int i = 0; i < count; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.rotate(rotZ * pi, 0F, 0F, 1F);
			GlStateManager.rotate(rotY * pi + (i * (360 / count)), 0F, 1F, 0F);
			GlStateManager.rotate(rotX * pi, 1F, 0F, 0F);
			GlStateManager.scale(scale, -scale, scale);
			GlStateManager.translate(0.75F, 0F, 0F);
			RenderUtils.renderItem(render, this.stack, 0, 1.6F, 0, 0, 0, 0, 0);
			GlStateManager.popMatrix();
		}
	}
}
