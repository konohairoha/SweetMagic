package sweetmagic.init.block.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.enumblock.EnumCook;
import sweetmagic.api.enumblock.EnumCook.FaceCookMeta;
import sweetmagic.api.enumblock.EnumCook.PropertyCook;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.base.BaseCookBlock;
import sweetmagic.init.tile.cook.TileStove;

public class BlockStove extends BaseCookBlock {

	private static boolean keepInventory = false;
	public static final PropertyCook COOK = new PropertyCook("cook", EnumCook.getStoveList());
	private final int data;

	public BlockStove(String name, int data) {
		super(name);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(COOK, EnumCook.OFF));
		this.data = data;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileStove();
	}

	//右クリックの処理
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float x, float y, float z) {

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
			ItemStack stack = new ItemStack(this);
			TileStove tile = (TileStove) world.getTileEntity(pos);
			NBTTagCompound tags = new NBTTagCompound();
			NBTTagCompound tags1 = new NBTTagCompound();
			tags.setTag("BlockEntityTag", ((TileStove) tile).writeToNBT(tags1));
			stack.setTagCompound(tags);
			spawnAsEntity(world, pos, stack);
		}
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

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (this.data == 0) { return; }
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.smstove.name")));
	}
}
