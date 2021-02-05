package sweetmagic.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.book.BookCategory;
import sweetmagic.init.tile.book.BookEntry;
import sweetmagic.init.tile.book.page.BookPageCategories;
import sweetmagic.init.tile.book.page.BookPageText;

public class BookInit {

	// category list
	public static List<BookCategory> categories = new ArrayList<BookCategory>();

	// categories
	public static BookCategory magic;
	public static BookCategory structure;
	public static BookCategory update;

	// entries
	public static BookEntry categories_entry;

	public static BookEntry aether_crystal;
	public static BookEntry sugarbell;
	public static BookEntry sannyflower;
	public static BookEntry moonblossom;
	public static BookEntry alstroemeria;
	public static BookEntry pedalcreate;
	public static BookEntry aetherwand;
	public static BookEntry obmagia;
	public static BookEntry mfchanger;
	public static BookEntry mftank;
	public static BookEntry mftable;
	public static BookEntry mfstuff;
	public static BookEntry fire_nasturtium;
	public static BookEntry drizzly_mysotis;

	public static BookEntry door, magic_add, bugs;

	public static void init() {

		magic = new BookCategory("magic").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/block/alstroemeria_stage1.png"));
//		structure = new BookCategory("structure").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/items/magicianbeginner_book.png"));
		update = new BookCategory("update").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/items/magic_light.png"));
		categories.add(magic);
//		categories.add(structure);
		categories.add(update);

		categories_entry = new BookEntry("book", null).addPage(new BookPageCategories());

//		aether_crystal = new BookEntry("aether_crystal", magic).setIcon(new ItemStack(ItemInit.aether_crystal));
		sugarbell = new BookEntry("sugarbell", magic).setIcon(new ItemStack(ItemInit.sugarbell));
		sannyflower = new BookEntry("sannyflower", magic).setIcon(new ItemStack(BlockInit.sannyflower_plant));
		moonblossom = new BookEntry("moonblossom", magic).setIcon(new ItemStack(BlockInit.moonblossom_plant));
		fire_nasturtium = new BookEntry("fire_nasturtium", magic).setIcon(new ItemStack(ItemInit.fire_nasturtium_petal));
		drizzly_mysotis = new BookEntry("drizzly_mysotis", magic).setIcon(new ItemStack(ItemInit.dm_flower));
		alstroemeria = new BookEntry("alstroemeria", magic).setIcon(new ItemStack(BlockInit.twilight_alstroemeria));
		pedalcreate = new BookEntry("pedalcreate", magic).setIcon(new ItemStack(BlockInit.pedestal_creat));
		aetherwand = new BookEntry("aetherwand", magic).setIcon(new ItemStack(ItemInit.aether_wand));
		obmagia = new BookEntry("obmagia", magic).setIcon(new ItemStack(BlockInit.obmagia_bottom));
		mfchanger = new BookEntry("mfchanger", magic).setIcon(new ItemStack(BlockInit.mfchanger));
		mftank = new BookEntry("mftank", magic).setIcon(new ItemStack(BlockInit.mftank));
		mftable = new BookEntry("mftable", magic).setIcon(new ItemStack(BlockInit.mftable));
		mfstuff = new BookEntry("mfstuff", magic).setIcon(new ItemStack(ItemInit.mf_stuff));

		door = new BookEntry("door", update).setIcon(new ItemStack(ItemInit.woodgold_3));
		magic_add = new BookEntry("magic_add", update).setIcon(new ItemStack(ItemInit.magic_gravity_break));
		bugs = new BookEntry("bugs", update).setIcon(new ItemStack(ItemInit.fire_nasturtium_petal));

		sugarbell.addPage(new BookPageText(sugarbell, "sugarbell").addRelatedEntries(sannyflower, moonblossom, alstroemeria));
		sannyflower.addPage(new BookPageText(sannyflower, "sannyflower").addRelatedEntries(moonblossom, alstroemeria));
		moonblossom.addPage(new BookPageText(moonblossom, "moonblossom").addRelatedEntries(sannyflower, alstroemeria));
		fire_nasturtium.addPage(new BookPageText(fire_nasturtium, "fire_nasturtium").addRelatedEntries(alstroemeria));
		drizzly_mysotis.addPage(new BookPageText(drizzly_mysotis, "drizzly_mysotis").addRelatedEntries(alstroemeria));
		alstroemeria.addPage(new BookPageText(alstroemeria, "alstroemeria").addRelatedEntries(sannyflower, moonblossom, pedalcreate))
		.addPage(new BookPageText(alstroemeria, "alscraft").addRelatedEntries(sannyflower, moonblossom, pedalcreate));
		pedalcreate.addPage(new BookPageText(pedalcreate, "pedalcreate").addRelatedEntries(alstroemeria, aetherwand));
		aetherwand.addPage(new BookPageText(aetherwand, "aetherwand").addRelatedEntries(obmagia, mfchanger, mftank, mftable));
		obmagia.addPage(new BookPageText(obmagia, "obmagia").addRelatedEntries(aetherwand));
		mfchanger.addPage(new BookPageText(mfchanger, "mfchanger").addRelatedEntries(aetherwand, mftank, mftable, mfstuff));
		mftank.addPage(new BookPageText(mftank, "mftank").addRelatedEntries(aetherwand, mfchanger, mftable, mfstuff));
		mftable.addPage(new BookPageText(mftable, "mftable").addRelatedEntries(aetherwand, mfchanger, mftank, mfstuff));
		mfstuff.addPage(new BookPageText(mfstuff, "mfstuff").addRelatedEntries(aetherwand, mfchanger, mftank, mftable));

		door.addPage(new BookPageText(door, "door"));
		magic_add.addPage(new BookPageText(magic_add, "magic_add"));
		bugs.addPage(new BookPageText(bugs, "bugs"));
	}
}
