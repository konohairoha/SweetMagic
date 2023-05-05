package sweetmagic.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.book.BookCategory;
import sweetmagic.init.tile.book.BookEntry;
import sweetmagic.init.tile.book.page.BookPageCategories;
import sweetmagic.init.tile.book.page.BookPageText;
import sweetmagic.util.SMStack;

public class BookInit {

	// category list
	public static List<BookCategory> categories = new ArrayList<BookCategory>();

	// categories
	public static BookCategory plant;
	public static BookCategory magic_tier1, magic_tier2, magic_tier3;
	public static BookCategory structure;
	public static BookCategory update;
	public static BookCategory enchant;
	public static BookCategory potion;

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
	public static BookEntry tool_repar, write, drawer, hopper, robe, harness, choker;
	public static BookEntry seaweed, seedbag;
	public static BookEntry lantan, succesor;
	public static BookEntry arcana;
	public static BookEntry un_fire;
	public static BookEntry cos_book, magia_light;
	public static BookEntry planter;
	public static BookEntry rankup;
	public static BookEntry acce;
	public static BookEntry notepc;

	public static BookEntry mf_costdown;
	public static BookEntry mf_cooltimedown;
	public static BookEntry wand_addpower;
	public static BookEntry max_mfup;
	public static BookEntry mf_recover;
	public static BookEntry aetherCharm;
	public static BookEntry elementadd;
	public static BookEntry glider;
	public static BookEntry drop_increase;

	public static BookEntry aether_barrier;
	public static BookEntry refresh_effect;
	public static BookEntry resistance_blow;
	public static BookEntry mf_down;
	public static BookEntry regene;
	public static BookEntry shadow;
	public static BookEntry aether_shield;
	public static BookEntry frosty;
	public static BookEntry flame;
	public static BookEntry gravity;
	public static BookEntry gravity_accele;
	public static BookEntry cyclone;
	public static BookEntry deadly_poison;
	public static BookEntry grant_poison;
	public static BookEntry timestop;
	public static BookEntry electric_armor;
	public static BookEntry babule;
	public static BookEntry breakblock;
	public static BookEntry enchanttable;
	public static BookEntry magiastorage;


	public static BookEntry key, flower_pot, mb_book;

	public static BookEntry hammer, reincarnation, havester, render_pedal;

	public static void init() {

		plant = new BookCategory("plant").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/block/alstroemeria_stage1.png"));
		magic_tier1 = new BookCategory("magic").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/items/aether_wand.png"));
		magic_tier2 = new BookCategory("magic_t2").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/items/divine_wand.png"));
		magic_tier3 = new BookCategory("magic_t3").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/items/purecrystal_wand.png"));
		enchant = new BookCategory("enchant").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/items/obmagia.png"));
		potion = new BookCategory("potion").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/items/mf_bottle.png"));
		structure = new BookCategory("structure").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/items/magicianbeginner_book.png"));
		update = new BookCategory("update").setIcon(new ResourceLocation(SweetMagicCore.MODID, "textures/items/magic_light.png"));

		categories_entry = new BookEntry("book", null).addPage(new BookPageCategories());

		sugarbell = new BookEntry("sugarbell", plant).setIcon(new ItemStack(ItemInit.sugarbell));
		sannyflower = new BookEntry("sannyflower", plant).setIcon(new ItemStack(BlockInit.sannyflower_plant));
		moonblossom = new BookEntry("moonblossom", plant).setIcon(new ItemStack(BlockInit.moonblossom_plant));
		fire_nasturtium = new BookEntry("fire_nasturtium", plant).setIcon(new ItemStack(ItemInit.fire_nasturtium_petal));
		drizzly_mysotis = new BookEntry("drizzly_mysotis", plant).setIcon(new ItemStack(ItemInit.dm_flower));
		alstroemeria = new BookEntry("alstroemeria", plant).setIcon(new ItemStack(BlockInit.twilight_alstroemeria));
		seedbag = new BookEntry("seedbag", plant).setIcon(new ItemStack(ItemInit.seedbag));
		seaweed = new BookEntry("seaweed", plant).setIcon(new ItemStack(ItemInit.seaweed));
		planter = new BookEntry("planter", plant).setIcon(new ItemStack(BlockInit.antique_brick_pot_r));

		pedalcreate = new BookEntry("pedalcreate", magic_tier1).setIcon(new ItemStack(BlockInit.pedestal_creat));
		aetherwand = new BookEntry("aetherwand", magic_tier1).setIcon(new ItemStack(ItemInit.aether_wand));
		obmagia = new BookEntry("obmagia", magic_tier1).setIcon(new ItemStack(BlockInit.obmagia_bottom));
		mfchanger = new BookEntry("mfchanger", magic_tier1).setIcon(new ItemStack(BlockInit.mfchanger));
		mftank = new BookEntry("mftank", magic_tier1).setIcon(new ItemStack(BlockInit.mftank));
		mftable = new BookEntry("mftable", magic_tier1).setIcon(new ItemStack(BlockInit.mftable));
		mfstuff = new BookEntry("mfstuff", magic_tier1).setIcon(new ItemStack(ItemInit.mf_stuff));
		lantan = new BookEntry("lantan", magic_tier1).setIcon(new ItemStack(BlockInit.aether_lanp));

		rankup = new BookEntry("rankup", magic_tier2).setIcon(new ItemStack(ItemInit.divine_wand));
		color_wand = new BookEntry("color_wand", magic_tier2).setIcon(new ItemStack(ItemInit.deuscrystal_wand_r));
		choker = new BookEntry("choker", magic_tier2).setIcon(new ItemStack(ItemInit.aether_choker));
		robe = new BookEntry("robe", magic_tier2).setIcon(new ItemStack(ItemInit.windine_robe));
		harness = new BookEntry("harness", magic_tier2).setIcon(new ItemStack(ItemInit.angel_harness));
		acce = new BookEntry("acce", magic_tier2).setIcon(new ItemStack(ItemInit.accebag));
		tool_repar = new BookEntry("tool_repar", magic_tier2).setIcon(new ItemStack(BlockInit.tool_repair));
		drawer = new BookEntry("drawer", magic_tier2).setIcon(new ItemStack(BlockInit.gravity_chest));
		hopper = new BookEntry("hopper", magic_tier2).setIcon(new ItemStack(BlockInit.aether_hopper));
		arcana = new BookEntry("arcana", magic_tier2).setIcon(new ItemStack(BlockInit.arcane_table));
		magia_light = new BookEntry("magia_light", magic_tier2).setIcon(new ItemStack(BlockInit.magia_light));
		notepc = new BookEntry("notepc", magic_tier2).setIcon(new ItemStack(BlockInit.note_pc));
		enchanttable = new BookEntry("enchanttable", magic_tier2).setIcon(new ItemStack(BlockInit.aether_enchanttable));

		ele_wand = new BookEntry("ele_wand", magic_tier3).setIcon(new ItemStack(ItemInit.sacred_meteor_wand));
		write = new BookEntry("write", magic_tier3).setIcon(new ItemStack(BlockInit.magia_rewrite));
		succesor = new BookEntry("succesor", magic_tier3).setIcon(new ItemStack(BlockInit.magia_successor));
		un_fire = new BookEntry("un_fire", magic_tier3).setIcon(new ItemStack(ItemInit.unyielding_fire));
		cos_book = new BookEntry("cos_book", magic_tier3).setIcon(new ItemStack(ItemInit.magic_book_cosmic));
		magiastorage = new BookEntry("magiastorage", magic_tier3).setIcon(new ItemStack(BlockInit.magia_storage_5));

		mf_costdown = new BookEntry("mf_costdown", enchant).setIcon(new ItemStack(Items.ENCHANTED_BOOK));
		mf_cooltimedown = new BookEntry("mf_cooltimedown", enchant).setIcon(new ItemStack(Items.ENCHANTED_BOOK));
		wand_addpower = new BookEntry("wand_addpower", enchant).setIcon(new ItemStack(Items.ENCHANTED_BOOK));
		max_mfup = new BookEntry("max_mfup", enchant).setIcon(new ItemStack(Items.ENCHANTED_BOOK));
		mf_recover = new BookEntry("mf_recover", enchant).setIcon(new ItemStack(Items.ENCHANTED_BOOK));
		aetherCharm = new BookEntry("aetherCharm", enchant).setIcon(new ItemStack(Items.ENCHANTED_BOOK));
		elementadd = new BookEntry("elementadd", enchant).setIcon(new ItemStack(Items.ENCHANTED_BOOK));

		aether_barrier = new BookEntry("aether_barrier", potion).setIcon(new ItemStack(ItemInit.magic_barrier));
		refresh_effect = new BookEntry("refresh_effect", potion).setIcon(new ItemStack(ItemInit.magic_refresh));
		resistance_blow = new BookEntry("resistance_blow", potion).setIcon(new ItemStack(ItemInit.aether_boot));
		mf_down = new BookEntry("mf_down", potion).setIcon(new ItemStack(ItemInit.coffee));
		regene = new BookEntry("regene", potion).setIcon(new ItemStack(ItemInit.magic_regene_shield));
		shadow = new BookEntry("shadow", potion).setIcon(new ItemStack(ItemInit.magic_shadow));
		aether_shield = new BookEntry("aether_shield", potion).setIcon(new ItemStack(ItemInit.magic_aether_shield));
		grant_poison = new BookEntry("grant_poison", potion).setIcon(new ItemStack(ItemInit.magic_poison));
		timestop = new BookEntry("timestop", potion).setIcon(new ItemStack(ItemInit.magic_futurevision));
		electric_armor = new BookEntry("electric_armor", potion).setIcon(new ItemStack(ItemInit.magic_elecarmor));
		gravity_accele = new BookEntry("gravity_accele", potion).setIcon(new ItemStack(ItemInit.magic_vector_halten));
		cyclone = new BookEntry("cyclone", potion).setIcon(new ItemStack(ItemInit.magic_avoid));
		drop_increase = new BookEntry("drop_increase", potion).setIcon(new ItemStack(ItemInit.accebag));
		frosty = new BookEntry("frosty", potion).setIcon(new ItemStack(ItemInit.magic_frost));
		flame = new BookEntry("flame", potion).setIcon(new ItemStack(ItemInit.magic_fire));
		gravity = new BookEntry("gravity", potion).setIcon(new ItemStack(ItemInit.magic_ballast));
		deadly_poison = new BookEntry("deadly_poison", potion).setIcon(new ItemStack(ItemInit.magic_deadly_poison));
		babule = new BookEntry("babule", potion).setIcon(new ItemStack(ItemInit.magic_bubleprison));
		breakblock = new BookEntry("breakblock", potion).setIcon(new ItemStack(BlockInit.magicbarrier_on));

		key = new BookEntry("key", structure).setIcon(new ItemStack(ItemInit.magickey));
		flower_pot = new BookEntry("flower_pot", structure).setIcon(new ItemStack(BlockInit.solid_star_pot));
		mb_book = new BookEntry("mb_book", structure).setIcon(new ItemStack(ItemInit.magicianbeginner_book));

//		public static BookEntry hammer, reincarnation, havester, render_pedal;
		hammer = new BookEntry("hammer", update).setIcon(new ItemStack(ItemInit.aether_hammer));
		reincarnation = new BookEntry("reincarnation", update).setIcon(new ItemStack(BlockInit.magia_reincarnation));
		havester = new BookEntry("havester", update).setIcon(new ItemStack(BlockInit.mfharvester));
		render_pedal = new BookEntry("render_pedal", update).setIcon(new ItemStack(BlockInit.pedestal_creat));

		sugarbell.addPage(new BookPageText(sugarbell, new SMStack(BlockInit.sugarbell_plant, BlockInit.glowflower_plant, BlockInit.clerodendrum), "sugarbell")
				.addRelatedEntries(sannyflower, moonblossom, alstroemeria));
		sannyflower.addPage(new BookPageText(sannyflower, new SMStack(BlockInit.sannyflower_plant, ItemInit.sannyflower_petal, ItemInit.sannyflower_seed), "sannyflower")
				.addRelatedEntries(moonblossom, alstroemeria));
		moonblossom.addPage(new BookPageText(moonblossom, new SMStack(BlockInit.moonblossom_plant, ItemInit.moonblossom_petal, ItemInit.moonblossom_seed), "moonblossom")
				.addRelatedEntries(sannyflower, alstroemeria));
		fire_nasturtium.addPage(new BookPageText(fire_nasturtium, new SMStack(BlockInit.fire_nasturtium_plant, ItemInit.fire_nasturtium_petal, ItemInit.fire_nasturtium_seed), "fire_nasturtium")
				.addRelatedEntries(alstroemeria));
		drizzly_mysotis.addPage(new BookPageText(drizzly_mysotis, new SMStack(BlockInit.dm_plant, ItemInit.dm_flower, ItemInit.dm_seed), "drizzly_mysotis")
				.addRelatedEntries(alstroemeria));
		alstroemeria.addPage(new BookPageText(alstroemeria, new SMStack(BlockInit.twilight_alstroemeria), "alstroemeria")
				.addRelatedEntries(sannyflower, moonblossom, pedalcreate))
			.addPage(new BookPageText(alstroemeria, new SMStack(BlockInit.twilight_alstroemeria, BlockInit.twilightlight), "alstroemeriacr")
				.addRelatedEntries(pedalcreate))
			.addPage(new BookPageText(alstroemeria, new SMStack(ItemInit.sannyflower_petal, ItemInit.moonblossom_petal, ItemInit.fire_nasturtium_petal, ItemInit.dm_flower), "alscraft")
					.addRelatedEntries(sannyflower, moonblossom, fire_nasturtium, drizzly_mysotis));
		alstroemeria.addPage(new BookPageText(alstroemeria, "alst_all").addRelatedEntry(alstroemeria));
		seedbag.addPage(new BookPageText(seedbag, "seedbag"));
		seaweed.addPage(new BookPageText(seaweed, "seaweed"));
		planter.addPage(new BookPageText(planter, new SMStack(BlockInit.compost_drit, BlockInit.antique_brick_pot_r, BlockInit.chestnut_planks_planter), "planter"));

		pedalcreate.addPage(new BookPageText(pedalcreate, new SMStack(BlockInit.pedestal_creat), "pedalcreate").addRelatedEntries(alstroemeria, aetherwand));
		pedalcreate.addPage(new BookPageText(pedalcreate, new SMStack(BlockInit.pedestal_creat, BlockInit.altar_creat, BlockInit.altar_creation_star), "pedalcreate_equip"));
		pedalcreate.addPage(new BookPageText(pedalcreate, "pedalcreate_block"));
		aetherwand.addPage(new BookPageText(aetherwand, new SMStack(ItemInit.aether_wand), "aetherwand")
				.addRelatedEntries(obmagia, mfchanger, mftank, mftable, ele_wand, color_wand, rankup));
		obmagia.addPage(new BookPageText(obmagia, new SMStack(BlockInit.obmagia_bottom), "obmagia")
				.addRelatedEntries(aetherwand));
		mfchanger.addPage(new BookPageText(mfchanger, new SMStack(BlockInit.mfchanger), "mfchanger")
				.addRelatedEntries(aetherwand, mftank, mftable, mfstuff));
		mftank.addPage(new BookPageText(mftank, new SMStack(BlockInit.mftank), "mftank")
				.addRelatedEntries(aetherwand, mfchanger, mftable, mfstuff));
		mftable.addPage(new BookPageText(mftable, new SMStack(BlockInit.mftable), "mftable")
				.addRelatedEntries(aetherwand, mfchanger, mftank, mfstuff));
		mfstuff.addPage(new BookPageText(mfstuff, new SMStack(ItemInit.mf_stuff), "mfstuff")
				.addRelatedEntries(aetherwand, mfchanger, mftank, mftable));
		lantan.addPage(new BookPageText(lantan, new SMStack(BlockInit.aether_lanp), "lantan")
				.addRelatedEntries(mfchanger));

		rankup.addPage(new BookPageText(rankup, new SMStack(ItemInit.divine_wand), "rankup").addRelatedEntries(aetherwand, color_wand, ele_wand));
		color_wand.addPage(new BookPageText(color_wand, new SMStack(ItemInit.deuscrystal_wand_b, ItemInit.deuscrystal_wand_r, ItemInit.deuscrystal_wand_y, ItemInit.deuscrystal_wand_g), "color_wand").addRelatedEntries(aetherwand, ele_wand));
		ele_wand.addPage(new BookPageText(ele_wand, new SMStack(ItemInit.sacred_meteor_wand), "ele_wand").addRelatedEntries(aetherwand, color_wand));
		tool_repar.addPage(new BookPageText(tool_repar, new SMStack(BlockInit.tool_repair), "tool_repar"));
		write.addPage(new BookPageText(write, new SMStack(BlockInit.magia_rewrite), "write"));
		drawer.addPage(new BookPageText(drawer, new SMStack(BlockInit.gravity_chest), "drawer"));
		hopper.addPage(new BookPageText(hopper, new SMStack(BlockInit.aether_hopper), "hopper"));
		robe.addPage(new BookPageText(robe, new SMStack(ItemInit.magicians_robe), "robe"));
		harness.addPage(new BookPageText(harness, new SMStack(ItemInit.angel_harness), "harness"));
		choker.addPage(new BookPageText(choker, new SMStack(ItemInit.aether_choker), "choker"));
		succesor.addPage(new BookPageText(succesor, new SMStack(BlockInit.magia_successor), "succesor"));
		arcana.addPage(new BookPageText(arcana, new SMStack(BlockInit.arcane_table), "arcana"));
		un_fire.addPage(new BookPageText(un_fire, new SMStack(ItemInit.unyielding_fire, ItemInit.frosted_chain, ItemInit.holly_charm, ItemInit.wind_relief), "un_fire"));
		cos_book.addPage(new BookPageText(cos_book, new SMStack(ItemInit.magic_book_cosmic), "cos_book"));
		magia_light.addPage(new BookPageText(magia_light, new SMStack(BlockInit.magia_light), "magia_light"));
		acce.addPage(new BookPageText(acce, new SMStack(ItemInit.accebag), "acce"));
		acce.addPage(new BookPageText(acce, "porch_cool"));
		notepc.addPage(new BookPageText(notepc, new SMStack(BlockInit.note_pc, BlockInit.note_pc_r, ItemInit.sm_phone_o, ItemInit.sm_phone_l), "notepc"));
		notepc.addPage(new BookPageText(notepc, "notepc_sell"));
		notepc.addPage(new BookPageText(notepc, new SMStack(BlockInit.chestnut_sapling, ItemInit.sugarbell_seed, ItemInit.peach_tart, ItemInit.aether_crystal), "notepc_list"));
		notepc.addPage(new BookPageText(notepc, new SMStack(ItemInit.seedbag, BlockInit.chestnut_sapling, Items.IRON_NUGGET, ItemInit.unmeltable_ice), "notepc_buy"));
		notepc.addPage(new BookPageText(notepc, new SMStack(Blocks.REDSTONE_TORCH, Blocks.LEVER), "notepc_auto"));
		notepc.addPage(new BookPageText(notepc, new SMStack(BlockInit.sm_display), "display"));
		enchanttable.addPage(new BookPageText(enchanttable, new SMStack(BlockInit.aether_enchanttable), "enchanttable"));
		magiastorage.addPage(new BookPageText(magiastorage, new SMStack(BlockInit.magia_storage_5), "magiastorage"));

		mf_costdown.addPage(new BookPageText(mf_costdown, "mf_costdown"));
		mf_cooltimedown.addPage(new BookPageText(mf_cooltimedown, "mf_cooltimedown"));
		wand_addpower.addPage(new BookPageText(wand_addpower, "wand_addpower"));
		max_mfup.addPage(new BookPageText(max_mfup, "max_mfup"));
		mf_recover.addPage(new BookPageText(mf_recover, "mf_recover"));
		aetherCharm.addPage(new BookPageText(aetherCharm, "aetherCharm"));
		elementadd.addPage(new BookPageText(elementadd, "elementadd"));

		aether_barrier.addPage(new BookPageText(aether_barrier, new SMStack(ItemInit.magic_barrier), "aether_barrier"));
		refresh_effect.addPage(new BookPageText(refresh_effect, new SMStack(ItemInit.magic_refresh), "refresh_effect"));
		resistance_blow.addPage(new BookPageText(resistance_blow, new SMStack(ItemInit.aether_boot), "resistance_blow"));
		mf_down.addPage(new BookPageText(mf_down, new SMStack(ItemInit.coffee), "mf_down"));
		regene.addPage(new BookPageText(regene, new SMStack(ItemInit.magic_regene_shield), "regene"));
		shadow.addPage(new BookPageText(shadow, new SMStack(ItemInit.magic_shadow), "shadow"));
		aether_shield.addPage(new BookPageText(aether_shield, new SMStack(ItemInit.magic_aether_shield), "aether_shield"));
		gravity_accele.addPage(new BookPageText(gravity_accele, new SMStack(ItemInit.magic_vector_halten), "gravity_accele").addRelatedEntries(gravity));
		grant_poison.addPage(new BookPageText(grant_poison, new SMStack(ItemInit.magic_poison), "grant_poison").addRelatedEntries(deadly_poison));
		cyclone.addPage(new BookPageText(cyclone, new SMStack(ItemInit.magic_avoid), "cyclone"));
		drop_increase.addPage(new BookPageText(drop_increase, "drop_increase"));
		timestop.addPage(new BookPageText(timestop, new SMStack(ItemInit.magic_futurevision), "timestop"));
		electric_armor.addPage(new BookPageText(electric_armor, new SMStack(ItemInit.magic_elecarmor), "electric_armor"));
		frosty.addPage(new BookPageText(frosty, new SMStack(ItemInit.magic_frost), "frosty"));
		flame.addPage(new BookPageText(flame, new SMStack(ItemInit.magic_fire), "flame"));
		gravity.addPage(new BookPageText(gravity, new SMStack(ItemInit.magic_ballast), "gravity").addRelatedEntries(gravity_accele));
		deadly_poison.addPage(new BookPageText(deadly_poison, new SMStack(ItemInit.magic_deadly_poison), "deadly_poison").addRelatedEntries(grant_poison));
		babule.addPage(new BookPageText(babule, new SMStack(ItemInit.magic_bubleprison), "babule"));
		breakblock.addPage(new BookPageText(breakblock, new SMStack(BlockInit.magicbarrier_on), "breakblock"));

		key.addPage(new BookPageText(key, new SMStack(BlockInit.magicbarrier_on, ItemInit.magickey), "key"));
		flower_pot.addPage(new BookPageText(flower_pot, "flower_pot"));
		mb_book.addPage(new BookPageText(mb_book, new SMStack(ItemInit.magicianbeginner_book, ItemInit.smhouse), "mb_book"));

		hammer.addPage(new BookPageText(hammer, "hammer"));
		reincarnation.addPage(new BookPageText(reincarnation, "reincarnation"));
		havester.addPage(new BookPageText(havester, "havester"));
		render_pedal.addPage(new BookPageText(render_pedal, "render_pedal"));
	}
}
