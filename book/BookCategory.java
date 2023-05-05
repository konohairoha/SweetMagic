package sweetmagic.init.tile.book;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import sweetmagic.init.BookInit;

public class BookCategory {


	protected final String name;
	protected List<BookEntry> entries = new ArrayList<BookEntry>();
	protected ResourceLocation icon = null;
	protected BookEntryCategory entry;

	public BookCategory(String name) {
		this.name = "smguid.category." + name;
		this.entry = new BookEntryCategory(name, this);
		BookInit.categories.add(this);
	}

	public String getName() {
		return this.name;
	}

	public ResourceLocation getIcon() {
		return this.icon;
	}

	public BookCategory setIcon(ResourceLocation icon) {
		this.icon = icon;
		return this;
	}

	public List<BookEntry> getEntries() {
		return this.entries;
	}

	public BookCategory addEntry(BookEntry entry) {
		this.entries.add(entry);
		return this;
	}

	public BookEntryCategory getCategoryEntry() {
		return this.entry;
	}
}
