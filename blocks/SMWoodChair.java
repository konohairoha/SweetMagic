package sweetmagic.init.block.blocks;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class SMWoodChair extends SMChair {

	private static final PropertyBool LEFT = PropertyBool.create("left");
	private static final PropertyBool RIGHT = PropertyBool.create("right");

	public SMWoodChair (String name, int data) {
		super(name, data);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(LEFT, false).withProperty(RIGHT, false));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

		EnumFacing face = state.getValue(BlockModenRack.FACING);
		IBlockState west = world.getBlockState(pos.west());
		IBlockState east = world.getBlockState(pos.east());
		IBlockState north = world.getBlockState(pos.north());
		IBlockState south = world.getBlockState(pos.south());

		switch (face) {
		case NORTH:
			return state.withProperty(RIGHT, this.isShowCase(face, west)).withProperty(LEFT, this.isShowCase(face, east));
		case SOUTH:
			return state.withProperty(RIGHT, this.isShowCase(face, east)).withProperty(LEFT, this.isShowCase(face, west));
		case WEST:
			return state.withProperty(RIGHT, this.isShowCase(face, south)).withProperty(LEFT, this.isShowCase(face, north));
		case EAST:
			return state.withProperty(RIGHT, this.isShowCase(face, north)).withProperty(LEFT, this.isShowCase(face, south));
		}

		return super.getActualState(state, world, pos);
	}

	// ショーケース
	public boolean isShowCase (EnumFacing face, IBlockState state) {
		return state.getBlock() == this && face == state.getValue(BlockShowCase.FACING);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, RIGHT, LEFT });
	}
}
