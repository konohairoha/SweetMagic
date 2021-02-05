package sweetmagic.init.item.sm.sweetmagic;

import net.minecraft.block.Block;
import net.minecraft.item.ItemDoor;
import sweetmagic.init.ItemInit;

public class SMIDoor extends ItemDoor {

	public SMIDoor(String name, Block block) {
		super(block);
        setUnlocalizedName(name);
        setRegistryName(name);
		ItemInit.itemList.add(this);
    }
}