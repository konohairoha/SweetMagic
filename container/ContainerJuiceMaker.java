package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.cook.TileJuiceMaker;
import sweetmagic.init.tile.slot.CookedItemSlot;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerJuiceMaker extends BaseContainer {

	protected TileJuiceMaker chest;

	public ContainerJuiceMaker(InventoryPlayer invPlayer, TileJuiceMaker tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileJuiceMaker) this.tile;

		this.addSlotToContainer(new ValidatedSlot(this.chest.getWater(), 0, 8, 78, SlotPredicates.WATERCUP));
		this.addSlotToContainer(new ValidatedSlot(this.chest.getHand(), 0, 71, 8, SlotPredicates.ALLITEM));

		// 調理スロット
		for (int i = 0; i < 3; i++)
			this.addSlotToContainer(new ValidatedSlot(this.chest.getInput(), i, 71, 44 + 18 * i, SlotPredicates.ALLITEM));

		// 出力スロット
		for (int i = 0; i < 4; i++)
			this.addSlotToContainer(new CookedItemSlot(this.chest.getOutput(), i, 134, 8 + 18* i, s -> false));

		this.initPlayerSlot(invPlayer, 8, 103, 8, 161);
	}

	@Override
	protected int getSlotSize() {
		return 9;
	}
}
