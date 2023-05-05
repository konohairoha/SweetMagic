package sweetmagic.api.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class NormalRecipeInfo {

	/**
	 * レシピに使用するアイテム情報
	 * アイテム情報…[インベントリ内にアイテムが存在するポインタ、アイテム、アイテムの個数]
	 */
	public List<Object[]> inputItems;

	// 手に持ってるアイテム
	public ItemStack handItem;
	public List<Object> outputs;
	public List<ItemStack> outputItems;
	public boolean canComplete;
	public boolean keepTag;

	// 鉱石辞書対応用メインアイテムリスト
	public List<ItemStack> handList;

	// 初期化用
	public NormalRecipeInfo() {
		this.inputItems = null;
		this.outputItems = null;
		this.canComplete = false;
	}

	// 手に持ってるアイテム情報
	public ItemStack getHandItem() {
		return this.handItem;
	}

	public List<Object> getOutputItems() {
		return this.outputs;
	}

	public List<Object[]> getinputs() {
		List<Object[]> ipts = new ArrayList<Object[]>(this.inputItems);
		return ipts;
	}

	public List<ItemStack> getOutputs() {
		return this.outputItems;
	}

	// 鉱石辞書対応版メインアイテムリスト
	public List<ItemStack> getHandList () {
		return this.handList;
	}

	public boolean canCompleteRecipe() {
		return this.canComplete;
	}
}
