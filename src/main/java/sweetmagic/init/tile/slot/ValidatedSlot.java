package sweetmagic.init.tile.slot;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ValidatedSlot extends SlotItemHandler {

	private final Predicate<ItemStack> validator;

	public ValidatedSlot(IItemHandler item, int index, int x, int y, Predicate<ItemStack> validator) {
		super(item, index, x, y);
		this.validator = validator;
	}

	public boolean isItemValid(@Nonnull ItemStack stack) {
		return super.isItemValid(stack) && this.validator.test(stack);
	}
}
