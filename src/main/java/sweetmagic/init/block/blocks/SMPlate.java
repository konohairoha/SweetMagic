package sweetmagic.init.block.blocks;

import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import sweetmagic.init.BlockInit;

public class SMPlate extends BlockPressurePlate {

    public SMPlate(String name) {
        super(Material.WOOD, BlockPressurePlate.Sensitivity.MOBS);
        setUnlocalizedName(name);
        setRegistryName(name);
        setHardness(0.4F);
        setResistance(4.0F);
        setSoundType(SoundType.WOOD);
		BlockInit.blockList.add(this);
    }
}
