package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.container.ContainerMFFisher;
import sweetmagic.init.tile.magic.TileMFFisher;
import sweetmagic.init.tile.magic.TileMFGeneration;

public class GuiMFFisher extends GuiContainer {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_aetherfurnace.png");
	private static final ResourceLocation TEX_GEN = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_generetion.png");
	private final TileMFFisher tile;
	private final int data;

	public GuiMFFisher(InventoryPlayer invPlayer, TileMFFisher tile) {
		super(new ContainerMFFisher(invPlayer, tile));
		this.tile = tile;
		this.data = tile.getData();
		this.xSize = 176;
		this.ySize = 218 - (this.data == 3 ? 40 : 0);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float parTick) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, parTick);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {

		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(this.data == 3 ? TEX_GEN : TEX);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		//こっちではゲージ量を計算する
		if (!this.tile.isMfEmpty()) {

			// ゲージの値を設定
			int progress = this.tile.getMfProgressScaled(76);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 13, y + 87 - progress, 180, 87 - progress, 15, progress);

		}

		if (this.data == 3) {

			TileMFGeneration gen = (TileMFGeneration) this.tile;
			if (gen.isLavaEmpty()) { return; }

			// ゲージの値を設定
			int progress = gen.getLavaProgressScaled(76);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 149, y + 87 - progress, 199, 87 - progress, 15, progress);
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

		if (this.data == 3) {

			tip_x += 136;

			if (tip_x <= mouseX && mouseX <= tip_x + 15
					&& tip_y <= mouseY && mouseY <= tip_y + 76) {

				TileMFGeneration gen = (TileMFGeneration) this.tile;

				//GUIの左上からの位置
				int xAxis = (mouseX - (this.width - this.xSize) / 2);
				int yAxis = (mouseY - (this.height - this.ySize) / 2);

				this.drawHoveringText(String.format("%,d", gen.getLavaAmount()) + "mb / " + String.format("%,d", gen.getMaxLavaAmount()) + "mb", xAxis, yAxis);
			}
		}
	}
}
