package sweetmagic.init.tile.magic;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileWorkbenchStorage extends TileMFBase {

	public final int maxMagiaFlux = 500000; 	// 最大MF量を設定
	public final int needTick = 1200;
	public int nowTick_L = 0;
	public int nowTick_C = 0;
	public int nowTick_R = 0;

	// ブロックスロット
	public final ItemStackHandler toolInv = new InventoryWoodChest(this, 3) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

	// 出力スロット
	public final ItemStackHandler outInv = new InventoryWoodChest(this, this.getInvSize()) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

	@Override
	public void update() {

		super.update();

		this.tickTime++;
		if (this.tickTime % 10 != 0 || !this.isActive(this.pos)) { return; }

		this.tickTime = 0;
		boolean isLeftStack = !this.getWandItem(0).isEmpty();
		boolean isCenterStack = !this.getWandItem(1).isEmpty();
		boolean isRightStack = !this.getWandItem(2).isEmpty();

		// MFがあるなら
		if (this.getMF() >= 10) {


			// サーバーのみ処理
			if (this.isSever()) {

				int shrinkMF = (isLeftStack ? 3 : 0) + (isCenterStack ? 3 : 0) + (isRightStack ? 3 : 0);

				this.setMF(this.getMF() - shrinkMF);
				this.sentClient();
			}

			this.nowTick_L = (isLeftStack ? this.nowTick_L + 3 : 0);
			this.nowTick_C = (isCenterStack ? this.nowTick_C + 3 : 0);
			this.nowTick_R = (isRightStack ? this.nowTick_R + 3 : 0);
		}

		// MFが無かったら
		else {
			this.nowTick_L = (isLeftStack ? this.nowTick_L + 1 : 0);
			this.nowTick_C = (isCenterStack ? this.nowTick_C + 1 : 0);
			this.nowTick_R = (isRightStack ? this.nowTick_R + 1 : 0);
		}

		// 時間を満たしたら
		if (this.nowTick_L >= this.needTick) {

			ItemStack copy = this.getWandItem(0).copy();
			copy.setCount(64);
			ItemStack out = ItemHandlerHelper.insertItemStacked(this.outInv, copy, true);

			if (out.isEmpty() || out.getCount() <= 63) {
				ItemHandlerHelper.insertItemStacked(this.outInv, copy, false);
				this.nowTick_L = 0;
			}
		}

		// 時間を満たしたら
		if (this.nowTick_C >= this.needTick) {

			ItemStack copy = this.getWandItem(1).copy();
			copy.setCount(64);
			ItemStack out = ItemHandlerHelper.insertItemStacked(this.outInv, copy, true);

			if (out.isEmpty() || out.getCount() <= 63) {
				ItemHandlerHelper.insertItemStacked(this.outInv, copy, false);
				this.nowTick_C = 0;
			}
		}

		// 時間を満たしたら
		if (this.nowTick_R >= this.needTick) {

			ItemStack copy = this.getWandItem(2).copy();
			copy.setCount(64);
			ItemStack out = ItemHandlerHelper.insertItemStacked(this.outInv, copy, true);

			if (out.isEmpty() || out.getCount() <= 63) {
				ItemHandlerHelper.insertItemStacked(this.outInv, copy, false);
				this.nowTick_R = 0;
			}
		}
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("tool", this.toolInv.serializeNBT());
		tags.setTag("out", this.outInv.serializeNBT());
		tags.setInteger("nowTick_L", this.nowTick_L);
		tags.setInteger("nowTick_C", this.nowTick_C);
		tags.setInteger("nowTick_R", this.nowTick_R);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.toolInv.deserializeNBT(tags.getCompoundTag("tool"));
		this.outInv.deserializeNBT(tags.getCompoundTag("out"));
		this.nowTick_L = tags.getInteger("nowTick_L");
		this.nowTick_C = tags.getInteger("nowTick_C");
		this.nowTick_R = tags.getInteger("nowTick_R");
	}

	// インベントリの数
	public int getInvSize() {
		return 27;
	}

	// ブロックスロットの取得
	public IItemHandler getTool() {
		return this.toolInv;
	}

	// ブロックスロットのアイテムを取得
	public  ItemStack getWandItem(int i) {
		return this.getTool().getStackInSlot(i);
	}

	// 出力スロットの取得
	public IItemHandler getOut() {
		return this.outInv;
	}

	// 出力スロットのアイテムを取得
	public  ItemStack getOutItem(int i) {
		return this.getOut().getStackInSlot(i);
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 10000;
    }

	private final IItemHandlerModifiable output = new WrappedItemHandler(this.outInv, WrappedItemHandler.WriteMode.OUT);

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

	// MFゲージの描画量を計算するためのメソッド
	public int getProgressScaled(int value, int now, int max) {
		return Math.min(value, (int) (value * (float) (now) / (float) (max)));
    }
}
