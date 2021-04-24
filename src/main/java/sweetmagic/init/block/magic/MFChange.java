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

	private static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.5D, 1D);
	private final int data;

	public MFChange (String name, int data) {
		super(name);
		this.data = data;
		this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
		BlockInit.magicList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return; }

		int guiId = 0;

		switch (this.data) {
		case 0:
			guiId = SMGuiHandler.MFCHANGER_GUI;
			break;
		case 1:
			guiId = SMGuiHandler.MFCHANGER_ADVANCED_GUI;
			break;
		}

		player.openGui(SweetMagicCore.INSTANCE, guiId, world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {

		switch (this.data) {
		case 0:	return new TileMFChanger();
		case 1:	return new TileMFChangerAdvanced();
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
