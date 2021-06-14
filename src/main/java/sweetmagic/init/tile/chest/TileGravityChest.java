package sweetmagic.init.tile.chest;

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
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import sweetmagic.init.tile.magic.TileMFBase;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileGravityChest extends TileMFBase {

	public int maxMagiaFlux = 100000;	// 最大MF量を設定

	// スロット数
	public int getInvSize () {
		return 104;
	}

	// インベントリ
	public final StackHandler chestInv = new StackHandler(this, this.getInvSize());

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

	@Override
	public void serverUpdate() {

		super.serverUpdate();

		if (!this.isActive(this.world, this.pos)  || this.getTime() % 30 != 0) { return; }

		// MF量が最大に足してなかったら動かす
		if (!this.isMfEmpty()) {
			this.suctionItem();
		}
	}

	// アイテム吸い込み
	public void suctionItem () {

		// 範囲のえんちちーを取得
        AxisAlignedBB aabb = new AxisAlignedBB(this.pos.add(-5, 0, -5), this.pos.add(5, 5, 5));
		List<EntityItem> entityList = this.world.getEntitiesWithinAABB(EntityItem.class, aabb);
		if (entityList.isEmpty()) { return; }

		// えんちちー分回す
		for (EntityItem entity : entityList) {

			ItemStack entityStack = entity.getItem();
			int useMF = entityStack.getCount() * 2;
			if (this.getMF() < useMF) { continue; }

			ItemStack stack = ItemHandlerHelper.insertItemStacked(this.chestInv, entityStack, false);

			// 全て入ったら
			if (stack.isEmpty()) {
				entity.setDead();
				this.setMF(this.getMF() - useMF);
				this.sentClient();
			}

			// 全部は入らないなら入った分だけ減らす
			else {

				int count = stack.getCount();
				entityStack.setCount(count);
				this.setMF(this.getMF() - (useMF - count * 10) );
				this.sentClient();
			}
		}
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
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.chestInv.deserializeNBT(tags.getCompoundTag("chestInv"));
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}
}
