package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;

public class BlockFaceWood extends BaseFaceBlock {

	private final int data;
	private static final AxisAlignedBB PATH_TREE = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.0125D, 1D);

	public BlockFaceWood (String name, SoundType sound, int data) {
		super(Material.WOOD, name);
        setSoundType(sound);
        setHardness(1F);
        setResistance(1024F);
        this.data = data;
		BlockInit.furniList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {

		switch (this.data) {
		case 0:
			return FULL_BLOCK_AABB;
		case 1:
			return PATH_TREE;
		}

		return FULL_BLOCK_AABB;
	}
}
