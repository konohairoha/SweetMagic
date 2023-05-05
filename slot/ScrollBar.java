package sweetmagic.init.tile.slot;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;

@SideOnly(Side.CLIENT)
public class ScrollBar {

	protected GuiContainer gui;
	protected static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_parallel_book.png");

	// スクロールバーのテクスチャ座標
	protected final int scroll_on_x = 0;
	protected final int scroll_on_y = 202;

	// スクロールバーのテクスチャ座標(OFF)
	protected final int scroll_off_x = 12;
	protected final int scroll_off_y = 202;

	// スクロールバーのサイズ
	protected final int size_width = 12;
	protected final int size_height = 15;

	// スクロールバー基準点
	protected int scroll_base_x;
	protected int scroll_base_y;

	protected int scroll_height;		// スクロールバーの座標
	protected float scroll_coord_y;	// スクロールバー座標位置
	protected int max_page;			// スクロールバーのページ数
	public int now_page;				// スクロールバーの現在ページ数

	protected boolean leftClick = false;

	public ScrollBar(GuiContainer gui, int x, int y, int height, int page) {
		this.gui = gui;
		this.scroll_base_x = x;
		this.scroll_base_y = y;

		// ページを計算する
		this.scroll_height = height - this.size_height;
		this.scroll_coord_y = 0;
		this.max_page = page;
		this.now_page = 0;
	}

	public void setScrollMaxPage(int page) {
		if (page == this.max_page) { return; }
		this.now_page = 0;
		this.max_page = page;
	}

	public void resetScrollPage() {
		this.now_page = 0;
		this.scroll_coord_y = 0;
	}

	public void drawScrollBar() {

		// テクスチャバインド
		this.gui.mc.getTextureManager().bindTexture(TEX);

		// 描画位置を計算
		float x = gui.getGuiLeft() + this.scroll_base_x;
		float y = gui.getGuiTop() + this.scroll_base_y;

		// スクロールバーの位置を計算
		y += this.scroll_coord_y * (float) this.scroll_height;
		int tex_x = scroll_on_x;
		int tex_y = scroll_on_y;

		if (this.max_page == 0) {
			tex_x = scroll_off_x;
			tex_y = scroll_off_y;
		}

		// スクロールバーの描画
		this.gui.drawTexturedModalRect(x, y, tex_x, tex_y, size_width, size_height);
	}

	public void handleMouseInput() {

		int dwheel = Mouse.getEventDWheel();

		// trueなら上方向へ
		if (dwheel != 0) {
			this.setScrollTo(this.scroll_coord_y, 0 >= dwheel);
		}
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		// 左クリックの場合のみ実施
		if (mouseButton != 0) { return; }

		int x_str = this.gui.getGuiLeft() + this.scroll_base_x;
		int y_str = this.gui.getGuiTop() + this.scroll_base_y;
		int x_end = this.gui.getGuiLeft() + this.scroll_base_x + this.size_width;
		int y_end = this.gui.getGuiTop() + this.scroll_base_y + this.size_height + this.scroll_height;

		// スクロールバーの範囲内
		if (x_str <= mouseX && mouseX <= x_end && y_str <= mouseY && mouseY <= y_end) {

			// マウスクリック状態
			this.ScrollToMouse(mouseY - y_str);
			this.leftClick = true;
		}
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {

		// 左クリックの場合のみ実施
		if (state != 0) { return; }

		// マウスクリック状態
		this.leftClick = false;
	}

	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

		// 左クリックの場合のみ実施
		if (clickedMouseButton != 0 || !this.leftClick) { return; }

		// スクロールの位置からRateを計算
		int scrollY = this.gui.getGuiTop() + this.scroll_base_y;
		scrollY = Math.max(0, mouseY - scrollY);
		scrollY = Math.min(this.scroll_height + this.size_height, scrollY);
		this.ScrollToMouse(scrollY);
	}

	protected void ScrollToMouse(int scrollY) {

		// スクロールの位置 0.0F-0.1F
		float scrollRateY = 0.0F;
		scrollRateY = (float) scrollY / (float) this.scroll_height;
		scrollRateY = (float) (Math.floor(scrollRateY * 1000F) / 1000F);

		// スクロール位置を設定する
		this.setScrollTo(scrollRateY, false);
	}

	protected void setScrollTo(float work, boolean next) {

		// ページ設定がない場合は何もしない
		if (this.max_page == 0) { return; }

		// スクロールレートを設定
		this.scroll_coord_y = this.calScrollRate(work, next);
		IScrollBarChanged inf = (IScrollBarChanged) this.gui;
		inf.onScrollChanged(this.now_page);
	}

	private float calScrollRate(float work, boolean next) {

		if (!next && work <= 0.0F) { return 0.0F; }
		if (next && 1.0F <= work) { return 1.0F; }

		// 位置のRate計算
		float scrollPageY = Math.round((1F / (float) this.max_page) * 1000F) / 1000F;
		List<Float> scrollList = new ArrayList<Float>();

		for (float i = 0; i < this.max_page; i++) {
			scrollList.add(Math.round(scrollPageY * i * 1000F) / 1000F);
		}

		// 逆順で検索
		int scroll_idx = 0;
		scrollList.add(1F);

		if (next) {

			// work値より大きく一番近い値を取得
			for (int i = 0; 0 < scrollList.size(); i++) {

				double rate = scrollList.get(i);

				if (work < rate) {
					scroll_idx = i;
					break;
				}
			}
		}

		else {

			// work値より小さく一番近い値を取得
			for (int i = scrollList.size() - 1; 0 <= i; i--) {

				double rate = scrollList.get(i);

				if (rate < work) {
					scroll_idx = i;
					break;
				}
			}
		}

		this.now_page = scroll_idx;
		return scrollList.get(scroll_idx);
	}

	public interface IScrollBarChanged {
		public void onScrollChanged(int page);
	}
}
