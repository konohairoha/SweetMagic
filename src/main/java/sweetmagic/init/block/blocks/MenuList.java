package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;

public class MenuList extends BaseFaceBlock {

	private final int data;
	private final static AxisAlignedBB WALL_NORTH = new AxisAlignedBB(0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D, 1D);
	private final static AxisAlignedBB WALL_SOUTH = new AxisAlignedBB(0.0625D, 0.0625D, 0D, 0.9375D, 0.9375D, 0.0625D);
	private final static AxisAlignedBB WALL_EAST = new AxisAlignedBB(0D, 0.085D, 0.0625D, 0.0625D, 0.9375D, 0.9375D);
	private final static AxisAlignedBB WALL_WEST = new AxisAlignedBB(0.9375D, 0.0625D, 0.0625D, 1D, 0.9375D, 0.9375D);

	public MenuList(String name, int data) {
		super(Material.WOOD, name);
		setHardness(0.1F);
		setResistance(1024F);
		setSoundType(SoundType.WOOD);
		this.data = data;
		BlockInit.furniList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (state.getValue(FACING)) {
		case NORTH: return WALL_NORTH;
		case SOUTH: return WALL_SOUTH;
		case EAST:  return WALL_EAST;
		case WEST:  return WALL_WEST;
		}

		return FULL_BLOCK_AABB;
	}
}
