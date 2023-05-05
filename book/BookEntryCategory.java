package sweetmagic.init.tile.book;

import java.util.ArrayList;
import java.util.List;

import sweetmagic.init.tile.book.page.BookPageCategory;

public class BookEntryCategory extends BookEntry {

	private static int entries_per_page = 13;

	private BookCategory entryCategory;

	public BookEntryCategory(String name, BookCategory category) {
		super(name, null);
		this.entryCategory = category;
	}

	@Override
	public String getName() {
		return this.entryCategory.getName();
	}

	@Override
	public List<BookPage> getPages() {

		List<BookPage> pages = new ArrayList<BookPage>();
		int numEntries = this.entryCategory.getEntries().size();
		int numPages = numEntries / this.entries_per_page;

		if (numEntries % this.entries_per_page != 0) {
			numPages++;
		}

		for (int i = 0; i < numPages; i++) {

			BookPageCategory page = new BookPageCategory(this.entryCategory, this);

			for (int j = 0; j < this.entries_per_page && (j + (i * this.entries_per_page)) < numEntries; j++) {
				page.addEntry(this.entryCategory.getEntries().get(j + (i * this.entries_per_page)));
			}
			pages.add(page);
		}

		return pages;
	}
}
