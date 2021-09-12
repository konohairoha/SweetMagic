package sweetmagic.init.item.sm.sweetmagic;

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

public class SMReturn extends SMItem {

    public SMReturn(String name) {
		super(name, ItemInit.magicList);
        setMaxStackSize(1);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    //クラフトしても帰ってくるように
    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        return stack.copy();
    }

    //ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
  		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.returnitem.name")));
  	}
}