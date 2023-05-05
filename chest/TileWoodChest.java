package sweetmagic.init.tile.chest;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.init.tile.magic.TileSMBase;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileWoodChest extends TileSMBase {

	// スロット数
	public int getInvSize () {
		return 104;
	}

	// インベントリ
	public final InventoryWoodChest chestInv = new InventoryWoodChest(this, this.getInvSize());
	private final IItemHandlerModifiable output = new WrappedItemHandler(this.chestInv, WrappedItemHandler.WriteMode.IN_OUT);

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.output);
		}

		return super.getCapability(cap, side);
	}

	// インベントリの取得
	public IItemHandler getChest() {
		return this.chestInv;
	}

	// インベントリのアイテムを取得
	public  ItemStack getChestItem(int i) {
		return this.getChest().getStackInSlot(i);
	}

    public boolean isSlotEmpty () {

    	for (int i = 0; i < this.getInvSize(); i++) {
    		ItemStack stack = this.getChestItem(i);
    		if (!stack.isEmpty()) { return false; }
    	}

    	return true;
    }

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("chestInv", this.chestInv.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.chestInv.deserializeNBT(tags.getCompoundTag("chestInv"));
	}
}

