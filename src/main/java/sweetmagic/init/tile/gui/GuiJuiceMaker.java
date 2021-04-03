package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.container.ContainerJuiceMaker;
import sweetmagic.init.tile.cook.TileJuiceMaker;

@SideOnly(Side.CLIENT)
public class GuiJuiceMaker extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_juicemaker.png");
	private final TileJuiceMaker tile;

	public GuiJuiceMaker(InventoryPlayer invPlayer, TileJuiceMaker tile) {
		super(new ContainerJuiceMaker(invPlayer, tile));
		this.xSize = 176;
		this.ySize = 185;
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
		this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);

		// 座標の取得
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		if (!this.tile.isEmptyWater()) {

			// ゲージ描画数
			int progress;

			// ゲージの値を設定
			progress = this.tile.getMfProgressScaled(76);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 29, y + 95 - progress, 191, 77 - progress, 15, progress);
		}

		if (this.tile.cookTime > 0) {

			// ゲージ描画数
			int progress = this.tile.getCookProgress(22);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 96, y + 35, 211 , 14, 2 +progress, 16);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		//ツールチップでMF量を表示する
		int mf = this.tile.getWaterValue();
		int max = this.tile.getWaterMaxValue();
		//基準点

		//描画位置を計算
		int tip_x = (this.width - this.xSize) / 2;
		int tip_y = (this.height - this.ySize) / 2;

		//ゲージの位置を計算
		tip_x += 28;
		tip_y += 18;

		if (tip_x <= mouseX && mouseX <= tip_x + 15
				&& tip_y <= mouseY && mouseY <= tip_y + 76) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			this.drawHoveringText(mf + "ml / " + max + "ml", xAxis, yAxis);
		}
	}
}
