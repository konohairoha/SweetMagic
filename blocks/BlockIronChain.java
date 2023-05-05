package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class BlockIronChain extends BaseModelBlock {

	private static final PropertyBool TOP = PropertyBool.create("top");
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.35D, 0D, 0.35D, 0.65D, 1D, 0.65D);

    public BlockIronChain(String name) {
    	super(Material.GLASS, name);
        setHardness(0F);
        setResistance(1024F);
        setSoundType(SoundType.METAL);
		this.setEmptyAABB();
		setDefaultState(this.blockState.getBaseState().withProperty(TOP, false));
		BlockInit.furniList.add(this);
    }

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	//右クリックの処理
    @Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (!this.checkBlock(stack)) { return false;}

    	return this.setBlockSound(world, state, pos, player, stack, 10, true);
    }

	// ブロックチェック
    public boolean checkBlock (ItemStack stack) {
    	Item item = stack.getItem();
    	return item == Item.getItemFromBlock(this) || item == Item.getItemFromBlock(BlockInit.antique_lantern);
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
    	super.breakBlock(world, pos, state);
    	if (world.getBlockState(pos.down()).getBlock() == this) {
    		this.breakBlock(pos.down(), world, true);
    	}
    }

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(TOP, world.getBlockState(pos.up()).getBlock() == this);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { TOP });
	}
}
