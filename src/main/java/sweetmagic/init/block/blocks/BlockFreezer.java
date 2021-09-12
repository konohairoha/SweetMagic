package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.cook.TileFreezer;

public class BlockFreezer extends BaseFaceBlock {

	public final boolean isTop;
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 1D);

	public BlockFreezer(String name, boolean isTop, List<Block> list) {
		super(Material.IRON, name);
        setHardness(0.1F);
        setResistance(99999F);
		this.setSoundType(SoundType.METAL);
		this.isTop = isTop;
		list.add(this);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileFreezer();
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return super.canPlaceBlockAt(world, pos) && super.canPlaceBlockAt(world, pos.up());
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	// ブロックをこわしたとき(下のブロックを指定)
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.breakBlock(!this.isTop ? pos.up() : pos.down(), world, true);
		pos = !this.isTop ? pos.up() : pos.down();
        world.removeTileEntity(pos);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) { }

	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return true; }
		player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.FREEZER_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

    	// 上のブロックなら
		if (this.isTop) {

			TileFreezer tile2 = (TileFreezer) world.getTileEntity(pos);

			if (tile2 == null) { return; }

			// スロットに入れているアイテムをスポーン
			for (ItemStack s : tile2.allSlotItem()) {
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), s));
			}
			return;
		}

		ItemStack stack = new ItemStack(this);
		TileFreezer tile = (TileFreezer) world.getTileEntity(pos);
		NBTTagCompound tileTags = tile.writeToNBT(new NBTTagCompound());
		NBTTagCompound tags = new NBTTagCompound();
		tags.setTag("BlockEntityTag", tileTags);

		// 製粉機（オフ状態）か製粉機（稼働状態）のときtileの入力リストを取り出す
		for (ItemStack s : tile.inPutList) {
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), s));
		}

		if (tags != null) {
			tags.removeTag("inPutList");
			tags.removeTag("outPutList");
			tags.removeTag("isCook");
			tags.removeTag("cookTime");
		}

		stack.setTagCompound(tags);

		world.updateComparatorOutputLevel(pos, state.getBlock());
		spawnAsEntity(world, pos, stack);

        super.breakBlock(world, !this.isTop ? pos.up() : pos, state);
        super.breakBlock(world, this.isTop ? pos.down() : pos, state);

	}

	// ブロック設置したときの処理(上のブロックを設置)
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos.up(), BlockInit.freezer_top.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	// アイテムをドロップ
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ItemStack.EMPTY.getItem();
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(BlockInit.freezer_bottom);
	}
}
