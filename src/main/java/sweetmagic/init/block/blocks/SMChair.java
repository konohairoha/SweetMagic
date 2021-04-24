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

	private final int data;
	private final static AxisAlignedBB CHAIR = new AxisAlignedBB(0.175D, 0.55D, 0.175D, 0.825D, 0D, 0.825D);
	private final static AxisAlignedBB CHAIR_2 = new AxisAlignedBB(0.125D, 0.6D, 0.125D, 0.875D, 0D, 0.875D);
	private final static AxisAlignedBB RATAN = new AxisAlignedBB(0.175D, 0.55D, 0.175D, 0.825D, 0.0D, 0.825D);

	public SMChair(String name, int data) {
		super(Material.WOOD, name);
		setHardness(0.5F);
		setResistance(1024F);
        setSoundType(SoundType.WOOD);
        this.data = data;
		BlockInit.blockList.add(this);
    }

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (this.data) {
		case 0: return CHAIR;
		case 1: return CHAIR_2;
		case 3: return CHAIR_2;
		}

		return RATAN;
	}

	// 以下が座れるようにするための処理
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!world.isRemote) {

			double y = 0;

			switch (this.data) {
			case 0:
				y = -0.15D;
				break;
			case 1:
				y = -0.075D;
				break;
			case 2:
				y = -0.25D;
				break;
			case 3:
				y = 0.05D;
				break;
			}

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
