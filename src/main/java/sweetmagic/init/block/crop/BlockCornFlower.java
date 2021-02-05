package sweetmagic.init.block.crop;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import sweetmagic.init.BlockInit;

public class BlockCornFlower extends BlockBush {

	public int data;

	public BlockCornFlower(String name, int meta) {
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setHardness(0.0F);
		this.setTickRandomly(true);
		this.setSoundType(SoundType.PLANT);
		this.disableStats();
		this.setCreativeTab(null);
		this.data = meta;
		BlockInit.blockList.add(this);
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

	//置いた後Renderで位置がずれる(XYZ または XZ)
	public EnumOffsetType getOffsetType() {
		return EnumOffsetType.XZ;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(new ItemStack(this));
	}
}
