package sweetmagic.api.recipe.pan;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.ItemInit;
import sweetmagic.util.OreItems;

@SMPanRecipePlugin(priority = EventPriority.LOW)
public class PanRecipePlugin implements IPanRecipePlugin {

	@Override
	public void registerPanRecipe(PanRecipes recipe) {

		//レシピ登録方法…ItemStack(hand)、ItemStack[](Input)、ItemStack[](Output)
		//注意：handの中身をinputの方にも含めて定義すること(HandItemをShrinkしてないため)

		// ポップコーン(キャラメル)
		recipe.addRecipe(new PanRecipes(
			"cropCorn",
			new Object[] { "dustSugar", "foodOil"},
			new ItemStack[] { new ItemStack(ItemInit.caramel_popcorn, 2)}
		));

		// ポップコーン(キャラメル)
		recipe.addRecipe(new PanRecipes(
			new OreItems("seedCorn", 16),
			new Object[] { "dustSugar", "foodOil"},
			new ItemStack[] { new ItemStack(ItemInit.caramel_popcorn, 2)}
		));

		// ポップコーン(塩)
		recipe.addRecipe(new PanRecipes(
			"cropCorn",
			new Object[] { "dustSalt", "foodOil"},
			new ItemStack[] { new ItemStack(ItemInit.salt_popcorn, 2)}
		));

		// ポップコーン(塩)
		recipe.addRecipe(new PanRecipes(
			new OreItems("seedCorn", 16),
			new Object[] { "dustSalt", "foodOil"},
			new ItemStack[] { new ItemStack(ItemInit.salt_popcorn, 2)}
		));

		// 目玉焼き
		recipe.addRecipe(new PanRecipes(
			new OreItems("egg", 2),
			new Object[] { "dustSalt", "foodOil", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.medamayaki, 2)}
		));

		// 卵焼き
		recipe.addRecipe(new PanRecipes(
			new OreItems("egg", 3),
			new Object[] { new OreItems("dustSugar", 2), "foodOil"},
			new ItemStack[] { new ItemStack(ItemInit.tamagoyaki, 3)}
		));

		// きのこソテー
		recipe.addRecipe(new PanRecipes(
			new OreItems("listAllmushroom", 2),
			new Object[] { "foodButter", "cropLemon", "cropCabbage" },
			new ItemStack[] { new ItemStack(ItemInit.sauteed_mushrooms, 4)}
		));

		// カスタード
		recipe.addRecipe(new PanRecipes(
			new OreItems("bucketMilk", 2),
			new Object[] { new OreItems("egg", 2), "dustSalt", "dustFlour"},
			new ItemStack[] { new ItemStack(ItemInit.custard, 4)}
		));

		// フレンチトースト
		recipe.addRecipe(new PanRecipes(
			new OreItems("bread", 4),
			new Object[] { "foodButter", new OreItems("egg", 2), "bucketMilk", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.french_toast, 4)}
		));

		// クレープ
		recipe.addRecipe(new PanRecipes(
			"dustFlour",
			new Object[] { new OreItems("listAllfruit", 3), new OreItems("egg", 2), "bucketMilk", "foodCream", "bucketWater", "foodButter", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.fruit_crepe, 8)}
		));

		// 鯖味噌
		recipe.addRecipe(new PanRecipes(
			new ItemStack (Items.FISH, 2),
			new Object[] { new ItemStack(ItemInit.miso, 2), "bucketWater", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.saba_miso, 4) }
		));

		// ワッフル
		recipe.addRecipe(new PanRecipes(
			new OreItems("dustSugar", 3),
			new Object[] { new OreItems("dustFlour", 4), new OreItems("egg", 4), new OreItems("foodButter", 4), "dustSalt", "foodOil" },
			new ItemStack[] { new ItemStack(ItemInit.waffle, 10) }
		));

		// 野菜炒め
		recipe.addRecipe(new PanRecipes(
			new OreItems("cropCabbage", 3),
			new Object[] { new OreItems("listAllporkraw", 2), new OreItems("cropOnion", 4), new OreItems("cropCarrot", 3), new OreItems("foodOil", 2) },
			new ItemStack[] { new ItemStack(ItemInit.salad_mixoil, 3) }
		));

		// チャーハン
		recipe.addRecipe(new PanRecipes(
			new OreItems("foodRice", 8),
			new Object[] { new OreItems("cropOnion", 4), new OreItems("cropCarrot", 4), new ItemStack(Items.COOKED_PORKCHOP), new OreItems("egg", 8), "foodOil", "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.fried_rice, 8) }
		));

		// ハンバーグ
		recipe.addRecipe(new PanRecipes(
			new OreItems("listAllbeefraw", 6),
			new Object[] { new OreItems("dustBread", 6), new OreItems("egg", 8), new OreItems("bucketMilk", 2), new OreItems("foodOil", 2),
					"dustSalt", "foodLiquor", new OreItems("bucketWater", 2) },
			new ItemStack[] { new ItemStack(ItemInit.steak_hamburg, 8) }
		));

		// ハンバーガー
		recipe.addRecipe(new PanRecipes(
			new ItemStack(ItemInit.steak_hamburg, 2),
			new Object[] { new OreItems("bread", 4), new OreItems("cropOnion", 3), new OreItems("cropTomato", 4), new OreItems("cropLettuce", 3), new OreItems("foodCheese", 4) },
			new ItemStack[] { new ItemStack(ItemInit.hamburger, 4) }
		));

		// ほうれん草の卵とじ
		recipe.addRecipe(new PanRecipes(
			new OreItems("cropSpinach", 6),
			new Object[] { new OreItems("egg", 3), new OreItems("foodSoysauce", 2), new OreItems("foodLiquor", 2), new OreItems("foodOil", 2) },
			new ItemStack[] { new ItemStack(ItemInit.spinach_egg, 6) }
		));

		// ホットケーキ
		recipe.addRecipe(new PanRecipes(
			new ItemStack(ItemInit.vannila_essence, 2),
			new Object[] { new OreItems("egg", 3), new OreItems("dustFlour", 6), new OreItems("dustSugar", 3), new OreItems("bucketMilk", 2), new OreItems("foodOil", 2), "foodButter" },
			new ItemStack[] { new ItemStack(ItemInit.hotcake, 6) }
		));

		// 焼きおにぎり
		recipe.addRecipe(new PanRecipes(
			new OreItems("foodRice", 4),
			new Object[] { new OreItems("foodSoysauce", 4), new OreItems("foodLiquor", 2), new OreItems("foodOil", 1) },
			new ItemStack[] { new ItemStack(ItemInit.riceball_grilled, 4) }
		));

		// 酢豚
		recipe.addRecipe(new PanRecipes(
			new OreItems("listAllporkraw", 6),
			new Object[] { new OreItems("cropOnion", 4), new OreItems("cropCarrot", 3), new OreItems("cropBellpepper", 3), new OreItems("cropPineapple", 2), new OreItems("foodSoysauce", 4),
					new OreItems("foodLiquor", 3), new OreItems("foodVinegar", 2), "foodOil", new OreItems("bucketWater", 3), new OreItems("dustSugar", 2) },
			new ItemStack[] { new ItemStack(ItemInit.sweet_and_sour_pork, 6) }
		));

		// ピーマンの肉詰め
		recipe.addRecipe(new PanRecipes(
			new OreItems("cropBellpepper", 4),
			new Object[] { new OreItems("listAllmeatraw", 4), new OreItems("cropTomato", 2), new OreItems("egg", 2), new OreItems("dustBread", 2),
					new OreItems("bucketMilk", 2), "dustSalt", "foodLiquor", "foodOil" },
			new ItemStack[] { new ItemStack(ItemInit.peppers_stuffed_with_meat, 8) }
		));
	}
}
