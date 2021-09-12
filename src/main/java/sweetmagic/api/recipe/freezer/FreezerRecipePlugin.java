package sweetmagic.api.recipe.freezer;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.ItemInit;
import sweetmagic.util.OreItems;

@SMFreezerRecipePlugin(priority = EventPriority.LOW)
public class FreezerRecipePlugin implements IFreezerRecipePlugin {

	@Override
	public void registerFreezerRecipe(FreezerRecipes recipe) {

		//レシピ登録方法…ItemStack(hand)、ItemStack[](Input)、ItemStack[](Output)
		//注意：handの中身をinputの方にも含めて定義すること(HandItemをShrinkしてないため)

		// ミカンゼリー
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.orange),
			new Object[] { "foodGelatine", "dustSugar", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.orange_jelly, 2) }
		));

		// イチゴゼリー
		recipe.addRecipe(new FreezerRecipes(
			"cropStrawberry",
			new Object[] { "foodGelatine", "dustSugar", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.strawberry_jelly, 2) }
		));

		// りんごゼリー
		recipe.addRecipe(new FreezerRecipes(
			"cropApple",
			new Object[] { "foodGelatine", "dustSugar", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.apple_jelly, 2) }
		));

		// 桃ゼリー
		recipe.addRecipe(new FreezerRecipes(
			"cropPeach",
			new Object[] { "foodGelatine", "dustSugar", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.peach_jelly, 2) }
		));

		// レモンかき氷
		recipe.addRecipe(new FreezerRecipes(
			"cropLemon",
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.kakigori_lemon, 2) }
		));

		// イチゴかき氷
		recipe.addRecipe(new FreezerRecipes(
			"cropStrawberry",
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.kakigori_strawberry, 2) }
		));

		// 練乳かき氷
		recipe.addRecipe(new FreezerRecipes(
			"bucketMilk",
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.kakigori_milk, 2) }
		));

		// チョコレート
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.cocoamass, 3),
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", "bucketMilk" },
			new ItemStack[] { new ItemStack(ItemInit.chocolate, 3) }
		));

		// ホワイトチョコレート
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.cocoabutter, 3),
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", "bucketMilk" },
			new ItemStack[] { new ItemStack(ItemInit.white_chocolate, 3) }
		));

		// ようかん
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.azuki_seed, 4),
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", new ItemStack(ItemInit.gelatin, 2), "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.youkan, 4) }
		));

		// ロールケーキ
		recipe.addRecipe(new FreezerRecipes(
			"foodDough",
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", new OreItems("foodCream", 2), new OreItems("bucketMilk", 2) },
			new ItemStack[] { new ItemStack(ItemInit.cake_roll, 5) }
		));

		// エクレア
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("foodCustard", 2),
			new Object[] { new ItemStack(ItemInit.chocolate), "foodButter", "dustSalt", "egg", "bucketWater", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.eclair, 7) }
		));

		// マシュマロ
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("foodGelatine", 2),
			new Object[] { "egg", new ItemStack(ItemInit.vannila_essence, 2), "bucketWater", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.marshmallow, 8) }
		));

		// イチゴ大福
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.azuki_seed, 4),
			new Object[] { "cropStrawberry", new OreItems("dustFlour", 2), "bucketWater", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.itigo_daihuku, 5) }
		));

		// プリン
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.vannila_essence, 2),
			new Object[] { "egg", "bucketMilk", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.pudding, 4) }
		));

		// ショートケーキ
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("cropStrawberry", 3),
			new Object[] { "foodDough", new ItemStack(ItemInit.vannila_essence, 2), new OreItems("foodCream", 2), new OreItems("dustSugar", 2) },
			new ItemStack[] { new ItemStack(ItemInit.short_cake, 5) }
		));

		// フルーツポンチ
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.banana, 2),
			new Object[] { "listAllfruit", new ItemStack(ItemInit.vannila_essence), "dustSugar", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.fluit_mix, 5) }
		));

		// コーンサラダ
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("cropLettuce", 3),
			new Object[] { new ItemStack(ItemInit.corn, 2), "cropCarrot", new ItemStack(ItemInit.salad_dressing) },
			new ItemStack[] { new ItemStack(ItemInit.salad_mixcorn, 6) }
		));

		// コールスロー
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("cropCabbage", 3),
			new Object[] { new ItemStack(ItemInit.corn, 2), "cropCarrot", "dustSalt", new ItemStack(ItemInit.salad_dressing) },
			new ItemStack[] { new ItemStack(ItemInit.salad_coleslaw, 6) }
		));

		// ポテサラ
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("cropPotato", 3),
			new Object[] { new OreItems("cropCarrot", 2), "dustSalt", new ItemStack(ItemInit.mayonnaise, 3), new ItemStack(ItemInit.vinegar) },
			new ItemStack[] { new ItemStack(ItemInit.salad_potate, 6) }
		));

		// カプレーゼ
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("cropTomato", 3),
			new Object[] { new OreItems("foodCheese", 3), "cropLemon", "dustSalt", "foodOil" },
			new ItemStack[] { new ItemStack(ItemInit.salad_caprese, 3) }
		));

		// 鮭どんぶり
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(Items.FISH, 3, 1),
			new Object[] { new OreItems("foodRice", 6), new ItemStack(ItemInit.vinegar, 3), new ItemStack(ItemInit.dry_seaweed, 6) },
			new ItemStack[] { new ItemStack(ItemInit.salmondon, 6) }
		));

		// りんご飴
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("cropApple", 2),
			new Object[] { new OreItems("dustSugar", 6), new OreItems("bucketWater", 2), new OreItems("stickWood", 2) },
			new ItemStack[] { new ItemStack(ItemInit.applecandy, 2) }
		));

		// サーモン寿司
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(Items.FISH, 2, 1),
			new Object[] { new OreItems("foodRice", 4), new ItemStack(ItemInit.vinegar, 3), new OreItems("dustSalt", 2), new OreItems("dustSugar", 2) },
			new ItemStack[] { new ItemStack(ItemInit.sushi_salmon, 6) }
		));

		// 卵焼き寿司
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.tamagoyaki, 2),
			new Object[] { new OreItems("foodRice", 4), new ItemStack(ItemInit.vinegar, 3), new OreItems("dustSalt", 2), new OreItems("dustSugar", 2), new ItemStack(ItemInit.dry_seaweed, 2) },
			new ItemStack[] { new ItemStack(ItemInit.sushi_egg, 8) }
		));

		// ピーチタルト
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("cropPeach", 8),
			new Object[] { new OreItems("egg", 4), new OreItems("foodCream", 2), new OreItems("foodCustard", 3), new ItemStack(ItemInit.vannila_essence, 2), "foodLiquor", new OreItems("dustSugar", 2) },
			new ItemStack[] { new ItemStack(ItemInit.peach_tart, 8) }
		));

		// 桃のコンポーネント
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("cropPeach", 6),
			new Object[] { new OreItems("bucketWater", 6), new OreItems("dustSugar", 3), "cropLemon" },
			new ItemStack[] { new ItemStack(ItemInit.peach_compote, 6) }
		));

		// フルーツサンド
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("listAllfruit", 3),
			new Object[] { new OreItems("listAllfruit", 3), new OreItems("bread", 3), new OreItems("foodCream", 3), new OreItems("dustSugar", 2) },
			new ItemStack[] { new ItemStack(ItemInit.sandwich_fruit, 8) }
		));

		// サンドイッチ
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("cropLettuce", 3),
			new Object[] { new OreItems("listAllveggie", 3), new OreItems("bread", 3), new ItemStack(ItemInit.mayonnaise, 3), new OreItems("foodButter", 2) },
			new ItemStack[] { new ItemStack(ItemInit.sandwich, 8) }
		));

		// 卵サンド
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("egg", 3),
			new Object[] { new OreItems("cropLettuce", 3), new OreItems("bread", 3), new OreItems("foodButter", 2), "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.egg_sandwitch, 8) }
		));

		// ソフトクリーム（バニラ）
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("bucketMilk", 4),
			new Object[] { new OreItems("foodCream", 4), new ItemStack(ItemInit.vannila_essence, 4), "dustSalt", "foodGelatine" },
			new ItemStack[] { new ItemStack(ItemInit.softcream_vannila, 8) }
		));

		// ソフトクリーム（ストロベリー）
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("bucketMilk", 4),
			new Object[] { new OreItems("foodCream", 4), new OreItems("cropStrawberry", 4), "dustSalt", "foodGelatine" },
			new ItemStack[] { new ItemStack(ItemInit.softcream_strawberry, 8) }
		));

		// ソフトクリーム（チョコ）
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("bucketMilk", 4),
			new Object[] { new OreItems("foodCream", 4), new ItemStack(ItemInit.chocolate, 2), "dustSalt", "foodGelatine" },
			new ItemStack[] { new ItemStack(ItemInit.softcream_chocolate, 8) }
		));

		// サーモンカルパッチョ
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(Items.FISH, 4, 1),
			new Object[] { new OreItems("cropOnion", 4), new ItemStack(ItemInit.olive_oil, 4), "cropLemon", "dustSalt", new OreItems("bucketWater", 2) },
			new ItemStack[] { new ItemStack(ItemInit.salmon_carpaccio, 8) }
		));

		// パンナコッタ
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("listAllberry", 4),
			new Object[] { new OreItems("foodCream", 6), new OreItems("bucketMilk", 6), "cropLemon", "foodGelatine", "bucketWater", new OreItems("dustSugar", 3) },
			new ItemStack[] { new ItemStack(ItemInit.panna_cotta, 12) }
		));

		// フルーツサラダ
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("listAllfruit", 4),
			new Object[] { new OreItems("cropApple", 2), new OreItems("cropLettuce", 4), "foodCheese", new ItemStack(ItemInit.salad_dressing, 3) },
			new ItemStack[] { new ItemStack(ItemInit.salad_fruit, 6) }
		));

		// ピーマンのおかか醤油
		recipe.addRecipe(new FreezerRecipes(
			new OreItems("cropBellpepper", 4),
			new Object[] { new ItemStack(ItemInit.bonito_flakes, 3), new OreItems("foodSoysauce", 2), "foodOil" },
			new ItemStack[] { new ItemStack(ItemInit.peppers_with_soy_sauce_and_bonito, 6) }
		));
	}
}
