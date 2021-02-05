package sweetmagic.init.tile.cook;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.blocks.BlockStove;
import sweetmagic.init.tile.furnace.WrappedItemHandler;
import sweetmagic.init.tile.magic.TileSMBase;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.StackHandler;

public class TileStove extends TileSMBase {

	public int needTickTime = 100;		// 必要稼働時間
	public int needBurnTime = 300;		// 必要燃焼時間
	public int burnTime = 0;			// 燃焼時間
	public int maxBurnTime = 10000;		// 最大燃焼時間

	public final ItemStackHandler fuelInv = new StackHandler(this, 1);

	private final IItemHandlerModifiable automationFuel = new WrappedItemHandler(this.fuelInv, WrappedItemHandler.WriteMode.IN) {

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.MFF_FUEL.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
		}
	};

	private final CombinedInvWrapper joined = new CombinedInvWrapper(automationFuel);

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.joined);
			}
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void update() {

		this.tickTime++;

		// 稼働時間が一定数を超えているかつコンロがon状態なら
		if (this.tickTime >= this.needTickTime && this.getBlock(this.pos) == BlockInit.stove_on) {

			this.tickTime = 0;

			// 燃焼時間が一定数を超えていたら
			if (this.canCook()) {
				this.setBurnTime(this.getBurnTime() - this.needBurnTime);

			// 燃焼時間が一定未満なら
			} else {

				// 精錬出来るなら精錬
				if (this.canSmelt()) {
					this.setBurnTime(this.getItemBurnTime());
				}
			}
		}
	}

	// スロットのアイテムを取得し燃焼時間を返す
	protected int getItemBurnTime() {
		int burnTime = this.getBurnTime() + TileEntityFurnace.getItemBurnTime(this.fuelInv.getStackInSlot(0));
		this.fuelInv.getStackInSlot(0).shrink(1);
		return burnTime;
	}

	// 燃焼できるか
	public boolean canSmelt () {
		return TileEntityFurnace.isItemFuel(this.fuelInv.getStackInSlot(0)) && !this.maxBurnTime();
	}

	// 最大燃焼時間かどうか
	public boolean maxBurnTime () {
		return this.burnTime >= this.maxBurnTime;
	}

	// 燃焼時間の設定
	public void setBurnTime (int time) {
		this.burnTime = time;
	}

	// 燃焼時間の取得
	public int getBurnTime () {
		return this.burnTime;
	}

	// 必要燃焼時間を超えているか
	public boolean canCook () {
		return this.burnTime >= this.needBurnTime;
	}

	// ブロック置換処理
	public void updateState () {
		BlockStove.setState(this.world, this.pos);
	}

	public IItemHandler getFuel() {
		return this.fuelInv;
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		tags.setTag("Fuel", this.fuelInv.serializeNBT());
		tags.setInteger("burnTime", this.getBurnTime());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		this.fuelInv.deserializeNBT(tags.getCompoundTag("Fuel"));
		this.setBurnTime(tags.getInteger("burnTime"));
	}
}
