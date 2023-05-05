package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.util.FaceAABB;

public class BlockFaceLanp extends BaseFaceBlock {

	private final int data;
	private final static AxisAlignedBB[] SPOT = new FaceAABB(0D, 0.25D, 0.46875D, 1D, 1D, 0.53125D).getRotatedBounds();

	public BlockFaceLanp (String name, int data) {
		super(Material.GLASS, name);
		this.data = data;
        setHardness(0.2F);
        setResistance(1024F);
		setLightLevel(1);
		this.setSoundType(data == 0 ? SoundType.GLASS : SoundType.WOOD);
		BlockInit.furniList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (this.data) {
		case 0: return SPOT[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		}

		return FULL_BLOCK_AABB;
	}
}
