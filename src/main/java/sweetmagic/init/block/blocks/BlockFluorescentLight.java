package sweetmagic.init.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.util.FaceAABB;

public class BlockFluorescentLight extends BaseFaceBlock {

	public static final PropertyBool TOP = PropertyBool.create("top");
	protected static final PropertyInteger SIDE = PropertyInteger.create("side", 0, 2);
	private final static AxisAlignedBB[] WALLRACK = new FaceAABB(0D, 0D, 0.9375D, 1D, 0.0625D, 1D).getRotatedBounds();
	private final static AxisAlignedBB[] WALLRACK_BOT = new FaceAABB(0D, 0.9375D, 0.9375D, 1D, 1D, 1D).getRotatedBounds();

	public BlockFluorescentLight(String name) {
		super(Material.WOOD, name);
        setHardness(0.1F);
		setResistance(1024F);
		setSoundType(SoundType.GLASS);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH).withProperty(TOP, false).withProperty(SIDE, 0));
        setLightLevel(1F);
		BlockInit.furniList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		if (!state.getValue(TOP)) {
			return WALLRACK_BOT[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		}

		else {
			return WALLRACK[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		}
	}

	@Deprecated
	public Vec3d getOffset(IBlockState state, IBlockAccess world, BlockPos pos) {
		return new Vec3d(0D, !state.getValue(TOP) ? 0.9375D : 0D, 0D);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, TOP, SIDE});
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

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

		int side = 0;

		EnumFacing face = state.getValue(FACING);
		IBlockState leftState = world.getBlockState(pos.offset(face.rotateY()));
		Block leftBlock = leftState.getBlock();

		IBlockState rightState = world.getBlockState(pos.offset(face.rotateYCCW()));
		Block rightBlock = rightState.getBlock();

		if (leftBlock.isFullBlock(leftState) && !rightBlock.isFullBlock(rightState)) {
			side = 1;
		}

		else if (!leftBlock.isFullBlock(leftState) && rightBlock.isFullBlock(rightState)) {
			side = 2;
		}

		return state.withProperty(SIDE, side);
	}
}
