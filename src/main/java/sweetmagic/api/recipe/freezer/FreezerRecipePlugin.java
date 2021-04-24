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
			new Object[] { "listAllfruit", "listAllfruit", new ItemStack(ItemInit.vannila_essence), "dustSugar", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.fluit_mix, 6) }
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
			new OreItems("cropAplle", 2),
			new Object[] { new OreItems("dustSugar", 6), new OreItems("bucketWater", 2), new OreItems("stickWood", 2) },
			new ItemStack[] { new ItemStack(ItemInit.applecandy, 2) }
		));
	}
}
