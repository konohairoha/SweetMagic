package sweetmagic.plugin.jei.mftank;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
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
		ingredients.setInput(ItemStack.class, this.inputs);
		ingredients.setOutput(ItemStack.class, this.output);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawInfo(Minecraft mine, int width, int height, int mouseX, int mouseY) {

		//文字列(テキストフォーマット付き可能)、X、Y、色コード、文字に影をつけるか
		int mf = 0;
		Item item = this.output.getItem();

		if(item == ItemInit.mf_sbottle) {
			mf = 1000;
		}

		else if(item == ItemInit.mf_bottle) {
			mf = 10000;
		}

		else if(item == ItemInit.mf_magiabottle) {
			mf = 100000;
		}

		mine.fontRenderer.drawString("require: " + String.format("%,d", mf) + "mf", 58, 40, 0xFFFFFF, true);
	}
}
