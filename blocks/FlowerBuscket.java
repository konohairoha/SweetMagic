package sweetmagic.init.block.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.api.enumblock.EnumLocal;
import sweetmagic.api.enumblock.EnumLocal.PropertyLocal;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class FlowerBuscket extends BaseModelBlock {

	protected static final PropertyLocal LOCAL = new PropertyLocal("local", EnumLocal.getLocalList());
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.2D, 0D, 0.2D, 0.8D, 1D, 0.8D);

	public FlowerBuscket (String name) {
		super(Material.PLANTS, name);
		this.setEmptyAABB();
		setDefaultState(this.blockState.getBaseState().withProperty(LOCAL, EnumLocal.NOR));
		BlockInit.furniList.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	//右クリックの処理
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float x, float y, float z) {

        ItemStack stack = player.getHeldItem(hand);
		if (!this.checkBlock(stack)) { return false; }

    	return this.setBlockSound(world, state, pos, player, stack, 10, true);
    }

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean top = this.getBlock(world, pos.down()) == this;
		boolean bot = this.getBlock(world, pos.up()) == this;
		return state.withProperty(LOCAL, EnumLocal.getLocal(top, bot));
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
		return new BlockStateContainer(this, new IProperty[] { LOCAL });
	}
}
