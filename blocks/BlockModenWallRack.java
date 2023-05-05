package sweetmagic.init.block.blocks;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.util.FaceAABB;

public class BlockModenWallRack extends BlockModenRack {

	public static final PropertyBool TOP = PropertyBool.create("top");
	private final static AxisAlignedBB[] WALLRACK = new FaceAABB(0D, 0.9375D, 0.25D, 1D, 1D, 1D).getRotatedBounds();
	private final static AxisAlignedBB[] WALLRACK_BOT = new FaceAABB(0D, 0.3125D, 0.25D, 1D, 0.375D, 1D).getRotatedBounds();

	public BlockModenWallRack (String name, int data) {
		super(name, data);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH).withProperty(TOP, false));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		if (!state.getValue(TOP)) {
			return WALLRACK[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		}

		else {
			return WALLRACK_BOT[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		}
	}

	@Deprecated
	public Vec3d getOffset(IBlockState state, IBlockAccess world, BlockPos pos) {
		return new Vec3d(0D, !state.getValue(TOP) ? 0D : -0.625D, 0D);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, TOP});
	}

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float x, float y, float z, int meta, EntityLivingBase placer) {

    	if (face == EnumFacing.UP) {
    		y = 0F;
    	}

    	else if (face == EnumFacing.DOWN) {
    		y = 1F;
    	}

        return this.getStateFromMeta(meta).withProperty(TOP, y < 0.5F).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

	// IBlockStateからItemStackのmetadataを生成。ドロップ時とテクスチャ・モデル参照時に呼ばれる
	@Override
	public int getMetaFromState(IBlockState state) {
		return super.getMetaFromState(state) + (!state.getValue(TOP) ? 5 : 0);
	}

	// ItemStackのmetadataからIBlockStateを生成。設置時に呼ばれる
	@Override
	public IBlockState getStateFromMeta(int meta) {

		boolean isTop = false;

		if (meta > 4) {
			meta -= 5;
			isTop = true;
		}

		return super.getStateFromMeta(meta).withProperty(TOP, !isTop);
	}
}
