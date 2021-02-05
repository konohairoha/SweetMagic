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
	public void drawScreen(GuiGuidBook gui, int mouseX, int mouseY, float partialTicks) {
		int y = gui.guiTop + 12;
		int x = gui.guiLeft + (GuiGuidBook.WIDTH / 2);
		gui.drawCenteredText(TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + I18n.translateToLocal(BookInit.categories_entry.getName()), x, y, 0x000000);
	}

	@Override
	public void onOpened(GuiGuidBook gui) {
		int y = gui.guiTop + 28;
		int x = gui.guiLeft + (GuiGuidBook.WIDTH / 2) - 16;

		for (BookCategory category : BookInit.categories) {
			gui.getButtonList().add(new CategoryButton(gui.nextButtonID(), x, y, 32, gui.getFontRenderer(), category));
			y += 48;
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

		private int ticksHovered = 0;
		private int frame = 0;
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
				boolean flag = mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height;

				GlStateManager.enableBlend();
	            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

	            if (flag) {
	            	ticksHovered++;
	            	if (ticksHovered % 2 == 0 && frame < 7) {
	            		frame++;
	            	}
	            	String text = I18n.translateToLocal(this.displayString);
	            	int stringWidth = fontRenderer.getStringWidth(text);
	            	fontRenderer.drawString(text, x + (width / 2) - (stringWidth / 2), y + height, 0x000000);
					GlStateManager.color(1F, 1F, 1F, 1F);
	            } else {
	            	ticksHovered = 0;
	            	if (frame > 0) {
	            		frame--;
	            	}
//	            	GlStateManager.color(0F, 0F, 0F, 1F);
	            }

				mc.getTextureManager().bindTexture(category.getIcon());
				Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 32, 32, 32, 32F, 32);
//				Gui.drawModalRectWithCustomSizedTexture(x, y, 0, frame * 32F, 32, 32, 32F, 256F);
				GlStateManager.color(1F, 1F, 1F, 1F);
				GlStateManager.disableBlend();
			}
		}

		public BookEntryCategory getCategoryEntry() {
			return this.category.getCategoryEntry();
		}
	}
}
