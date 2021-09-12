package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.tile.inventory.InventoryPouch;
import sweetmagic.init.tile.inventory.InventorySMWand;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerPouch extends Container {

	private final InventoryPouch inventory;
	private final World world;
	private final EntityPlayer player;
	private final ItemStack stack;
	private boolean isWand;
	private int slotMaxCount = 0;

	public ContainerPouch(InventoryPlayer invPlayer, InventoryPouch gemInv) {

		this.inventory = gemInv;
		this.player = invPlayer.player;
		this.world = this.player.getEntityWorld();
		this.stack = this.player.getHeldItemMainhand();
		this.isWand = this.stack.getItem() instanceof IWand && !this.player.isCreative();
		int downY = isWand ? 113 : 0;

		if (this.isWand) {
			this.wandContainer(this.stack);
		}

		if (this.inventory.slotSize == 8) {
			for (int k = 0; k < 2; ++k)
				for (int j = 0; j < 4; ++j)
					this.addSlotToContainer(new ValidatedSlot(this.inventory, j + k * 4, 53 + j * 18, downY + 7 + k * 18, SlotPredicates.ISMAGICITEMS));
		}

		else {
			for (int k = 0; k < 2; ++k)
				for (int j = 0; j < 8; ++j)
					this.addSlotToContainer(new ValidatedSlot(this.inventory, j + k * 8, 17 + j * 18, downY + 7 + k * 18, SlotPredicates.ISMAGICITEMS));
		}

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 7 + j * 18, downY + 49 + i * 18));

		for (int l = 0; l < 9; ++l)
			this.addSlotToContainer(new Slot(invPlayer, l, 7 + l * 18, downY + 107));
	}

	public void wandContainer (ItemStack stack) {

		IWand wand = (IWand) stack.getItem();
		int slotCount = 0;
		this.slotMaxCount = wand.getSlot();
		InventorySMWand wandInv = new InventorySMWand(stack, this.player);
		SlotPredicates.stack = this.player.getHeldItemMainhand();

		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {
				this.addSlotToContainer(new ValidatedSlot(wandInv, y * 5 + x, 37 + x * 21, 7 + y * 20, SlotPredicates.SMELEMENT));
				slotCount++;
				if (slotCount >= this.slotMaxCount) { return; }
			}
		}
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

		int wandSlot = this.slotMaxCount;
		int slotCount = this.inventory.getSlots() + wandSlot;

		if (slot != null && slot.getHasStack()) {

			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (slotIndex >= wandSlot && slotIndex < slotCount) {

				if (this.isWand && SlotPredicates.canInputSMItem(stack1) && !this.mergeItemStack(stack1, 0, wandSlot, false)) {
					return ItemStack.EMPTY;
				}

				else if (!this.mergeItemStack(stack1, slotCount, 36 + slotCount, false)) {
					return ItemStack.EMPTY;
				}

			}

			if (slotIndex < slotCount && !this.mergeItemStack(stack1, slotCount, 36 + slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (slotIndex >= slotCount && !this.mergeItemStack(stack1, wandSlot, slotCount, false)) {
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

	public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {

		if (slotId >= 0 && this.stack == this.inventorySlots.get(slotId).getStack()) {
			return ItemStack.EMPTY;
		}

		if (slotId >= 0 && slotId < this.slotMaxCount) {

			ItemStack stack = player.inventory.getItemStack();
			Slot slot = this.inventorySlots.get(slotId);

			if (!stack.isEmpty() && SlotPredicates.isMagicItems(stack) ) {
				int i3 = dragType == 0 ? stack.getCount() : 1;

				if (i3 > slot.getItemStackLimit(stack)) {
					i3 = slot.getItemStackLimit(stack);
				}

				slot.putStack(stack.splitStack(i3));

				return stack;
			}
		}

		return super.slotClick(slotId, dragType, clickType, player);
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
