package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;

public class SMLeaves extends BlockLeaves {

	private final int data;

	public SMLeaves(String name, int data) {
		setRegistryName(name);
		setUnlocalizedName(name);
		setHardness(0F);
		setResistance(1024F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
		this.data = data;
		//ブロックの光を透過する強さ　数値が高いほどブロックは不透明、光を通さないようになる。
		this.setLightOpacity(0);
		setLightLevel(this.data == 2 ? 0.5F : 0F);
		BlockInit.blockList.add(this);
	}

	/**
	 * 0 = 栗
	 * 1 = ココナッツ
	 * 2 = プリズミウム
	 * 3 = バナナ
	 * 4 = マギアウッド
	 */

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(DECAYABLE, (meta & 4) > 0).withProperty(CHECK_DECAY, (meta & 8) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		if (state.getValue(DECAYABLE)) { i |= 4; }
		if (state.getValue(CHECK_DECAY)) { i |= 8; }
		return i;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		list.add(new ItemStack(this));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this.getCrop());
	}

	//ドロップする作物
	protected Block getCrop() {
		switch (this.data) {
		case 0: return BlockInit.chestnut_sapling;
		case 1: return BlockInit.coconut_sapling;
		case 2: return BlockInit.prism_sapling;
		case 3: return BlockInit.banana_sapling;
		case 4: return BlockInit.magiawood_sapling;
		}
		return null;
	}

	@Override
	protected void dropApple(World world, BlockPos pos, IBlockState state, int chance) { }

	@Override
	public ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(this);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this);
	}

	@Override
	public int getSaplingDropChance(IBlockState state) {
		switch (this.data) {
		case 0: return 30;
		case 1: return 45;
		case 2: return 25;
		case 3: return 45;
		}
		return 20;
	}

	@Override
	public BlockPlanks.EnumType getWoodType(int meta) {
		return null;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return NonNullList.withSize(1, new ItemStack(this));
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 60;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 30;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return this.data != 2;
	}

	//テクスチャが不透明か　falseだと透明を使用可能
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return this.data == 2 ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.CUTOUT_MIPPED;
	}

	//テクスチャが透明で、重ねて表示したい場合
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {

		AxisAlignedBB aabb = state.getBoundingBox(world, pos);

		switch (side) {
		case DOWN:
			if (aabb.minY > 0D) { return true; }
			break;
		case UP:
			if (aabb.maxY < 1D) { return true; }
			break;
		case NORTH:
			if (aabb.minZ > 0D) { return true; }
			break;
		case SOUTH:
			if (aabb.maxZ < 1D) { return true; }
			break;
		case WEST:
			if (aabb.minX > 0D) { return true; }
			break;
		case EAST:
			if (aabb.maxX < 1D) { return true; }
			break;
		}
		return !world.getBlockState(pos.offset(side)).doesSideBlockRendering(world, pos.offset(side), side.getOpposite());
	}
}
