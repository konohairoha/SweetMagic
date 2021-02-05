package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.util.SittableUtil;

public class SMChair extends BaseFaceBlock {

	public final int data;

	public SMChair(String name, int data) {
		super(Material.WOOD, name);
        setHardness(1F);
        setSoundType(SoundType.WOOD);
        this.data = data;
		BlockInit.blockList.add(this);
    }

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (this.data) {
		case 0:
			return new AxisAlignedBB(0.175D, 0.55D, 0.175D, 0.825D, 0.0D, 0.825D);
		case 1:
			return new AxisAlignedBB(0.125D, 0.6D, 0.125D, 0.875D, 0.0D, 0.875D);
		}

		return new AxisAlignedBB(0.175D, 0.55D, 0.175D, 0.825D, 0.0D, 0.825D);
	}

	// 以下が座れるようにするための処理
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {

			double y = this.data == 0 ? -0.15D : -0.075D;

			if (SittableUtil.sitOnBlock(world, pos.getX(), pos.getY() + y, pos.getZ(), player, 6 * 0.0625)) {
				world.updateComparatorOutputLevel(pos, this);
			}
		}
		return true;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return SittableUtil.isSomeoneSitting(world, pos.getX(), pos.getY(), pos.getZ()) ? 1 : 0;
	}
}
