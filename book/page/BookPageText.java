package sweetmagic.init.tile.book.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.tile.book.BookEntry;
import sweetmagic.init.tile.book.BookPage;
import sweetmagic.init.tile.gui.GuiGuidBook;
import sweetmagic.util.SMStack;

public class BookPageText extends BookPage {

	protected List<String> textKeys = new ArrayList<String>();
	protected Set<BookEntry> relatedEntries = new HashSet<BookEntry>();
	private List<ItemStack> stackList = new ArrayList<>();

	public BookPageText(BookEntry entry, String... textKeys) {
		super(entry);
		for (String textKey : textKeys) {
			this.textKeys.add("smguid.text." + textKey);
		}
	}

	public BookPageText(BookEntry entry, SMStack smStack, String... textKeys) {
		super(entry);
		this.stackList = smStack.getStackList();
		for (String textKey : textKeys) {
			this.textKeys.add("smguid.text." + textKey);
		}
	}

	@Override
	public void drawScreen(GuiGuidBook gui, int mouseX, int mouseY, float partialTicks) {

		int y = gui.guiTop + 6;
		int x = gui.guiLeft + (GuiGuidBook.WIDTH / 2);
		int maxY = gui.guiTop + GuiGuidBook.HEIGHT;

		Gui.drawRect(x - 71 , y - 2, x + 68, y + 170, 0x62ffffff);

		// タイトル
		gui.drawCenteredText(TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + I18n.translateToLocal(this.entry.getName()), x, y, 0x000000);

		// ページ数
		String page = (gui.currentPageNum + 1) + "/" + gui.currentEntry.getPages().size();
		gui.drawCenteredText(TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + I18n.translateToLocal(page), x, y + 160, 0x000000);


		if (!this.stackList.isEmpty()) {

			Gui.drawRect(x - 70 , y + 10, x + 67, y + 29, 0x58000000);

			GlStateManager.pushMatrix();
			RenderHelper.enableGUIStandardItemLighting();

			//描画位置を計算
			int tip_x = (gui.width - GuiGuidBook.WIDTH) / 2 + 37;
			int tip_y = (gui.height - GuiGuidBook.HEIGHT) / 2 + 17;

			// 座標の取得
			RenderItem render = Minecraft.getMinecraft().getRenderItem();

			int size = this.stackList.size();
			int addX = size > 1 ? 0 : 27;
			addX += size > 3 ? -3 : 0;
			int dis = size > 3 ? 20 : 25;

			for (int i = 0; i < size; i++) {
				render.renderItemIntoGUI(this.stackList.get(i), tip_x + i * dis + addX, tip_y);
			}

			y += 20;

			RenderHelper.disableStandardItemLighting();
			GlStateManager.popMatrix();
		}

		FontRenderer font = gui.getFontRenderer();
		y += font.FONT_HEIGHT + 4;
		x = gui.guiLeft + 5;

		float textScale = 0.935F;
		GlStateManager.pushMatrix();

		GlStateManager.scale(textScale, textScale, textScale);

		for (int i = 0; i < this.textKeys.size(); i++) {

			int increment = (int) ((font.FONT_HEIGHT + 1) * textScale);
			List<String> lines = this.getLine(gui, I18n.translateToLocal(this.textKeys.get(i)), textScale);

			for (String text : lines) {

				if (y + increment > maxY) { break; }

				text = text.replace("<t>", "  ").replace("<s>", " ");
				float slid = 0.1F;
				int pX = (int) (x / textScale);
				int pY = (int) (y / textScale);
				font.drawString(text, pX + slid, pY + slid, 0x2c2c33, false);
				font.drawString(text, pX, pY, 0x2c2c33, false);
				y += increment;
			}

			y += (int) (4 * textScale);
		}

		GlStateManager.popMatrix();

		if (this.relatedEntries.size() > 0) {
			String text = TextFormatting.UNDERLINE + I18n.translateToLocal("smguid.label.related_page");
			int textWidth = gui.getFontRenderer().getStringWidth(text);
			GlStateManager.color(1F, 1F, 1F, 1F);
			gui.mc.getTextureManager().bindTexture(GuiGuidBook.TEX);
			gui.drawTexturedModalRect(gui.guiLeft + GuiGuidBook.WIDTH, gui.guiTop + 10, 136 - (textWidth + 2), 210, textWidth + 12, 16);
			gui.getFontRenderer().drawString(text, gui.guiLeft + GuiGuidBook.WIDTH + 2, gui.guiTop + 10 + 4, 0x000000);
		}

		if (!this.stackList.isEmpty()) {

			GlStateManager.pushMatrix();

			//描画位置を計算
			int tip_x = (gui.width - GuiGuidBook.WIDTH) / 2 + 64;
			int tip_y = (gui.height - GuiGuidBook.HEIGHT) / 2 + 18;
			int size = this.stackList.size();

			for (int i = 0; i < size; i++) {

				int addX = size > 1 ? 27 : 0;
				addX += size > 3 ? 1 : 0;
				int dis = size > 3 ? 20 : 25;
				addX = addX + i * -dis;

				if (tip_x <= mouseX + addX && mouseX + addX <= tip_x + 15
						&& tip_y <= mouseY && mouseY <= tip_y + 15) {

					gui.renderToolTip(this.stackList.get(i), mouseX, mouseY);
				}
			}

			GlStateManager.popMatrix();
		}
	}

	public List<String> getLine (GuiGuidBook gui,String text, float textScale) {

		List<String> newLine = new ArrayList<>();

		for (String line : Arrays.<String>asList(text.split("<br>"))) {
			newLine.addAll(gui.getFontRenderer().listFormattedStringToWidth(line, (int) ((GuiGuidBook.WIDTH - 10) / textScale)));
		}

		return newLine;
	}

	@Override
	public void onOpened(GuiGuidBook gui) {

		super.onOpened(gui);
		gui.addNavButtons();
		int x = gui.guiLeft + GuiGuidBook.WIDTH;
		int y = gui.guiTop + 10 + 16 + 2;
		int i = 0;
		for (BookEntry entry : this.relatedEntries) {
			if (i < 9) {
				gui.getButtonList().add(new RelatedEntryButton(gui.nextButtonID(), x, y, gui.getFontRenderer(), entry));
				y += 16;
				i++;
			}
		}
	}

	@Override
	public void onClosed(GuiGuidBook gui) {
		super.onClosed(gui);
	}

	@Override
	public void actionPerformed(GuiGuidBook gui, GuiButton button) {
		if (button instanceof RelatedEntryButton) {
			gui.goToEntry(((RelatedEntryButton) button).getEntry());
		}
	}

	public BookPageText addRelatedEntry(BookEntry entry) {
		if (entry != null) {
			this.relatedEntries.add(entry);
		}
		return this;
	}

	public BookPageText addRelatedEntries(BookEntry... entries) {
		for (BookEntry entry : entries)
			if (entry != null) {
				this.relatedEntries.add(entry);
			}
		return this;
	}

	@SideOnly(Side.CLIENT)
	public class RelatedEntryButton extends GuiButton {

		private static final int BASE_WIDTH = 24;
		private static final int BASE_HEIGHT = 16;
		private int ticksHovered = 0;
		private FontRenderer fontRenderer;
		private final BookEntry entry;

		public RelatedEntryButton(int buttonId, int x, int y, FontRenderer fontRenderer, BookEntry entry) {
			super(buttonId, x, y, BASE_WIDTH, BASE_HEIGHT, entry.getName());
			this.fontRenderer = fontRenderer;
			this.entry = entry;
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {

			if (this.visible) {
				String text = I18n.translateToLocal(this.displayString);
				int textWidth = this.fontRenderer.getStringWidth(text);

				boolean flag = mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height;
				if (flag) {

					if (this.width < 140 && this.width < textWidth + BASE_WIDTH + 2) {
						this.ticksHovered++;
						this.width = Math.min(BASE_WIDTH + (ticksHovered * 8), Math.min(textWidth + BASE_WIDTH + 2, 144));
					}
				}

				else {
					this.ticksHovered = 0;
					if (this.width > BASE_WIDTH) {
						this.width = Math.max(this.width - 12, BASE_WIDTH);
					}
				}

				mc.getTextureManager().bindTexture(GuiGuidBook.TEX);
				Gui.drawModalRectWithCustomSizedTexture(this.x, this.y, 146 - this.width, 226, this.width, this.height, 256F, 256F);
				text = this.fontRenderer.trimStringToWidth(text, this.width - BASE_WIDTH, true);
				textWidth = this.fontRenderer.getStringWidth(text);
				this.fontRenderer.drawString(text, x + (this.width - BASE_WIDTH) - textWidth, y + 4, 0x000000);

				if (this.entry != null && !this.entry.getIcon().isEmpty()) {
					GlStateManager.pushMatrix();
					GlStateManager.color(1F, 1F, 1F, 1F);
					RenderHelper.enableGUIStandardItemLighting();
					mc.getRenderItem().renderItemIntoGUI(this.entry.getIcon(), x + width - 22, y);
					RenderHelper.disableStandardItemLighting();
					GlStateManager.popMatrix();
				}
			}
		}

		public BookEntry getEntry() {
			return this.entry;
		}
	}
}
