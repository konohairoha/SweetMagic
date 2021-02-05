package sweetmagic.init.tile.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import sweetmagic.init.tile.container.ContainerRobe;
import sweetmagic.init.tile.inventory.InventoryRobe;
import vazkii.quark.api.IChestButtonCallback;

@Optional.Interface(modid="quark", iface="vazkii.quark.api.IChestButtonCallback")
public class GuiRobe extends GuiContainer implements IChestButtonCallback {

	private final static ResourceLocation TEX = new ResourceLocation( "textures/gui/container/generic_54.png");

	public GuiRobe(InventoryPlayer inventoryPlayer, InventoryRobe inv) {
		super(new ContainerRobe(inventoryPlayer, inv));
		this.xSize = 176;
		this.ySize = 222;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int xMouse, int yMouse){
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEX);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

	@Optional.Method(modid="quark")
	@Override
	public boolean onAddChestButton(GuiButton button, int type) {
		return true;
	}
}
