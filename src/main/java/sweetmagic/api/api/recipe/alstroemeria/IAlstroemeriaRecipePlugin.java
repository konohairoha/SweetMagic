package sweetmagic.api.recipe.alstroemeria;

import sweetmagic.api.recipe.alstroemeria.AlstroemeriaRecipes;

public interface IAlstroemeriaRecipePlugin {

	// アルストロメリアレシピ登録処理
	default void registerAlstroemeriaRecipe(AlstroemeriaRecipes recipe) {}
}
