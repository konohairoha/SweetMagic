package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.magic.TileMFTank;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerMFTank extends Container {

	final TileMFTank tile;

	public ContainerMFTank(InventoryPlayer invPlayer, TileMFTank tile) {
		this.tile = tile;
		this.initSlots(invPlayer);
	}

	void initSlots(InventoryPlayer invPlayer) {

		IItemHandler input = tile.getInput();
		IItemHandler output = tile.getOutput();

		//Input(0)
		this.addSlotToContainer(new ValidatedSlot(input, 0, 51, 35, SlotPredicates.MFBOTTLE));

		int counter = input.getSlots() - 1;

		counter = output.getSlots() - 1;

		//Output(0)
		this.addSlotToContainer(new ValidatedSlot(output, counter, 116, 35, s -> false));

		//Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		//Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
	}


	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return true;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {

		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 2) {
				if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index != 1 && index != 0) {
				if (itemstack1.getItem() == Items.GLASS_BOTTLE || itemstack1.getItem() == ItemInit.b_mf_bottle) {
					if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 2 && index < 29) {
					if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 29 && index < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}
}
