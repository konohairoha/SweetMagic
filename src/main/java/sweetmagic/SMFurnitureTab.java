package sweetmagic;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import sweetmagic.init.BlockInit;

public class SMFurnitureTab extends CreativeTabs {

	public SMFurnitureTab(String type) {
		super(type);
	}

	public ItemStack getTabIconItem() {
		return new ItemStack(BlockInit.awning_tent_r);
	}
}
