package sweetmagic.api.recipe.pedal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.util.OreItems;

@SMPedalRecipePlugin(priority = EventPriority.LOW)
public class PedalRecipePlugin implements IPedalRecipePlugin {

	@Override
	public void registerPedalRecipe(PedalRecipes recipe) {

		// MFかまど
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Blocks.FURNACE),
			new Object[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.mf_sbottle, 4), new OreItems("stone", 16) },
			new ItemStack[] { new ItemStack(BlockInit.mffurnace_off) },
			10000
		));

		// MF釣り機
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.sugarglass, 4),
			new Object[] { new OreItems("ingotIron", 6), new ItemStack(Items.FISHING_ROD), new ItemStack(ItemInit.aether_crystal, 2) },
			new ItemStack[] { new ItemStack(BlockInit.mffisher) },
			1000
		));

		// 肉生産機
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.prismglass, 4),
			new Object[] { new OreItems("ingotIron", 6), new ItemStack(ItemInit.machete), new ItemStack(ItemInit.aether_crystal, 2) },
			new ItemStack[] { new ItemStack(BlockInit.flyishforer) },
			1000
		));

		// 牛乳絞り機
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.ami_glass, 4),
			new Object[] { new OreItems("ingotIron", 6), new ItemStack(Items.LEATHER, 4), new ItemStack(ItemInit.aether_crystal, 2) },
			new ItemStack[] { new ItemStack(BlockInit.mfsqueezer) },
			1000
		));

		// 溶岩生成機
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.brown4panel_glass, 4),
			new Object[] { new OreItems("ingotIron", 6), new ItemStack(Items.LAVA_BUCKET), new ItemStack(ItemInit.pure_crystal, 2) },
			new ItemStack[] { new ItemStack(BlockInit.mfgeneration) },
			100000
		));

		// ハーヴェスター
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.green4panel_glass, 4),
			new Object[] { new OreItems("ingotIron", 6), new ItemStack(ItemInit.alt_sickle, 1, 32767), new ItemStack(ItemInit.magic_book_scarlet) },
			new ItemStack[] { new ItemStack(BlockInit.mfharvester) },
			300000
		));

		// 不死のトーテム
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Items.EMERALD, 2),
			new Object[] { new ItemStack(ItemInit.witch_tears, 2), new ItemStack(ItemInit.magicmeal, 16) },
			new ItemStack[] { new ItemStack(Items.TOTEM_OF_UNDYING) },
			300000
		));

		// 魔法流の杖
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.ender_shard),
			new Object[] { new OreItems("stickWood", 2), "nuggetIron" },
			new ItemStack[] { new ItemStack(ItemInit.mf_stuff) }
		));

		// エーテルワンド
		recipe.addRecipe(new PedalRecipes(
			"nuggetGold",
			new Object[] { new ItemStack(ItemInit.aether_crystal, 3), new OreItems("stickWood", 2), new OreItems("feather", 2) },
			new ItemStack[] { new ItemStack(ItemInit.aether_wand) }
		));

		// 機敏な羽根
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.mf_bottle, 2),
			new Object[] { new ItemStack(ItemInit.cotton_cloth, 8), new OreItems("feather", 4) },
			new ItemStack[] { new ItemStack(ItemInit.prompt_feather) },
			1000
		));

		// ワイドレンジグローブ
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Items.IRON_INGOT, 4),
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 2), new ItemStack(ItemInit.cotton_cloth, 8) },
			new ItemStack[] { new ItemStack(ItemInit.range_glove) },
			1000
		));

		// 不思議なフォーク
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.alt_block),
			new Object[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(ItemInit.mysterious_page) },
			new ItemStack[] { new ItemStack(ItemInit.mysterious_fork) },
			1000
		));

		// マジシャンズビギナーズハウス
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.mystical_page),
			new Object[] { new ItemStack(BlockInit.mfchanger), new ItemStack(BlockInit.mftable), new ItemStack(BlockInit.mftank) },
			new ItemStack[] { new ItemStack(ItemInit.magicianbeginner_book) },
			10000
		));

		// スイートマジックハウス
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.magicianbeginner_book),
			new Object[] { new ItemStack(ItemInit.alternative_ingot, 4), new ItemStack(ItemInit.mf_sbottle, 8) },
			new ItemStack[] { new ItemStack(ItemInit.smhouse) },
			100000
		));

		// エーテル・フラッシュライト
		recipe.addRecipe(new PedalRecipes(
			new OreItems("blockGlass", 4),
			new Object[] { new ItemStack(ItemInit.alternative_ingot, 2), new ItemStack(ItemInit.prizmium) },
			new ItemStack[] { new ItemStack(ItemInit.aether_flashlight) },
			1000
		));

		// 種収穫袋
		recipe.addRecipe(new PedalRecipes(
			new OreItems("listAllseed", 16),
			new Object[] { new ItemStack(ItemInit.cotton_cloth, 8), new OreItems("chestWood", 2) },
			new ItemStack[] { new ItemStack(ItemInit.seed_harvest_bag) },
			100
		));

		// 苗木収穫袋
		recipe.addRecipe(new PedalRecipes(
			new OreItems("treeSapling", 16),
			new Object[] { new ItemStack(ItemInit.cotton_cloth, 8), new OreItems("chestWood", 2) },
			new ItemStack[] { new ItemStack(ItemInit.sapling_harvest_bag) },
			100
		));

		// エーテル炉
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.gorgeous_lamp),
			new Object[] { new ItemStack(BlockInit.glow_lamp), new ItemStack(ItemInit.divine_crystal, 4), new OreItems("ingotIron", 12), new OreItems("ironbar", 4) },
			new ItemStack[] { new ItemStack(BlockInit.aether_furnace_bottom) },
			20000
		));

		// ツールリペアラー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.divine_crystal, 2),
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 4), new ItemStack(BlockInit.pillar_stone, 8), new ItemStack(ItemInit.alternative_ingot, 8) },
			new ItemStack[] { new ItemStack(BlockInit.tool_repair) },
			100000
		));

		// マギア・リライト
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.deus_crystal),
			new Object[] { new ItemStack(ItemInit.cosmic_crystal_shard, 8), new ItemStack(ItemInit.pure_crystal, 8), new ItemStack(BlockInit.pillar_stone_w, 16),
					new ItemStack(ItemInit.cosmos_light_ingot, 8), new ItemStack(ItemInit.magicmeal, 24), new ItemStack(ItemInit.mystical_page, 4) },
			new ItemStack[] { new ItemStack(BlockInit.magia_rewrite) },
			500000
		));

		// マギア・リインカネーション
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.magia_rewrite),
			new Object[] { new ItemStack(ItemInit.cosmic_crystal, 3), new ItemStack(ItemInit.deus_crystal, 5), new ItemStack(BlockInit.magiaflux_block, 15),
					new ItemStack(BlockInit.cosmos_light_block, 4), new ItemStack(ItemInit.mystical_page, 8), new ItemStack(ItemInit.magic_book_scarlet) },
			new ItemStack[] { new ItemStack(BlockInit.magia_reincarnation) },
			true,
			5000000
		));

		// 収納付き作業台
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Blocks.CRAFTING_TABLE),
			new Object[] { new OreItems("chestWood"), new ItemStack(ItemInit.divine_crystal, 4), new ItemStack(ItemInit.mf_bottle, 12), new ItemStack(ItemInit.mf_sbottle, 8) },
			new ItemStack[] { new ItemStack(BlockInit.workbench_storage) },
			20000
		));

		// エーテルエンチャントテーブル
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Blocks.ENCHANTING_TABLE),
			new Object[] { new OreItems("blockGlass", 16), new ItemStack(ItemInit.divine_crystal, 4), new ItemStack(Items.BOOK, 16) },
			new ItemStack[] { new ItemStack(BlockInit.aether_enchanttable) },
			20000
		));

		// エーテルホッパー
		recipe.addRecipe(new PedalRecipes(
			"hopper",
			new Object[] { new OreItems("chestWood", 2), new ItemStack(ItemInit.aether_crystal, 4) },
			new ItemStack[] { new ItemStack(BlockInit.aether_hopper, 2) },
			2000
		));

		// マギアランタン
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.twilightlight),
			new Object[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.alternative_ingot, 2), new OreItems("blockGlass", 4), new ItemStack(ItemInit.mf_sbottle, 4) },
			new ItemStack[] { new ItemStack(BlockInit.magia_lantern) },
			2000
		));

		// マギアライト
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.magia_lantern),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 3), new ItemStack(ItemInit.alternative_ingot, 8), new OreItems("smLog", 16), new ItemStack(ItemInit.mf_bottle, 6) },
			new ItemStack[] { new ItemStack(BlockInit.magia_light) },
			true,
			100000
		));

		// パラレル・インターフィアー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Items.BOOK),
			new Object[] { new ItemStack(ItemInit.sugarbell, 4), new ItemStack(ItemInit.blank_page, 2), new ItemStack(ItemInit.divine_crystal, 2)
					, new ItemStack(Blocks.CARPET, 4, 5), new ItemStack(Items.ENDER_EYE, 4) , new ItemStack(ItemInit.mf_bottle, 2)
					, new ItemStack(ItemInit.witch_tears), new ItemStack(Blocks.ENCHANTING_TABLE) },
			new ItemStack[] { new ItemStack(BlockInit.parallel_interfere) },
			10000
		));

		// スターダスト・ウィッシュ
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Items.BOOK, 16),
			new Object[] { new ItemStack(ItemInit.cosmic_crystal_shard, 16), new OreItems("chestWood", 16), new ItemStack(ItemInit.pure_crystal, 2)
					, new ItemStack(ItemInit.mf_magiabottle), new ItemStack(ItemInit.mystical_page, 2), "magicBookCosmic" },
			new ItemStack[] { new ItemStack(BlockInit.stardust_wish) },
			100000
		));

		// ノートPC
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Items.IRON_NUGGET, 4),
			new Object[] { new ItemStack(ItemInit.aether_crystal), new OreItems("listAllseed", 16) },
			new ItemStack[] { new ItemStack(BlockInit.note_pc) },
			500
		));

		// スマホ
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.note_pc),
			new Object[] { new ItemStack(ItemInit.divine_crystal), new ItemStack(Items.IRON_INGOT, 4), new OreItems("listAllseed", 32) },
			new ItemStack[] { new ItemStack(ItemInit.sm_phone) },
			3000
		));

		// ディスプレイ
		recipe.addRecipe(new PedalRecipes(
			"notepc",
			new Object[] { new ItemStack(Blocks.EMERALD_BLOCK), new ItemStack(ItemInit.pure_crystal, 2) },
			new ItemStack[] { new ItemStack(BlockInit.sm_display) },
			true,
			20000
		));

		// マギアストレージ1
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.treasure_chest),
			new Object[] { new ItemStack(ItemInit.aether_crystal, 4), new ItemStack(Items.IRON_INGOT, 8), new ItemStack(Blocks.CHEST, 16) },
			new ItemStack[] { new ItemStack(BlockInit.magia_storage_1) },
			true,
			20000
		));

		// マギアストレージ2
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.magia_storage_1),
			new Object[] { new ItemStack(ItemInit.divine_crystal, 4), new ItemStack(Items.IRON_INGOT, 16), new ItemStack(Blocks.CHEST, 24) },
			new ItemStack[] { new ItemStack(BlockInit.magia_storage_2) },
			true,
			80000
		));

		// マギアストレージ3
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.magia_storage_2),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 4), new ItemStack(Items.GOLD_INGOT, 4), new ItemStack(Items.IRON_INGOT, 24), new ItemStack(Blocks.CHEST, 32) },
			new ItemStack[] { new ItemStack(BlockInit.magia_storage_3) },
			true,
			200000
		));

		// マギアストレージ4
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.magia_storage_3),
			new Object[] { new ItemStack(ItemInit.deus_crystal, 2), new ItemStack(Items.GOLD_INGOT, 8), new ItemStack(Items.IRON_INGOT, 32), new ItemStack(Blocks.CHEST, 48) },
			new ItemStack[] { new ItemStack(BlockInit.magia_storage_4) },
			true,
			800000
		));

		// マギアストレージ5
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.magia_storage_4),
			new Object[] { new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(Items.GOLD_INGOT, 12), new ItemStack(Items.IRON_INGOT, 40), new ItemStack(Blocks.CHEST, 64) },
			new ItemStack[] { new ItemStack(BlockInit.magia_storage_5) },
			true,
			3000000
		));

		// 夜の帳
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.cotton_cloth, 36),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 3), new ItemStack(ItemInit.poison_bottle, 4), new ItemStack(ItemInit.tiny_feather, 4),
					 new ItemStack(ItemInit.unmeltable_ice, 4), new ItemStack(ItemInit.electronic_orb, 4), new ItemStack(ItemInit.stray_soul, 4),
					 new ItemStack(ItemInit.mystical_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.veil_darkness) },
			10000
		));

		// カフェショップの服
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.cotton_cloth, 12),
			new Object[] { new OreItems("chestWood", 4), new ItemStack(Items.IRON_INGOT, 4) },
			new ItemStack[] { new ItemStack(ItemInit.shop_uniform) },
			500
		));

		// エーテルローブ
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.cotton_cloth, 24),
			new Object[] { new ItemStack(ItemInit.aether_crystal, 6), new ItemStack(Items.GOLD_INGOT, 4), new OreItems("slimeball", 16) },
			new ItemStack[] { new ItemStack(ItemInit.magicians_robe) },
			5000
		));

		// ポーチ
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Items.LEATHER, 16),
			new Object[] { new OreItems("chestWood"), new ItemStack(ItemInit.aether_crystal, 4), new OreItems("string", 8), new ItemStack(Items.IRON_NUGGET), new ItemStack(Blocks.WOODEN_BUTTON) },
			new ItemStack[] { new ItemStack(ItemInit.magicians_pouch) },
			5000
		));

		// ハーネス
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Items.FEATHER, 8),
			new Object[] { new ItemStack(ItemInit.cotton_cloth, 8), new ItemStack(ItemInit.pure_crystal, 4), new OreItems("string", 8), new ItemStack(ItemInit.mystical_page) },
			new ItemStack[] { new ItemStack(ItemInit.angel_harness) },
			40000
		));

		// エーテル・ブーツ
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.alternative_ingot, 8),
			new Object[] { new ItemStack(Items.IRON_INGOT, 4), new ItemStack(ItemInit.aether_crystal, 4), new ItemStack(ItemInit.cotton_cloth, 4) },
			new ItemStack[] { new ItemStack(ItemInit.aether_boot) },
			10000
		));

		// チョーカー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.aether_crystal, 4),
			new Object[] { new ItemStack(Items.IRON_INGOT, 6), new ItemStack(Items.GOLD_NUGGET, 4), new ItemStack(ItemInit.b_mf_bottle, 2) },
			new ItemStack[] { new ItemStack(ItemInit.aether_choker) },
			10000
		));

		// ワープブロック
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.witch_tears),
			new Object[] { new ItemStack(ItemInit.clero_petal, 8), new ItemStack(BlockInit.alt_block) },
			new ItemStack[] { new ItemStack(BlockInit.warp_block, 2) },
			2000
		));

		// 杖の台座
		recipe.addRecipe(new PedalRecipes(
			new OreItems("stone", 4),
			new Object[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(Items.IRON_INGOT, 4) },
			new ItemStack[] { new ItemStack(BlockInit.wand_pedal) }
		));

		// 霧雨の勿忘草の花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.dm_flower, 8),
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 4), new ItemStack(ItemInit.alternative_ingot, 2), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.mfpot) },
			10000
		));

		// 黄昏時の夢百合草の花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.mfpot),
			new Object[] { new ItemStack(BlockInit.twilight_alstroemeria), new ItemStack(ItemInit.pure_crystal), new ItemStack(ItemInit.cosmos_light_ingot, 2), new ItemStack(ItemInit.mf_magiabottle, 2) },
			new ItemStack[] { new ItemStack(BlockInit.twilightalstroemeria_pot) },
			400000
		));

		// スノードロップの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.snowdrop, 4),
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 4), new ItemStack(ItemInit.aether_crystal, 6), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.snowdrop_pot) },
			10000
		));

		// トルコキキョウの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.turkey_balloonflower, 16),
			new Object[] { new ItemStack(ItemInit.divine_crystal), new ItemStack(ItemInit.mf_sbottle, 4), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.turkey_balloonflower_pot) },
			20000
		));

		// 群青の薔薇の花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.ultramarine_rose, 16),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 2), new ItemStack(ItemInit.mf_magiabottle), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.ultramarine_rose_pot) },
			100000
		));

		// ソリッドスターの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.solid_star, 16),
			new Object[] { new ItemStack(ItemInit.divine_crystal), new ItemStack(ItemInit.mf_bottle, 2), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.solid_star_pot) },
			200000
		));

		// ジニアの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.zinnia, 16),
			new Object[] { new ItemStack(ItemInit.divine_crystal), new ItemStack(ItemInit.mf_magiabottle), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.zinnia_pot) },
			200000
		));

		// カーネーションの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.carnation_crayola, 8),
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 4), new ItemStack(ItemInit.divine_crystal), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.carnation_crayola_pot) },
			10000
		));

		// ハイドラの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.hydrangea, 8),
			new Object[] { new ItemStack(ItemInit.divine_crystal), new ItemStack(ItemInit.mf_sbottle, 6), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.hydrangea_pot) },
			10000
		));

		// クリスマスローズ・エリックスミシィの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.christmarose_ericsmithii, 8),
			new Object[] { new ItemStack(ItemInit.divine_crystal), new ItemStack(ItemInit.mf_sbottle, 4), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.christmarose_ericsmithii_pot) },
			10000
		));

		// マギア・ドロワー
		recipe.addRecipe(new PedalRecipes(
			"woodChest",
			new Object[] { new ItemStack(ItemInit.mf_bottle), new ItemStack(ItemInit.mysterious_page, 3), new OreItems("chestWood", 2) },
			new ItemStack[] { new ItemStack(BlockInit.gravity_chest) },
			true,
			10000
		));

		// 改良型MFテーブル
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.mftable),
			new Object[] { new ItemStack(ItemInit.mysterious_page, 8), new ItemStack(ItemInit.sannyflower_petal, 16), new ItemStack(ItemInit.moonblossom_petal, 16),
					new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.witch_tears) },
			new ItemStack[] { new ItemStack(BlockInit.advanced_mftable) },
			true,
			20000
		));

		// 改良型MFチェンジャー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.mfchanger),
			new Object[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.magicmeal, 16), new ItemStack(Items.ENDER_PEARL, 4), new ItemStack(ItemInit.witch_tears) },
			new ItemStack[] { new ItemStack(BlockInit.advanced_mfchanger) },
			true,
			20000
		));

		// 改良型MFタンク
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.mftank),
			new Object[] { new ItemStack(BlockInit.sugarglass, 64), new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.witch_tears) },
			new ItemStack[] { new ItemStack(BlockInit.advanced_mftank) },
			true,
			20000
		));

		// 改良型エーテル炉
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.aether_furnace_bottom),
			new Object[] { new ItemStack(BlockInit.glow_lamp, 4), new ItemStack(ItemInit.pure_crystal, 4), new ItemStack(Items.GOLD_INGOT, 12)
					, new OreItems("ironbar", 16), new ItemStack(ItemInit.witch_tears) },
			new ItemStack[] { new ItemStack(BlockInit.advanced_aether_furnace_bottom) },
			true,
			200000
		));

		// 改良型魔法流かまど
		recipe.addRecipe(new PedalRecipes(
			"mffurnace",
			new Object[] { new ItemStack(ItemInit.pure_crystal, 6), new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.mf_magiabottle), "magicBookCosmic" },
			new ItemStack[] { new ItemStack(BlockInit.advanced_mffurnace_off) },
			true,
			300000
		));

		// マスターマギアタンク
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.advanced_mftank),
			new Object[] { new ItemStack(BlockInit.magicbarrier_off, 64), new ItemStack(ItemInit.pure_crystal, 8), new ItemStack(ItemInit.mystical_page, 2), "magicBookCosmic" },
			new ItemStack[] { new ItemStack(BlockInit.mm_tank) },
			true,
			200000
		));

		// マスターマギアテーブル
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.advanced_mftable),
			new Object[] { new ItemStack(BlockInit.prism_log, 64), new ItemStack(ItemInit.pure_crystal, 8), new ItemStack(ItemInit.mystical_page, 2), "magicBookCosmic" },
			new ItemStack[] { new ItemStack(BlockInit.mm_table) },
			true,
			200000
		));

		// ピュアクリスタルチョーカー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.aether_choker),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 4), new ItemStack(ItemInit.alternative_ingot, 2), new ItemStack(ItemInit.mf_magiabottle, 2) },
			new ItemStack[] { new ItemStack(ItemInit.pure_choker) },
			true,
			100000
		));

		// デウスクリスタルチョーカー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.pure_choker),
			new Object[] { new ItemStack(ItemInit.deus_crystal), new ItemStack(ItemInit.cosmos_light_ingot, 2), new ItemStack(ItemInit.mf_magiabottle, 8), "magicBookCosmic" },
			new ItemStack[] { new ItemStack(ItemInit.deus_choker) },
			true,
			500000
		));

		// マスターマギアポーチ
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.magicians_pouch),
			new Object[] { new ItemStack(ItemInit.deus_crystal), new ItemStack(ItemInit.mystical_page, 4), new ItemStack(ItemInit.mf_magiabottle), "magicBookCosmic" },
			new ItemStack[] { new ItemStack(ItemInit.master_magia_pouch) },
			true,
			200000
		));

		// マギア・サクセサー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.magicbarrier_off, 8),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 8), new ItemStack(BlockInit.pillar_stone_w, 8), new ItemStack(ItemInit.cosmos_light_ingot, 4) },
			new ItemStack[] { new ItemStack(BlockInit.magia_successor) },
			300000
		));

		// アルカナ・テーブル
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.peach_log, 8),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 6), new ItemStack(ItemInit.cotton_cloth, 8), new ItemStack(ItemInit.blank_page, 4), new ItemStack(Items.FEATHER, 2), new OreItems("dyeBlack", 2) },
			new ItemStack[] { new ItemStack(BlockInit.arcane_table) },
			300000
		));

		// マギアアクセラレータ
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.ami_glass, 8),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 6), new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(BlockInit.pillar_stone_w, 6), new ItemStack(ItemInit.cosmos_light_ingot, 4) },
			new ItemStack[] { new ItemStack(BlockInit.magia_accelerator) },
			400000
		));

		// 創造の祭壇
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.pedestal_creat),
			new Object[] { new ItemStack(ItemInit.mf_magiabottle, 1), new ItemStack(ItemInit.divine_crystal, 3), new OreItems("stone", 16) },
			new ItemStack[] { new ItemStack(BlockInit.altar_creat) },
			0
		));

		// 創星の祭壇
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.altar_creat),
			new Object[] { new ItemStack(BlockInit.magiaflux_block, 1), new ItemStack(ItemInit.pure_crystal, 4), new OreItems("stone", 32) },
			new ItemStack[] { new ItemStack(BlockInit.altar_creation_star) },
			0
		));

		// スターライトワンド
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.cosmos_light_ingot, 4),
			new Object[] { new ItemStack(ItemInit.alternative_ingot, 2), new ItemStack(ItemInit.witch_tears) },
			new ItemStack[] { new ItemStack(ItemInit.startlight_wand) },
			100000
		));

		// エーテルハンマー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.alt_pick, 1, 32767),
			new Object[] { new ItemStack(ItemInit.cosmos_light_ingot, 4), new ItemStack(ItemInit.deus_crystal), new ItemStack(ItemInit.magic_book_scarlet) },
			new ItemStack[] { new ItemStack(ItemInit.aether_hammer) },
			true,
			100000
		));

		// 上位魔術書
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.magic_book),
			new Object[] { new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.pure_crystal, 4), new ItemStack(ItemInit.mf_magiabottle), new ItemStack(ItemInit.mf_bottle, 3), new ItemStack(ItemInit.mf_sbottle, 8),
					new ItemStack(ItemInit.mystical_page), new ItemStack(ItemInit.mysterious_page, 4), new ItemStack(ItemInit.witch_tears) },
			new ItemStack[] { new ItemStack(ItemInit.magic_book_cosmic) },
			200000
		));

		// 最上位魔術書
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.magic_book_cosmic),
			new Object[] { new ItemStack(ItemInit.aether_crystal_shard, 4), new ItemStack(ItemInit.aether_crystal), new ItemStack(ItemInit.divine_crystal), new ItemStack(ItemInit.pure_crystal),
					new ItemStack(ItemInit.deus_crystal), new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.cosmic_crystal) },
			new ItemStack[] { new ItemStack(ItemInit.magic_book_scarlet) },
			1000000
		));

		// 不浄の炎
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.fire_nasturtium_petal, 16),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 4), new ItemStack(ItemInit.mf_magiabottle, 3), new ItemStack(ItemInit.cosmos_light_ingot, 2), new ItemStack(ItemInit.accebag, 3), "magicBookCosmic" },
			new ItemStack[] { new ItemStack(ItemInit.unyielding_fire) },
			200000
		));

		// フロストチェーン
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.unmeltable_ice, 16),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 4), new ItemStack(ItemInit.mf_magiabottle, 3), new ItemStack(ItemInit.cosmos_light_ingot, 2), new ItemStack(ItemInit.accebag, 3), "magicBookCosmic" },
			new ItemStack[] { new ItemStack(ItemInit.frosted_chain) },
			200000
		));

		// ホーリーチャーム
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.prizmium, 16),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 4), new ItemStack(ItemInit.mf_magiabottle, 3), new ItemStack(ItemInit.cosmos_light_ingot, 2), new ItemStack(ItemInit.accebag, 3), "magicBookCosmic" },
			new ItemStack[] { new ItemStack(ItemInit.holly_charm) },
			200000
		));

		// 風のレリーフ
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.tiny_feather, 16),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 4), new ItemStack(ItemInit.mf_magiabottle, 3), new ItemStack(ItemInit.cosmos_light_ingot, 2), new ItemStack(ItemInit.accebag, 3), "magicBookCosmic" },
			new ItemStack[] { new ItemStack(ItemInit.wind_relief) },
			200000
		));

		Map<Item, String> aetherRecipe = new HashMap<>();
		aetherRecipe.put(ItemInit.aether_wand_r, "dyeRed");
		aetherRecipe.put(ItemInit.aether_wand_g, "dyeGreen");
		aetherRecipe.put(ItemInit.aether_wand_b, "dyeBlue");
		aetherRecipe.put(ItemInit.aether_wand_y, "dyeYellow");
		aetherRecipe.put(ItemInit.aether_wand_p, "dyePurple");

		for (Entry<Item, String> map : aetherRecipe.entrySet()) {

			// エーテルワンド
			recipe.addRecipe(new PedalRecipes(
				"aetherWand",
				new Object[] { new OreItems(map.getValue(), 2), new ItemStack(ItemInit.magicmeal, 2) },
				new ItemStack[] { new ItemStack(map.getKey()) },
				true,
				2000
			));
		}

		Map<Item, String> divineRecipe = new HashMap<>();
		divineRecipe.put(ItemInit.divine_wand_r, "dyeRed");
		divineRecipe.put(ItemInit.divine_wand_g, "dyeGreen");
		divineRecipe.put(ItemInit.divine_wand_b, "dyeBlue");
		divineRecipe.put(ItemInit.divine_wand_y, "dyeYellow");
		divineRecipe.put(ItemInit.divine_wand_p, "dyePurple");

		for (Entry<Item, String> map : divineRecipe.entrySet()) {

			// ディバインワンド
			recipe.addRecipe(new PedalRecipes(
				"divineWand",
				new Object[] { new OreItems(map.getValue(), 2), new ItemStack(ItemInit.aether_crystal, 2) },
				new ItemStack[] { new ItemStack(map.getKey()) },
				true,
				5000
			));
		}

		Map<Item, String> pureRecipe = new HashMap<>();
		pureRecipe.put(ItemInit.purecrystal_wand_r, "dyeRed");
		pureRecipe.put(ItemInit.purecrystal_wand_g, "dyeGreen");
		pureRecipe.put(ItemInit.purecrystal_wand_b, "dyeBlue");
		pureRecipe.put(ItemInit.purecrystal_wand_y, "dyeYellow");
		pureRecipe.put(ItemInit.purecrystal_wand_p, "dyePurple");

		for (Entry<Item, String> map : pureRecipe.entrySet()) {

			// ピュアワンド
			recipe.addRecipe(new PedalRecipes(
				"purecrystalWand",
				new Object[] { new OreItems(map.getValue(), 2), new ItemStack(ItemInit.divine_crystal, 2) },
				new ItemStack[] { new ItemStack(map.getKey()) },
				true,
				8000
			));
		}

		Map<Item, String> deusRecipe = new HashMap<>();
		deusRecipe.put(ItemInit.deuscrystal_wand_r, "dyeRed");
		deusRecipe.put(ItemInit.deuscrystal_wand_g, "dyeGreen");
		deusRecipe.put(ItemInit.deuscrystal_wand_b, "dyeBlue");
		deusRecipe.put(ItemInit.deuscrystal_wand_y, "dyeYellow");
		deusRecipe.put(ItemInit.deuscrystal_wand_p, "dyePurple");

		for (Entry<Item, String> map : deusRecipe.entrySet()) {

			// デウスワンド
			recipe.addRecipe(new PedalRecipes(
				"deuscrystalWand",
				new Object[] { new OreItems(map.getValue(), 2), new ItemStack(ItemInit.pure_crystal, 2) },
				new ItemStack[] { new ItemStack(map.getKey()) },
				true,
				50000
			));
		}
	}
}
