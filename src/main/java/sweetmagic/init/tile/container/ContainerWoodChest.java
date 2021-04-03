package sweetmagic.init.tile.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.block.blocks.BlockWoodChest;
import sweetmagic.init.tile.chest.TileWoodChest;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerWoodChest extends Container {

	final TileWoodChest tile;

	public ContainerWoodChest(InventoryPlayer invPlayer, TileWoodChest tile) {
		this.tile = tile;
		this.initSlots(invPlayer);
	}

	void initSlots(InventoryPlayer invPlayer) {

		for (int i = 0; i < 8; i++)
			for (int k = 0; k < 13; k++)
				this.addSlotToContainer(new ValidatedSlot(this.tile.getChest(), k + i * 13, 12 + k * 18, 5 + i * 18, s -> true));

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
		return this.tile.isNotAir();
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.getSlot(slotIndex);
		int slotCount = this.tile.getInvSize();

		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (slotIndex < slotCount && !this.mergeItemStack(stack1, slotCount, 36 + slotCount, false)) {
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
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);

		BlockWoodChest block = (BlockWoodChest) this.tile.getBlock(this.tile.getPos());

		switch (block.data) {
		case 0:
			player.playSound(SoundEvents.BLOCK_PISTON_CONTRACT, 0.5F, player.world.rand.nextFloat() * 0.1F + 0.9F);
			break;
		case 1:
			player.world.playEvent(player, 1011, new BlockPos(player), 0);
			break;
		}
	}
}
