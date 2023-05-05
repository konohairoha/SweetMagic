package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class SMTable extends BaseModelBlock {

	private static final PropertyBool BACK = PropertyBool.create("back");
	private static final PropertyBool FORWARD = PropertyBool.create("forward");
	private static final PropertyBool LEFT = PropertyBool.create("left");
	private static final PropertyBool RIGHT = PropertyBool.create("right");
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0D, 0.5D, 0D, 1D, 1D, 1D);
	private final int data;

	public SMTable(String name, int data) {
		super(Material.WOOD, name);
		setSoundType(data == 2 ? SoundType.METAL : SoundType.WOOD);
		setHardness(0.5F);
        setResistance(1024F);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(BACK, false)
				.withProperty(FORWARD, false)
				.withProperty(LEFT, false)
				.withProperty(RIGHT, false));
		this.data = data;
		BlockInit.furniList.add(this);
	}

	/**
	 * 0 = 通常
	 * 1 = レース付き
	 * 2 = ステンレス
	 */

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return this.data == 1;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean back = this.getBlock(world, pos.south()) == this;
		boolean forward = this.getBlock(world, pos.north()) == this;
		boolean left = this.getBlock(world, pos.west()) == this;
		boolean right = this.getBlock(world, pos.east()) == this;
		return state.withProperty(BACK, back).withProperty(FORWARD, forward).withProperty(LEFT, left).withProperty(RIGHT, right);
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
		return new BlockStateContainer(this, new IProperty[] { BACK, FORWARD, LEFT, RIGHT });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    	return AABB;
    }
}
