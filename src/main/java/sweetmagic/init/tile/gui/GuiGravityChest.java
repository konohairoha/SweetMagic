package sweetmagic.init.tile.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.chest.TileGravityChest;
import sweetmagic.init.tile.container.ContainerGravityChest;
import vazkii.quark.api.IChestButtonCallback;

@Optional.Interface(modid="quark", iface="vazkii.quark.api.IChestButtonCallback")
public class GuiGravityChest extends GuiContainer implements IChestButtonCallback {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_gravitychest.png");
	private final TileGravityChest tile;

	public GuiGravityChest(InventoryPlayer invPlayer, TileGravityChest tile) {
		super(new ContainerGravityChest(invPlayer, tile));
		this.xSize = 242;
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

		this.drawTexturedModalRect(x + 217, y + 155, 246, 114, 6, 51);

		//こっちではゲージ量を計算する　かまどのMFの内容ができてから？
		if (!this.tile.isMfEmpty()) {
			int progress = this.tile.getMfProgressScaled(50);
			this.drawTexturedModalRect(x + 218, y + 206 - progress, 247, 217 - progress, 4, progress + 1);
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
		tip_x += 217;
		tip_y += 155;

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

	@Optional.Method(modid="quark")
	@Override
	public boolean onAddChestButton(GuiButton button, int type) {
		return true;
	}
}
