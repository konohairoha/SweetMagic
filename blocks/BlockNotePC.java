package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.chest.TileDisplay;
import sweetmagic.init.tile.chest.TileNotePC;
import sweetmagic.util.FaceAABB;
import sweetmagic.util.SMChunkLoader;
import sweetmagic.util.SMChunkLoader.IChunkBlock;

public class BlockNotePC  extends BaseFaceBlock implements IChunkBlock {

	private final static AxisAlignedBB[] AABB = new FaceAABB(0.1D, 0D, 0.05D, 0.9D, 0.3D, 0.7D).getRotatedBounds();
	private final static AxisAlignedBB[] DISPLAY = new FaceAABB(0.0625D, 0D, 0.1875D, 0.9375D, 0.75D, 0.5625D).getRotatedBounds();
	public static final PropertyBool ON = PropertyBool.create("on");
	private boolean keepInventory = false;
	private final int data;

	public BlockNotePC (String name, int data) {
        super(Material.GLASS, name);
        setHardness(0.2F);
        setResistance(9999F);
        setSoundType(SoundType.METAL);
        setLightLevel(0.35F);
        this.data = data;
		setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH).withProperty(ON, true));
		BlockInit.furniList.add(this);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		switch (this.data) {
		case 0:
			return new TileNotePC();
		case 1:
			return new TileDisplay();
		}
		return new TileNotePC();
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

		if (!this.keepInventory) {
	    	TileNotePC tile = (TileNotePC) world.getTileEntity(pos);
			ItemStack stack = new ItemStack(this);
			NBTTagCompound tags = new NBTTagCompound();
			tags.setTag("BlockEntityTag", tile.writeToNBT(new NBTTagCompound()));
			stack.setTagCompound(tags);
			spawnAsEntity(world, pos, stack);
			world.updateComparatorOutputLevel(pos, state.getBlock());
	        super.breakBlock(world, pos, state);
		}
	}

    public void setState(World world, IBlockState state, BlockPos pos, boolean flag) {
        TileEntity tile = world.getTileEntity(pos);
        this.keepInventory = true;
		world.setBlockState(pos, state.withProperty(ON, flag), 3);
		this.keepInventory = false;
        if (tile != null){
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (this.data) {
		case 0:
			return AABB[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		case 1:
			return DISPLAY[state.getValue(FACING).rotateY().getHorizontalIndex()];
		}

		return AABB[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
	}

	// 右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.NOTEPC_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		int d = world.provider.getDimension();
		SMChunkLoader.getInstance().setBlockTicket(world, pos.getX(), pos.getY(), pos.getZ(), pos.getX() >> 4, pos.getZ() >> 4, d);
	}

	@Override
	public boolean canLoad(World world, int x, int y, int z) {
		return true;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, ON });
	}

	// IBlockStateからItemStackのmetadataを生成。ドロップ時とテクスチャ・モデル参照時に呼ばれる
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex() + ( state.getValue(ON) ? 4 : 0 );
	}

	// ItemStackのmetadataからIBlockStateを生成。設置時に呼ばれる
	@Override
	public IBlockState getStateFromMeta(int meta) {

		boolean isOn = false;

		if (meta >= 4) {
			meta -= 4;
			isOn = true;
		}

		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(ON, isOn);
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    //ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
  		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.note_pc.name")));

  		if (this.data == 1) {
  	  		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.note_pc_addmenu.name")));
  		}
  	}
}
