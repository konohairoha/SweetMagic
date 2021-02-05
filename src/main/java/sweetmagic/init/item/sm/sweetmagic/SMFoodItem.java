package sweetmagic.init.item.sm.sweetmagic;

import net.minecraft.item.Item;
import sweetmagic.init.ItemInit;

public class SMFoodItem extends Item {

	public SMFoodItem(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        ItemInit.foodList.add(this);
    }
}
