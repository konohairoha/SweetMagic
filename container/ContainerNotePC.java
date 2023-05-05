package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.chest.TileNotePC;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerNotePC extends BaseContainer {

	protected TileNotePC chest;

	public ContainerNotePC(InventoryPlayer invPlayer, TileNotePC tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileNotePC) this.tile;

		this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), 0, 16, 8 , s -> true));

		this.initPlayerSlot(invPlayer, 12, 176, 12, 232);
	}

	@Override
	protected int getSlotSize() {
		return this.chest.getInvSize();
	}
}
