package sweetmagic.init.tile.inventory;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.util.ItemHelper;

public class InventoryPouch implements IItemHandlerModifiable {

	public final IItemHandlerModifiable inventory;
	public final ItemStack invItem;
	public final int slotSize;
	private final IPouch porch;
	private final EntityPlayer player;
	private List<ItemStack> stackList = new ArrayList<>();

	public InventoryPouch(EntityPlayer player) {
		this.player = player;
		this.invItem = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		this.porch = (IPouch) this.invItem.getItem();
		this.slotSize = this.porch.getSlotSize();
		this.inventory = new ItemStackHandler(this.slotSize);
		this.readFromNBT(ItemHelper.getNBT(this.invItem));
	}

	@Override
	public int getSlots() {
		return this.inventory.getSlots();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory.getStackInSlot(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		ItemStack ret = this.inventory.insertItem(slot, stack, simulate);
		this.writeBack();
		return ret;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack ret = this.inventory.extractItem(slot, amount, simulate);
		this.writeBack();

		if (ret.getItem() instanceof IAcce) {
			IAcce acc = (IAcce) ret.getItem();
			acc.extractPorch(this.player);
		}

		return ret;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		this.inventory.setStackInSlot(slot, stack);
		this.writeBack();
	}

	public void writeBack() {
		for (int i = 0; i < 2; ++i) {
			if (this.inventory.getStackInSlot(i).isEmpty()) {
				this.inventory.setStackInSlot(i, ItemStack.EMPTY);
			}
		}

		// Listの初期化
		this.setStackList();
		this.writeToNBT(this.invItem.getTagCompound());
	}

	public void readFromNBT(NBTTagCompound nbt) {
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(this.inventory, null, nbt.getTagList("InventoryPouch", Constants.NBT.TAG_COMPOUND));
	}

	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("InventoryPouch", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(this.inventory, null));
	}

	// インベントリに入れてるアイテムをList化
	public List<ItemStack> getStackList() {
		return this.setStackList();
	}

	// Listの初期化
	public List<ItemStack> setStackList () {

		this.stackList.clear();

		for (int i = 0; i < this.inventory.getSlots(); i++) {

			// アイテムを取得し空かアクセサリー以外なら次へ
			ItemStack st = this.inventory.getStackInSlot(i);
			if (st.isEmpty() || !(st.getItem() instanceof IAcce)) { continue; }

			this.stackList.add(st);
		}

		return this.stackList;
	}
}
