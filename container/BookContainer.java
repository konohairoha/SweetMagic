package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BookContainer extends Container {

	private final InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	private final InventoryCraftResult craftResult = new InventoryCraftResult();
	private final World worldObj;
	private final EntityPlayer player;

	public BookContainer(InventoryPlayer invPlayer) {

		this.player = invPlayer.player;
		this.worldObj = player.getEntityWorld();

		//テクスチャの影響ですべての座標が＋10
		this.addSlotToContainer(new SlotCrafting(invPlayer.player, this.craftMatrix, this.craftResult, 0, 124, 64));

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 42 + j * 18, 46 + i * 18));

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));

		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 198));

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inv) {
		this.slotChangedCraftingGrid(this.player.world, this.player, this.craftMatrix, this.craftResult);
	}

	public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (!this.worldObj.isRemote) {
            this.clearContainer(player, this.worldObj, this.craftMatrix);
        }
    }

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return true;
	}

	public ItemStack transferStackInSlot(EntityPlayer player, int index) {

        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {

            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if (index == 0) {
                stack1.getItem().onCreated(stack1, this.worldObj, player);
                if (!this.mergeItemStack(stack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack1, stack);
            }

            else if (index >= 10 && index < 37) {
                if (!this.mergeItemStack(stack1, 37, 46, false)) {
                    return ItemStack.EMPTY;
                }
            }

            else if (index >= 37 && index < 46) {
                if (!this.mergeItemStack(stack1, 10, 37, false)) {
                    return ItemStack.EMPTY;
                }
            }

            else if (!this.mergeItemStack(stack1, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }

            else {
                slot.onSlotChanged();
            }

            if (stack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            if (index == 0) {
                player.dropItem(slot.onTake(player, stack1), false);
            }
        }
        return stack;
    }

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slot){
		return slot.inventory != this.craftResult && super.canMergeSlot(stack, slot);
	}
}
