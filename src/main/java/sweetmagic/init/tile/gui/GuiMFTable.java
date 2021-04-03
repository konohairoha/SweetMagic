package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.init.tile.container.ContainerMFTable;
import sweetmagic.init.tile.magic.TileMFTable;

public class GuiMFTable extends GuiContainer {

	private static final ResourceLocation TEX_TIER1 = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_magictable_tier1.png");
	private static final ResourceLocation TEX_TIER2 = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_magictable_tier2.png");
	private static final ResourceLocation TEX_TIER3 = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_magictable_tier3.png");
	private final TileMFTable tile;

	public GuiMFTable(InventoryPlayer invPlayer, TileMFTable tile) {
		super(new ContainerMFTable(invPlayer, tile));
		this.xSize = 234;
		this.ySize = 243;
		this.tile = tile;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {

		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(this.getTex());

		// 座標の取得
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		// ゲージ描画数
		int progress;

		//こっちではゲージ量を計算する
		if (!this.tile.isMfEmpty()) {

			// ゲージの値を設定
			progress = this.tile.getMfProgressScaled(50);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 172, y + 99 - progress, 240, 50 - progress, 3, progress);
		}

		if (!this.tile.getWandItem(0).isEmpty()) {
			ItemStack stack = this.tile.getWandItem(0);
			IMFTool wand = (IMFTool) stack.getItem();
			progress = wand.getMfProgressScaled(stack, 14);
			this.drawTexturedModalRect(x + 39, y + 66, 240, 50, progress, 3);
		}

		if (this.tile.getInvSize() < 4) { return; }

		for (int i = 1; i < this.tile.getInvSize(); i++) {
			ItemStack stack = this.tile.getWandItem(i);

			if (stack.isEmpty()) { continue; }
			IMFTool wand = (IMFTool) stack.getItem();
			progress = wand.getMfProgressScaled(stack, 14);

			switch (i) {
			case 1:
				this.drawTexturedModalRect(x + 39, y + 102, 240, 50, progress, 3);
				break;
			case 2:
				this.drawTexturedModalRect(x + 16, y + 40, 240, 50, progress, 3);
				break;
			case 3:
				this.drawTexturedModalRect(x + 63, y + 40, 240, 50, progress, 3);
				break;
			case 4:
				this.drawTexturedModalRect(x + 3, y + 77, 240, 50, progress, 3);
				break;
			case 5:
				this.drawTexturedModalRect(x + 75, y + 77, 240, 50, progress, 3);
				break;
			}
		}
	}

	public ResourceLocation getTex () {

		ResourceLocation tex = null;

		switch (this.tile.getInvSize()) {
		case 1:
			tex = TEX_TIER1;
			break;
		case 4:
			tex = TEX_TIER2;
			break;
		case 6:
			tex = TEX_TIER3;
			break;
		}

		return tex;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		//ツールチップでMF量を表示する
		int mf = this.tile.getMF();
		int max = this.tile.getMaxMF();
		//基準点

		//描画位置を計算
		int tip_x = (this.width - this.xSize) / 2;
		int tip_y = (this.height - this.ySize) / 2;

		//ゲージの位置を計算
		tip_x += 172;
		tip_y += 49;

		if (tip_x <= mouseX && mouseX <= tip_x + 5
				&& tip_y <= mouseY && mouseY <= tip_y + 50) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			this.drawHoveringText(String.format("%,d", mf) + "mf / " + String.format("%,d", max) + "mf", xAxis, yAxis);
		}


		if (!this.tile.getWandItem(0).isEmpty()) {

			ItemStack stack = this.tile.getWandItem(0);
			IMFTool wand = (IMFTool) stack.getItem();
			int wandMF = wand.getMF(stack);
			int wabdMax = wand.getMaxMF(stack);

			//描画位置を計算
			int tip_x2 = (this.width - this.xSize) / 2;
			int tip_y2 = (this.height - this.ySize) / 2;

			//ゲージの位置を計算
			tip_x2 += 38;
			tip_y2 += 67;

			if (tip_x2 <= mouseX && mouseX <= tip_x2 + 15
					&& tip_y2 <= mouseY && mouseY <= tip_y2 + 2) {

				//GUIの左上からの位置
				int xAxis = (mouseX - (this.width - this.xSize) / 2);
				int yAxis = (mouseY - (this.height - this.ySize) / 2);
				this.drawHoveringText(String.format("%,d", wandMF) + "mf / " + String.format("%,d", wabdMax) + "mf", xAxis, yAxis);
			}
		}

		if (this.tile.getInvSize() < 4) { return; }

		for (int i = 1; i < this.tile.getInvSize(); i++) {

			ItemStack iStack = this.tile.getWandItem(i);
			if (iStack.isEmpty()) { continue; }

			IMFTool wand = (IMFTool) iStack.getItem();
			int wandMF = wand.getMF(iStack);
			int wabdMax = wand.getMaxMF(iStack);

			//描画位置を計算
			int tip_x2 = (this.width - this.xSize) / 2;
			int tip_y2 = (this.height - this.ySize) / 2;

			switch (i) {
			case 1:
				this.renderMF(tip_x2 + 39, tip_y2 + 104, mouseX, mouseY, wandMF, wabdMax);
				break;
			case 2:
				this.renderMF(tip_x2 + 16, tip_y2 + 42, mouseX, mouseY, wandMF, wabdMax);
				break;
			case 3:
				this.renderMF(tip_x2 + 63, tip_y2 + 42, mouseX, mouseY, wandMF, wabdMax);
				break;
			case 4:
				this.renderMF(tip_x2 + 3, tip_y2 + 79, mouseX, mouseY, wandMF, wabdMax);
				break;
			case 5:
				this.renderMF(tip_x2 + 75, tip_y2 + 79, mouseX, mouseY, wandMF, wabdMax);
				break;
			}
		}
	}

	public void renderMF (int x, int y, int mouseX, int mouseY, int wandMF, int wabdMax) {

		if (x <= mouseX && mouseX <= x + 15
				&& y <= mouseY && mouseY <= y + 2) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			this.drawHoveringText(String.format("%,d", wandMF) + "mf / " + String.format("%,d", wabdMax) + "mf", xAxis, yAxis);
		}
	}
}
