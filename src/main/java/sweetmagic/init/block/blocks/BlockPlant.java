package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class BlockPlant extends BaseModelBlock {

    public BlockPlant(String name) {
        super(Material.PLANTS, name);
        setHardness(1.0F);
        setResistance(1024F);
        setSoundType(SoundType.PLANT);
		BlockInit.blockList.add(this);
    }
}
