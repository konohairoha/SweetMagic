package sweetmagic.init.render.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sweetmagic.init.tile.magic.TilePedalCreate;
import sweetmagic.util.RenderUtils;

public class RenderPedalCreate extends TileEntitySpecialRenderer<TilePedalCreate> {

	private static final float size = 0.45F;
	private static final float mainSize = 0.45F;

	@Override
	public void render(TilePedalCreate te, double x, double y, double z, float parTick, int stage, float alpha) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;
		float rotY = (worldTime + parTick) / 90F;
		List<ItemStack> stackList = new ArrayList<>();

		try {
			stackList = te.getList();
		}

		catch (Throwable e) {}

		if (stackList.isEmpty()) { return; }
		int count = stackList.size() - 1;
		float pi = 180F / (float) Math.PI;
		RenderItem render = Minecraft.getMinecraft().getRenderItem();
		int nowTick = te.nowTick;
		float posY = 2.45F + nowTick * 0.015F;

		for (int i = 0; i < count; i++) {

			ItemStack stack = stackList.get(i + 1);
			if (stack.isEmpty()) { continue; }

			GlStateManager.pushMatrix();
	        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
	        GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) / 10F) * 0.15F + 0.2F, 0);
			GlStateManager.rotate(rotY * pi + (i * (360 / count)) + nowTick * 6.75F, 0F, 1F, 0F);
			GlStateManager.scale(size, size, size);
			GlStateManager.translate(1F - (0.0055 * nowTick) , 0F, 0F);
			RenderUtils.renderItem(render, stack, 0F, posY, 0F, 0F, 0F, 0F, 0F);
			GlStateManager.popMatrix();
		}

		ItemStack stack = te.getHandItem();
		if (stack.isEmpty()) { return; }

		rot = worldTime % 360;
		RenderItem renderCrystal = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) * 0.1F) * 0.15F + 0.2F, 0);
		GlStateManager.scale(size, size, size);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		RenderUtils.renderItem(renderCrystal, stack, 0F, posY, 0F, 0F, 1F, 0F, 0F);
        GlStateManager.popMatrix();
	}
}
