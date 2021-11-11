package sweetmagic.init.block.blocks;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockFenceVertical extends BlockSMFence {

	private static final PropertyInteger CENTER = PropertyInteger.create("center", 0, 2);

	public BlockFenceVertical(String name, int data) {
		super(name, data);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(BACK, false)
				.withProperty(FORWARD, false)
				.withProperty(LEFT, false)
				.withProperty(RIGHT, false)
				.withProperty(CENTER, 0));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(FORWARD, this.canConnect(world, pos, EnumFacing.NORTH))
				.withProperty(BACK, this.canConnect(world, pos, EnumFacing.SOUTH))
				.withProperty(LEFT, this.canConnect(world, pos, EnumFacing.WEST))
				.withProperty(RIGHT, this.canConnect(world, pos, EnumFacing.EAST))
				.withProperty(CENTER, this.checkCenter(world, pos));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BACK, FORWARD, LEFT, RIGHT, CENTER });
	}
}
