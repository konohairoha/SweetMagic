package sweetmagic.init.tile.chest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.init.block.blocks.BlockModenRack;
import sweetmagic.init.block.blocks.WandPedal;
import sweetmagic.init.tile.magic.TileSMBase;
import sweetmagic.init.tile.slot.StackHandler;

public class TileWandPedal extends TileSMBase {

	// スロット数
	public int getInvSize () {
		return 1;
	}

	// インベントリ
	public final ItemStackHandler chestInv = new StackHandler(this, this.getInvSize()) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

    public boolean isSlotEmpty () {
    	return this.getChestItem().isEmpty();
    }

	// インベントリの取得
	public IItemHandler getChest() {
		return this.chestInv;
	}

	// インベントリのアイテムを取得
	public  ItemStack getChestItem() {
		return this.getChest().getStackInSlot(0);
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

	// 向きの取得
	public EnumFacing getFace () {
		return this.getState(this.pos).getValue(BlockModenRack.FACING);
	}

	// データ値の取得
	public int getData() {
		return ((WandPedal) this.getBlock(this.pos)).data;
	}
}
