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
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerMFTable extends Container {

	public final TileMFTable tile;

	public ContainerMFTable(InventoryPlayer invPlayer, TileMFTable tile) {
		this.tile = tile;
		this.initSlots(invPlayer);
	}

	void initSlots(InventoryPlayer invPlayer) {

		// 杖
		this.addSlotToContainer(new ValidatedSlot(this.tile.getWand(), 0, 38, 48, SlotPredicates.SMWAND));

		// MFアイテム
		this.addSlotToContainer(new ValidatedSlot(this.tile.getInput(), 0, 134, 67, SlotPredicates.MFF_FUEL));

		// Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 138 + i * 18));

		// Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 196));
	}


	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.getSlot(slotIndex);

		int slotCount = 2;

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
