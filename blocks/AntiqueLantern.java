package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class AntiqueLantern extends BaseModelBlock {

	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.2D, 0.7D, 0.2D, 0.8D, 0D, 0.8D);

    public AntiqueLantern(String name) {
    	super(Material.GLASS, name);
        setHardness(0.7F);
        setResistance(1024F);
        setSoundType(SoundType.METAL);
        setLightLevel(1F);
		BlockInit.furniList.add(this);
    }

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Deprecated
	public Vec3d getOffset(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean downAir = world.getBlockState(pos.down()).getBlock() != Blocks.AIR;
		boolean upAir = world.getBlockState(pos.up()).getBlock() == Blocks.AIR;
		double y = downAir && upAir ? -0.3125D : 0D;
		return new Vec3d(0D, y, 0D);
	}
}
