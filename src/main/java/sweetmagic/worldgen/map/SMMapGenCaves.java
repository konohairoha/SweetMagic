package sweetmagic.worldgen.map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.MapGenCaves;

public class SMMapGenCaves extends MapGenCaves {

	public boolean isACStone(IBlockState state) {
		return state.getBlock() instanceof BlockStone;
	}

	public boolean isACMisc(Block block) {
		return block instanceof BlockDirt || block instanceof BlockSand;
	}

	@Override
	public boolean canReplaceBlock(IBlockState state1, IBlockState state2) {
		return this.isACStone(state1) ? true : isACMisc(state1.getBlock()) ? true : super.canReplaceBlock(state1, state2);
	}
}
