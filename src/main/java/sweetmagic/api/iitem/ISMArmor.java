package sweetmagic.api.iitem;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public interface ISMArmor {

	default int getEnchantLevel (Enchantment enchant, ItemStack stack) {
		int level = EnchantmentHelper.getEnchantmentLevel(enchant, stack);
		return level > 10 ? 10 : level;
	}
}
