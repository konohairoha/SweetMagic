package sweetmagic.plugin.jei.mftank;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;
import sweetmagic.recipe.JeiRecipeMFTank;

public class MFTankJeiRecipeWrapper implements IRecipeWrapper {

	public List<ItemStack> inputs;
	public ItemStack output;

	public MFTankJeiRecipeWrapper(JeiRecipeMFTank recipe) {
		this.inputs = new ArrayList<ItemStack>();
		this.inputs.add(recipe.getInput());
		this.output = recipe.getOutput();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, output);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		//文字列(テキストフォーマット付き可能)、X、Y、色コード、文字に影をつけるか
		if(output.isItemEqual(new ItemStack(ItemInit.mf_sbottle))) {
			minecraft.fontRenderer.drawString("require : 1000 mf", 58, 40, 0xFFFFFF, true);
		} else {
			minecraft.fontRenderer.drawString("require : 10000 mf", 58, 40, 0xFFFFFF, true);
		}
	}
}
