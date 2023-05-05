package sweetmagic.init.tile.inventory;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import sweetmagic.init.tile.chest.TileMagiaStorage;

public class InventoryMagiaStorage extends InventoryWoodChest {

	private final TileMagiaStorage tile;

	public InventoryMagiaStorage(TileMagiaStorage tile, int size) {
		super(tile, size);
		this.tile = tile;
	}

	@Override
	public int getSlotLimit(int slot) {
		int tier = this.tile.getTier();
		int limit = 256;

		if (tier == 5) {
			limit = Integer.MAX_VALUE;
		}

		else if (tier != 1) {
			limit *= (int) Math.pow(2, tier);
		}

		return limit;
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

		if (stack.isEmpty()) { return stack; }

        ItemStack slotStack = this.stacks.get(slot);

        if(slotStack.isEmpty()) {
            if (!simulate) {
                this.stacks.set(slot, stack.copy());
            }
            return ItemStack.EMPTY;
        }

        if (!ItemHandlerHelper.canItemStacksStack(stack, slotStack)) {
           return stack;
        }

        int newStackCount = Math.min(slotStack.getCount() + stack.getCount(), this.getSlotLimit(slot));
        int returnCount = slotStack.getCount() + stack.getCount() - newStackCount;

        if (!simulate) {
        	slotStack.setCount(newStackCount);
        }

        if (returnCount == 0) {
            return ItemStack.EMPTY;
        }

        ItemStack returnStack = stack.copy();
        returnStack.setCount(returnCount);

        return returnStack;
	}
}
