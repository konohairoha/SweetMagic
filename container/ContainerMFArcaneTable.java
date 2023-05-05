package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.magic.TileMFArcaneTable;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerMFArcaneTable extends BaseContainer {

	protected TileMFArcaneTable chest;

	public ContainerMFArcaneTable(InventoryPlayer invPlayer, TileMFArcaneTable tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileMFArcaneTable) this.tile;

		//Input
		for (int i = 0; i < 2; i++)
			this.addSlotToContainer(new ValidatedSlot(this.chest.getWand(), i, 39, 18 + i * 36, SlotPredicates.ISITEM));

		this.initPlayerSlot(invPlayer, 8, 96, 8, 154);
	}

	@Override
	protected int getSlotSize() {
		return 2;
	}
}
