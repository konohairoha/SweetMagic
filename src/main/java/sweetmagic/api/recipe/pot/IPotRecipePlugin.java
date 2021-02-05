package sweetmagic.api.recipe.pot;

public interface IPotRecipePlugin {

	// アルストロメリアレシピ登録処理
	default void registerPotRecipe(PotRecipes recipe) {}
}
