package sweetmagic.init.tile.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.container.ContainerMFArcaneTable;
import sweetmagic.init.tile.magic.TileMFArcaneTable;

public class GuiMFArcaneTable extends GuiContainer {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_arcanetable.png");
	private final TileMFArcaneTable tile;

	public GuiMFArcaneTable(InventoryPlayer invPlayer, TileMFArcaneTable tile) {
		super(new ContainerMFArcaneTable(invPlayer, tile));
		this.xSize = 176;
		this.ySize = 178;
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

		// 進行度の表示
		if (this.tile.isCharm && this.tile.charmTime > 0) {
			int progress = this.tile.charmTime;
			this.drawTexturedModalRect(x + 95, y + 39, 201, 10, progress, 3);
		}

		if (this.tile.isHeal && this.tile.healTime > 0) {
			int progress = this.tile.healTime;
			this.drawTexturedModalRect(x + 95, y + 75, 201, 10, progress, 3);
		}

		boolean isOver = this.tile.canEnchant() && !this.tile.getWandItem(0).isEmpty();
		String text = this.getText(new TextComponentTranslation("enchantment.aethercharm.name", new Object[0]).getFormattedText(), isOver);
		this.fontRenderer.drawString(I18n.format(text, new Object[0]), x + 63, y + 21, 0x404040, true);

		boolean isHeal = this.tile.canEnchant() && !this.tile.getWandItem(1).isEmpty();
		String healText = this.getText(new TextComponentTranslation("enchantment.mfrecover.name", new Object[0]).getFormattedText(), isHeal);
		this.fontRenderer.drawString(I18n.format(healText, new Object[0]), x + 63, y + 57, 0x404040, true);
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

		int tip_x2 = tip_x + 49;
		int tip_y2 = tip_y + 7;

		if (tip_x2 <= mouseX && mouseX <= tip_x2 + 84
				&& tip_y2 <= mouseY && mouseY <= tip_y2 + 17) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);
			this.drawHoveringText(this.getLine("tip.aether_charm.name"), xAxis, yAxis);
		}

		tip_x2 = tip_x + 49;
		tip_y2 = tip_y + 43;

		if (tip_x2 <= mouseX && mouseX <= tip_x2 + 84
				&& tip_y2 <= mouseY && mouseY <= tip_y2 + 17) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);
			this.drawHoveringText(this.getLine("tip.mfrecover.name"), xAxis, yAxis);
		}

		tip_x2 = tip_x + 82;
		tip_y2 = tip_y + 28;

		if (tip_x2 <= mouseX && mouseX <= tip_x2 + 52
				&& tip_y2 <= mouseY && mouseY <= tip_y2 + 5) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			float progress = this.tile.charmTime * 2F;
			this.drawHoveringText(progress + "%", xAxis, yAxis);
		}

		tip_x2 = tip_x + 82;
		tip_y2 = tip_y + 64;

		if (tip_x2 <= mouseX && mouseX <= tip_x2 + 52
				&& tip_y2 <= mouseY && mouseY <= tip_y2 + 5) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			float progress = this.tile.healTime * 2F;
			this.drawHoveringText(progress + "%", xAxis, yAxis);
		}
	}

	public List<String> getLine (String tips) {

		List<String> textList = new ArrayList<>();

		for (String text : Arrays.<String>asList( new TextComponentTranslation(tips, new Object[0]).getFormattedText().split("<br>"))) {
			textList.add(I18n.format(TextFormatting.GREEN + text));
		}

		return textList;
	}


}

