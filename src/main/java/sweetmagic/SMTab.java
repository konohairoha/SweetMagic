package sweetmagic;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import sweetmagic.init.BlockInit;

public class SMTab extends CreativeTabs {

	public SMTab(String type) {
		super(type);
	}

	public ItemStack getTabIconItem() {
		return new ItemStack(BlockInit.sannyflower_plant);
	}
}
