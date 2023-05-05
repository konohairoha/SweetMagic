package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.magic.TileAetherHopper;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerAetherHopper extends BaseContainer {

	protected TileAetherHopper chest;

	public ContainerAetherHopper(InventoryPlayer invPlayer, TileAetherHopper tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileAetherHopper) this.tile;

		for (int i = 0; i < 3; i++)
			for (int k = 0; k < 6; k++)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), k + i * 6, 44 + k * 18, 11 + i * 18, s -> true));

		this.addSlotToContainer(new ValidatedSlot(this.chest.getClero(), 0, 44, 70, SlotPredicates.ISSTUFF));

		this.initPlayerSlot(invPlayer, 8, 93, 8, 151);
	}

	@Override
	protected int getSlotSize() {
		return this.chest.getInvSize() + 1;
	}
}
