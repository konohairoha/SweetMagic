package sweetmagic.init.tile.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.IItemHandler;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.slot.SlotArmor;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;
import sweetmagic.util.SMUtil;

public class ContainerMFTable extends BaseContainer {

	protected TileMFTable chest;

	public ContainerMFTable(InventoryPlayer invPlayer, TileMFTable tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileMFTable) this.tile;

		int size = this.chest.getInvSize();
		IItemHandler wand = this.chest.getWand();

		this.addSlotToContainer(new ValidatedSlot(wand, 0, 38, 49, SlotPredicates.SMWAND));

		if (size >= 4) {
			this.addSlotToContainer(new ValidatedSlot(wand, 1, 38, 85, SlotPredicates.SMWAND));
			this.addSlotToContainer(new ValidatedSlot(wand, 2, 15, 23, SlotPredicates.SMWAND));
			this.addSlotToContainer(new ValidatedSlot(wand, 3, 62, 23, SlotPredicates.SMWAND));
		}

		if (size >= 6) {
			this.addSlotToContainer(new ValidatedSlot(wand, 4, 2, 60, SlotPredicates.SMWAND));
			this.addSlotToContainer(new ValidatedSlot(wand, 5, 74, 60, SlotPredicates.SMWAND));
		}

		// MFアイテム
		this.addSlotToContainer(new ValidatedSlot(this.chest.getInput(), 0, 134, 67, SlotPredicates.MFF_FUEL));

		this.initPlayerSlot(invPlayer, 38, 138, 38, 196);

        // Armor slots
        for (int y = 0; y < 4; y++)
            this.addSlotToContainer(new SlotArmor(invPlayer.player, SMUtil.getEquipmentSlot(y), invPlayer, 39 - y, 10, 138 + y * 18));
	}

	@Override
	protected int getSlotSize() {
		return this.chest.getInvSize() + 1;
	}
}
