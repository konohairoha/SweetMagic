package sweetmagic.api.recipe.pot;

import java.util.List;

import net.minecraft.item.ItemStack;
import sweetmagic.api.recipe.NormalRecipeInfo;

public class PotRecipeInfo extends NormalRecipeInfo {

	// 初期化用
	public PotRecipeInfo() {
		this.handList = null;
		this.inputItems = null;
		this.outputs = null;
		this.canComplete = false;
	}

	// アイテム情報セット用初期化
	public PotRecipeInfo(List<ItemStack> hand, List<Object[]> input, List<Object> outs) {
		this.handList = hand;
		this.inputItems = input;
		this.outputs = outs;
	}

	// 指定したアイテム情報、リザルトアイテムをこのクラスにセット
	public PotRecipeInfo setRecipeInfo(List<ItemStack> hand, List<Object[]> input, List<Object> outputs) {
		this.handList = hand;
		this.inputItems = input;
		this.outputs = outputs;
		return this;
	}

	// レシピ読み込みのNULLチェック　一つでもNULLになってたらダメ
	public boolean nullCheck() {
		return this.handList == null || this.inputItems == null || this.outputs == null;
	}
}
