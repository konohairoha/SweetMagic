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

		//レシピ登録方法…Object(hand)、Object[](Input)、ItemStack[](Output)

		// 魔法の粉
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("dustSugar", 2),
			new Object[] { new ItemStack(Items.DYE, 1, 15) },
			new ItemStack[] { new ItemStack(ItemInit.magicmeal, 2) }
		));

		// ガラスの瓶→空っぽの魔法流の瓶変換
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(Items.GLASS_BOTTLE),
			new Object[] { new ItemStack(Items.GLASS_BOTTLE, 9) },
			new ItemStack[] { SMUtil.getStack(ItemInit.b_mf_bottle) }
		));

		// 空っぽの魔法流の瓶→空のマギアボトル変換
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.b_mf_bottle),
			new Object[] { new ItemStack(ItemInit.b_mf_bottle, 9) },
			new ItemStack[] { SMUtil.getStack(ItemInit.b_mf_magiabottle) }
		));

		// 魔法流の小瓶→魔法流の瓶変換
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.mf_sbottle),
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 9) },
			new ItemStack[] { SMUtil.getStack(ItemInit.mf_bottle) }
		));

		// 魔法流の瓶→マギアボトル変換
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.mf_bottle),
			new Object[] { new ItemStack(ItemInit.mf_bottle, 9) },
			new ItemStack[] { SMUtil.getStack(ItemInit.mf_magiabottle) }
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
			new Object[] { new ItemStack(ItemInit.mf_sbottle), new ItemStack(Items.WHEAT_SEEDS, 2) },
			new ItemStack[] { new ItemStack(ItemInit.dm_seed) }
		));

		// オルタナティブインゴット
		recipe.addRecipe(new AlstroemeriaRecipes(
			"ingotIron",
			new Object[] { new ItemStack(ItemInit.aether_crystal, 2)},
			new ItemStack[] { new ItemStack(ItemInit.alternative_ingot, 2) }
		));

		// コズモライトインゴット
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.alternative_ingot),
			new Object[] { new ItemStack(Items.GOLD_INGOT), new ItemStack(ItemInit.mf_magiabottle) },
			new ItemStack[] { new ItemStack(ItemInit.cosmos_light_ingot, 2) }
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
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 4), new ItemStack(ItemInit.sannyflower_petal, 4), new ItemStack(ItemInit.moonblossom_petal, 4) },
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal) }
		));

		// ピュアクリスタル
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.divine_crystal, 4),
			new Object[] { new ItemStack(ItemInit.mf_bottle, 6), new ItemStack(ItemInit.sannyflower_petal, 8), new ItemStack(ItemInit.moonblossom_petal, 8) },
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal) }
		));

		// デウスクリスタル
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.pure_crystal, 3),
			new Object[] { new ItemStack(ItemInit.mf_magiabottle, 1), new ItemStack(ItemInit.sannyflower_petal, 12), new ItemStack(ItemInit.moonblossom_petal, 12)
					, new ItemStack(ItemInit.witch_tears, 4) },
			new ItemStack[] { new ItemStack(ItemInit.deus_crystal) }
		));

		// コズミッククリスタル
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.deus_crystal, 1),
			new Object[] { new ItemStack(ItemInit.cosmic_crystal_shard, 16), new ItemStack(ItemInit.mf_magiabottle, 4), new ItemStack(ItemInit.sannyflower_petal, 16)
					, new ItemStack(ItemInit.moonblossom_petal, 16), new ItemStack(ItemInit.mystical_page, 4) },
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

		// 創世の台座
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("stone", 4),
			new Object[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(Items.IRON_INGOT, 4), new ItemStack(ItemInit.sugarbell, 8)},
			new ItemStack[] { new ItemStack(BlockInit.pedestal_creat) }
		));

		// エーテルランタン
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("blockGlass", 4),
			new Object[] { new OreItems("ingotIron", 4), new OreItems("plankWood", 8) },
			new ItemStack[] { new ItemStack(BlockInit.aether_lanp) }
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
			new Object[] { new OreItems("dustGlowstone", 2) },
			new ItemStack[] { new ItemStack(Items.BLAZE_ROD) }
		));

		// ネザーラック
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.fire_powder, 2),
			new Object[] { new OreItems("dirt", 16) },
			new ItemStack[] { new ItemStack(Blocks.NETHERRACK, 32) }
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

		// カフェキッチンテーブル
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("chestWood", 4),
			new Object[] { new OreItems("smLog", 4) },
			new ItemStack[] { new ItemStack(BlockInit.cafe_kitchen_table) }
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
			new Object[] { "ironbar", new OreItems("stickWood", 4) },
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

		// 種袋
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.cotton_cloth, 4),
			new Object[] { new ItemStack(Items.STRING), new OreItems("listAllseed", 5) },
			new ItemStack[] { new ItemStack(ItemInit.seedbag) }
		));

		// アクセ袋
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("magicAccessori"),
			new Object[] { new ItemStack(ItemInit.cotton_cloth, 4),new ItemStack(Items.STRING), new ItemStack(ItemInit.mf_bottle, 4) },
			new ItemStack[] { new ItemStack(ItemInit.accebag) }
		));

		// ガストの涙
		recipe.addRecipe(new AlstroemeriaRecipes(
			new OreItems("gemQuartz", 4),
			new Object[] { new OreItems("dustGlowstone", 4) },
			new ItemStack[] { new ItemStack(Items.GHAST_TEAR, 2) }
		));

		// プリズムの苗木
		recipe.addRecipe(new AlstroemeriaRecipes(
			new ItemStack(ItemInit.magicmeal),
			new Object[] { "treeSapling" },
			new ItemStack[] { new ItemStack(BlockInit.prism_sapling) }
		));

		Map<Block, String> saplingRecipe = new HashMap<>();
		saplingRecipe.put(BlockInit.chestnut_sapling, "dyeBrown");
		saplingRecipe.put(BlockInit.orange_sapling, "dyeOrange");
		saplingRecipe.put(BlockInit.lemon_sapling, "dyeYellow");
		saplingRecipe.put(BlockInit.coconut_sapling, "dyeLime");
		saplingRecipe.put(BlockInit.banana_sapling, "dyeGreen");
		saplingRecipe.put(BlockInit.estor_sapling, "dyeRed");
		saplingRecipe.put(BlockInit.peach_sapling, "dyePink");

		for (Entry<Block, String> map : saplingRecipe.entrySet()) {

			// 苗木
			recipe.addRecipe(new AlstroemeriaRecipes(
				map.getValue(),
				new Object[] { "treeSapling" },
				new ItemStack[] { new ItemStack(map.getKey()) }
			));
		}

		Map<Block, String> brickRecipe = new HashMap<>();
		brickRecipe.put(BlockInit.antique_brick_0, "dyeRed");
		brickRecipe.put(BlockInit.antique_brick_0w, "dyeWhite");
		brickRecipe.put(BlockInit.antique_brick_b, "dyeBlack");
		brickRecipe.put(BlockInit.antique_brick_0l, "dyeBrown");
		brickRecipe.put(BlockInit.antique_brick_0g, "dyeGreen");

		for (Entry<Block, String> map : brickRecipe.entrySet()) {

			// アンティークレンガ
			recipe.addRecipe(new AlstroemeriaRecipes(
				new OreItems("antique_brick_all", 48),
				new Object[] { map.getValue() },
				new ItemStack[] { new ItemStack(map.getKey(), 64) }
			));
		}

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

		Map<Block, String> whiteLineRecipe = new HashMap<>();
		whiteLineRecipe.put(BlockInit.whiteline_brick, "dyeOrange");
		whiteLineRecipe.put(BlockInit.whiteline_brick_y, "dyeYellow");
		whiteLineRecipe.put(BlockInit.whiteline_brick_b, "dyeBlack");

		for (Entry<Block, String> map : whiteLineRecipe.entrySet()) {

			// ホワイトラインレンガ
			recipe.addRecipe(new AlstroemeriaRecipes(
				new ItemStack(Blocks.SAND, 4),
				new Object[] { new OreItems("antique_brick_all", 32), map.getValue() },
				new ItemStack[] { new ItemStack(map.getKey(), 64) }
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
