package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.container.ContainerAetherFurnace;
import sweetmagic.init.tile.magic.TileAetherFurnace;

public class GuiAetherFurnace extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_aetherfurnace.png");
	private final TileAetherFurnace tile;

	public GuiAetherFurnace(InventoryPlayer invPlayer, TileAetherFurnace tile) {
		super(new ContainerAetherFurnace(invPlayer, tile));
		this.xSize = 176;
		this.ySize = 218;
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

		// 座標の取得
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		//こっちではゲージ量を計算する
		if (!this.tile.isMfEmpty()) {

			// ゲージ描画数
			int progress;

			// ゲージの値を設定
			progress = this.tile.getMfProgressScaled(76);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 13, y + 87 - progress, 180, 87 - progress, 15, progress);
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
		tip_x += 12;
		tip_y += 10;

		if (tip_x <= mouseX && mouseX <= tip_x + 15
				&& tip_y <= mouseY && mouseY <= tip_y + 76) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			this.drawHoveringText(String.format("%,d", mf) + "mf / " + String.format("%,d", max) + "mf", xAxis, yAxis);
		}
	}
}
