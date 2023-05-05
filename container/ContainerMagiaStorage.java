package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.tile.chest.TileMagiaStorage;
import sweetmagic.init.tile.inventory.InventoryMagiaStorage;
import sweetmagic.init.tile.slot.ValidatedSlot;
import sweetmagic.packet.PacketUpdateSlot;

public class ContainerMagiaStorage extends Container {

	private final TileMagiaStorage tile;
	private final InventoryMagiaStorage inventory;
	private int tickTime = 0;

	public ContainerMagiaStorage(InventoryPlayer invPlayer, TileMagiaStorage tile) {
		this.tile = tile;
		this.inventory = tile.chestInv;
		this.initSlots(invPlayer);
	}

	private void initSlots(InventoryPlayer invPlayer) {

		for (int i = 0; i < 8; i++)
			for (int k = 0; k < 13; k++)
				this.addSlotToContainer(new ValidatedSlot(this.tile.getChest(), k + i * 13, 5 + k * 18, 13 + i * 18, s -> true));

		// Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 41 + j * 18, 159 + i * 18));

		// Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 41 + i * 18, 218));
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return this.tile.isUsableByPlayer(player);
	}

	@Override
	public boolean canDragIntoSlot(Slot slot) {
		return true;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {

        Slot clickSlot = this.inventorySlots.get(index);

        if (clickSlot.getHasStack()) {

            if (index > 103 && index < 140) {
                clickSlot.putStack(this.addStack(clickSlot.getStack()));
                player.inventory.markDirty();
                return ItemStack.EMPTY;
            }

            else {

                int slotIndex = clickSlot.getSlotIndex();
                ItemStack slotStack = this.inventory.extractItem(slotIndex, this.inventory.getStackInSlot(slotIndex).getMaxStackSize(), true);

                IItemHandler playerHandler = new PlayerMainInvWrapper(player.inventory);
                ItemStack notAdded = ItemHandlerHelper.insertItemStacked(playerHandler, slotStack, false);
                if (notAdded.getCount() < slotStack.getCount()) {
                    this.inventory.extractItem(slotIndex, slotStack.getCount() - notAdded.getCount(), false);
                    clickSlot.onSlotChanged();
                }
            }
        }
        return ItemStack.EMPTY;

	}

    @Override
    public ItemStack slotClick(int index, int dragType, ClickType clickType, EntityPlayer player) {

//    	System.out.println("=======" + dragType);

        if (index >= 0 && index < 140 && this.tickTime + 3 >= player.ticksExisted && dragType <= 1 && clickType == ClickType.PICKUP ) {
        	return ItemStack.EMPTY;
        }

        Slot slot = this.getSlot(index);
		this.tickTime = player.ticksExisted;

        if (slot == null || index >= 104 && clickType != ClickType.QUICK_MOVE || clickType == ClickType.CLONE) {
            return super.slotClick(index, dragType, clickType, player);
        }

        if (clickType == ClickType.QUICK_MOVE) {
            return transferStackInSlot(player, index);
        }

        InventoryPlayer invPlayer = player.inventory;
        ItemStack heldStack = invPlayer.getItemStack();

        if (index <= 103 && clickType == ClickType.PICKUP) {

            ItemStack slotStack = slot.getStack();
            if (!slotStack.isEmpty()) {

            	if (heldStack.isEmpty()) {
            		ItemStack returnStack = slotStack.copy();
            		returnStack.setCount(Math.min(returnStack.getCount(), 64));
            		slotStack.shrink(returnStack.getCount());
            		invPlayer.setItemStack(returnStack);

    				if (player instanceof EntityPlayerMP) {
    					((EntityPlayerMP) player).updateHeldItem();
                    }
            	}

            	return ItemStack.EMPTY;
            }

            if (!heldStack.isEmpty()) {

                ItemStack toAdd = heldStack.copy();

                if (dragType == 1) {
                    toAdd.setCount(1);
                }

                ItemStack leftover = this.addStack(toAdd);

                if (dragType == 0) {
                	invPlayer.setItemStack(!leftover.isEmpty() ? leftover : ItemStack.EMPTY);

                }

                else if (dragType == 1 && leftover.isEmpty()) {
                    heldStack.shrink(1);
                    invPlayer.setItemStack(heldStack);
                }

				if (player instanceof EntityPlayerMP) {
					((EntityPlayerMP) player).updateHeldItem();
                }

            }

            else if (!slotStack.isEmpty()) {

                int amount = Math.min(slotStack.getCount(), slotStack.getMaxStackSize());
                if (dragType == 1) {
                    amount = Math.floorDiv(amount, 2);
                }

                ItemStack newStack = slot.decrStackSize(amount);
                invPlayer.setItemStack(newStack);

                if (player instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) player).updateHeldItem();
                }
            }
        }

        return ItemStack.EMPTY;
    }

    private ItemStack addStack(ItemStack stack) {

		ItemStack leftover = stack.copy();
		for (int i = 0; i < this.inventory.getSlots(); i++) {
			if (this.inventory.isItemValid(i, leftover)) {
				leftover = this.inventory.insertItem(i, leftover, false);
			}
		}

		for (int i = 0; i < this.inventory.getSlots(); i++) {
			if (this.inventory.getStackInSlot(i).isEmpty() && this.inventory.isItemValid(i, leftover)) {
				this.inventory.setStackInSlot(i, leftover);
				return ItemStack.EMPTY;
			}
		}

        return leftover;
    }

    @Override
    public Slot getSlot(final int slotId) {
        if (slotId < inventorySlots.size() && slotId >= 0) {
            return inventorySlots.get(slotId);
        }
        return null;
    }

    @Override
    public void detectAndSendChanges() {

        for (int i = 0; i < this.inventorySlots.size(); ++i) {

            ItemStack slotStack = this.inventorySlots.get(i).getStack();
            ItemStack clientStack = this.inventoryItemStacks.get(i);
            if (ItemStack.areItemStacksEqual(clientStack, slotStack)) { continue; }

            if (ItemStack.areItemStacksEqualUsingNBTShareTag(clientStack, slotStack)) { continue; }

            clientStack = slotStack.isEmpty() ? ItemStack.EMPTY : slotStack.copy();
            this.inventoryItemStacks.set(i, clientStack);
            for (final IContainerListener listener : this.listeners) {
                if (listener instanceof EntityPlayerMP) {
            		PacketHandler.sendToPlayer(new PacketUpdateSlot(i, clientStack), (EntityPlayerMP) listener);
                }
            }
        }
    }
}
