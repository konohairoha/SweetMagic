package sweetmagic.init.tile.magic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.magic.MFPot;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.init.tile.slot.WrappedItemHandler;
import sweetmagic.packet.TileMFBlockPKT;

public class TileMFFisher extends TileMFBase {

	public int maxTime;
	public Random rand = new Random();
	public int needMF = 300;
	public int randTick = 300;

	public final ItemStackHandler outputInv = new StackHandler(this, this.getInvSize());
	public final ItemStackHandler fuelInv = new StackHandler(this, 1);

	private final IItemHandlerModifiable autoFuel = new WrappedItemHandler(this.fuelInv,
			WrappedItemHandler.WriteMode.IN) {
		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.MFF_FUEL.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
		}
	};

	private final IItemHandlerModifiable autoOutput = new WrappedItemHandler(this.outputInv, WrappedItemHandler.WriteMode.OUT);
	private final IItemHandler autoSide = new CombinedInvWrapper(this.autoFuel, this.autoOutput);
	private final CombinedInvWrapper join = new CombinedInvWrapper(this.autoFuel, this.autoOutput);

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
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoOutput);
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoSide);
				}
			}
		}

		return super.getCapability(cap, side);
	}

	// 必要MF
	public int getNeedMF() {
		return this.needMF;
	}

	@Override
	public void serverUpdate() {

		super.serverUpdate();

		this.tickTime++;
		if (!this.isActive(this.world, this.pos) || this.getTime() % 20 != 0) { return; }

		// ランダム時間の初期化
		if (this.randTime == 0) {
			this.randTime = this.rand.nextInt(this.randTick);
		}

		// MFが最大に達していないなら
		if (this.getMF() < this.getMaxMF()) {
			this.smeltItem();
		}

		// 時間を超えた場合釣りを行う
		if (this.tickTime >= (this.randTick + this.randTime)) {

			this.tickTime = 0;

			// 必要MFを持っていると釣りのルートテーブル呼び出し
			if (this.getMF() >= this.getNeedMF()) {
				this.onFishing();
			}
		}

		this.markDirty();
	}

	// 燃焼処理
	public void smeltItem() {

		ItemStack stack = this.getFuelItem();

		// 燃焼アイテムのMFを取得してMFに加算する
		this.setMF(this.getMF() + this.getItemBurnTime(stack));
		stack.shrink(1);
		PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));
	}

	// 釣りのルートテーブルから引き出す
	public boolean onFishing () {

		this.setMF(this.getMF() - this.getNeedMF());
		PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));

		if (!this.world.isRemote) {
			LootContext.Builder lootcontext = new LootContext.Builder((WorldServer) this.world);

			// ルートテーブルをリストに入れる
			List<ItemStack> items = this.world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING)
					.generateLootForPools(this.rand, lootcontext.build());

			// アイテムをスポーン
			for (ItemStack stack : items) {
				ItemHandlerHelper.insertItemStacked(this.outputInv, stack, false);
			}
		}

		this.playSound(this.pos, SoundEvents.ENTITY_BOBBER_SPLASH, 0.5F, 1F);
		this.randTick = this.getBlock(this.pos.up()) instanceof MFPot ? 150 : 300;
		return false;
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("Output", this.outputInv.serializeNBT());
		tags.setTag("Fuel", this.fuelInv.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.outputInv.deserializeNBT(tags.getCompoundTag("Output"));
		this.fuelInv.deserializeNBT(tags.getCompoundTag("Fuel"));
	}

	// インベントリの数
	protected int getInvSize() {
		return 18;
	}

	public IItemHandler getOutput() {
		return this.outputInv;
	}

	public IItemHandler getFuel() {
		return this.fuelInv;
	}

	private ItemStack getFuelItem() {
		return this.fuelInv.getStackInSlot(0);
	}

	public boolean isFisher () {
		return this.getBlock(this.pos) == BlockInit.mffisher;
	}
}
