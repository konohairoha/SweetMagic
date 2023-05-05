package sweetmagic.init.tile.container;

import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.chest.TileWoodChest;
import sweetmagic.init.tile.slot.ValidatedSlot;

@ChestContainer(rowSize = 13)
public class ContainerWoodChest extends BaseContainer {

	protected TileWoodChest chest;

	public ContainerWoodChest(InventoryPlayer invPlayer, TileWoodChest tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileWoodChest) this.tile;

		for (int i = 0; i < 8; i++)
			for (int k = 0; k < 13; k++)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), k + i * 13, 12 + k * 18, 20 + i * 18, s -> true));

		this.initPlayerSlot(invPlayer, 48, 166, 48, 225);
	}

	@Override
	protected int getSlotSize() {
		return this.chest.getInvSize();
	}
}
