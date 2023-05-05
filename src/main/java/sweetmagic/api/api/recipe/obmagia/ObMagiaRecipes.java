package sweetmagic.api.recipe.obmagia;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.util.SMUtil;

public class ObMagiaRecipes {

	public ArrayList<ItemStack> handList = new ArrayList<ItemStack>();
    public List<List<ItemStack>> inputList = new ArrayList<List<ItemStack>>();
	public ArrayList<Object> resultItems = new ArrayList<Object>();

	// ハンドアイテム
	public List<ItemStack> getHandList () {
		return this.handList;
	}

	public ObMagiaRecipes() {
        this.clear();
    }

    public ObMagiaRecipes(Object hand , Object[] inputs ,ItemStack[] outputs) {
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
			if (o instanceof ItemStack) {
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

    public List<List<ItemStack>> getInputList() {
        return this.inputList;
    }

    // JEI連携用
    public ItemStack[] getOutputIngArray() {

        ItemStack[] arrays = new ItemStack[resultItems.size()];
        for(int i = 0; i < arrays.length; i++) {
        	arrays[i] = (ItemStack)resultItems.get(i);
        }
    	return arrays;
    }

    public List<Object> getOutputItemStack() {
        return (List<Object>) this.resultItems.clone();
    }

    //レシピ登録
    public static void addRecipe(ObMagiaRecipes recipes) {
    	SweetMagicAPI.magiaRecipe.add(recipes);
    }
}
