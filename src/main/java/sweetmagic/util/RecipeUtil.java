package sweetmagic.util;

import java.util.List;

import net.minecraft.item.ItemStack;
import sweetmagic.api.recipe.NormalRecipeInfo;

public class RecipeUtil {

	public NormalRecipeInfo recipeInfo;
	public ItemStack copy;
	public List<ItemStack> inputs;
	public List<ItemStack> results;

	public RecipeUtil (NormalRecipeInfo recipeInfo, ItemStack copy, List<ItemStack> inputs, List<ItemStack> results) {
		this.recipeInfo = recipeInfo;
		this.copy = copy;
		this.inputs = inputs;
		this.results = results;
	}

	// レシピの取得
	public NormalRecipeInfo getRecipe () {
		return this.recipeInfo;
	}

	// ハンドの取得
	public ItemStack getHand () {
		return this.copy;
	}

	// 投入リストの取得
	public List<ItemStack> getInput () {
		return this.inputs;
	}

	// 出力リストの取得
	public List<ItemStack> getResult () {
		return this.results;
	}
}
