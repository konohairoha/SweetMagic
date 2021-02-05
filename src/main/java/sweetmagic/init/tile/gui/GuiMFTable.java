package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.tile.container.ContainerMFTable;
import sweetmagic.init.tile.magic.TileMFTable;

@SideOnly(Side.CLIENT)
public class GuiMFTable extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("sweetmagic", "textures/gui/gui_magictable.png");
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
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

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

		ItemStack stack = this.tile.getWandItem(0);

		if (!stack.isEmpty()) {
			IWand wand = (IWand) stack.getItem();
			progress = wand.getMfProgressScaled(stack, 14);
			this.drawTexturedModalRect(x + 39, y + 66, 240, 50, progress, 3);
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
		tip_x += 172;
		tip_y += 49;

		if (tip_x <= mouseX && mouseX <= tip_x + 5
				&& tip_y <= mouseY && mouseY <= tip_y + 50) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			this.drawHoveringText(String.format("%,d", mf) + "mf / " + String.format("%,d", max) + "mf", xAxis, yAxis);
		}


		ItemStack stack = this.tile.getWandItem(0);

		if (stack.isEmpty()) {
			return;
		}

//		IWand wand = (IWand) stack.getItem();

//		int wandMF = wand.getMF(stack);
//		int wabdMax = wand.getMaxMF(stack);

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

			this.drawHoveringText(String.format("%,d", mf) + "mf / " + String.format("%,d", max) + "mf", xAxis, yAxis);
		}
	}
}
