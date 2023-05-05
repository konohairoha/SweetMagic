package sweetmagic.init.block.blocks;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;
import sweetmagic.init.tile.magic.TileCrystalCore;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.SoundHelper;

public class CrystalCore extends BaseModelBlock {

	private final int data;
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.1D, 0.4D, 0.1D, 0.9D, 1.3D, 0.9D);

	public CrystalCore (String name, int data) {
        super(Material.ROCK, name);
        setHardness(99999);
        setResistance(9999F);
        setHarvestLevel("pickaxe", 3);
        setSoundType(SoundType.GLASS);
        setLightLevel(0.25F);
        this.data = data;
		BlockInit.blockList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	// 右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if(!player.isCreative()) { return false; }

		if (this.data == 0) {

			if (world.isRemote) { return true; }

			TileCrystalCore tile = (TileCrystalCore) world.getTileEntity(pos);
			ItemStack wand = tile.getChestItem();

			// アイテムをセットしてないなら
			if (wand.isEmpty()) {

				if (stack.isEmpty()) { return true; }

				ItemStack copy = stack.copy();
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

		return false;
	}

    @Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileCrystalCore();
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    public int getData() {
    	return this.data;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}
