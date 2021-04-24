package sweetmagic.api.recipe.pedal;

import java.util.List;

import net.minecraft.item.ItemStack;
import sweetmagic.api.recipe.NormalRecipeInfo;

public class PedalRecipeInfo extends NormalRecipeInfo {

	// 初期化用
	public PedalRecipeInfo() {
		this.handList = null;
		this.inputItems = null;
		this.outputs = null;
		this.canComplete = false;
		this.keepTag = false;
	}

	// アイテム情報セット用初期化
	public PedalRecipeInfo(List<ItemStack> hand, List<Object[]> input, List<Object> outs, boolean keepTag) {
		this.handList = hand;
		this.inputItems = input;
		this.outputs = outs;
		this.keepTag = keepTag;
	}

	// 指定したアイテム情報、リザルトアイテムをこのクラスにセットする
	public PedalRecipeInfo setRecipeInfo(List<ItemStack> hand, List<Object[]> input, List<Object> outs, boolean keepTag) {
		this.handList = hand;
		this.inputItems = input;
		this.outputs = outs;
		this.keepTag = keepTag;
		return this;
	}

	// レシピ読み込みのNULLチェック　一つでもNULLになってたらダメ
	public boolean nullCheck() {
		return this.handItem == null || this.inputItems == null || this.outputItems == null;
	}
}
