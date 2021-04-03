package sweetmagic.init.tile.chest;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileModenWallRack extends TileModenRack {

	// スロット数
	public int getInvSize () {
		return 3;
	}

	// インベントリ
	public ItemStackHandler chestInv = new StackHandler(this, this.getInvSize()) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

	private final IItemHandlerModifiable output = new WrappedItemHandler(this.chestInv, WrappedItemHandler.WriteMode.OUT);
	private final IItemHandler side = new CombinedInvWrapper(this.chestInv);
	private final CombinedInvWrapper join = new CombinedInvWrapper(this.chestInv);

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.join);
			} else {
				switch (side) {
				case DOWN:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.output);
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.side);
				}
			}
		}

		return super.getCapability(cap, side);
	}

	// インベントリの取得
	@Override
	public IItemHandler getChest() {
		return this.chestInv;
	}

	// インベントリのアイテムを取得
	@Override
	public  ItemStack getChestItem(int i) {
		return this.getChest().getStackInSlot(i);
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
