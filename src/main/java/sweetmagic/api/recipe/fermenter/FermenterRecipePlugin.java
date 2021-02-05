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

		// とうもろこし
		recipe.addRecipe(new FermenterRecipes(
			new ItemStack(ItemInit.corn_seed),
			new Object[] { new ItemStack(ItemInit.corn_seed, 15) },
			new ItemStack[] { new ItemStack(ItemInit.corn)}
		));

		// ゾンビ肉
		recipe.addRecipe(new FermenterRecipes(
			new ItemStack(Items.ROTTEN_FLESH),
			new Object[] { new ItemStack(Items.ROTTEN_FLESH) },
			new ItemStack[] { new ItemStack(Items.LEATHER, 2)}
		));

		// ヨーグルト
		recipe.addRecipe(new FermenterRecipes(
			new OreItems("bucketMilk", 4),
			new Object[] { "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.yogurt, 4)}
		));

		// 味噌
		recipe.addRecipe(new FermenterRecipes(
			new OreItems("cropSoybean", 4),
			new Object[] { "dustSalt" },
			new ItemStack[] { new ItemStack(ItemInit.miso, 4)}
		));
	}
}
