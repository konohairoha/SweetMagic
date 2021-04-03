package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.container.ContainerMFFurnace;
import sweetmagic.init.tile.magic.TileMFF;

public class GuiMFF extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/rmfurnace_8.png");
	private final TileMFF tile;

	public GuiMFF(InventoryPlayer invPlayer, TileMFF tile) {
		super(new ContainerMFFurnace(invPlayer, tile));
		this.xSize = 209;
		this.ySize = 165;
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
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		int progress;

		if (this.tile.canSmelt()) {
			progress = this.tile.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(x + 66, y + 38 + 10 - progress, 210, 10 - progress, 21, progress + 2);
		}

		progress = this.tile.getCookProgressScaled(24);
		this.drawTexturedModalRect(x + 88, y + 35, 210, 14, progress, 17);

		//こっちではゲージ量を計算する　かまどのMFの内容ができてから？
		if (!this.tile.isMfEmpty()) {
			progress = this.tile.getMfProgressScaled(50);
			this.drawTexturedModalRect(x + 90, y + 11, 90, 168, progress, 5);
		}
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
		tip_x += 89;
		tip_y += 10;

		//drawGuiContainerForegroundLayerの場合はGUI上にないとだめのよう
		//72 * 26
		if (tip_x <= mouseX && mouseX <= tip_x + 50
				&& tip_y <= mouseY && mouseY <= tip_y + 5) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			this.drawHoveringText(String.format("%,d", mf) + "mf / " + String.format("%,d", max) + "mf", xAxis, yAxis);
		}
	}
}
