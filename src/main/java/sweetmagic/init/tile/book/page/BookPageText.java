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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.tile.book.BookEntry;
import sweetmagic.init.tile.book.BookPage;
import sweetmagic.init.tile.gui.GuiGuidBook;

public class BookPageText extends BookPage {

	protected List<String> textKeys = new ArrayList<String>();
	protected Set<BookEntry> relatedEntries = new HashSet<BookEntry>();

	public BookPageText(BookEntry entry, String... textKeys) {
		super(entry);
		for (String textKey : textKeys) {
			this.textKeys.add("smguid.text." + textKey);
		}
	}

	@Override
	public void drawScreen(GuiGuidBook gui, int mouseX, int mouseY, float partialTicks) {

		int y = gui.guiTop + 12;
		int x = gui.guiLeft + (GuiGuidBook.WIDTH / 2);
		int maxY = gui.guiTop + GuiGuidBook.HEIGHT - 19;

//		if (this.entry != null && !this.entry.getIcon().isEmpty()) {
//
//			GlStateManager.pushMatrix();
//			GlStateManager.scale(1F, 1F, 1F);
//			GlStateManager.color(1F, 1F, 1F, 1F);
//			RenderHelper.enableGUIStandardItemLighting();
//			gui.mc.getRenderItem().renderItemIntoGUI(this.entry.getIcon(), x - 10, y - 15);
//			RenderHelper.disableStandardItemLighting();
//			GlStateManager.popMatrix();
//		}

		gui.drawCenteredText(TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + I18n.translateToLocal(entry.getName()), x, y, 0x000000);
		y += gui.getFontRenderer().FONT_HEIGHT + 4;
		x = gui.guiLeft + 8;

		float textScale = 0.9F;
		GlStateManager.pushMatrix();
		GlStateManager.scale(textScale, textScale, textScale);

		for (int i = 0; i < this.textKeys.size(); i++) {

			String text = I18n.translateToLocal(this.textKeys.get(i));
			int increment = (int) ((gui.getFontRenderer().FONT_HEIGHT + 4) * textScale);
			List<String> lines = this.getLine(text);
			List<String> newLine = this.getBreak(gui, lines, textScale);

			for (String line : newLine) {

				if (y + increment > maxY) { break; }
				gui.getFontRenderer().drawString(line, (int) (x / textScale), (int) (y / textScale), 0xFFFFFF, true);
				y += increment;
			}
			y += (int) (4 * textScale);
		}

		GlStateManager.popMatrix();

		if (relatedEntries.size() > 0) {
			String text = TextFormatting.UNDERLINE + I18n.translateToLocal("smguid.label.related_page");
			int textWidth = gui.getFontRenderer().getStringWidth(text);
			GlStateManager.color(1F, 1F, 1F, 1F);
			gui.mc.getTextureManager().bindTexture(GuiGuidBook.BOOK_BACKGROUND);
			gui.drawTexturedModalRect(gui.guiLeft + GuiGuidBook.WIDTH, gui.guiTop + 10, (146 - 10) - (textWidth + 2), 210, (textWidth + 2) + 10, 16);
			gui.getFontRenderer().drawString(text, gui.guiLeft + GuiGuidBook.WIDTH + 2, gui.guiTop + 10 + 4, 0x000000);
		}
	}

	public List<String> getLine (String lines) {
		return Arrays.<String>asList(lines.split("<br>"));
	}

	public List<String> getBreak (GuiGuidBook gui, List<String> line, float textScale) {

		List<String> newLine = new ArrayList<>();

		for (String text : line) {
			newLine.addAll(gui.getFontRenderer().listFormattedStringToWidth(text, (int) ((GuiGuidBook.WIDTH - 12) / textScale)));
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
				} else {
					this.ticksHovered = 0;
					if (this.width > BASE_WIDTH) {
						this.width = Math.max(this.width - 12, BASE_WIDTH);
					}
				}

				mc.getTextureManager().bindTexture(GuiGuidBook.BOOK_BACKGROUND);
				Gui.drawModalRectWithCustomSizedTexture(x, y, 146 - this.width, 226, this.width, this.height, 256F, 256F);
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
