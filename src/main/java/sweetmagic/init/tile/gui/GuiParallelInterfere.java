package sweetmagic.init.tile.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.tile.container.ContainerParallelInterfere;
import sweetmagic.init.tile.magic.TileParallelInterfere;
import sweetmagic.init.tile.slot.ScrollBar;
import sweetmagic.init.tile.slot.ScrollBar.IScrollBarChanged;
import sweetmagic.packet.ScrollPagePKT;
import vazkii.quark.api.IChestButtonCallback;

@Optional.Interface(modid="quark", iface="vazkii.quark.api.IChestButtonCallback")
public class GuiParallelInterfere extends GuiContainer implements IScrollBarChanged, IChestButtonCallback  {

	private static final ResourceLocation TEX = new ResourceLocation("sweetmagic", "textures/gui/gui_parallel_book.png");
	private final TileParallelInterfere tile;

	public ContainerParallelInterfere continer;
	protected ScrollBar scrollBar;
	protected int guiWidth = 193;
	protected int guiHeight = 202;
	public EntityPlayer player;
	public int maxPage = 20;

	public GuiParallelInterfere(InventoryPlayer invPlayer, TileParallelInterfere tile) {
		super(new ContainerParallelInterfere(invPlayer, tile));
		this.continer = new ContainerParallelInterfere(invPlayer, tile);
		this.xSize = this.guiWidth;
		this.ySize = this.guiHeight;
		this.tile = tile;
		// x座標 y座標 スクロールの高さ スクロールのページ数
		this.scrollBar = new ScrollBar(this, 174, 18, 106, this.maxPage);
		this.player = invPlayer.player;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		// テクスチャロード
		Minecraft.getMinecraft().renderEngine.bindTexture(TEX);

		// 画面へバインド（かまどのGUIサイズ）
		int xSize = this.guiWidth;
		int ySize = this.guiHeight;

		// 描画位置を計算
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2 + 10;

		// 画面へ描画
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		// スクロールバーの描画
		this.scrollBar.setScrollMaxPage(this.maxPage);
		this.scrollBar.drawScrollBar();
		this.continer.scrollPage = this.scrollBar.now_page;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	public interface IScrollChanged {
		public void onScrollChanged(int page);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.scrollBar.handleMouseInput();
	}


	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
		super.mouseClicked(x, y, button);
		this.scrollBar.mouseClicked(x, y, button);
	}

	@Override
	protected void mouseReleased(int x, int y, int state) {
		super.mouseReleased(x, y, state);
		this.scrollBar.mouseReleased(x, y, state);
	}

	@Override
	protected void mouseClickMove(int x, int y, int button, long lastClick) {
		super.mouseClickMove(x, y, button, lastClick);
		this.scrollBar.mouseClickMove(x, y, button, lastClick);
	}

	//ページ設定
	@Override
	public void onScrollChanged(int page) {
		this.tile.setPage(page);
		PacketHandler.sendToServer(new ScrollPagePKT(this.scrollBar.now_page));
		this.continer.onScrollChanged(page);
	}

	@Optional.Method(modid="quark")
	@Override
	public boolean onAddChestButton(GuiButton button, int type) {
		return true;
	}
}
