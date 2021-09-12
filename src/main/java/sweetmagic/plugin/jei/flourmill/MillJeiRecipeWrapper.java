package sweetmagic.plugin.jei.flourmill;

import java.util.List;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.recipe.flourmill.FlourMillRecipes;

public class MillJeiRecipeWrapper implements IRecipeWrapper {

	private int extraLines;
	public List<ItemStack> inputs;
	public List<ItemStack> outputs;
	public IDrawable item;

	public MillJeiRecipeWrapper(FlourMillRecipes recipe) {
		this.inputs = recipe.getHandList();
		this.outputs = recipe.getOutputIngLists();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, this.inputs);
		ingredients.setOutputs(ItemStack.class, this.outputs);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		String rClick = I18n.format("info.right_click.name");
		String inv = I18n.format("info.subitem.name");

		//Splitで指定した文字列を使用して文字配列を分割し、改行コードのような扱いにしている
		String[] spInv = inv.split("<br>");

		// 文字列(テキストフォーマット付き可能)、X、Y、色コード、文字に影をつけるか
		minecraft.fontRenderer.drawString(rClick, recipeWidth / 2 - 20, 6, 0xFFFFFF, true);
		minecraft.fontRenderer.drawString("milltime : 5sec", 58, 40, 0xFFFFFF, true);

		this.drawStrings(spInv, minecraft);
	}

	// 複数個のアイテムを説明欄に記載するとき用楽メソッド
	public void drawStrings(String[] splits, Minecraft minecraft) {

		// 改行を再現しつつ描画
		for (int i = 0; i < splits.length; i++) {
			minecraft.fontRenderer.drawString(splits[i], 20, i * 9 + 76, 0xFFFFFF, true);
		}
	}
}
