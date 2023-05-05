package sweetmagic.init.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import sweetmagic.init.BlockInit;

public class SMIron extends Block {

    public SMIron(String name, float hardness, float resistance, int harvestLevel, float light) {
        super(Material.IRON);
        setUnlocalizedName(name);
        setRegistryName(name);
        setHardness(hardness);
        setResistance(resistance);
        setHarvestLevel("pickaxe", harvestLevel);
        setSoundType(SoundType.METAL);
        setLightLevel(light);
		BlockInit.blockList.add(this);
    }

    public SMIron(Material mate, String name) {
        super(mate);
        setUnlocalizedName(name);
        setRegistryName(name);
    }
}