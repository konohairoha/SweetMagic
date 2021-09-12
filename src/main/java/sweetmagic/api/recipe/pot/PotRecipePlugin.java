package sweetmagic.api.recipe.pot;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.ItemInit;
import sweetmagic.util.OreItems;

@SMPotRecipePlugin(priority = EventPriority.LOW)
public class PotRecipePlugin implements IPotRecipePlugin {

	@Override
	public void registerPotRecipe(PotRecipes recipe) {

		//レシピ登録方法…Object(hand)、Object[](Input)、ItemStack[](Output)
		//注意：handの中身をinputの方にも含めて定義すること(HandItemをShrinkしてないため)

		// 肉じゃが
		recipe.addRecipe(new PotRecipes(
			new OreItems("cropPotato", 2),
			new Object[] { "cropCarrot", "listAllbeefraw", "cropOnion", "cropGreenSoybeans", new OreItems("bucketWater", 2) },
			new ItemStack[] { new ItemStack(ItemInit.nikujaga, 4)}
		));

		// ぶり大根
		recipe.addRecipe(new PotRecipes(
			new ItemStack(Items.FISH),
			new Object[] { new ItemStack(ItemInit.j_radish), "bucketWater", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.buridaikon, 2)}
		));

		// 茹でた枝豆
		recipe.addRecipe(new PotRecipes(
			new OreItems("cropGreenSoybeans", 2),
			new Object[] { "bucketWater", "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.boiled_edamame, 3)}
		));

		// すき焼き
		recipe.addRecipe(new PotRecipes(
			new OreItems("listAllbeefraw", 2),
			new Object[] { "listAllmushroom", new OreItems("egg", 2), "cropLettuce", "foodOil", "bucketWater", "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.sukiyaki, 4)}
		));

		// ロールキャベツ
		recipe.addRecipe(new PotRecipes(
				new OreItems("cropCabbage", 4),
			new Object[] { "listAllbeefraw", "cropOnion", "listAllporkraw", "bucketMilk", new OreItems("bucketWater", 2), "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.roll_cabbage, 4)}
		));

		// ビーフシチュー
		recipe.addRecipe(new PotRecipes(
				"listAllbeefraw",
			new Object[] {  new OreItems("cropOnion", 2), new OreItems("cropTomato", 4), "cropCarrot",
							"foodOil", "dustFlour", "foodButter", new OreItems("bucketWater", 2), "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.beefstew, 6)}
		));

		// ホワイトシチュー
		recipe.addRecipe(new PotRecipes(
			new OreItems("listAllchikenraw", 2),
			new Object[] {  "cropPotato", "cropCarrot", "foodButter", "cropOnion", "dustFlour","bucketMilk", new OreItems("bucketWater", 2), "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.stew, 6)}
		));

		// かぼちゃの煮つけ
		recipe.addRecipe(new PotRecipes(
			new ItemStack(Blocks.PUMPKIN),
			new Object[] { "bucketWater", "dustSugar"},
			new ItemStack[] { new ItemStack(ItemInit.pumpkin_nituke, 3)}
		));

		// 栗きんとん
		recipe.addRecipe(new PotRecipes(
			new OreItems("cropChestnut", 4),
			new Object[] { new ItemStack(ItemInit.sweetpotato), new OreItems("bucketWater", 2), "dustSalt", new OreItems("dustSugar", 2)},
			new ItemStack[] { new ItemStack(ItemInit.kurikinton, 6) }
		));

		// ゼラチン
		recipe.addRecipe(new PotRecipes(
			new ItemStack(Items.LEATHER),
			new Object[] { "bucketWater"},
			new ItemStack[] { new ItemStack(ItemInit.gelatin, 2)}
		));

		// コロッケ
		recipe.addRecipe(new PotRecipes(
				new OreItems("cropPotato", 4),
			new Object[] { "listAllmeatraw", "cropOnion", "foodButter", "egg", "dustFlour", "dustBread", new OreItems("foodOil", 2) },
			new ItemStack[] { new ItemStack(ItemInit.croquette, 12)}
		));

		// 味噌汁
		recipe.addRecipe(new PotRecipes(
			new OreItems("foodMiso", 3),
			new Object[] { new OreItems("cropOnion", 2), new OreItems("cropRadish", 2), "cropSeaweed", new OreItems("bucketWater", 2) },
			new ItemStack[] { new ItemStack(ItemInit.soy_soup, 9) }
		));

		// 豚汁
		recipe.addRecipe(new PotRecipes(
			new OreItems("listAllporkraw"),
			new Object[] { new OreItems("foodMiso", 3), new OreItems("cropOnion", 2), new OreItems("cropRadish", 2), "cropCarrot", new OreItems("bucketWater", 2) },
			new ItemStack[] { new ItemStack(ItemInit.pork_soup, 8)}
		));

		// プレーンドーナツ
		recipe.addRecipe(new PotRecipes(
			new OreItems("dustFlour", 4),
			new Object[] { new OreItems("foodButter", 2), new OreItems("egg", 2), new OreItems("dustSugar", 3), new OreItems("bucketMilk", 2), new OreItems("foodOil", 2) },
			new ItemStack[] { new ItemStack(ItemInit.donut_plane, 8) }
		));

		// チョコドーナツ
		recipe.addRecipe(new PotRecipes(
			new OreItems("foodChocolate", 4),
			new Object[] { new OreItems("dustFlour", 3), new OreItems("foodButter", 2), new OreItems("egg", 2), new OreItems("dustSugar", 3),
							new OreItems("bucketMilk", 2), new OreItems("foodOil", 2) },
			new ItemStack[] { new ItemStack(ItemInit.donut_choco, 8) }
		));

		// ストロベリーチョコドーナツ
		recipe.addRecipe(new PotRecipes(
			new OreItems("cropStrawberry", 3),
			new Object[] { new OreItems("foodChocolate", 2), new OreItems("dustFlour", 3), new OreItems("foodButter", 2), new OreItems("egg", 2), new OreItems("dustSugar", 3),
							new OreItems("bucketMilk", 2), new OreItems("foodOil", 2) },
			new ItemStack[] { new ItemStack(ItemInit.donut_strawberrychoco, 8) }
		));

		// 栗ご飯
		recipe.addRecipe(new PotRecipes(
			new ItemStack(ItemInit.chestnut, 8),
			new Object[] { new OreItems("foodRice", 4), new OreItems("dustSalt", 2), new OreItems("bucketWater", 4), new ItemStack(ItemInit.seaweed, 4) },
			new ItemStack[] { new ItemStack(ItemInit.kurigohan, 4) }
		));

		// 牛丼
		recipe.addRecipe(new PotRecipes(
			new OreItems("listAllbeefraw", 3),
			new Object[] { new OreItems("foodRice", 6), new OreItems("cropOnion", 3), new OreItems("bucketWater", 3), "foodSoysauce",
					"foodLiquor", new OreItems("dustSugar", 2) },
			new ItemStack[] { new ItemStack(ItemInit.gyuudon, 6) }
		));

		// チー牛
		recipe.addRecipe(new PotRecipes(
			new OreItems("foodCheese", 2),
			new Object[] { new OreItems("listAllbeefraw", 3),new OreItems("foodRice", 6), new OreItems("cropOnion", 3), new OreItems("bucketWater", 3),
					"foodSoysauce", "foodLiquor", new OreItems("dustSugar", 2) },
			new ItemStack[] { new ItemStack(ItemInit.cheegyu, 6) }
		));

		// 豚丼
		recipe.addRecipe(new PotRecipes(
			new OreItems("listAllporkraw", 3),
			new Object[] { new OreItems("foodRice", 6), new OreItems("cropOnion", 3), new OreItems("bucketWater", 3), "foodSoysauce",
					"foodLiquor", new OreItems("dustSugar", 2) },
			new ItemStack[] { new ItemStack(ItemInit.butadon, 6) }
		));

		// ほうれん草のおひたし
		recipe.addRecipe(new PotRecipes(
			new ItemStack(ItemInit.spinach, 6),
			new Object[] { new ItemStack(ItemInit.bonito_flakes, 3), new OreItems("foodSoysauce", 2), new OreItems("bucketWater", 3), new OreItems("dustSalt", 2) },
			new ItemStack[] { new ItemStack(ItemInit.salad_ohitasi, 6) }
		));

		// フライドポテト
		recipe.addRecipe(new PotRecipes(
			new OreItems("cropPotato", 3),
			new Object[] { new OreItems("dustFlour", 2), new OreItems("dustSalt", 2), new OreItems("foodOil", 2) },
			new ItemStack[] { new ItemStack(ItemInit.fried_potato, 5) }
		));

		// モチ
		recipe.addRecipe(new PotRecipes(
			new OreItems("cropRice", 6),
			new Object[] { new OreItems("bucketWater", 4) },
			new ItemStack[] { new ItemStack(ItemInit.mochi, 6) }
		));

		// ずんだ餅
		recipe.addRecipe(new PotRecipes(
			new ItemStack(ItemInit.mochi, 4),
			new Object[] { new OreItems("cropGreenSoybeans", 3), "dustSugar" },
			new ItemStack[] { new ItemStack(ItemInit.zunda, 6) }
		));

		// プレッツェル
		recipe.addRecipe(new PotRecipes(
			new OreItems("foodButter", 2),
			new Object[] { new OreItems("dustFlour", 6), "bucketMilk", new OreItems("bucketWater", 2), new OreItems("dustSugar", 2), "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.pretzel, 8) }
		));
	}
}
