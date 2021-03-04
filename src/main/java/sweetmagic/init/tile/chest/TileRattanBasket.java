package sweetmagic.init.tile.chest;

import javax.annotation.Nonnull;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import sweetmagic.init.tile.furnace.WrappedItemHandler;
import sweetmagic.init.tile.magic.TileSMBase;
import sweetmagic.init.tile.slot.StackHandler;

public class TileRattanBasket extends TileSMBase {

	private boolean hasBeenCleared;
	public boolean removeTick = false;		//TickUpdate用Removeフラグ

	@Override
	public void update() {

		this.tickTime++;

		//30秒経ったら
		if(this.tickTime % 600 == 0) {

			//捨てる処理のタイミングで音を鳴らす
			this.world.playSound(null, this.pos, SoundEvents.BLOCK_SAND_BREAK,
					SoundCategory.BLOCKS, 0.25F, this.world.rand.nextFloat() * 0.19F + 0.9F);
			this.removeTick = true;
			this.tickTime = 0;

			for (int i = 0 ; i < this.getInvSize(); i++) {
				ItemStack stack = this.getChestItem(i);
				if (stack.isEmpty()) { continue; }
				stack.shrink(stack.getCount());
			}

			this.markDirty();	//一応マルチのことを考えてクライアントへのNBT書き換え通知もしておく。
		}
	}

    public NBTTagCompound writeNBT(NBTTagCompound tags) {
		return tags;
	}

	// スロット数
	public int getInvSize () {
		return 54;
	}

	// インベントリ
	public final ItemStackHandler chestInv = new StackHandler(this, this.getInvSize());

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
	public IItemHandler getChest() {
		return this.chestInv;
	}

	// インベントリのアイテムを取得
	public  ItemStack getChestItem(int i) {
		return this.getChest().getStackInSlot(i);
	}
}
