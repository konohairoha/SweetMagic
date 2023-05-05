package sweetmagic.init.tile.magic;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileGravityChest extends TileMFBase {

	public int maxMagiaFlux = 100000;	// 最大MF量を設定
	public int range = 5;				// 半径

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

	@Override
	public void serverUpdate() {

		super.serverUpdate();
		if (this.getTime() % 30 != 0 || !this.isActive(this.pos)) { return; }

		// MF量が最大に足してなかったら動かす
		if (!this.isMfEmpty()) {
			this.suctionItem();
		}
	}

	// アイテム吸い込み
	public void suctionItem () {

		int useMF = this.range * 5;
		if (this.getMF() < useMF) { return; }

		// 範囲のえんちちーを取得
		int range = this.range;
        AxisAlignedBB aabb = this.getAABB(this.pos.add(-range, 0, -range), this.pos.add(range, range, range));
		List<EntityItem> entityList = this.getEntityList(EntityItem.class, aabb);
		if (entityList.isEmpty()) { return; }

		this.setMF(this.getMF() - useMF);
		this.sentClient();

		// えんちちー分回す
		for (EntityItem entity : entityList) {

			ItemStack entityStack = entity.getItem();
			ItemStack stack = ItemHandlerHelper.insertItemStacked(this.chestInv, entityStack, false);

			// 全て入ったら
			if (stack.isEmpty()) {
				entity.setDead();
			}

			// 全部は入らないなら入った分だけ減らす
			else {
				int count = stack.getCount();
				entityStack.setCount(count);
			}
		}
	}

    public boolean isSlotEmpty () {

    	for (int i = 0; i < this.getInvSize(); i++) {
    		ItemStack stack = this.getChestItem(i);
    		if (!stack.isEmpty()) { return false; }
    	}

    	return true;
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
		tags.setInteger("range", this.range);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.chestInv.deserializeNBT(tags.getCompoundTag("chestInv"));
		this.range = tags.getInteger("range");
	}

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
}
