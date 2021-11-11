package sweetmagic.init.block.blocks;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.enumblock.EnumCook;
import sweetmagic.api.enumblock.EnumCook.FaceCookMeta;
import sweetmagic.api.enumblock.EnumCook.PropertyCook;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.cook.TileStove;

public class BlockStove  extends BaseFaceBlock {

	public static boolean keepInventory = false;
	public static final PropertyCook COOK = new PropertyCook("cook", EnumCook.getStoveList());

	public BlockStove(String name) {
		super(Material.IRON, name);
		setHardness(0.3F);
        setResistance(1024F);
		setSoundType(SoundType.STONE);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH).withProperty(COOK, EnumCook.OFF));
		disableStats();
		BlockInit.furniList.add(this);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileStove();//TileEntityは処理自体ほぼ同じなため製粉機を指定
	}

	//右クリックの処理
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {

		if (world.isRemote) { return true; }

		if (face == EnumFacing.UP && world.isAirBlock(pos.up())) {

			ItemStack stack = player.getHeldItem(hand);
			if (stack.getItem() instanceof ItemBlock && this.checkBlock(((ItemBlock) stack.getItem()).getBlock())) {
            	world.setBlockState(pos.up(), this.getBlock(stack).getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
                if (!player.isCreative()) { stack.shrink(1); }

                SoundType sound = this.getSoundType(state, world, pos, player);
                this.playerSound(world, pos, sound.getPlaceSound(),(sound.getVolume() + 1F) / 2F, sound.getPitch() * 0.8F);
            	return true;
            }
		}

		player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.STOVE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
    	return true;
    }

    public boolean checkBlock (Block block) {
    	return block instanceof BlockFryPan || block instanceof BlockPot;
    }

    public Block getBlock (ItemStack stack) {
    	return ((ItemBlock) stack.getItem()).getBlock();
    }

    public static void setState(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        keepInventory = true;
		world.setBlockState(pos, EnumCook.transitionStove(world.getBlockState(pos), COOK), 3);
        keepInventory = false;
        if (tile != null){
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			ItemStack stack = new ItemStack(BlockInit.stove_off);
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

	public EnumCook getCook (IBlockState state) {
		return state.getValue(COOK);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, COOK });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() + state.getValue(COOK).getMeta();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		FaceCookMeta fcMeta = EnumCook.getMeta(meta);
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(fcMeta.getMeta())).withProperty(COOK, fcMeta.getCook());
	}

	public static boolean isStoveOn (World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return state.getBlock() instanceof BlockStove && state.getValue(COOK).isON();
	}
}
