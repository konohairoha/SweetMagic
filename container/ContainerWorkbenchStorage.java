package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.magic.TileWorkbenchStorage;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerWorkbenchStorage extends BaseContainer {

	protected TileWorkbenchStorage chest;

	public ContainerWorkbenchStorage(InventoryPlayer invPlayer, TileWorkbenchStorage tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileWorkbenchStorage) this.tile;

		//Input
		for (int i = 0; i < 3; i++)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getTool(), i, 55 + i * 41, 32, SlotPredicates.ISBLOCK));

		//Output Storage
		for (int i = 0; i < 3; i++)
			for (int k = 0; k < 9; k++)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getOut(), k + i * 9, 8 + 18 * k, 90 + 18 * i, s -> false));

		this.initPlayerSlot(invPlayer, 8, 148, 8, 206);
	}

	@Override
	protected int getSlotSize() {
		return this.chest.getInvSize() + 3;
	}
}
