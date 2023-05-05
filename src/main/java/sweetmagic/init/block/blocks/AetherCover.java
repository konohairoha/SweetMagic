package sweetmagic.init.block.blocks;

import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.magic.TileAetherCover;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.SoundHelper;

public class AetherCover extends BaseFaceBlock {

	private final int data;

	public AetherCover(String name, int data) {
		super(Material.WOOD, name);
		setHardness(0F);
		setResistance(1024F);
		setSoundType(SoundType.WOOD);
		this.data = data;
		BlockInit.noTabList.add(this);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileAetherCover();
	}

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (player.isPotionActive(PotionInit.breakblock)) { return false; }

		if (world.isRemote) { return true; }

		TileAetherCover tile = (TileAetherCover) world.getTileEntity(pos);
		ItemStack drop = tile.getChestItem();

		// アイテムをセットしてないなら
		if (drop.isEmpty()) {

			ItemStack copy = stack.copy();
			Item item = copy.getItem();
			if (copy.isEmpty() || !(item instanceof ItemBlock) || item == Item.getItemFromBlock(BlockInit.aether_cover) || item == Item.getItemFromBlock(BlockInit.aether_cover_rock)) { return false; }

			copy.setCount(1);
			ItemHandlerHelper.insertItemStacked(tile.chestInv, copy, false);

			// クライアント（プレイヤー）へ送りつける
			if (player instanceof EntityPlayerMP) {
				PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_SHRINK, 1F, 0.33F), (EntityPlayerMP) player);
			}
		}

		// アイテムをセットしてるなら
		else {
			drop.shrink(1);
		}

		tile.markDirty();
		world.notifyBlockUpdate(pos, state, state, 3);

		return true;
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

    	TileAetherCover tile = (TileAetherCover) world.getTileEntity(pos);
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

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {

		TileEntity tile = world.getTileEntity(pos);
		if (tile == null || !(tile instanceof TileAetherCover)) { return new ItemStack(this); }

		TileAetherCover cover = (TileAetherCover) tile;
		return cover.isSlotEmpty() ? new ItemStack(this) : cover.getChestItem();
	}

	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return false;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> aabbList, Entity entity, boolean flag) {
		if (this.data != 0) {
			super.addCollisionBoxToList(state, world, pos, aabb, aabbList, entity, flag);
		}
	}
}
