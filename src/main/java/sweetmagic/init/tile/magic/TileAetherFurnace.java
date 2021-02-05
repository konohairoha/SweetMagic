package sweetmagic.init.tile.magic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

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
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.furnace.WrappedItemHandler;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.packet.TileMFBlockPKT;
import sweetmagic.util.ItemHelper;

public class TileAetherFurnace extends TileMFBase {

	public int maxMagiaFlux = 60000;
	private final ItemStackHandler inputInventory = new StackHandler(this, 5);	// MFアイテムのスロット
	private final ItemStackHandler crystaleInventory = new StackHandler(this, 18);	// クリスタルスロット

	public TileAetherFurnace () { }

	@Override
	public void serverUpdate() {

		super.serverUpdate();

		if (this.getTime() % 50 != 0) { return; }

		// MF量が最大に足してなかったら動かす
		if (this.canMFChange()) {
			this.doBurn();
		}

		// MFが空出なければ
		if (!this.isMfEmpty()) {
			this.createCrystal();
		}

		PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));
		ItemHelper.compactInventory(this.inputInventory);
		ItemHelper.compactInventory(this.crystaleInventory);
		this.markDirty();
	}

	// 精錬
	public void doBurn () {

		for (int i = 0; i < 4; i++) {

			// スロットが空なら終了
			ItemStack stack = this.getInputItem(i);
			if (stack.isEmpty()) { continue; }

			// 1アイテムのMF量の取得
			int mfValue = this.getItemMF(stack.getItem());

			// 消費個数を計算
			int value = Math.min((this.getMaxMF() - this.getMF()) / mfValue + 1, stack.getCount());

			// MF量を計算
			int itemMF = mfValue * value;

			// MFを入れる
			this.setMF(this.getMF() + itemMF);

			// スロットのアイテムを減らす
			stack.shrink(value);
		}

		PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));
	}

	// クリスタル生成
	public void createCrystal () {
		// 必要MFの取得

		Item item = this.getBlock(this.pos) == BlockInit.aether_furnace_bottom ? ItemInit.aether_crystal : ItemInit.divine_crystal;
		int needMF = this.getItemMF(item);
		ItemStack stack = new ItemStack(item, 1);

		for (int i = 0; i < 8; i++) {

			// MFが足りないなら終了
			if (this.getMF() < needMF) { return; }

			ItemStack copy = stack.copy();

			if (!ItemHandlerHelper.insertItemStacked(this.crystaleInventory, copy, true).isEmpty()) {
				return;
			}

			ItemHandlerHelper.insertItemStacked(this.crystaleInventory, copy, false);
			this.setMF(this.getMF() - needMF);
		}
	}

	@Override
	public int getMaxMF() {
		return 50000;
	}

	private final IItemHandlerModifiable autoOutput = new WrappedItemHandler(this.crystaleInventory, WrappedItemHandler.WriteMode.OUT);
	private final IItemHandler autoSide = new CombinedInvWrapper(this.inputInventory);
	private final CombinedInvWrapper joined = new CombinedInvWrapper(this.inputInventory);

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.joined);
			} else {
				switch (side) {
				case DOWN:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoOutput);
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoSide);
				}
			}
		}

		return super.getCapability(cap, side);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("Input", this.inputInventory.serializeNBT());
		tags.setTag("Crystal", this.crystaleInventory.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.inputInventory.deserializeNBT(tags.getCompoundTag("Input"));
		this.crystaleInventory.deserializeNBT(tags.getCompoundTag("Crystal"));
	}

	// MFスロットの取得
	public IItemHandler getInput() {
		return this.inputInventory;
	}

	// MFスロットのアイテムを取得
	public  ItemStack getInputItem(int slot) {
		return this.getInput().getStackInSlot(slot);
	}

	// クリスタルスロットの取得
	public IItemHandler getCrystal() {
		return this.crystaleInventory;
	}

	// クリスタルスロットのアイテムを取得
	public  ItemStack getCrystalItem(int slot) {
		return this.getCrystal().getStackInSlot(slot);
	}

	@Override
	public List<ItemStack> getList() {
		List<ItemStack> stackList = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			stackList.add(this.getInputItem(i));
		}

		for (int i = 0; i < 18; i++) {
			stackList.add(this.getCrystalItem(i));
		}

		return stackList;
	}
}
