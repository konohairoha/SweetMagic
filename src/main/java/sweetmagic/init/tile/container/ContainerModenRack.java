package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerModenRack extends Container {

	final TileModenRack tile;

	public ContainerModenRack(InventoryPlayer invPlayer, TileModenRack tile) {
		this.tile = tile;

		try {
			this.addSlot();
		}

		catch (Throwable e) {
			invPlayer.closeInventory(invPlayer.player);
		}

		// Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 7 + j * 18, 50 + i * 18));

		// Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 7 + i * 18, 108));
	}

	public void addSlot () {

		// データ値で分岐
		switch (this.tile.getRackData()) {
		case 0:
			for (int i = 0; i < 2; i++)
				for (int k = 0; k < 9; k++)
					this.addSlotToContainer(new ValidatedSlot(this.tile.getChest(), k + i * 9, 7 + 18 * k, 8 + 18 * i, s -> true));
			break;
		case 1:
			for (int i = 0; i < 3; i++)
				this.addSlotToContainer(new ValidatedSlot(this.tile.getChest(), i, 61 + 18 * i, 25, s -> true));
			break;
		case 2:
		case 3:
		case 4:
		case 5:
			this.addSlotToContainer(new ValidatedSlot(this.tile.getChest(), 0, 79, 25, s -> true));
			break;
		case 6:
			for (int i = 0; i < 2; i++)
				this.addSlotToContainer(new ValidatedSlot(this.tile.getChest(), i, 79, 11 + 18 * i, s -> true));
			break;
		}
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return this.tile.isUsableByPlayer(player);
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.getSlot(slotIndex);
		int slotCount = this.tile.getInvSize();

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
