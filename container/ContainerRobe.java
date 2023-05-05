package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sweetmagic.init.tile.inventory.InventoryRobe;
import sweetmagic.init.tile.slot.ValidatedSlot;

@ChestContainer(rowSize = 9)
public class ContainerRobe extends Container {

	private final InventoryRobe inventory;

	public ContainerRobe(InventoryPlayer invPlayer, InventoryRobe gemInv) {

		this.inventory = gemInv;

		for (int k = 0; k < 6; ++k)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new ValidatedSlot(this.inventory, j + k * 9, 8 + j * 18, 18 + k * 18, s -> true));

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));

		for (int l = 0; l < 9; ++l)
			this.addSlotToContainer(new Slot(invPlayer, l, 8 + l * 18, 198));

	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(slotIndex);

		int slotCount = 54;

		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (slotIndex < slotCount && !this.mergeItemStack(stack1, slotCount, 36 + slotCount, true)) {
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

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		this.inventory.writeBack();
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return true;
	}

	@Override
	public boolean canDragIntoSlot(Slot slot) {
		return true;
	}
}
