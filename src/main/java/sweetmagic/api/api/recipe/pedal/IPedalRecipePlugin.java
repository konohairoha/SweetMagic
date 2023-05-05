package sweetmagic.api.recipe.pedal;

public interface IPedalRecipePlugin {

	// 創造の台座レシピ登録処理
	default void registerPedalRecipe(PedalRecipes recipe) {}
}
