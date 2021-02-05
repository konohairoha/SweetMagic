package sweetmagic.api.recipe.freezer;

import java.util.List;

import net.minecraft.item.ItemStack;
import sweetmagic.api.recipe.NormalRecipeInfo;

public class FreezerRecipeInfo extends NormalRecipeInfo {

	// 初期化用
	public FreezerRecipeInfo() {
		this.handList = null;
		this.outputs = null;
		this.canComplete = false;
	}

	// アイテム情報セット用初期化
	public FreezerRecipeInfo(List<ItemStack> hand, List<Object> outs) {
		this.handList = hand;
		this.outputs = outs;
	}

	// 指定したアイテム情報、リザルトアイテムをこのクラスにセットする
	public FreezerRecipeInfo setRecipeInfo(List<ItemStack> hand, List<Object> outputs) {
		this.handList = hand;
		this.outputs = outputs;
		return this;
	}

	// レシピ読み込みのNULLチェック　一つでもNULLになってたらダメ
	public boolean nullCheck() {
		return this.handList == null || this.outputs == null;
	}
}
