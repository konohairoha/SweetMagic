package sweetmagic.api.recipe.obmagia;

public interface IObMagiaRecipePlugin {

	// アルストロメリアレシピ登録処理
	default void registerObMagiaRecipe(ObMagiaRecipes recipe) {}
}
