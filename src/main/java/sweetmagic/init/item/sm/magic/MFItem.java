package sweetmagic.init.item.sm.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.sweetmagic.SMItem;

public class MFItem extends SMItem {

	// 保有MF
	public int magiaflux;

	public MFItem (String name, int magiaflux) {
		super(name, ItemInit.magicList);
		this.magiaflux = magiaflux;
	}

	public int getMF() {
		return this.magiaflux;
	}

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		int itemMF = this.getMF();
		String mf = String.format("%,d", itemMF);
		String stackMF = String.format("%,d", itemMF * stack.getCount());
		tooltip.add(I18n.format(TextFormatting.GREEN + mf + "MF"));
		tooltip.add(I18n.format(TextFormatting.GREEN + "Stack：" + stackMF + "MF"));
	}
}
