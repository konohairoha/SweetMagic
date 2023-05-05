package sweetmagic.api.recipe.freezer;

public interface IFreezerRecipePlugin {

	// 冷蔵庫レシピ登録処理
	default void registerFreezerRecipe(FreezerRecipes recipe) {}
}
