package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SMFuel extends SMItem {

	private final int time;

	public SMFuel(String name, int fuel) {
		super(name);
		this.time = fuel;
    }

	public SMFuel(String name, int fuel, List<Item> list) {
		super(name, list);
		this.time = fuel;
	}

	@Override
	public int getItemBurnTime(ItemStack stack){
		return this.time;
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag){
  		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(I18n.format(TextFormatting.YELLOW + this.getTip("tip.burntick.name") +" : " + time));
  	}
}