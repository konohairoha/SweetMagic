package sweetmagic.api.recipe.alstroemeria;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.util.SMUtil;

public class AlstroemeriaRecipes {

	public ArrayList<ItemStack> handList = new ArrayList<ItemStack>();
    public List<List<ItemStack>> inputList = new ArrayList<List<ItemStack>>();
	public ArrayList<Object> resultItems = new ArrayList<Object>();

    // メインアイテムリスト
	public List<ItemStack> getHandList () {
		return this.handList;
	}

	public AlstroemeriaRecipes() {
        this.clear();
    }

    public AlstroemeriaRecipes(Object hand , Object[] inputs ,Object[] outputs) {

        this.clear();
        this.handList = SMUtil.getOreList(hand);

        // 例外処理：レシピアイテム定義が9個以上入ってたらレシピ上限違反
        if(inputs.length > 9) {
        	throw new IndexOutOfBoundsException("Out of bounds Alstroemeria Recipes. [inputItems]   Caused recipe's inputItems_length:" + inputs.length);
        }

        // 例外処理：完成品アイテム定義が1個以上入ってたらレシピ上限違反
        if(outputs.length > 1) {
        	throw new IndexOutOfBoundsException("Out of bounds Alstroemeria Recipes. [outputItems]   Caused recipe's outputItems_length:" + outputs.length);
        }

        // inputのアイテム分回して鉱石辞書をチェックしてリストに突っ込む
        List<List<ItemStack>> input = new ArrayList<List<ItemStack>>();
        for (Object o : inputs) {
        	ArrayList<ItemStack> inputOres = new ArrayList<ItemStack>();
        	inputOres = SMUtil.getOreList(o);
        	input.add(inputOres);
        }

        this.inputList = input;

    	for (Object o : outputs) {
			if(o instanceof ItemStack){
				this.resultItems.add(o);
			}
			else throw new IllegalArgumentException("Not a itemStack");
        }

    }

    // 初期化
    public void clear() {
    	this.handList = new ArrayList<ItemStack>();
    	this.resultItems = new ArrayList<Object>();
    	this.inputList = new ArrayList<List<ItemStack>>();
    }

    public List<Object> getOutputItemStack() {
        return (List<Object>) this.resultItems.clone();
    }

    // JEI連携用
    public ItemStack[] getOutputIngArray() {

        ItemStack[] arrays = new ItemStack[resultItems.size()];
        for(int i = 0; i < arrays.length; i++) {
        	arrays[i] = (ItemStack)resultItems.get(i);
        }
    	return arrays;
    }

    public List<List<ItemStack>> getInputList() {
        return this.inputList;
    }

    //レシピ登録
    //Todo：レシピ削除用メソッドを作ること
    public static void addRecipe(AlstroemeriaRecipes recipes) {
    	SweetMagicAPI.alsRecipe.add(recipes);
    }
}
