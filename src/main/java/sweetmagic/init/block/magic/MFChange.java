package sweetmagic.init.block.magic;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.tile.magic.TileMFChanger;
import sweetmagic.init.tile.magic.TileMFChangerAdvanced;

public class MFChange extends BaseMFBlock {

	public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
	public final int data;

	public MFChange (String name, int data) {
		super(name);
		this.data = data;
		this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
		BlockInit.blockList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
	}

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return; }

		switch (this.data) {
		case 0:
			player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.MFCHANGER_GUI, world, pos.getX(), pos.getY(), pos.getZ());
			break;
		case 1:
			player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.MFCHANGER_ADVANCED_GUI, world, pos.getX(), pos.getY(), pos.getZ());
			break;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		switch (this.data) {
		case 0:
			return new TileMFChanger();
		case 1:
			return new TileMFChangerAdvanced();
		}
    	return null;
	}

	//===========================
	// blockstate持たせるための必要なソース

	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		return ((Integer) blockState.getValue(LEVEL)).intValue();
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
	}

	public int getMetaFromState(IBlockState state) {
		return ((Integer) state.getValue(LEVEL)).intValue();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { LEVEL });
	}

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return 1;
	}
}
