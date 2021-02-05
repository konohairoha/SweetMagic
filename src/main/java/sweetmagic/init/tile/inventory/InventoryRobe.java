package sweetmagic.init.tile.inventory;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.util.ItemHelper;

public class InventoryRobe implements IItemHandlerModifiable {

	public final IItemHandlerModifiable inventory;
	public final ItemStack invItem;

	public InventoryRobe(EntityPlayer player) {
		this.invItem = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		this.inventory = new ItemStackHandler(54);
		this.readFromNBT(ItemHelper.getNBT(this.invItem));
	}

	@Override
	public int getSlots() {
		return this.inventory.getSlots();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory.getStackInSlot(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		ItemStack ret = this.inventory.insertItem(slot, stack, simulate);
		this.writeBack();
		return ret;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack ret = this.inventory.extractItem(slot, amount, simulate);
		this.writeBack();
		return ret;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		this.inventory.setStackInSlot(slot, stack);
		this.writeBack();
	}

	public void writeBack() {
		for (int i = 0; i < 2; ++i) {
			if (this.inventory.getStackInSlot(i).isEmpty()) {
				this.inventory.setStackInSlot(i, ItemStack.EMPTY);
			}
		}

		this.writeToNBT(this.invItem.getTagCompound());
	}

	public void readFromNBT(NBTTagCompound nbt) {
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(this.inventory, null, nbt.getTagList("InventoryRobe", Constants.NBT.TAG_COMPOUND));
	}

	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("InventoryRobe", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(this.inventory, null));
	}
}
