package sweetmagic.api.recipe.mftable;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.util.SMUtil;

public class MFTableRecipes {

	public ArrayList<ItemStack> handList = new ArrayList<ItemStack>();
    public List<List<ItemStack>> inputList = new ArrayList<List<ItemStack>>();
	public List<Object> resultItems = new ArrayList<Object>();
    public int needLevel;

	// ハンドアイテム
	public List<ItemStack> getHandList () {
		return this.handList;
	}

	public MFTableRecipes() {
        this.clear();
    }

    public MFTableRecipes(Object hand , Object[] inputs ,Object[] outputs, int needLevel) {

        this.clear();
        this.handList = SMUtil.getOreList(hand);

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

        this.needLevel = needLevel;
	}

    // 初期化
    public void clear() {
    	this.handList = new ArrayList<ItemStack>();
    	this.resultItems = new ArrayList<Object>();
    	this.inputList = new ArrayList<List<ItemStack>>();
    	this.needLevel = 0;
    }

    public List<List<ItemStack>> getInputList() {
        return this.inputList;
    }

    public List<Object> getOutputItemStack() {
        return this.resultItems;
    }

    public ItemStack[] getOutputIngArray() {

        ItemStack[] arrays = new ItemStack[resultItems.size()];
        for(int i = 0; i < arrays.length; i++) {
        	arrays[i] = (ItemStack)resultItems.get(i);
        }
    	return arrays;
    }

    // 必要経験値の取得
    public int getNeedLevel () {
    	return this.needLevel;
    }

    //レシピ登録
    public static void addRecipe(MFTableRecipes recipes) {
    	SweetMagicAPI.mfTableRecipe.add(recipes);
    }
}
