package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sweetmagic.init.entity.monster.EntityBlackCat;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerCatChest extends Container {

    private final EntityBlackCat entity;
    private final EntityPlayer player;

    public ContainerCatChest (EntityBlackCat entity, InventoryPlayer invPlayer) {

    	this.entity = entity;
    	this.player = invPlayer.player;

		for (int i = 0; i < 8; i++)
			for (int k = 0; k < 13; k++)
				this.addSlotToContainer(new ValidatedSlot(entity, k + i * 13, 12 + k * 18, 20 + i * 18, s -> true));

		// Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 48 + j * 18, 166 + i * 18));

		// Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 48 + i * 18, 225));
    }

	@Override
	public boolean canInteractWith(EntityPlayer player) {
        return /*this.entity.isUsableByPlayer(player) &&*/ this.entity.isEntityAlive() && this.entity.getDistance(player) < 8F;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		this.entity.writeBack();
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(slotIndex);

		int slotCount = 104;

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
			}

			else {
				slot.onSlotChanged();
			}
		}
		return stack;
	}
}
