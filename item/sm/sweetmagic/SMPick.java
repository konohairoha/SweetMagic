package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;

public class SMPick extends ItemPickaxe {

	public SMPick(String name, ToolMaterial material) {
		super(material);
		setUnlocalizedName(name);
        setRegistryName(name);
        ItemInit.itemList.add(this);
	}

	// エンチャントのレベルの取得
	public int getEnchant (Enchantment ench, ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(ench, stack);
	}

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		String tip = new TextComponentTranslation("tip.alt_pick.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GREEN + tip));
	}
}