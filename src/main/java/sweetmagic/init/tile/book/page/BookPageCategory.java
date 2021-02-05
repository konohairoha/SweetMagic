package sweetmagic.init.tile.book.page;

import java.util.ArrayList;
import java.util.List;

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
import sweetmagic.init.tile.book.BookCategory;
import sweetmagic.init.tile.book.BookEntry;
import sweetmagic.init.tile.book.BookEntryCategory;
import sweetmagic.init.tile.book.BookPage;
import sweetmagic.init.tile.gui.GuiGuidBook;

public class BookPageCategory extends BookPage {

	private final BookCategory category;

	protected List<BookEntry> entries = new ArrayList<BookEntry>();

	public BookPageCategory(BookCategory category, BookEntryCategory entry) {
		super(entry);
		this.category = category;
	}

	@Override
	public void drawScreen(GuiGuidBook gui, int mouseX, int mouseY, float partialTicks) {
		int y = gui.guiTop + 9;
		int x = gui.guiLeft + (GuiGuidBook.WIDTH / 2);
		gui.drawCenteredText(TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + I18n.translateToLocal(category.getName()), x, y, 0x000000);
	}

	@Override
	public void onOpened(GuiGuidBook gui) {
		gui.addNavButtons();

		int x = gui.guiLeft + 6;
		int y = gui.guiTop + 20;
		for (BookEntry entry : this.entries) {
			gui.getButtonList().add(new EntryLinkButton(gui.nextButtonID(), x, y, GuiGuidBook.WIDTH - 30, gui.getFontRenderer(), entry));
			y += (gui.getFontRenderer().FONT_HEIGHT + 3);
		}
	}

	@Override
	public void onClosed(GuiGuidBook gui) {
		super.onClosed(gui);
	}

	@Override
	public void actionPerformed(GuiGuidBook gui, GuiButton button) {
		if (button instanceof EntryLinkButton) {
			gui.goToEntry(((EntryLinkButton) button).getEntry());
		}
	}

	public BookPageCategory addEntry(BookEntry entry) {
		this.entries.add(entry);
		return this;
	}

	@SideOnly(Side.CLIENT)
	public class EntryLinkButton extends GuiButton {

		private int ticksHovered = 0;
		private FontRenderer fontRenderer;
		private final BookEntry entry;

		public EntryLinkButton(int buttonId, int x, int y, int width, FontRenderer fontRenderer, BookEntry entry) {
			super(buttonId, x, y, width, fontRenderer.FONT_HEIGHT + 2, entry.getName());
			this.fontRenderer = fontRenderer;
			this.entry = entry;
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				boolean flag = mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height;
				if (flag) {
					if (this.ticksHovered < width) {
						this.ticksHovered++;
					}
					int rectWidth = Math.min(ticksHovered * 10, width + 15);
					Gui.drawRect(x, y, x + rectWidth, y + height, 0x40000000);
				} else {
					this.ticksHovered = 0;
				}
				this.fontRenderer.drawString(I18n.translateToLocal(this.displayString), x + fontRenderer.FONT_HEIGHT + 2 + 1, y + 2, 0xFFFFFF, true);
				if (this.entry != null && !this.entry.getIcon().isEmpty()) {
					float scale = (fontRenderer.FONT_HEIGHT + 2) / 16F;

					GlStateManager.pushMatrix();
					GlStateManager.scale(scale, scale, scale);
					GlStateManager.color(1F, 1F, 1F, 1F);
					RenderHelper.enableGUIStandardItemLighting();

					mc.getRenderItem().renderItemIntoGUI(this.entry.getIcon(), (int) ((x) / scale), (int) (y / scale));

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
