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
import sweetmagic.init.tile.cook.TileFreezer;
import sweetmagic.init.tile.slot.CookedItemSlot;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

@ChestContainer(rowSize = 13)
public class ContainerFreezer extends Container {

	private final TileFreezer tile;

	public ContainerFreezer(InventoryPlayer invPlayer, TileFreezer tile) {
		this.tile = tile;
		this.initSlots(invPlayer);
	}

	public void initSlots(InventoryPlayer invPlayer) {

		// 上のブロックなら
		if (this.tile.isTop()) {
			this.slotTop(invPlayer);
		}

		// 下のブロックなら
		else {
			this.slotBottom(invPlayer);
		}
	}

	// 上のブロックのスロット
	public void slotTop (InventoryPlayer invPlayer) {

		// 水スロット
		this.addSlotToContainer(new ValidatedSlot(this.tile.getWater(), 0, 8, 78, SlotPredicates.WATERCUP));

		// 氷スロット
		for (int i = 0; i < 2; i++)
			this.addSlotToContainer(new ValidatedSlot(this.tile.getIce(), i, 8, 6 + 18 * i, s -> false));

		// ハンドスロット
		this.addSlotToContainer(new ValidatedSlot(this.tile.getHand(), 0, 62, 11, SlotPredicates.ALLITEM));

		// 入力スロット
		for (int i = 0; i < 2; i++)
			for (int k = 0; k < 3; k++)
				this.addSlotToContainer(new ValidatedSlot(this.tile.getInput(), k + i * 3, 53 + 18 * i, 44 + 18 * k, SlotPredicates.ALLITEM));

		// 出力スロット
		for (int i = 0; i < 4; i++)
			this.addSlotToContainer(new CookedItemSlot(this.tile.getOutput(), i, 134, 8 + 18* i, s -> false));

		// Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 103 + i * 18));

		// Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 161));
	}

	// 下のブロックのスロット
	public void slotBottom (InventoryPlayer invPlayer) {

		// 入力スロット
		for (int i = 0; i < 8; i++)
			for (int k = 0; k < 13; k++)
				this.addSlotToContainer(new ValidatedSlot(this.tile.getChest(), k + i * 13, 12 + 18 * k, 5 + 18 * i, s -> true));

		// Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 48 + j * 18, 152 + i * 18));

		// Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 48 + i * 18, 210));
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return this.tile.isUsableByPlayer(player);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) { }

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.getSlot(slotIndex);
		boolean isTop = this.tile.isTop();
		int slotCount = isTop ? 14 : 104;
		int startIndex = isTop ? 1 : 0;

		if (slot != null && slot.getHasStack()) {

			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (slotIndex < slotCount && !this.mergeItemStack(stack1, slotCount, 36 + slotCount, !isTop)) {
				return ItemStack.EMPTY;
			}

			if (slotIndex >= slotCount && !this.mergeItemStack(stack1, startIndex, slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (stack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}

			else {
				slot.onSlotChanged();
			}
		}
		return stack;
	}
}
