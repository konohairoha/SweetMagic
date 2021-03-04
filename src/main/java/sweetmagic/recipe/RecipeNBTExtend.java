package sweetmagic.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeNBTExtend extends ShapedOreRecipe {

    public RecipeNBTExtend(ResourceLocation loc, ItemStack result, Object... recipe) {
        super(loc,result, recipe);
    }

    // クラフト時のリザルト
    @Override
    public ItemStack getCraftingResult(InventoryCrafting craft) {

    	// クラフト結果
        ItemStack result = super.getCraftingResult(craft);

		// 作業台のクラフトスロット分回す
		for (int idx = 0; idx < craft.getSizeInventory(); idx++) {

			// スロットのアイテムを受け取り
            ItemStack stack = craft.getStackInSlot(idx);
            if(stack.isEmpty() || !stack.hasTagCompound()) { continue; }

    		NBTTagCompound tags = stack.getTagCompound();
    		result.setTagCompound(tags);

    		// レッドベリルかつスロットのNBTを持っていれば
			break;
        }
        return result;
    }
}
