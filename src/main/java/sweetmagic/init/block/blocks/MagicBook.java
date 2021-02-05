package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;

public class MagicBook extends BaseFaceBlock {

	public MagicBook(String name) {
		super(Material.WOOD, name);
		setHardness(1.0F);
        setResistance(1024F);
		setSoundType(SoundType.WOOD);
		BlockInit.blockList.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.05D, 0D, 0.05D, 0.95D, 0.875D, 0.95D);
	}

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return 1F;
	}
}
