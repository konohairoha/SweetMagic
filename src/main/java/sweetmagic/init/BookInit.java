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
	public static BookEntry color_wand;
	public static BookEntry ele_wand;

	public static BookEntry key, flower_pot;

	public static BookEntry tier3_block, tool_repar, write, drawer, hopper, robe, harness, choker;

//	public static BookEntry grobe, basket, wand_pedal, warp, dungen;

	public static void init() {

		magic = new BookCategory("magic").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/block/alstroemeria_stage1.png"));
		structure = new BookCategory("structure").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/items/magicianbeginner_book.png"));
		update = new BookCategory("update").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/items/magic_light.png"));
		categories.add(magic);
		categories.add(structure);
		categories.add(update);

		categories_entry = new BookEntry("book", null).addPage(new BookPageCategories());

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
		ele_wand = new BookEntry("ele_wand", magic).setIcon(new ItemStack(ItemInit.sacred_meteor_wand));
		color_wand = new BookEntry("color_wand", magic).setIcon(new ItemStack(ItemInit.deuscrystal_wand_r));

		key = new BookEntry("key", structure).setIcon(new ItemStack(ItemInit.magickey));
		flower_pot = new BookEntry("flower_pot", structure).setIcon(new ItemStack(BlockInit.solid_star_pot));

//		grobe = new BookEntry("grobe", update).setIcon(new ItemStack(ItemInit.magicians_grobe));
//		basket = new BookEntry("basket", update).setIcon(new ItemStack(BlockInit.rattan_basket_y));
//		wand_pedal = new BookEntry("wand_pedal", update).setIcon(new ItemStack(BlockInit.wand_pedal));
//		warp = new BookEntry("warp", update).setIcon(new ItemStack(BlockInit.warp_block_on));
//		dungen = new BookEntry("dungen", update).setIcon(new ItemStack(ItemInit.magicianbeginner_book));

		tier3_block = new BookEntry("tier3_block", update).setIcon(new ItemStack(BlockInit.mm_tank));
		tool_repar = new BookEntry("tool_repar", update).setIcon(new ItemStack(BlockInit.tool_repair));
		write = new BookEntry("write", update).setIcon(new ItemStack(BlockInit.magia_rewrite));
		drawer = new BookEntry("drawer", update).setIcon(new ItemStack(BlockInit.gravity_chest));
		hopper = new BookEntry("hopper", update).setIcon(new ItemStack(BlockInit.aether_hopper));
		robe = new BookEntry("robe", update).setIcon(new ItemStack(ItemInit.windine_robe));
		harness = new BookEntry("harness", update).setIcon(new ItemStack(ItemInit.angel_harness));
		choker = new BookEntry("choker", update).setIcon(new ItemStack(ItemInit.aether_choker));

		sugarbell.addPage(new BookPageText(sugarbell, "sugarbell").addRelatedEntries(sannyflower, moonblossom, alstroemeria));
		sannyflower.addPage(new BookPageText(sannyflower, "sannyflower").addRelatedEntries(moonblossom, alstroemeria));
		moonblossom.addPage(new BookPageText(moonblossom, "moonblossom").addRelatedEntries(sannyflower, alstroemeria));
		fire_nasturtium.addPage(new BookPageText(fire_nasturtium, "fire_nasturtium").addRelatedEntries(alstroemeria));
		drizzly_mysotis.addPage(new BookPageText(drizzly_mysotis, "drizzly_mysotis").addRelatedEntries(alstroemeria));
		alstroemeria.addPage(new BookPageText(alstroemeria, "alstroemeria").addRelatedEntries(sannyflower, moonblossom, pedalcreate))
		.addPage(new BookPageText(alstroemeria, "alscraft").addRelatedEntries(sannyflower, moonblossom, pedalcreate));
		pedalcreate.addPage(new BookPageText(pedalcreate, "pedalcreate").addRelatedEntries(alstroemeria, aetherwand));
		aetherwand.addPage(new BookPageText(aetherwand, "aetherwand").addRelatedEntries(obmagia, mfchanger, mftank, mftable, ele_wand, color_wand));
		obmagia.addPage(new BookPageText(obmagia, "obmagia").addRelatedEntries(aetherwand));
		mfchanger.addPage(new BookPageText(mfchanger, "mfchanger").addRelatedEntries(aetherwand, mftank, mftable, mfstuff));
		mftank.addPage(new BookPageText(mftank, "mftank").addRelatedEntries(aetherwand, mfchanger, mftable, mfstuff));
		mftable.addPage(new BookPageText(mftable, "mftable").addRelatedEntries(aetherwand, mfchanger, mftank, mfstuff));
		mfstuff.addPage(new BookPageText(mfstuff, "mfstuff").addRelatedEntries(aetherwand, mfchanger, mftank, mftable));
		color_wand.addPage(new BookPageText(color_wand, "color_wand").addRelatedEntries(aetherwand, ele_wand));
		ele_wand.addPage(new BookPageText(ele_wand, "ele_wand").addRelatedEntries(aetherwand, color_wand));

		key.addPage(new BookPageText(key, "key"));
		flower_pot.addPage(new BookPageText(flower_pot, "flower_pot"));

		tier3_block.addPage(new BookPageText(tier3_block, "tier3_block"));
		tool_repar.addPage(new BookPageText(tool_repar, "tool_repar"));
		write.addPage(new BookPageText(write, "write"));
		drawer.addPage(new BookPageText(drawer, "drawer"));
		hopper.addPage(new BookPageText(hopper, "hopper"));
		robe.addPage(new BookPageText(robe, "robe"));
		harness.addPage(new BookPageText(harness, "harness"));
		choker.addPage(new BookPageText(choker, "choker"));

//		pouch.addPage(new BookPageText(pouch, "pouch"));
//		acce.addPage(new BookPageText(acce, "acce"));
//		grobe.addPage(new BookPageText(grobe, "grobe"));
//		basket.addPage(new BookPageText(basket, "basket"));
//		wand_pedal.addPage(new BookPageText(wand_pedal, "wand_pedal"));
//		warp.addPage(new BookPageText(warp, "warp"));
//		dungen.addPage(new BookPageText(dungen, "dungen"));
	}
}
