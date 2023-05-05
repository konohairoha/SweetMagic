package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.cook.TileStove;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerStove extends BaseContainer {

	protected TileStove chest;

	public ContainerStove(InventoryPlayer invPlayer, TileStove tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileStove) this.tile;

		//Fuel
		this.addSlotToContainer(new ValidatedSlot(this.chest.getFuel(), 0, 80, 18, SlotPredicates.FURNACE_FUEL));

		this.initPlayerSlot(invPlayer, 8, 70, 8, 128);
	}

	@Override
	protected int getSlotSize() {
		return 1;
	}
}
