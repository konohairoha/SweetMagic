package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.magic.TileToolRepair;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerToolRepair extends BaseContainer {

	protected TileToolRepair chest;

	public ContainerToolRepair(InventoryPlayer invPlayer, TileToolRepair tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileToolRepair) this.tile;

		//Input
		for (int i = 0; i < 4; ++i)
			this.addSlotToContainer(new ValidatedSlot(this.chest.getTool(), i, 57 + i * 18, 31, SlotPredicates.ISDAMA));

		this.initPlayerSlot(invPlayer, 8, 96, 8, 154);
	}

	@Override
	protected int getSlotSize() {
		return this.chest.getInvSize();
	}
}
