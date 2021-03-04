package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import sweetmagic.init.tile.container.ContainerPouch;
import sweetmagic.init.tile.inventory.InventoryPouch;
import vazkii.quark.api.IChestButtonCallback;

@Optional.Interface(modid="quark", iface="vazkii.quark.api.IChestButtonCallback")
public class GuiPouch extends GuiContainer implements IChestButtonCallback {

	private static final ResourceLocation TEX = new ResourceLocation("sweetmagic","textures/gui/gui_moden_rack.png");

	public GuiPouch(InventoryPlayer inventoryPlayer, InventoryPouch inv) {
		super(new ContainerPouch(inventoryPlayer, inv));
		this.xSize = 173;
		this.ySize = 132;
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

		// 座標の取得
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		for (int k = 0; k < 2; k++) {
			this.drawTexturedModalRect(x + 52, y + 7 + k * 18, 173, 0, 54, 18);
			this.drawTexturedModalRect(x + 70, y + 7 + k * 18, 173, 0, 54, 18);
		}
	}

	@Override
	public boolean onAddChestButton(GuiButton arg0, int arg1) {
		return true;
	}
}
