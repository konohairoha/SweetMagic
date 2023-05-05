package sweetmagic.api.recipe.fermenter;

public interface IFermenterRecipePlugin {

	// アルストロメリアレシピ登録処理
	default void registerFermenterRecipe(FermenterRecipes recipe) {}
}
