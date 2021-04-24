package sweetmagic.init.block.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class FlowerBuscket extends BaseModelBlock {

	private static final PropertyBool TOP = PropertyBool.create("top");
	private static final PropertyBool BOT = PropertyBool.create("bot");
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.2D, 0D, 0.2D, 0.8D, 1D, 0.8D);

	public FlowerBuscket (String name) {
		super(Material.PLANTS, name);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(TOP, false)
				.withProperty(BOT, false));
		BlockInit.blockList.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		Block block = world.getBlockState(pos.down()).getBlock();
		boolean bot = block == this;
		boolean top = world.getBlockState(pos.up()).getBlock() == this;
		return state.withProperty(TOP, bot).withProperty(BOT, top);
	}

	// 一番下か
	public boolean isBot (IBlockState state) {
		return state == state.withProperty(TOP, false).withProperty(BOT, true) ||
				state == state.withProperty(TOP, false).withProperty(BOT, false);
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
		return new BlockStateContainer(this, new IProperty[] { TOP, BOT });
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> aabbList, Entity entity, boolean flag) { }
}
