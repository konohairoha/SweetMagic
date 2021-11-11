package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;

public class BlockWallTowel extends BaseFaceBlock {

	private final static AxisAlignedBB WALL_NORTH = new AxisAlignedBB(0D, 0.625D, 1D, 1D, 0.875D, 1D);
	private final static AxisAlignedBB WALL_SOUTH = new AxisAlignedBB(0D, 0.625D, 0D, 1D, 0.875D, 0D);
	private final static AxisAlignedBB WALL_EAST = new AxisAlignedBB(0D, 0.625D, 0D, 0D, 0.875D, 1D);
	private final static AxisAlignedBB WALL_WEST = new AxisAlignedBB(1D, 0.625D, 0D, 1D, 0.875D, 1D);
	private final int data;

	public BlockWallTowel(String name, int data, List<Block> list) {
		super(Material.WOOD, name);
		setHardness(0F);
		setResistance(1024F);
		setSoundType(SoundType.CLOTH);
		this.setEmptyAABB();
		this.data = data;
		list.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (state.getValue(FACING)) {
		case NORTH: return WALL_NORTH;
		case SOUTH: return WALL_SOUTH;
		case EAST:  return WALL_EAST;
		case WEST:  return WALL_WEST;
		}

		return WALL_NORTH;
	}

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		Block block = this.data == 0 ? BlockInit.wall_none : BlockInit.wall_towel;
		world.setBlockState(pos, block.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);

		SoundEvent sound = this.data == 0 ? SoundEvents.BLOCK_CLOTH_BREAK : SoundEvents.BLOCK_CLOTH_PLACE;
		this.playerSound(world, pos, sound, 1F, 1F);
		return true;
	}

	// アイテムをドロップ
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return new ItemStack(BlockInit.wall_towel).getItem();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(BlockInit.wall_towel);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.walltowel.name")));
	}
}
