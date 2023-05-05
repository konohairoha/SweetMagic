package sweetmagic.api.recipe.alstroemeria;

import java.util.List;

import net.minecraft.item.ItemStack;
import sweetmagic.api.recipe.NormalRecipeInfo;

public class AlstroemeriaRecipeInfo extends NormalRecipeInfo {

	// 初期化用
	public AlstroemeriaRecipeInfo() {
		this.handList = null;
		this.inputItems = null;
		this.outputs = null;
		this.canComplete = false;
	}

	// アイテム情報セット用初期化
	public AlstroemeriaRecipeInfo(List<ItemStack> hand, List<Object[]> input, List<Object> outs) {
		this.handList = hand;
		this.inputItems = input;
		this.outputs = outs;
	}

	// 指定したアイテム情報、リザルトアイテムをこのクラスにセットする
	public AlstroemeriaRecipeInfo setRecipeInfo(List<ItemStack> hand, List<Object[]> input, List<Object> outs) {
		this.handList = hand;
		this.inputItems = input;
		this.outputs = outs;
		return this;
	}

	// レシピ読み込みのNULLチェック　一つでもNULLになってたらダメ
	public boolean nullCheck() {
		return this.handItem == null || this.inputItems == null || this.outputItems == null;
	}
}
