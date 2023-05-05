package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.magic.TileMagiaWrite;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerMagiaWrite extends BaseContainer {

	protected TileMagiaWrite chest;

	public ContainerMagiaWrite(InventoryPlayer invPlayer, TileMagiaWrite tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileMagiaWrite) this.tile;

		//Input
		this.addSlotToContainer(new ValidatedSlot(this.chest.getTool(), 0, 39, 39, SlotPredicates.HASENCHA));

		this.initPlayerSlot(invPlayer, 8, 96, 8, 154);
	}

	@Override
	protected int getSlotSize() {
		return 1;
	}
}
