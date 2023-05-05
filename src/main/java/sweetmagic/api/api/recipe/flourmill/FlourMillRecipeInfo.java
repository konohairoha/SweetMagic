package sweetmagic.api.recipe.flourmill;

import java.util.List;

import net.minecraft.item.ItemStack;
import sweetmagic.api.recipe.NormalRecipeInfo;

public class FlourMillRecipeInfo extends NormalRecipeInfo {

	// 初期化用
	public FlourMillRecipeInfo() {
		this.handList = null;
		this.outputItems = null;
		this.canComplete = false;
	}

	// アイテム情報セット用初期化
	public FlourMillRecipeInfo(List<ItemStack> hand, List<ItemStack> outputs) {
		this.handList = hand;
		this.outputItems = outputs;
	}

	// 指定したアイテム情報、リザルトアイテムをこのクラスにセットする
	public FlourMillRecipeInfo setRecipeInfo(List<ItemStack> hand, List<ItemStack> outputs) {
		this.handList = hand;
		this.outputItems = outputs;
		return this;
	}

	// レシピ読み込みのNULLチェック　一つでもNULLになってたらダメ
	public boolean nullCheck() {
		return this.handItem == null || this.outputItems == null;
	}
}
