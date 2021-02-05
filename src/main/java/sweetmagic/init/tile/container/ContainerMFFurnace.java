package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import sweetmagic.init.item.sm.magic.MFItem;
import sweetmagic.init.tile.furnace.TileMFF;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerMFFurnace extends Container {

	final TileMFF tile;
	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;

	public ContainerMFFurnace(InventoryPlayer invPlayer, TileMFF tile) {
		this.tile = tile;
		this.initSlots(invPlayer);
	}

	void initSlots(InventoryPlayer invPlayer) {

		IItemHandler fuel = this.tile.getFuel();
		IItemHandler input = this.tile.getInput();
		IItemHandler output = this.tile.getOutput();

		//Fuel
		this.addSlotToContainer(new ValidatedSlot(fuel, 0, 65, 53, SlotPredicates.MFF_FUEL));

		//Input(0)
		this.addSlotToContainer(new ValidatedSlot(input, 0, 65, 17, SlotPredicates.SMELTABLE));

		int counter = input.getSlots() - 1;

		//Input storage
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 4; j++)
				this.addSlotToContainer(
						new ValidatedSlot(input, counter--, 11 + i * 18, 8 + j * 18, SlotPredicates.SMELTABLE));

		counter = output.getSlots() - 1;

		//Output(0)
		this.addSlotToContainer(new ValidatedSlot(output, counter--, 125, 35, s -> false));

		//Output Storage
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 4; j++)
				this.addSlotToContainer(new ValidatedSlot(output, counter--, 165 + i * 18, 8 + j * 18, s -> false));

		//Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 24 + j * 18, 84 + i * 18));

		//Player HotBar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 24 + i * 18, 142));
	}


	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return true;
	}

	@Override
	public void addListener(IContainerListener container) {
		super.addListener(container);
		container.sendWindowProperty(this, 0, this.tile.furnaceCookTime);
		container.sendWindowProperty(this, 1, this.tile.getMF());
		container.sendWindowProperty(this, 2, this.tile.currentItemBurnTime);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (IContainerListener crafter : this.listeners) {
			if (this.lastCookTime != this.tile.furnaceCookTime)
				crafter.sendWindowProperty(this, 0, this.tile.furnaceCookTime);

			if (this.lastBurnTime != this.tile.getMF())
				crafter.sendWindowProperty(this, 1, tile.getMF());

			if (this.lastItemBurnTime != this.tile.currentItemBurnTime)
				crafter.sendWindowProperty(this, 2, this.tile.currentItemBurnTime);
		}

		this.lastCookTime = this.tile.furnaceCookTime;
		this.lastBurnTime = this.tile.getMF();
		this.lastItemBurnTime = this.tile.currentItemBurnTime;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0)
			this.tile.furnaceCookTime = par2;

		if (par1 == 1)
			this.tile.setMF(par2);

		if (par1 == 2)
			this.tile.currentItemBurnTime = par2;
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

		if (slotIndex <= 18) {
			if (!this.mergeItemStack(stack, 19, 55, false)) {
				return ItemStack.EMPTY;
			}
		} else {

			if (TileEntityFurnace.isItemFuel(newStack) || newStack.getItem() instanceof MFItem) {
				if (!this.mergeItemStack(stack, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
				// かまどレシピか鉄どうか
				} else if (!FurnaceRecipes.instance().getSmeltingResult(newStack).isEmpty()) {
				if (!this.mergeItemStack(stack, 1, 10, false)) {
					return ItemStack.EMPTY;
				}
			} else {
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
}
