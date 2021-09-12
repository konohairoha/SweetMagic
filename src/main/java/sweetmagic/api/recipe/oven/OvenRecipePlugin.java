package sweetmagic.api.recipe.oven;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.ItemInit;
import sweetmagic.util.OreItems;

@SMOvenRecipePlugin(priority = EventPriority.LOW)
public class OvenRecipePlugin implements IOvenRecipePlugin {

	@Override
	public void registerOvenRecipe(OvenRecipes recipe) {

		//レシピ登録方法…ItemStack(hand)、ItemStack[](Input)、ItemStack[](Output)
		//注意：handの中身をinputの方にも含めて定義すること(HandItemをShrinkしてないため)

		//レモンクッキー
		recipe.addRecipe(new OvenRecipes(
			new OreItems("cropLemon", 2),
			new Object[] { new OreItems("egg", 2), "foodButter", "foodOil", new OreItems("dustSugar", 2), "dustSalt", "dustFlour" },
			new ItemStack[] { new ItemStack(ItemInit.lemon_cookie, 8)}
		));

		// アイスボックスクッキー
		recipe.addRecipe(new OvenRecipes(
			new OreItems("egg", 2),
			new Object[] { "foodButter", "dustFlour", new OreItems("dustSugar", 2), new ItemStack(ItemInit.cocoamass), new ItemStack(ItemInit.cocoabutter) },
			new ItemStack[] { new ItemStack(ItemInit.icebox_cookie, 8)}
		));

		//blueberryマフィン
		recipe.addRecipe(new OvenRecipes(
			new OreItems("cropBlueberry", 4),
			new Object[] { new OreItems("egg", 2), new OreItems("dustSugar", 2), new OreItems("dustFlour", 2), "bucketMilk", "foodButter" },
			new ItemStack[] { new ItemStack(ItemInit.blueberry_muffin, 4)}
		));

		//チョコマフィン
		recipe.addRecipe(new OvenRecipes(
			new ItemStack(ItemInit.cocoapowder, 4),
			new Object[] {new OreItems("dustFlour", 2), new OreItems("egg", 2), new OreItems("dustSugar", 2), "bucketMilk", "foodButter" },
			new ItemStack[] { new ItemStack(ItemInit.chocolate_muffin, 4)}
		));

		// アップルマフィン
		recipe.addRecipe(new OvenRecipes(
			new OreItems("cropApple", 2),
			new Object[] { new ItemStack(ItemInit.vannila_essence, 2), new OreItems("dustFlour", 2), new OreItems("egg", 2), new OreItems("dustSugar", 2), "bucketMilk", "foodButter" },
			new ItemStack[] { new ItemStack(ItemInit.apple_muffin, 4)}
		));

		//サーモンのムニエル
		recipe.addRecipe(new OvenRecipes(
			new ItemStack(Items.FISH, 1, 1),
			new Object[] { new OreItems("dustFlour", 2), new OreItems("foodOil", 2), new OreItems("dustSalt", 2), "foodButter" },
			new ItemStack[] { new ItemStack(ItemInit.salmon_meuniere, 3)}
		));

		//ポテトバター
		recipe.addRecipe(new OvenRecipes(
			new OreItems("cropPotato", 2),
			new Object[] { "dustSalt", new OreItems("foodButter", 2) },
			new ItemStack[] { new ItemStack(ItemInit.potatobutter, 4) }
		));

		// スポンジ
		recipe.addRecipe(new OvenRecipes(
			"dustFlour",
			new Object[] { new OreItems("egg", 2), "bucketMilk", "foodButter", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.cake_dough, 4)}
		));

		// 焼きとうもろこし
		recipe.addRecipe(new OvenRecipes(
			"cropCorn",
			new Object[] { "foodButter"},
			new ItemStack[] { new ItemStack(ItemInit.cooked_corn, 2) }
		));

		// イチゴタルト
		recipe.addRecipe(new OvenRecipes(
			new OreItems("cropStrawberry", 6),
			new Object[] { "foodCream", new OreItems("foodCustard", 2), "dustFlour", "egg", "foodButter",  new OreItems("dustSugar", 2) },
			new ItemStack[] { new ItemStack(ItemInit.strawberry_tart, 6)}
		));

		// トースト
		recipe.addRecipe(new OvenRecipes(
			new OreItems("dustFlour", 3),
			new Object[] { "foodButter", "bucketMilk", new OreItems("bucketWater", 2), "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.toast, 8)}
		));

		// スコーン
		recipe.addRecipe(new OvenRecipes(
			new OreItems("dustFlour", 2),
			new Object[] { "foodButter", "bucketMilk", new OreItems("dustSugar", 2) },
			new ItemStack[] { new ItemStack(ItemInit.scone, 2)}
		));

		// アップルパイ
		recipe.addRecipe(new OvenRecipes(
			new OreItems("cropApple", 3),
			new Object[] { new OreItems("foodCustard", 2), "egg", "dustSugar",  new OreItems("dustFlour", 3), "bucketWater", "foodButter" },
			new ItemStack[] { new ItemStack(ItemInit.applepie, 6) }
		));

		// クリームパン
		recipe.addRecipe(new OvenRecipes(
			new OreItems("foodCustard", 4),
			new Object[] { new OreItems("bread", 2), "egg" },
			new ItemStack[] { new ItemStack(ItemInit.cream_filled_roll, 4) }
		));

		// チョココロネ
		recipe.addRecipe(new OvenRecipes(
			new OreItems("bread", 3),
			new Object[] { new ItemStack(ItemInit.chocolate, 4), new OreItems("dustSugar", 2), "bucketMilk", "foodButter","egg" },
			new ItemStack[] { new ItemStack(ItemInit.choko_cornet, 10) }
		));

		// モンブラン
		recipe.addRecipe(new OvenRecipes(
			new OreItems("cropChestnut", 3),
			new Object[] { new OreItems("dustSugar", 2), new OreItems("foodCream", 3), "foodDough" },
			new ItemStack[] { new ItemStack(ItemInit.mont_blanc, 3)}
		));

		// 小倉トースト
		recipe.addRecipe(new OvenRecipes(
			new OreItems("bread", 2),
			new Object[] { new ItemStack(ItemInit.azuki_seed, 4), new OreItems("foodButter", 4) },
			new ItemStack[] { new ItemStack(ItemInit.ogura_toast, 4) }
		));

		// バタートースト
		recipe.addRecipe(new OvenRecipes(
			new OreItems("bread", 2),
			new Object[] { new OreItems("foodButter", 2) },
			new ItemStack[] { new ItemStack(ItemInit.butter_toast, 2) }
		));

		// ジャムトースト
		recipe.addRecipe(new OvenRecipes(
			new OreItems("bread", 2),
			new Object[] { new OreItems("cropStrawberry", 4), new OreItems("dustSalt", 2) },
			new ItemStack[] { new ItemStack(ItemInit.jam_toast, 4)}
		));

		// シュークリーム
		recipe.addRecipe(new OvenRecipes(
			new OreItems("foodCustard", 2),
			new Object[] { "egg", "foodButter", new OreItems("dustFlour", 4), "dustSalt", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.cream_puff, 6) }
		));

		// チョコレートケーキ
		recipe.addRecipe(new OvenRecipes(
			"foodDough",
			new Object[] { new ItemStack(ItemInit.chocolate, 2), new OreItems("foodCream", 2), new ItemStack(ItemInit.cocoapowder, 4) },
			new ItemStack[] { new ItemStack(ItemInit.chocolate_cake, 4) }
		));

		// チーズケーキ
		recipe.addRecipe(new OvenRecipes(
			new OreItems("foodCheese", 3),
			new Object[] { "cropLemon", "foodButter", new OreItems("egg", 2), new OreItems("dustFlour", 2), "foodCream", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.cheese_cake, 5) }
		));

		// チョコパイ
		recipe.addRecipe(new OvenRecipes(
			new ItemStack(ItemInit.chocolate),
			new Object[] { new OreItems("dustFlour", 2), "foodButter", "egg", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.choco_pie, 4) }
		));

		// スイートポテト
		recipe.addRecipe(new OvenRecipes(
			new ItemStack(ItemInit.sweetpotato, 4),
			new Object[] { "foodButter", "bucketMilk", "egg", "dustSalt", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.sweet_potato, 6) }
		));

		// ピザ
		recipe.addRecipe(new OvenRecipes(
			new OreItems("cropTomato", 4),
			new Object[] { new OreItems("cropOnion", 2), new OreItems("listAllmeatraw", 2), new OreItems("foodCheese", 4), new OreItems("dustFlour", 4), "dustSalt", "dustSugar", "bucketWater", new OreItems("foodOil", 2) },
			new ItemStack[] { new ItemStack(ItemInit.pizza, 8) }
		));

		// グラタン
		recipe.addRecipe(new OvenRecipes(
			new OreItems("listAllchikenraw", 2),
			new Object[] { new OreItems("foodCheese", 3), new OreItems("cropOnion", 2),  new OreItems("dustFlour", 3),  new OreItems("foodButter", 2), "dustSalt", "bucketMilk", "foodOil" },
			new ItemStack[] { new ItemStack(ItemInit.gratin, 12) }
		));

		// クレームブリュレ
		recipe.addRecipe(new OvenRecipes(
			new OreItems("foodCustard", 3),
			new Object[] { new ItemStack(ItemInit.vannila_essence, 2), new OreItems("dustSugar", 2) },
			new ItemStack[] { new ItemStack(ItemInit.cream_brulee, 6) }
		));

		// ラスベリーパイ
		recipe.addRecipe(new OvenRecipes(
			new ItemStack(ItemInit.raspberry, 4),
			new Object[] { "cropLemon", new OreItems("foodCustard", 2),  new OreItems("dustFlour", 2), new OreItems("foodButter", 2), new OreItems("dustSugar", 2), "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.raspberrypie, 6) }
		));

		// バームクーヘン
		recipe.addRecipe(new OvenRecipes(
			new OreItems("bucketMilk", 2),
			new Object[] { new OreItems("dustFlour", 3), new OreItems("egg", 2), "foodButter", "dustSugar", "foodOil", new ItemStack(ItemInit.vannila_essence) },
			new ItemStack[] { new ItemStack(ItemInit.german_tree_cake, 4) }
		));

		// タルトタタン
		recipe.addRecipe(new OvenRecipes(
			new OreItems("cropAplle", 12),
			new Object[] { new OreItems("dustFlour", 4), new OreItems("egg", 2), new OreItems("bucketMilk", 4), new OreItems("foodButter", 2), new OreItems("dustSugar", 2), "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.talttatan, 8) }
		));

		// ガトーショコラ
		recipe.addRecipe(new OvenRecipes(
			new ItemStack(ItemInit.cocoapowder, 8),
			new Object[] { new ItemStack(ItemInit.chocolate, 2), new OreItems("foodCream", 2), new OreItems("dustFlour", 4), new OreItems("egg", 2), new OreItems("foodButter", 2), "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.gateau_chocolat, 6) }
		));

		// ジャムクッキー
		recipe.addRecipe(new OvenRecipes(
			new OreItems("cropStrawberry", 4),
			new Object[] { new OreItems("dustSugar", 4), "cropLemon", new OreItems("dustFlour", 3), new OreItems("egg", 2), "foodButter", "bucketMilk" },
			new ItemStack[] { new ItemStack(ItemInit.cookie_jam, 8) }
		));

		// シフォンケーキ
		recipe.addRecipe(new OvenRecipes(
			new OreItems("egg", 6),
			new Object[] { new OreItems("dustSugar", 8), new OreItems("dustFlour", 2), new OreItems("bucketWater", 2), new OreItems("foodOil", 2) },
			new ItemStack[] { new ItemStack(ItemInit.cake_chiffon, 8) }
		));

		// きなこもち
		recipe.addRecipe(new OvenRecipes(
			new ItemStack(ItemInit.mochi, 4),
			new Object[] { new ItemStack(ItemInit.kinako, 4), new OreItems("dustSugar", 2), new OreItems("bucketWater", 3) },
			new ItemStack[] { new ItemStack(ItemInit.kinakomochi, 6) }
		));

		// おはぎ
		recipe.addRecipe(new OvenRecipes(
			new ItemStack(ItemInit.mochi, 4),
			new Object[] { new OreItems("cropBean", 5), new OreItems("dustSugar", 6), new OreItems("bucketWater", 3), new OreItems("dustSalt", 2) },
			new ItemStack[] { new ItemStack(ItemInit.ohagi, 8) }
		));

		// メロンパン
		recipe.addRecipe(new OvenRecipes(
			new OreItems("dustSugar", 4),
			new Object[] { new OreItems("egg", 2), new OreItems("foodButter", 2), new OreItems("bucketWater", 2), new OreItems("dustFlour", 6), "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.melon_bread, 8) }
		));

		// クロワッサン
		recipe.addRecipe(new OvenRecipes(
			new OreItems("bucketMilk", 2),
			new Object[] { new OreItems("dustFlour", 4), "egg", new OreItems("foodButter", 4), new OreItems("dustSugar", 2), "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.croissant, 7) }
		));

		// カヌレ
		recipe.addRecipe(new OvenRecipes(
			"foodLiquor",
			new Object[] { new OreItems("dustFlour", 4), "egg", new OreItems("foodButter", 2), new OreItems("bucketMilk", 2), new OreItems("dustSugar", 2), new ItemStack(ItemInit.vannila_essence) },
			new ItemStack[] { new ItemStack(ItemInit.canele, 6) }
		));

		// バターロール
		recipe.addRecipe(new OvenRecipes(
			new OreItems("foodButter", 4),
			new Object[] { new OreItems("dustFlour", 8),"egg", new OreItems("bucketWater", 2), "dustSugar", "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.butter_role, 12) }
		));

		// マドレーヌ
		recipe.addRecipe(new OvenRecipes(
			"foodLiquor",
			new Object[] { new OreItems("dustFlour", 4), "egg", new OreItems("foodButter", 2), new OreItems("dustSugar", 2), "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.madeleine, 6) }
		));
	}
}
