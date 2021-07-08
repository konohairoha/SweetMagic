package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.init.tile.container.ContainerModenRack;

public class GuiModenRack extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_moden_rack.png");
	private final TileModenRack tile;

	public GuiModenRack(InventoryPlayer invPlayer, TileModenRack tile) {
		super(new ContainerModenRack(invPlayer, tile));
		this.xSize = 173;
		this.ySize = 132;
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

		try {
			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			switch (this.tile.getRackData()) {
			case 0:
				for (int i = 0; i < 3; i++)
					for (int k = 0; k < 2; k++)
						this.drawTexturedModalRect(x + 6 + 54 * i, y + 7 + k * 18, 173, 0, 54, 18);
				break;
			case 1:
				this.drawTexturedModalRect(x + 60, y + 24, 173, 0, 54, 18);
				break;
			case 2:
			case 3:
				this.drawTexturedModalRect(x + 78, y + 24, 173, 0, 18, 18);
				break;
			}
		}

		catch (Throwable e) { }
	}
}
