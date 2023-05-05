package sweetmagic.init.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import sweetmagic.init.BlockInit;

public class AntiqueBrick extends Block {

    public AntiqueBrick(String name, float hardness, float resistance, int harvestLevel, float light) {
        super(Material.ROCK);
        setUnlocalizedName(name);
        setRegistryName(name);
        setHardness(hardness);
        setResistance(resistance);
        setHarvestLevel("pickaxe", harvestLevel);
        setSoundType(SoundType.STONE);
        setLightLevel(light);
		BlockInit.blockList.add(this);
    }
}
