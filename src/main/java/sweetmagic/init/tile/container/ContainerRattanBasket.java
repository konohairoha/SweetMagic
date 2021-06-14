package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sweetmagic.init.tile.chest.TileRattanBasket;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerRattanBasket extends Container {

	public final TileRattanBasket tile;

	public ContainerRattanBasket(InventoryPlayer invPlayer, TileRattanBasket tile) {

		this.tile = tile;
		for (int k = 0; k < 6; ++k)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new ValidatedSlot(this.tile.getChest(), j + k * 9, 8 + j * 18, 18 + k * 18, s -> true));

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

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return this.tile.isUsableByPlayer(player);
	}

	@Override
	public boolean canDragIntoSlot(Slot slot) {
		return true;
	}
}
