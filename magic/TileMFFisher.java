package sweetmagic.init.tile.magic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
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
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.magic.MFFisher;
import sweetmagic.init.block.magic.MFPot;
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileMFFisher extends TileMFBase {

	public int maxTime;
	public int needMF = 300;
	public final int randTick = 300;
	public boolean isActive = false;
	public boolean findPlayer = false;

	public final ItemStackHandler outputInv = new InventoryWoodChest(this, this.getInvSize());

	public final ItemStackHandler fuelInv = new InventoryWoodChest(this, 1) {

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.MFF_FUEL.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
		}
	};

	private final CombinedInvWrapper fuelOut = new CombinedInvWrapper(this.fuelInv, this.outputInv);
	private final IItemHandlerModifiable output = new WrappedItemHandler(this.fuelOut, WrappedItemHandler.WriteMode.IN_OUT);

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

	// 必要MF
	public int getNeedMF() {
		return this.needMF;
	}

	@Override
	public void update() {

		super.update();

		this.tickTime++;
		if (this.getTime() % 20 != 0) { return; }

		this.isActive = this.isActive(this.pos);
		if (!this.isActive) { return; }

		// クライアント側処理
		if (!this.isSever()) {
			this.findPlayer = this.findRangePlayer();
			return;
		}

		// ランダム時間の初期化
		if (this.randTime <= 0) {
			this.randTime = this.clearRandTime();
		}

		// MFが最大に達していないなら
		if (this.getMF() < this.getMaxMF()) {
			this.smeltItem();
		}

		// 時間を超えた場合釣りを行う
		if (this.checkTime()) {

			this.tickTime = 0;

			// 必要MFを持っていると釣りのルートテーブル呼び出し
			if (this.checkMF()) {
				this.onFishing();
			}
		}

		this.aftrterAction();

		this.markDirty();
	}

	// 次の処理時間の初期化
	public int clearRandTime () {
		return this.world.rand.nextInt(this.randTick);
	}

	// 燃焼処理
	public void smeltItem() {

		ItemStack stack = this.getFuelItem();

		// 燃焼アイテムのMFを取得してMFに加算する
		this.setMF(this.getMF() + this.getItemBurnTime(stack));
		stack.shrink(1);
		this.sentClient();
	}

	public boolean checkTime () {
		return this.tickTime >= (this.randTick + this.randTime);
	}

	public boolean checkMF () {
		return this.getMF() >= this.getNeedMF();
	}

	// 釣りを終えた後に
	public void aftrterAction () { }

	// 釣りのルートテーブルから引き出す
	public boolean onFishing () {

		this.setMF(this.getMF() - this.getNeedMF());
		this.sentClient();
		Random rand = world.rand;

		if (this.isSever()) {
			LootContext.Builder lootcontext = new LootContext.Builder((WorldServer) this.world);

			// ルートテーブルをリストに入れる
			List<ItemStack> items = this.world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING)
					.generateLootForPools(rand, lootcontext.build());

			// アイテムをスポーン
			for (ItemStack stack : items) {
				ItemHandlerHelper.insertItemStacked(this.outputInv, stack, false);
			}

			// 確率で海藻を追加
			if (rand.nextFloat() <= 0.3F) {
				ItemHandlerHelper.insertItemStacked(this.outputInv, new ItemStack(ItemInit.seaweed), false);
			}

			// 確率で鮭を追加
			if (rand.nextFloat() <= 0.1F) {
				ItemHandlerHelper.insertItemStacked(this.outputInv, new ItemStack(Items.FISH, 1, 1), false);
			}
		}

		this.playSound(this.pos, SoundEvents.ENTITY_BOBBER_SPLASH, 0.25F, 1F);
		this.randTime = rand.nextInt(this.getBlock(this.pos.up()) instanceof MFPot ? 150 : 300);
		return false;
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("Output", this.outputInv.serializeNBT());
		tags.setTag("Fuel", this.fuelInv.serializeNBT());
		tags.setBoolean("isActive", this.isActive);
		tags.setBoolean("findPlayer", this.findPlayer);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.outputInv.deserializeNBT(tags.getCompoundTag("Output"));
		this.fuelInv.deserializeNBT(tags.getCompoundTag("Fuel"));
		this.isActive = tags.getBoolean("isActive");
		this.findPlayer = tags.getBoolean("findPlayer");
	}

	// インベントリの数
	public int getInvSize() {
		return 18;
	}

	public IItemHandler getOutput() {
		return this.outputInv;
	}

	public IItemHandler getFuel() {
		return this.fuelInv;
	}

	public ItemStack getFuelItem() {
		return this.fuelInv.getStackInSlot(0);
	}

	public int getData () {
		return ((MFFisher) this.getBlock(this.pos)).getData();
	}

	public EntityLivingBase getEntity() {
		return null;
	}
}
