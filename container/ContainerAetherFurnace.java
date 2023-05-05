package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.magic.TileAetherFurnace;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerAetherFurnace extends BaseContainer {

	protected TileAetherFurnace chest;

	public ContainerAetherFurnace(InventoryPlayer invPlayer, TileAetherFurnace tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileAetherFurnace) this.tile;

		for (int i = 0; i < 5; i++)
			this.addSlotToContainer(new ValidatedSlot(this.chest.getInput(), i, 57 + 18 * i, 31, SlotPredicates.ISNOTCRYSTAL));

		// Crystal
		for (int i = 0; i < 2; i++)
			for (int k = 0; k < 9; k++)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getCrystal(), k + i * 9, 8 + 18 * k, 96 + 18 * i, s -> false));

		this.initPlayerSlot(invPlayer, 8, 136, 8, 194);
	}

	@Override
	protected int getSlotSize() {
		return 23;
	}
}
