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
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.AdvancedInit;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
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
		BlockInit.furniList.add(this);
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

	// 右クリックの処理
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {

		if (this.data == 2 && face == EnumFacing.UP && player.getHeldItem(hand).getItem() == Items.BUCKET) {
			if (!world.isRemote) {
				this.spawnItem(world, player, new ItemStack(ItemInit.watercup, 9));
			}
            player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
			return true;
		}

		return super.onBlockActivated(world, pos, state, player, hand, face, hitX, hitY, hitZ);
	}

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		int guiId = 0;
		SoundEvent sound = null;

		switch (this.data) {
		case 0:
			sound = SoundEvents.BLOCK_PISTON_CONTRACT;
			guiId = SMGuiHandler.WOODCHEST;
			break;
		case 1:
			sound = SoundEvents.BLOCK_IRON_DOOR_OPEN;
			guiId = SMGuiHandler.WOODCHEST;
			break;
		case 2:
			sound = SoundEvents.BLOCK_PISTON_CONTRACT;
			guiId = SMGuiHandler.KICHEN_CHEST_GUI;
			break;
		}

		this.playerSound(world, pos, sound, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		player.openGui(SweetMagicCore.INSTANCE, guiId, world, pos.getX(), pos.getY(), pos.getZ());

		// 神殿のトレジャーチェストなら開く
		if (this.data == 1 && world.getBlockState(pos.down()).getBlock() == BlockInit.gorgeous_lamp) {
			AdvancedInit.bonuschest_open.triggerFor(player);
		}

		return true;
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

    	TileWoodChest tile = (TileWoodChest) world.getTileEntity(pos);
		ItemStack stack = new ItemStack(this);

		if (tile.isSlotEmpty()) {
			spawnAsEntity(world, pos, stack);
			return;
		}

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

    // 向き変更対応
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing face) {

    	TileWoodChest tile = (TileWoodChest) world.getTileEntity(pos);
		boolean flag = super.rotateBlock(world, pos, face);

		if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }

		return flag;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.parallelinterfere_title.name")));
	}
}
