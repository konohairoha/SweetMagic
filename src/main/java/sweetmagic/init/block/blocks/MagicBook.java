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

	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.05D, 0D, 0.05D, 0.95D, 0.875D, 0.95D);

	public MagicBook(String name) {
		super(Material.WOOD, name);
		setHardness(1.0F);
        setResistance(1024F);
		setSoundType(SoundType.WOOD);
		BlockInit.furniList.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return 1F;
	}
}
