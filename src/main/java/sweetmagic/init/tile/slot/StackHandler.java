package sweetmagic.init.tile.slot;

import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.init.tile.magic.TileSMBase;

public class StackHandler extends ItemStackHandler {

	TileSMBase tile;

	public StackHandler (TileSMBase tile, int size) {
		super(size);
		this.tile = tile;
	}

	@Override
	public void onContentsChanged(int slot) {
		this.tile.markDirty();
	}
}