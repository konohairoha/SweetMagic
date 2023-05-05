package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.magic.TileMFHarvester;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerMFHarvester extends BaseContainer {

	protected TileMFHarvester chest;

	public ContainerMFHarvester(InventoryPlayer invPlayer, TileMFHarvester tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileMFHarvester) this.tile;

		//Output Storage
		for (int i = 0; i < 3; i++)
			for (int k = 0; k < 9; k++)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getOutput(), k + i * 9, 8 + 18 * k, 90 + 18 * i, s -> false));

		this.initPlayerSlot(invPlayer, 8, 148, 8, 206);
	}

	@Override
	protected int getSlotSize() {
		return this.chest.getInvSize() + 1;
	}
}
