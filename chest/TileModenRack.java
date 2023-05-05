package sweetmagic.init.tile.chest;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.init.block.blocks.BlockModenRack;
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.init.tile.magic.TileSMBase;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileModenRack extends TileSMBase {

	public boolean findPlayer = false;
	public int renderTime = 0;

	// スロット数
	public int getInvSize () {
		return 18;
	}

	public void clientUpdate () {

		this.renderTime++;
		if (this.renderTime % this.checkTime() != 0) { return; }

		this.checkRangePlayer();
	}

	public void checkRangePlayer () {
		if (!this.isSever()) {
			this.findPlayer = this.findRangePlayer(24D, 12D, 24D);
		}
	}

	public int checkTime () {
		return 30;
	}

	// インベントリ
	public final ItemStackHandler chestInv = new InventoryWoodChest(this, this.getInvSize()) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

    // スロットが空かどうか
    public ItemStackHandler getSlot () {
    	return this.chestInv;
    }

    public boolean isSlotEmpty () {

    	for (int i = 0; i < this.getInvSize(); i++) {
    		ItemStack stack = this.getChestItem(i);
    		if (!stack.isEmpty()) { return false; }
    	}

    	return true;
    }

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

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("chestInv", this.chestInv.serializeNBT());
		tags.setBoolean("findPlayer", this.findPlayer);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.chestInv.deserializeNBT(tags.getCompoundTag("chestInv"));
		this.findPlayer = tags.getBoolean("findPlayer");
	}

	// モダンラックシリーズかどうか
	public boolean isRack () {
		return this.getRackData() == 0;
	}

	public int getRackData () {
		Block block = this.getBlock(this.getPos());
		return ((BlockModenRack) block).data;
	}
}
