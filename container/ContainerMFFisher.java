package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.magic.MFItem;
import sweetmagic.init.tile.magic.TileMFFisher;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerMFFisher extends Container {

	private final TileMFFisher tile;
	private final int data;

	public ContainerMFFisher(InventoryPlayer invPlayer, TileMFFisher tile) {
		this.tile = tile;
		this.data = this.tile.getData();
		this.initSlots(invPlayer);
	}

	void initSlots(InventoryPlayer invPlayer) {

		IItemHandler fuel = this.tile.getFuel();
		IItemHandler output = this.tile.getOutput();
		boolean isGen = this.data == 3;

		int addY = isGen ? -40 : 0;
		int addX = isGen ? -29 : 0;

		//Fuel
		this.addSlotToContainer(new ValidatedSlot(fuel, 0, 93 + (isGen ? -47 : 0), 31 + (isGen ? -1 : 0), isGen ? SlotPredicates.ISBUCKET : SlotPredicates.MFF_FUEL));

		if (!isGen) {

			//Output Storage
			for (int i = 0; i < 2; i++)
				for (int k = 0; k < 9; k++)
					this.addSlotToContainer(new ValidatedSlot(output, k + i * 9, 8 + 18 * k, 96 + 18 * i, s -> false));
		}

		else {
			this.addSlotToContainer(new ValidatedSlot(output, 0, 140 + addX, 31 + (isGen ? -2 : 0), s -> false));
		}

		// Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 136 + i * 18 + addY));

		// Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 194 + addY));
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return this.tile.isUsableByPlayer(player);
	}

	@Override
	public void addListener(IContainerListener container) {
		super.addListener(container);
		container.sendWindowProperty(this, 1, this.tile.getMF());
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 1)
			this.tile.setMF(par2);
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

		Slot slot = this.getSlot(slotIndex);

		if (slot == null || !slot.getHasStack()) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getStack();
		ItemStack newStack = stack.copy();
		int slotSize = this.tile.getInvSize();

		if (slotIndex <= slotSize) {
			if (!this.mergeItemStack(stack, 1 + slotSize, 37 + slotSize, false)) {
				return ItemStack.EMPTY;
			}
		}

		else {

			if (this.checkItem(newStack)) {
				if (!this.mergeItemStack(stack, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			}

			else {
				return ItemStack.EMPTY;
			}
		}

		if (stack.isEmpty()) {
			slot.putStack(ItemStack.EMPTY);
		} else {
			slot.onSlotChanged();
		}

		return newStack;
	}

	public boolean checkItem (ItemStack stack) {

		if (this.data != 3) {
			return TileEntityFurnace.isItemFuel(stack) || stack.getItem() instanceof MFItem;
		}

		else {
			Item item = stack.getItem();
			return item == Items.BUCKET || item == ItemInit.alt_bucket || item == ItemInit.alt_bucket_lava;
		}
	}
}
