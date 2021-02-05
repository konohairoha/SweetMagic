package sweetmagic.init.tile.inventory;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.api.iitem.IWand;
import sweetmagic.util.ItemHelper;

public class InventorySMWand implements IItemHandlerModifiable {

	public final ItemStack stack;
	public final IItemHandlerModifiable inventory;

	public InventorySMWand(ItemStack stack, EntityPlayer player) {
		this.stack = stack;
		IWand wand = (IWand) this.stack.getItem();
		this.inventory = new ItemStackHandler(wand.getSlot(stack));
		this.readFromNBT(ItemHelper.getTag(stack));
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
		for (int i = 0; i < this.inventory.getSlots(); ++i) {
			if (this.inventory.getStackInSlot(i).isEmpty()) {
				this.inventory.setStackInSlot(i, ItemStack.EMPTY);
			}
		}

		this.writeToNBT(this.stack.getTagCompound());
	}

	public void readFromNBT(NBTTagCompound nbt) {
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(this.inventory, null, nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND));
	}

	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("Items", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(this.inventory, null));
	}
}
