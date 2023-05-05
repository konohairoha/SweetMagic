package sweetmagic.init.block.blocks;

import sweetmagic.init.BlockInit;

public class SMGlassAlpha extends SMGlass {

	public SMGlassAlpha (String name, int data, boolean shading, boolean isPass) {
		super(name, data, shading, isPass);
		BlockInit.blockList.remove(this);
		BlockInit.noTabList.add(this);
	}
}
