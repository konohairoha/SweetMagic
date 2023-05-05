package sweetmagic.init.tile.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Optional;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.tile.container.ContainerMFHarvester;
import sweetmagic.init.tile.magic.TileMFHarvester;
import sweetmagic.packet.MFHarvesterSeverPKT;
import vazkii.quark.api.IChestButtonCallback;

@Optional.Interface(modid="quark", iface="vazkii.quark.api.IChestButtonCallback")
public class GuiMFHarvester extends GuiContainer implements IChestButtonCallback {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_mfharvester.png");
	private final TileMFHarvester tile;

	public GuiMFHarvester(InventoryPlayer invPlayer, TileMFHarvester tile) {
		super(new ContainerMFHarvester(invPlayer, tile));
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

	public String getTip (String tip) {
		return new TextComponentTranslation(tip).getFormattedText();
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

		String rangeTip = TextFormatting.GOLD + this.getTip("tip.sm_range.name") + TextFormatting.WHITE + "： " + TextFormatting.GREEN + this.tile.range + TextFormatting.WHITE;
		this.fontRenderer.drawString(I18n.format(rangeTip), x + 102, y + 30, 0x404040, true);

		String flag = this.tile.isRangeRender ? "on" : "off";
		String renderTip = TextFormatting.GOLD + this.getTip("tip.render_range.name") + TextFormatting.WHITE + "： " + TextFormatting.GREEN + flag + TextFormatting.WHITE;
		this.fontRenderer.drawString(I18n.format(renderTip), x + 102, y + 40, 0x404040, true);
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
	}

	@Override
	public void initGui() {

		super.initGui();

		int y = this.height / 2 - 114;

		// X座標、Y座標、カーソル合わせたときの元X座標の差, テクスチャ
		this.buttonList.add(new GuiButtonImage(0, this.guiLeft + 53, y + 28, 13, 9, 179, 88, 10, TEX));
		this.buttonList.add(new GuiButtonImage(1, this.guiLeft + 53, y + 41, 13, 9, 211, 88, 10, TEX));
		this.buttonList.add(new GuiButtonImage(2, this.guiLeft + 68, y + 28, 15, 9, 194, 88, 10, TEX));
		this.buttonList.add(new GuiButtonImage(3, this.guiLeft + 68, y + 41, 15, 9, 226, 88, 10, TEX));

		this.buttonList.add(new GuiButtonImage(4, this.guiLeft + 56, y + 54, 21, 18, 179, 111, 19, TEX));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		BlockPos pos = this.tile.getPos();
		int range = this.tile.range;
		boolean isRender = this.tile.isRangeRender;
		boolean isChangeRange = button.id != 4;

		switch (button.id) {
		case 0:
			range++;
			break;
		case 1:
			range--;
			break;
		case 2:
			range += 10;
			break;
		case 3:
			range -= 10;
			break;
		case 4:
			isRender = !isRender;
			break;
		}

		this.tile.range = range = Math.max(0, Math.min(12, range));
		this.tile.isRangeRender = isRender;
		this.tile.resetInfo();
		PacketHandler.sendToServer(new MFHarvesterSeverPKT(pos.getX(), pos.getY(), pos.getZ(), range, isRender, isChangeRange));
	}

	@Optional.Method(modid="quark")
	@Override
	public boolean onAddChestButton(GuiButton button, int type) {
		return true;
	}
}
