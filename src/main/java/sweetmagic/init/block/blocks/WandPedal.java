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
import sweetmagic.util.FaceAABB;
import sweetmagic.util.SoundHelper;

public class WandPedal extends BaseFaceBlock {

	public final int data;
	private final static AxisAlignedBB PEDAL = new AxisAlignedBB(0.1D, 0D, 0.1D, 0.9D, 0.6D, 0.9D);
	private final static AxisAlignedBB[] WALL = new FaceAABB(0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D, 1D).getRotatedBounds();
	private final static AxisAlignedBB[] BOAD = new FaceAABB(0.435D, 0D, 0D, 0.56D, 1D, 1D).getRotatedBounds();
	private final static AxisAlignedBB POT = new AxisAlignedBB(0.375D, 0D, 0.375D, 0.625D, 0.5D, 0.625D);
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.1D, 0D, 0.1D, 0.9D, 0.6D, 0.9D);

	public WandPedal(String name, int data) {
		super(Material.ROCK, name);
		setHardness(0.3F);
		setResistance(99999F);
		setSoundType(SoundType.STONE);
		this.data = data;
		BlockInit.furniList.add(this);
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

		// 杖の台座
		case 0: return PEDAL;

		// ウォールボード
		case 1:
			return WALL[face.rotateYCCW().getHorizontalIndex()];
		// 看板
		case 2:
			return BOAD[face.rotateYCCW().getHorizontalIndex()];
		// 花瓶
		case 3:
			return POT;
		}

		return AABB;
	}

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		TileWandPedal tile = (TileWandPedal) world.getTileEntity(pos);
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

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.wandpedal.name")));
	}
}
