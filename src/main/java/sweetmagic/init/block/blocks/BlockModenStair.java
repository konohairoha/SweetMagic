package sweetmagic.init.block.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.util.Bounds;

public class BlockModenStair extends BaseFaceBlock {

    private static final AxisAlignedBB[] BOTTOM = new Bounds(0, 0, 0, 16, 8, 16).getRotatedBounds();
    private static final AxisAlignedBB[] TOP = new Bounds(8, 0, 0, 16, 16, 16).getRotatedBounds();

	public BlockModenStair(String name) {
		super(Material.WOOD, name);
		setHardness(1.0F);
		setResistance(16F);
		setSoundType(SoundType.WOOD);
		BlockInit.blockList.add(this);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		EnumFacing facing = state.getValue(FACING);
		Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BOTTOM[facing.getHorizontalIndex()]);
		Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, TOP[facing.getHorizontalIndex()]);
	}

	protected List<AxisAlignedBB> getCollisionBoxList(IBlockState state, World world, BlockPos pos) {
		List<AxisAlignedBB> boxes = new ArrayList<>();
		EnumFacing facing = state.getValue(FACING);
		boxes.add(BOTTOM[facing.getHorizontalIndex()]);
		boxes.add(TOP[facing.getHorizontalIndex()]);
		return boxes;
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, World world, BlockPos pos, Vec3d start, Vec3d end) {
		List<RayTraceResult> list = Lists.newArrayList();

		for (AxisAlignedBB axisalignedbb : getCollisionBoxList(blockState, world, pos)) {
			list.add(this.rayTrace(pos, start, end, axisalignedbb));
		}

		RayTraceResult result = null;
		double d1 = 0.0D;

		for (RayTraceResult raytraceresult : list) {
			if (raytraceresult != null) {
				double d0 = raytraceresult.hitVec.squareDistanceTo(end);
				if (d0 > d1) {
					result = raytraceresult;
					d1 = d0;
				}
			}
		}

		return result;
	}
}
