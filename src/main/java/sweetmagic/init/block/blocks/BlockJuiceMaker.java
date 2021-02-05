package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.cook.TileJuiceMaker;

public class BlockJuiceMaker extends BaseFaceBlock {

	public static boolean keepInventory = false;

	public BlockJuiceMaker(String name, List<Block> list) {
		super(Material.IRON, name);
		setHardness(1.0F);
		setResistance(16F);
		setSoundType(SoundType.STONE);
		this.disableStats();
		list.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.2D, 0.7D, 0.2D, 0.8D, 0D, 0.8D);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileJuiceMaker();
	}

	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return true; }
		player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.MAKER_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

    public static void setState(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);
        Block block = state.getBlock();
        keepInventory = true;
		if (block == BlockInit.juicemaker_off) {
			world.setBlockState(pos, BlockInit.juicemaker_on.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
		} else if (block == BlockInit.juicemaker_on) {
			world.setBlockState(pos, BlockInit.juicemaker_off.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
		}
        keepInventory = false;
        if (tile != null){
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

		if (keepInventory) { return; }

		ItemStack stack = new ItemStack(Item.getItemFromBlock(BlockInit.juicemaker_off));
		TileJuiceMaker tile = (TileJuiceMaker) world.getTileEntity(pos);
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
