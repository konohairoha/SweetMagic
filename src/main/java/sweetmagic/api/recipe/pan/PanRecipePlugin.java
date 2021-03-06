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
			new ItemStack(ItemInit.corn),
			new Object[] { "dustSugar", "foodOil"},
			new ItemStack[] { new ItemStack(ItemInit.caramel_popcorn, 2)}
		));

		// ポップコーン(キャラメル)
		recipe.addRecipe(new PanRecipes(
			new ItemStack(ItemInit.corn_seed, 16),
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
			new ItemStack(ItemInit.corn_seed, 16),
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
			new OreItems("dustFlour"),
			new Object[] { new OreItems("listAllfruit", 3), new OreItems("egg", 2), "bucketMilk", "foodCream", "bucketWater", "foodButter", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.fruit_crepe, 8)}
		));

		// 鯖味噌
		recipe.addRecipe(new PanRecipes(
			new ItemStack (Items.FISH, 2),
			new Object[] { new ItemStack(ItemInit.miso, 2), "bucketWater", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.saba_miso, 4) }
		));
	}
}
