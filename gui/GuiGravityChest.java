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
import sweetmagic.init.tile.container.ContainerGravityChest;
import sweetmagic.init.tile.magic.TileGravityChest;
import sweetmagic.packet.GravityChestSeverPKT;
import vazkii.quark.api.IChestButtonCallback;

@Optional.Interface(modid="quark", iface="vazkii.quark.api.IChestButtonCallback")
public class GuiGravityChest extends GuiContainer implements IChestButtonCallback {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_gravitychest.png");
	private final TileGravityChest tile;

	public GuiGravityChest(InventoryPlayer invPlayer, TileGravityChest tile) {
		super(new ContainerGravityChest(invPlayer, tile));
		this.xSize = 242;
		this.ySize = 229 + 8;
		this.tile = tile;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	public String getTip (String tip) {
		return new TextComponentTranslation(tip, new Object[0]).getFormattedText();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float parTick, int xMouse, int yMouse) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEX);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		this.drawTexturedModalRect(x + 217, y + 160, 246, 119, 6, 51);

		//こっちではゲージ量を計算する　かまどのMFの内容ができてから？
		if (!this.tile.isMfEmpty()) {
			int progress = this.tile.getMfProgressScaled(50);
			this.drawTexturedModalRect(x + 218, y + 211 - progress, 247, 222 - progress, 4, progress + 1);
		}

		this.drawTexturedModalRect(x + 2, y + 181, 74, 237, 37, 18);

		String tip = TextFormatting.GOLD + this.getTip("tip.sm_range.name") + TextFormatting.WHITE + "：" + TextFormatting.GREEN + this.tile.range + TextFormatting.WHITE;
		this.fontRenderer.drawString(I18n.format(tip), x + 4, y + 183, 0x404040, true);
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
		tip_x += 217;
		tip_y += 160;

		//drawGuiContainerForegroundLayerの場合はGUI上にないとだめのよう
		//72 * 26
		if (tip_x <= mouseX && mouseX <= tip_x + 5
				&& tip_y <= mouseY && mouseY <= tip_y + 51) {

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			this.drawHoveringText(String.format("%,d", mf) + "mf / " + String.format("%,d", max) + "mf", xAxis, yAxis);
		}
	}

	@Override
	public void initGui() {

		super.initGui();

		// X座標、Y座標、カーソル合わせたときの元X座標の差, テクスチャ
		this.buttonList.add(new GuiButtonImage(0, this.guiLeft + 5, this.height / 2 + 41, 13, 9, 6, 237, 10, TEX));
		this.buttonList.add(new GuiButtonImage(1, this.guiLeft + 5, this.height / 2 + 52, 13, 9, 40, 237, 10, TEX));
		this.buttonList.add(new GuiButtonImage(2, this.guiLeft + 20, this.height / 2 + 41, 15, 9, 21, 237, 10, TEX));
		this.buttonList.add(new GuiButtonImage(3, this.guiLeft + 20, this.height / 2 + 52, 15, 9, 55, 237, 10, TEX));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		BlockPos pos = this.tile.getPos();
		int range = this.tile.range;

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
		}

		this.tile.range = range = Math.max(1, Math.min(64, range));
		PacketHandler.sendToServer(new GravityChestSeverPKT(pos.getX(), pos.getY(), pos.getZ(), range));
	}

	@Optional.Method(modid="quark")
	@Override
	public boolean onAddChestButton(GuiButton button, int type) {
		return true;
	}
}
