package sweetmagic.init.block.blocks;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
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
import sweetmagic.SweetMagicCore;
import sweetmagic.api.enumblock.EnumCook;
import sweetmagic.api.enumblock.EnumCook.FaceCookMeta;
import sweetmagic.api.enumblock.EnumCook.PropertyCook;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseCookBlock;
import sweetmagic.init.tile.cook.TileJuiceMaker;
import sweetmagic.util.FaceAABB;

public class BlockJuiceMaker extends BaseCookBlock {

	private static boolean keepInventory = false;
	private final int data;
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.2D, 0.7D, 0.2D, 0.8D, 0D, 0.8D);
	private final static AxisAlignedBB[] COFFE = new FaceAABB(0.1625D, 0D, 0.25D, 0.9375D, 0.90625D, 0.75D).getRotatedBounds();
	public static final PropertyCook COOK = new PropertyCook("cook", EnumCook.getStoveList());

	public BlockJuiceMaker(String name, int data, boolean isRegister) {
		super(name);
		this.data = data;
		setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH).withProperty(COOK, EnumCook.OFF));

		if (isRegister) {
			BlockInit.juiceList.add(this);
		}
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.data == 0 ? AABB : COFFE[state.getValue(FACING).getHorizontalIndex()];
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileJuiceMaker();
	}

	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return true; }
		player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.MAKER_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
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

		if (keepInventory) { return; }

		ItemStack stack = new ItemStack(this);
		TileJuiceMaker tile = (TileJuiceMaker) world.getTileEntity(pos);
		NBTTagCompound tileTags = tile.writeToNBT(new NBTTagCompound());
		NBTTagCompound tags = new NBTTagCompound();
		tags.setTag("BlockEntityTag", tileTags);
		stack.setTagCompound(tags);
		spawnAsEntity(world, pos, stack);
		world.updateComparatorOutputLevel(pos, state.getBlock());
        super.breakBlock(world, pos, state);
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

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		if (this.data == 0) { return; }

		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.sm_coffee.name")));
		super.addInformation(stack, world, tooltip, advanced);
	}
}
