package sweetmagic.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iblock.ISmeltItemBlock;
import sweetmagic.init.block.blocks.AntiqueBrick;
import sweetmagic.init.block.blocks.AntiqueSlab;
import sweetmagic.init.block.blocks.AntiqueStairs;
import sweetmagic.init.block.blocks.BlockCafeBoard;
import sweetmagic.init.block.blocks.BlockCandle;
import sweetmagic.init.block.blocks.BlockFermenter;
import sweetmagic.init.block.blocks.BlockFlourMill;
import sweetmagic.init.block.blocks.BlockFreezer;
import sweetmagic.init.block.blocks.BlockFryPan;
import sweetmagic.init.block.blocks.BlockJuiceMaker;
import sweetmagic.init.block.blocks.BlockKandan;
import sweetmagic.init.block.blocks.BlockLanp;
import sweetmagic.init.block.blocks.BlockLantern;
import sweetmagic.init.block.blocks.BlockLight;
import sweetmagic.init.block.blocks.BlockModenRack;
import sweetmagic.init.block.blocks.BlockModenStair;
import sweetmagic.init.block.blocks.BlockOven;
import sweetmagic.init.block.blocks.BlockParallelInterfere;
import sweetmagic.init.block.blocks.BlockPole;
import sweetmagic.init.block.blocks.BlockPot;
import sweetmagic.init.block.blocks.BlockSMOre;
import sweetmagic.init.block.blocks.BlockStove;
import sweetmagic.init.block.blocks.ChestnutPlank;
import sweetmagic.init.block.blocks.ChestnutSlab;
import sweetmagic.init.block.blocks.ChestnutStairs;
import sweetmagic.init.block.blocks.CleroLanp;
import sweetmagic.init.block.blocks.FacePlanks;
import sweetmagic.init.block.blocks.FruitLeaves;
import sweetmagic.init.block.blocks.IronFence;
import sweetmagic.init.block.blocks.MagicBook;
import sweetmagic.init.block.blocks.ObMagia;
import sweetmagic.init.block.blocks.PlantPot;
import sweetmagic.init.block.blocks.SMBlockItem;
import sweetmagic.init.block.blocks.SMChair;
import sweetmagic.init.block.blocks.SMDoor;
import sweetmagic.init.block.blocks.SMGlass;
import sweetmagic.init.block.blocks.SMGlassPane;
import sweetmagic.init.block.blocks.SMIron;
import sweetmagic.init.block.blocks.SMLeaves;
import sweetmagic.init.block.blocks.SMLog;
import sweetmagic.init.block.blocks.SMPlate;
import sweetmagic.init.block.blocks.SMSapling;
import sweetmagic.init.block.blocks.SMTable;
import sweetmagic.init.block.blocks.SMTrapDoor;
import sweetmagic.init.block.blocks.SpawnStone;
import sweetmagic.init.block.blocks.StardustCrystal;
import sweetmagic.init.block.crop.BlockAlstroemeria;
import sweetmagic.init.block.crop.BlockChestnut;
import sweetmagic.init.block.crop.BlockCornFlower;
import sweetmagic.init.block.crop.BlockLierRose;
import sweetmagic.init.block.crop.BlockWhitenet;
import sweetmagic.init.block.crop.MagiaFlower;
import sweetmagic.init.block.crop.SweetCrops_STAGE4;
import sweetmagic.init.block.crop.SweetCrops_STAGE5;
import sweetmagic.init.block.crop.SweetCrops_STAGE6;
import sweetmagic.init.block.crop.SweetCrops_Tall;
import sweetmagic.init.block.magic.AetherFurnace;
import sweetmagic.init.block.magic.MFChange;
import sweetmagic.init.block.magic.MFFisher;
import sweetmagic.init.block.magic.MFFurnace;
import sweetmagic.init.block.magic.MFPot;
import sweetmagic.init.block.magic.MFTable;
import sweetmagic.init.block.magic.MFTank;
import sweetmagic.init.block.magic.MagicDirt;
import sweetmagic.init.block.magic.MagicLight;
import sweetmagic.init.block.magic.PedalCreate;

public class BlockInit {

	// 栗木材
	public static Block chestnut_log, chestnut_leaves, chestnut_sapling;
	public static Block chestnut_planks, chestnut_slab, chestnut_stairs, chestnut_plate, chestnut_plant;

	// レモン木材
	public static Block lemon_log, lemon_leaves, lemon_sapling;
	public static Block lemon_planks, lemon_slab, lemon_stairs, lemon_trapdoor, lemon_trapdoor_n, lemon_plate;

	// ミカン木材
	public static Block orange_log, orange_sapling, orange_leaves;
	public static Block orange_planks, orange_planks_mossy, orange_planks_huti1, orange_planks_huti3;
	public static Block orange_slab, orange_huti_slab, orange_stairs, orange_huti_stairs, orange_plate;

	// ミカン白木材
	public static Block orange_planks_w, orange_planks_wdamage, orange_planks_wmossy, orange_planks_w_huti1, orange_planks_w_huti3;
	public static Block orange_wstairs, orange_wslab, orange_whuti_slab, orange_whuti_stairs, orange_wplate;

	// ヤシ木材
	public static Block coconut_log, coconut_sapling, coconut_leaves;
	public static Block coconut_planks, coconut_slab, coconut_stairs, coconut_trapdoor, coconut_plate, coconut_plant;

	// プリズミ原木
	public static Block prism_log, prism_leaves, prism_sapling;
	public static Block prism_planks, prism_slab, prism_stairs, prism_plate;

	public static Block banana_leaves, banana_sapling;

	// ガラス
	public static Block sugarglass, shading_sugarglass, frosted_glass_line, frosted_glass, prismglass;

	// 板ガラス
	public static Block sugarglass_pane, shading_sugarglass_pane, frosted_glass_line_pane, frosted_glass_pane, prismglass_pane;

	// 料理器具
	public static Block flourmill_off, flourmill_on, flourmill_re;
	public static Block oven, oven_on, oven_re;
	public static Block stove_off, stove_on;
	public static Block pot_off, pot_on, pot_re;
	public static Block frypan_off, frypan_on, frypan_re;
	public static Block juicemaker_off, juicemaker_on;
	public static Block freezer_top, freezer_bottom;
	public static Block matured_bottle;

	// 花
	public static Block cornflower, lily_valley, twilight_alstroemeria, lier_rose;
	public static Block clerodendrum, clerolanp;

	// 鉱石
	public static Block ac_ore, alt_block;

	// MFブロック
	public static Block mfchanger, mftank, mfpot, mffisher, flyishforer, mftable;
	public static Block advanced_mftank, advanced_mftable, advanced_mfchanger;
	public static Block advanced_aether_furnace_top, advanced_aether_furnace_bottom;
	public static Block obmagia_top, obmagia_bottom;
	public static Block mffurnace_on, mffurnace_off;
	public static Block pedestal_creat;
	public static Block aether_furnace_top, aether_furnace_bottom;
	public static Block parallel_interfere, twilightalstroemeria_pot;

	// 光源
	public static Block magiclight, glow_light, antique_candle, gorgeous_lamp, glow_lamp, magic_circle, twilightlight;

	// レンガ
	public static Block antique_brick_0, antique_brick_1, antique_brick_2;
	public static Block antique_brick_0w, antique_brick_1w, antique_brick_2w;
	public static Block antique_brick_b;
	public static Block antique_brick_stairs, antique_brick_slab;
	public static Block antique_brick_stairs_w, antique_brick_slab_w;
	public static Block antique_brick_stairs_b, antique_brick_slab_b;
	public static Block antique_tdoor_0, antique_tdoor_0w, antique_tdoor_b;
	public static Block flagstone, flagstone_stairs, flagstone_slab;
	public static Block flagstone_color, flagstone_color_stairs, flagstone_color_slab;
	public static Block old_brick, old_brick_stairs, old_brick_slab;
	public static Block old_brick_r, old_brick_r_stairs, old_brick_r_slab;
	public static Block old_brick_g, old_brick_g_stairs, old_brick_g_slab;
	public static Block old_brick_y, old_brick_y_stairs, old_brick_y_slab;
	public static Block old_brick_l, old_brick_l_stairs, old_brick_l_slab;
	public static Block old_brick_b, old_brick_b_stairs, old_brick_b_slab;
	public static Block old_brick_s, old_brick_s_stairs, old_brick_s_slab;

	// 窓
	public static Block antique_window_white, antique_window_brown, antique_window_brown2;

	// ドア
	public static Block black_moderndoor, brown_2paneldoor, brown_5paneldoor, brown_elegantdoor, brown_arch_door, brown_arch_plantdoor;
	public static Block woodgold_3, whitewoodgold_3;

	// トラップドア
	public static Block white_woodtrapdoor;

	// 作物
	public static Block sannyflower_plant, moonblossom_plant, dm_plant, sugarbell_plant;
	public static Block blueberry_plant, sticky_stuff_plant, fire_nasturtium_plant, strawberry_plant;
	public static Block vannila_plant, olive_plant, rice_plant, soybean_plant;
	public static Block whitenet_plant, corn_plant, sweetpotato_plant;
	public static Block tomato_plant, egg_plant,  j_radish_plant, lettuce_plant, cabbage_plant;
	public static Block azuki_plant, onion_plant, raspberry_plant, glowflower_plant, cotton_plant;
	public static Block banana_plant;

	// 街頭
	public static Block pole_down, pole, lantern, lantern_side1, lantern_side2;

	// 家具
	public static Block cafeboard, kanban_top, kanban_bot;
	public static Block smchair, antique_back_chair, smtable, smtable_lace;
	public static Block moden_rack, moden_rack_brown, moden_wallrack;
	public static Block moden_stair, plate, magicbook;

	public static Block white_ironfence;
	public static Block antique_brick_pot_r, orange_planks_pot, orange_planks_pot_w, compost_drit;

	public static Block woodbox;

	public static Block spawn_stone, smspaner, sturdust_crystal_bot, sturdust_crystal_top;

	// ブロックをまず登録
	public static Block sample;

    public static List<Block> blockList = new ArrayList();
    public static List<Block> noTabList = new ArrayList();

	// 登録したブロックにデータを与える
	public static void init() {

		// 栗木材
		chestnut_log = new SMLog("chestnut_log");
		chestnut_leaves = new SMLeaves("chestnut_leaves", 0);
		chestnut_sapling = new SMSapling("chestnut_sapling", 2);
		chestnut_planks = new ChestnutPlank("chestnut_planks");
		chestnut_slab = new ChestnutSlab("chestnut_slab");
		chestnut_stairs = new ChestnutStairs("chestnut_stairs", chestnut_planks.getDefaultState());
		chestnut_plate = new SMPlate("chestnut_plate");

		// レモン木材
		lemon_log = new SMLog("lemon_log");
		lemon_leaves = new FruitLeaves("lemon_leaves", 0);
		lemon_sapling = new SMSapling("lemon_sapling", 1);
		lemon_planks = new ChestnutPlank("lemon_planks");
		lemon_slab = new ChestnutSlab("lemon_slab");
		lemon_stairs = new ChestnutStairs("lemon_stairs", lemon_planks.getDefaultState());
		lemon_trapdoor = new SMTrapDoor("lemon_trapdoor", 0, Material.WOOD);
		lemon_trapdoor_n = new SMTrapDoor("lemon_trapdoor_normal", 1, Material.WOOD);
		lemon_plate = new SMPlate("lemon_plate");

		// ミカン木材
		orange_log = new SMLog("orange_log");
		orange_leaves = new FruitLeaves("orange_leaves", 1);
		orange_sapling = new SMSapling("orange_sapling", 0);
		orange_planks = new ChestnutPlank("orange_planks");
		orange_planks_mossy = new ChestnutPlank("orange_planks_mossy");
		orange_planks_huti1 = new FacePlanks("orange_planks_huti1");
		orange_planks_huti3 = new ChestnutPlank("orange_planks_huti3");
		orange_slab = new ChestnutSlab("orange_slab");
		orange_huti_slab = new ChestnutSlab("orange_huti_slab");
		orange_stairs = new ChestnutStairs("orange_stairs", orange_planks.getDefaultState());
		orange_huti_stairs = new ChestnutStairs("orange_huti_stairs", orange_planks_huti3.getDefaultState());
		orange_plate = new SMPlate("orange_plate");

		// ミカン白木材
		orange_planks_w = new ChestnutPlank("orange_planks_w");
		orange_planks_wdamage = new ChestnutPlank("orange_planks_wdamage");
		orange_planks_wmossy = new ChestnutPlank("orange_planks_wmossy");
		orange_planks_w_huti1 = new FacePlanks("orange_planks_w_huti1");
		orange_planks_w_huti3 = new ChestnutPlank("orange_planks_w_huti3");
		orange_wslab = new ChestnutSlab("orange_wslab");
		orange_whuti_slab = new ChestnutSlab("orange_whuti_slab");
		orange_wstairs = new ChestnutStairs("orange_wstairs", orange_planks_w.getDefaultState());
		orange_whuti_stairs = new ChestnutStairs("orange_whuti_stairs", orange_planks_w_huti3.getDefaultState());
		orange_wplate = new SMPlate("orange_wplate");
		white_woodtrapdoor = new SMTrapDoor("white_woodtrapdoor", 0, Material.WOOD);

		// ヤシ木材
		coconut_log = new SMLog("coconut_log");
		coconut_leaves = new SMLeaves("coconut_leaves", 1);
		coconut_sapling = new SMSapling("coconut_sapling", 3);
		coconut_planks = new ChestnutPlank("coconut_planks");
		coconut_slab = new ChestnutSlab("coconut_slab");
		coconut_stairs = new ChestnutStairs("coconut_stairs", coconut_planks.getDefaultState());
		coconut_trapdoor = new SMTrapDoor("coconut_trapdoor", 0, Material.WOOD);
		coconut_plate = new SMPlate("coconut_plate");

		prism_log = new SMLog("prism_log");
		prism_leaves = new SMLeaves("prism_leaves", 2);
		prism_sapling = new SMSapling("prism_sapling", 4);
		prism_planks = new ChestnutPlank("prism_planks");
		prism_slab = new ChestnutSlab("prism_slab");
		prism_stairs = new ChestnutStairs("prism_stairs", prism_planks.getDefaultState());
		prism_plate = new SMPlate("prism_plate");

		banana_leaves = new SMLeaves("banana_leaves", 3);
		banana_sapling = new SMSapling("banana_sapling", 5);

		// ガラス
		sugarglass = new SMGlass("sugarglass", 0);
		shading_sugarglass = new SMGlass("shading_sugarglass", 1);
		frosted_glass = new SMGlass("frosted_glass", 0);
		frosted_glass_line = new SMGlass("frosted_glass_line", 0);
		prismglass = new SMGlass("prismglass", 2);

		// 板ガラス
		sugarglass_pane = new SMGlassPane("sugarglass_pane", blockList, 0);
		shading_sugarglass_pane = new SMGlassPane("shading_sugarglass_pane", blockList, 1);
		frosted_glass_pane = new SMGlassPane("frosted_glass_pane", blockList, 0);
		frosted_glass_line_pane = new SMGlassPane("frosted_glass_line_pane", blockList, 0);
		prismglass_pane = new SMGlassPane("prismglass_pane", blockList, 2);

		// 料理器具
		flourmill_off = new BlockFlourMill("flourmill_off", blockList);
		flourmill_on = new BlockFlourMill("flourmill_on", noTabList);
		flourmill_re = new BlockFlourMill("flourmill_re", noTabList);
		oven = new BlockOven("oven",0, blockList);
		oven_on = new BlockOven("oven_on",0.8f, noTabList);
		oven_re = new BlockOven("oven_re",0, noTabList);
		stove_off = new BlockStove("stove_off", 0F, blockList);
		stove_on = new BlockStove("stove_on", 0.5F, noTabList);
		pot_off = new BlockPot("pot_off",0, 0, blockList);
		pot_on = new BlockPot("pot_on",0.8F, 1, noTabList);
		pot_re = new BlockPot("pot_re",0, 1, noTabList);
		frypan_off = new BlockFryPan("frypan_off",0, blockList);
		frypan_on = new BlockFryPan("frypan_on",0.8f, noTabList);
		frypan_re = new BlockFryPan("frypan_re",0, noTabList);
		juicemaker_off = new BlockJuiceMaker("juicemaker_off", blockList);
		juicemaker_on = new BlockJuiceMaker("juicemaker_on", noTabList);
		freezer_top = new BlockFreezer("freezer_top", true, noTabList);
		freezer_bottom = new BlockFreezer("freezer_bottom", false, blockList);
		matured_bottle = new BlockFermenter("matured_bottle", blockList);

		// 花
		cornflower = new BlockCornFlower("cornflower", 0);
		lily_valley = new BlockCornFlower("lily_valley", 1);
		twilight_alstroemeria = new BlockAlstroemeria("twilight_alstroemeria");
		lier_rose = new BlockLierRose("lier_rose");
		clerolanp = new CleroLanp("clerolanp");

		// 鉱石
		ac_ore = new BlockSMOre("ac_ore", 0);
		alt_block = new SMIron("alt_block", 2.4F, 16.0F, 1, 0);

		// MFブロック
		mfchanger = new MFChange("mfchanger", 0);
		advanced_mfchanger = new MFChange("advanced_mfchanger", 1);
		mftank = new MFTank("mftank", 0);
		advanced_mftank = new MFTank("advanced_mftank", 1);
		mfpot = new MFPot("mfpot", 0);
		twilightalstroemeria_pot = new MFPot("twilightalstroemeria_pot", 1);
		mffurnace_off = new MFFurnace("mffurnace_off", blockList);
		mffurnace_on = new MFFurnace("mffurnace_on", noTabList);
		mffisher = new MFFisher("mffisher", 0);
		flyishforer = new MFFisher("flyishforer", 1);
		mftable = new MFTable("mftable", 0);
		advanced_mftable = new MFTable("advanced_mftable", 1);
		obmagia_top = new ObMagia("alt_table_top", noTabList);
		obmagia_bottom = new ObMagia("alt_table_bottom", blockList);
		pedestal_creat = new PedalCreate("pedestal_creat");
		aether_furnace_top = new AetherFurnace("aether_furnace_top", noTabList, true, false);
		aether_furnace_bottom = new AetherFurnace("aether_furnace_bottom", blockList, false, false);

		advanced_aether_furnace_top = new AetherFurnace("advanced_aether_furnace_top", noTabList, true, true);
		advanced_aether_furnace_bottom = new AetherFurnace("advanced_aether_furnace_bottom", blockList, false, true);
		parallel_interfere = new BlockParallelInterfere("parallel_interfere");

		// 光源
		antique_candle = new BlockCandle("antique_candle");
		glow_lamp = new BlockLanp("glow_lamp", 0, 32F, 0, 1, 0.5F, SoundType.METAL, blockList);
		magic_circle = new BlockLanp("magic_circle", 0, 32F, 0, 1, 0.5F, SoundType.GLASS, noTabList);
		glow_light = new BlockLight("glow_light");
		gorgeous_lamp = new BlockLight("gorgeous_lamp");

		// レンガ
		antique_brick_0 = new AntiqueBrick("antique_brick_0", 1.25F, 1024F, 0, 0);
		antique_brick_1 = new AntiqueBrick("antique_brick_1", 1.25F, 1024F, 0, 0);
		antique_brick_2 = new AntiqueBrick("antique_brick_2", 1.25F, 1024F, 0, 0);
		antique_brick_0w = new AntiqueBrick("antique_brick_0w", 1.25F, 1024F, 0, 0);
		antique_brick_1w = new AntiqueBrick("antique_brick_1w", 1.25F, 1024F, 0, 0);
		antique_brick_2w = new AntiqueBrick("antique_brick_2w", 1.25F, 1024F, 0, 0);
		antique_brick_b = new AntiqueBrick("antique_brick_b", 1.25F, 1024F, 0, 0);
		antique_brick_stairs = new AntiqueStairs("antique_brick_stairs", antique_brick_0.getDefaultState());
		antique_brick_stairs_w = new AntiqueStairs("antique_brick_stairs_w", antique_brick_0w.getDefaultState());
		antique_brick_stairs_b = new AntiqueStairs("antique_brick_stairs_b", antique_brick_b.getDefaultState());
		antique_brick_slab = new AntiqueSlab("antique_brick_slab");
		antique_brick_slab_w = new AntiqueSlab("antique_brick_slab_w");
		antique_brick_slab_b = new AntiqueSlab("antique_brick_slab_b");
		antique_tdoor_0 = new SMTrapDoor("antique_tdoor_0", 0, Material.ROCK);
		antique_tdoor_0w = new SMTrapDoor("antique_tdoor_0w", 0, Material.ROCK);
		antique_tdoor_b = new SMTrapDoor("antique_tdoor_b", 0, Material.ROCK);
		flagstone = new AntiqueBrick("flagstone", 1.25F, 1024F, 0, 0);
		flagstone_stairs = new AntiqueStairs("flagstone_stairs", flagstone.getDefaultState());
		flagstone_slab = new AntiqueSlab("flagstone_slab");
		flagstone_color = new AntiqueBrick("flagstone_color", 1.25F, 1024F, 0, 0);
		flagstone_color_stairs = new AntiqueStairs("flagstone_color_stairs", flagstone_color.getDefaultState());
		flagstone_color_slab = new AntiqueSlab("flagstone_color_slab");

		old_brick = new AntiqueBrick("old_brick", 1.25F, 1024F, 0, 0);
		old_brick_stairs = new AntiqueStairs("old_brick_stairs", old_brick.getDefaultState());
		old_brick_slab = new AntiqueSlab("old_brick_slab");
		old_brick_r = new AntiqueBrick("old_brick_r", 1.25F, 1024F, 0, 0);
		old_brick_r_stairs = new AntiqueStairs("old_brick_r_stairs", old_brick_r.getDefaultState());
		old_brick_r_slab = new AntiqueSlab("old_brick_r_slab");
		old_brick_g = new AntiqueBrick("old_brick_g", 1.25F, 1024F, 0, 0);
		old_brick_g_stairs = new AntiqueStairs("old_brick_g_stairs", old_brick_g.getDefaultState());
		old_brick_g_slab = new AntiqueSlab("old_brick_g_slab");
		old_brick_y = new AntiqueBrick("old_brick_y", 1.25F, 1024F, 0, 0);
		old_brick_y_stairs = new AntiqueStairs("old_brick_y_stairs", old_brick_y.getDefaultState());
		old_brick_y_slab = new AntiqueSlab("old_brick_y_slab");
		old_brick_l = new AntiqueBrick("old_brick_l", 1.25F, 1024F, 0, 0);
		old_brick_l_stairs = new AntiqueStairs("old_brick_l_stairs", old_brick_l.getDefaultState());
		old_brick_l_slab = new AntiqueSlab("old_brick_l_slab");
		old_brick_b = new AntiqueBrick("old_brick_b", 1.25F, 1024F, 0, 0);
		old_brick_b_stairs = new AntiqueStairs("old_brick_b_stairs", old_brick_b.getDefaultState());
		old_brick_b_slab = new AntiqueSlab("old_brick_b_slab");
		old_brick_s = new AntiqueBrick("old_brick_s", 1.25F, 1024F, 0, 0);
		old_brick_s_stairs = new AntiqueStairs("old_brick_s_stairs", old_brick_s.getDefaultState());
		old_brick_s_slab = new AntiqueSlab("old_brick_s_slab");

		antique_window_white = new SMTrapDoor("antique_window_white", 0, Material.WOOD);
		antique_window_brown = new SMTrapDoor("antique_window_brown", 0, Material.WOOD);
		antique_window_brown2 = new SMTrapDoor("antique_window_brown2", 0, Material.WOOD);

		// ドア
		black_moderndoor = new SMDoor("black_moderndoor", 0, Material.WOOD, SoundType.METAL);
		brown_2paneldoor = new SMDoor("brown_2paneldoor", 1, Material.WOOD, SoundType.WOOD);
		brown_5paneldoor = new SMDoor("brown_5paneldoor", 2, Material.WOOD, SoundType.WOOD);
		brown_elegantdoor = new SMDoor("brown_elegantdoor", 3, Material.WOOD, SoundType.WOOD);
		brown_arch_door = new SMDoor("brown_arch_door", 4, Material.WOOD, SoundType.WOOD);
		brown_arch_plantdoor = new SMDoor("brown_arch_plantdoor", 5, Material.WOOD, SoundType.WOOD);
		woodgold_3 = new SMDoor("woodgold_3", 6, Material.WOOD, SoundType.WOOD);
		whitewoodgold_3 = new SMDoor("whitewoodgold_3", 7, Material.WOOD, SoundType.WOOD);


		// 作物
		sannyflower_plant = new MagiaFlower("sannyflower_plant", 0);
		moonblossom_plant = new MagiaFlower("moonblossom_plant", 1);
		dm_plant = new MagiaFlower("drizzly_mysotis_plant", 2);
		sugarbell_plant = new SweetCrops_STAGE4("sugarbell_plant", 0, 1, 6.5f);
		clerodendrum = new SweetCrops_STAGE4("clerodendrum", 7, 1, 6.5f);
		strawberry_plant = new SweetCrops_STAGE4("strawberry_plant", 1, 0, 7.2f);
		sweetpotato_plant = new SweetCrops_STAGE4("sweetpotato_plant", 2, 0, 7.5f);
		j_radish_plant = new SweetCrops_STAGE4("j_radish_plant", 3, 0, 7.2f);
		lettuce_plant = new SweetCrops_STAGE4("lettuce_plant", 4, 0, 6.8f);
		cabbage_plant = new SweetCrops_STAGE4("cabbage_plant", 5, 0, 6.8f);
		glowflower_plant = new SweetCrops_STAGE4("glowflower_plant", 6, 1, 6.8f);
		cotton_plant = new SweetCrops_STAGE4("cotton_plant", 8, 1, 6.8f);
		blueberry_plant = new SweetCrops_STAGE5("blueberry_plant", 0, 1, 7.5f);
		sticky_stuff_plant = new SweetCrops_STAGE5("sticky_stuff_plant", 4, 1, 5.0f);
		rice_plant = new SweetCrops_STAGE6("rice_plant", 1, 0, 6.0f);
		soybean_plant = new SweetCrops_STAGE6("soybean_plant",2, 0, 7.1f);
		azuki_plant = new SweetCrops_STAGE6("azuki_plant",3, 0, 6.7f);
		whitenet_plant = new BlockWhitenet("whitenet_plant", -1, -1, 5.3f);
		corn_plant = new SweetCrops_Tall("corn_plant", 0, 1, 7.8f);
		tomato_plant = new SweetCrops_Tall("tomato_plant", 1, 1, 7.5f);
		egg_plant = new SweetCrops_Tall("egg_plant", 2, 1, 6.4f);
		fire_nasturtium_plant = new SweetCrops_STAGE4("fire_nasturtium_plant", 9, 1, 8.0f);
		olive_plant = new SweetCrops_STAGE5("olive_plant", 1, 1, 5.6f);
		vannila_plant = new SweetCrops_STAGE5("vannila_plant",2 ,0, 6.3f);
		onion_plant = new SweetCrops_STAGE5("onion_plant", 3, 0, 5.6f);
		raspberry_plant = new SweetCrops_STAGE6("raspberry_plant",0, 1, 6.0f);
		chestnut_plant = new BlockChestnut("chestnut_plant", 0);
		coconut_plant = new BlockChestnut("coconut_plant", 1);
		banana_plant = new BlockChestnut("banana_plant", 2);
		magiclight = new MagicLight("magiclight", 0, noTabList);
		twilightlight = new MagicLight("twilightlight", 1, blockList);
		spawn_stone = new SpawnStone("spawn_stone", 0);
		smspaner = new SpawnStone("smspaner", 1);
		sturdust_crystal_bot = new StardustCrystal("sturdust_crystal_bot", 0, blockList);
		sturdust_crystal_top = new StardustCrystal("sturdust_crystal_top", 1, noTabList);

		pole_down = new BlockPole("pole_down", blockList, 0);
		pole = new BlockPole("pole", noTabList, 1);
		lantern = new BlockPole("lantern", noTabList, 2);
		lantern_side1 = new BlockLantern("lantern_side1", 0, blockList);
		lantern_side2 = new BlockLantern("lantern_side2", 1, noTabList);

		cafeboard = new BlockCafeBoard("cafeboard");
		kanban_top = new BlockKandan("kanban_top", noTabList);
		kanban_bot = new BlockKandan("kanban_bot", blockList);
		smchair = new SMChair("smchair", 0);
		antique_back_chair = new SMChair("antique_back_chair", 1);
		woodbox = new ChestnutSlab("woodbox");
		smtable = new SMTable("smtable");
		smtable_lace = new SMTable("smtable_lace");

		moden_rack = new BlockModenRack("moden_rack", 0);
		moden_rack_brown = new BlockModenRack("moden_rack_brown", 0);
		moden_wallrack = new BlockModenRack("moden_wallrack", 1);
		plate = new BlockModenRack("plate", 2);
		moden_stair = new BlockModenStair("moden_stair");
		magicbook = new MagicBook("magicbook");

		antique_brick_pot_r = new PlantPot("antique_brick_pot_r", SoundType.STONE, 0);
		orange_planks_pot = new PlantPot("orange_planks_pot", SoundType.WOOD, 1);
		orange_planks_pot_w = new PlantPot("orange_planks_pot_w", SoundType.WOOD, 2);
		compost_drit = new PlantPot("compost_drit", SoundType.GROUND, 3);

		white_ironfence = new IronFence("white_ironfence");

		// 裏データ
		sample = new MagicDirt("sample");

	}

	//登録したブロックをクリエタブにセット
	public static void register() {

		for (Block block : blockList) {
			registerBlock(block, 0);
		}

		for (Block block : noTabList) {
			registerBlock(block, 1);
		}
	}

	public static void registerBlock(Block block, int data) {

        ForgeRegistries.BLOCKS.register(block);
        if (data == 0) { block.setCreativeTab(SweetMagicCore.SMTab); }
        ItemBlock item = getItemBlock(block);
        item.setRegistryName(block.getRegistryName());
        ForgeRegistries.ITEMS.register(item);
        if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
        	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }
    }

	// ItemBlockの取得
	public static ItemBlock getItemBlock (Block block) {

		ItemBlock item = null;

		// 燃焼時間の取得
		if (block instanceof ISmeltItemBlock) {
			ISmeltItemBlock sm = (ISmeltItemBlock) block;
			item = new SMBlockItem(block, sm.getSmeltTime());
		}

		// それ以外
		else {
			item = new ItemBlock(block);
		}

		return item;
	}
}
