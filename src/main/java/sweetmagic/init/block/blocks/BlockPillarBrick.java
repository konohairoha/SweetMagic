package sweetmagic.init.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.enumblock.EnumLocal;
import sweetmagic.api.enumblock.EnumLocal.PropertyLocal;

public class BlockPillarBrick extends AntiqueBrick {

	protected static final PropertyLocal LOCAL = new PropertyLocal("local", EnumLocal.getLocalList());
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.125D, 0D, 0.125D, 0.875D, 1D, 0.875D);

    public BlockPillarBrick(String name) {
        super(name, 1F, 1024F, 0, 0);
    	setDefaultState(this.blockState.getBaseState().withProperty(LOCAL, EnumLocal.NOR));
    }

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean top = this.checkBlock(world.getBlockState(pos.down()).getBlock());
		boolean bot = this.checkBlock(world.getBlockState(pos.up()).getBlock());
		return state.withProperty(LOCAL, EnumLocal.getLocal(top, bot));
	}

	public boolean checkBlock (Block block) {
		return block == this;
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
		return new BlockStateContainer(this, new IProperty[] { LOCAL });
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	// フェンスとかにつながないように
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
