package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class BlockBreadbasket extends BaseModelBlock {

	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.2D, 0D, 0.2D, 0.8D, 1D, 0.8D);
	private final int data;

	public BlockBreadbasket (String name, int data) {
		super(Material.PLANTS, name);
		setSoundType(data == 0 ? SoundType.WOOD : SoundType.METAL);
		this.data = data;
		this.setEmptyAABB();
		BlockInit.furniList.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}
}
