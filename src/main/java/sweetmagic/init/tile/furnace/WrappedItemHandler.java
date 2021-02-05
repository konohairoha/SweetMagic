package sweetmagic.init.tile.furnace;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class WrappedItemHandler implements IItemHandlerModifiable {

	private final IItemHandlerModifiable compose;
	private final WriteMode mode;

	public WrappedItemHandler(IItemHandlerModifiable compose, WriteMode mode) {
		this.compose = compose;
		this.mode = mode;
	}

	@Override
	public int getSlots() {
		return compose.getSlots();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return compose.getStackInSlot(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (this.mode == WriteMode.IN || this.mode == WriteMode.IN_OUT)
			return this.compose.insertItem(slot, stack, simulate);
		else
			return stack;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (this.mode == WriteMode.OUT || this.mode == WriteMode.IN_OUT)
			return this.compose.extractItem(slot, amount, simulate);
		else
			return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return this.compose.getSlotLimit(slot);
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		this.compose.setStackInSlot(slot, stack);
	}

	public enum WriteMode {
		IN, OUT, IN_OUT, NONE
	}
}
