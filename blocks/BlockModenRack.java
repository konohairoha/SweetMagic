package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.chest.TileFruitCrate;
import sweetmagic.init.tile.chest.TileIronShelf;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.init.tile.chest.TileModenWallRack;
import sweetmagic.init.tile.chest.TilePlateShaped;
import sweetmagic.init.tile.chest.TileShocase;
import sweetmagic.init.tile.chest.TileWoodenBox;
import sweetmagic.init.tile.cook.TilePlate;
import sweetmagic.init.tile.cook.TileTray;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.FaceAABB;
import sweetmagic.util.SoundHelper;

public class BlockModenRack extends BaseFaceBlock {

	public final int data;
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.125D, 0D, 0.125D, 0.875D, 0.1D, 0.875D);
	private final static AxisAlignedBB HOLDER = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.125D, 1D);
	private final static AxisAlignedBB[] BOTTLE = new FaceAABB(0D, 0D, 0.625D, 1D, 1D, 1D).getRotatedBounds();
	private final static AxisAlignedBB[] WALL = new FaceAABB(0.0625D, 0.0625D, 0.7D, 0.9375D, 0.9375D, 1D).getRotatedBounds();
	private final static AxisAlignedBB[] SHAPED = new FaceAABB(0.125D, 0.4375D, 0.25D, 0.875D, 0.0D, 0.75D).getRotatedBounds();
	private final static AxisAlignedBB[] PLATE = new FaceAABB(0.125D, 0D, 0.1875D, 0.875D, 0.125D, 0.8125D).getRotatedBounds();
	private final static AxisAlignedBB[] IRON = new FaceAABB(0D, 0D, 0.5D, 1D, 1D, 1D).getRotatedBounds();
	private final static AxisAlignedBB[] CRATE = new FaceAABB(0.0625D, 0D, -0.2D, 0.9375D, 0.8D, 0.9D).getRotatedBounds();
	private final static AxisAlignedBB[] CRATEBOX = new FaceAABB(0.125D, 0D, 0.0625D, 0.875D, 0.40625D, 0.9375D).getRotatedBounds();

	public BlockModenRack(String name, int data) {
		super(Material.WOOD, name);
		setHardness(0.5F);
		setResistance(1024F);
		setSoundType((data == 9 || data == 12) ? SoundType.METAL : SoundType.WOOD);
		this.data = data;
		BlockInit.furniList.add(this);
	}

	/**
	 * 0 = モダンラック
	 * 1 = モダンウォールラック
	 * 2 = 皿
	 * 3 = 木皿
	 * 4 = パン置き
	 * 5 = パン置き
	 * 6 = ショーケース
	 * 7 = ボトルラック
	 * 8 = 天井だな
	 * 9 = レンジフードラック
	 * 10 = 皿立て
	 * 11 = トレー
	 * 12 = アイロンシェルフ
	 * 13 = 果物箱
	 * 14 = 木箱
	 * 15 = 果物箱(横)
	 */

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		switch (this.data) {
		case 0:	return new TileModenRack();
		case 1:	return new TileModenWallRack();
		case 2:	return new TilePlate();
		case 3:	return new TilePlate();
		case 4:	return new TilePlate();
		case 5:	return new TilePlate();
		case 6:	return new TileShocase();
		case 7:	return new TileShocase();
		case 8:	return new TileShocase();
		case 9:	return new TileShocase();
		case 10:return new TilePlateShaped();
		case 11:return new TileTray();
		case 12:return new TileIronShelf();
		case 13:return new TileFruitCrate();
		case 14:return new TileWoodenBox();
		case 15:return new TileFruitCrate();
		default : return null;
		}
	}

	// ブロック設置したときの処理(上のブロックを設置)
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileModenRack tile = (TileModenRack) world.getTileEntity(pos);
		tile.checkRangePlayer();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (this.data) {
		case 2:
		case 3:
			return AABB;
		case 4:
		case 5:
			return HOLDER;
		case 7:
			return BOTTLE[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		case 9:
			return WALL[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		case 10:
			return SHAPED[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		case 11:
			return PLATE[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		case 12:
			return IRON[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		case 13:
			return CRATE[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		case 15:
			return CRATEBOX[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		default :
			return FULL_BLOCK_AABB;
		}
	}

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (player == null) { return true; }

		if (this.data >= 2 && this.data <= 5) {

			TilePlate tile = (TilePlate) world.getTileEntity(pos);
			ItemStack food = tile.getChestItem(0);

			if (food.isEmpty() && stack.getItem() instanceof ItemFood) {

				tile.tickTime = 0;
				if (world.isRemote) { return true; }

				ItemStack copy = stack.copy();
				stack.shrink(1);
				copy.setCount(1);

				ItemHandlerHelper.insertItemStacked(tile.chestInv, copy, false);
				tile.markDirty();
				world.notifyBlockUpdate(pos, state, state, 3);

				// クライアント（プレイヤー）へ送りつける
				if (player instanceof EntityPlayerMP) {
					PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_SHRINK, 1F, 0.33F), (EntityPlayerMP) player);
				}

				return true;
			}

			else if (!food.isEmpty() && stack.isEmpty()) {

				tile.tickTime = 0;
				if (world.isRemote) { return true; }

				this.spawnItem(world, player, food.copy());
				food.shrink(food.getCount());
				tile.markDirty();
				world.notifyBlockUpdate(pos, state, state, 3);
				return true;
			}
		}

		if (!world.isRemote) {
			player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.MODENRACK_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

    	TileModenRack tile = (TileModenRack) world.getTileEntity(pos);
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
		if (this.data == 8 || this.data == 9) {
			tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.range_hood_rack.name")));
		}
	}
}
