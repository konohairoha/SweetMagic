package sweetmagic.api.recipe.juicemaker;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.ItemInit;

@SMJuiceMakerRecipePlugin(priority = EventPriority.LOW)
public class JuiceMakerRecipePlugin implements IJuiceMakerRecipePlugin {

	@Override
	public void registerJuiceMakerRecipe(JuiceMakerRecipes recipe) {

		//レシピ登録方法…ItemStack(hand)、ItemStack[](Input)、ItemStack[](Output)
		//注意：handの中身をinputの方にも含めて定義すること(HandItemをShrinkしてないため)

		// コーンスープ
		recipe.addRecipe(new JuiceMakerRecipes(
			new ItemStack(ItemInit.corn),
			new Object[] { "dustSalt", "bucketMilk", "cropOnion" },
			new ItemStack[] { new ItemStack(ItemInit.corn_soup, 3) }
		));

		// ベリーオレンジジュース
		recipe.addRecipe(new JuiceMakerRecipes(
			"listAllberry",
			new Object[] { "cropStrawberry", new ItemStack(ItemInit.orange), "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.berryorange_juice, 3) }
		));

		// イチゴミルク
		recipe.addRecipe(new JuiceMakerRecipes(
			"cropStrawberry",
			new Object[] { "cropStrawberry", "bucketMilk", "dustSugar"  },
			new ItemStack[] { new ItemStack(ItemInit.strawberrymilk, 3) }
		));

		// ココナッツジュース
		recipe.addRecipe(new JuiceMakerRecipes(
			new ItemStack(ItemInit.coconut),
			new Object[] { "cropLemon", "cropMelon", "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.coconut_juice, 3) }
		));

		// ココア
		recipe.addRecipe(new JuiceMakerRecipes(
			new ItemStack(ItemInit.cocoapowder),
			new Object[] { new ItemStack(ItemInit.cocoamass), "bucketMilk", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.cocoa, 3) }
		));

		// パンプキンスープ
		recipe.addRecipe(new JuiceMakerRecipes(
			new ItemStack(Blocks.PUMPKIN),
			new Object[] { "cropOnion", "bucketMilk", "foodCream" },
			new ItemStack[] { new ItemStack(ItemInit.pumpkin_soup, 3) }
		));

		// バナナすむーじ
		recipe.addRecipe(new JuiceMakerRecipes(
			new ItemStack(ItemInit.yogurt),
			new Object[] { "bucketMilk", new ItemStack(ItemInit.banana), "cropLemon" },
			new ItemStack[] { new ItemStack(ItemInit.banana_smoothy, 3) }
		));

		// ミックスジュース
		recipe.addRecipe(new JuiceMakerRecipes(
			new ItemStack(ItemInit.banana),
			new Object[] { "listAllfruit", "bucketMilk", new ItemStack(Blocks.ICE) },
			new ItemStack[] { new ItemStack(ItemInit.mixed_juice, 4) }
		));

		// コーヒー
		recipe.addRecipe(new JuiceMakerRecipes(
			new ItemStack(ItemInit.coffee_seed),
			new Object[] { new ItemStack(ItemInit.coffee_seed, 9), "waterBucket", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.coffee) }
		));

		// カフェラテ
		recipe.addRecipe(new JuiceMakerRecipes(
			new ItemStack(ItemInit.coffee_seed),
			new Object[] { new ItemStack(ItemInit.coffee_seed, 9), "bucketMilk", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.cafe_latte, 1) }
		));

	}
}
