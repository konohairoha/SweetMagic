package sweetmagic.api.recipe.mftable;

public interface IMFTableRecipePlugin {

	// アルストロメリアレシピ登録処理
	default void registerMFTableRecipe(MFTableRecipes recipe) {}
}
