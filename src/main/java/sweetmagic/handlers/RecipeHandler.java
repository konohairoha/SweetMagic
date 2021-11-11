package sweetmagic.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import sweetmagic.SweetMagicCore;
import sweetmagic.config.SMConfig;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.recipe.JeiRecipeMFTank;
import sweetmagic.recipe.RecipeNBTExtend;
import sweetmagic.recipe.RecipeRegisterHelper;
import sweetmagic.util.SMUtil;

public class RecipeHandler {

	private static final String MODID =  SweetMagicCore.MODID;
	private static final ResourceLocation MODSRC =  new ResourceLocation(MODID);

    //レシピ定数List格納
	public static void registerCrafting() {

		String MODID = SweetMagicCore.MODID;
		Ingredient recipeBook = getIngred("recipeBook");
		Ingredient alt_ingot = getIngred(ItemInit.alternative_ingot);
		Ingredient cobble = getIngred("cobblestone");
		Ingredient stickWood = getIngred("stickWood");
		Ingredient foodRice = getIngred("foodRice");
		Ingredient dustSalt = getIngred("dustSalt");
		Ingredient dry_seaweed = getIngred(ItemInit.dry_seaweed);
		Ingredient compost_drit = getIngred(BlockInit.compost_drit);
		Ingredient treeSapling = getIngred("treeSapling");
		Ingredient iron_bars = getIngred("ironbar");
		Ingredient ironNugget = getIngred(Items.IRON_NUGGET);
		Ingredient smPlanks = getIngred("smPlanks");
		Ingredient cosmos_light_ingot = getIngred(ItemInit.cosmos_light_ingot);
		Ingredient blockGlass = getIngred("blockGlass");
		Ingredient magicBook = getIngred("magicBook");
		Ingredient glowStoneDust = getIngred(Items.GLOWSTONE_DUST);
		Ingredient iron_chain = getIngred(BlockInit.iron_chain);
		Ingredient dyeRed = getIngred("dyeRed");
		Ingredient dyeBlue = getIngred("dyeBlue");
		Ingredient dyeYellow = getIngred("dyeYellow");
		Ingredient dyeWhite = getIngred("dyeWhite");
		Ingredient dyeLime = getIngred("dyeLime");
		Ingredient dyeBlack = getIngred("dyeBlack");
		Ingredient dyeGreen = getIngred("dyeGreen");
		Ingredient dyeLightGray = getIngred("dyeLightGray");
		Ingredient prismglass = getIngred(BlockInit.prismglass);
		Ingredient sugarglass = getIngred(BlockInit.sugarglass);
		Ingredient prism_planks = getIngred(BlockInit.prism_planks);
		Ingredient sugar = getIngred(Items.SUGAR);
		Ingredient glass = getIngred(Blocks.GLASS);
		Ingredient smLog = getIngred("smLog");
		Ingredient wool = getIngred("wool");
		Ingredient plankWood = getIngred("plankWood");
		Ingredient slabWood = getIngred("slabWood");
		Ingredient dye = getIngred("dye");
		Ingredient feather = getIngred("feather");
		Ingredient paper = getIngred("paper");
		Ingredient chestWood = getIngred("chestWood");
		Ingredient cotton = getIngred(ItemInit.cotton);
		Ingredient slimeball = getIngred("slimeball");
		Ingredient ice = getIngred("ice");
		Ingredient seed = getIngred("listAllseed");
		Ingredient door = getIngred("doorWood");
		Ingredient orange_planks_w = getIngred("orange_planks_w");
		Ingredient smtable = getIngred(BlockInit.smtable);
//		Ingredient ingotIron = getIngred("ingotIron");
		Ingredient dustGlowstone = getIngred("dustGlowstone");
		Ingredient water_bucket = getIngred(Items.WATER_BUCKET);
		Ingredient milk_bucket = getIngred(Items.MILK_BUCKET);
		Ingredient ender_shard = getIngred(ItemInit.ender_shard);
		Ingredient hopper = getIngred("hopper");
		Ingredient stone = getIngred("stone");
		Ingredient book = getIngred(Items.BOOK);
		Ingredient ingotIron = getIngred("ingotIron", "ingotCopper");

		/*草ブロックから粘土*/
		if (SMConfig.help_recipe) {
			addShapedRecipe("help_magicmeal", new ItemStack(ItemInit.magicmeal, 4),
				" X ",
				"XYX",
				" X ",
				'X', sugar,
				'Y', new ItemStack(Items.DYE, 1,15)
			);
		}

		// アルストロメリア
		addShapedRecipe("alstroemeria", new ItemStack(BlockInit.twilight_alstroemeria),
			"SSS",
			"SCM",
			"MMM",
			'S', ItemInit.sannyflower_petal,
			'M', ItemInit.moonblossom_petal,
			'C', Items.CLOCK
		);

		// オルタナティブアックス
		addShapedRecipe("alt_axe", new ItemStack(ItemInit.alt_axe),
			"AA ",
			"AS ",
			" S ",
			'A', ItemInit.alternative_ingot,
			'S', stickWood
		);

		// オルタナティブバケツ
		addShapedRecipe("alt_bucket_2", new ItemStack(ItemInit.alt_bucket),
			"A A",
			" A ",
			'A', ItemInit.alternative_ingot
		);

		// オルタナティブクワ
		addShapedRecipe("alt_hoe", new ItemStack(ItemInit.alt_hoe),
			"AA ",
			" S ",
			" S ",
			'A', ItemInit.alternative_ingot,
			'S', stickWood
		);

		// オルタナティブサイス
		addShapedRecipe("alt_sickle", new ItemStack(ItemInit.alt_sickle),
			"AAA",
			"AS ",
			"S  ",
			'A', ItemInit.alternative_ingot,
			'S', stickWood
		);

		// オルタナティブピッケル
		addShapedRecipe("alt_pick", new ItemStack(ItemInit.alt_pick),
			"AAA",
			" S ",
			" S ",
			'A', ItemInit.alternative_ingot,
			'S', stickWood
		);

		// オルタナティブシザー
		addShapelessRecipe("alt_shears", new ItemStack(ItemInit.alt_shears),
			alt_ingot, alt_ingot
		);

		// オルタナティブシャベル
		addShapedRecipe("alt_shovel", new ItemStack(ItemInit.alt_shovel),
			" A ",
			" S ",
			" S ",
			'A', ItemInit.alternative_ingot,
			'S', stickWood
		);

		// オルタナティブソード
		addShapedRecipe("alt_sword", new ItemStack(ItemInit.alt_sword),
			" A ",
			" A ",
			" S ",
			'A', ItemInit.alternative_ingot,
			'S', stickWood
		);

		// マチェット
		addShapedRecipe("machete", new ItemStack(ItemInit.machete),
			"  A",
			" A ",
			"S  ",
			'A', ItemInit.alternative_ingot,
			'S', stickWood
		);

		// 盗賊ナイフ
		addShapedRecipe("knife_of_thief", new ItemStack(ItemInit.knife_of_thief),
			"  I",
			" II",
			"SF ",
			'I', ingotIron,
			'S', stickWood,
			'F', Items.FEATHER
		);

		// ガイドブック
		addShapelessRecipe( "guide_book", new ItemStack(ItemInit.guide_book),
			book, getIngred(ItemInit.sugarbell)
		);

		// マジックブック
		addShapelessRecipe( "magicbook", new ItemStack(BlockInit.magicbook),
				book, book, book, recipeBook
		);

		// 魔術書
		addShapedRecipe("magic_book", new ItemStack(ItemInit.magic_book),
			"SCS",
			"PBP",
			"SCS",
			'P', ItemInit.blank_page,
			'B', book,
			'C', ItemInit.aether_crystal,
			'S', ItemInit.sugarbell
		);

		// オブマギア
		addShapedRecipe("ob_magia", new ItemStack(BlockInit.obmagia_bottom),
			"CBC",
			"OYO",
			"P P",
			'P', plankWood,
			'Y', ItemInit.aether_crystal,
			'B', magicBook,
			'O', Blocks.OBSIDIAN,
			'C', new ItemStack(Blocks.CARPET, 1, 32767)
		);

		// MFタンク
		addShapedRecipe("mftank", new ItemStack(BlockInit.mftank),
			"SSS",
			"SBS",
			"SSS",
			'S', BlockInit.sugarglass,
			'B', magicBook
		);

		// ボトル
		addShapedRecipe("bottle", new ItemStack(Items.GLASS_BOTTLE, 16),
			"G G",
			" G ",
			'G', blockGlass
		);

		// カフェテーブル
		addShapedRecipe("cafe_table", new ItemStack(BlockInit.cafe_table, 6),
			"WWW",
			" I ",
			"III",
			'W', smPlanks,
			'I', Items.IRON_INGOT
		);

		// カフェチェア
		addShapedRecipe("cafe_chair", new ItemStack(BlockInit.cafe_chair, 6),
			"W  ",
			"WWW",
			"I I",
			'W', smPlanks,
			'I', Items.IRON_INGOT
		);

		// バルコニーテーブル
		addShapedRecipe("balcony_table", new ItemStack(BlockInit.balcony_table, 6),
			"WWW",
			" I ",
			"III",
			'W', smPlanks,
			'I', iron_bars
		);

		// バルコニーチェア
		addShapedRecipe("balcony_chair", new ItemStack(BlockInit.balcony_chair, 6),
			"W  ",
			"WWW",
			"I I",
			'W', smPlanks,
			'I', iron_bars
		);

		// モダンドア
		addShapedRecipe("black_moderndoor", new ItemStack(ItemInit.black_moderndoor),
			" II",
			"BII",
			" II",
			'I', ingotIron,
			'B', recipeBook
		);

		// 製粉機
		addShapedRecipe("flourmill_off", new ItemStack(BlockInit.flourmill_off),
			"HSA",
			"ISA",
			"CSA",
			'H', hopper,
			'S', stone,
			'A', ingotIron,
			'C', chestWood,
			'I', iron_bars
		);

		// フライパン
		addShapedRecipe("frypan_off", new ItemStack(BlockInit.frypan_off),
			"PII",
			" II",
			'I', ingotIron,
			'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE
		);

		// 鍋
		addShapedRecipe("pot_off", new ItemStack(BlockInit.pot_off),
			"BPB",
			"I I",
			"IPI",
			'I', ingotIron,
			'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
			'B', iron_bars
		);

		// ジュースメイカー
		addShapedRecipe("juicemaker_off", new ItemStack(BlockInit.juicemaker_off),
			"GRH",
			"GFP",
			"III",
			'F', Blocks.FURNACE,
			'G', blockGlass,
			'R', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 14),
			'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
			'H', hopper,
			'I', ingotIron
		);

		// オーブン
		addShapedRecipe("oven", new ItemStack(BlockInit.oven),
			"III",
			"GFI",
			"EBI",
			'F', Blocks.FURNACE,
			'G', new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 15),
			'B', Items.BLAZE_ROD,
			'I', ingotIron,
			'E', slimeball
		);

		// コンロ
		addShapedRecipe("stove_off", new ItemStack(BlockInit.stove_off),
			"PBP",
			"IFI",
			"III",
			'F', Blocks.FURNACE,
			'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
			'I', ingotIron,
			'B', iron_bars
		);

		// 熟成瓶
		addShapedRecipe("matured_bottle", new ItemStack(BlockInit.matured_bottle),
			"GPG",
			"GBG",
			"GGG",
			'P', plankWood,
			'G', blockGlass,
			'B', recipeBook
		);

		// MFテーブル
		addShapelessRecipe( "mftable", new ItemStack(BlockInit.mftable),
			getIngred(Blocks.CARPET, 1, 14), getIngred(Blocks.CRAFTING_TABLE), magicBook
		);

		// 白紙のページ
		addShapelessRecipe( "blank_page", new ItemStack(ItemInit.blank_page, 2),
			paper, feather, dye
		);

		// 魔法の布
		addShapelessRecipe( "cotton_cloth", new ItemStack(ItemInit.cotton_cloth),
			cotton, cotton
		);

		// 柱
		addShapelessRecipe( "pillar_stone", new ItemStack(BlockInit.pillar_stone, 4),
			getIngred(Blocks.STONEBRICK, 1, 3), recipeBook
		);

        // 柱
		addShapelessRecipe( "pillar_white", new ItemStack(BlockInit.pillar_stone_w, 4),
			getIngred(Blocks.QUARTZ_BLOCK, 1, 32767), recipeBook
		);

		// 料理皿
		addShapelessRecipe( "plate", new ItemStack(BlockInit.plate, 4),
			iron_bars, iron_bars, recipeBook
		);

		// 木皿
		addShapelessRecipe( "wood_plate", new ItemStack(BlockInit.wood_plate, 4),
			slabWood, slabWood, recipeBook
		);

		// 木皿
		addShapelessRecipe( "iron_plate", new ItemStack(BlockInit.iron_plate, 4),
			ironNugget, ironNugget, recipeBook
		);

		// メニュー表
		addShapelessRecipe( "menyu_list_1", new ItemStack(BlockInit.menu_list, 4),
			 stickWood, stickWood, recipeBook
		);

		// アンティークランタン
		addShapelessRecipe( "antique_lantern", new ItemStack(BlockInit.antique_lantern, 4),
			blockGlass, blockGlass, glowStoneDust, recipeBook
		);

		// アイロンチェーン
		addShapelessRecipe( "iron_chain", new ItemStack(BlockInit.iron_chain, 16),
			ironNugget, ironNugget, ironNugget, recipeBook
		);

		// 鉄格子
		addShapelessRecipe( "iron_bars_1", new ItemStack(Blocks.IRON_BARS, 16),
			iron_chain, iron_chain, iron_chain, iron_chain
		);

		// ガラスコップ
		addShapelessRecipe( "glass_cup", new ItemStack(BlockInit.glass_cup, 4),
			blockGlass, recipeBook
		);

		// フライパン(赤)
		addShapelessRecipe( "frypan_red_off", new ItemStack(BlockInit.frypan_red_off),
			getIngred(BlockInit.frypan_off), dyeRed
		);

		// カウンターチェア
		addShapedRecipe("counter_chair", new ItemStack(BlockInit.counter_chair, 4),
			"WWW",
			"I I",
			'W', smPlanks,
			'I', iron_bars
		);

		// ブレッドウッドトレイ
		addShapedRecipe("bread_wood_tray", new ItemStack(BlockInit.bread_wood_tray, 8),
			"WWW",
			"S S",
			'W', smPlanks,
			'S', stickWood
		);

		// ブレッドバスケット
		addShapedRecipe("bread_basket", new ItemStack(BlockInit.bread_basket, 8),
			"S S",
			"WWW",
			'W', smPlanks,
			'S', stickWood
		);

		// トングスタンド
		addShapedRecipe("tong_stand", new ItemStack(BlockInit.tong_stand, 8),
			"I I",
			"WWW",
			'W', smPlanks,
			'I', ironNugget
		);

		// ショーケース
		addShapedRecipe("showcase", new ItemStack(BlockInit.showcase, 8),
			"G G",
			"WWW",
			'W', smPlanks,
			'G', blockGlass
		);

		// ショーケーススタンド
		addShapelessRecipe( "showcase_stand", new ItemStack(BlockInit.showcase_stand, 4),
			smLog, recipeBook
		);

		// 食パンバスケット
		addShapedRecipe("bread_baskets_1", new ItemStack(BlockInit.bread_baskets, 4),
			"SBS",
			" S ",
			'S', stickWood,
			'B', getIngred("foodBread")
		);

		// ブリキのバケツ
		addShapedRecipe("tinplate_bucket", new ItemStack(BlockInit.tinplate_bucket, 6),
			"CCC",
			"IRI",
			" I ",
			'C', iron_chain,
			'R', recipeBook,
			'I', ironNugget
		);

		// 壁掛けタオル
		addShapedRecipe("wall_towel", new ItemStack(BlockInit.wall_towel, 4),
			"III",
			" C ",
			'C', ItemInit.cotton_cloth,
			'I', iron_bars
		);

		// 換気扇
		addShapedRecipe("ventilation_fan", new ItemStack(BlockInit.ventilation_fan, 2),
			" II",
			" II",
			"PPP",
			'P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
			'I', iron_bars
		);

		// モダンランプ
		addShapedRecipe("modenlanp", new ItemStack(BlockInit.modenlanp, 4),
			" I ",
			" I ",
			" G ",
			'I', iron_bars,
			'G', dustGlowstone
		);

		// パラソルポール
		addShapedRecipe("parasol_pole", new ItemStack(BlockInit.parasol_pole, 16),
			" I ",
			" I ",
			" I ",
			'I', iron_bars
		);

		// アンティークキャンドル
		addShapedRecipe("antique_candle", new ItemStack(BlockInit.antique_candle, 8),
			" T ",
			"GGG",
			" G ",
			'T', Blocks.TORCH,
			'G', Items.GOLD_NUGGET
		);

		// カフェボード
		addShapedRecipe("cafeboard", new ItemStack(BlockInit.cafeboard, 2),
			" S ",
			"SWS",
			'S', stickWood,
			'W', new ItemStack(Blocks.WOOL, 1, 13)
		);

		// 白いカフェボード
		addShapedRecipe("cafeboard_w", new ItemStack(BlockInit.cafeboard_w, 2),
			" S ",
			"SWS",
			'S', stickWood,
			'W', new ItemStack(Blocks.WOOL, 1, 0)
		);

		// ローブ
		Map<Item, String> mapRecipe = new HashMap<>();
		mapRecipe.put(ItemInit.wizard_robe, "dyeBlack");
		mapRecipe.put(ItemInit.feary_robe, "dyeGreen");
		mapRecipe.put(ItemInit.windine_robe, "dyeBlue");
		mapRecipe.put(ItemInit.ifrite_robe, "dyeRed");
		mapRecipe.put(ItemInit.sandryon_robe, "dyeYellow");

		for (Entry<Item, String> map : mapRecipe.entrySet()) {

			// ローブ
			Item robe = map.getKey();

			RecipeHandler.addRecipe(robe.getUnlocalizedName(),
				new RecipeNBTExtend(new ResourceLocation(MODID, robe.getUnlocalizedName()),
				new ItemStack(robe),
					"APA",
					"DRD",
					"APA",
					'D', map.getValue(),
					'P', ItemInit.pure_crystal,
					'A', "magicAccessori",
					'R', "magicians_grobe"
				)
			);
		}

		// カフェキッチンシンク
		RecipeHandler.addRecipe("cafe_kitchen_sink",
			new RecipeNBTExtend(new ResourceLocation(MODID, "cafe_kitchen_sink"),
			new ItemStack(BlockInit.cafe_kitchen_sink),
				"III",
				" C ",
				'I', Items.IRON_NUGGET,
				'C', BlockInit.cafe_kitchen_table
			)
		);

		// シュガーベルの種
		addShapelessRecipe( "sugarbell_seed", new ItemStack(ItemInit.sugarbell_seed, 4),
			getIngred("listAllseed"), getIngred("dirt")
		);

		// 鮭おにぎり
		addShapelessRecipe( "riceball_salmon", new ItemStack(ItemInit.riceball_salmon, 3),
			foodRice, foodRice,
			dry_seaweed, getIngred(Items.COOKED_FISH, 1, 1)
		);

		// 塩おにぎり
		addShapelessRecipe( "foodRice", new ItemStack(ItemInit.riceball_salt, 3),
			foodRice, foodRice,
			dry_seaweed, dustSalt
		);

		// flagstone
		addShapelessRecipe( "flagstone", new ItemStack(BlockInit.flagstone, 6),
			cobble, cobble, cobble, recipeBook
		);

		// シュガーガラス
		addShapelessRecipe( "sugarglass", new ItemStack(BlockInit.sugarglass, 2),
			sugar, glass
		);

		// 遮光シュガーガラス
		addShapelessRecipe( "shading_sugarglass", new ItemStack(BlockInit.shading_sugarglass, 4),
			sugarglass, cobble, cobble, cobble
		);

		// プリズムガラス
		addShapelessRecipe( "prismglass", new ItemStack(BlockInit.prismglass, 4),
			sugarglass, sugarglass,
			prism_planks, prism_planks
		);

		// 遮光プリズムガラス
		addShapelessRecipe( "shading_prismglass", new ItemStack(BlockInit.shading_prismglass, 4),
			prismglass, cobble, cobble, cobble
		);

		// コズモライトブロック
		addShapelessRecipe( "cosmos_light_ingot,", new ItemStack(BlockInit.cosmos_light_block),
			cosmos_light_ingot,
			cosmos_light_ingot,
			cosmos_light_ingot,
			cosmos_light_ingot,
			cosmos_light_ingot,
			cosmos_light_ingot,
			cosmos_light_ingot,
			cosmos_light_ingot,
			cosmos_light_ingot
		);

		// 水コップ
		addShapelessRecipe( "watercup_0,", new ItemStack(ItemInit.watercup, 64),
			water_bucket,
			water_bucket,
			water_bucket,
			water_bucket,
			water_bucket,
			water_bucket,
			water_bucket,
			water_bucket
		);

		// 牛乳パック
		addShapelessRecipe( "milk_pack_0,", new ItemStack(ItemInit.milk_pack, 64),
			milk_bucket,
			milk_bucket,
			milk_bucket,
			milk_bucket,
			milk_bucket,
			milk_bucket,
			milk_bucket,
			milk_bucket
		);

		// エンダーパール
		addShapelessRecipe( "ender_pearl,", new ItemStack(Items.ENDER_PEARL),
			ender_shard,
			ender_shard,
			ender_shard,
			ender_shard,
			ender_shard,
			ender_shard,
			ender_shard,
			ender_shard,
			ender_shard
		);

		// 木の棒
		addShapelessRecipe( "smlog_stick", new ItemStack(Items.STICK, 16),
			smLog, smLog
		);

		// チェスト
		addShapedRecipe("chest", new ItemStack(Blocks.CHEST, 4),
			"LLL",
			"L L",
			"LLL",
			'L', smLog
		);

		// 木の箱
		addShapedRecipe("woodbox", new ItemStack(BlockInit.woodbox, 8),
			"SRS",
			"SPS",
			'S', stickWood,
			'R', recipeBook,
			'P', plankWood
		);

		// 2パネドア
		addShapedRecipe("brown_2paneldoor", new ItemStack(ItemInit.brown_2paneldoor, 8),
			" LL",
			" LL",
			" LL",
			'L', BlockInit.lemon_planks
		);

		// アーチドア
		addShapedRecipe("brown_arch_door", new ItemStack(ItemInit.brown_arch_door, 8),
			" PP",
			" PP",
			"RPP",
			'P', plankWood,
			'R', recipeBook
		);

		// アーチドア
		addShapedRecipe("brown_arch_plantdoor", new ItemStack(ItemInit.brown_arch_plantdoor, 8),
			" PP",
			"RPP",
			" PP",
			'P', plankWood,
			'R', recipeBook
		);

		// エレガントドア
		addShapedRecipe("brown_elegantdoor", new ItemStack(ItemInit.brown_elegantdoor, 8),
			" SS",
			" PP",
			" PP",
			'P', plankWood,
			'S', stickWood
		);

		// 緑の染料
		addShapelessRecipe( "dyeGreen", new ItemStack(Items.DYE, 2, 4),
			dyeBlue, dyeYellow
		);

		// イチゴ
		addShapelessRecipe( "strawberry", new ItemStack(ItemInit.strawberry, 4),
			seed, dyeRed, dyeRed, dyeLime
		);

		// イチゴ
		addShapelessRecipe( "vannila_pods", new ItemStack(ItemInit.vannila_pods, 4),
			seed, dyeLightGray, dyeLightGray, dyeBlack
		);

		// オリーブ
		addShapelessRecipe( "olive", new ItemStack(ItemInit.olive, 4),
			seed, dyeLime, dyeLime, dyeGreen
		);

		// エンダーシャード
		addShapelessRecipe( "dyeGreen", new ItemStack(ItemInit.ender_shard, 9),
			getIngred(Items.ENDER_PEARL), recipeBook
		);

		// カラフルな石畳
		addShapelessRecipe( "flagstone_color", new ItemStack(BlockInit.flagstone_color, 12),
			cobble, cobble, cobble, cobble, cobble, cobble, recipeBook
		);

		// かすんだガラス
		addShapelessRecipe( "frosted_glass", new ItemStack(BlockInit.frosted_glass, 4),
			sugarglass, sugarglass, dyeWhite, dyeWhite
		);

		// かすんだライン入りガラス
		addShapelessRecipe( "frosted_glass_line", new ItemStack(BlockInit.frosted_glass_line, 4),
			getIngred(BlockInit.frosted_glass), getIngred(BlockInit.frosted_glass), dyeWhite, dyeWhite
		);

		// 冷蔵庫
		addShapedRecipe("freezer_bottom", new ItemStack(BlockInit.freezer_bottom, 2),
			"ED ",
			"ID ",
			"CD ",
			'D', Items.IRON_DOOR,
			'E', slimeball,
			'I', ice,
			'C', chestWood
		);

		// 白トラップドア
		addShapedRecipe("white_woodtrapdoor", new ItemStack(BlockInit.white_woodtrapdoor, 8),
			" R ",
			"PPP",
			"PPP",
			'R', recipeBook,
			'P', orange_planks_w
		);

		// シンプルドア
		addShapedRecipe("simple_door_1", new ItemStack(ItemInit.simple_door_1, 8),
			"II ",
			"WW ",
			"WW ",
			'I', iron_bars,
			'W', smPlanks
		);

		// 3段ドア
		addShapelessRecipe( "woodgold_3", new ItemStack(ItemInit.woodgold_3, 4),
			door, door, getIngred(Items.GOLD_NUGGET)
		);

		// 3段白ドア
		addShapelessRecipe( "whitewoodgold_3", new ItemStack(ItemInit.whitewoodgold_3, 4),
			door, door, getIngred(Items.IRON_NUGGET)
		);

		// イス
		addShapedRecipe("smchair", new ItemStack(BlockInit.smchair, 4),
			"PPP",
			"SRS",
			'P', plankWood,
			'S', stickWood,
			'R', recipeBook
		);

		// アンティークチェア
		addShapedRecipe("antique_back_chair", new ItemStack(BlockInit.antique_back_chair, 4),
			"  P",
			"WWP",
			"S S",
			'P', slabWood,
			'W', wool,
			'S', stickWood
		);

		// テーブル
		addShapedRecipe("smtable", new ItemStack(BlockInit.smtable, 4),
			"PPP",
			"SRS",
			"S S",
			'P', slabWood,
			'R', recipeBook,
			'S', stickWood
		);

		// ドットテーブル
		addShapelessRecipe( "smtable_dot", new ItemStack(BlockInit.smtable_dot, 4),
			smtable, smtable, wool, dyeRed
		);

		// レーステーブル
		addShapelessRecipe( "smtable_lace", new ItemStack(BlockInit.smtable_lace, 4),
			smtable, smtable, wool, wool
		);

		// 鉄柵
		addShapedRecipe("iron_bars", new ItemStack(Blocks.IRON_BARS, 64),
			"III",
			"III",
			'I', ItemInit.alternative_ingot
		);

		// 街灯
		addShapedRecipe("pole_down", new ItemStack(BlockInit.pole_down, 4),
			" G ",
			" I ",
			" I ",
			'I', iron_bars,
			'G', glowStoneDust
		);

		// グロウランプ
		addShapedRecipe("glow_lamp", new ItemStack(BlockInit.glow_lamp, 2),
			" B ",
			"BGB",
			'B', iron_bars,
			'G', glowStoneDust
		);

		// グロウライト
		addShapedRecipe("glow_light", new ItemStack(BlockInit.glow_light, 8),
			"QIQ",
			"IGI",
			"QIQ",
			'Q', Items.QUARTZ,
			'I', iron_bars,
			'G', Blocks.GLOWSTONE
		);

		// ゴージャスランプ
		addShapedRecipe("gorgeous_lamp", new ItemStack(BlockInit.gorgeous_lamp, 4),
			" B ",
			"BGB",
			" B ",
			'B', iron_bars,
			'G', Blocks.GLOWSTONE
		);

		// ランタン
		addShapelessRecipe( "lantern_side1", new ItemStack(BlockInit.lantern_side1, 4),
			dustGlowstone, ingotIron
		);

		// ブロック、ハーフ、階段をリストに突っ込む
		List<RecipeRegisterHelper> recipeList = new ArrayList<>();
		recipeList.add(new RecipeRegisterHelper(BlockInit.chestnut_planks, BlockInit.chestnut_stairs, BlockInit.chestnut_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.lemon_planks, BlockInit.lemon_stairs, BlockInit.lemon_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.coconut_planks, BlockInit.coconut_stairs, BlockInit.coconut_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.prism_planks, BlockInit.prism_stairs, BlockInit.prism_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.estor_planks, BlockInit.estor_stairs, BlockInit.estor_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.peach_planks, BlockInit.peach_stairs, BlockInit.peach_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.antique_brick_b, BlockInit.antique_brick_stairs_b, BlockInit.antique_brick_slab_b));
		recipeList.add(new RecipeRegisterHelper(BlockInit.flagstone, BlockInit.flagstone_stairs, BlockInit.flagstone_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.flagstone_color, BlockInit.flagstone_color_stairs, BlockInit.flagstone_color_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick, BlockInit.old_brick_stairs, BlockInit.old_brick_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick_r, BlockInit.old_brick_r_stairs, BlockInit.old_brick_r_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick_g, BlockInit.old_brick_g_stairs, BlockInit.old_brick_g_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick_y, BlockInit.old_brick_y_stairs, BlockInit.old_brick_y_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick_l, BlockInit.old_brick_l_stairs, BlockInit.old_brick_l_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick_b, BlockInit.old_brick_b_stairs, BlockInit.old_brick_b_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick_s, BlockInit.old_brick_s_stairs, BlockInit.old_brick_s_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick, BlockInit.longtile_brick_stairs, BlockInit.longtile_brick_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_b, BlockInit.longtile_brick_b_stairs, BlockInit.longtile_brick_b_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_g, BlockInit.longtile_brick_g_stairs, BlockInit.longtile_brick_g_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_y, BlockInit.longtile_brick_y_stairs, BlockInit.longtile_brick_y_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_r, BlockInit.longtile_brick_r_stairs, BlockInit.longtile_brick_r_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_l, BlockInit.longtile_brick_l_stairs, BlockInit.longtile_brick_l_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_p, BlockInit.longtile_brick_p_stairs, BlockInit.longtile_brick_p_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_o, BlockInit.longtile_brick_o_stairs, BlockInit.longtile_brick_o_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_w, BlockInit.longtile_brick_w_stairs, BlockInit.longtile_brick_w_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.whiteline_brick, BlockInit.whiteline_brick_stairs, BlockInit.whiteline_brick_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.whiteline_brick_y, BlockInit.whiteline_brick_y_stairs, BlockInit.whiteline_brick_y_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.whiteline_brick_b, BlockInit.whiteline_brick_b_stairs, BlockInit.whiteline_brick_b_slab));

		// リストの分だけ回す
		for (RecipeRegisterHelper recipe : recipeList ) {

			Block planks = recipe.getPlanks();
			Block stairs = recipe.getStairs();
			Block slab = recipe.getSlab();

			Ingredient slabItem = Ingredient.fromItems(SMUtil.getItemBlock(slab));
			Ingredient stairsItem = Ingredient.fromItems(SMUtil.getItemBlock(stairs));

			// ハーフ→ブロック
			addShapelessRecipe( planks.getUnlocalizedName() + "_0", new ItemStack(planks),
				slabItem, slabItem
			);

			// 階段→ブロック
			addShapelessRecipe( planks.getUnlocalizedName() + "_1", new ItemStack(planks),
				stairsItem, stairsItem, stairsItem, stairsItem
			);

			// ブロック→ハーフ
			addShapedRecipe(slab.getUnlocalizedName(), new ItemStack(slab, 6),
				"BBB",
				'B', planks
			);

			// ブロック→階段
			addShapedRecipe(stairs.getUnlocalizedName(), new ItemStack(stairs, 8),
				"  B",
				" BB",
				"BBB",
				'B', planks
			);
		}

		// ブロック、ハーフ、階段をリストに突っ込む
		List<RecipeRegisterHelper> recipeList2 = new ArrayList<>();
		recipeList2.add(new RecipeRegisterHelper(BlockInit.chestnut_log, BlockInit.chestnut_planks, BlockInit.chestnut_plate, false));
		recipeList2.add(new RecipeRegisterHelper(BlockInit.coconut_log, BlockInit.coconut_planks, BlockInit.coconut_plate, false));
		recipeList2.add(new RecipeRegisterHelper(BlockInit.lemon_log, BlockInit.lemon_planks, BlockInit.lemon_plate, false));
		recipeList2.add(new RecipeRegisterHelper(BlockInit.prism_log, BlockInit.prism_planks, BlockInit.prism_plate, false));
		recipeList2.add(new RecipeRegisterHelper(BlockInit.estor_log, BlockInit.estor_planks, BlockInit.estor_plate, false));
		recipeList2.add(new RecipeRegisterHelper(BlockInit.peach_log, BlockInit.peach_planks, BlockInit.peach_plate, false));

		for (RecipeRegisterHelper recipe : recipeList2) {

			Block log = recipe.getLog();
			Block planks = recipe.getPlanks();
			Block plate = recipe.getPlate();

			// 原木→ブロック
			addShapelessRecipe( planks.getUnlocalizedName() + "_2",
				new ItemStack(planks, 4),
				new Ingredient[] { getIngred(log) }
			);

			// ブロック→感圧版
			addShapedRecipe(plate.getUnlocalizedName() + "_0", new ItemStack(plate, 4),
				"R ",
				"BB",
				'B', planks,
				'R', recipeBook
			);

			// ブロック→感圧版
			addShapedRecipe(plate.getUnlocalizedName() + "_1", new ItemStack(plate, 4),
				"BB",
				"R ",
				'B', planks,
				'R', recipeBook
			);

			// ブロック→感圧版
			addShapedRecipe(plate.getUnlocalizedName() + "_2", new ItemStack(plate, 4),
				"BB",
				'B', planks
			);
		}

		// 完成品、染料をリストに突っ込む
		List<RecipeRegisterHelper> recipeList3 = new ArrayList<>();
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick, "dyeBlue"));
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick_b, "dyeBrown"));
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick_y, "dyeYellow"));
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick_g, "dyeGreen"));
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick_l, "dyeGray"));
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick_r, "dyeRed"));
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick_s, "dyeLightBlue"));

		for (RecipeRegisterHelper recipe : recipeList3) {

			// ブロックとアイテムの取得
			Block stoneBlock = recipe.getStone();
			Ingredient dyeOre = getIngred(recipe.getOre());

			// 素材→古びたレンガ
			addShapelessRecipe( stoneBlock.getUnlocalizedName() + "__0", new ItemStack(stoneBlock, 16),
				cobble, cobble, cobble, cobble, cobble, cobble, cobble,
				dyeOre, recipeBook
			);
		}

		// 板ガラス
		Map<Block, Block> recipeList4 = new HashMap<>();
		recipeList4.put(BlockInit.sugarglass, BlockInit.sugarglass_pane);
		recipeList4.put(BlockInit.shading_sugarglass, BlockInit.shading_sugarglass_pane);
		recipeList4.put(BlockInit.frosted_glass, BlockInit.frosted_glass_pane);
		recipeList4.put(BlockInit.frosted_glass_line, BlockInit.frosted_glass_line_pane);
		recipeList4.put(BlockInit.prismglass, BlockInit.prismglass_pane);
		recipeList4.put(BlockInit.green4panel_glass, BlockInit.green4panel_glass_pane);
		recipeList4.put(BlockInit.brown4panel_glass, BlockInit.brown4panel_glass_pane);
		recipeList4.put(BlockInit.lightbrown4panel_glass, BlockInit.lightbrown4panel_glass_pane);
		recipeList4.put(BlockInit.darkbrown4panel_glass, BlockInit.darkbrown4panel_glass_pane);
		recipeList4.put(BlockInit.ami_glass, BlockInit.ami_glass_pane);
		recipeList4.put(BlockInit.gorgeous_glass, BlockInit.gorgeous_glass_pane);
		recipeList4.put(BlockInit.gorgeous_glass_w, BlockInit.gorgeous_glass_w_pane);

		for (Entry<Block, Block> map : recipeList4.entrySet()) {

			Block pane = map.getValue();

			addShapedRecipe(pane.getUnlocalizedName(), new ItemStack(pane, 16),
				"BBB",
				"BBB",
				'B', map.getKey()
			);
		}

		// 完成品、染料をリストに突っ込む
		List<RecipeRegisterHelper> recipeList5 = new ArrayList<>();
		recipeList5.add(new RecipeRegisterHelper("antique_brick", BlockInit.antique_brick_0, BlockInit.antique_brick_1, BlockInit.antique_brick_2, BlockInit.antique_brick_stairs, BlockInit.antique_brick_slab));
		recipeList5.add(new RecipeRegisterHelper("antique_brick_white", BlockInit.antique_brick_0w, BlockInit.antique_brick_1w, BlockInit.antique_brick_2w, BlockInit.antique_brick_stairs_w, BlockInit.antique_brick_slab_w));
		recipeList5.add(new RecipeRegisterHelper("antique_brick_brown", BlockInit.antique_brick_0l, BlockInit.antique_brick_1l, BlockInit.antique_brick_2l, BlockInit.antique_brick_stairs_l, BlockInit.antique_brick_slab_l));
		recipeList5.add(new RecipeRegisterHelper("antique_brick_green", BlockInit.antique_brick_0g, BlockInit.antique_brick_1g, BlockInit.antique_brick_2g, BlockInit.antique_brick_stairs_g, BlockInit.antique_brick_slab_g));

		// リストの分だけ回す
		for (RecipeRegisterHelper recipe : recipeList5 ) {

			Ingredient brick = getIngred(recipe.getBrick());
			Block brick0 = recipe.getBrick0();
			Block brick1 = recipe.getBrick1();
			Block brick2 = recipe.getBrick2();
			Block stairs = recipe.getStairs();
			Block slab = recipe.getSlab();

			Ingredient slabItem = Ingredient.fromItems(SMUtil.getItemBlock(slab));
			Ingredient stairsItem = Ingredient.fromItems(SMUtil.getItemBlock(stairs));

			// ハーフ→ブロック
			addShapelessRecipe( brick0.getUnlocalizedName() + "_0", new ItemStack(brick0),
				slabItem, slabItem
			);

			// 階段→ブロック
			addShapelessRecipe( brick0.getUnlocalizedName() + "_0", new ItemStack(brick0, 3),
				stairsItem, stairsItem, stairsItem, stairsItem
			);

			// ひび割れ
			addShapelessRecipe( brick1.getUnlocalizedName() + "_2", new ItemStack(brick1, 2),
				brick, brick, recipeBook
			);

			// コケ付き
			addShapelessRecipe( brick2.getUnlocalizedName() + "_3", new ItemStack(brick2, 3),
				brick, brick, brick, recipeBook
			);

			// 無印
			addShapelessRecipe( brick0.getUnlocalizedName() + "_4", new ItemStack(brick0, 4),
				brick, brick, brick, brick, recipeBook
			);

			// ブロック→ハーフ
			addShapedRecipe(slab.getUnlocalizedName(), new ItemStack(slab, 6),
				"BBB",
				'B', brick
			);

			// ブロック→階段
			addShapedRecipe(stairs.getUnlocalizedName(), new ItemStack(stairs, 8),
				"  B",
				" BB",
				"BBB",
				'B', brick
			);
		}

		// 花バスケット
		Map<Block, Block> busketRecipe = new HashMap<>();
		busketRecipe.put(BlockInit.iberis_umbellata, BlockInit.iberis_umbellata_basket);
		busketRecipe.put(BlockInit.campanula, BlockInit.campanula_basket);
		busketRecipe.put(BlockInit.primula_polyansa, BlockInit.primula_polyansa_basket);
		busketRecipe.put(BlockInit.christmas_rose, BlockInit.christmas_rose_basket);
		busketRecipe.put(BlockInit.portulaca, BlockInit.portulaca_basket);
		busketRecipe.put(BlockInit.surfinia, BlockInit.surfinia_basket);
		busketRecipe.put(BlockInit.pansy_blue, BlockInit.pansy_blue_basket);
		busketRecipe.put(BlockInit.pansy_yellowmazenta, BlockInit.pansy_yellowmazenta_basket);

		for (Entry<Block, Block> map : busketRecipe.entrySet()) {

			Block flower = map.getKey();
			Block basket = map.getValue();

			addShapelessRecipe( basket.getUnlocalizedName(), new ItemStack(basket, 4),
				getIngred(flower), stickWood, stickWood, stickWood
			);
		}

		// ウォールラック
		Map<Block, String> wallRecipe = new HashMap<>();
		wallRecipe.put(BlockInit.moden_wallrack, "dyeBrown");
		wallRecipe.put(BlockInit.moden_wallrack_b, "dyeBlack");
		wallRecipe.put(BlockInit.moden_wallrack_l, "dyeWhite");
		wallRecipe.put(BlockInit.cafe_wallrack, "recipeBook");

		for (Entry<Block, String> map : wallRecipe.entrySet()) {

			Block block = map.getKey();

			addShapedRecipe(block.getUnlocalizedName(), new ItemStack(block, 4),
				"PSP",
				"SDS",
				'P', slabWood,
				'S', stickWood,
				'D', map.getValue()
			);
		}

		// ラタンバスケット
		Map<Block, String> r_basketRecipe = new HashMap<>();
		r_basketRecipe.put(BlockInit.rattan_basket_b, "dyeBrown");
		r_basketRecipe.put(BlockInit.rattan_basket_d, "dyeOrange");
		r_basketRecipe.put(BlockInit.rattan_basket_y, "dyeYellow");

		for (Entry<Block, String> map : r_basketRecipe.entrySet()) {

			Block block = map.getKey();

			addShapedRecipe(block.getUnlocalizedName(), new ItemStack(block, 4),
				"IDI",
				"III",
				'I', ItemInit.ine,
				'D', map.getValue()
			);
		}

		// ラタンバスケット
		Map<Block, String> r_chairtRecipe = new HashMap<>();
		r_chairtRecipe.put(BlockInit.rattan_chair_b, "dyeBrown");
		r_chairtRecipe.put(BlockInit.rattan_chair_d, "dyeOrange");
		r_chairtRecipe.put(BlockInit.rattan_chair_y, "dyeYellow");

		for (Entry<Block, String> map : r_chairtRecipe.entrySet()) {

			Block block = map.getKey();

			addShapedRecipe(block.getUnlocalizedName(), new ItemStack(block, 4),
				" DI",
				"III",
				'I', ItemInit.ine,
				'D', map.getValue()
			);
		}

		// 植木鉢
		Map<Ingredient, Block> potRecipe = new HashMap<>();
		potRecipe.put(orange_planks_w, BlockInit.orange_planks_pot_w);
		potRecipe.put(getIngred("orange_planks"), BlockInit.orange_planks_pot);
		potRecipe.put(getIngred("antique_brick"), BlockInit.antique_brick_pot_r);
		potRecipe.put(getIngred("antique_brick_white"), BlockInit.antique_brick_pot_w);
		potRecipe.put(getIngred("antique_brick_brown"), BlockInit.antique_brick_pot_l);
		potRecipe.put(getIngred("antique_brick_green"), BlockInit.antique_brick_pot_g);
		potRecipe.put(getIngred(BlockInit.estor_planks), BlockInit.estor_planks_pot);
		potRecipe.put(getIngred(BlockInit.longtile_brick_l), BlockInit.longtile_brick_pot_l);
		potRecipe.put(getIngred(BlockInit.longtile_brick_o), BlockInit.longtile_brick_pot_o);
		potRecipe.put(getIngred(BlockInit.whiteline_brick), BlockInit.whiteline_brick_pot);
		potRecipe.put(getIngred(BlockInit.whiteline_brick_y), BlockInit.whiteline_brick_pot_y);
		potRecipe.put(getIngred(BlockInit.whiteline_brick_b), BlockInit.whiteline_brick_pot_b);

		for (Entry<Ingredient, Block> map : potRecipe.entrySet()) {

			Block block = map.getValue();

			addShapedRecipe(block.getUnlocalizedName(), new ItemStack(block, 4),
				"BDB",
				" B ",
				'B', map.getKey(),
				'D', compost_drit
			);
		}

		// トラップドア
		Map<Ingredient, Block> trapdoorRecipe = new HashMap<>();
		trapdoorRecipe.put(getIngred("antique_brick"), BlockInit.antique_tdoor_0);
		trapdoorRecipe.put(getIngred("antique_brick_white"), BlockInit.antique_tdoor_0w);
		trapdoorRecipe.put(getIngred("antique_brick_black"), BlockInit.antique_tdoor_b);
		trapdoorRecipe.put(getIngred("antique_brick_brown"), BlockInit.antique_tdoor_l);
		trapdoorRecipe.put(getIngred("antique_brick_green"), BlockInit.antique_tdoor_g);

		trapdoorRecipe.put(getIngred(BlockInit.old_brick), BlockInit.old_tdoor);
		trapdoorRecipe.put(getIngred(BlockInit.old_brick_r), BlockInit.old_tdoor_r);
		trapdoorRecipe.put(getIngred(BlockInit.old_brick_g), BlockInit.old_tdoor_g);
		trapdoorRecipe.put(getIngred(BlockInit.old_brick_y), BlockInit.old_tdoor_y);
		trapdoorRecipe.put(getIngred(BlockInit.old_brick_l), BlockInit.old_tdoor_l);
		trapdoorRecipe.put(getIngred(BlockInit.old_brick_b), BlockInit.old_tdoor_b);
		trapdoorRecipe.put(getIngred(BlockInit.old_brick_s), BlockInit.old_tdoor_s);

		trapdoorRecipe.put(getIngred(BlockInit.longtile_brick_o), BlockInit.longtile_brick_o_tdoor);
		trapdoorRecipe.put(getIngred(BlockInit.longtile_brick_p), BlockInit.longtile_brick_p_tdoor);
		trapdoorRecipe.put(getIngred(BlockInit.longtile_brick_r), BlockInit.longtile_brick_r_tdoor);

		trapdoorRecipe.put(getIngred(BlockInit.longtile_brick_y), BlockInit.longtile_brick_y_tdoor);
		trapdoorRecipe.put(getIngred(BlockInit.longtile_brick_b), BlockInit.longtile_brick_b_tdoor);
		trapdoorRecipe.put(getIngred(BlockInit.longtile_brick_g), BlockInit.longtile_brick_g_tdoor);
		trapdoorRecipe.put(getIngred(BlockInit.longtile_brick), BlockInit.longtile_brick_tdoor);
		trapdoorRecipe.put(getIngred(BlockInit.longtile_brick_l), BlockInit.longtile_brick_l_tdoor);
		trapdoorRecipe.put(getIngred(BlockInit.longtile_brick_w), BlockInit.longtile_brick_w_tdoor);

		trapdoorRecipe.put(getIngred(BlockInit.whiteline_brick), BlockInit.whiteline_brick_tdoor);
		trapdoorRecipe.put(getIngred(BlockInit.whiteline_brick_y), BlockInit.whiteline_brick_y_tdoor);
		trapdoorRecipe.put(getIngred(BlockInit.whiteline_brick_b), BlockInit.whiteline_brick_b_tdoor);

		trapdoorRecipe.put(getIngred(BlockInit.coconut_planks), BlockInit.coconut_trapdoor);

		for (Entry<Ingredient, Block> map : trapdoorRecipe.entrySet()) {

			Block block = map.getValue();

			addShapedRecipe(block.getUnlocalizedName(), new ItemStack(block, 8),
				"BBB",
				"BBB",
				'B', map.getKey()
			);
		}

		// トラップドア
		Map<Ingredient, Block> trapdoorRecipe2 = new HashMap<>();
		trapdoorRecipe2.put(getIngred("dyeWhite"), BlockInit.antique_window_white);
		trapdoorRecipe2.put(getIngred("dyeGreen"), BlockInit.antique_window_green);
		trapdoorRecipe2.put(getIngred("dyeBlack"), BlockInit.antique_window_brown2);
		trapdoorRecipe2.put(getIngred("dyeBrown"), BlockInit.antique_window_brown);

		for (Entry<Ingredient, Block> map : trapdoorRecipe2.entrySet()) {

			Block block = map.getValue();

			addShapedRecipe(block.getUnlocalizedName(), new ItemStack(block, 8),
				" D ",
				"BBB",
				'D', map.getKey(),
				'B', smPlanks
			);
		}

		// 植え込み
		Map<Block, Block> plantRecipe = new HashMap<>();
		plantRecipe.put(BlockInit.chestnut_leaves, BlockInit.chestnut_planting);
		plantRecipe.put(BlockInit.orange_leaves, BlockInit.orange_planting);
		plantRecipe.put(BlockInit.estor_leaves, BlockInit.estor_planting);
		plantRecipe.put(BlockInit.peach_leaves, BlockInit.peach_planting);

		for (Entry<Block, Block> map : plantRecipe.entrySet()) {

			Ingredient plant = getIngred(map.getKey());
			Block leave = map.getValue();

			addShapelessRecipe(leave.getUnlocalizedName(), new ItemStack(leave, 8),
				plant, plant, treeSapling
			);
		}

		// フェンス
		Map<Block, Block> fenceRecipe = new HashMap<>();
		fenceRecipe.put(Blocks.IRON_BARS, BlockInit.white_ironfence);
		fenceRecipe.put(BlockInit.white_ironfence, BlockInit.fance_gothic_white);

		for (Entry<Block, Block> map : fenceRecipe.entrySet()) {

			Block block = map.getValue();

			addShapedRecipe(block.getUnlocalizedName() + "_1", new ItemStack(block, 24),
				"BBB",
				"BBB",
				'B', map.getKey()
			);
		}

		// 木のフェンス　原木、木材、結果1、結果2、結果3
		List<RecipeRegisterHelper> fenceList = new ArrayList<>();
		fenceList.add(new RecipeRegisterHelper(BlockInit.estor_log, BlockInit.estor_planks, BlockInit.log_fence_estor, BlockInit.log_fence_vertical_estor, BlockInit.log_fence_slanted_estor, BlockInit.log_fence_normal_estor));
		fenceList.add(new RecipeRegisterHelper(BlockInit.prism_log, BlockInit.prism_planks, BlockInit.log_fence_prism, BlockInit.log_fence_vertical_prism, BlockInit.log_fence_slanted_prism, BlockInit.log_fence_normal_prism));
		fenceList.add(new RecipeRegisterHelper(BlockInit.peach_log, BlockInit.peach_planks, BlockInit.log_fence_peach, BlockInit.log_fence_vertical_peach, BlockInit.log_fence_slanted_peach, BlockInit.log_fence_normal_peach));

		for (RecipeRegisterHelper recipe : fenceList) {

			Block log = recipe.getLog();
			Block planks = recipe.getPlanks();
			Block fence1 = recipe.getBrick0();
			Block fence2 = recipe.getBrick1();
			Block fence3 = recipe.getBrick2();
			Block fence4 = recipe.getBrick3();

			addShapedRecipe(fence1.getUnlocalizedName(), new ItemStack(fence1, 16),
				"SLS",
				"PLP",
				'L', log,
				'P', planks,
				'S', stickWood
			);

			addShapedRecipe(fence2.getUnlocalizedName(), new ItemStack(fence2, 16),
				"PLS",
				"PLS",
				'L', log,
				'P', planks,
				'S', stickWood
			);

			addShapedRecipe(fence3.getUnlocalizedName(), new ItemStack(fence3, 16),
				"SLP",
				"PLS",
				'L', log,
				'P', planks,
				'S', stickWood
			);

			addShapedRecipe(fence4.getUnlocalizedName(), new ItemStack(fence4, 16),
				"PLP",
				"SLS",
				'L', log,
				'P', planks,
				'S', stickWood
			);
		}

		// カウンターテーブル
		List<RecipeRegisterHelper> counterList = new ArrayList<>();
		counterList.add(new RecipeRegisterHelper(smPlanks, BlockInit.whiteline_brick, BlockInit.counter_table_white_brick));
		counterList.add(new RecipeRegisterHelper(smPlanks, BlockInit.whiteline_brick_y, BlockInit.counter_table_white_brick_y));
		counterList.add(new RecipeRegisterHelper(smPlanks, BlockInit.whiteline_brick_b, BlockInit.counter_table_white_brick_b));
		counterList.add(new RecipeRegisterHelper(smLog, BlockInit.orange_planks_w, BlockInit.counter_table_orange_planks));
		counterList.add(new RecipeRegisterHelper(orange_planks_w, BlockInit.estor_log, BlockInit.counter_table_modan));

		for (RecipeRegisterHelper recipe : counterList) {

			Ingredient base = recipe.getIng();
			Block counter = recipe.getBrick1();

			addShapedRecipe(counter.getUnlocalizedName(), new ItemStack(counter, 16),
				"WWW",
				"BB ",
				"WWW",
				'B', recipe.getBrick0(),
				'W', base
			);
		}

		// オーニングテント
		Map<Ingredient, Block> tentRecipe = new HashMap<>();
		tentRecipe.put(getIngred("dyeBlue"), BlockInit.awning_tent_b);
		tentRecipe.put(getIngred("dyeOrange"), BlockInit.awning_tent_o);
		tentRecipe.put(getIngred("dyePurple"), BlockInit.awning_tent_p);
		tentRecipe.put(getIngred("dyeRed"), BlockInit.awning_tent_r);
		tentRecipe.put(getIngred("dyeLightBlue"), BlockInit.awning_tent_s);

		for (Entry<Ingredient, Block> map : tentRecipe.entrySet()) {

			Block block = map.getValue();

			addShapedRecipe(block.getUnlocalizedName(), new ItemStack(block, 16),
				"  W",
				" WI",
				"WID",
				'D', map.getKey(),
				'W', wool,
				'I', iron_bars
			);
		}

		// 木の道
		Map<Ingredient, Block> pathRecipe = new HashMap<>();
		pathRecipe.put(getIngred(BlockInit.lemon_planks), BlockInit.path_tree_lemon);
		pathRecipe.put(orange_planks_w, BlockInit.path_tree_orange);

		for (Entry<Ingredient, Block> map : pathRecipe.entrySet()) {

			Block block = map.getValue();
			Ingredient ing = map.getKey();

			addShapelessRecipe(block.getUnlocalizedName(), new ItemStack(block, 8),
				ing, ing, ing, recipeBook
			);
		}

		// 単体クラフト
		singleCraft(new ItemStack(BlockInit.alt_block), new ItemStack(ItemInit.alternative_ingot, 4));
		singleCraft(new ItemStack(BlockInit.magicbook), new ItemStack(Items.BOOK, 3));
		singleCraft(new ItemStack(ItemInit.whipping_cream), new ItemStack(ItemInit.butter, 2));
		singleCraft(new ItemStack(ItemInit.cabbage), new ItemStack(ItemInit.cabbage_seed));
		singleCraft(new ItemStack(ItemInit.clero_petal), new ItemStack(ItemInit.clero_petal));
		singleCraft(new ItemStack(ItemInit.corn), new ItemStack(ItemInit.corn_seed));
		singleCraft(new ItemStack(BlockInit.cornflower), new ItemStack(Items.DYE, 2 , 6));
		singleCraft(new ItemStack(ItemInit.eggplant), new ItemStack(ItemInit.eggplant_seed));
		singleCraft(new ItemStack(ItemInit.j_radish), new ItemStack(ItemInit.j_radish_seed));
		singleCraft(new ItemStack(ItemInit.spinach), new ItemStack(ItemInit.spinach_seed));
		singleCraft(new ItemStack(BlockInit.lemon_trapdoor_n), new ItemStack(BlockInit.lemon_trapdoor));
		singleCraft(new ItemStack(ItemInit.lettuce), new ItemStack(ItemInit.lettuce_seed));
		singleCraft(new ItemStack(ItemInit.greenpepper), new ItemStack(ItemInit.greenpepper_seed));
		singleCraft(new ItemStack(ItemInit.pineapple), new ItemStack(ItemInit.pineapple_seed));
		singleCraft(new ItemStack(ItemInit.lettuce), new ItemStack(ItemInit.lettuce_seed));
		singleCraft(new ItemStack(ItemInit.ine), new ItemStack(ItemInit.rice_seed, 2));
		singleCraft(new ItemStack(ItemInit.cotton), new ItemStack(Items.STRING, 2));
		singleCraft(new ItemStack(ItemInit.sugarbell), new ItemStack(Items.SUGAR, 2), "sugar_0");
		singleCraft(new ItemStack(ItemInit.coconut), new ItemStack(Items.SUGAR, 4), "sugar_1");
		singleCraft(new ItemStack(Items.WATER_BUCKET), new ItemStack(ItemInit.watercup, 8));
		singleCraft(new ItemStack(ItemInit.alt_bucket_water), new ItemStack(ItemInit.watercup, 8), "alt_cup");
		singleCraft(new ItemStack(Items.MILK_BUCKET), new ItemStack(ItemInit.milk_pack, 8));
		singleCraft(new ItemStack(ItemInit.milk_pack), new ItemStack(ItemInit.whipping_cream, 2));
		singleCraft(new ItemStack(BlockInit.white_ironfence), new ItemStack(BlockInit.black_ironfence));
		singleCraft(new ItemStack(BlockInit.black_ironfence), new ItemStack(BlockInit.white_ironfence));
		singleCraft(new ItemStack(BlockInit.fance_gothic_white), new ItemStack(BlockInit.fance_gothic_black));
		singleCraft(new ItemStack(BlockInit.fance_gothic_black), new ItemStack(BlockInit.fance_gothic_white));
		singleCraft(new ItemStack(BlockInit.orange_log), new ItemStack(BlockInit.orange_planks, 4));
		singleCraft(new ItemStack(BlockInit.lemon_trapdoor), new ItemStack(BlockInit.lemon_trapdoor_n));
		singleCraft(new ItemStack(BlockInit.cosmos_light_block), new ItemStack(ItemInit.cosmos_light_ingot, 9));
		singleCraft(new ItemStack(ItemInit.simple_door_1), new ItemStack(ItemInit.simple_door_2));
		singleCraft(new ItemStack(ItemInit.simple_door_2), new ItemStack(ItemInit.simple_door_1));
		singleCraft(new ItemStack(ItemInit.brown_5paneldoor), new ItemStack(ItemInit.brown_2paneldoor));
		singleCraft(new ItemStack(ItemInit.brown_2paneldoor), new ItemStack(ItemInit.brown_5paneldoor));
		singleCraft(new ItemStack(BlockInit.menu_list), new ItemStack(BlockInit.menu_list_w));
		singleCraft(new ItemStack(BlockInit.menu_list_w), new ItemStack(BlockInit.menu_list));
		singleCraft(new ItemStack(BlockInit.bread_baskets), new ItemStack(BlockInit.hard_bread_basket));
		singleCraft(new ItemStack(BlockInit.hard_bread_basket), new ItemStack(BlockInit.bread_baskets));
		singleCraft(new ItemStack(ItemInit.alt_bucket_lava), new ItemStack(ItemInit.alt_bucket));
		singleCraft(new ItemStack(ItemInit.milk_pack), new ItemStack(ItemInit.whipping_cream, 4));
	}

    public static Multimap<String,IRecipe> recipeMultimap = HashMultimap.create();

    // MBT引継ぎクラフト用
    public static void addRecipe(String key, IRecipe value) {

        if(value.getRegistryName() == null) {
			value.setRegistryName(new ResourceLocation(value.getGroup()));
        }

        ForgeRegistries.RECIPES.register(value);
        recipeMultimap.put(key, value);
    }

    // 定型レシピ
    public static void addShapedRecipe (String name, ItemStack stack, Object... obj) {
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, name), MODSRC, stack, obj );
    }

    // 複数不定レシピ
    public static void addShapelessRecipe (String name, ItemStack resalt, Ingredient... ing) {
		GameRegistry.addShapelessRecipe(new ResourceLocation(MODID, name), MODSRC, resalt, ing );
    }

    // 単体クラフト用
    public static void singleCraft (ItemStack useStack, ItemStack resaltStack) {
		GameRegistry.addShapelessRecipe(new ResourceLocation(MODID, resaltStack.getUnlocalizedName()), MODSRC, resaltStack,
			new Ingredient[] { Ingredient.fromStacks(useStack) }
		);
    }

    // 単体クラフト用
    public static void singleCraft (ItemStack useStack, ItemStack resaltStack, String name) {
		GameRegistry.addShapelessRecipe(new ResourceLocation(MODID, name), MODSRC, resaltStack,
			new Ingredient[] { Ingredient.fromStacks(useStack) }
		);
    }

    public static Ingredient getIngred (Item item) {
    	return Ingredient.fromStacks(new ItemStack(item));
    }

    public static Ingredient getIngred (Item item, int amount, int data) {
    	return Ingredient.fromStacks(new ItemStack(item, amount, data));
    }

    public static Ingredient getIngred (Block block) {
    	return Ingredient.fromStacks(new ItemStack(block));
    }

    public static Ingredient getIngred (Block block, int amount, int data) {
    	return Ingredient.fromStacks(new ItemStack(block, amount, data));
    }

    public static Ingredient getIngred (String... oreNameArray) {

    	// 配列のリストの初期化
    	int arraySize = 0;
    	List<ItemStack[]> arrayList = new ArrayList<>();

    	// 配列を取得してリストに突っ込む
    	for (String oreName : oreNameArray) {
    		ItemStack[] oreArray = SMUtil.getOreArray(oreName);
    		arrayList.add(oreArray);
    		arraySize += oreArray.length;
    	}

    	// 配列の初期化
    	int putLength = 0;
    	ItemStack[] stackArray = new ItemStack[arraySize];

    	// リスト分回して配列にまとめる
    	for (ItemStack[] stack : arrayList) {
    		System.arraycopy(stack, 0, stackArray, putLength, stack.length);
    		putLength = stack.length;
    	}

    	return Ingredient.fromStacks(stackArray);
    }

	public static void registerSmelting() {
		//たまごっちメモ：(精錬前のアイテムまたはブロック)、（精錬後のアイテムまたはブロック）、精錬時に出る経験値
		GameRegistry.addSmelting(ItemInit.watercup, new ItemStack(ItemInit.salt, 16), 0.3F);
		GameRegistry.addSmelting(ItemInit.plant_chips, new ItemStack(Items.DYE, 1, 15), 0.05F);
		GameRegistry.addSmelting(ItemInit.sweetpotato, new ItemStack(ItemInit.yaki_imo), 0.1F);
		GameRegistry.addSmelting(ItemInit.eggplant, new ItemStack(ItemInit.yakinasu), 0.1F);
		GameRegistry.addSmelting(BlockInit.chestnut_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(BlockInit.coconut_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(BlockInit.lemon_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(BlockInit.orange_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(BlockInit.prism_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(BlockInit.estor_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(BlockInit.peach_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(ItemInit.banana, new ItemStack(ItemInit.yaki_banana), 0.1F);
		GameRegistry.addSmelting(ItemInit.estor_apple, new ItemStack(ItemInit.yaki_apple), 0.1F);
	}

	// JEI追加レシピ
	public static void JEIaddRecipe () {
        JeiRecipeMFTank.init();
	}
}
