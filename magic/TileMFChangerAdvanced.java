package sweetmagic.init.tile.magic;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import sweetmagic.init.tile.inventory.InventoryWoodChest;

public class TileMFChangerAdvanced extends TileMFChanger {

	public int maxMagiaFlux = 200000;

	public final ItemStackHandler inputInventory = new InventoryWoodChest(this, this.getInvSize());	// MFアイテムのスロット

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("Input", this.inputInventory.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.inputInventory.deserializeNBT(tags.getCompoundTag("Input"));
	}

	// インベントリの数
	@Override
	public int getInvSize() {
		return 5;
	}

	// MFスロットの取得
	@Override
	public IItemHandler getInput() {
		return this.inputInventory;
	}

	// インプットスロットのアイテムを取得
	@Override
	public  ItemStack getInputItem(int i) {
		return this.getInput().getStackInSlot(i);
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 5000;
    }

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

	public IItemHandler side = new CombinedInvWrapper(this.inputInventory);
	public CombinedInvWrapper top = new CombinedInvWrapper(this.inputInventory);

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.top);
			} else {
				switch (side) {
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.side);
				}
			}
		}
		return super.getCapability(cap, side);
	}
}
