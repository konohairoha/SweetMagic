package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sweetmagic.init.tile.inventory.InventoryFarmClothes;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerFarmClothes extends Container {

	private final InventoryFarmClothes inventory;

	public ContainerFarmClothes(InventoryPlayer invPlayer, InventoryFarmClothes gemInv) {

		this.inventory = gemInv;

		for (int k = 0; k < 10; ++k)
			for (int j = 0; j < 15; ++j)
				this.addSlotToContainer(new ValidatedSlot(this.inventory, j + k * 15, 8 + j * 16, 6 + k * 17, s -> true));

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 48 + j * 17, 179 + i * 17));

		for (int l = 0; l < 9; ++l)
			this.addSlotToContainer(new Slot(invPlayer, l, 48 + l * 17, 233));

	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(slotIndex);

		int slotCount = 150;

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
