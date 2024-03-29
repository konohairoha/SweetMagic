package sweetmagic.plugin.jei.pedal;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.recipe.pedal.PedalRecipes;

public class PedalJeiRecipeWrapper implements IRecipeWrapper {

	private int extraLines;
	public ItemStack hand;
	public List<ItemStack> handList;
	public List<List<ItemStack>> inputList;
	public List<List<ItemStack>> prevList;
	public ItemStack[] outputs;
	private int needMF;
	public IDrawable item;
	private IGuiHelper guiHelper;

	public PedalJeiRecipeWrapper(PedalRecipes recipe) {

		this.handList = recipe.getHandList();
		this.outputs = recipe.getOutputIngArray();

		this.prevList = Lists.newArrayList();
		this.prevList.add(this.handList);

		this.inputList = new ArrayList<List<ItemStack>>();
		for (List<ItemStack> stackList : recipe.getInputList()) {
			this.inputList.add(stackList);
			this.prevList.add(stackList);
		}

		this.needMF = recipe.getNeedMF();
	}

	// ここでセットすることが必ず必要っぽい…？でも未検証
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, this.prevList);
		ingredients.setOutput(ItemStack.class, outputs[0]);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		String rClick = I18n.format("info.right_click.name");
		String inv = I18n.format("info.inv_in.name");
		String needMF = I18n.format("info.needmf.name") + "： " + String.format("%,d", this.needMF) + "MF";

		// Splitで指定した文字列を使用して文字配列を分割し、改行コードのような扱いにしている
		String[] spInv = inv.split("<br>");

		// 文字列(テキストフォーマット付き可能)、X、Y、色コード、文字に影をつけるか
		minecraft.fontRenderer.drawString(rClick, recipeWidth / 2 - 20, 16, 0xFFFFFF, true);

		// 文字列(テキストフォーマット付き可能)、X、Y、色コード、文字に影をつけるか
		minecraft.fontRenderer.drawString(needMF, recipeWidth / 2 - 40, 68, 0xFF4830, true);

		this.drawStrings(spInv, minecraft);
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
