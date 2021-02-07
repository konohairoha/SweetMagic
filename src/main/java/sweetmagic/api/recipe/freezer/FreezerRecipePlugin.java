package sweetmagic.api.recipe.freezer;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.ItemInit;

@SMFreezerRecipePlugin(priority = EventPriority.LOW)
public class FreezerRecipePlugin implements IFreezerRecipePlugin {

	@Override
	public void registerFreezerRecipe(FreezerRecipes recipe) {

		//レシピ登録方法…ItemStack(hand)、ItemStack[](Input)、ItemStack[](Output)
		//注意：handの中身をinputの方にも含めて定義すること(HandItemをShrinkしてないため)

		// ミカンゼリー
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.orange),
			new Object[] { "foodGelatine", "dustSugar", "bucketWater"},
			new ItemStack[] { new ItemStack(ItemInit.orange_jelly, 2)}
		));

		// イチゴゼリー
		recipe.addRecipe(new FreezerRecipes(
			"cropStrawberry",
			new Object[] { "foodGelatine", "dustSugar", "bucketWater"},
			new ItemStack[] { new ItemStack(ItemInit.strawberry_jelly, 2)}
		));

		// レモンかき氷
		recipe.addRecipe(new FreezerRecipes(
			"cropLemon",
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", "bucketWater"},
			new ItemStack[] { new ItemStack(ItemInit.kakigori_lemon, 2)}
		));

		// イチゴかき氷
		recipe.addRecipe(new FreezerRecipes(
			"cropStrawberry",
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", "bucketWater"},
			new ItemStack[] { new ItemStack(ItemInit.kakigori_strawberry, 2)}
		));

		// 練乳かき氷
		recipe.addRecipe(new FreezerRecipes(
			"bucketMilk",
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", "bucketWater"},
			new ItemStack[] { new ItemStack(ItemInit.kakigori_milk, 2)}
		));

		// チョコレート
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.cocoamass),
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", "bucketMilk"},
			new ItemStack[] { new ItemStack(ItemInit.chocolate, 2)}
		));

		// ホワイトチョコレート
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.cocoabutter),
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", "bucketMilk"},
			new ItemStack[] { new ItemStack(ItemInit.white_chocolate, 2)}
		));

		// ようかん
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.azuki_seed),
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", new ItemStack(ItemInit.gelatin), "bucketWater"},
			new ItemStack[] { new ItemStack(ItemInit.youkan, 2)}
		));

		// ロールケーキ
		recipe.addRecipe(new FreezerRecipes(
			"foodDough",
			new Object[] { new ItemStack(Blocks.ICE), "dustSugar", "foodCream", "bucketMilk"},
			new ItemStack[] { new ItemStack(ItemInit.cake_roll, 4)}
		));

		// エクレア
		recipe.addRecipe(new FreezerRecipes(
			"foodCustard",
			new Object[] { new ItemStack(ItemInit.chocolate), "foodButter", "dustSalt", "egg", "bucketWater", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.eclair, 6)}
		));

		// マシュマロ
		recipe.addRecipe(new FreezerRecipes(
			"foodGelatine",
			new Object[] { "egg", new ItemStack(ItemInit.vannila_essence), "bucketWater", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.marshmallow, 8)}
		));

		// イチゴ大福
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.azuki_seed),
			new Object[] { "cropStrawberry", "dustFlour", "bucketWater", "dustSugar"},
			new ItemStack[] { new ItemStack(ItemInit.itigo_daihuku, 3)}
		));

		// プリン
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.vannila_essence),
			new Object[] { "egg", "bucketMilk", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.pudding, 3)}
		));

		// ショートケーキ
		recipe.addRecipe(new FreezerRecipes(
			"cropStrawberry",
			new Object[] { "foodDough", new ItemStack(ItemInit.vannila_essence), "foodCream", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.short_cake, 3)}
		));

		// フルーツポンチ
		recipe.addRecipe(new FreezerRecipes(
			new ItemStack(ItemInit.banana),
			new Object[] { "listAllfruit", "listAllfruit", new ItemStack(ItemInit.vannila_essence), "dustSugar", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.fluit_mix, 6)}
		));
	}
}
