package sweetmagic.init.block.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import sweetmagic.api.enumblock.EnumLocal;
import sweetmagic.api.enumblock.EnumLocal.PropertyLocal;

public class SMGlassPaneVertical extends SMGlassPane {

	public static final PropertyLocal LOCAL = new PropertyLocal("local", EnumLocal.getLocalList());

	public SMGlassPaneVertical(String name, List<Block> list, int data) {
		super(name, list, data);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(BACK, false)
				.withProperty(FORWARD, false)
				.withProperty(LEFT, false)
				.withProperty(RIGHT, false)
				.withProperty(LOCAL, EnumLocal.NOR));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean top = world.getBlockState(pos.down()).getBlock() == this;
		boolean bot = world.getBlockState(pos.up()).getBlock() == this;
		return super.getActualState(state, world, pos).withProperty(LOCAL, EnumLocal.getLocal(top, bot));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BACK, FORWARD, LEFT, RIGHT, LOCAL });
	}
}
