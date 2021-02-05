package sweetmagic;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import sweetmagic.init.ItemInit;

public class SMFoodTab extends CreativeTabs {

	public SMFoodTab(String type) {
		super(type);
	}

	public ItemStack getTabIconItem() {
		return new ItemStack(ItemInit.sugarbell);
	}
}
