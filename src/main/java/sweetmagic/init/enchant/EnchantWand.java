package sweetmagic.init.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.ISMArmor;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.EnchantInit;

public class EnchantWand extends Enchantment {

	public final String name;
	public final int maxLevel;
	public final static EnumEnchantmentType type = EnumHelper.addEnchantmentType("wandEncha", s -> s instanceof IWand);

	public EnchantWand(Enchantment.Rarity rarity, String name, int level) {
		super(rarity, type, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		this.setRegistryName(SweetMagicCore.MODID, name);
		this.name = name;
		this.maxLevel = level;
		EnchantInit.enchantList.add(this);
	}

	public boolean canApplyAtEnchantingTable(ItemStack stack) {

		Item item = stack.getItem();

		if (this == EnchantInit.mfRecover && item instanceof ISMArmor) {
			return true;
		}

		return item instanceof IWand;
	}

	@Override
	public int getMaxLevel() {
		return this.maxLevel;
	}

	@Override
	public String getName() {
		return "enchantment." + this.name + ".name";
	}

	public int getMinEnchantability(int level) {
		return 0 + level * 4;
	}

	public int getMaxEnchantability(int level) {
		return this.getMinEnchantability(level) + 20;
	}
}
