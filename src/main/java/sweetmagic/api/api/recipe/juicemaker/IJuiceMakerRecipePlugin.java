package sweetmagic.api.recipe.juicemaker;

public interface IJuiceMakerRecipePlugin {

	// アルストロメリアレシピ登録処理
	default void registerJuiceMakerRecipe(JuiceMakerRecipes recipe) {}
}
