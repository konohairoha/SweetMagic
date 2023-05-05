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
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileMFTank extends TileMFBase {

	private final ItemStackHandler inputInv = new InventoryWoodChest(this, this.getInvSize()) {

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.MFBOTTLE.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
		}
	};

	private final ItemStackHandler outputInv = new InventoryWoodChest(this, this.getInvSize()) {

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.MFFULLBOTTLE.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
		}
	};

	public int maxMagiaFlux = 500000;	// 最大MF量を設定
	public boolean findPlayer = false;
	public int renderTick = 0;

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

	public void clientUpdate () {

		this.renderTick++;
		if (this.renderTick % 30 != 0) { return; }

		this.renderTick = 0;
		this.checkRangePlayer();
	}

	public void checkRangePlayer () {
		if (!this.isSever()) {
			this.findPlayer = this.findRangePlayer(24D, 12D, 24D);
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
		tags.setTag("Input", this.inputInv.serializeNBT());
		tags.setTag("Output", this.outputInv.serializeNBT());
		tags.setBoolean("findPlayer", this.findPlayer);
		return tags;
	}

	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.inputInv.deserializeNBT(tags.getCompoundTag("Input"));
		this.outputInv.deserializeNBT(tags.getCompoundTag("Output"));
		this.findPlayer = tags.getBoolean("findPlayer");
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
		return this.inputInv;
	}

	public ItemStack getInputStack() {
		return this.getInput().getStackInSlot(0);
	}

	public IItemHandler getOutput() {
		return this.outputInv;
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

		System.out.println("==========");

		// smeltResultがnullなら何もしない
		if (smeltResult.isEmpty()) { return; }

		System.out.println("==========InsertCkeck:" + smeltResult);
		if (!ItemHandlerHelper.insertItemStacked(this.getOutput(), smeltResult, true).isEmpty()) { return; }

		System.out.println("==========Clear");
		if (!this.isCreative()) {
			this.setMF(this.getMF() - this.needMF());
		}

		this.sentClient();

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

	private final CombinedInvWrapper inv = new CombinedInvWrapper(this.inputInv, this.outputInv);
	private final IItemHandlerModifiable output = new WrappedItemHandler(this.inv, WrappedItemHandler.WriteMode.IN_OUT);

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
}
