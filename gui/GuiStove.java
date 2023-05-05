package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.container.ContainerStove;
import sweetmagic.init.tile.cook.TileStove;

public class GuiStove extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_stove.png");
	private final TileStove tile;

	public GuiStove(InventoryPlayer invPlayer, TileStove tile) {
		super(new ContainerStove(invPlayer, tile));
		this.xSize = 176;
		this.ySize = 152;
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

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

//		int progress;
	}
}
