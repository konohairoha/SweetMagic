package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.tile.magic.TileAetherFurnace;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerAetherFurnace extends Container {

	final TileAetherFurnace tile;

	public ContainerAetherFurnace(InventoryPlayer invPlayer, TileAetherFurnace tile) {
		this.tile = tile;
		this.initSlots(invPlayer);
	}

	void initSlots(InventoryPlayer invPlayer) {

		// Fuel
		for (int i = 0; i < 5; i++)
			this.addSlotToContainer(new ValidatedSlot(this.tile.getInput(), i, 57 + 18 * i, 31, SlotPredicates.ISNOTCRYSTAL));

		// Crystal
		for (int i = 0; i < 2; i++)
			for (int k = 0; k < 9; k++)
				this.addSlotToContainer(new ValidatedSlot(this.tile.getCrystal(), k + i * 9, 8 + 18 * k, 96 + 18 * i, s -> false));

		// Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 136 + i * 18));

		// Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 194));
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
		int slotCount = 23;

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
