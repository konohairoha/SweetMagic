package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.container.ContainerMFChanger;
import sweetmagic.init.tile.magic.TileMFChanger;

public class GuiMFChanger extends GuiContainer {

	private static final ResourceLocation TEX_TIER1 = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_mfchanger_tier1.png");
	private static final ResourceLocation TEX_TIER2 = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_mfchanger_tier2.png");
	private final TileMFChanger tile;

	public GuiMFChanger(InventoryPlayer invPlayer, TileMFChanger tile) {
		super(new ContainerMFChanger(invPlayer, tile));
		this.xSize = 176;
		this.ySize = 152;
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
			progress = this.tile.getMfProgressScaled(106);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 35, y + 53, 0, 166, progress, 10);
		}
	}

	public ResourceLocation getTex () {

		ResourceLocation tex = null;

		switch (this.tile.getInvSize()) {
		case 1:
			tex = TEX_TIER1;
			break;
		case 5:
			tex = TEX_TIER2;
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
		tip_x += 35;
		tip_y += 53;

		if (tip_x <= mouseX && mouseX <= tip_x + 106
				&& tip_y <= mouseY && mouseY <= tip_y + 8) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			this.drawHoveringText(String.format("%,d", mf) + "mf / " + String.format("%,d", max) + "mf", xAxis, yAxis);
		}
	}
}
