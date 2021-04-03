package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.block.crop.BlockAlstroemeria;
import sweetmagic.init.block.crop.BlockCornFlower;
import sweetmagic.init.item.sm.seed.SMSeed;
import sweetmagic.init.tile.chest.TileWandPedal;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.SoundHelper;

public class WandPedal extends BaseFaceBlock {

	public final int data;

	public WandPedal(String name, int data) {
		super(Material.ROCK, name);
		setHardness(0.3F);
		setResistance(99999F);
		setSoundType(SoundType.STONE);
		this.data = data;
		BlockInit.blockList.add(this);
	}

	/**
	 * 0 = 杖の台座
	 * 1 = ウォールボード
	 * 2 = 看板
	 * 3 = 花瓶
	 */

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileWandPedal();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		EnumFacing face = state.getValue(FACING);

		switch (this.data) {
		case 0:
			// 杖の台座
			return new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.6, 0.9);
		case 1:
			// ウォールボード
			if (face == EnumFacing.NORTH) {
				return new AxisAlignedBB(0.0625, 0.0625, 0.9375, 0.9375, 0.9375, 1);
			} else if (face == EnumFacing.SOUTH) {
				return new AxisAlignedBB(0.0625, 0.0625, 0, 0.9375, 0.9375, 0.0625);
			} else if (face == EnumFacing.EAST) {
				return new AxisAlignedBB(0, 0.085, 0.0625, 0.0625, 0.9375, 0.9375);
			} else if (face == EnumFacing.WEST) {
				return new AxisAlignedBB(0.9375, 0.0625, 0.0625, 1, 0.9375, 0.9375);
			}
		case 2:
			// 看板
			switch (face) {
			case NORTH:
			case SOUTH:
				return new AxisAlignedBB(0.435, 0, 0, 0.56, 1, 1);
			case EAST:
			case WEST:
				return new AxisAlignedBB(0, 0, 0.435, 1, 1, 0.56);
			}
		case 3:
			// 花瓶
			return new AxisAlignedBB(0.375, 0, 0.375, 0.625, 0.5, 0.625);

		}

		return new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.6, 0.9);
	}

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		TileWandPedal tile = (TileWandPedal)world.getTileEntity(pos);
		ItemStack wand = tile.getChestItem();

		// アイテムをセットしてないなら
		if (wand.isEmpty()) {

			ItemStack copy = stack.copy();
			if (copy.isEmpty() || this.canNotSetItem(stack)) { return false; }

			stack.shrink(1);
			copy.setCount(1);
			ItemHandlerHelper.insertItemStacked(tile.chestInv, copy, false);

			// クライアント（プレイヤー）へ送りつける
			if (player instanceof EntityPlayerMP) {
				PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_SHRINK, 1F, 0.33F), (EntityPlayerMP) player);
			}
		}

		// アイテムをセットしてるなら
		else {
			this.spawnItem(world, player, wand.copy());
			wand.shrink(1);
		}

		tile.markDirty();
		world.notifyBlockUpdate(pos, state, state, 3);

		return true;
	}

	// 花か種以外なら設置不可
	public boolean canNotSetItem (ItemStack stack) {
		Item item = stack.getItem();
		return this.data == 3 && !(item instanceof SMSeed) && !this.isFlower(item);
	}

	// 花かどうか
	public boolean isFlower (Item item) {

		if (!(item instanceof ItemBlock)) { return false; }

		Block block = ((ItemBlock) item).getBlock();
		return block instanceof BlockCornFlower || block instanceof BlockFlower || block instanceof BlockSapling
				|| block instanceof SMSapling || block instanceof BlockAlstroemeria || block instanceof GoldCrest;
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
    	TileWandPedal tile = (TileWandPedal) world.getTileEntity(pos);
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


	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		String text = new TextComponentTranslation("tip.wandpedal.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GOLD + text));
	}
}
