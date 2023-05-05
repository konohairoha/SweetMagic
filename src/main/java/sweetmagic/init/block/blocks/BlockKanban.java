package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.util.FaceAABB;

public class BlockKanban extends BaseFaceBlock {

	private final static AxisAlignedBB[] KANBAN = new FaceAABB(0D, 0D, 0.9375D, 1D, 1D, 1D).getRotatedBounds();

	public BlockKanban(String name, List<Block> list) {
		super(Material.WOOD, name);
        setHardness(0.4F);
        setResistance(1024);
		this.setSoundType(SoundType.WOOD);
		this.setEmptyAABB();
		list.add(this);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return super.canPlaceBlockAt(world, pos) && super.canPlaceBlockAt(world, pos.up());
	}

	// ブロック設置したときの処理(上のブロックを設置)
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos.up(), BlockInit.kanban_top.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
	}

	// ブロックをこわしたとき(下のブロックを指定)
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.breakBlock(this == BlockInit.kanban_bot ? pos.up() : pos.down(), world, true);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return KANBAN[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
	}

	// アイテムをドロップ
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this == BlockInit.kanban_bot ? new ItemStack(this).getItem() : ItemStack.EMPTY.getItem();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(BlockInit.kanban_bot);
	}
}
