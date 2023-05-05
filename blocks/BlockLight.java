package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import sweetmagic.init.BlockInit;

public class BlockLight extends SMIron {

    public BlockLight(String name) {
    	super(Material.GLASS, name);
        setHardness(0.7F);
        setResistance(64F);
        setHarvestLevel("pickaxe", 1);
        setSoundType(SoundType.METAL);
        setLightLevel(1F);
		BlockInit.blockList.add(this);
    }
}
