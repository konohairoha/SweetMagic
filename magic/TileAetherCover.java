package sweetmagic.init.tile.magic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.init.tile.inventory.InventoryWoodChest;

public class TileAetherCover extends TileSMBase {

	public boolean findPlayer = false;
	public int renderTime = 0;

	// スロット数
	public int getInvSize () {
		return 1;
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

	public void clientUpdate () {

		this.renderTime++;
		if (this.renderTime % 30 != 0) { return; }

		this.checkRangePlayer();
	}

	public void checkRangePlayer () {
		this.findPlayer = this.findRangePlayer(48D, 24D, 48D);
	}

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

	public ItemStack getDefaultStack () {
		return new ItemStack(this.getBlock(this.pos));
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
}
