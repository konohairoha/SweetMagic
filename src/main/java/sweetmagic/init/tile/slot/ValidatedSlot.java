package sweetmagic.init.tile.slot;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ValidatedSlot extends SlotItemHandler {

	private final Predicate<ItemStack> validator;

	public ValidatedSlot(IItemHandler item, int index, int xPosition, int yPosition, Predicate<ItemStack> validator) {
		super(item, index, xPosition, yPosition);
		this.validator = validator;
	}

	public boolean isItemValid(@Nonnull ItemStack stack) {
		return super.isItemValid(stack) && this.validator.test(stack);
	}
}
