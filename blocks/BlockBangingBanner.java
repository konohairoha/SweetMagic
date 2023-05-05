package sweetmagic.init.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.util.FaceAABB;

public class BlockBangingBanner extends BaseFaceBlock {

	public static final PropertyBool POLE = PropertyBool.create("pole");
	private final static AxisAlignedBB[] WALLRACK = new FaceAABB(0.45D, 0.575D, 0.075D, 0.55D, 0.675D, 1D).getRotatedBounds();

	public BlockBangingBanner(String name) {
		super(Material.WOOD, name);
		setHardness(0.2F);
		setResistance(1024F);
		setSoundType(SoundType.METAL);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH).withProperty(POLE, false));
		BlockInit.furniList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return WALLRACK[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, POLE});
	}


	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    	Block block = world.getBlockState(pos.offset(state.getValue(FACING).rotateY().rotateY())).getBlock();
		return state.withProperty(POLE, block instanceof BlockPole);
	}

	// IBlockStateからItemStackのmetadataを生成。ドロップ時とテクスチャ・モデル参照時に呼ばれる
	@Override
	public int getMetaFromState(IBlockState state) {
		return super.getMetaFromState(state) + (!state.getValue(POLE) ? 5 : 0);
	}

	// ItemStackのmetadataからIBlockStateを生成。設置時に呼ばれる
	@Override
	public IBlockState getStateFromMeta(int meta) {

		boolean isTop = false;

		if (meta > 4) {
			meta -= 5;
			isTop = true;
		}

		return super.getStateFromMeta(meta).withProperty(POLE, !isTop);
	}
}
