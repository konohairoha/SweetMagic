     package sweetmagic.worldgen.gen;

import net.minecraft.block.Block;
import sweetmagic.init.base.BaseFlowerGen;

public class CFlowerGen extends BaseFlowerGen {

	public CFlowerGen(Block flower, int seedRand) {
		super(flower);
		this.seedRand = seedRand;
	}
}
