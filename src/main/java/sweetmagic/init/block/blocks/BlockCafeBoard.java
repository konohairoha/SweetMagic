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

public class BlockCafeBoard extends BaseFaceBlock {

	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.25D, 0D, 0.25D, 0.75D, 0.65D, 0.75D);
	private final static AxisAlignedBB STAND = new AxisAlignedBB(0.1D, 0D, 0.1D, 0.9D, 0.625D, 0.9D);
	private final static AxisAlignedBB[] MENU = new FaceAABB(0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D, 1D).getRotatedBounds();
	private final int data;

	public BlockCafeBoard (String name, int data) {
		super(data == 1 ? Material.GLASS : Material.WOOD, name);
		this.data = data;
        setHardness(0.2F);
        setResistance(1024F);
		this.setSoundType(data == 1 ? SoundType.GLASS : SoundType.WOOD);
		BlockInit.furniList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (this.data) {
		case 0: return AABB;
		case 1: return STAND;
		case 2: return MENU[state.getValue(FACING).rotateYCCW().getHorizontalIndex()];
		}

		return AABB;
	}
}
