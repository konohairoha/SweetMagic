package sweetmagic.init.tile.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.tile.container.BookContainer;

@SideOnly(Side.CLIENT)
public class GuiBook extends GuiContainer {

	private static final ResourceLocation TEX = new ResourceLocation("sweetmagic","textures/gui/obmagia.png");

	public GuiBook(InventoryPlayer inventoryPlayer) {
		super(new BookContainer(inventoryPlayer));
        this.xSize = 194;
		this.ySize = 222;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GlStateManager.color(1, 1, 1, 1);
		this.mc.getTextureManager().bindTexture(TEX);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
}