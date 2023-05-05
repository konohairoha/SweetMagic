package sweetmagic.init.tile.book.page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BookInit;
import sweetmagic.init.tile.book.BookCategory;
import sweetmagic.init.tile.book.BookEntryCategory;
import sweetmagic.init.tile.book.BookPage;
import sweetmagic.init.tile.gui.GuiGuidBook;

public class BookPageCategories extends BookPage {

	public BookPageCategories() {
		super(BookInit.categories_entry);
	}

	@Override
	public void drawScreen(GuiGuidBook gui, int mouseX, int mouseY, float parTick) {
		int y = gui.guiTop + 6;
		int x = gui.guiLeft + (GuiGuidBook.WIDTH / 2);
		Gui.drawRect(x - 72 , y - 2, x + 70, y + 10, 0x62ffffff);
		gui.drawCenteredText(TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + I18n.translateToLocal(BookInit.categories_entry.getName()), x, y, 0x000000);
	}

	@Override
	public void onOpened(GuiGuidBook gui) {
		int y = gui.guiTop + 14;
		int x = gui.guiLeft + (GuiGuidBook.WIDTH / 2) - 45;
		int i = 0;

		for (BookCategory category : BookInit.categories) {
			gui.getButtonList().add(new CategoryButton(gui.nextButtonID(), x, y, 32, gui.getFontRenderer(), category));
			x += 55;
			i++;

			if (i % 2 == 0) {
				x = gui.guiLeft + (GuiGuidBook.WIDTH / 2) - 45;
				y += 40;
			}
		}
	}

	@Override
	public void onClosed(GuiGuidBook gui) {
		super.onClosed(gui);
	}

	@Override
	public void actionPerformed(GuiGuidBook gui, GuiButton button) {
		if (button instanceof CategoryButton) {
			gui.goToEntry(((CategoryButton) button).getCategoryEntry());
		}
	}

	@SideOnly(Side.CLIENT)
	public class CategoryButton extends GuiButton {

		private FontRenderer fontRenderer;
		private BookCategory category;

		public CategoryButton(int buttonId, int x, int y, int width, FontRenderer fontRenderer, BookCategory category) {
			super(buttonId, x, y, width, width, category.getName());
			this.fontRenderer = fontRenderer;
			this.category = category;
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {

			if (this.visible) {
				boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

				GlStateManager.enableBlend();
	            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

	            if (flag) {



	            	String text = I18n.translateToLocal(this.displayString);
	            	int stringWidth = this.fontRenderer.getStringWidth(text);

					Gui.drawRect(this.x - 4 - (stringWidth / 3), this.y + 31, this.x + 35 + (stringWidth / 3), this.y + 42, 0x40000000);
	    			this.fontRenderer.drawString(text, x + (this.width / 2) - (stringWidth / 2), y + this.height, 0xFFFFFF, true);
					GlStateManager.color(1F, 1F, 1F, 1F);
	            }

				mc.getTextureManager().bindTexture(this.category.getIcon());
				Gui.drawModalRectWithCustomSizedTexture(this.x, this.y, 0, 32, 32, 32, 32F, 32);
				GlStateManager.color(1F, 1F, 1F, 1F);
				GlStateManager.disableBlend();
			}
		}

		public BookEntryCategory getCategoryEntry() {
			return this.category.getCategoryEntry();
		}
	}
}
