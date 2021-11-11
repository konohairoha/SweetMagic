package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class IronFence extends BaseModelBlock {

	private static final PropertyBool BACK = PropertyBool.create("back");
	private static final PropertyBool FORWARD = PropertyBool.create("forward");
	private static final PropertyBool LEFT = PropertyBool.create("left");
	private static final PropertyBool RIGHT = PropertyBool.create("right");
	public static final AxisAlignedBB PILLAR_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.5D, 0.625D);
    public static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.625D, 0.625D, 1.5D, 1.0D);
    public static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.375D, 1.5D, 0.625D);
    public static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 0.375D);
    public static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.625D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);
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

	public IronFence(String name) {
		super(Material.GLASS, name);
        setHardness(0.2F);
		setResistance(1024F);
		setSoundType(SoundType.METAL);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(BACK, false)
				.withProperty(FORWARD, false)
				.withProperty(LEFT, false)
				.withProperty(RIGHT, false));
		BlockInit.furniList.add(this);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> aabbList, Entity entity, boolean bool) {

		state = this.getActualState(state, world, pos);

		if (!(entity instanceof IMob)) {

			addCollisionBoxToList(pos, aabb, aabbList, AABB_BY_INDEX[0]);

			if (state.getValue(FORWARD)) {
				addCollisionBoxToList(pos, aabb, aabbList, AABB_BY_INDEX[this.getAABBIndex(EnumFacing.NORTH)]);
			}

			if (state.getValue(BACK)) {
				addCollisionBoxToList(pos, aabb, aabbList, AABB_BY_INDEX[this.getAABBIndex(EnumFacing.SOUTH)]);
			}

			if (state.getValue(LEFT)) {
				addCollisionBoxToList(pos, aabb, aabbList, AABB_BY_INDEX[this.getAABBIndex(EnumFacing.WEST)]);
			}

			if (state.getValue(RIGHT)) {
				addCollisionBoxToList(pos, aabb, aabbList, AABB_BY_INDEX[this.getAABBIndex(EnumFacing.EAST)]);
			}
		}

		else {

			addCollisionBoxToList(pos, aabb, aabbList, PILLAR_AABB);

			if (state.getValue(FORWARD)) {
				addCollisionBoxToList(pos, aabb, aabbList, NORTH_AABB);
			}

			if (state.getValue(BACK)) {
				addCollisionBoxToList(pos, aabb, aabbList, SOUTH_AABB);
			}

			if (state.getValue(LEFT)) {
				addCollisionBoxToList(pos, aabb, aabbList, WEST_AABB);
			}

			if (state.getValue(RIGHT)) {
				addCollisionBoxToList(pos, aabb, aabbList, EAST_AABB);
			}
		}
	}

	protected static void addCollisionBoxToList(BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> aabbList, @Nullable AxisAlignedBB aabb2) {

		if (aabb2 != NULL_AABB) {
			AxisAlignedBB aabb3 = aabb2.offset(pos);

			if (aabb.intersects(aabb3)) {
				aabbList.add(aabb3);
			}
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB_BY_INDEX[this.getBoundingBoxIndex(this.getActualState(state, source, pos))];
	}

	public int getAABBIndex(EnumFacing face) {
		return 1 << face.getHorizontalIndex();
	}

	public int getBoundingBoxIndex(IBlockState state) {

		int i = 0;

		if (state.getValue(FORWARD)) {
			i |= this.getAABBIndex(EnumFacing.NORTH);
		}
		if (state.getValue(RIGHT)) {
			i |= this.getAABBIndex(EnumFacing.EAST);
		}
		if (state.getValue(BACK)) {
			i |= this.getAABBIndex(EnumFacing.SOUTH);
		}
		if (state.getValue(LEFT)) {
			i |= this.getAABBIndex(EnumFacing.WEST);
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
		return  (state.getBlock().canBeConnectedTo(world, pos.offset(face), face.getOpposite())
				|| this.canPaneConnectToBlock(state.getBlock())
				|| state.isSideSolid(world, pos.offset(face), face.getOpposite()));
	}

	public boolean canPaneConnectToBlock(Block block) {
		return block.getDefaultState().isFullCube() || block instanceof IronFence;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	// フェンスとかにつながないように
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.SOLID;
	}

	public int quantityDropped(Random rand) {
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
