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
import sweetmagic.api.iblock.ISlabItemBlock;
import sweetmagic.api.iblock.ISmeltItemBlock;
import sweetmagic.init.block.blocks.AetherCover;
import sweetmagic.init.block.blocks.AntiqueBrick;
import sweetmagic.init.block.blocks.AntiqueLantern;
import sweetmagic.init.block.blocks.AntiqueSlab;
import sweetmagic.init.block.blocks.AntiqueStairs;
import sweetmagic.init.block.blocks.BlockAwningTent;
import sweetmagic.init.block.blocks.BlockBangingBanner;
import sweetmagic.init.block.blocks.BlockBlinder;
import sweetmagic.init.block.blocks.BlockBreadbasket;
import sweetmagic.init.block.blocks.BlockCafeBoard;
import sweetmagic.init.block.blocks.BlockCafeKitchen;
import sweetmagic.init.block.blocks.BlockCandle;
import sweetmagic.init.block.blocks.BlockCeilingLight;
import sweetmagic.init.block.blocks.BlockCounterTable;
import sweetmagic.init.block.blocks.BlockFaceLanp;
import sweetmagic.init.block.blocks.BlockFaceWood;
import sweetmagic.init.block.blocks.BlockFenceVertical;
import sweetmagic.init.block.blocks.BlockFermenter;
import sweetmagic.init.block.blocks.BlockFlourMill;
import sweetmagic.init.block.blocks.BlockFluorescentLight;
import sweetmagic.init.block.blocks.BlockFreezer;
import sweetmagic.init.block.blocks.BlockFryPan;
import sweetmagic.init.block.blocks.BlockGlassCup;
import sweetmagic.init.block.blocks.BlockIronChain;
import sweetmagic.init.block.blocks.BlockJuiceMaker;
import sweetmagic.init.block.blocks.BlockKanban;
import sweetmagic.init.block.blocks.BlockLanp;
import sweetmagic.init.block.blocks.BlockLantern;
import sweetmagic.init.block.blocks.BlockLight;
import sweetmagic.init.block.blocks.BlockMagiaFluxCore;
import sweetmagic.init.block.blocks.BlockMagiaStorage;
import sweetmagic.init.block.blocks.BlockModenLanp;
import sweetmagic.init.block.blocks.BlockModenRack;
import sweetmagic.init.block.blocks.BlockModenStair;
import sweetmagic.init.block.blocks.BlockModenWallRack;
import sweetmagic.init.block.blocks.BlockNotePC;
import sweetmagic.init.block.blocks.BlockOven;
import sweetmagic.init.block.blocks.BlockParallelInterfere;
import sweetmagic.init.block.blocks.BlockParasolPole;
import sweetmagic.init.block.blocks.BlockPillarBrick;
import sweetmagic.init.block.blocks.BlockPillarStone;
import sweetmagic.init.block.blocks.BlockPlant;
import sweetmagic.init.block.blocks.BlockPole;
import sweetmagic.init.block.blocks.BlockPot;
import sweetmagic.init.block.blocks.BlockRattanBasket;
import sweetmagic.init.block.blocks.BlockRegister;
import sweetmagic.init.block.blocks.BlockSMFence;
import sweetmagic.init.block.blocks.BlockSMOre;
import sweetmagic.init.block.blocks.BlockShowCase;
import sweetmagic.init.block.blocks.BlockSink;
import sweetmagic.init.block.blocks.BlockSofa;
import sweetmagic.init.block.blocks.BlockStendGlass;
import sweetmagic.init.block.blocks.BlockStove;
import sweetmagic.init.block.blocks.BlockTableLanp;
import sweetmagic.init.block.blocks.BlockVerticalHalf;
import sweetmagic.init.block.blocks.BlockWallTowel;
import sweetmagic.init.block.blocks.BlockWoodChest;
import sweetmagic.init.block.blocks.ChestnutPlank;
import sweetmagic.init.block.blocks.ChestnutStairs;
import sweetmagic.init.block.blocks.ChoppingBoard;
import sweetmagic.init.block.blocks.CrystalCore;
import sweetmagic.init.block.blocks.FacePlanks;
import sweetmagic.init.block.blocks.FencePole;
import sweetmagic.init.block.blocks.FigurineStand;
import sweetmagic.init.block.blocks.FlowerBuscket;
import sweetmagic.init.block.blocks.FruitLeaves;
import sweetmagic.init.block.blocks.GoldCrest;
import sweetmagic.init.block.blocks.IronFence;
import sweetmagic.init.block.blocks.MagicBook;
import sweetmagic.init.block.blocks.ObMagia;
import sweetmagic.init.block.blocks.PlantPot;
import sweetmagic.init.block.blocks.Planter;
import sweetmagic.init.block.blocks.SMChair;
import sweetmagic.init.block.blocks.SMDoor;
import sweetmagic.init.block.blocks.SMGlass;
import sweetmagic.init.block.blocks.SMGlassAlpha;
import sweetmagic.init.block.blocks.SMGlassPane;
import sweetmagic.init.block.blocks.SMGlassPaneVertical;
import sweetmagic.init.block.blocks.SMIron;
import sweetmagic.init.block.blocks.SMLeaves;
import sweetmagic.init.block.blocks.SMLog;
import sweetmagic.init.block.blocks.SMPlate;
import sweetmagic.init.block.blocks.SMSapling;
import sweetmagic.init.block.blocks.SMTable;
import sweetmagic.init.block.blocks.SMTableDot;
import sweetmagic.init.block.blocks.SMTrapDoor;
import sweetmagic.init.block.blocks.SMWoodChair;
import sweetmagic.init.block.blocks.SpawnStone;
import sweetmagic.init.block.blocks.StardustCrystal;
import sweetmagic.init.block.blocks.TrapBlock;
import sweetmagic.init.block.blocks.WandPedal;
import sweetmagic.init.block.blocks.WarpBlock;
import sweetmagic.init.block.blocks.WoodPaneGlass;
import sweetmagic.init.block.blocks.items.SMBlockItem;
import sweetmagic.init.block.blocks.items.SMBlockSlab;
import sweetmagic.init.block.crop.BlockAlstroemeria;
import sweetmagic.init.block.crop.BlockCornFlower;
import sweetmagic.init.block.crop.BlockLierRose;
import sweetmagic.init.block.crop.BlockWhitenet;
import sweetmagic.init.block.crop.MagiaFlower;
import sweetmagic.init.block.crop.SweetCrops_STAGE3;
import sweetmagic.init.block.crop.SweetCrops_STAGE4;
import sweetmagic.init.block.crop.SweetCrops_STAGE5;
import sweetmagic.init.block.crop.SweetCrops_STAGE6;
import sweetmagic.init.block.crop.SweetCrops_Tall;
import sweetmagic.init.block.magic.AetherFurnace;
import sweetmagic.init.block.magic.AetherHopper;
import sweetmagic.init.block.magic.AetherLanp;
import sweetmagic.init.block.magic.CleroLanp;
import sweetmagic.init.block.magic.GravityChest;
import sweetmagic.init.block.magic.MFChange;
import sweetmagic.init.block.magic.MFFisher;
import sweetmagic.init.block.magic.MFFurnace;
import sweetmagic.init.block.magic.MFPot;
import sweetmagic.init.block.magic.MFTable;
import sweetmagic.init.block.magic.MFTank;
import sweetmagic.init.block.magic.MagiaLight;
import sweetmagic.init.block.magic.MagicBarrier;
import sweetmagic.init.block.magic.MagicLight;
import sweetmagic.init.block.magic.PedalCreate;
import sweetmagic.init.block.magic.ToolRepairBlock;

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

	// プリズム原木
	public static Block prism_log, prism_leaves, prism_sapling;
	public static Block prism_planks, prism_slab, prism_stairs, prism_plate;

	// バナナ原木
	public static Block banana_leaves, banana_sapling;

	// エストール原木
	public static Block estor_log, estor_leaves, estor_sapling;
	public static Block estor_planks, estor_slab, estor_stairs, estor_plate;

	// 桃原木
	public static Block peach_log, peach_leaves, peach_sapling;
	public static Block peach_planks, peach_slab, peach_stairs, peach_plate;

	// マギアウッド原木
	public static Block magiawood_log, magiawood_leaves, magiawood_sapling;
	public static Block magiawood_planks, magiawood_slab, magiawood_stairs, magiawood_plate;

	// ガラス
	public static Block sugarglass, shading_sugarglass, frosted_glass_line, frosted_glass, prismglass, shading_prismglass;

	// 板ガラス
	public static Block sugarglass_pane, shading_sugarglass_pane, frosted_glass_line_pane, frosted_glass_pane, prismglass_pane;
	public static Block green4panel_glass_pane, brown4panel_glass_pane, lightbrown4panel_glass_pane, darkbrown4panel_glass_pane, ami_glass_pane;
	public static Block gorgeous_glass_pane, gorgeous_glass_w_pane;

	// 4パネガラス
	public static Block green4panel_glass, brown4panel_glass, lightbrown4panel_glass, darkbrown4panel_glass, ami_glass;
	public static Block gorgeous_glass, gorgeous_glass_w;

	// 料理器具
	public static Block flourmill_off;
	public static Block mixer_r, mixer_s, mixer_y, mixer_o, mixer_l, mixer_p;
	public static Block oven;
	public static Block toaster_s, toaster_r, toaster_y, toaster_o, toaster_l, toaster_p;
	public static Block stove_off;
	public static Block pot_off, pot_yellow_off, pot_red_off, pot_skyblue_off, pot_lime_off, pot_pink_off;
	public static Block single_pot_s, single_pot_b, single_pot_o, single_pot_p, single_pot_r, single_pot_y, single_pot_l, single_pot_i;
	public static Block frypan_off, frypan_red_off, frypan_purple_off, frypan_skyblue_off, frypan_yellow_off, frypan_lime_off, frypan_pink_off;
	public static Block juicemaker_off, coffee_maker_s, coffee_maker_r, coffee_maker_o, coffee_maker_p, coffee_maker_l, coffee_maker_i;
	public static Block freezer_top, freezer_bottom;
	public static Block matured_bottle;
	public static Block chopping_board, chopping_board_w, chopping_board_b;

	// 花
	public static Block cornflower, lily_valley, twilight_alstroemeria, lier_rose, white_clover, foxtail_grass;
	public static Block clerodendrum, clerolanp, cosmos, blackrose, snowdrop, turkey_balloonflower;
	public static Block iberis_umbellata, ultramarine_rose, solid_star, zinnia, hydrangea, carnation_crayola;
	public static Block campanula, primula_polyansa, christmas_rose, portulaca;
	public static Block pansy_blue, pansy_yellowmazenta, surfinia, marigold, christmarose_ericsmithii;

	// 花瓶
	public static Block flower_vese_o, flower_vese_b, flower_vese_p, flower_vese_s, flower_vese_w, flower_vese_y;

	// バスケット
	public static Block iberis_umbellata_basket, campanula_basket, primula_polyansa_basket, christmas_rose_basket, portulaca_basket;
	public static Block surfinia_basket, pansy_blue_basket, pansy_yellowmazenta_basket, marigold_basket;

	// 鉱石
	public static Block ac_ore, alt_block, cosmos_light_block, cosmic_crystal_ore;
	public static Block magiaflux_block, aethercrystal_block, divinecrystal_block, purecrystal_block;
	public static Block aethercrystal_block_alpha, divinecrystal_block_alpha, cosmos_light_block_alpha;

	// MFブロック
	public static Block mfchanger, advanced_mfchanger;
	public static Block mftank, advanced_mftank, mm_tank, creative_tank;
	public static Block mftable, advanced_mftable, mm_table;
	public static Block obmagia_top, obmagia_bottom;
	public static Block pedestal_creat, altar_creat, altar_creation_star;
	public static Block mffisher, flyishforer, mfsqueezer, mfgeneration, mfharvester;
	public static Block mffurnace_on, mffurnace_off, advanced_mffurnace_on, advanced_mffurnace_off;
	public static Block aether_furnace_top, aether_furnace_bottom, advanced_aether_furnace_top, advanced_aether_furnace_bottom;
	public static Block parallel_interfere, stardust_wish;
	public static Block mfpot, twilightalstroemeria_pot, turkey_balloonflower_pot, snowdrop_pot, ultramarine_rose_pot;
	public static Block solid_star_pot, zinnia_pot, hydrangea_pot, carnation_crayola_pot, christmarose_ericsmithii_pot;
	public static Block tool_repair, magia_rewrite, gravity_chest, aether_hopper, magia_successor;
	public static Block aether_lanp, arcane_table, magia_accelerator, workbench_storage, aether_enchanttable, magia_lantern, magia_light;
	public static Block magia_reincarnation;

	public static Block aether_cover, aether_cover_rock;

	// 光源
	public static Block magiclight, glow_light, antique_candle, gorgeous_lamp, glow_lamp, magic_circle, twilightlight;
	public static Block modenlanp, walllamp, walllamp_long;
	public static Block stendglass_lamp_g_off, stendglass_lamp_g_on, stendglass_lamp_off, stendglass_lamp_on;
	public static Block table_modernlamp_off, table_modernlamp_on, table_lamp, table_lantern;
	public static Block ceiling_light, fluorescent_light, ceiling_spotlight;

	// レンガ
	public static Block antique_brick_0, antique_brick_1, antique_brick_2;
	public static Block antique_brick_0w, antique_brick_1w, antique_brick_2w;
	public static Block antique_brick_b;
	public static Block antique_brick_0l, antique_brick_1l, antique_brick_2l;
	public static Block antique_brick_0g, antique_brick_1g, antique_brick_2g;
	public static Block antique_brick_stairs, antique_brick_slab;
	public static Block antique_brick_stairs_w, antique_brick_slab_w;
	public static Block antique_brick_stairs_b, antique_brick_slab_b;
	public static Block antique_brick_stairs_l, antique_brick_slab_l;
	public static Block antique_brick_stairs_g, antique_brick_slab_g;
	public static Block flagstone, flagstone_stairs, flagstone_slab;
	public static Block flagstone_color, flagstone_color_stairs, flagstone_color_slab;
	public static Block old_brick, old_brick_stairs, old_brick_slab;
	public static Block old_brick_r, old_brick_r_stairs, old_brick_r_slab;
	public static Block old_brick_g, old_brick_g_stairs, old_brick_g_slab;
	public static Block old_brick_y, old_brick_y_stairs, old_brick_y_slab;
	public static Block old_brick_l, old_brick_l_stairs, old_brick_l_slab;
	public static Block old_brick_b, old_brick_b_stairs, old_brick_b_slab;
	public static Block old_brick_s, old_brick_s_stairs, old_brick_s_slab;
	public static Block longtile_brick_o, longtile_brick_o_stairs, longtile_brick_o_slab;
	public static Block longtile_brick_p, longtile_brick_p_stairs, longtile_brick_p_slab;
	public static Block longtile_brick_r, longtile_brick_r_stairs, longtile_brick_r_slab;
	public static Block longtile_brick_y, longtile_brick_y_stairs, longtile_brick_y_slab;
	public static Block longtile_brick_b, longtile_brick_b_stairs, longtile_brick_b_slab;
	public static Block longtile_brick_g, longtile_brick_g_stairs, longtile_brick_g_slab;
	public static Block longtile_brick, longtile_brick_stairs, longtile_brick_slab;
	public static Block longtile_brick_l, longtile_brick_l_stairs, longtile_brick_l_slab;
	public static Block longtile_brick_w, longtile_brick_w_stairs, longtile_brick_w_slab;
	public static Block whiteline_brick, whiteline_brick_stairs, whiteline_brick_slab;
	public static Block whiteline_brick_y, whiteline_brick_y_stairs, whiteline_brick_y_slab;
	public static Block whiteline_brick_b, whiteline_brick_b_stairs, whiteline_brick_b_slab;
	public static Block pasteltile_brick_y, pasteltile_brick_y_stairs, pasteltile_brick_y_slab;
	public static Block pasteltile_brick_r, pasteltile_brick_r_stairs, pasteltile_brick_r_slab;
	public static Block pasteltile_brick_b, pasteltile_brick_b_stairs, pasteltile_brick_b_slab;
	public static Block pasteltile_brick_lg, pasteltile_brick_lg_stairs, pasteltile_brick_lg_slab;
	public static Block pasteltile_brick_br, pasteltile_brick_br_stairs, pasteltile_brick_br_slab;
	public static Block pasteltile_brick_gr, pasteltile_brick_gr_stairs, pasteltile_brick_gr_slab;
	public static Block pasteltile_brick_bl, pasteltile_brick_bl_stairs, pasteltile_brick_bl_slab;

	// レンガのトラップドア
	public static Block antique_tdoor_0, antique_tdoor_0w, antique_tdoor_b, antique_tdoor_l, antique_tdoor_g;
	public static Block old_tdoor, old_tdoor_r, old_tdoor_g , old_tdoor_y, old_tdoor_l, old_tdoor_b, old_tdoor_s;
	public static Block longtile_brick_o_tdoor, longtile_brick_p_tdoor, longtile_brick_r_tdoor, longtile_brick_y_tdoor, longtile_brick_b_tdoor;
	public static Block longtile_brick_g_tdoor, longtile_brick_tdoor, longtile_brick_l_tdoor, longtile_brick_w_tdoor;
	public static Block whiteline_brick_tdoor, whiteline_brick_y_tdoor, whiteline_brick_b_tdoor;
	public static Block pasteltile_brick_y_tdoor, pasteltile_brick_r_tdoor, pasteltile_brick_b_tdoor, pasteltile_brick_lg_tdoor, pasteltile_brick_br_tdoor, pasteltile_brick_gr_tdoor, pasteltile_brick_bl_tdoor;

	public static Block antique_brick_pillar, antique_brick_pillar_l, whiteline_brick_pillar, whiteline_brick_pillar_y, pasteltile_brick_pillar_y;

	public static Block pillar_stone, pillar_stone_base, pillar_stone_w, pillar_stone_w_base;
	public static Block magic_square;

	// 窓
	public static Block antique_window_white, antique_window_brown, antique_window_brown2, antique_window_green;

	// ドア
	public static Block black_moderndoor, brown_2paneldoor, brown_5paneldoor, brown_elegantdoor, brown_arch_door, brown_arch_plantdoor;
	public static Block woodgold_3, whitewoodgold_3, simple_door_1, simple_door_2;

	// トラップドア
	public static Block white_woodtrapdoor;

	// 作物
	public static Block sannyflower_plant, moonblossom_plant, dm_plant, sugarbell_plant;
	public static Block blueberry_plant, sticky_stuff_plant, fire_nasturtium_plant, strawberry_plant;
	public static Block vannila_plant, olive_plant, rice_plant, soybean_plant;
	public static Block whitenet_plant, corn_plant, sweetpotato_plant;
	public static Block tomato_plant, egg_plant,  j_radish_plant, lettuce_plant, cabbage_plant;
	public static Block azuki_plant, onion_plant, raspberry_plant, glowflower_plant, cotton_plant;
	public static Block banana_plant, coffee_plant, spinach_plant;
	public static Block quartz_plant, pineapple_plant, greenpepper_plant;

	// 街頭
	public static Block pole_down, pole, lantern, lantern_side1, lantern_side2;

	public static Block hanging_banner_b, hanging_banner_r, hanging_banner_o, hanging_banner_p, hanging_banner_s, hanging_banner_l;

	// 家具
	public static Block cafeboard, cafeboard_w, kanban_top, kanban_bot;
	public static Block smchair, antique_back_chair, balcony_chair, cafe_chair, counter_chair, moden_chair, moden_chair_b, moden_chair_l;
	public static Block woodbench, woodbench_b;
	public static Block sofa_r, sofa_b, sofa_w;
	public static Block blinder, blinder_o, blinder_s, blinder_y, blinder_g, blinder_p, blinder_b, blinder_w;

	// PC
	public static Block note_pc, note_pc_r, note_pc_p, note_pc_b, note_pc_o, note_pc_l, note_pc_i;
	public static Block sm_display;

	public static Block smtable, smtable_lace, smtable_dot, balcony_table, cafe_table, stainless_steel_table, moden_table, moden_table_b, moden_table_l;
	public static Block moden_rack, moden_rack_brown;
	public static Block iron_shelf;
	public static Block moden_wallrack, moden_wallrack_l, moden_wallrack_b, iron_wallrack;
	public static Block moden_shelf_y, moden_shelf_s, moden_shelf_r, moden_shelf_o, moden_shelf_p, moden_shelf_l, moden_shelf_i;
	public static Block bottle_rack, bottle_rack_b;
	public static Block ceiling_shelf, range_hood_rack;
	public static Block fruit_crate, fruit_crate_coconut, fruit_crate_orange, fruit_crate_smwood, fruit_crate_wood_black;
	public static Block fruit_crate_box, fruit_crate_box_coconut, fruit_crate_box_orange, fruit_crate_box_smwood, fruit_crate_box_wood_black;
	public static Block wooden_tool_box;
	public static Block moden_stair, moden_stair_b, moden_stair_l, moden_stair_coconut, moden_stair_chestnut, moden_stair_lemon;
	public static Block plate, wood_plate, iron_plate, plate_shaped;
	public static Block tray, tray_wood;
	public static Block magicbook, magicbook_b;
	public static Block bread_wood_tray, bread_basket, showcase, showcase_stand;
	public static Block tong_stand;
	public static Block antique_lantern, iron_chain;
	public static Block glass_cup;
	public static Block bread_baskets, hard_bread_basket, tinplate_bucket;
	public static Block wall_towel, wall_none;
	public static Block register_s, register_r, register_y, register_o, register_p, register_w, register_l, register_m, register_i;

	public static Block rattan_chair_y, rattan_chair_b, rattan_chair_d;
	public static Block rattan_basket_y, rattan_basket_b, rattan_basket_d;

	public static Block prism_woodchest, estor_woodchest;
	public static Block rattan_chest_y, rattan_chest_b;
	public static Block treasure_chest;
	public static Block magia_storage_1, magia_storage_2, magia_storage_3, magia_storage_4, magia_storage_5;
	public static Block shoe_box, shoe_box_r, shoe_box_g;
	public static Block shoe_box_piling, shoe_box_piling_r, shoe_box_piling_g;
	public static Block post_w, post_r;
	public static Block post_wood_orange, post_wood_orange_w, post_wood_chestnut, post_wood_smstone;
	public static Block wallboard, wallboard_black, corkboard, shopboard;
	public static Block cafe_kitchen_table, cafe_kitchen_sink, cafe_wallrack;
	public static Block menu_list, menu_list_w;
	public static Block path_tree_orange, path_tree_lemon;
	public static Block awning_tent_b, awning_tent_o, awning_tent_p, awning_tent_r, awning_tent_s, awning_tent_w, awning_tent_bl, awning_tent_or, awning_tent_g, parasol_pole, parasol_pole_base;
	public static Block fence_pole_estor, fence_pole_estor_base, fence_pole_prism, fence_pole_prism_base, fence_pole_pearch, fence_pole_pearch_base;
	public static Block log_fence_estor, log_fence_vertical_estor, log_fence_normal_estor, log_fence_slanted_estor;
	public static Block log_fence_prism, log_fence_vertical_prism, log_fence_normal_prism, log_fence_slanted_prism;
	public static Block log_fence_peach, log_fence_vertical_peach, log_fence_normal_peach, log_fence_slanted_peach;
	public static Block stone_fence_whiteline, stone_fence_whiteline_b, stone_fence_whiteline_y;
	public static Block stone_fence_antique, stone_fence_antique_w, stone_fence_antique_l, stone_fence_antique_g;
	public static Block stone_fence_pastel_y, stone_fence_pastel_r, stone_fence_pastel_b, stone_fence_pastel_lg, stone_fence_pastel_gr;
	public static Block brick_fence_antique, brick_fence_antique_w, brick_fence_antique_b, brick_fence_antique_g, brick_fence_whiteline_y, brick_fence_whiteline_b;

	// 草
	public static Block goldcrest;

	// 植え込み
	public static Block orange_planting, chestnut_planting, estor_planting, peach_planting;

	public static Block white_ironfence, black_ironfence, fance_gothic_white, fance_gothic_black;
	public static Block antique_brick_pot_r, orange_planks_pot, orange_planks_pot_w, estor_planks_pot, compost_drit;
	public static Block longtile_brick_pot_o, longtile_brick_pot_l, whiteline_brick_pot;
	public static Block antique_brick_pot_w, antique_brick_pot_l, antique_brick_pot_g, whiteline_brick_pot_y, whiteline_brick_pot_b;
	public static Block pasteltile_brick_pot_y, pasteltile_brick_pot_r, pasteltile_brick_pot_b, pasteltile_brick_pot_lg, pasteltile_brick_pot_bl, pasteltile_brick_pot_gr;
	public static Block antique_brick_planter_r, chestnut_planks_planter,orange_planks_planter, orange_planks_planter_w;
	public static Block antique_brick_planter_l, whiteline_brick_planter_y, whiteline_brick_planter_b;

	public static Block counter_table_white_brick_y, counter_table_white_brick, counter_table_white_brick_b, counter_table_orange_planks, counter_table_modan;
	public static Block counter_table_stove_white_brick_y, counter_table_stove_white_brick, counter_table_stove_white_brick_b;
	public static Block counter_table_stove_orange_planks, counter_table_stove_modan;
	public static Block counter_table_sink_white_brick_y, counter_table_sink_white_brick, counter_table_sink_white_brick_b;
	public static Block counter_table_sink_orange_planks, counter_table_sink_modan;
	public static Block counter_table_oven_white_brick_y, counter_table_oven_white_brick, counter_table_oven_white_brick_b, counter_table_oven_orange_planks, counter_table_oven_modan;
	public static Block counter_table_chest_white_brick_y, counter_table_chest_white_brick, counter_table_chest_white_brick_b, counter_table_chest_orange_planks, counter_table_chest_modan;
	public static Block counter_table_half_white_brick_y, counter_table_half_white_brick, counter_table_half_white_brick_b, counter_table_half_orange_planks, counter_table_half_modan;
	public static Block ventilation_fan, ventilation_fan_light;

	public static Block woodbox;

	public static Block spawn_stone, smspaner, sturdust_crystal_bot, sturdust_crystal_top, magicbarrier_off, magicbarrier_on;
	public static Block crystal_core, magic_core;
	public static Block magiaflux_core;

	public static Block figurine_wm, figurine_wd, figurine_if, figurine_bs, figurine_sa;
	public static Block figurine_zh, figurine_af, figurine_rk, figurine_ec, figurine_zk;

	public static Block poison_block, warp_block, warp_block_on, wand_pedal;

	// ブロックをまず登録
	public static Block sample;

    public static List<Block> blockList = new ArrayList();
    public static List<Block> noTabList = new ArrayList();
    public static List<Block> magicList = new ArrayList();
    public static List<Block> furniList = new ArrayList();

    // JEIリスト
    public static List<Block> pedalList = new ArrayList();
    public static List<Block> mftableList = new ArrayList();
    public static List<Block> mftankList = new ArrayList();
    public static List<Block> ovenList = new ArrayList();
    public static List<Block> juiceList = new ArrayList();
    public static List<Block> millList = new ArrayList();
    public static List<Block> potList = new ArrayList();
    public static List<Block> frypanList = new ArrayList();

	// 登録したブロックにデータを与える
	public static void init() {

		// 栗木材
		chestnut_log = new SMLog("chestnut_log");
		chestnut_leaves = new SMLeaves("chestnut_leaves", 0);
		chestnut_sapling = new SMSapling("chestnut_sapling", 2);
		chestnut_planks = new ChestnutPlank("chestnut_planks");
		chestnut_slab = new AntiqueSlab("chestnut_slab", BlockInit.blockList);
		chestnut_stairs = new ChestnutStairs("chestnut_stairs", chestnut_planks.getDefaultState());
		chestnut_plate = new SMPlate("chestnut_plate");

		// レモン木材
		lemon_log = new SMLog("lemon_log");
		lemon_leaves = new FruitLeaves("lemon_leaves", 0);
		lemon_sapling = new SMSapling("lemon_sapling", 1);
		lemon_planks = new ChestnutPlank("lemon_planks");
		lemon_slab = new AntiqueSlab("lemon_slab", BlockInit.blockList);
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
		orange_slab = new AntiqueSlab("orange_slab", BlockInit.blockList);
		orange_huti_slab = new AntiqueSlab("orange_huti_slab", BlockInit.blockList);
		orange_stairs = new ChestnutStairs("orange_stairs", orange_planks.getDefaultState());
		orange_huti_stairs = new ChestnutStairs("orange_huti_stairs", orange_planks_huti3.getDefaultState());
		orange_plate = new SMPlate("orange_plate");

		// ミカン白木材
		orange_planks_w = new ChestnutPlank("orange_planks_w");
		orange_planks_wdamage = new ChestnutPlank("orange_planks_wdamage");
		orange_planks_wmossy = new ChestnutPlank("orange_planks_wmossy");
		orange_planks_w_huti1 = new FacePlanks("orange_planks_w_huti1");
		orange_planks_w_huti3 = new ChestnutPlank("orange_planks_w_huti3");
		orange_wslab = new AntiqueSlab("orange_wslab", BlockInit.blockList);
		orange_whuti_slab = new AntiqueSlab("orange_whuti_slab", BlockInit.blockList);
		orange_wstairs = new ChestnutStairs("orange_wstairs", orange_planks_w.getDefaultState());
		orange_whuti_stairs = new ChestnutStairs("orange_whuti_stairs", orange_planks_w_huti3.getDefaultState());
		orange_wplate = new SMPlate("orange_wplate");
		white_woodtrapdoor = new SMTrapDoor("white_woodtrapdoor", 0, Material.WOOD);

		// ヤシ木材
		coconut_log = new SMLog("coconut_log");
		coconut_leaves = new SMLeaves("coconut_leaves", 1);
		coconut_sapling = new SMSapling("coconut_sapling", 3);
		coconut_planks = new ChestnutPlank("coconut_planks");
		coconut_slab = new AntiqueSlab("coconut_slab", BlockInit.blockList);
		coconut_stairs = new ChestnutStairs("coconut_stairs", coconut_planks.getDefaultState());
		coconut_trapdoor = new SMTrapDoor("coconut_trapdoor", 0, Material.WOOD);
		coconut_plate = new SMPlate("coconut_plate");

		// プリズム
		prism_log = new SMLog("prism_log");
		prism_leaves = new SMLeaves("prism_leaves", 2);
		prism_sapling = new SMSapling("prism_sapling", 4);
		prism_planks = new ChestnutPlank("prism_planks");
		prism_slab = new AntiqueSlab("prism_slab", BlockInit.blockList);
		prism_stairs = new ChestnutStairs("prism_stairs", prism_planks.getDefaultState());
		prism_plate = new SMPlate("prism_plate");

		// バナナ
		banana_leaves = new SMLeaves("banana_leaves", 3);
		banana_sapling = new SMSapling("banana_sapling", 5);

		// エストール
		estor_log = new SMLog("estor_log");
		estor_leaves = new FruitLeaves("estor_leaves", 2);
		estor_sapling = new SMSapling("estor_sapling", 6);
		estor_planks = new ChestnutPlank("estor_planks");
		estor_slab = new AntiqueSlab("estor_slab", BlockInit.blockList);
		estor_stairs = new ChestnutStairs("estor_stairs", estor_planks.getDefaultState());
		estor_plate = new SMPlate("estor_plate");

		// 桃
		peach_log = new SMLog("peach_log");
		peach_leaves = new FruitLeaves("peach_leaves", 3);
		peach_sapling = new SMSapling("peach_sapling", 7);
		peach_planks = new ChestnutPlank("peach_planks");
		peach_slab = new AntiqueSlab("peach_slab", BlockInit.blockList);
		peach_stairs = new ChestnutStairs("peach_stairs", peach_planks.getDefaultState());
		peach_plate = new SMPlate("peach_plate");

		// マギアウッド
		magiawood_log = new SMLog("magiawood_log");
		magiawood_leaves = new SMLeaves("magiawood_leaves", 4);
		magiawood_sapling = new SMSapling("magiawood_sapling", 8);
		magiawood_planks = new ChestnutPlank("magiawood_planks");
		magiawood_slab = new AntiqueSlab("magiawood_slab", BlockInit.blockList);
		magiawood_stairs = new ChestnutStairs("magiawood_stairs", magiawood_planks.getDefaultState());
		magiawood_plate = new SMPlate("magiawood_plate");

		// 料理器具
		flourmill_off = new BlockFlourMill("flourmill_off", 0, true);
		mixer_r = new BlockFlourMill("mixer_r", 1, true);
		mixer_s = new BlockFlourMill("mixer_s", 1, false);
		mixer_y = new BlockFlourMill("mixer_y", 1, false);
		mixer_o = new BlockFlourMill("mixer_o", 1, false);
		mixer_l = new BlockFlourMill("mixer_l", 1, false);
		mixer_p = new BlockFlourMill("mixer_p", 1, false);
		oven = new BlockOven("oven", 0, true);
		toaster_s = new BlockOven("toaster_s", 1, true);
		toaster_r = new BlockOven("toaster_r", 1, false);
		toaster_y = new BlockOven("toaster_y", 1, false);
		toaster_o = new BlockOven("toaster_o", 1, false);
		toaster_l = new BlockOven("toaster_l", 1, false);
		toaster_p = new BlockOven("toaster_p", 1, false);
		stove_off = new BlockStove("stove_off", 0);
		pot_off = new BlockPot("pot_off", 0, true);
		pot_yellow_off = new BlockPot("pot_yellow_off", 0, false);
		pot_red_off = new BlockPot("pot_red_off", 0, false);
		pot_skyblue_off = new BlockPot("pot_skyblue_off", 0, false);
		pot_lime_off = new BlockPot("pot_lime_off", 0, false);
		pot_pink_off = new BlockPot("pot_pink_off", 0, false);
		single_pot_s = new BlockPot("single_pot_s", 1, true);
		single_pot_y = new BlockPot("single_pot_y", 1, false);
		single_pot_r = new BlockPot("single_pot_r", 1, false);
		single_pot_p = new BlockPot("single_pot_p", 1, false);
		single_pot_o = new BlockPot("single_pot_o", 1, false);
		single_pot_b = new BlockPot("single_pot_b", 1, false);
		single_pot_l = new BlockPot("single_pot_l", 1, false);
		single_pot_i = new BlockPot("single_pot_i", 1, false);
		frypan_off = new BlockFryPan("frypan_off", 0, true);
		frypan_red_off = new BlockFryPan("frypan_red_off", 1, true);
		frypan_purple_off = new BlockFryPan("frypan_purple_off", 1, false);
		frypan_skyblue_off = new BlockFryPan("frypan_skyblue_off", 1, false);
		frypan_yellow_off = new BlockFryPan("frypan_yellow_off", 1, false);
		frypan_lime_off = new BlockFryPan("frypan_lime_off", 1, false);
		frypan_pink_off = new BlockFryPan("frypan_pink_off", 1, false);
		juicemaker_off = new BlockJuiceMaker("juicemaker_off", 0, true);
		coffee_maker_s = new BlockJuiceMaker("coffee_maker_s", 1, true);
		coffee_maker_r = new BlockJuiceMaker("coffee_maker_r", 1, false);
		coffee_maker_o = new BlockJuiceMaker("coffee_maker_o", 1, false);
		coffee_maker_p = new BlockJuiceMaker("coffee_maker_p", 1, false);
		coffee_maker_l = new BlockJuiceMaker("coffee_maker_l", 1, false);
		coffee_maker_i = new BlockJuiceMaker("coffee_maker_i", 1, false);
		freezer_top = new BlockFreezer("freezer_top", true, noTabList);
		freezer_bottom = new BlockFreezer("freezer_bottom", false, furniList);
		matured_bottle = new BlockFermenter("matured_bottle");
		chopping_board = new ChoppingBoard("chopping_board");
		chopping_board_w = new ChoppingBoard("chopping_board_w");
		chopping_board_b = new ChoppingBoard("chopping_board_b");
		glass_cup = new BlockGlassCup("glass_cup");
		plate = new BlockModenRack("plate", 2);
		wood_plate = new BlockModenRack("wood_plate", 3);
		iron_plate = new BlockModenRack("iron_plate", 2);
		plate_shaped = new BlockModenRack("plate_shaped", 10);
		tray = new BlockModenRack("tray", 11);
		tray_wood = new BlockModenRack("tray_wood", 11);
		bread_wood_tray = new BlockModenRack("bread_wood_tray", 4);
		bread_basket = new BlockModenRack("bread_basket", 5);
		showcase = new BlockShowCase("showcase");
		showcase_stand = new BlockFaceWood("showcase_stand", SoundType.WOOD, 0);
		tong_stand = new BlockCafeBoard("tong_stand", 1);
		tinplate_bucket = new BlockBreadbasket("tinplate_bucket", 1);
		bread_baskets = new BlockBreadbasket("bread_baskets", 0);
		hard_bread_basket = new BlockBreadbasket("hard_bread_basket", 0);
		wall_towel = new BlockWallTowel("wall_towel", 0, furniList);
		wall_none = new BlockWallTowel("wall_none", 1, noTabList);
		register_s = new BlockRegister("register_s");
		register_r = new BlockRegister("register_r");
		register_y = new BlockRegister("register_y");
		register_o = new BlockRegister("register_o");
		register_p = new BlockRegister("register_p");
		register_w = new BlockRegister("register_w");
		register_m = new BlockRegister("register_m");
		register_i = new BlockRegister("register_i");
		register_l = new BlockRegister("register_l");

		// レンガ
		antique_brick_0 = new AntiqueBrick("antique_brick_0", 1.25F, 1024F, 0, 0);
		antique_brick_1 = new AntiqueBrick("antique_brick_1", 1.25F, 1024F, 0, 0);
		antique_brick_2 = new AntiqueBrick("antique_brick_2", 1.25F, 1024F, 0, 0);
		antique_brick_0w = new AntiqueBrick("antique_brick_0w", 1.25F, 1024F, 0, 0);
		antique_brick_1w = new AntiqueBrick("antique_brick_1w", 1.25F, 1024F, 0, 0);
		antique_brick_2w = new AntiqueBrick("antique_brick_2w", 1.25F, 1024F, 0, 0);
		antique_brick_b = new AntiqueBrick("antique_brick_b", 1.25F, 1024F, 0, 0);
		antique_brick_0l = new AntiqueBrick("antique_brick_0l", 1.25F, 1024F, 0, 0);
		antique_brick_1l = new AntiqueBrick("antique_brick_1l", 1.25F, 1024F, 0, 0);
		antique_brick_2l = new AntiqueBrick("antique_brick_2l", 1.25F, 1024F, 0, 0);
		antique_brick_0g = new AntiqueBrick("antique_brick_0g", 1.25F, 1024F, 0, 0);
		antique_brick_1g = new AntiqueBrick("antique_brick_1g", 1.25F, 1024F, 0, 0);
		antique_brick_2g = new AntiqueBrick("antique_brick_2g", 1.25F, 1024F, 0, 0);
		antique_brick_stairs = new AntiqueStairs("antique_brick_stairs", antique_brick_0.getDefaultState());
		antique_brick_stairs_w = new AntiqueStairs("antique_brick_stairs_w", antique_brick_0w.getDefaultState());
		antique_brick_stairs_b = new AntiqueStairs("antique_brick_stairs_b", antique_brick_b.getDefaultState());
		antique_brick_stairs_l = new AntiqueStairs("antique_brick_stairs_l", antique_brick_0l.getDefaultState());
		antique_brick_stairs_g = new AntiqueStairs("antique_brick_stairs_g", antique_brick_0g.getDefaultState());
		antique_brick_slab = new AntiqueSlab("antique_brick_slab");
		antique_brick_slab_w = new AntiqueSlab("antique_brick_slab_w");
		antique_brick_slab_b = new AntiqueSlab("antique_brick_slab_b");
		antique_brick_slab_l = new AntiqueSlab("antique_brick_slab_l");
		antique_brick_slab_g = new AntiqueSlab("antique_brick_slab_g");
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

		longtile_brick_o = new AntiqueBrick("longtile_brick_o", 1.25F, 1024F, 0, 0);
		longtile_brick_o_stairs = new AntiqueStairs("longtile_brick_o_stairs", longtile_brick_o.getDefaultState());
		longtile_brick_o_slab = new AntiqueSlab("longtile_brick_o_slab");
		longtile_brick_p = new AntiqueBrick("longtile_brick_p", 1.25F, 1024F, 0, 0);
		longtile_brick_p_stairs = new AntiqueStairs("longtile_brick_p_stairs", longtile_brick_p.getDefaultState());
		longtile_brick_p_slab = new AntiqueSlab("longtile_brick_p_slab");
		longtile_brick_r = new AntiqueBrick("longtile_brick_r", 1.25F, 1024F, 0, 0);
		longtile_brick_r_stairs = new AntiqueStairs("longtile_brick_r_stairs", longtile_brick_r.getDefaultState());
		longtile_brick_r_slab = new AntiqueSlab("longtile_brick_r_slab");
		longtile_brick_y = new AntiqueBrick("longtile_brick_y", 1.25F, 1024F, 0, 0);
		longtile_brick_y_stairs = new AntiqueStairs("longtile_brick_y_stairs", longtile_brick_y.getDefaultState());
		longtile_brick_y_slab = new AntiqueSlab("longtile_brick_y_slab");
		longtile_brick_b = new AntiqueBrick("longtile_brick_b", 1.25F, 1024F, 0, 0);
		longtile_brick_b_stairs = new AntiqueStairs("longtile_brick_b_stairs", longtile_brick_b.getDefaultState());
		longtile_brick_b_slab = new AntiqueSlab("longtile_brick_b_slab");
		longtile_brick_g = new AntiqueBrick("longtile_brick_g", 1.25F, 1024F, 0, 0);
		longtile_brick_g_stairs = new AntiqueStairs("longtile_brick_g_stairs", longtile_brick_g.getDefaultState());
		longtile_brick_g_slab = new AntiqueSlab("longtile_brick_g_slab");
		longtile_brick = new AntiqueBrick("longtile_brick", 1.25F, 1024F, 0, 0);
		longtile_brick_stairs = new AntiqueStairs("longtile_brick_stairs", longtile_brick.getDefaultState());
		longtile_brick_slab = new AntiqueSlab("longtile_brick_slab");
		longtile_brick_l = new AntiqueBrick("longtile_brick_l", 1.25F, 1024F, 0, 0);
		longtile_brick_l_stairs = new AntiqueStairs("longtile_brick_l_stairs", longtile_brick_l.getDefaultState());
		longtile_brick_l_slab = new AntiqueSlab("longtile_brick_l_slab");
		longtile_brick_w = new AntiqueBrick("longtile_brick_w", 1.25F, 1024F, 0, 0);
		longtile_brick_w_stairs = new AntiqueStairs("longtile_brick_w_stairs", longtile_brick_w.getDefaultState());
		longtile_brick_w_slab = new AntiqueSlab("longtile_brick_w_slab");

		whiteline_brick = new AntiqueBrick("whiteline_brick", 1.25F, 1024F, 0, 0);
		whiteline_brick_stairs = new AntiqueStairs("whiteline_brick_stairs", whiteline_brick.getDefaultState());
		whiteline_brick_slab = new AntiqueSlab("whiteline_brick_slab");
		whiteline_brick_y = new AntiqueBrick("whiteline_brick_y", 1.25F, 1024F, 0, 0);
		whiteline_brick_y_stairs = new AntiqueStairs("whiteline_brick_y_stairs", whiteline_brick_y.getDefaultState());
		whiteline_brick_y_slab = new AntiqueSlab("whiteline_brick_y_slab");
		whiteline_brick_b = new AntiqueBrick("whiteline_brick_b", 1.25F, 1024F, 0, 0);
		whiteline_brick_b_stairs = new AntiqueStairs("whiteline_brick_b_stairs", whiteline_brick_b.getDefaultState());
		whiteline_brick_b_slab = new AntiqueSlab("whiteline_brick_b_slab");

		pasteltile_brick_y = new AntiqueBrick("pasteltile_brick_y", 1.25F, 1024F, 0, 0);
		pasteltile_brick_y_stairs = new AntiqueStairs("pasteltile_brick_y_stairs", pasteltile_brick_y.getDefaultState());
		pasteltile_brick_y_slab = new AntiqueSlab("pasteltile_brick_y_slab");
		pasteltile_brick_r = new AntiqueBrick("pasteltile_brick_r", 1.25F, 1024F, 0, 0);
		pasteltile_brick_r_stairs = new AntiqueStairs("pasteltile_brick_r_stairs", pasteltile_brick_r.getDefaultState());
		pasteltile_brick_r_slab = new AntiqueSlab("pasteltile_brick_r_slab");
		pasteltile_brick_b = new AntiqueBrick("pasteltile_brick_b", 1.25F, 1024F, 0, 0);
		pasteltile_brick_b_stairs = new AntiqueStairs("pasteltile_brick_b_stairs", pasteltile_brick_b.getDefaultState());
		pasteltile_brick_b_slab = new AntiqueSlab("pasteltile_brick_b_slab");
		pasteltile_brick_lg = new AntiqueBrick("pasteltile_brick_lg", 1.25F, 1024F, 0, 0);
		pasteltile_brick_lg_stairs = new AntiqueStairs("pasteltile_brick_lg_stairs", pasteltile_brick_lg.getDefaultState());
		pasteltile_brick_lg_slab = new AntiqueSlab("pasteltile_brick_lg_slab");
		pasteltile_brick_br = new AntiqueBrick("pasteltile_brick_br", 1.25F, 1024F, 0, 0);
		pasteltile_brick_br_stairs = new AntiqueStairs("pasteltile_brick_br_stairs", pasteltile_brick_br.getDefaultState());
		pasteltile_brick_br_slab = new AntiqueSlab("pasteltile_brick_br_slab");
		pasteltile_brick_bl = new AntiqueBrick("pasteltile_brick_bl", 1.25F, 1024F, 0, 0);
		pasteltile_brick_bl_stairs = new AntiqueStairs("pasteltile_brick_bl_stairs", pasteltile_brick_bl.getDefaultState());
		pasteltile_brick_bl_slab = new AntiqueSlab("pasteltile_brick_bl_slab");
		pasteltile_brick_gr = new AntiqueBrick("pasteltile_brick_gr", 1.25F, 1024F, 0, 0);
		pasteltile_brick_gr_stairs = new AntiqueStairs("pasteltile_brick_gr_stairs", pasteltile_brick_gr.getDefaultState());
		pasteltile_brick_gr_slab = new AntiqueSlab("pasteltile_brick_gr_slab");

		pillar_stone = new BlockPillarStone("pillar_stone", 0, false);
		pillar_stone_base = new BlockPillarStone("pillar_stone_base", 1, true);
		pillar_stone_w = new BlockPillarStone("pillar_stone_w", 2, false);
		pillar_stone_w_base = new BlockPillarStone("pillar_stone_w_base", 3, true);
		magic_square = new SMIron("magic_square", 2.4F, 1024F, 1, 0);

		// レンガトラップドア
		antique_tdoor_0 = new SMTrapDoor("antique_tdoor_0", 0, Material.ROCK);
		antique_tdoor_0w = new SMTrapDoor("antique_tdoor_0w", 0, Material.ROCK);
		antique_tdoor_b = new SMTrapDoor("antique_tdoor_b", 0, Material.ROCK);
		antique_tdoor_l = new SMTrapDoor("antique_tdoor_l", 0, Material.ROCK);
		antique_tdoor_g = new SMTrapDoor("antique_tdoor_g", 0, Material.ROCK);

		old_tdoor = new SMTrapDoor("old_tdoor", 0, Material.ROCK);
		old_tdoor_r = new SMTrapDoor("old_tdoor_r", 0, Material.ROCK);
		old_tdoor_g = new SMTrapDoor("old_tdoor_g", 0, Material.ROCK);
		old_tdoor_y = new SMTrapDoor("old_tdoor_y", 0, Material.ROCK);
		old_tdoor_l = new SMTrapDoor("old_tdoor_l", 0, Material.ROCK);
		old_tdoor_b = new SMTrapDoor("old_tdoor_b", 0, Material.ROCK);
		old_tdoor_s = new SMTrapDoor("old_tdoor_s", 0, Material.ROCK);

		longtile_brick_o_tdoor = new SMTrapDoor("longtile_brick_o_tdoor", 0, Material.ROCK);
		longtile_brick_p_tdoor = new SMTrapDoor("longtile_brick_p_tdoor", 0, Material.ROCK);
		longtile_brick_r_tdoor = new SMTrapDoor("longtile_brick_r_tdoor", 0, Material.ROCK);
		longtile_brick_y_tdoor = new SMTrapDoor("longtile_brick_y_tdoor", 0, Material.ROCK);
		longtile_brick_b_tdoor = new SMTrapDoor("longtile_brick_b_tdoor", 0, Material.ROCK);
		longtile_brick_g_tdoor = new SMTrapDoor("longtile_brick_g_tdoor", 0, Material.ROCK);
		longtile_brick_tdoor = new SMTrapDoor("longtile_brick_tdoor", 0, Material.ROCK);
		longtile_brick_l_tdoor = new SMTrapDoor("longtile_brick_l_tdoor", 0, Material.ROCK);
		longtile_brick_w_tdoor = new SMTrapDoor("longtile_brick_w_tdoor", 0, Material.ROCK);

		whiteline_brick_tdoor = new SMTrapDoor("whiteline_brick_tdoor", 0, Material.ROCK);
		whiteline_brick_y_tdoor = new SMTrapDoor("whiteline_brick_y_tdoor", 0, Material.ROCK);
		whiteline_brick_b_tdoor = new SMTrapDoor("whiteline_brick_b_tdoor", 0, Material.ROCK);

		pasteltile_brick_y_tdoor = new SMTrapDoor("pasteltile_brick_y_tdoor", 0, Material.ROCK);
		pasteltile_brick_r_tdoor = new SMTrapDoor("pasteltile_brick_r_tdoor", 0, Material.ROCK);
		pasteltile_brick_b_tdoor = new SMTrapDoor("pasteltile_brick_b_tdoor", 0, Material.ROCK);
		pasteltile_brick_lg_tdoor = new SMTrapDoor("pasteltile_brick_lg_tdoor", 0, Material.ROCK);
		pasteltile_brick_br_tdoor = new SMTrapDoor("pasteltile_brick_br_tdoor", 0, Material.ROCK);
		pasteltile_brick_gr_tdoor = new SMTrapDoor("pasteltile_brick_gr_tdoor", 0, Material.ROCK);
		pasteltile_brick_bl_tdoor = new SMTrapDoor("pasteltile_brick_bl_tdoor", 0, Material.ROCK);

		antique_brick_pillar = new BlockPillarBrick("antique_brick_pillar");
		antique_brick_pillar_l = new BlockPillarBrick("antique_brick_pillar_l");
		whiteline_brick_pillar = new BlockPillarBrick("whiteline_brick_pillar");
		whiteline_brick_pillar_y = new BlockPillarBrick("whiteline_brick_pillar_y");
		pasteltile_brick_pillar_y = new BlockPillarBrick("pasteltile_brick_pillar_y");

		antique_window_white = new SMTrapDoor("antique_window_white", 0, Material.WOOD);
		antique_window_brown = new SMTrapDoor("antique_window_brown", 0, Material.WOOD);
		antique_window_brown2 = new SMTrapDoor("antique_window_brown2", 0, Material.WOOD);
		antique_window_green = new SMTrapDoor("antique_window_green", 0, Material.WOOD);

		// ガラス
		sugarglass = new SMGlass("sugarglass", 0, false, false);
		shading_sugarglass = new SMGlass("shading_sugarglass", 1, true, false);
		frosted_glass = new SMGlass("frosted_glass", 2, false, false);
		frosted_glass_line = new SMGlass("frosted_glass_line", 3, false, false);
		prismglass = new SMGlass("prismglass", 4, false, true);
		shading_prismglass = new SMGlass("shading_prismglass", 5, true, true);

		// 4パネガラス
		green4panel_glass = new WoodPaneGlass("green4panel_glass", 0, false, false);
		lightbrown4panel_glass = new WoodPaneGlass("lightbrown4panel_glass", 0, false, false);
		brown4panel_glass = new WoodPaneGlass("brown4panel_glass", 0, false, false);
		darkbrown4panel_glass = new WoodPaneGlass("darkbrown4panel_glass", 0, false, false);
		ami_glass = new WoodPaneGlass("ami_glass", 0, false, false);
		gorgeous_glass = new WoodPaneGlass("gorgeous_glass", 0, false, false);
		gorgeous_glass_w = new WoodPaneGlass("gorgeous_glass_w", 0, false, false);

		// 板ガラス
		sugarglass_pane = new SMGlassPane("sugarglass_pane", blockList, 0);
		shading_sugarglass_pane = new SMGlassPane("shading_sugarglass_pane", blockList, 1);
		frosted_glass_pane = new SMGlassPane("frosted_glass_pane", blockList, 0);
		frosted_glass_line_pane = new SMGlassPane("frosted_glass_line_pane", blockList, 0);
		prismglass_pane = new SMGlassPane("prismglass_pane", blockList, 2);
		green4panel_glass_pane = new SMGlassPaneVertical("green4panel_glass_pane", blockList, 0);
		brown4panel_glass_pane = new SMGlassPaneVertical("brown4panel_glass_pane", blockList, 0);
		lightbrown4panel_glass_pane = new SMGlassPaneVertical("lightbrown4panel_glass_pane", blockList, 0);
		darkbrown4panel_glass_pane = new SMGlassPaneVertical("darkbrown4panel_glass_pane", blockList, 0);
		ami_glass_pane = new SMGlassPaneVertical("ami_glass_pane", blockList, 0);
		gorgeous_glass_pane = new SMGlassPaneVertical("gorgeous_glass_pane", blockList, 0);
		gorgeous_glass_w_pane = new SMGlassPaneVertical("gorgeous_glass_w_pane", blockList, 0);

		// 花
		twilight_alstroemeria = new BlockAlstroemeria("twilight_alstroemeria");
		lier_rose = new BlockLierRose("lier_rose");

		// バスケット
		iberis_umbellata_basket = new FlowerBuscket("iberis_umbellata_basket");
		campanula_basket = new FlowerBuscket("campanula_basket");
		primula_polyansa_basket = new FlowerBuscket("primula_polyansa_basket");
		christmas_rose_basket = new FlowerBuscket("christmas_rose_basket");
		portulaca_basket = new FlowerBuscket("portulaca_basket");
		pansy_yellowmazenta_basket = new FlowerBuscket("pansy_yellowmazenta_basket");
		pansy_blue_basket = new FlowerBuscket("pansy_blue_basket");
		surfinia_basket = new FlowerBuscket("surfinia_basket");
		marigold_basket = new FlowerBuscket("marigold_basket");

		// 花瓶
		flower_vese_o = new WandPedal("flower_vese_o", 3);
		flower_vese_b = new WandPedal("flower_vese_b", 3);
		flower_vese_p = new WandPedal("flower_vese_p", 3);
		flower_vese_s = new WandPedal("flower_vese_s", 3);
		flower_vese_w = new WandPedal("flower_vese_w", 3);
		flower_vese_y = new WandPedal("flower_vese_y", 3);

		// 鉱石
		ac_ore = new BlockSMOre("ac_ore", 0);
		cosmic_crystal_ore = new BlockSMOre("cosmic_crystal_ore", 1);
		alt_block = new SMIron("alt_block", 2.4F, 1024F, 1, 0);
		cosmos_light_block = new SMIron("cosmos_light_block", 2.4F, 1024F, 1, 0);
		aethercrystal_block = new SMIron("aethercrystal_block", 2.4F, 1024F, 1, 0);
		divinecrystal_block = new SMIron("divinecrystal_block", 2.4F, 1024F, 1, 0);
		purecrystal_block = new SMIron("purecrystal_block", 2.4F, 1024F, 1, 0);
		magiaflux_block = new SMIron("magiaflux_block", 2.4F, 1024F, 1, 0);

		aethercrystal_block_alpha = new SMGlassAlpha("aethercrystal_block_alpha", 0, false, false);
		divinecrystal_block_alpha = new SMGlassAlpha("divinecrystal_block_alpha", 0, false, false);
		cosmos_light_block_alpha = new SMGlassAlpha("cosmos_light_block_alpha", 0, false, false);

		// MFブロック(基本)
		twilightlight = new MagicLight("twilightlight", 1, magicList);
		mfchanger = new MFChange("mfchanger", 0);
		advanced_mfchanger = new MFChange("advanced_mfchanger", 1);
		mftank = new MFTank("mftank", 0);
		advanced_mftank = new MFTank("advanced_mftank", 1);
		mm_tank = new MFTank("mm_tank", 2);
		creative_tank = new MFTank("creative_tank", 3);
		mftable = new MFTable("mftable", 0);
		advanced_mftable = new MFTable("advanced_mftable", 1);
		mm_table = new MFTable("mm_table", 2);
		obmagia_top = new ObMagia("alt_table_top", noTabList);
		obmagia_bottom = new ObMagia("alt_table_bottom", magicList);
		pedestal_creat = new PedalCreate("pedestal_creat", 0);
		altar_creat = new PedalCreate("altar_creat", 1);
		altar_creation_star = new PedalCreate("altar_creation_star", 2);
		aether_lanp = new AetherLanp("aether_lanp");
		sturdust_crystal_bot = new StardustCrystal("sturdust_crystal_bot", 0, magicList);
		sturdust_crystal_top = new StardustCrystal("sturdust_crystal_top", 1, noTabList);

		// MFブロック(その他)
		mffurnace_off = new MFFurnace("mffurnace_off", 0, magicList);
		mffurnace_on = new MFFurnace("mffurnace_on", 0, noTabList);
		advanced_mffurnace_off = new MFFurnace("advanced_mffurnace_off", 1, magicList);
		advanced_mffurnace_on = new MFFurnace("advanced_mffurnace_on", 1, noTabList);
		mffisher = new MFFisher("mffisher", 0);
		flyishforer = new MFFisher("flyishforer", 1);
		mfsqueezer = new MFFisher("mfsqueezer", 2);
		mfgeneration = new MFFisher("mfgeneration", 3);
		mfharvester = new ToolRepairBlock("mfharvester", 8);
		aether_furnace_top = new AetherFurnace("aether_furnace_top", noTabList, true, false);
		aether_furnace_bottom = new AetherFurnace("aether_furnace_bottom", magicList, false, false);
		advanced_aether_furnace_top = new AetherFurnace("advanced_aether_furnace_top", noTabList, true, true);
		advanced_aether_furnace_bottom = new AetherFurnace("advanced_aether_furnace_bottom", magicList, false, true);
		tool_repair = new ToolRepairBlock("tool_repair", 0);
		magia_rewrite = new ToolRepairBlock("magia_rewrite", 1);
		magia_reincarnation = new ToolRepairBlock("magia_reincarnation", 7);
		magia_successor = new ToolRepairBlock("magia_successor", 2);
		arcane_table = new ToolRepairBlock("arcane_table", 3);
		magia_accelerator = new ToolRepairBlock("magia_accelerator", 4);
		workbench_storage = new ToolRepairBlock("workbench_storage", 5);
		aether_enchanttable = new ToolRepairBlock("aether_enchanttable", 6);
		gravity_chest = new GravityChest("gravity_chest", 0);
		aether_hopper = new AetherHopper("aether_hopper", 0);
		clerolanp = new CleroLanp("clerolanp");
		aether_cover = new AetherCover("aether_cover", 0);
		aether_cover_rock = new AetherCover("aether_cover_rock", 1);
		magia_lantern = new MagiaLight("magia_lantern", 1);
		magia_light = new MagiaLight("magia_light", 0);

		// MFブロック(花瓶)
		mfpot = new MFPot("mfpot", 0);
		twilightalstroemeria_pot = new MFPot("twilightalstroemeria_pot", 1);
		snowdrop_pot = new MFPot("snowdrop_pot", 2);
		turkey_balloonflower_pot = new MFPot("turkey_balloonflower_pot", 3);
		ultramarine_rose_pot = new MFPot("ultramarine_rose_pot", 4);
		solid_star_pot = new MFPot("solid_star_pot", 5);
		zinnia_pot = new MFPot("zinnia_pot", 6);
		hydrangea_pot = new MFPot("hydrangea_pot", 7);
		carnation_crayola_pot = new MFPot("carnation_crayola_pot", 8);
		christmarose_ericsmithii_pot = new MFPot("christmarose_ericsmithii_pot", 9);

		// 花
		cornflower = new BlockCornFlower("cornflower", 0);
		lily_valley = new BlockCornFlower("lily_valley", 1);
		cosmos = new BlockCornFlower("cosmos", 2);
		blackrose = new BlockCornFlower("blackrose", 3);
		white_clover = new BlockCornFlower("white_clover", 4);
		foxtail_grass = new BlockCornFlower("foxtail_grass", 5);
		snowdrop = new BlockCornFlower("snowdrop", 6);
		turkey_balloonflower = new BlockCornFlower("turkey_balloonflower", 7);
		iberis_umbellata = new BlockCornFlower("iberis_umbellata", 8);
		ultramarine_rose = new BlockCornFlower("ultramarine_rose", 9);
		solid_star = new BlockCornFlower("solid_star", 10);
		zinnia = new BlockCornFlower("zinnia", 11);
		campanula = new BlockCornFlower("campanula", 12);
		primula_polyansa = new BlockCornFlower("primula_polyansa", 13);
		hydrangea = new BlockCornFlower("hydrangea", 14);
		carnation_crayola = new BlockCornFlower("carnation_crayola", 15);
		christmas_rose = new BlockCornFlower("christmas_rose", 16);
		portulaca = new BlockCornFlower("portulaca", 17);
		surfinia = new BlockCornFlower("surfinia", 18);
		pansy_blue = new BlockCornFlower("pansy_blue", 19);
		pansy_yellowmazenta = new BlockCornFlower("pansy_yellowmazenta", 20);
		marigold = new BlockCornFlower("marigold", 21);
		christmarose_ericsmithii = new BlockCornFlower("christmarose_ericsmithii", 22);

		// 光源
		pole_down = new BlockPole("pole_down", furniList, 0);
		pole = new BlockPole("pole", noTabList, 1);

		hanging_banner_b = new BlockBangingBanner("hanging_banner_b");
		hanging_banner_r = new BlockBangingBanner("hanging_banner_r");
		hanging_banner_o = new BlockBangingBanner("hanging_banner_o");
		hanging_banner_p = new BlockBangingBanner("hanging_banner_p");
		hanging_banner_s = new BlockBangingBanner("hanging_banner_s");
		hanging_banner_l = new BlockBangingBanner("hanging_banner_l");

		lantern = new BlockPole("lantern", noTabList, 2);
		lantern_side1 = new BlockLantern("lantern_side1", 0, furniList);
		lantern_side2 = new BlockLantern("lantern_side2", 1, noTabList);
		iron_chain = new BlockIronChain("iron_chain");
		antique_lantern = new AntiqueLantern("antique_lantern");
		antique_candle = new BlockCandle("antique_candle");
		modenlanp = new BlockModenLanp("modenlanp", 0);
		walllamp = new BlockModenLanp("walllamp", 1);
		walllamp_long = new BlockModenLanp("walllamp_long", 2);
		ceiling_spotlight = new BlockFaceLanp("ceiling_spotlight", 0);
		table_modernlamp_off = new BlockTableLanp("table_modernlamp_off", 0, 0, true, furniList);
		table_modernlamp_on = new BlockTableLanp("table_modernlamp_on", 1, 0, true, noTabList);
		table_lamp = new BlockTableLanp("table_lamp", 1, 0, false, furniList);
		table_lantern = new BlockTableLanp("table_lantern", 1, 0, false, furniList);

		ceiling_light = new BlockCeilingLight("ceiling_light");
		fluorescent_light = new BlockFluorescentLight("fluorescent_light");
		glow_lamp = new BlockLanp("glow_lamp", 0, 32F, 0, 1, 0.5F, SoundType.METAL, furniList);
		magic_circle = new BlockLanp("magic_circle", 0, 32F, 0, 1, 0.5F, SoundType.GLASS, noTabList);
		glow_light = new BlockLight("glow_light");
		gorgeous_lamp = new BlockLight("gorgeous_lamp");
		stendglass_lamp_g_off = new BlockStendGlass("stendglass_lamp_g_off", false, 0, blockList);
		stendglass_lamp_g_on = new BlockStendGlass("stendglass_lamp_g_on", true, 1, noTabList);
		stendglass_lamp_off = new BlockStendGlass("stendglass_lamp_off", false, 2, blockList);
		stendglass_lamp_on = new BlockStendGlass("stendglass_lamp_on", true, 3, noTabList);

		// ドア
		black_moderndoor = new SMDoor("black_moderndoor", 0, Material.WOOD, SoundType.METAL);
		brown_2paneldoor = new SMDoor("brown_2paneldoor", 1, Material.WOOD, SoundType.WOOD);
		brown_5paneldoor = new SMDoor("brown_5paneldoor", 2, Material.WOOD, SoundType.WOOD);
		brown_elegantdoor = new SMDoor("brown_elegantdoor", 3, Material.WOOD, SoundType.WOOD);
		brown_arch_door = new SMDoor("brown_arch_door", 4, Material.WOOD, SoundType.WOOD);
		brown_arch_plantdoor = new SMDoor("brown_arch_plantdoor", 5, Material.WOOD, SoundType.WOOD);
		woodgold_3 = new SMDoor("woodgold_3", 6, Material.WOOD, SoundType.WOOD);
		whitewoodgold_3 = new SMDoor("whitewoodgold_3", 7, Material.WOOD, SoundType.WOOD);
		simple_door_1 = new SMDoor("simple_door_1", 8, Material.WOOD, SoundType.WOOD);
		simple_door_2 = new SMDoor("simple_door_2", 9, Material.WOOD, SoundType.WOOD);

		// 作物
		sannyflower_plant = new MagiaFlower("sannyflower_plant", 0);
		moonblossom_plant = new MagiaFlower("moonblossom_plant", 1);
		dm_plant = new MagiaFlower("drizzly_mysotis_plant", 2);
		sugarbell_plant = new SweetCrops_STAGE4("sugarbell_plant", 0, 1, 6.5F);
		clerodendrum = new SweetCrops_STAGE4("clerodendrum", 7, 1, 6.5F);
		strawberry_plant = new SweetCrops_STAGE4("strawberry_plant", 1, 0, 7.2F);
		sweetpotato_plant = new SweetCrops_STAGE4("sweetpotato_plant", 2, 0, 7.5F);
		j_radish_plant = new SweetCrops_STAGE4("j_radish_plant", 3, 0, 7.2F);
		lettuce_plant = new SweetCrops_STAGE4("lettuce_plant", 4, 0, 6.8F);
		cabbage_plant = new SweetCrops_STAGE4("cabbage_plant", 5, 0, 6.8F);
		glowflower_plant = new SweetCrops_STAGE4("glowflower_plant", 6, 1, 6.8F);
		cotton_plant = new SweetCrops_STAGE4("cotton_plant", 8, 1, 6.8F);
		spinach_plant = new SweetCrops_STAGE4("spinach_plant", 10, 1, 6.8F);
		greenpepper_plant = new SweetCrops_STAGE4("greenpepper_plant", 11, 1, 6.8F);
		blueberry_plant = new SweetCrops_STAGE5("blueberry_plant", 0, 1, 7.5F);
		sticky_stuff_plant = new SweetCrops_STAGE5("sticky_stuff_plant", 4, 1, 5.0F);
		rice_plant = new SweetCrops_STAGE6("rice_plant", 1, 0, 6.0F);
		soybean_plant = new SweetCrops_STAGE6("soybean_plant",2, 0, 7.1F);
		azuki_plant = new SweetCrops_STAGE6("azuki_plant",3, 0, 6.7F);
		whitenet_plant = new BlockWhitenet("whitenet_plant", -1, -1, 5.3F);
		corn_plant = new SweetCrops_Tall("corn_plant", 0, 1, 7.8F);
		tomato_plant = new SweetCrops_Tall("tomato_plant", 1, 1, 7.5F);
		egg_plant = new SweetCrops_Tall("egg_plant", 2, 1, 6.4F);
		fire_nasturtium_plant = new SweetCrops_STAGE4("fire_nasturtium_plant", 9, 1, 8.0F);
		olive_plant = new SweetCrops_STAGE5("olive_plant", 1, 1, 5.6F);
		vannila_plant = new SweetCrops_STAGE5("vannila_plant",2 ,0, 6.3F);
		onion_plant = new SweetCrops_STAGE5("onion_plant", 3, 0, 5.6F);
		coffee_plant = new SweetCrops_STAGE5("coffee_plant", 5, 0, 5.6F);
		pineapple_plant = new SweetCrops_STAGE5("pineapple_plant", 6, 0, 5.6F);
		raspberry_plant = new SweetCrops_STAGE6("raspberry_plant",0, 1, 6.0F);
		quartz_plant = new SweetCrops_STAGE6("quartz_plant",4, 1, 6.0F);
		chestnut_plant = new SweetCrops_STAGE3("chestnut_plant", 0);
		coconut_plant = new SweetCrops_STAGE3("coconut_plant", 1);
		banana_plant = new SweetCrops_STAGE3("banana_plant", 2);

		magiclight = new MagicLight("magiclight", 0, noTabList);
		spawn_stone = new SpawnStone("spawn_stone", 0);
		smspaner = new SpawnStone("smspaner", 1);
		crystal_core = new CrystalCore("crystal_core", 0);
		magic_core = new CrystalCore("magic_core", 1);

		// フィギア
		figurine_wm = new FigurineStand("figurine_wm", 0);
		figurine_wd = new FigurineStand("figurine_wd", 1);
		figurine_if = new FigurineStand("figurine_if", 2);
		figurine_bs = new FigurineStand("figurine_bs", 3);
		figurine_sa = new FigurineStand("figurine_sa", 4);
		figurine_zh = new FigurineStand("figurine_zh", 5);
		figurine_af = new FigurineStand("figurine_af", 6);
		figurine_rk = new FigurineStand("figurine_rk", 7);
		figurine_ec = new FigurineStand("figurine_ec", 8);
		figurine_zk = new FigurineStand("figurine_zk", 9);

		// ダンジョンブロック
		magicbarrier_on = new MagicBarrier("magicbarrier_on", 0);
		magicbarrier_off = new MagicBarrier("magicbarrier_off", 1);
		poison_block = new TrapBlock("poison_block", 0);
		warp_block = new WarpBlock("warp_block", 0, blockList);
		warp_block_on = new WarpBlock("warp_block_on", 1, noTabList);

		// 飾る用
		wand_pedal = new WandPedal("wand_pedal", 0);
		corkboard = new WandPedal("corkboard", 1);
		wallboard = new WandPedal("wallboard", 1);
		wallboard_black = new WandPedal("wallboard_black", 1);
		shopboard = new WandPedal("shopboard", 2);
		menu_list = new BlockCafeBoard("menu_list", 2);
		menu_list_w = new BlockCafeBoard("menu_list_w", 2);

		cafeboard = new BlockCafeBoard("cafeboard", 0);
		cafeboard_w = new BlockCafeBoard("cafeboard_w", 0);
		kanban_top = new BlockKanban("kanban_top", noTabList);
		kanban_bot = new BlockKanban("kanban_bot", furniList);

		smchair = new SMChair("smchair", 0);
		antique_back_chair = new SMChair("antique_back_chair", 1);
		balcony_chair = new SMChair("balcony_chair", 3);
		cafe_chair = new SMChair("cafe_chair", 3);
		counter_chair = new SMChair("counter_chair", 4);
		moden_chair = new SMChair("moden_chair", 3);
		moden_chair_b = new SMChair("moden_chair_b", 3);
		moden_chair_l = new SMChair("moden_chair_l", 3);
		woodbench = new SMWoodChair("woodbench", 5);
		woodbench_b = new SMWoodChair("woodbench_b", 5);
		sofa_r = new BlockSofa("sofa_r", 1);
		sofa_b = new BlockSofa("sofa_b", 1);
		sofa_w = new BlockSofa("sofa_w", 1);
		blinder = new BlockBlinder("blinder", 0);
		blinder_o = new BlockBlinder("blinder_o", 0);
		blinder_s = new BlockBlinder("blinder_s", 0);
		blinder_y = new BlockBlinder("blinder_y", 0);
		blinder_g = new BlockBlinder("blinder_g", 0);
		blinder_p = new BlockBlinder("blinder_p", 0);
		blinder_b = new BlockBlinder("blinder_b", 0);
		blinder_w = new BlockBlinder("blinder_w", 0);

		note_pc = new BlockNotePC("note_pc", 0);
		note_pc_r = new BlockNotePC("note_pc_r", 0);
		note_pc_p = new BlockNotePC("note_pc_p", 0);
		note_pc_o = new BlockNotePC("note_pc_o", 0);
		note_pc_l = new BlockNotePC("note_pc_l", 0);
		note_pc_i = new BlockNotePC("note_pc_i", 0);
		note_pc_b = new BlockNotePC("note_pc_b", 0);
		sm_display = new BlockNotePC("sm_display", 1);

		woodbox = new AntiqueSlab("woodbox", BlockInit.furniList);
		smtable = new SMTable("smtable", 0);
		smtable_lace = new SMTable("smtable_lace", 1);
		smtable_dot = new SMTableDot("smtable_dot");
		balcony_table = new SMTableDot("balcony_table");
		cafe_table = new SMTableDot("cafe_table");
		stainless_steel_table = new SMTable("stainless_steel_table", 2);
		moden_table = new SMTable("moden_table", 0);
		moden_table_b = new SMTable("moden_table_b", 0);
		moden_table_l = new SMTable("moden_table_l", 0);

		// チェスト系
		moden_rack = new BlockModenRack("moden_rack", 0);
		moden_rack_brown = new BlockModenRack("moden_rack_brown", 0);
		moden_wallrack = new BlockModenWallRack("moden_wallrack", 1);
		moden_wallrack_b = new BlockModenWallRack("moden_wallrack_b", 1);
		moden_wallrack_l = new BlockModenWallRack("moden_wallrack_l", 1);
		cafe_wallrack = new BlockModenWallRack("cafe_wallrack", 1);
		iron_wallrack = new BlockModenWallRack("iron_wallrack", 1);
		moden_shelf_y = new BlockWoodChest("moden_shelf_y", 3);
		moden_shelf_s = new BlockWoodChest("moden_shelf_s", 3);
		moden_shelf_r = new BlockWoodChest("moden_shelf_r", 3);
		moden_shelf_o = new BlockWoodChest("moden_shelf_o", 3);
		moden_shelf_p = new BlockWoodChest("moden_shelf_p", 3);
		moden_shelf_l = new BlockWoodChest("moden_shelf_l", 3);
		moden_shelf_i = new BlockWoodChest("moden_shelf_i", 3);
		bottle_rack = new BlockModenRack("bottle_rack", 7);
		bottle_rack_b = new BlockModenRack("bottle_rack_b", 7);
		iron_shelf = new BlockModenRack("iron_shelf", 12);
		ceiling_shelf = new BlockModenRack("ceiling_shelf", 8);
		range_hood_rack = new BlockModenRack("range_hood_rack", 9);
		fruit_crate = new BlockModenRack("fruit_crate", 13);
		fruit_crate_coconut = new BlockModenRack("fruit_crate_coconut", 13);
		fruit_crate_orange = new BlockModenRack("fruit_crate_orange", 13);
		fruit_crate_smwood = new BlockModenRack("fruit_crate_smwood", 13);
		fruit_crate_wood_black = new BlockModenRack("fruit_crate_wood_black", 13);
		wooden_tool_box = new BlockModenRack("wooden_tool_box", 14);
		fruit_crate_box = new BlockModenRack("fruit_crate_box", 15);
		fruit_crate_box_coconut = new BlockModenRack("fruit_crate_box_coconut", 15);
		fruit_crate_box_orange = new BlockModenRack("fruit_crate_box_orange", 15);
		fruit_crate_box_smwood = new BlockModenRack("fruit_crate_box_smwood", 15);
		fruit_crate_box_wood_black = new BlockModenRack("fruit_crate_box_wood_black", 15);
		moden_stair = new BlockModenStair("moden_stair");
		moden_stair_b = new BlockModenStair("moden_stair_b");
		moden_stair_l = new BlockModenStair("moden_stair_l");
		moden_stair_coconut = new BlockModenStair("moden_stair_coconut");
		moden_stair_chestnut = new BlockModenStair("moden_stair_chestnut");
		moden_stair_lemon = new BlockModenStair("moden_stair_lemon");
		magicbook = new MagicBook("magicbook");
		magicbook_b = new MagicBook("magicbook_b");
		prism_woodchest = new BlockWoodChest("prism_woodchest", 0);
		estor_woodchest = new BlockWoodChest("estor_woodchest", 0);
		rattan_chest_y = new BlockWoodChest("rattan_chest_y", 0);
		rattan_chest_b = new BlockWoodChest("rattan_chest_b", 0);
		treasure_chest = new BlockWoodChest("treasure_chest", 1);
		magia_storage_1 = new BlockMagiaStorage("magia_storage_1", 0);
		magia_storage_2 = new BlockMagiaStorage("magia_storage_2", 1);
		magia_storage_3 = new BlockMagiaStorage("magia_storage_3", 2);
		magia_storage_4 = new BlockMagiaStorage("magia_storage_4", 3);
		magia_storage_5 = new BlockMagiaStorage("magia_storage_5", 4);
		shoe_box = new BlockWoodChest("shoe_box", 4);
		shoe_box_r = new BlockWoodChest("shoe_box_r", 4);
		shoe_box_g = new BlockWoodChest("shoe_box_g", 4);
		shoe_box_piling = new BlockWoodChest("shoe_box_piling", 5);
		shoe_box_piling_r = new BlockWoodChest("shoe_box_piling_r", 5);
		shoe_box_piling_g = new BlockWoodChest("shoe_box_piling_g", 5);
		post_w = new BlockWoodChest("post_w", 6);
		post_r = new BlockWoodChest("post_r", 6);
		post_wood_orange = new BlockWoodChest("post_wood_orange", 7);
		post_wood_orange_w = new BlockWoodChest("post_wood_orange_w", 7);
		post_wood_chestnut = new BlockWoodChest("post_wood_chestnut", 7);
		post_wood_smstone = new BlockWoodChest("post_wood_smstone", 7);
		cafe_kitchen_table = new BlockCafeKitchen("cafe_kitchen_table", 2);
		cafe_kitchen_sink = new BlockWoodChest("cafe_kitchen_sink", 2);

		parallel_interfere = new BlockParallelInterfere("parallel_interfere", 0);
		stardust_wish = new BlockParallelInterfere("stardust_wish", 1);
		magiaflux_core = new BlockMagiaFluxCore("magiaflux_core");

		// ラタンシリーズ
		rattan_chair_y = new SMChair("rattan_chair_y", 2);
		rattan_chair_b = new SMChair("rattan_chair_b", 2);
		rattan_chair_d = new SMChair("rattan_chair_d", 2);
		rattan_basket_y = new BlockRattanBasket("rattan_basket_y", 0);
		rattan_basket_b = new BlockRattanBasket("rattan_basket_b", 0);
		rattan_basket_d = new BlockRattanBasket("rattan_basket_d", 0);

		// 飾り用植物
		goldcrest = new GoldCrest("goldcrest");

		// 植え込み
		chestnut_planting = new BlockPlant("chestnut_planting");
		orange_planting = new BlockPlant("orange_planting");
		estor_planting = new BlockPlant("estor_planting");
		peach_planting = new BlockPlant("peach_planting");

		// 植木鉢
		compost_drit = new PlantPot("compost_drit", SoundType.GROUND, 3);
		antique_brick_pot_r = new PlantPot("antique_brick_pot_r", SoundType.GROUND, 0);
		antique_brick_pot_w = new PlantPot("antique_brick_pot_w", SoundType.GROUND, 0);
		antique_brick_pot_l = new PlantPot("antique_brick_pot_l", SoundType.GROUND, 0);
		antique_brick_pot_g = new PlantPot("antique_brick_pot_g", SoundType.GROUND, 0);

		orange_planks_pot = new PlantPot("orange_planks_pot", SoundType.GROUND, 1);
		orange_planks_pot_w = new PlantPot("orange_planks_pot_w", SoundType.GROUND, 2);
		estor_planks_pot = new PlantPot("estor_planks_pot", SoundType.GROUND, 3);
		longtile_brick_pot_o = new PlantPot("longtile_brick_pot_o", SoundType.GROUND, 4);
		longtile_brick_pot_l = new PlantPot("longtile_brick_pot_l", SoundType.GROUND, 5);
		whiteline_brick_pot = new PlantPot("whiteline_brick_pot", SoundType.GROUND, 6);
		whiteline_brick_pot_y = new PlantPot("whiteline_brick_pot_y", SoundType.GROUND, 6);
		whiteline_brick_pot_b = new PlantPot("whiteline_brick_pot_b", SoundType.GROUND, 6);
		pasteltile_brick_pot_y = new PlantPot("pasteltile_brick_pot_y", SoundType.GROUND, 6);
		pasteltile_brick_pot_r = new PlantPot("pasteltile_brick_pot_r", SoundType.GROUND, 6);
		pasteltile_brick_pot_b = new PlantPot("pasteltile_brick_pot_b", SoundType.GROUND, 6);
		pasteltile_brick_pot_lg = new PlantPot("pasteltile_brick_pot_lg", SoundType.GROUND, 6);
		pasteltile_brick_pot_bl = new PlantPot("pasteltile_brick_pot_bl", SoundType.GROUND, 6);
		pasteltile_brick_pot_gr = new PlantPot("pasteltile_brick_pot_gr", SoundType.GROUND, 6);

		antique_brick_planter_r = new Planter("antique_brick_planter_r", 0);
		antique_brick_planter_l = new Planter("antique_brick_planter_l", 0);
		whiteline_brick_planter_y = new Planter("whiteline_brick_planter_y", 0);
		whiteline_brick_planter_b = new Planter("whiteline_brick_planter_b", 0);
		chestnut_planks_planter = new Planter("chestnut_planks_planter", 0);
		orange_planks_planter = new Planter("orange_planks_planter", 0);
		orange_planks_planter_w = new Planter("orange_planks_planter_w", 0);

		// ガーデンフェンス
		white_ironfence = new IronFence("white_ironfence");
		black_ironfence = new IronFence("black_ironfence");
		fance_gothic_white = new IronFence("fance_gothic_white");
		fance_gothic_black = new IronFence("fance_gothic_black");

		path_tree_orange = new BlockFaceWood("path_tree_orange", SoundType.WOOD, 1);
		path_tree_lemon = new BlockFaceWood("path_tree_lemon", SoundType.WOOD, 1);

		awning_tent_b = new BlockAwningTent("awning_tent_b", 0);
		awning_tent_o = new BlockAwningTent("awning_tent_o", 0);
		awning_tent_p = new BlockAwningTent("awning_tent_p", 0);
		awning_tent_r = new BlockAwningTent("awning_tent_r", 0);
		awning_tent_s = new BlockAwningTent("awning_tent_s", 0);
		awning_tent_w = new BlockAwningTent("awning_tent_w", 0);
		awning_tent_bl = new BlockAwningTent("awning_tent_bl", 0);
		awning_tent_or = new BlockAwningTent("awning_tent_or", 0);
		awning_tent_g = new BlockAwningTent("awning_tent_g", 0);
		parasol_pole = new BlockParasolPole("parasol_pole", false);
		parasol_pole_base = new BlockParasolPole("parasol_pole_base", true);

		fence_pole_estor = new FencePole("fence_pole_estor", 0, false);
		fence_pole_estor_base = new FencePole("fence_pole_estor_base", 1, true);
		fence_pole_prism = new FencePole("fence_pole_prism", 2, false);
		fence_pole_prism_base = new FencePole("fence_pole_prism_base", 3, true);
		fence_pole_pearch = new FencePole("fence_pole_pearch", 4, false);
		fence_pole_pearch_base = new FencePole("fence_pole_pearch_base", 5, true);

		log_fence_normal_estor = new BlockSMFence("log_fence_normal_estor", 0);
		log_fence_estor = new BlockSMFence("log_fence_estor", 0);
		log_fence_vertical_estor = new BlockFenceVertical("log_fence_vertical_estor", 0);
		log_fence_slanted_estor = new BlockSMFence("log_fence_slanted_estor", 0);
		log_fence_normal_peach = new BlockSMFence("log_fence_normal_peach", 0);
		log_fence_peach = new BlockSMFence("log_fence_peach", 0);
		log_fence_vertical_peach = new BlockFenceVertical("log_fence_vertical_peach", 0);
		log_fence_slanted_peach = new BlockSMFence("log_fence_slanted_peach", 0);
		log_fence_normal_prism = new BlockSMFence("log_fence_normal_prism", 0);
		log_fence_prism = new BlockSMFence("log_fence_prism", 0);
		log_fence_vertical_prism = new BlockFenceVertical("log_fence_vertical_prism", 0);
		log_fence_slanted_prism = new BlockSMFence("log_fence_slanted_prism", 0);

		stone_fence_whiteline = new BlockSMFence("stone_fence_whiteline", 1);
		stone_fence_whiteline_b = new BlockSMFence("stone_fence_whiteline_b", 1);
		stone_fence_whiteline_y = new BlockSMFence("stone_fence_whiteline_y", 1);
		stone_fence_antique = new BlockSMFence("stone_fence_antique", 1);
		stone_fence_antique_w = new BlockSMFence("stone_fence_antique_w", 1);
		stone_fence_antique_l = new BlockSMFence("stone_fence_antique_l", 1);
		stone_fence_antique_g = new BlockSMFence("stone_fence_antique_g", 1);
		stone_fence_pastel_y = new BlockSMFence("stone_fence_pastel_y", 1);
		stone_fence_pastel_r = new BlockSMFence("stone_fence_pastel_r", 1);
		stone_fence_pastel_b = new BlockSMFence("stone_fence_pastel_b", 1);
		stone_fence_pastel_lg = new BlockSMFence("stone_fence_pastel_lg", 1);
		stone_fence_pastel_gr = new BlockSMFence("stone_fence_pastel_gr", 1);

		brick_fence_antique = new BlockSMFence("brick_fence_antique", 1);
		brick_fence_antique_w = new BlockSMFence("brick_fence_antique_w", 1);
		brick_fence_antique_b = new BlockSMFence("brick_fence_antique_b", 1);
		brick_fence_antique_g = new BlockSMFence("brick_fence_antique_g", 1);
		brick_fence_whiteline_y = new BlockSMFence("brick_fence_whiteline_y", 1);
		brick_fence_whiteline_b = new BlockSMFence("brick_fence_whiteline_b", 1);

		counter_table_white_brick = new BlockCounterTable("counter_table_white_brick", 0);
		counter_table_stove_white_brick = new BlockStove("counter_table_stove_white_brick", 1);
		counter_table_sink_white_brick = new BlockSink("counter_table_sink_white_brick");
		counter_table_oven_white_brick = new BlockOven("counter_table_oven_white_brick", 2, true);
		counter_table_chest_white_brick = new BlockWoodChest("counter_table_chest_white_brick", 0);
		counter_table_half_white_brick = new BlockVerticalHalf("counter_table_half_white_brick", 0);

		counter_table_white_brick_y = new BlockCounterTable("counter_table_white_brick_y", 0);
		counter_table_stove_white_brick_y = new BlockStove("counter_table_stove_white_brick_y", 1);
		counter_table_sink_white_brick_y = new BlockSink("counter_table_sink_white_brick_y");
		counter_table_oven_white_brick_y = new BlockOven("counter_table_oven_white_brick_y", 2, false);
		counter_table_chest_white_brick_y = new BlockWoodChest("counter_table_chest_white_brick_y", 0);
		counter_table_half_white_brick_y = new BlockVerticalHalf("counter_table_half_white_brick_y", 0);

		counter_table_white_brick_b = new BlockCounterTable("counter_table_white_brick_b", 0);
		counter_table_stove_white_brick_b = new BlockStove("counter_table_stove_white_brick_b", 1);
		counter_table_sink_white_brick_b = new BlockSink("counter_table_sink_white_brick_b");
		counter_table_oven_white_brick_b = new BlockOven("counter_table_oven_white_brick_b", 2, false);
		counter_table_chest_white_brick_b = new BlockWoodChest("counter_table_chest_white_brick_b", 0);
		counter_table_half_white_brick_b = new BlockVerticalHalf("counter_table_half_white_brick_b", 0);

		counter_table_orange_planks = new BlockCounterTable("counter_table_orange_planks", 0);
		counter_table_stove_orange_planks = new BlockStove("counter_table_stove_orange_planks", 1);
		counter_table_sink_orange_planks = new BlockSink("counter_table_sink_orange_planks");
		counter_table_oven_orange_planks = new BlockOven("counter_table_oven_orange_planks", 2, false);
		counter_table_chest_orange_planks = new BlockWoodChest("counter_table_chest_orange_planks", 0);
		counter_table_half_orange_planks = new BlockVerticalHalf("counter_table_half_orange_planks", 0);

		counter_table_modan = new BlockCounterTable("counter_table_modan", 0);
		counter_table_stove_modan = new BlockStove("counter_table_stove_modan", 1);
		counter_table_sink_modan = new BlockSink("counter_table_sink_modan");
		counter_table_oven_modan = new BlockOven("counter_table_oven_modan", 1, false);
		counter_table_chest_modan = new BlockWoodChest("counter_table_chest_modan", 0);
		counter_table_half_modan = new BlockVerticalHalf("counter_table_half_modan", 0);

		ventilation_fan = new BlockFaceWood("ventilation_fan", SoundType.METAL, 3);
		ventilation_fan_light = new BlockFaceWood("ventilation_fan_light", SoundType.METAL, 4);


		// 裏データ
		sample = new BlockLanp("sample", 0, 32F, 0, 1, 0.5F, SoundType.GLASS, noTabList);

	}

	//登録したブロックをクリエタブにセット
	public static void register() {

		// 通常
		for (Block block : blockList) {
			registerBlock(block, 0);
		}

		// 家具
		for (Block block : furniList) {
			registerBlock(block, 3);
		}

		// 魔法
		for (Block block : magicList) {
			registerBlock(block, 2);
		}

		for (Block block : noTabList) {
			registerBlock(block, 1);
		}
	}

	public static void registerBlock(Block block, int data) {

		ForgeRegistries.BLOCKS.register(block);

		switch (data) {

		// 通常
		case 0:
			block.setCreativeTab(SweetMagicCore.SMTab);
			break;

		// 魔法
		case 2:
			block.setCreativeTab(SweetMagicCore.SMMagicTab);
			break;

		// 家具
		case 3:
			block.setCreativeTab(SweetMagicCore.SMFurnitureTab);
			break;
		}

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

		else if (block instanceof ISlabItemBlock) {
			item = new SMBlockSlab(block, (ISlabItemBlock) block);
		}

		// それ以外
		else {
			item = new ItemBlock(block);
		}

		return item;
	}
}
