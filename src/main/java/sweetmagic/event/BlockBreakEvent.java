package sweetmagic.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.init.PotionInit;

public class BlockBreakEvent {

	@SubscribeEvent
	public void onBlockBreakEvent(BlockEvent.BreakEvent event) {

		// クリエか破壊無効がついてないなら終了
		EntityPlayer player =  event.getPlayer();
		if (!player.isPotionActive(PotionInit.breakblock) || player.isCreative()) { return; }

		// FullCubeかつ採掘速度が1以上の場合はキャンセル
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		IBlockState state = event.getState();
		Block block = state.getBlock();
		if ( state.getBlockHardness(world, pos) < 1D || !block.isFullCube(state) || block == Blocks.MOB_SPAWNER ) { return; }

		event.setCanceled(true);
	}
}
