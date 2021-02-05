package sweetmagic.plugin.jei.juicemaker;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import sweetmagic.api.recipe.juicemaker.JuiceMakerRecipes;

public class JuiceMakerJeiRecipeWrapper implements IRecipeWrapper {

	private int extraLines;
	public List<ItemStack> handList;
	public List<List<ItemStack>> inputList;
	public ItemStack[] outputs;
	public IDrawable item;

	public JuiceMakerJeiRecipeWrapper(JuiceMakerRecipes recipe) {
		this.handList = recipe.getHandList();
		this.outputs = recipe.getOutputIngArray();

		this.inputList = new ArrayList<List<ItemStack>>();
		for (List<ItemStack> stackList : recipe.getInputList()) {
			this.inputList.add(stackList);
		}
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, this.handList);
		ingredients.setInputLists(ItemStack.class, this.inputList);
		ingredients.setOutput(ItemStack.class, this.outputs[0]);
	}
}
