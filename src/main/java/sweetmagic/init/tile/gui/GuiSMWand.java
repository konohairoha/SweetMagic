package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.tile.container.ContainerSMWand;
import sweetmagic.init.tile.inventory.InventorySMWand;

@SideOnly(Side.CLIENT)
public class GuiSMWand extends GuiContainer {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID,"textures/gui/gui_wand.png");

	EntityPlayer player;	// プレイヤー
	ItemStack stack;		// 杖のアイテムスタック
	IWand wand;				// 杖
	int slot;				// スロット数

	public GuiSMWand(InventoryPlayer inventoryPlayer, InventorySMWand invGem) {
		super(new ContainerSMWand(inventoryPlayer, invGem));
		this.player = inventoryPlayer.player;
		this.stack = this.player.getHeldItemMainhand();
		this.wand = (IWand) this.stack.getItem();
		this.xSize = 196;
		this.ySize = 211;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

	// GUIの描画処理
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {

		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(TEX);

		// 座標の取得
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		// ゲージ描画数
		int progress;

		//こっちではゲージ量を計算する
		if (this.wand.getMF(this.stack) > 0) {

			// ゲージの値を設定
			progress = this.wand.getMfProgressScaled(this.stack, 50);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 21, y + 90 - progress, 231, 49 - progress, 5, progress + 1);
		}

		this.slot = this.wand.getSlot(this.stack);

		// スロット数に合わせてスロットの表示する数を変更
		switch (this.slot) {
		case 2:
			this.twoSlot(x, y);
			break;
		case 3:
			this.threeSlot(x, y);
			break;
		case 4:
			this.fourSlot(x, y);
			break;
		case 6:
			this.sixSlot(x, y);
			break;
		case 8:
			this.eightSlot(x, y);
			break;
		case 9:
			this.nineSlot(x, y);
			break;
		case 12:
			this.to12Slot(x, y);
			break;
		case 16:
			this.to16Slot(x, y);
			break;
		case 20:
			this.to20Slot(x, y);
			break;
		case 25:
			this.to25Slot(x, y);
			break;
		}

	}

	// 2スロット（真ん中）
	public void twoSlot (int x, int y) {
		for (int i = 0; i < 2; i++)
			this.drawTexturedModalRect(x + 59 + 60 * i, y + 59, 238, 0, 18, 18);
	}

	// 3スロット（真ん中）
	public void threeSlot (int x, int y) {
		for (int i = 0; i < 3; i++)
			this.drawTexturedModalRect(x + 59 + 30 * i, y + 59, 238, 0, 18, 18);
	}

	// 4スロット（上と下）
	public void fourSlot (int x, int y) {
		for (int i = 0; i < 2; i++)
			for (int k = 0; k < 2; k++)
				this.drawTexturedModalRect(x + 59 + 50 * i, y + 39 + 40 * k, 238, 0, 18, 18);
	}

	// 6スロット（上と下）
	public void sixSlot (int x, int y) {
		for (int i = 0; i < 3; i++)
			for (int k = 0; k < 2; k++)
				this.drawTexturedModalRect(x + 59 + 30 * i, y + 39 + 40 * k, 238, 0, 18, 18);
	}

	// 8スロット
	public void eightSlot (int x, int y) {

		// 上と下
		for (int i = 0; i < 3; i++)
			for (int k = 0; k < 2; k++)
				this.drawTexturedModalRect(x + 59 + 30 * i, y + 29 + 60 * k, 238, 0, 18, 18);

		// 真ん中
		for (int i = 0; i < 2; i++)
			this.drawTexturedModalRect(x + 59 + 60 * i, y + 59, 238, 0, 18, 18);
	}

	// 9スロット
	public void nineSlot (int x, int y) {

		// 上と下
		for (int i = 0; i < 3; i++)
			for (int k = 0; k < 3; k++)
				this.drawTexturedModalRect(x + 59 + 30 * i, y + 29 + 30 * k, 238, 0, 18, 18);
	}

	// 12スロット
	public void to12Slot (int x, int y) {

		// 上と下
		for (int i = 0; i < 4; i++)
			for (int k = 0; k < 3; k++)
				this.drawTexturedModalRect(x + 49 + 30 * i, y + 29 + 30 * k, 238, 0, 18, 18);
	}

	// 16スロット
	public void to16Slot (int x, int y) {

		// 上と下
		for (int i = 0; i < 4; i++)
			for (int k = 0; k < 4; k++)
				this.drawTexturedModalRect(x + 49 + 30 * i, y + 19 + 23 * k, 238, 0, 18, 18);
	}

	// 20スロット
	public void to20Slot (int x, int y) {

		// 上と下
		for (int i = 0; i < 5; i++)
			for (int k = 0; k < 4; k++)
				this.drawTexturedModalRect(x + 39 + 24 * i, y + 13 + 21 * k, 238, 0, 18, 18);
	}

	// 25スロット
	public void to25Slot (int x, int y) {

		// 上と下
		for (int i = 0; i < 5; i++)
			for (int k = 0; k < 5; k++)
				this.drawTexturedModalRect(x + 39 + 24 * i, y + 13 + 19 * k, 238, 0, 18, 18);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		//ツールチップでMF量を表示する
		int mf = this.wand.getMF(this.stack);
		int max = this.wand.getMaxMF(this.stack);

		//基準点

		//描画位置を計算
		int tip_x = (this.width - this.xSize) / 2;
		int tip_y = (this.height - this.ySize) / 2;

		//ゲージの位置を計算
		tip_x += 19;
		tip_y += 39;

		if (tip_x <= mouseX && mouseX <= tip_x + 6
				&& tip_y <= mouseY && mouseY <= tip_y + 117) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			this.drawHoveringText(String.format("%,d", mf) + "mf / " + String.format("%,d", max) + "mf", xAxis, yAxis);
		}
	}
}
