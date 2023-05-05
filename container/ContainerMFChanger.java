package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.IItemHandler;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.magic.TileMFChanger;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerMFChanger extends BaseContainer {

	protected TileMFChanger chest;

	public ContainerMFChanger(InventoryPlayer invPlayer, TileMFChanger tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {

		this.chest = (TileMFChanger) this.tile;
		IItemHandler fuel = this.chest.getInput();

		if (this.getSlotSize() >= 5) {

			for (int i = 0; i < 5; i++)
				this.addSlotToContainer(new ValidatedSlot(fuel, i, 44 + i * 18, 27, SlotPredicates.MFF_FUEL));
		}

		else {
			this.addSlotToContainer(new ValidatedSlot(fuel, 0, 80, 27, SlotPredicates.MFF_FUEL));
		}

		this.initPlayerSlot(invPlayer, 8, 70, 8, 128);
	}

	@Override
	protected int getSlotSize() {
		return this.chest.getInvSize();
	}
}
