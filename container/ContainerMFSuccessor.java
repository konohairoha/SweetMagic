package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.magic.TileMFSuccessor;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerMFSuccessor extends BaseContainer {

	protected TileMFSuccessor chest;

	public ContainerMFSuccessor(InventoryPlayer invPlayer, TileMFSuccessor tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileMFSuccessor) this.tile;

		// オリジナル
		for (int i = 0; i < 2; i++)
			this.addSlotToContainer(new ValidatedSlot(this.chest.getWand(), i, 46 + 65 * i, 30, SlotPredicates.SMWAND));

		this.initPlayerSlot(invPlayer, 8, 96, 8, 154);
	}

	@Override
	protected int getSlotSize() {
		return 2;
	}
}
