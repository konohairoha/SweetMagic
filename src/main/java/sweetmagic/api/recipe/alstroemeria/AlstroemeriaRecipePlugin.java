package sweetmagic.api.recipe.alstroemeria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.recipe.RecipeRegisterHelper;
import sweetmagic.util.OreItems;
import sweetmagic.util.SMUtil;

@SMAlstroemeriaRecipePlugin(priority = EventPriority.LOW)
public class AlstroemeriaRecipePlugin implements IAlstroemeriaRecipePlugin {

	@Override
	public void registerAlstroemeriaRecipe(AlstroemeriaRecipes recipe) {

		//レシピ登録方法…ItemStack(hand)、ItemStack[](Input)、ItemStack[](Output)

		// 魔法の粉
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("dustSugar", 2),
			new Object[] { new ItemStack(Items.DYE, 1, 15) },
			new ItemStack[] { new ItemStack(ItemInit.magicmeal, 2) }
		));

//		// ライアーローズ
//		recipe.addRecipe(new AlstroemeriaRecipes(
//			new ItemStack(ItemInit.sticky_stuff_petal),
//			new Object[] { SMUtil.getStack(ItemInit.clero_petal), SMUtil.getStack(Items.POISONOUS_POTATO), new ItemStack(ItemInit.moonblossom_petal, 4), new ItemStack(Items.REDSTONE, 4), new ItemStack(Items.DYE, 2, 9)  },
//			new ItemStack[] { new ItemStack(SMUtil.getItemBlock(BlockInit.lier_rose)) }
//		));

		// ガラスの瓶→空っぽの魔法流の瓶変換
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Items.GLASS_BOTTLE),
			new Object[] { new ItemStack(Items.GLASS_BOTTLE, 10) },
			new ItemStack[] { SMUtil.getStack(ItemInit.b_mf_bottle) }
		));

		// 魔法流の小瓶→魔法流の瓶変換
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.mf_sbottle),
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 9) },
			new ItemStack[] { SMUtil.getStack(ItemInit.mf_bottle) }
		));

		// 石の模様付きレンガ6つ→魔法流生成器
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Blocks.STONEBRICK, 6, 3),
			new Object[] { new ItemStack(ItemInit.magicmeal, 2), new OreItems("logWood", 4) },
			new ItemStack[] { new ItemStack(BlockInit.mfchanger) }
		));

		// ファイアナスタチウムの種
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.fire_powder),
			new Object[] { new ItemStack(Items.FLINT, 10), new OreItems("gemQuartz", 4), new OreItems("netherrack", 64) },
			new ItemStack[] { new ItemStack(ItemInit.fire_nasturtium_seed) }
		));

		// ドリズリィ・ミオソチスの種
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.cornflower),
			new Object[] { new ItemStack(ItemInit.mf_sbottle), new ItemStack(Items.WHEAT_SEEDS, 2)},
			new ItemStack[] { new ItemStack(ItemInit.dm_seed) }
		));

		// オルタナティブインゴット
		recipe.addRecipe(new AlstroemeriaRecipes(
			"ingotIron",
			new Object[] { new ItemStack(ItemInit.aether_crystal, 2)},
			new ItemStack[] { new ItemStack(ItemInit.alternative_ingot, 2) }
		));

		// エーテルクリスタルのかけら
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.aether_crystal_shard, 1),
			new Object[] { new ItemStack(ItemInit.aether_crystal_shard, 11)},
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal) }
		));

		// ディバインクリスタル
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.aether_crystal, 6),
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 4), new ItemStack(ItemInit.sannyflower_petal, 4), new ItemStack(ItemInit.moonblossom_petal, 4)},
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal) }
		));

		// ピュアクリスタル
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.divine_crystal, 6),
			new Object[] { new ItemStack(ItemInit.mf_bottle, 3), new ItemStack(ItemInit.sannyflower_petal, 16), new ItemStack(ItemInit.moonblossom_petal, 16)},
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal) }
		));

		// デウスクリスタル
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.pure_crystal, 4),
			new Object[] { new ItemStack(ItemInit.mf_bottle, 6), new ItemStack(ItemInit.sannyflower_petal, 32), new ItemStack(ItemInit.moonblossom_petal, 32)
					, new ItemStack(ItemInit.witch_tears, 4)},
			new ItemStack[] { new ItemStack(ItemInit.deus_crystal) }
		));

		// コズミッククリスタル
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.deus_crystal, 1),
			new Object[] { new ItemStack(ItemInit.cosmic_crystal_shard, 16), new ItemStack(ItemInit.mf_bottle, 10), new ItemStack(ItemInit.sannyflower_petal, 64)
					, new ItemStack(ItemInit.moonblossom_petal, 64), new ItemStack(ItemInit.mystical_page, 4) },
			new ItemStack[] { new ItemStack(ItemInit.cosmic_crystal) }
		));

		// コーラスフルーツ
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.ender_shard),
			new Object[] { new ItemStack(ItemInit.mf_sbottle), new ItemStack(BlockInit.cornflower)},
			new ItemStack[] { new ItemStack(Items.CHORUS_FRUIT, 3) }
		));

		// クレロランプ
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.prizmium),
			new Object[] { new ItemStack(BlockInit.sugarglass, 2), new ItemStack(ItemInit.alternative_ingot, 8)},
			new ItemStack[] { new ItemStack(BlockInit.clerolanp) }
		));

		// 栗の苗木
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Blocks.CARPET, 1, 12),
			new Object[] { "treeSapling"},
			new ItemStack[] { new ItemStack(BlockInit.chestnut_sapling) }
		));

		// オレンジの苗木
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Blocks.CARPET, 1, 1),
			new Object[] { "treeSapling" },
			new ItemStack[] { new ItemStack(BlockInit.orange_sapling) }
		));

		// レモンの苗木
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Blocks.CARPET, 1, 4),
			new Object[] { "treeSapling" },
			new ItemStack[] { new ItemStack(BlockInit.lemon_sapling) }
		));

		// ヤシの苗木
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Blocks.CARPET, 1, 5),
			new Object[] { "treeSapling" },
			new ItemStack[] { new ItemStack(BlockInit.coconut_sapling) }
		));

		// プリズムの苗木
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.magicmeal),
			new Object[] { "treeSapling" },
			new ItemStack[] { new ItemStack(BlockInit.prism_sapling) }
		));

		// バナナの苗木
		recipe.addRecipe(new AlstroemeriaRecipes(
			"dyeYellow",
			new Object[] { "treeSapling" },
			new ItemStack[] { new ItemStack(BlockInit.banana_sapling) }
		));

		// エストールの苗木
		recipe.addRecipe(new AlstroemeriaRecipes(
			"dyeRed",
			new Object[] { "treeSapling" },
			new ItemStack[] { new ItemStack(BlockInit.estor_sapling) }
		));

		// 創世の台座
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("stone", 4),
			new Object[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(Items.IRON_INGOT, 4), new ItemStack(ItemInit.sugarbell, 8)},
			new ItemStack[] { new ItemStack(BlockInit.pedestal_creat) }
		));

		// 粘土
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.sticky_stuff_petal),
			new Object[] { "dirt", "sand"},
			new ItemStack[] { new ItemStack(Items.CLAY_BALL, 16) }
		));

		// ブレイズロッド
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.fire_powder, 4),
			new Object[] { new OreItems("dustGlowstone", 2)},
			new ItemStack[] { new ItemStack(Items.BLAZE_ROD) }
		));

		// 改良型MFテーブル
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.mftable),
			new Object[] { new ItemStack(ItemInit.mysterious_page, 8), new ItemStack(ItemInit.sannyflower_petal, 16), new ItemStack(ItemInit.moonblossom_petal, 16),
					new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.witch_tears) },
			new ItemStack[] { new ItemStack(BlockInit.advanced_mftable) }
		));

		// 改良型MFチェンジャー
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.mfchanger),
			new Object[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.magicmeal, 16), new ItemStack(Items.ENDER_PEARL, 4), new ItemStack(ItemInit.witch_tears)},
			new ItemStack[] { new ItemStack(BlockInit.advanced_mfchanger) }
		));

		// 改良型MFタンク
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.mftank),
			new Object[] { new ItemStack(BlockInit.sugarglass, 64), new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.witch_tears)},
			new ItemStack[] { new ItemStack(BlockInit.advanced_mftank) }
		));

		// 改良型エーテル炉
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.aether_furnace_bottom),
			new Object[] { new ItemStack(BlockInit.glow_lamp, 4), new ItemStack(ItemInit.pure_crystal, 4), new ItemStack(Items.GOLD_INGOT, 12)
					, new ItemStack(Blocks.IRON_BARS, 16), new ItemStack(ItemInit.witch_tears)},
			new ItemStack[] { new ItemStack(BlockInit.advanced_aether_furnace_bottom) }
		));

		// マスターマギアタンク
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.advanced_mftank),
			new Object[] { new ItemStack(BlockInit.sugarglass, 64), new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.mystical_page, 2)},
			new ItemStack[] { new ItemStack(BlockInit.mm_tank) }
		));

		// マスターマギアテーブル
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.advanced_mftable),
			new Object[] { new ItemStack(BlockInit.prism_log, 64), new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.mystical_page, 2) },
			new ItemStack[] { new ItemStack(BlockInit.mm_table) }
		));

		// 黄昏の明かり
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("glowstone", 4),
			new Object[] { new ItemStack(ItemInit.sannyflower_petal, 16), new ItemStack(ItemInit.moonblossom_petal, 16), new ItemStack(ItemInit.prizmium, 4), new ItemStack(ItemInit.witch_tears)},
			new ItemStack[] { new ItemStack(BlockInit.twilightlight) }
		));

		// アンティークレンガ
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Blocks.BRICK_BLOCK, 4),
			new Object[] { new OreItems("stone", 32) },
			new ItemStack[] { new ItemStack(BlockInit.antique_brick_0, 64) }
		));

		// 赤色のアンティークレンガ
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("antique_brick_all", 48),
			new Object[] { new OreItems("dyeRed", 1) },
			new ItemStack[] { new ItemStack(BlockInit.antique_brick_0, 64) }
		));

		// 白色のアンティークレンガ
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("antique_brick_all", 48),
			new Object[] { new OreItems("dyeWhite", 1) },
			new ItemStack[] { new ItemStack(BlockInit.antique_brick_0w, 64) }
		));

		// 黒色のアンティークレンガ
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("antique_brick_all", 48),
			new Object[] { new OreItems("dyeBlack", 1) },
			new ItemStack[] { new ItemStack(BlockInit.antique_brick_b, 64) }
		));

		// 茶色のアンティークレンガ
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("antique_brick_all", 48),
			new Object[] { new OreItems("dyeBrown", 1) },
			new ItemStack[] { new ItemStack(BlockInit.antique_brick_0l, 64) }
		));

		// 緑色のアンティークレンガ
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("antique_brick_all", 48),
			new Object[] { new OreItems("dyeGreen", 1) },
			new ItemStack[] { new ItemStack(BlockInit.antique_brick_0g, 64) }
		));

		// 堆肥土
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("listAllseed", 8),
			new Object[] { new ItemStack(ItemInit.magicmeal, 8), new OreItems("dirt", 8) },
			new ItemStack[] { new ItemStack(BlockInit.compost_drit, 8) }
		));

		// 赤砂
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Blocks.SAND, 16),
			new Object[] { new OreItems("dyeRed") },
			new ItemStack[] { new ItemStack(Blocks.SAND, 16, 1) }
		));

		// 赤ネザーレンガ
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Blocks.NETHERRACK, 16),
			new Object[] { new OreItems("dyeRed") },
			new ItemStack[] { new ItemStack(Blocks.RED_NETHER_BRICK, 24) }
		));

		// プリズムウッドチェスト
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.prism_log, 4),
			new Object[] { new OreItems("chestWood", 4) },
			new ItemStack[] { new ItemStack(BlockInit.prism_woodchest) }
		));

		// エストールウッドチェスト
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.estor_log, 4),
			new Object[] { new OreItems("chestWood", 4) },
			new ItemStack[] { new ItemStack(BlockInit.estor_woodchest) }
		));

		// 黄色のラックチェスト
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.ine, 8),
			new Object[] { "dyeYellow", new OreItems("logWood", 2), new OreItems("chestWood", 4) },
			new ItemStack[] { new ItemStack(BlockInit.rattan_chest_y) }
		));

		// 茶色のラックチェスト
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.ine, 8),
			new Object[] { "dyeBrown", new OreItems("logWood", 2), new OreItems("chestWood", 4) },
			new ItemStack[] { new ItemStack(BlockInit.rattan_chest_b) }
		));

		// 緑4パネガラス
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.sugarglass, 16),
			new Object[] { "dyeGreen", new OreItems("stickWood", 4) },
			new ItemStack[] { new ItemStack(BlockInit.green4panel_glass, 32) }
		));

		// 緑4パネガラス
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.sugarglass, 16),
			new Object[] { "dyeGreen", new OreItems("stickWood", 4) },
			new ItemStack[] { new ItemStack(BlockInit.green4panel_glass, 32) }
		));

		// 茶4パネガラス
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.sugarglass, 16),
			new Object[] { "dyeBrown", new OreItems("stickWood", 4) },
			new ItemStack[] { new ItemStack(BlockInit.brown4panel_glass, 32) }
		));

		// 薄茶4パネガラス
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.brown4panel_glass, 16),
			new Object[] { "dyeWhite", new OreItems("stickWood", 4) },
			new ItemStack[] { new ItemStack(BlockInit.lightbrown4panel_glass, 32) }
		));

		// 濃茶4パネガラス
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.brown4panel_glass, 16),
			new Object[] { "dyeBlack", new OreItems("stickWood", 4) },
			new ItemStack[] { new ItemStack(BlockInit.darkbrown4panel_glass, 32) }
		));

		// 網ガラス
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.sugarglass, 16),
			new Object[] { new ItemStack(Blocks.IRON_BARS), new OreItems("stickWood", 4) },
			new ItemStack[] { new ItemStack(BlockInit.ami_glass, 32) }
		));

		// ゴージャスガラス
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.sugarglass, 16),
			new Object[] { new ItemStack(Items.IRON_NUGGET), "dyeBlack" },
			new ItemStack[] { new ItemStack(BlockInit.gorgeous_glass, 32) }
		));

		// ゴージャスガラス（白）
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.sugarglass, 16),
			new Object[] { new ItemStack(Items.IRON_NUGGET), "dyeWhite" },
			new ItemStack[] { new ItemStack(BlockInit.gorgeous_glass_w, 32) }
		));

		// ゴージャスガラス（白）
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.sugarglass, 16),
			new Object[] { new ItemStack(Items.IRON_NUGGET), "dyeWhite" },
			new ItemStack[] { new ItemStack(BlockInit.gorgeous_glass_w, 32) }
		));

		// マジックバリアガラス
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(BlockInit.sugarglass, 16),
			new Object[] { new ItemStack(ItemInit.magicmeal, 4) },
			new ItemStack[] { new ItemStack(BlockInit.magicbarrier_off, 32) }
		));

		// コルクボード
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Items.LEATHER),
			new Object[] { new OreItems("stickWood", 4), "dyeBrown" },
			new ItemStack[] { new ItemStack(BlockInit.corkboard, 4) }
		));

		// ウォールボード
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Items.LEATHER),
			new Object[] { new OreItems("stickWood", 4), "dyeWhite" },
			new ItemStack[] { new ItemStack(BlockInit.wallboard, 4) }
		));

		// ウォールボード（黒）
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Items.LEATHER),
			new Object[] { new OreItems("stickWood", 4), "dyeBlack" },
			new ItemStack[] { new ItemStack(BlockInit.wallboard_black, 4) }
		));

		// 店看板
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Items.IRON_NUGGET, 4),
			new Object[] { new ItemStack(Items.LEATHER) },
			new ItemStack[] { new ItemStack(BlockInit.shopboard, 4) }
		));

		// 羽根
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.cotton),
			new Object[] { new ItemStack(Items.STRING, 4) },
			new ItemStack[] { new ItemStack(Items.FEATHER, 2) }
		));

		// ブロック、ハーフ、階段をリストに突っ込む
		List<RecipeRegisterHelper> recipeList = new ArrayList<>();
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick, "dyeBrown", false));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_o, "dyeOrange", false));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_b, "dyeBlue", false));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_g, "dyeGreen", false));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_y, "dyeYellow", false));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_r, "dyeRed", false));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_l, "dyeBlack", false));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_p, "dyePurple", false));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_w, "dyeWhite", false));

		// リストの分だけ回す
		for (RecipeRegisterHelper re : recipeList ) {

			// アンティーク→ロングタイルレンガ
			recipe.addRecipe(new AlstroemeriaRecipes(
				new OreItems("stone", 12),
				new Object[] { new OreItems("antique_brick_all", 16), new OreItems(re.getDye()) },
				new ItemStack[] { new ItemStack(re.getPlanks(), 32) }
			));
		}

		Map<Block, String> vaseRecipe = new HashMap<>();
		vaseRecipe.put(BlockInit.flower_vese_o, "dyeOrange");
		vaseRecipe.put(BlockInit.flower_vese_w, "dyeWhite");
		vaseRecipe.put(BlockInit.flower_vese_b, "dyeBlue");
		vaseRecipe.put(BlockInit.flower_vese_p, "dyePurple");
		vaseRecipe.put(BlockInit.flower_vese_s, "dyeLightBlue");
		vaseRecipe.put(BlockInit.flower_vese_y, "dyeYellow");

		for (Entry<Block, String> map : vaseRecipe.entrySet()) {

			// 花瓶
			recipe.addRecipe(new AlstroemeriaRecipes(
				new ItemStack(Items.BRICK, 3),
				new Object[] { map.getValue() },
				new ItemStack[] { new ItemStack(map.getKey(), 4) }
			));
		}
	}
}
