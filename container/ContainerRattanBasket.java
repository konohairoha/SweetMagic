package sweetmagic.init.tile.container;

import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.chest.TileRattanBasket;
import sweetmagic.init.tile.slot.ValidatedSlot;

@ChestContainer(rowSize = 9)
public class ContainerRattanBasket extends BaseContainer {

	protected TileRattanBasket chest;

	public ContainerRattanBasket(InventoryPlayer invPlayer, TileRattanBasket tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileRattanBasket) this.tile;

		for (int k = 0; k < 6; ++k)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), j + k * 9, 8 + j * 18, 18 + k * 18, s -> true));

		this.initPlayerSlot(invPlayer, 8, 140, 8, 198);
	}

	@Override
	protected int getSlotSize() {
		return this.chest.getInvSize();
	}
}
