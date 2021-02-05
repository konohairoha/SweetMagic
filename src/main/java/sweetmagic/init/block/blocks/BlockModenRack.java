package sweetmagic.init.block.blocks;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.init.tile.chest.TileModenWallRack;
import sweetmagic.init.tile.cook.TilePlate;

public class BlockModenRack extends BaseFaceBlock {

	public final int data;

	public BlockModenRack(String name, int data) {
		super(Material.WOOD, name);
		setHardness(1.0F);
		setResistance(16F);
		setSoundType(SoundType.WOOD);
		this.data = data;
		BlockInit.blockList.add(this);
	}

	/**
	 * 0 = モダンラック
	 * 1 = モダンウォールラック
	 * 2 = 皿
	 */

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		switch (this.data) {
		case 0:
			return new TileModenRack();
		case 1:
			return new TileModenWallRack();
		case 2:
			return new TilePlate();
		}

		return null;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (this.data) {
		case 0:
			return FULL_BLOCK_AABB;
		case 1:

			EnumFacing face = state.getValue(FACING);
			if (face == EnumFacing.NORTH) {
				return new AxisAlignedBB(0, 0.9375, 0.38, 1, 1, 1);
			} else if (face == EnumFacing.SOUTH) {
				return new AxisAlignedBB(0, 0.9375, 0, 1, 1, 0.62);
			} else if (face == EnumFacing.EAST) {
				return new AxisAlignedBB(0, 0.9375, 0, 0.62, 1, 1);
			} else if (face == EnumFacing.WEST) {
				return new AxisAlignedBB(0.38, 0.9375, 0, 1, 1, 1);
			}
		case 2:
			return new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.1, 0.875);
		}

		return FULL_BLOCK_AABB;
	}

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return true; }
		player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.MODENRACK_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
    	TileModenRack tile = (TileModenRack) world.getTileEntity(pos);
		ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
		NBTTagCompound tags = new NBTTagCompound();
		tags.setTag("BlockEntityTag", tile.writeToNBT(new NBTTagCompound()));
		stack.setTagCompound(tags);
		spawnAsEntity(world, pos, stack);
		world.updateComparatorOutputLevel(pos, state.getBlock());
        super.breakBlock(world, pos, state);
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
}
