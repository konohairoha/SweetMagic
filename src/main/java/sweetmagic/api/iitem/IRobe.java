package sweetmagic.api.iitem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IRobe extends ISMArmor {

	// GUIを開く
	void openGUI (World world, EntityPlayer player, ItemStack stack);

	// SMモブのダメージカット率（1だとダメージカット無し）
	default float getSMMobDamageCut () {
		return 0.75F;
	}

	// 魔法ダメージカット率（1だとダメージカット無し）
	default float getMagicDamageCut () {
		return 0.75F;
	}
}
