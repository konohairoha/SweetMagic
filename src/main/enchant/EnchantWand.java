package sweetmagic.init.enchant;

import java.util.Arrays;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IChoker;
import sweetmagic.api.iitem.IHarness;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.api.iitem.IRobe;
import sweetmagic.api.iitem.ISMArmor;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.item.sm.magic.AetherHammer;
import sweetmagic.init.item.sm.magic.StarLightWand;

public class EnchantWand extends Enchantment {

	private final String name;
	private final int maxLevel;
	public final static EnumEnchantmentType type = EnumHelper.addEnchantmentType("wandEncha", s -> s instanceof IWand);
	private final List<Enchantment> wandEnchaList = Arrays.<Enchantment> asList(
		EnchantInit.aetherCharm, EnchantInit.maxMFUP, EnchantInit.mfRecover, EnchantInit.mfCostDown
	);

	public EnchantWand(Enchantment.Rarity rarity, String name, int level) {
		super(rarity, type, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		this.setRegistryName(SweetMagicCore.MODID, name);
		this.name = name;
		this.maxLevel = level;
		EnchantInit.enchantList.add(this);
	}

	public boolean canApplyAtEnchantingTable(ItemStack stack) {

		Item item = stack.getItem();

		if (item instanceof ISMArmor) {

			if (item instanceof IRobe || item instanceof IChoker) {
				return this == EnchantInit.mfRecover || this == EnchantInit.aetherCharm || this == EnchantInit.maxMFUP;
			}

			else if (item instanceof IPouch) {
				return this == EnchantInit.aetherCharm;
			}

			else if (item instanceof IHarness) {
				return this == EnchantInit.mfRecover || this == EnchantInit.aetherCharm || this == EnchantInit.maxMFUP;
			}
		}

		else if (item instanceof IWand) {

			if (this != EnchantInit.elementBonus) {
				return true;
			}

			IWand wand = (IWand) item;
			return wand.getTier() >= 5 || wand.isCreativeWand();
		}

		else if (item instanceof StarLightWand || item instanceof AetherHammer) {
			return this == EnchantInit.aetherCharm || this == EnchantInit.maxMFUP || this == EnchantInit.mfRecover || this == EnchantInit.mfCostDown;
		}

		return false;
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
