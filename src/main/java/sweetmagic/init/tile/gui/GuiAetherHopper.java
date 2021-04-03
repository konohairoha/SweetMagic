package sweetmagic.init.tile.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Optional;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.BlockInit;
import sweetmagic.init.tile.container.ContainerAetherHopper;
import sweetmagic.init.tile.magic.TileAetherHopper;
import vazkii.quark.api.IChestButtonCallback;

@Optional.Interface(modid="quark", iface="vazkii.quark.api.IChestButtonCallback")
public class GuiAetherHopper extends GuiContainer implements IChestButtonCallback {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_aetherhopper.png");
	private final TileAetherHopper tile;

	public GuiAetherHopper(InventoryPlayer invPlayer, TileAetherHopper tile) {
		super(new ContainerAetherHopper(invPlayer, tile));
		this.xSize = 176;
		this.ySize = 175;
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

		//こっちではゲージ量を計算する　かまどのMFの内容ができてから？
		if (!this.tile.isMfEmpty()) {

			// ゲージ描画数
			int progress = this.tile.getMfProgressScaled(76);

			// (X開始位置、Y開始位置、ゲージの左下X、ゲージの左下Y、ゲージのXサイズ、ゲージのYサイズ)
			this.drawTexturedModalRect(x + 13, y + 87 - progress, 180, 87 - progress, 15, progress);
		}

		if (!this.tile.getCleroItem().isEmpty()) {

			NBTTagCompound tags = this.tile.getCleroItem().getTagCompound();
			int posX = tags.getInteger("X");
			int posY = tags.getInteger("Y");
			int posZ = tags.getInteger("Z");
			String tip = "pos" + ": " + posX + "," + posY + "," + posZ;
			boolean isHopper = this.tile.getBlock(new BlockPos(posX, posY, posZ)) == BlockInit.aether_hopper;
			this.fontRenderer.drawString(I18n.format((isHopper ? TextFormatting.GREEN : TextFormatting.RED) + tip, new Object[0]), x + 70, y + 73, 0x404040, true);
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
		tip_x += 12;
		tip_y += 10;

		if (tip_x <= mouseX && mouseX <= tip_x + 15
				&& tip_y <= mouseY && mouseY <= tip_y + 76) {

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
