package sweetmagic.init.tile.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.init.block.blocks.ChoppingBoard;

public class ContainerChoppingBoard extends Container {

	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public InventoryCraftResult craftResult = new InventoryCraftResult();
	private final World world;
	private final BlockPos pos;
	private final EntityPlayer player;

	public ContainerChoppingBoard(InventoryPlayer pInv, World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
		this.player = pInv.player;
		this.addSlotToContainer(new SlotCrafting(pInv.player, this.craftMatrix, this.craftResult, 0, 124, 35));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
			}
		}

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlotToContainer(new Slot(pInv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlotToContainer(new Slot(pInv, l, 8 + l * 18, 142));
		}
	}

	public void onCraftMatrixChanged(IInventory inv) {
		this.slotChangedCraftingGrid(this.world, this.player, this.craftMatrix, this.craftResult);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);

		if (!this.world.isRemote) {
			this.clearContainer(player, this.world, this.craftMatrix);
		}
		player.playSound(SoundEvents.BLOCK_PISTON_CONTRACT, 0.5F, player.world.rand.nextFloat() * 0.1F + 0.9F);
	}

	public boolean canInteractWith(EntityPlayer player) {

		if (!(this.world.getBlockState(this.pos).getBlock() instanceof ChoppingBoard)) {
			return false;
		}

		else {
			return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64D;
		}
	}

	public ItemStack transferStackInSlot(EntityPlayer player, int index) {

		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {

			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (index == 0) {
				stack1.getItem().onCreated(stack1, this.world, player);

				if (!this.mergeItemStack(stack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(stack1, stack);
			}

			else if (index >= 10 && index < 37) {
				if (!this.mergeItemStack(stack1, 37, 46, false)) {
					return ItemStack.EMPTY;
				}
			}

			else if (index >= 37 && index < 46) {
				if (!this.mergeItemStack(stack1, 10, 37, false)) {
					return ItemStack.EMPTY;
				}
			}

			else if (!this.mergeItemStack(stack1, 10, 46, false)) {
				return ItemStack.EMPTY;
			}

			if (stack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}

			else {
				slot.onSlotChanged();
			}

			if (stack1.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack stack2 = slot.onTake(player, stack1);

			if (index == 0) {
				player.dropItem(stack2, false);
			}
		}

		return stack;
	}

	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
	}
}
