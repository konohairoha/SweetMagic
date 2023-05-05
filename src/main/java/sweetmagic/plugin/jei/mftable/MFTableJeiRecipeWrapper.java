package sweetmagic.plugin.jei.mftable;

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
import sweetmagic.api.iitem.IWand;
import sweetmagic.api.recipe.mftable.MFTableRecipes;

public class MFTableJeiRecipeWrapper implements IRecipeWrapper {

	private int extraLines;
	public List<ItemStack> handList;
	public List<List<ItemStack>> inputList;
	public List<List<ItemStack>> prevList;
	public ItemStack[] outputs;
	public IDrawable item;
	public int needLevel;
	private IGuiHelper guiHelper;
	private IWand wand;

	public MFTableJeiRecipeWrapper(MFTableRecipes recipe) {

		this.handList = recipe.getHandList();
		this.outputs = recipe.getOutputIngArray();

		this.prevList = Lists.newArrayList();
		this.prevList.add(this.handList);

		this.inputList = new ArrayList<List<ItemStack>>();
		for (List<ItemStack> stackList : recipe.getInputList()) {
			this.inputList.add(stackList);
			this.prevList.add(stackList);
		}

		this.needLevel = recipe.getNeedLevel();
	}

	// ここでセットすることが必ず必要っぽい…？でも未検証
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, this.prevList);
		ingredients.setOutput(ItemStack.class, this.outputs[0]);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawInfo(Minecraft mine, int width, int height, int mouseX, int mouseY) {

		String rClick = I18n.format("info.right_click.name");
		mine.fontRenderer.drawString(rClick, width / 2 - 20, 7, 0xFFFFFF, true);

		String tipWand = I18n.format("info.wand_slot.name") + "： " + this.needLevel + "<br>";
		String inv = tipWand + I18n.format("info.wand_item.name");

		// Splitで指定した文字列を使用して文字配列を分割し、改行コードのような扱いにしている
		String[] spInv = inv.split("<br>");

		// 文字列(テキストフォーマット付き可能)、X、Y、色コード、文字に影をつけるか
		if (this.outputs.length > 1) {
			mine.fontRenderer.drawString(String.valueOf(this.outputs.length), width / 2 + 33, 30, 0xFFFFFF, true);
		}
		this.drawStrings(spInv, mine);

	}

	// 複数個のアイテムを説明欄に記載するとき用楽メソッド
	public void drawStrings(String[] splits, Minecraft minecraft) {

		// 改行を再現しつつ描画
		for (int i = 0; i < splits.length; i++) {
			minecraft.fontRenderer.drawString(splits[i], 20, i * 9 + 77, 0xFFA000, true);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return null;
	}
}
