package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sweetmagic.init.tile.inventory.InventoryPouch;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerPouch extends Container {

	private final InventoryPouch inventory;
	private final World world;
	private final EntityPlayer player;

	public ContainerPouch(InventoryPlayer invPlayer, InventoryPouch gemInv) {

		this.inventory = gemInv;
		this.player = invPlayer.player;
		this.world = this.player.getEntityWorld();
		SlotPredicates.stack = this.player.getHeldItemMainhand();

		for (int k = 0; k < 2; ++k)
			for (int j = 0; j < 4; ++j)
				this.addSlotToContainer(new ValidatedSlot(this.inventory, j + k * 4, 53 + j * 18, 8 + k * 18, SlotPredicates.ISMAGICITEMS));

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 7 + j * 18, 50 + i * 18));

		for (int l = 0; l < 9; ++l)
			this.addSlotToContainer(new Slot(invPlayer, l, 7 + l * 18, 108));

	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		this.inventory.writeBack();
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(slotIndex);

		int slotCount = this.inventory.getSlots();

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
		return true;
	}

	@Override
	public boolean canDragIntoSlot(Slot slot) {
		return true;
	}
}
