package sweetmagic.api.recipe.fermenter;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.ItemInit;
import sweetmagic.util.OreItems;

@SMFermenterRecipePlugin(priority = EventPriority.LOW)
public class FermenterRecipePlugin implements IFermenterRecipePlugin {

	@Override
	public void registerFermenterRecipe(FermenterRecipes recipe) {

		// チーズ
		recipe.addRecipe(new FermenterRecipes(
			"cropLemon",
			new Object[] { new OreItems("bucketMilk", 4), "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.cheese, 6) }
		));

		// 菌糸ブロック
		recipe.addRecipe(new FermenterRecipes(
			"listAllmushroom",
			new Object[] { new ItemStack(Blocks.DIRT) },
			new ItemStack[] { new ItemStack(Blocks.MYCELIUM) }
		));

		// ポトゾル
		recipe.addRecipe(new FermenterRecipes(
			new ItemStack(Items.DYE, 1, 15),
			new Object[] { new ItemStack(Blocks.DIRT) },
			new ItemStack[] { new ItemStack(Blocks.DIRT, 1, 2) }
		));

		// 草ブロック
		recipe.addRecipe(new FermenterRecipes(
			new ItemStack(Blocks.DIRT, 16),
			new Object[] { "listAllseed" },
			new ItemStack[] { new ItemStack(Blocks.GRASS, 16) }
		));

		// とうもろこし
		recipe.addRecipe(new FermenterRecipes(
			"seedCorn",
			new Object[] { new OreItems("seedCorn", 15) },
			new ItemStack[] { new ItemStack(ItemInit.corn) }
		));

		// 革
		recipe.addRecipe(new FermenterRecipes(
			new ItemStack(Items.ROTTEN_FLESH),
			new Object[] { new ItemStack(ItemInit.fire_powder) },
			new ItemStack[] { new ItemStack(Items.LEATHER, 2) }
		));

		// ヨーグルト
		recipe.addRecipe(new FermenterRecipes(
			new OreItems("bucketMilk", 4),
			new Object[] { "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.yogurt, 4) }
		));

		// 味噌
		recipe.addRecipe(new FermenterRecipes(
			new OreItems("cropSoybean", 4),
			new Object[] { "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.miso, 4) }
		));

		// 骨粉
		recipe.addRecipe(new FermenterRecipes(
			new OreItems("listAllseed", 8),
			new Object[] { new OreItems("treeSapling", 4) },
			new ItemStack[] { new ItemStack(Items.DYE, 24, 15) }
		));

		// お酢
		recipe.addRecipe(new FermenterRecipes(
			new OreItems("cropRice", 6),
			new Object[] { new OreItems("bucketWater", 4) },
			new ItemStack[] { new ItemStack(ItemInit.vinegar, 4) }
		));

		// マヨネーズ
		recipe.addRecipe(new FermenterRecipes(
			new OreItems("foodVinegar", 4),
			new Object[] { new OreItems("egg", 2), "dustSalt", new OreItems("foodOil", 2) },
			new ItemStack[] { new ItemStack(ItemInit.mayonnaise, 8) }
		));

		// サラダドレッシング
		recipe.addRecipe(new FermenterRecipes(
			new OreItems("foodMayo", 4),
			new Object[] { new OreItems("foodVinegar", 2), "dustSalt", "dustSugar", "foodOil" },
			new ItemStack[] { new ItemStack(ItemInit.salad_dressing, 12) }
		));

		// 海苔
		recipe.addRecipe(new FermenterRecipes(
			"cropSeaweed",
			new Object[] { "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.dry_seaweed, 3) }
		));

		// 醤油
		recipe.addRecipe(new FermenterRecipes(
			"dustFlour",
			new Object[] { new OreItems("cropSoybean", 4), "dustSalt", "bucketWater" },
			new ItemStack[] { new ItemStack(ItemInit.soy_sauce, 12) }
		));

		// フルーツ酒
		recipe.addRecipe(new FermenterRecipes(
			new OreItems("listAllfruit", 8),
			new Object[] { new OreItems("bucketWater", 8), new OreItems("dustSugar", 8) },
			new ItemStack[] { new ItemStack(ItemInit.fruit_wine, 16) }
		));

		// かつお節
		recipe.addRecipe(new FermenterRecipes(
			new ItemStack(Items.FISH, 4, 0),
			new Object[] { new OreItems("dustSalt", 4) },
			new ItemStack[] { new ItemStack(ItemInit.bonito_flakes, 8) }
		));

		// ゾンビ肉
		recipe.addRecipe(new FermenterRecipes(
			new OreItems("listAllmeatraw", 2),
			new Object[] { new OreItems("slimeball", 2) },
			new ItemStack[] { new ItemStack(Items.ROTTEN_FLESH, 4) }
		));
	}
}
