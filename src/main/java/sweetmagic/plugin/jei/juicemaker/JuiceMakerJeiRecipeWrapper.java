package sweetmagic.plugin.jei.juicemaker;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import sweetmagic.api.recipe.juicemaker.JuiceMakerRecipes;

public class JuiceMakerJeiRecipeWrapper implements IRecipeWrapper {

	private int extraLines;
	public List<ItemStack> handList;
	public List<List<ItemStack>> inputList;
	public List<List<ItemStack>> prevList;
	public ItemStack[] outputs;
	public IDrawable item;

	public JuiceMakerJeiRecipeWrapper(JuiceMakerRecipes recipe) {

		this.handList = recipe.getHandList();
		this.outputs = recipe.getOutputIngArray();

		this.prevList = Lists.newArrayList();
		this.prevList.add(this.handList);

		this.inputList = new ArrayList<List<ItemStack>>();
		for (List<ItemStack> stackList : recipe.getInputList()) {
			this.inputList.add(stackList);
			this.prevList.add(stackList);
		}
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, this.prevList);
		ingredients.setOutput(ItemStack.class, this.outputs[0]);
	}
}
