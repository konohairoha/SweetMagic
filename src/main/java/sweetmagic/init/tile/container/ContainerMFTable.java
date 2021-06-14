package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.slot.SlotArmor;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;
import sweetmagic.util.SMUtil;

public class ContainerMFTable extends Container {

	public final TileMFTable tile;

	public ContainerMFTable(InventoryPlayer invPlayer, TileMFTable tile) {
		this.tile = tile;
		this.initSlots(invPlayer);

		// MFアイテム
		this.addSlotToContainer(new ValidatedSlot(this.tile.getInput(), 0, 134, 67, SlotPredicates.MFF_FUEL));

		// Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 38 + j * 18, 138 + i * 18));

		// Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 38 + i * 18, 196));

        // Armor slots
        for (int y = 0; y < 4; y++)
            this.addSlotToContainer(new SlotArmor(invPlayer.player, SMUtil.getEquipmentSlot(y), invPlayer, 39 - y, 10, 138 + y * 18));
	}

	void initSlots(InventoryPlayer invPlayer) {

		this.addSlotToContainer(new ValidatedSlot(this.tile.getWand(), 0, 38, 49, SlotPredicates.SMWAND));

		if (this.tile.getInvSize() < 4) { return; }

		this.addSlotToContainer(new ValidatedSlot(this.tile.getWand(), 1, 38, 85, SlotPredicates.SMWAND));
		this.addSlotToContainer(new ValidatedSlot(this.tile.getWand(), 2, 15, 23, SlotPredicates.SMWAND));
		this.addSlotToContainer(new ValidatedSlot(this.tile.getWand(), 3, 62, 23, SlotPredicates.SMWAND));

		if (this.tile.getInvSize() < 6) { return; }

		this.addSlotToContainer(new ValidatedSlot(this.tile.getWand(), 4, 2, 60, SlotPredicates.SMWAND));
		this.addSlotToContainer(new ValidatedSlot(this.tile.getWand(), 5, 74, 60, SlotPredicates.SMWAND));
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return this.tile.isUsableByPlayer(player);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.getSlot(slotIndex);

		int slotCount = this.tile.getInvSize() + 1;

		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (slotIndex < slotCount && !this.mergeItemStack(stack1, slotCount, 36 + slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (slotIndex >= slotCount && !this.mergeItemStack(stack1, 0, slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (stack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return stack;
	}
}
