package sweetmagic.init.block.blocks;

import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import sweetmagic.init.BlockInit;

public class SMTrapDoor extends BlockTrapDoor {

	private final int data;

	public SMTrapDoor (String name, int data, Material material) {
		super(material);
        setRegistryName(name);
        setUnlocalizedName(name);
		setHardness(1F);
		setResistance(1024F);
		setSoundType(SoundType.WOOD);
		BlockInit.blockList.add(this);
		this.data = data;
	}
}
