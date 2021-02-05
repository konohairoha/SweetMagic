package sweetmagic.init.tile.magic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.init.tile.slot.StackHandler;

public class TileMFTableAdvanced extends TileMFTable {

	public int maxMagiaFlux = 100000; 	// 最大MF量を設定
	public int useMF = 5000; 			// 一度に補給できるMF;
	public int slotSize = 4;

	// 杖スロット
	public final ItemStackHandler wandInventory = new StackHandler(this, 4) {

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
		tags.setInteger("magiaFlux", this.magiaFlux);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.wandInventory.deserializeNBT(tags.getCompoundTag("wand"));
		this.inputInventory.deserializeNBT(tags.getCompoundTag("Input"));
		this.setMF(tags.getInteger("magiaFlux"));
	}

}
