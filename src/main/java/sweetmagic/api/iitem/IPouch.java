package sweetmagic.api.iitem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IPouch extends ISMArmor {

	// GUIを開く
	void openGUI (World world, EntityPlayer player, ItemStack stack);

	default int getSlotSize () {
		return 18;
	}
}
