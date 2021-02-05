package sweetmagic.init.block.magic;

import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;

public class MagicDirt extends BaseMFBlock {

    public MagicDirt(String name) {
        super(name);
		BlockInit.noTabList.add(this);
    }
}