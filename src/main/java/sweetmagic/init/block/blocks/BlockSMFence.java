package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
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

public class BlockSMFence extends BaseModelBlock {

	protected final int data;
	protected static final PropertyBool BACK = PropertyBool.create("back");
	protected static final PropertyBool FORWARD = PropertyBool.create("forward");
	protected static final PropertyBool LEFT = PropertyBool.create("left");
	protected static final PropertyBool RIGHT = PropertyBool.create("right");
	public static final AxisAlignedBB PILLAR_AABB = new AxisAlignedBB(0.375D, 0D, 0.375D, 0.625D, 1.5D, 0.625D);
    public static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.375D, 0D, 0.625D, 0.625D, 1.5D, 1D);
    public static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0D, 0D, 0.375D, 0.375D, 1.5D, 0.625D);
    public static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.375D, 0D, 0D, 0.625D, 1.5D, 0.375D);
    public static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.625D, 0D, 0.375D, 1D, 1.5D, 0.625D);


    protected static final AxisAlignedBB[] AABB = new AxisAlignedBB[] {
    	new AxisAlignedBB(0.375D, 0D, 0.375D, 0.625D, 1D, 0.625D),
		new AxisAlignedBB(0.375D, 0D, 0.375D, 0.625D, 1D, 1D),
		new AxisAlignedBB(0D, 0D, 0.375D, 0.625D, 1D, 0.625D),
		new AxisAlignedBB(0D, 0D, 0.375D, 0.625D, 1D, 1D),
		new AxisAlignedBB(0.375D, 0D, 0D, 0.625D, 1D, 0.625D),
		new AxisAlignedBB(0.375D, 0D, 0D, 0.625D, 1D, 1D),
		new AxisAlignedBB(0D, 0D, 0D, 0.625D, 1D, 0.625D),
		new AxisAlignedBB(0D, 0D, 0D, 0.625D, 1D, 1D),
		new AxisAlignedBB(0.375D, 0D, 0.375D, 1D, 1D, 0.625D),
		new AxisAlignedBB(0.375D, 0D, 0.375D, 1D, 1D, 1D),
		new AxisAlignedBB(0D, 0D, 0.375D, 1D, 1D, 0.625D),
		new AxisAlignedBB(0D, 0D, 0.375D, 1D, 1D, 1D),
		new AxisAlignedBB(0.375D, 0D, 0D, 1D, 1D, 0.625D),
		new AxisAlignedBB(0.375D, 0D, 0D, 1D, 1D, 1D),
		new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 0.625D),
		new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D)
	};

	public BlockSMFence(String name, int data) {
		super(Material.WOOD, name);
        setHardness(0.2F);
		setResistance(1024F);
		setSoundType(SoundType.WOOD);
		this.data = data;
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

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB[this.getAABBIndexState(this.getActualState(state, source, pos))];
	}

	public int getAABBIndex(EnumFacing face) {
		return 1 << face.getHorizontalIndex();
	}

	public int getAABBIndexState(IBlockState state) {

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
		return state.withProperty(FORWARD, this.canConnect(world, pos, EnumFacing.NORTH))
				.withProperty(BACK, this.canConnect(world, pos, EnumFacing.SOUTH))
				.withProperty(LEFT, this.canConnect(world, pos, EnumFacing.WEST))
				.withProperty(RIGHT, this.canConnect(world, pos, EnumFacing.EAST));
	}

	public boolean canConnect(IBlockAccess world, BlockPos pos, EnumFacing face) {
		IBlockState state = world.getBlockState(pos.offset(face));
		return !(state.getBlock() instanceof BlockLeaves) &&
				(state.getBlock().canBeConnectedTo(world, pos.offset(face), face.getOpposite())
				|| this.canConnectBlock(state.getBlock()) || this.canBeConnectedTo(world, pos, face)
				|| state.isSideSolid(world, pos.offset(face), face.getOpposite()));
	}

	public boolean canConnectBlock(Block block) {
		return block.getDefaultState().isFullCube() || block instanceof BlockFence
				|| block instanceof BlockSMFence;
	}

	// 両サイドにブロックがあるかどうか
	public int checkCenter (IBlockAccess world, BlockPos pos) {

		Block north = world.getBlockState(pos.north()).getBlock();
		Block south = world.getBlockState(pos.south()).getBlock();
		Block west = world.getBlockState(pos.west()).getBlock();
		Block east = world.getBlockState(pos.east()).getBlock();

		boolean vartical = this.canConnectBlock(north) && this.canConnectBlock(south) && !this.canConnectBlock(west) && !this.canConnectBlock(east);
		boolean beside = !this.canConnectBlock(north) && !this.canConnectBlock(south) && this.canConnectBlock(west) && this.canConnectBlock(east);

		int center = 0;

		if (vartical) {
			center = 1;
		}

		else if (beside) {
			center = 2;
		}

		return center;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	// フェンスとかにつながないように
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.SOLID;
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
		return true;
	}

	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (!world.isRemote) {
			return ItemLead.attachToFence(player, world, pos);
		}

		else {
			return stack.getItem() == Items.LEAD;
		}
	}

	@Override
	public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing face) {
        IBlockState state = world.getBlockState(pos.offset(face));
        BlockFaceShape shape = state.getBlockFaceShape(world, pos, face);
        Block block = state.getBlock();
        boolean flag = shape == BlockFaceShape.MIDDLE_POLE && (state.getMaterial() == this.blockMaterial || block instanceof BlockFenceGate);
        return !isExcepBlockForAttachWithPiston(block) && shape == BlockFaceShape.SOLID || flag;
	}

    protected static boolean isExcepBlockForAttachWithPiston(Block block) {
        return Block.isExceptBlockForAttachWithPiston(block) || block == Blocks.BARRIER || block == Blocks.MELON_BLOCK || block == Blocks.PUMPKIN || block == Blocks.LIT_PUMPKIN;
    }
}
