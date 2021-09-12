package sweetmagic.init.block.crop;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.blocks.PlantPot;

public class BlockCornFlower extends BlockBush {

	private final int data;

	public BlockCornFlower(String name, int meta) {
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setHardness(0F);
		this.setSoundType(SoundType.PLANT);
		this.disableStats();
		this.setCreativeTab(null);
		this.data = meta;
		BlockInit.magicList.add(this);
	}

	/**
	 * yellowflowerはデータ値ない
	 * redflowerのデータ値で飾りの花が決まってくる
	 * アリウムは2
	 */

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Plains;
	}

	@Deprecated
	public Vec3d getOffset(IBlockState state, IBlockAccess world, BlockPos pos) {
		double y = world.getBlockState(pos.down()).getBlock() instanceof PlantPot ? -0.0625D : 0D;
		long i = MathHelper.getCoordinateRandom(pos.getX(), 0, pos.getZ());
		return new Vec3d(((double) ((float) (i >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D, y, ((double) ((float) (i >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D);
	}

	//置いた後Renderで位置がずれる(XYZ または XZ)
	public EnumOffsetType getOffsetType() {
		return EnumOffsetType.XZ;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(new ItemStack(this));
	}
}
