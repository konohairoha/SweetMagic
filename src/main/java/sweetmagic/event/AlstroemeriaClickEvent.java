package sweetmagic.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.crop.BlockAlstroemeria;

public class AlstroemeriaClickEvent {

	@SubscribeEvent
    public void rightClickBlock (RightClickBlock event) {

		ItemStack stack = event.getItemStack();
		if (stack.isEmpty()) { return; }

		EntityPlayer player = event.getEntityPlayer();
		if (!player.isSneaking()) { return; }

		BlockPos pos = event.getPos();
		IBlockState state = event.getWorld().getBlockState(pos);
		Block block = state.getBlock();

		if (block == BlockInit.twilight_alstroemeria && stack.getItem() != Item.getItemFromBlock(BlockInit.twilightlight)) {

			BlockAlstroemeria als = (BlockAlstroemeria) block;

			// 最大成長時にアルストロメリアクラフトの実行
			if (!player.world.isRemote && als.isMaxAge(state)) {
				als.getRecipeAlstroemeria(player.world, pos, player.inventory.mainInventory, player, stack, true);
			}
		}
    }
}
