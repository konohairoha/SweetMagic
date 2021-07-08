package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;

public class SMTableDot extends BaseFaceBlock {

	public SMTableDot(String name) {
		super(Material.WOOD, name);
		setSoundType(SoundType.WOOD);
		setHardness(0.5F);
        setResistance(1024F);
		BlockInit.blockList.add(this);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
	}
}
