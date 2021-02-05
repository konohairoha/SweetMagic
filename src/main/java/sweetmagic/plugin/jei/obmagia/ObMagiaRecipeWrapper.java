package sweetmagic.plugin.jei.obmagia;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.recipe.obmagia.ObMagiaRecipes;

public class ObMagiaRecipeWrapper implements IRecipeWrapper {

	private int extraLines;
	public List<ItemStack> handList;
	public List<List<ItemStack>> inputList;
	public ItemStack[] outputs;
	public IDrawable item;
	private IGuiHelper guiHelper;

	public ObMagiaRecipeWrapper(ObMagiaRecipes recipe) {
		this.handList = recipe.getHandList();
		this.outputs = recipe.getOutputIngArray();

		this.inputList = new ArrayList<List<ItemStack>>();
		for (List<ItemStack> stackList : recipe.getInputList()) {
			this.inputList.add(stackList);
		}
	}

	//ここでセットすることが必ず必要っぽい…？でも未検証
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, this.handList);
		ingredients.setInputLists(ItemStack.class, this.inputList);
		ingredients.setOutput(ItemStack.class, outputs[0]);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		String rClick = I18n.format("info.right_click.name");
		minecraft.fontRenderer.drawString(rClick, recipeWidth / 2 - 20, 7, 0xFFFFFF, true);

		String inv = I18n.format("info.inv_in.name");

		// Splitで指定した文字列を使用して文字配列を分割し、改行コードのような扱いにしている
		String[] spInv = inv.split("<br>");

		// 文字列(テキストフォーマット付き可能)、X、Y、色コード、文字に影をつけるか
		if(outputs.length > 1) {
            minecraft.fontRenderer.drawString(String.valueOf(outputs.length), recipeWidth / 2 + 33, 30, 0xFFFFFF, true);
        }
		drawStrings(spInv, minecraft);

	}

	// 複数個のアイテムを説明欄に記載するとき用楽メソッド
	public void drawStrings(String[] splits, Minecraft minecraft) {

		// 改行を再現しつつ描画
		for (int i = 0; i < splits.length; i++) {
			minecraft.fontRenderer.drawString( splits[i], 20, i * 9 + 80, 0xFFA000, true);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return null;
	}
}

