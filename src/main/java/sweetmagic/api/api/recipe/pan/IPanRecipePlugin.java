package sweetmagic.api.recipe.pan;

public interface IPanRecipePlugin {

	// アルストロメリアレシピ登録処理
	default void registerPanRecipe(PanRecipes recipe) {}
}
