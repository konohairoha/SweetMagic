package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
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
import sweetmagic.init.base.BaseModelBlock;
import sweetmagic.init.tile.chest.TileGlassCup;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.SoundHelper;

public class BlockGlassCup extends BaseModelBlock {

	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.375D, 0.5D, 0.375D, 0.625D, 0D, 0.625D);

	public BlockGlassCup (String name) {
		super(Material.GLASS, name);
		BlockInit.blockList.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileGlassCup();
	}

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		TileGlassCup tile = (TileGlassCup) world.getTileEntity(pos);
		ItemStack wand = tile.getChestItem(0);

		// アイテムをセットしてないなら
		if (wand.isEmpty()) {

			ItemStack copy = stack.copy();
			Item item = copy.getItem();
			if (copy.isEmpty() || !( item instanceof ItemFood ) || ((ItemFood) item).getItemUseAction(stack) != EnumAction.DRINK) { return false; }

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

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

    	TileGlassCup tile = (TileGlassCup) world.getTileEntity(pos);
		ItemStack stack = new ItemStack(Item.getItemFromBlock(this));

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
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.glasscup.name")));
	}
}
