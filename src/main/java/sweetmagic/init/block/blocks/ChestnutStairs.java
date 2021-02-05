package sweetmagic.init.block.blocks;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import sweetmagic.init.BlockInit;

public class ChestnutStairs extends BlockStairs{

//	private boolean isFragile = false;

	public ChestnutStairs(String name, IBlockState state) {
		super(state);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setHardness(0.5F);
        setResistance(1024F);
		this.useNeighborBrightness = true;
		this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT));
		BlockInit.blockList.add(this);
	}
}
