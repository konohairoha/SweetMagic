package sweetmagic.init.tile.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.container.ContainerAetherEnchantTable;
import sweetmagic.init.tile.magic.TileAetherEnchantTable;
import vazkii.quark.api.IChestButtonCallback;

@Optional.Interface(modid="quark", iface="vazkii.quark.api.IChestButtonCallback")
public class GuiAetherEnchantTable extends GuiContainer implements IChestButtonCallback {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_workbench_storage.png");
	private final TileAetherEnchantTable tile;

	public GuiAetherEnchantTable(InventoryPlayer invPlayer, TileAetherEnchantTable tile) {
		super(new ContainerAetherEnchantTable(invPlayer, tile));
		this.xSize = 175;
		this.ySize = 229;
		this.tile = tile;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float parTick, int xMouse, int yMouse) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEX);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		//こっちではゲージ量を計算する
		if (!this.tile.isMfEmpty()) {

			// ゲージの値を設定
			int progress = this.tile.getMfProgressScaled(76);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 19, y + 84 - progress, 180, 84 - progress, 15, progress);

		}

		int nowTick_R = this.tile.nowTick_R;
		int nowTick_C = this.tile.nowTick_C;
		int nowTick_L = this.tile.nowTick_L;

		if (nowTick_R > 0) {

			// ゲージの値を設定
			int progress = this.tile.getProgressScaled(22, nowTick_R, this.tile.needTick);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 137, y + 57 , 198, 62, 15, progress);
		}

		if (nowTick_C > 0) {

			// ゲージの値を設定
			int progress = this.tile.getProgressScaled(22, nowTick_C, this.tile.needTick);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 96, y + 57 , 198, 62, 15, progress);
		}

		if (nowTick_L > 0) {

			// ゲージの値を設定
			int progress = this.tile.getProgressScaled(22, nowTick_L, this.tile.needTick);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 55, y + 57 , 198, 62, 15, progress);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		//ツールチップでMF量を表示する
		int mf = this.tile.getMF();
		int max = this.tile.getMaxMF();
		//基準点

		//描画位置を計算
		int tip_x = (this.width - this.xSize) / 2 + 18;
		int tip_y = (this.height - this.ySize) / 2 + 7;

		if (tip_x <= mouseX && mouseX <= tip_x + 15
				&& tip_y <= mouseY && mouseY <= tip_y + 76) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			this.drawHoveringText(String.format("%,d", mf) + "mf / " + String.format("%,d", max) + "mf", xAxis, yAxis);
		}

		//ゲージの位置を計算
		tip_x = (this.width - this.xSize) / 2 + 55;
		tip_y = (this.height - this.ySize) / 2 + 57;

		if (tip_x <= mouseX && mouseX <= tip_x + 15
				&& tip_y <= mouseY && mouseY <= tip_y + 22) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);
			int pro = this.tile.getProgressScaled(100, this.tile.nowTick_L, this.tile.needTick);

			this.drawHoveringText(String.format("%,d", pro) + "% / " + String.format("%,d", 100) + "%", xAxis, yAxis);
		}

		//ゲージの位置を計算
		tip_x = (this.width - this.xSize) / 2 + 96;

		if (tip_x <= mouseX && mouseX <= tip_x + 15
				&& tip_y <= mouseY && mouseY <= tip_y + 22) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);
			int pro = this.tile.getProgressScaled(100, this.tile.nowTick_C, this.tile.needTick);

			this.drawHoveringText(String.format("%,d", pro) + "% / " + String.format("%,d", 100) + "%", xAxis, yAxis);
		}

		//ゲージの位置を計算
		tip_x = (this.width - this.xSize) / 2 + 137;

		if (tip_x <= mouseX && mouseX <= tip_x + 15
				&& tip_y <= mouseY && mouseY <= tip_y + 22) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);
			int pro = this.tile.getProgressScaled(100, this.tile.nowTick_R, this.tile.needTick);

			this.drawHoveringText(String.format("%,d", pro) + "% / " + String.format("%,d", 100) + "%", xAxis, yAxis);
		}
	}

	@Optional.Method(modid="quark")
	@Override
	public boolean onAddChestButton(GuiButton button, int type) {
		return true;
	}
}
