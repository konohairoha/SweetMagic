package sweetmagic.init.tile.magic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.furnace.WrappedItemHandler;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.packet.TileMFBlockPKT;

public class TileMFTank extends TileMFBase {

	private final ItemStackHandler inputInventory = new StackHandler(this, this.getInvSize());
	private final ItemStackHandler outputInventory = new StackHandler(this, this.getInvSize());
	public int maxMagiaFlux = 200000;	// 最大MF量を設定

	public TileMFTank () { }

	@Override
	public void serverUpdate() {

		super.serverUpdate();
		if (this.getTime() % 10 != 0) { return; }

		// 精錬可能かつ必要MF以上なら
		if (this.canSmelt() && this.getMF() >= this.needMF()) {
			this.smeltItem();
		}
	}

	public void checkChanger() {

		if (this.getBlock(this.pos.up()) == Blocks.AIR || !(this.getTile(this.pos) instanceof TileMFBase)) { return; }

		TileMFBase mf = (TileMFBase) this.getTile(this.pos);
		if (mf.getMF() > 0) {
			this.setMF(this.getMF() + mf.outputMF(1000));
		}
	}

	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("Input", this.inputInventory.serializeNBT());
		tags.setTag("Output", this.outputInventory.serializeNBT());
		return tags;
	}

	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.inputInventory.deserializeNBT(tags.getCompoundTag("Input"));
		this.outputInventory.deserializeNBT(tags.getCompoundTag("Output"));
	}

	// 最大MF量を設定
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

	//=========================
	// 以下がスロットのためのソース

	// インベントリの数
	protected int getInvSize() {
		return 1;
	}

	public IItemHandler getInput() {
		return this.inputInventory;
	}

	public IItemHandler getOutput() {
		return this.outputInventory;
	}

	public boolean canSmelt() {

		ItemStack toSmelt = this.inputInventory.getStackInSlot(0);

		if (toSmelt.isEmpty()) { return false; }

		// 鉄ならtoSmeltを書き換え
		if (toSmelt.getItem() == Items.GLASS_BOTTLE || toSmelt.getItem() == ItemInit.b_mf_bottle) {
			return true;
		}

		return false;
	}

	// 精錬後のアイテム
	private void smeltItem() {

		ItemStack toSmelt = this.inputInventory.getStackInSlot(0);
		ItemStack smeltResult = this.getSmeltItem(toSmelt);

		// smeltResultがnullなら何もしない
		if (smeltResult.isEmpty()) { return; }
		if (!ItemHandlerHelper.insertItemStacked(this.getOutput(), smeltResult, true).isEmpty()) { return; }

		this.setMF(this.getMF() - this.needMF());
		PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));

		// outputスロットが空なら
		ItemHandlerHelper.insertItemStacked(this.getOutput(), smeltResult, false);
		toSmelt.shrink(1);
	}

	// 精錬アイテムの取得
	public ItemStack getSmeltItem (ItemStack toSmelt) {

		ItemStack smeltResult = ItemStack.EMPTY;

		// MFボトル
		if (toSmelt.getItem() == ItemInit.b_mf_bottle) {
			smeltResult = new ItemStack(ItemInit.mf_bottle);
		}

		// MF小ボトル
		else if (toSmelt.getItem() == Items.GLASS_BOTTLE) {
			smeltResult = new ItemStack(ItemInit.mf_sbottle);
		}

		return smeltResult;
	}

	// 必要MF
	public int needMF () {

		ItemStack toSmelt = inputInventory.getStackInSlot(0);
		int needMF = 0;

		// MFボトル
		if (toSmelt.getItem() == ItemInit.b_mf_bottle) {
			needMF = 10000;
		}

		// MF小ボトル
		else if (toSmelt.getItem() == Items.GLASS_BOTTLE) {
			needMF = 1000;
		}

		return needMF;
	}

	@Override
	public List<ItemStack> getList() {

		List<ItemStack> stackList = new ArrayList<>();

		stackList.add(this.inputInventory.getStackInSlot(0));
		stackList.add(this.outputInventory.getStackInSlot(0));

		return stackList;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return this.maxMagiaFlux == 3000000 ? 5000 : 1000;
    }

	private final IItemHandlerModifiable down = new WrappedItemHandler(this.outputInventory, WrappedItemHandler.WriteMode.OUT);
	private final IItemHandler side = new CombinedInvWrapper(this.inputInventory);
	private final CombinedInvWrapper top = new CombinedInvWrapper(this.inputInventory);

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
				case DOWN:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.down);
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.side);
				}
			}
		}

		return super.getCapability(cap, side);
	}
}
