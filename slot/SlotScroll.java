package sweetmagic.init.tile.slot;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import sweetmagic.init.tile.container.ContainerParallelInterfere;

public class SlotScroll extends ValidatedSlot {

	public ContainerParallelInterfere container;
	public int index;

	public SlotScroll (IItemHandler item, int index, int x, int y, Predicate<ItemStack> validator, ContainerParallelInterfere container) {
		super(item, index, x, y, validator);
		this.container = container;
		this.index = index;
	}

	// アイテムを取れるかのチェック
	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return !this.getItemHandler().extractItem(this.index + (this.container.tile.getPage() * 9), 1, true).isEmpty();
	}

	// アイテムを取る処理
	@Nonnull
	public ItemStack decrStackSize(int amount) {
        return this.getItemHandler().extractItem(index + (this.container.tile.getPage() * 9), amount, false);
    }

	// アイテムを置く処理
	@Override
	public void putStack(@Nonnull ItemStack stack) {
		((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(this.index + (this.container.tile.getPage() * 9), stack);
		this.onSlotChanged();
	}

	// 特定スロットの描画とかいろいろなためのメソッド
	@Override
	public ItemStack getStack() {
		return this.getItemHandler().getStackInSlot(this.index + (this.container.tile.getPage() * 9));
	}
}
