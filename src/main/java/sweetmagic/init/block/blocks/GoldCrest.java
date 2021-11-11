package sweetmagic.init.block.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class GoldCrest extends BaseModelBlock {

	private static final PropertyBool TOP = PropertyBool.create("top");
	private static final PropertyBool BOT = PropertyBool.create("bot");
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.2D, 1D, 0.2D, 0.8D, 0D, 0.8D);

	public GoldCrest(String name) {
		super(Material.PLANTS, name);
		setSoundType(SoundType.PLANT);
		setHardness(0F);
        setResistance(1024F);
		this.setEmptyAABB();
		setDefaultState(this.blockState.getBaseState().withProperty(TOP, false).withProperty(BOT, false));
		BlockInit.furniList.add(this);
	}

	//土、草、耕地に置いても壊れないようにする
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		Block block = world.getBlockState(pos.down()).getBlock();
		return block != Blocks.AIR;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		world.scheduleUpdate(pos, this, this.tickRate(world));
	}

	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		world.scheduleUpdate(pos, this, this.tickRate(world));
	}

	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		if (world.isRemote) { return; }

		Block block = world.getBlockState(pos.down()).getBlock();
		if (block != Blocks.AIR) { return; }

		this.breakBlock(pos, world, true);
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if(world.getBlockState(pos.up()).getBlock() == this) {
			this.breakBlock(pos.up(), world, true);
		}
    }

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean bot = world.getBlockState(pos.down()).getBlock() == this ;
		boolean top = world.getBlockState(pos.up()).getBlock() == this;
		return state.withProperty(TOP, bot).withProperty(BOT, top);
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
		return new BlockStateContainer(this, new IProperty[] { TOP, BOT });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(this);
    }
}
