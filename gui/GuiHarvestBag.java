package sweetmagic.init.tile.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.base.BaseGuiContainer;
import sweetmagic.init.tile.container.ContainerHarvestBag;
import sweetmagic.init.tile.inventory.InventoryHarvestBag;

public class GuiHarvestBag extends BaseGuiContainer {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_gravitychest.png");

	public GuiHarvestBag(InventoryPlayer invPlayer, InventoryHarvestBag inv) {
		super(new ContainerHarvestBag(invPlayer, inv));
		this.xSize = 242;
		this.ySize = 229 + 8;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float parTick) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, parTick);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) { }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int xMouse, int yMouse) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEX);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
}
