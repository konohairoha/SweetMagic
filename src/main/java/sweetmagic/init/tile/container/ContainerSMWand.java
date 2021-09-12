package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sweetmagic.init.tile.inventory.InventoryPouch;
import sweetmagic.init.tile.inventory.InventorySMWand;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerSMWand extends Container {

	private final InventorySMWand inventory;
	private final World world;
	private final EntityPlayer player;
	private final int slot;
	public InventoryPouch invPouch;
	public boolean isPouch = false;
	public int slotSize = 0;

	public ContainerSMWand(InventoryPlayer invPlayer, InventorySMWand gemInv) {

		this.inventory = gemInv;
		this.player = invPlayer.player;
		this.world = this.player.getEntityWorld();
		this.slot = this.inventory.getSlots();
		SlotPredicates.stack = this.player.getHeldItemMainhand();

		switch (this.slot) {
		case 2:
			this.twoSlot(gemInv);
			break;
		case 3:
			this.threeSlot(gemInv);
			break;
		case 4:
			this.fourSlot(gemInv);
			break;
		case 6:
			this.sixSlot(gemInv);
			break;
		case 8:
			this.eightSlot(gemInv);
			break;
		case 9:
			this.nineSlot(gemInv);
			break;
		case 12:
			this.to12Slot(gemInv);
			break;
		case 16:
			this.to16Slot(gemInv);
			break;
		case 20:
			this.to20Slot(gemInv);
			break;
		case 25:
			this.to25Slot(gemInv);
			break;
		}

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 16 + j * 18, 112 + i * 18));

		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 16 + i * 18, 170));
	}

	// 2スロット
	public void twoSlot (InventorySMWand gemInv) {
		for (int j = 0; j < this.slot; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j, 60 + j * 60, 60, SlotPredicates.SMELEMENT));
	}

	// 3スロット
	public void threeSlot (InventorySMWand gemInv) {
		for (int j = 0; j < this.slot; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j, 60 + j * 30, 60, SlotPredicates.SMELEMENT));
	}

	// 4スロット
	public void fourSlot (InventorySMWand gemInv) {

		for (int j = 0; j < 2; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j, 60 + j * 50, 40, SlotPredicates.SMELEMENT));

		for (int j = 0; j < 2; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j + 2, 60 + j * 50, 80, SlotPredicates.SMELEMENT));
	}

	// 6スロット
	public void sixSlot (InventorySMWand gemInv) {

		for (int j = 0; j < 3; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j, 60 + j * 30, 40, SlotPredicates.SMELEMENT));

		for (int j = 0; j < 3; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j + 3, 60 + j * 30, 80, SlotPredicates.SMELEMENT));
	}

	// 8スロット
	public void eightSlot (InventorySMWand gemInv) {

		for (int j = 0; j < 3; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j, 60 + j * 30, 30, SlotPredicates.SMELEMENT));

		for (int j = 0; j < 2; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j + 3, 60 + j * 60, 60, SlotPredicates.SMELEMENT));

		for (int j = 0; j < 3; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, j + 5, 60 + j * 30, 90, SlotPredicates.SMELEMENT));
	}

	// 9スロット
	public void nineSlot (InventorySMWand gemInv) {

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, i * 3 + j, 60 + j * 30, 30 + i * 30, SlotPredicates.SMELEMENT));
	}

	// 12スロット
	public void to12Slot (InventorySMWand gemInv) {

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 4; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, i * 4 + j, 50 + j * 30, 30 + i * 30, SlotPredicates.SMELEMENT));
	}

	// 16スロット
	public void to16Slot (InventorySMWand gemInv) {

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, i * 4 + j, 50 + j * 30, 20 + i * 23, SlotPredicates.SMELEMENT));
	}

	// 25スロット
	public void to20Slot (InventorySMWand gemInv) {

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 5; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, i * 5 + j, 40 + j * 24, 14 + i * 21, SlotPredicates.SMELEMENT));
	}

	// 25スロット
	public void to25Slot (InventorySMWand gemInv) {

		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
			this.addSlotToContainer(new ValidatedSlot(gemInv, i * 5 + j, 40 + j * 24, 14 + i * 19, SlotPredicates.SMELEMENT));
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(slotIndex);

		int slotCount = this.slot + this.slotSize;

		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (slotIndex <= slotCount && !this.mergeItemStack(stack1, slotCount, 36 + slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (slotIndex > slotCount && !this.mergeItemStack(stack1, 0, slotCount, false)) {
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

		if (this.isPouch) {
			this.invPouch.writeBack();
		}
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return true;
	}
}
