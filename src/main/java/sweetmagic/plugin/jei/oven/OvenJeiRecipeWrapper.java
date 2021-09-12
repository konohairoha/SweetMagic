package sweetmagic.plugin.jei.oven;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.recipe.oven.OvenRecipes;

public class OvenJeiRecipeWrapper implements IRecipeWrapper {

	private int extraLines;
	public List<ItemStack> handList;
	public List<List<ItemStack>> inputList;
	public List<List<ItemStack>> prevList;
	public ItemStack[] outputs;
	public IDrawable item;

	public OvenJeiRecipeWrapper(OvenRecipes recipe) {

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
		ingredients.setOutput(ItemStack.class, outputs[0]);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawInfo(Minecraft mine, int width, int height, int mouseX, int mouseY) {

		String rClick = I18n.format("info.right_click.name");
		String inv = I18n.format("info.inv_in.name");

		// Splitで指定した文字列を使用して文字配列を分割し、改行コードのような扱いにしている
		String[] spInv = inv.split("<br>");

		// 文字列(テキストフォーマット付き可能)、X、Y、色コード、文字に影をつけるか
		mine.fontRenderer.drawString(rClick, width / 2 - 20, 6, 0xFFFFFF, true);
		mine.fontRenderer.drawString("cooktime : 5sec", 58, 40, 0xFFFFFF, true);

		this.drawStrings(spInv, mine);
	}

	// 複数個のアイテムを説明欄に記載するとき用楽メソッド
	public void drawStrings(String[] splits, Minecraft minecraft) {

		// 改行を再現しつつ描画
		for (int i = 0; i < splits.length; i++) {
			minecraft.fontRenderer.drawString( splits[i], 20, i * 9 + 80, 0xFFA000, true);
		}
	}
}
