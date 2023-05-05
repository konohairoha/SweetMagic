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

public class BlockVerticalHalf extends BaseFaceBlock {

	private final int data;
	private final static AxisAlignedBB[] AABB = new FaceAABB(0D, 0D, 0.5D, 1D, 1D, 1D).getRotatedBounds();

	public BlockVerticalHalf(String name, int data) {
		super(Material.WOOD, name);
        setHardness(0.25F);
        setResistance(1024F);
        setSoundType(data == 0 ? SoundType.STONE : SoundType.CLOTH);
        this.data = data;
		BlockInit.furniList.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
	}
}
