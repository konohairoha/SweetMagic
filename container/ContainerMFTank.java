package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.IItemHandler;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.magic.TileMFTank;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerMFTank extends BaseContainer {

	protected TileMFTank chest;

	public ContainerMFTank(InventoryPlayer invPlayer, TileMFTank tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileMFTank) this.tile;

		IItemHandler output = this.chest.getOutput();

		//Input(0)
		this.addSlotToContainer(new ValidatedSlot(this.chest.getInput(), 0, 51, 35, SlotPredicates.MFBOTTLE));

		//Output(0)
		this.addSlotToContainer(new ValidatedSlot(output, output.getSlots() - 1, 116, 35, s -> false));

		this.initPlayerSlot(invPlayer, 8, 84, 8, 142);
	}

	@Override
	protected int getSlotSize() {
		return 2;
	}
}
