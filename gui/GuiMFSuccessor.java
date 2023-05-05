package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.container.ContainerMFSuccessor;
import sweetmagic.init.tile.magic.TileMFSuccessor;

public class GuiMFSuccessor extends GuiContainer {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_successor.png");
	private final TileMFSuccessor tile;

	public GuiMFSuccessor(InventoryPlayer invPlayer, TileMFSuccessor tile) {
		super(new ContainerMFSuccessor(invPlayer, tile));
		this.xSize = 176;
		this.ySize = 178;
		this.tile = tile;
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
		Minecraft.getMinecraft().renderEngine.bindTexture(TEX);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		//こっちではゲージ量を計算する
		if (!this.tile.isMfEmpty()) {

			// ゲージ描画数
			int progress = this.tile.getMfProgressScaled(76);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 13, y + 87 - progress, 180, 87 - progress, 15, progress);
		}

		if (this.tile.checkWand()) {

			int useMF = this.tile.getSuccessorCost();

			if (useMF > 0) {

				// 進行度の表示
				int progress = this.tile.smeltTime / 2;
				if (progress > 0) {
					this.drawTexturedModalRect(x + 151, y + 74 - progress, 201, 60 - progress, 5, progress);
				}

				// 必要コストの表示
				boolean isOver = this.tile.getMF() >= useMF;
				String text = new TextComponentTranslation("gui.magiawrite.name", new Object[0]).getFormattedText();
				String tip = this.getText(text, isOver) + this.getText(String.format("%,d", useMF), isOver) + this.getText("mf", isOver);
				this.fontRenderer.drawString(I18n.format(tip, new Object[0]), x + 48, y + 62, 0x404040, true);
			}
		}
	}

	public String getText (String text, boolean isOver) {
		return ( isOver ? TextFormatting.GREEN : TextFormatting.RED ) + text;
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

		int tip_x2 = tip_x + 138;
		int tip_y2 = tip_y + 13;

		if (tip_x2 <= mouseX && mouseX <= tip_x2 + 5
				&& tip_y2 <= mouseY && mouseY <= tip_y2 + 52) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			float progress = this.tile.smeltTime * 1F;
			this.drawHoveringText(progress + "%", xAxis, yAxis);
		}
	}
}
