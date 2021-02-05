package sweetmagic.init.tile.book;

import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.tile.gui.GuiGuidBook;

public abstract class BookPage {

	protected BookEntry entry;

	public BookPage(BookEntry entry) {
		this.entry = entry;
	}

	@SideOnly(Side.CLIENT)
	public abstract void drawScreen(GuiGuidBook gui, int mouseX, int mouseY, float partialTicks);

	@SideOnly(Side.CLIENT)
	public void update() {};

	@SideOnly(Side.CLIENT)
	public void onOpened(GuiGuidBook gui) {}

	@SideOnly(Side.CLIENT)
	public void onClosed(GuiGuidBook gui) {
		gui.getButtonList().clear();
	}

	@SideOnly(Side.CLIENT)
	public void actionPerformed(GuiGuidBook gui, GuiButton button) {}

	@SideOnly(Side.CLIENT)
	public void keyPressed(char c, int key) {}

}
