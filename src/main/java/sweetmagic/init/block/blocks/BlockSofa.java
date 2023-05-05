package sweetmagic.init.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.util.FaceAABB;
import sweetmagic.util.SittableUtil;

public class BlockSofa extends BlockCounterTable {

	private static final PropertyBool LEFT = PropertyBool.create("left");
	private static final PropertyBool RIGHT = PropertyBool.create("right");
	private final static AxisAlignedBB[] AABB = new FaceAABB(0D, 0D, 0.15D, 1D, 0.6D, 1D).getRotatedBounds();

	public BlockSofa(String name, int data) {
		super(name, data);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(CENTER, 0).withProperty(LEFT, false).withProperty(RIGHT, false));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
	}

	// 以下が座れるようにするための処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (!world.isRemote) {

			double y = 0;

			switch (this.data) {
			case 1:
				y = -0.025D;
				break;
			}

			if (SittableUtil.sitOnBlock(world, pos.getX(), pos.getY() + y, pos.getZ(), player, 6 * 0.0625)) {
				world.updateComparatorOutputLevel(pos, this);
			}
		}
		return true;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return SittableUtil.isSomeoneSitting(world, pos.getX(), pos.getY(), pos.getZ()) ? 1 : 0;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

		EnumFacing face = state.getValue(BlockModenRack.FACING);
		IBlockState west = world.getBlockState(pos.west());
		IBlockState east = world.getBlockState(pos.east());
		IBlockState north = world.getBlockState(pos.north());
		IBlockState south = world.getBlockState(pos.south());
		int center = this.checkCenter(world, pos);

		switch (face) {
		case NORTH:
			return state.withProperty(CENTER, center).withProperty(RIGHT, this.isShowCase(face, west, center)).withProperty(LEFT, this.isShowCase(face, east, center));
		case SOUTH:
			return state.withProperty(CENTER, center).withProperty(RIGHT, this.isShowCase(face, east, center)).withProperty(LEFT, this.isShowCase(face, west, center));
		case WEST:
			return state.withProperty(CENTER, center).withProperty(RIGHT, this.isShowCase(face, south, center)).withProperty(LEFT, this.isShowCase(face, north, center));
		case EAST:
			return state.withProperty(CENTER, center).withProperty(RIGHT, this.isShowCase(face, north, center)).withProperty(LEFT, this.isShowCase(face, south, center));
		}

		return state;
	}

	// ショーケース
	public boolean isShowCase (EnumFacing face, IBlockState state, int center) {
		return state.getBlock() == this && center == 0 &&
				(face == state.getValue(BlockShowCase.FACING) || face.rotateY() == state.getValue(BlockShowCase.FACING) || face.rotateYCCW() == state.getValue(BlockShowCase.FACING))
				&& state.getValue(CENTER) == 0;
	}

	public boolean isConnectBlock (Block block) {
		return block == this;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, CENTER, RIGHT, LEFT });
	}
}
