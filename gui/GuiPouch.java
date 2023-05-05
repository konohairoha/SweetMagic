package sweetmagic.init.tile.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.tile.container.ContainerPouch;
import sweetmagic.init.tile.inventory.InventoryPouch;
import vazkii.quark.api.IChestButtonCallback;

@Optional.Interface(modid="quark", iface="vazkii.quark.api.IChestButtonCallback")
public class GuiPouch extends GuiContainer implements IChestButtonCallback {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID,"textures/gui/gui_porch.png");
	private InventoryPouch inv;
	private final EntityPlayer player;
	private final ItemStack stack;
	private final boolean isWnad;

	public GuiPouch(InventoryPlayer invPlayer, InventoryPouch inv) {
		super(new ContainerPouch(invPlayer, inv));
		this.inv = inv;
		this.player = invPlayer.player;
		this.stack = this.player.getHeldItemMainhand();
		this.isWnad = this.stack.getItem() instanceof IWand && !this.player.isCreative();
		this.xSize = 173;
		this.ySize = this.isWnad ? 244 : 132;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float parTick) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, parTick);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {

		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(TEX);

		// 座標の取得
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		int downY = this.isWnad ? 112 : -1;

		this.drawTexturedModalRect(x, y, 0, this.isWnad ? 0 : 113, this.xSize, this.ySize);

		if (this.inv.slotSize == 8) {
			for (int k = 0; k < 2; k++) {
				this.drawTexturedModalRect(x + 52, y + 6 + downY + k * 18, 173, 111, 54, 119);
				this.drawTexturedModalRect(x + 70, y + 6 + downY + k * 18, 173, 111, 54, 119);
			}
		}

		else {
			for (int k = 0; k < 2; k++) {
				this.drawTexturedModalRect(x + 16, y + 6 + downY + k * 18, 173, 111, 54, 119);
				this.drawTexturedModalRect(x + 70, y + 6 + downY + k * 18, 173, 111, 54, 119);
				this.drawTexturedModalRect(x + 106, y + 6 + downY + k * 18, 173, 111, 54, 119);
			}
		}

		if (this.isWnad) {

			IWand wand = (IWand) stack.getItem();
			int slotCount = 0;
			int slotMaxCount = wand.getSlot();

			for (int k = 0; k < 5; k++) {
				for (int l = 0; l < 5; l++) {
					this.drawTexturedModalRect(x + 36 + l * 21, y + 6 + k * 20, 173, 0, 18, 18);
					slotCount++;
					if (slotCount >= slotMaxCount) { return; }
				}
			}
		}
	}

	@Override
	public boolean onAddChestButton(GuiButton arg0, int arg1) {
		return true;
	}
}
