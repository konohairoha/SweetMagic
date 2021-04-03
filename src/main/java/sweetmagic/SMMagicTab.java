package sweetmagic;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import sweetmagic.init.ItemInit;

public class SMMagicTab extends CreativeTabs {

	public SMMagicTab(String type) {
		super(type);
	}

	public ItemStack getTabIconItem() {
		return new ItemStack(ItemInit.aether_wand);
	}
}
