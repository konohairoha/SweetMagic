package sweetmagic.init.base;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.tile.magic.TileSMBase;

public abstract class BaseContainer extends Container {

	protected final TileSMBase tile;

	public BaseContainer(InventoryPlayer invPlayer, TileSMBase tile) {
		this.tile = tile;
		this.initSlots(invPlayer);
	}

	protected abstract void initSlots(InventoryPlayer invPlayer);
	protected abstract int getSlotSize();

	protected void initPlayerSlot (InventoryPlayer invPlayer, int invX, int invY, int hotX, int hotY) {

		//Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, invX + j * 18, invY + i * 18));

		//Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, hotX + i * 18, hotY));
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return this.tile.isUsableByPlayer(player);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) { }

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {

		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.getSlot(index);
		int slotCount = this.getSlotSize();

		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (index < slotCount && !this.mergeItemStack(stack1, slotCount, 36 + slotCount, true)) {
				return ItemStack.EMPTY;
			}

			if (index >= slotCount && !this.mergeItemStack(stack1, 0, slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (stack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}

			else {
				slot.onSlotChanged();
			}
		}

        slot.onTake(player, stack);

		return stack;
	}
}
