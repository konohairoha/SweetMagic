package sweetmagic.init.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPillarStone extends AntiqueBrick {

	public static final PropertyBool TOP = PropertyBool.create("top");
	public static final PropertyBool BOT = PropertyBool.create("bot");

    public BlockPillarStone(String name) {
        super(name, 1F, 1024F, 0, 0);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(TOP, false)
				.withProperty(BOT, false));
    }

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		Block block = world.getBlockState(pos.down()).getBlock();
		boolean bot = block == this;
		boolean top = world.getBlockState(pos.up()).getBlock() == this;
		return state.withProperty(TOP, bot).withProperty(BOT, top);
	}

	// 一番下か
	public boolean isBot (IBlockState state) {
		return state == state.withProperty(TOP, false).withProperty(BOT, true) ||
				state == state.withProperty(TOP, false).withProperty(BOT, false);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { TOP, BOT });
	}
}
