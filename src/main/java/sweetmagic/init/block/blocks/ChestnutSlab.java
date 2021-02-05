package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import sweetmagic.api.iblock.ISmeltItemBlock;

public class ChestnutSlab extends AntiqueSlab implements ISmeltItemBlock {

    public ChestnutSlab(String name) {
        super(name, Material.WOOD);
		setSoundType(SoundType.WOOD);
    }

	// 精錬時間の取得
	@Override
	public int getSmeltTime() {
		return 150;
	}
}
