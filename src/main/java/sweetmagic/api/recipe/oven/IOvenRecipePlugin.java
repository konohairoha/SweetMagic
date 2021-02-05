package sweetmagic.api.recipe.oven;

public interface IOvenRecipePlugin {

	// アルストロメリアレシピ登録処理
	default void registerOvenRecipe(OvenRecipes recipe) {}
}
