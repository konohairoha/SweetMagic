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

	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.25D, 0D, 0.25D, 0.75D, 0.65D, 0.75D);
	private final static AxisAlignedBB STAND = new AxisAlignedBB(0.1D, 0D, 0.1D, 0.9D, 0.625D, 0.9D);
	private final int data;

	public BlockCafeBoard (String name, int data) {
		super(data == 0 ? Material.WOOD : Material.GLASS, name);
		this.data = data;
        setHardness(0.4F);
        setResistance(1024F);
		this.setSoundType(data == 0 ? SoundType.WOOD : SoundType.GLASS);
		BlockInit.furniList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (this.data) {
		case 0:
			return AABB;
		case 1:
			return STAND;
		}

		return AABB;
	}
}
