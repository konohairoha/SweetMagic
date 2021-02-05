package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;

public class BlockCafeBoard extends BaseFaceBlock {

	public BlockCafeBoard (String name) {
		super(Material.WOOD, name);
        setHardness(0.4F);
        setResistance(64F);
		this.setSoundType(SoundType.WOOD);
		BlockInit.blockList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.25, 0, 0.25, 0.75, 0.65, 0.75);
	}
}
