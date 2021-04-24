package sweetmagic.init.tile.magic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.StackHandler;

public class TileMFChanger extends TileMFBase {

	public int maxMagiaFlux = 10000;

	// MFアイテムのスロット
	public final ItemStackHandler inputInventory = new StackHandler(this, this.getInvSize()) {

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.MFF_FUEL.test(stack)
					? super.insertItem(slot, stack, simulate)
					: stack;
		}
	};

	public TileMFChanger () {
		super(false);
	}

	@Override
	public void serverUpdate() {

		super.serverUpdate();

		if (this.getTime() % 80 != 0) { return; }

		// MF量が最大に足してなかったら動かす
		if (this.canMFChange()) {
			this.changeMF();
		}

	}

	// MFに変換
	protected boolean changeMF() {

		for (int i = 0; i < this.getInvSize(); i++) {

			// スロットが空なら終了
			ItemStack stack = this.getInputItem(i);
			if (stack.isEmpty() || !SlotPredicates.isMFItem(stack)) { continue; }

			// 1アイテムのMF量の取得
			int mfValue = this.getItemMF(stack.getItem());

			// 消費個数を計算
			int value = (this.getMaxMF() - this.getMF()) / mfValue + 1;
			value = value > stack.getCount() ? stack.getCount(): value;

			// MF量を計算
			int itemMF = mfValue * value;

			// MFを入れる
			this.setMF(this.getMF() + itemMF);

			// スロットのアイテムを減らす
			stack.shrink(value);

			// 最大になったら終了
			if (!this.canMFChange()) { break; }
		}

		this.markDirty();
		this.sentClient();

		return true;
	}

	@Override
	public void setMF (int magiaFlux) {
		this.magiaFlux = magiaFlux < 0 ? 0 : magiaFlux;
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("Input", this.inputInventory.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.inputInventory.deserializeNBT(tags.getCompoundTag("Input"));
	}

	// インベントリの数
	public int getInvSize() {
		return 1;
	}

	// MFスロットの取得
	public IItemHandler getInput() {
		return this.inputInventory;
	}

	// インプットスロットのアイテムを取得
	public  ItemStack getInputItem(int i) {
		return this.getInput().getStackInSlot(i);
	}

	@Override
	public List<ItemStack> getList() {

		List<ItemStack> stackList = new ArrayList<>();

		for (int i = 0; i < this.getInvSize(); i++) {
			stackList.add(this.getInputItem(i));
		}

		return stackList;
	}

	public IItemHandler side = new CombinedInvWrapper(this.inputInventory);
	public CombinedInvWrapper top = new CombinedInvWrapper(this.inputInventory);

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.top);
			} else {
				switch (side) {
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.side);
				}
			}
		}

		return super.getCapability(cap, side);
	}
}
