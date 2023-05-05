package sweetmagic.api.recipe.flourmill;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.util.SMUtil;

public class FlourMillRecipes {

	public ArrayList<ItemStack> handList = new ArrayList<ItemStack>();
	public List<ItemStack> resultItems = new ArrayList<ItemStack>();

	// ハンドアイテム
	public List<ItemStack> getHandList () {
		return this.handList;
	}

	public FlourMillRecipes() {
        this.clear();
    }

    public FlourMillRecipes(Object hand , /*Object[] inputs, */ ItemStack[] outputs) {
        this.clear();
        this.handList = SMUtil.getOreList(hand);

//        //特殊処理：ハンド1個しかレシピにないのでもしレシピに被りがあったら例外を起こす
//        for(FlourMillRecipes recipelist :  SweetMagicAPI.millRecipe) {
//        	for (ItemStack stack : this.handList) {
//            	if(stack.isItemEqual((ItemStack) hand)) {
//            		throw new IllegalArgumentException( "this handItem: ( ItemStack or String ) is already defined to FlourMill recipes." );
//            	}
//        	}
//        }

        //例外処理：完成品アイテム定義が10個以上入ってたらレシピ上限違反
        if(outputs.length > 10) throw new IndexOutOfBoundsException("Out of bounds Alstroemeria Recipes. [outputItems]   Caused recipe's outputItems_length:" + outputs.length);

        for (ItemStack stack : outputs) {
        	this.resultItems.add(stack);
        }
    }

    // 初期化
    public void clear() {
    	this.handList = new ArrayList<ItemStack>();
    	this.resultItems = new ArrayList<ItemStack>();
    }

    // JEIレシピ連携用
    public List<ItemStack> getOutputIngLists() {
    	return this.resultItems;
    }

    public List<ItemStack> getOutputItemStack() {
        return this.resultItems;
    }

    //レシピ登録
    public static void addRecipe(FlourMillRecipes recipes) {
    	SweetMagicAPI.millRecipe.add(recipes);
    }
}
