package sweetmagic.init.block.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import sweetmagic.api.iblock.ISmeltItemBlock;
import sweetmagic.init.BlockInit;

public class ChestnutSlab extends AntiqueSlab implements ISmeltItemBlock {

    public ChestnutSlab(String name) {
        super(name, Material.WOOD);
		setSoundType(SoundType.WOOD);
    }

    public ChestnutSlab(String name, List<Block> list) {
        super(name, Material.WOOD);
		setSoundType(SoundType.WOOD);
		BlockInit.blockList.remove(this);
		list.add(this);
    }

	// 精錬時間の取得
	@Override
	public int getSmeltTime() {
		return 150;
	}
}
