package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import sweetmagic.init.ItemInit;

public class SMItem extends Item {

	public SMItem(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        ItemInit.itemList.add(this);
    }

	public SMItem(String name, List<Item> list) {
        setUnlocalizedName(name);
        setRegistryName(name);
        list.add(this);
    }

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ) {
		return this.useStack(world, player, player.getHeldItem(hand), pos, facing);
	}

	public EnumActionResult useStack (World world, EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing face) {
		return EnumActionResult.PASS;
	}

	public String getTip (String tip) {
		return new TextComponentTranslation(tip, new Object[0]).getFormattedText();
	}
}
