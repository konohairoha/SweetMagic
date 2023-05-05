package sweetmagic.api.iblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISlabItemBlock {

	EnumActionResult setBlock (World world, EntityPlayer player, BlockPos pos, ItemStack stack);
}
