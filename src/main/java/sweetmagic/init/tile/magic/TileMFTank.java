package sweetmagic.init.tile.magic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.init.tile.slot.WrappedItemHandler;
import sweetmagic.packet.TileMFBlockPKT;

public class TileMFTank extends TileMFBase {

	private final ItemStackHandler inputInventory = new StackHandler(this, this.getInvSize());
	private final ItemStackHandler outputInventory = new StackHandler(this, this.getInvSize());
	public int maxMagiaFlux = 500000;	// 最大MF量を設定

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

	public ItemStack getInputStack() {
		return this.getInput().getStackInSlot(0);
	}

	public IItemHandler getOutput() {
		return this.outputInventory;
	}

	public ItemStack getOutputStack() {
		return this.getOutput().getStackInSlot(0);
	}

	public boolean canSmelt() {

		ItemStack toSmelt = this.getInputStack();
		if (toSmelt.isEmpty()) { return false; }

		Item item = toSmelt.getItem();
		return item == Items.GLASS_BOTTLE || item == ItemInit.b_mf_bottle || item == ItemInit.b_mf_magiabottle;
	}

	// 精錬後のアイテム
	protected void smeltItem() {

		ItemStack toSmelt = this.getInputStack();
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
		Item bottle = toSmelt.getItem();

		// マギアボトル
		if (bottle == ItemInit.b_mf_magiabottle) {
			smeltResult = new ItemStack(ItemInit.mf_magiabottle);
		}

		// MFボトル
		else if (bottle == ItemInit.b_mf_bottle) {
			smeltResult = new ItemStack(ItemInit.mf_bottle);
		}

		// MF小ボトル
		else if (bottle == Items.GLASS_BOTTLE) {
			smeltResult = new ItemStack(ItemInit.mf_sbottle);
		}

		return smeltResult;
	}

	// 必要MF
	public int needMF () {

		ItemStack toSmelt = this.getInputStack();
		Item bottle = toSmelt.getItem();
		int needMF = 0;

		// マギアボトル
		if (bottle == ItemInit.b_mf_magiabottle) {
			needMF = 100000;
		}

		// MFボトル
		else if (bottle == ItemInit.b_mf_bottle) {
			needMF = 10000;
		}

		// MF小ボトル
		else if (bottle == Items.GLASS_BOTTLE) {
			needMF = 1000;
		}

		return needMF;
	}

	@Override
	public List<ItemStack> getList() {
		List<ItemStack> stackList = new ArrayList<>();
		this.putList(stackList, this.getInputStack());
		this.putList(stackList, this.getOutputStack());
		return stackList;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
		return 5000;
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
