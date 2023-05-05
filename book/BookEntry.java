package sweetmagic.init.tile.book;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class BookEntry {

	private final String name;
	private List<BookPage> pages = new ArrayList<BookPage>();
	private final BookCategory category;
	private ItemStack icon = ItemStack.EMPTY;

	public BookEntry(String name, BookCategory category) {
		this.name = "smguid.entry." + name;
		this.category = category;
		if (category != null) {
			category.addEntry(this);
		}
	}

	public String getName() {
		return this.name;
	}

	public BookCategory getCategory() {
		return this.category;
	}

	public ItemStack getIcon() {
		return this.icon;
	}

	public BookEntry setIcon(ItemStack icon) {
		this.icon = icon;
		return this;
	}

	public List<BookPage> getPages() {
		return this.pages;
	}

	public BookEntry addPage(BookPage page) {
		this.pages.add(page);
		return this;
	}
}
