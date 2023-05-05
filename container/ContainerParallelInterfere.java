package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.tile.gui.GuiParallelInterfere.IScrollChanged;
import sweetmagic.init.tile.magic.TileParallelInterfere;
import sweetmagic.init.tile.slot.SlotScroll;

@ChestContainer(rowSize = 9)
public class ContainerParallelInterfere extends Container implements IScrollChanged {

	public final TileParallelInterfere tile;
	public int scrollPage = 0;

	public ContainerParallelInterfere(InventoryPlayer invPlayer, TileParallelInterfere tile) {
		this.tile = tile;
		this.initSlots(invPlayer);
		this.scrollPage = this.tile.getPage();
	}

	public void initSlots(InventoryPlayer invPlayer) {

		for(int y = 0; y < 6; y++) {
			for(int x = 0; x < 9; x++) {
				int index = x + y * 9;
				this.addSlotToContainer(new SlotScroll(this.tile.getChest(), index, 8 + x * 18, 18 + y * 18, s -> true, this));
			}
		}

		// Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 130 + i * 18));

		// Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 188));
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return this.tile.isUsableByPlayer(player);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) { }

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.getSlot(slotIndex);

		if (slot != null && slot.getHasStack()) {

			int slotCount = 54;
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (slotIndex < slotCount && !this.mergeItemStack(stack1, slotCount, 36 + slotCount, true)) {
				return ItemStack.EMPTY;
			}

			if (slotIndex >= slotCount && !this.mergeItemStack(stack1, 0, slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (stack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return stack;
	}

	@Override
	public void onScrollChanged(int page) {

		//ページ切り替えが発生していない場合は更新しない
		if (this.tile.getPage() == page) { return; }
		this.tile.setPage(page);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		this.tile.setPage(0);
	}
}
