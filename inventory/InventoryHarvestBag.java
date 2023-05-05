package sweetmagic.init.tile.inventory;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.util.ItemHelper;

public class InventoryHarvestBag implements IItemHandlerModifiable {

	public final IItemHandlerModifiable inventory;
	public final ItemStack invItem;

	public InventoryHarvestBag(EntityPlayer player, ItemStack stack) {
		this.invItem = stack;
		this.inventory = new ItemStackHandler(104);
		this.readFromNBT(ItemHelper.getNBT(stack));
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

		if (stack.isEmpty()) { return stack; }

        ItemStack slotStack = this.inventory.getStackInSlot(slot);

        if(slotStack.isEmpty()) {
            if (!simulate) {
                this.inventory.setStackInSlot(slot, stack.copy());
        		this.writeBack();
            }
            return ItemStack.EMPTY;
        }

        if (!ItemHandlerHelper.canItemStacksStack(stack, slotStack)) {
    		this.writeBack();
           return stack;
        }

        int newStackCount = Math.min(slotStack.getCount() + stack.getCount(), Integer.MAX_VALUE);
        int returnCount = slotStack.getCount() + stack.getCount() - newStackCount;

        if (!simulate) {
        	slotStack.setCount(newStackCount);
        }

        if (returnCount == 0) {
    		this.writeBack();
            return ItemStack.EMPTY;
        }

        ItemStack returnStack = stack.copy();
        returnStack.setCount(returnCount);
		this.writeBack();

        return returnStack;
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
		return Integer.MAX_VALUE;
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		this.inventory.setStackInSlot(slot, stack);
		this.writeBack();
	}

	public void writeBack() {
		this.writeToNBT(this.invItem.getTagCompound());
	}

	public void readFromNBT(NBTTagCompound tags) {

		// インベントリ差し替えによる古いNBTを初回のみ読み込み
		if (tags.hasKey("InventoryHarvestBag")) {
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(this.inventory, null, tags.getTagList("InventoryHarvestBag", Constants.NBT.TAG_COMPOUND));
			tags.removeTag("InventoryHarvestBag");
			return;
		}

		NBTTagList nbtList = tags.getTagList("InventoryHarvestBagNeo", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < nbtList.tagCount(); ++i) {
			NBTTagCompound nbt = nbtList.getCompoundTagAt(i);
			ItemStack stack = new ItemStack(nbt);
			stack.setCount(nbt.getInteger("ICount"));
			int slot = nbt.getInteger("Slot");
			this.inventory.setStackInSlot(slot, stack);
		}
	}

	public void writeToNBT(NBTTagCompound tags) {

		// nbtのリストを作成
		NBTTagList nbtList = new NBTTagList();
		for (int i = 0; i < this.getSlots(); i++) {

			// アイテムスタックごとに保存
            NBTTagCompound nbt = new NBTTagCompound();
			ItemStack stack = this.inventory.getStackInSlot(i);
            stack.writeToNBT(nbt);
            nbt.setInteger("ICount", stack.getCount());
            nbt.setInteger("Slot", i);
            nbtList.appendTag(nbt);
		}

		// アイテムスタック
		if (!nbtList.hasNoTags()) {
			tags.setTag("InventoryHarvestBagNeo", nbtList);
		}
	}
}
