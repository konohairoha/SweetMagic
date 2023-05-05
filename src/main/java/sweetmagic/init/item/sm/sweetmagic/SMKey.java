package sweetmagic.init.item.sm.sweetmagic;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;

public class SMKey extends SMItem {

    public SMKey(String name) {
		super(name, ItemInit.magicList);
        setMaxStackSize(1);
    }

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (this.isInCreativeTab(tab)) {
			ItemStack stack = new ItemStack(this);
			stack.addEnchantment(EnchantInit.aetherCharm, 1);
			list.add(stack);
		}
	}

	@Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {

		if(stack.isItemEnchanted()) { return; }

		stack.addEnchantment(EnchantInit.aetherCharm, 1);
    }

	// エンチャントエフェクト描画
	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
	    return false;
    }
}
