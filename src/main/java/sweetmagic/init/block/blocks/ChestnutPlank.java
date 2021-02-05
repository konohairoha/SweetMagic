package sweetmagic.init.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import sweetmagic.api.iblock.ISmeltItemBlock;
import sweetmagic.init.BlockInit;

public class ChestnutPlank extends Block implements ISmeltItemBlock {

    public ChestnutPlank(String name) {
        super(Material.WOOD);
        setUnlocalizedName(name);
        setRegistryName(name);
        setHardness(1.0F);
        setResistance(1024F);
        setSoundType(SoundType.WOOD);
		BlockInit.blockList.add(this);
    }

	// 精錬時間の取得
	@Override
	public int getSmeltTime() {
		return 300;
	}
}
