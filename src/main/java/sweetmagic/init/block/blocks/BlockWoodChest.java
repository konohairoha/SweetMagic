package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.chest.TileWoodChest;

public class BlockWoodChest extends BaseFaceBlock {

	public final int data;

	public BlockWoodChest(String name, int data) {
		super(Material.WOOD, name);
		setHardness(0.5F);
		setResistance(1024);
		setSoundType(SoundType.WOOD);
		this.data = data;
		BlockInit.blockList.add(this);
	}

	/**
	 * 0 = 	ウッドチェスト
	 * 1 = トレジャーチェスト
	 * 2 = キッチンテーブル
	 */

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileWoodChest();
	}

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		int guiId = 0;

		switch (this.data) {
		case 0:
			this.playerSound(world, pos, SoundEvents.BLOCK_PISTON_CONTRACT, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
			guiId = SMGuiHandler.WOODCHEST;
			break;
		case 1:
			this.playerSound(world, pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
			guiId = SMGuiHandler.WOODCHEST;
			break;
		case 2:
			this.playerSound(world, pos, SoundEvents.BLOCK_PISTON_CONTRACT, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
			guiId = SMGuiHandler.KICHEN_CHEST_GUI;
			break;
		}

		player.openGui(SweetMagicCore.INSTANCE, guiId, world, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
    	TileWoodChest tile = (TileWoodChest) world.getTileEntity(pos);
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

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + new TextComponentTranslation("tip.parallelinterfere_title.name", new Object[0]).getFormattedText()));
	}
}
