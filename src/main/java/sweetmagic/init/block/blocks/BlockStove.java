package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.cook.TileStove;
import sweetmagic.util.PlayerHelper;

public class BlockStove  extends BaseFaceBlock {

	public static boolean keepInventory = false;

	public BlockStove(String name, float light, List<Block> list) {
		super(Material.IRON, name);
		setHardness(1.0F);
        setResistance(1024F);
		setSoundType(SoundType.STONE);
		this.setLightLevel(light);
		disableStats();
		list.add(this);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileStove();//TileEntityは処理自体ほぼ同じなため製粉機を指定
	}

	//Tick更新処理が必要なブロックには必ず入れること
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(world, pos, state, rand);
	}

	//右クリックの処理
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {

		if (world.isRemote) { return true; }

		if (face == EnumFacing.UP && world.isAirBlock(pos.up())) {

	        ItemStack stack = player.getHeldItem(hand);

            if (this.checkBlock(stack)) {
            	Block block = stack.getItem() == Item.getItemFromBlock(BlockInit.pot_off) ? BlockInit.pot_off : BlockInit.frypan_off;
            	world.setBlockState(pos.up(), block.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
                if (!PlayerHelper.isCleative(player)) { stack.setCount(stack.getCount() - 1); }

                SoundType sound = this.getSoundType(state, world, pos, player);
                this.playerSound(world, pos, sound.getPlaceSound(),(sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
            	return true;
            }
		}

		player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.STOVE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
    	return true;
    }

    public boolean checkBlock (ItemStack stack) {
    	return stack.getItem() == Item.getItemFromBlock(BlockInit.pot_off) || stack.getItem() == Item.getItemFromBlock(BlockInit.frypan_off);
    }

    public static void setState(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);
        Block block = state.getBlock();
        keepInventory = true;
		if (block == BlockInit.stove_on) {
			world.setBlockState(pos, BlockInit.stove_off.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
		} else if (block == BlockInit.stove_off) {
			world.playSound(null, pos, SMSoundEvent.STOVE_ON, SoundCategory.AMBIENT, 0.33F, 1F);
			world.setBlockState(pos, BlockInit.stove_on.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
		}
        keepInventory = false;
        if (tile != null){
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			ItemStack stack = new ItemStack(Item.getItemFromBlock(BlockInit.stove_off));
			TileStove tile = (TileStove) world.getTileEntity(pos);
			NBTTagCompound tags = new NBTTagCompound();
			NBTTagCompound tags1 = new NBTTagCompound();
			tags.setTag("BlockEntityTag", ((TileStove) tile).writeToNBT(tags1));
			stack.setTagCompound(tags);
			spawnAsEntity(world, pos, stack);
		}
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
}
