package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.base.BaseModelBlock;

public class SMGlassPane extends BaseModelBlock {

	private final int data;
	protected static final PropertyBool BACK = PropertyBool.create("back");
	protected static final PropertyBool FORWARD = PropertyBool.create("forward");
	protected static final PropertyBool LEFT = PropertyBool.create("left");
	protected static final PropertyBool RIGHT = PropertyBool.create("right");

    private static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] {
		new AxisAlignedBB(0.4375D, 0D, 0.4375D, 0.5625D, 1D, 0.5625D),
		new AxisAlignedBB(0.4375D, 0D, 0.4375D, 0.5625D, 1D, 1D),
		new AxisAlignedBB(0D, 0D, 0.4375D, 0.5625D, 1D, 0.5625D),
		new AxisAlignedBB(0D, 0D, 0.4375D, 0.5625D, 1D, 1D),
		new AxisAlignedBB(0.4375D, 0D, 0D, 0.5625D, 1D, 0.5625D),
		new AxisAlignedBB(0.4375D, 0D, 0D, 0.5625D, 1D, 1D),
		new AxisAlignedBB(0D, 0D, 0D, 0.5625D, 1D, 0.5625D),
		new AxisAlignedBB(0D, 0D, 0D, 0.5625D, 1D, 1D),
		new AxisAlignedBB(0.4375D, 0D, 0.4375D, 1D, 1D, 0.5625D),
		new AxisAlignedBB(0.4375D, 0D, 0.4375D, 1D, 1D, 1D),
		new AxisAlignedBB(0D, 0D, 0.4375D, 1D, 1D, 0.5625D),
		new AxisAlignedBB(0D, 0D, 0.4375D, 1D, 1D, 1D),
		new AxisAlignedBB(0.4375D, 0D, 0D, 1D, 1D, 0.5625D),
		new AxisAlignedBB(0.4375D, 0D, 0D, 1D, 1D, 1D),
		new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 0.5625D),
		new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D)
	};

	public SMGlassPane(String name, List<Block> list, int data) {
		super(Material.GLASS, name);
        setHardness(0.2F);
		setResistance(1024F);
		setSoundType(SoundType.GLASS);
		this.data = data;
		//ブロックの光を透過する強さ　数値が高いほどブロックは不透明、光を通さないようになる。
		this.setLightOpacity(this.data == 1 ? 255 : 0);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(BACK, false)
				.withProperty(FORWARD, false)
				.withProperty(LEFT, false)
				.withProperty(RIGHT, false));
		list.add(this);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> aabbList, Entity entity, boolean bool) {

		state = this.getActualState(state, world, pos);
		addCollisionBoxToList(pos, aabb, aabbList, AABB_BY_INDEX[0]);

		if (state.getValue(FORWARD)) {
			addCollisionBoxToList(pos, aabb, aabbList, AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.NORTH)]);
		}

		if (state.getValue(BACK)) {
			addCollisionBoxToList(pos, aabb, aabbList, AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.SOUTH)]);
		}

		if (state.getValue(LEFT)) {
			addCollisionBoxToList(pos, aabb, aabbList, AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.WEST)]);
		}

		if (state.getValue(RIGHT)) {
			addCollisionBoxToList(pos, aabb, aabbList, AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.EAST)]);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB_BY_INDEX[getBoundingBoxIndex(this.getActualState(state, source, pos))];
	}

	public int getBoundingBoxIndex(EnumFacing facing) {
		return 1 << facing.getHorizontalIndex();
	}

	public int getBoundingBoxIndex(IBlockState state) {

		int i = 0;

		if (state.getValue(FORWARD)) {
			i |= getBoundingBoxIndex(EnumFacing.NORTH);
		}
		if (state.getValue(RIGHT)) {
			i |= getBoundingBoxIndex(EnumFacing.EAST);
		}
		if (state.getValue(BACK)) {
			i |= getBoundingBoxIndex(EnumFacing.SOUTH);
		}
		if (state.getValue(LEFT)) {
			i |= getBoundingBoxIndex(EnumFacing.WEST);
		}

		return i;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(FORWARD, this.canPaneConnectTo(world, pos, EnumFacing.NORTH))
				.withProperty(BACK, this.canPaneConnectTo(world, pos, EnumFacing.SOUTH))
				.withProperty(LEFT, this.canPaneConnectTo(world, pos, EnumFacing.WEST))
				.withProperty(RIGHT, this.canPaneConnectTo(world, pos, EnumFacing.EAST));
	}

	public boolean canPaneConnectTo(IBlockAccess world, BlockPos pos, EnumFacing face) {
		IBlockState state = world.getBlockState(pos.offset(face));
		return !(state.getBlock() instanceof BlockLeaves) &&
				(state.getBlock().canBeConnectedTo(world, pos.offset(face), face.getOpposite())
				|| this.canPaneConnectToBlock(state.getBlock())
				|| state.isSideSolid(world, pos.offset(face), face.getOpposite()));
	}

	public boolean canPaneConnectToBlock(Block block) {
		return block.getDefaultState().isFullCube() || block instanceof BlockGlass
				|| block == Blocks.STAINED_GLASS || block == Blocks.STAINED_GLASS_PANE || block instanceof BlockPane
				|| block instanceof SMGlassPane;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	public int quantityDropped(Random random) {
        return 1;
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

	//テクスチャが透明で、重ねて表示したい場合
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return false;
	}
}
