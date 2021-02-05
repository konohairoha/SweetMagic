package sweetmagic.api.recipe.flourmill;

public interface IFlourMillRecipePlugin {

	// アルストロメリアレシピ登録処理
	default void registerFlourMillRecipe(FlourMillRecipes recipe) {}
}
