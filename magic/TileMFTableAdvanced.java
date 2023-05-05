package sweetmagic.init.tile.magic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.init.tile.inventory.InventoryWoodChest;

public class TileMFTableAdvanced extends TileMFTable {

	public int maxMagiaFlux = 400000; 	// 最大MF量を設定
	public int useMF = 20000; 			// 一度に補給できるMF;
	public int slotSize = 4;

	// 杖スロット
	public final ItemStackHandler wandInventory = new InventoryWoodChest(this, this.getInvSize()) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 5000;
    }

	// 杖スロットの取得
	public IItemHandler getWand() {
		return this.wandInventory;
	}

	// 杖スロットのアイテムを取得
	public  ItemStack getWandItem(int i) {
		return this.getWand().getStackInSlot(i);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("wand", this.wandInventory.serializeNBT());
		tags.setTag("Input", this.inputInventory.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.wandInventory.deserializeNBT(tags.getCompoundTag("wand"));
		this.inputInventory.deserializeNBT(tags.getCompoundTag("Input"));
	}

	// 杖に入れるMF量の取得
	public int getChargeValue () {
		return this.useMF;
	}

	// インベントリの数
	public int getInvSize() {
		return this.slotSize;
	}
}
