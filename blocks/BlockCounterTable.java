package sweetmagic.init.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;

public class BlockCounterTable extends BaseFaceBlock {

	protected final int data;
	protected static final PropertyInteger CENTER = PropertyInteger.create("center", 0, 4);

	public BlockCounterTable (String name, int data) {
		super(Material.WOOD, name);
        setSoundType(data == 0 ? SoundType.STONE : SoundType.CLOTH);
        setHardness(0.25F);
        setResistance(1024F);
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH)
				.withProperty(CENTER, 0));
        this.data = data;
		BlockInit.furniList.add(this);
	}

	public BlockCounterTable (Material material, String name, int data) {
		super(material, name);
        setHardness(0.25F);
        setResistance(1024F);
        this.data = data;
		BlockInit.furniList.add(this);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(CENTER, this.checkCenter(world, pos));
	}

	// 両サイドにブロックがあるかどうか
	public int checkCenter (IBlockAccess world, BlockPos pos) {

		IBlockState state = world.getBlockState(pos);
		EnumFacing face = state.getValue(FACING);
		IBlockState north = world.getBlockState(pos.north());
		IBlockState south = world.getBlockState(pos.south());
		IBlockState west = world.getBlockState(pos.west());
		IBlockState east = world.getBlockState(pos.east());

		switch (face) {
		case NORTH:
			return this.getConnect(north, south, face);
		case SOUTH:
			return this.getConnect(south, north, face);
		case WEST:
			return this.getConnect(west, east, face);
		case EAST:
			return this.getConnect(east, west, face);
		}

		return 0;
	}

	public int getConnect (IBlockState block1, IBlockState block2, EnumFacing face) {

		if (this.canConnectBlock(block2, face.rotateY())) {
			return 3;
		}

		else if (this.canConnectBlock(block2, face.rotateYCCW())) {
			return 4;
		}

		else if (this.canConnectBlock(block1, face.rotateY())) {
			return 1;
		}

		else if (this.canConnectBlock(block1, face.rotateYCCW())) {
			return 2;
		}

		return 0;
	}

	// 繋がるかのチェック
	public boolean canConnectBlock(IBlockState state, EnumFacing face) {
		return this.isConnectBlock(state.getBlock()) && face == state.getValue(FACING);
	}

	public boolean isConnectBlock (Block block) {
		return block instanceof BlockCounterTable || block instanceof BlockStove || block instanceof BlockSink
				|| block instanceof BlockOven || block instanceof BlockWoodChest;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, CENTER });
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return this.data == 0;
	}
}
