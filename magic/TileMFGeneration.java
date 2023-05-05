package sweetmagic.init.tile.magic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.sweetmagic.SMBucket;
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.WrappedItemHandler;
import sweetmagic.packet.MFGenerationPKT;

public class TileMFGeneration extends TileMFFisher {

	public int maxMagiaFlux = 100000;
	public int needMF = 10000;
	public int chargeMF = 5000; 			// 一度に補給できるMF;
	public final int randTick = 200;
	public int lavaValue = 0;
	public int lavaMaxValue = 1000000;
	public LavaTank lavaTank = new LavaTank(this.lavaMaxValue, this);
	private final IBlockState LAVA = Blocks.LAVA.getDefaultState();

	public final ItemStackHandler outputInv = new InventoryWoodChest(this, this.getInvSize()) {

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.ISLAVABUCKET.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
		}
	};

	public final ItemStackHandler fuelInv = new InventoryWoodChest(this, 1) {

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.ISBUCKET.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
		}
	};

	private static final List<EnumFacing> faceList = Arrays.<EnumFacing> asList(
		EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST
	);

	// 釣りのルートテーブルから引き出す
	public boolean onFishing () {

		this.setMF(this.getMF() - this.needMF);
		this.sentClient();

		// 溶岩を投入（最大4杯分）
		this.lavaValue = this.lavaValue + Math.min(this.getMaxLavaAmount() - this.getLavaAmount(), 10 * Fluid.BUCKET_VOLUME);

		for (EnumFacing face : faceList) {

			Block block = this.getBlock(this.pos.offset(face).down());
			if (block != Blocks.MAGMA) { continue; }

			this.world.setBlockState(this.pos.offset(face), LAVA, 3);
			this.lavaValue -= Fluid.BUCKET_VOLUME;
		}

		PacketHandler.sendToClient(new MFGenerationPKT(this.lavaValue, this.pos));

		return false;
	}

	public void smeltItem() { }

	// 釣りが出来るかチェック
	public boolean checkMF () {
		return this.getMF() >= this.getNeedMF() && this.getLavaAmount() < this.getMaxLavaAmount();
	}

	// 釣りを終えた後に
	public void aftrterAction() {

		ItemStack stack = this.getFuelItem();
		if(stack.isEmpty()) { return; }

		ItemStack out = this.getOutputItem();
		if(!out.isEmpty()) { return; }

		Item item = stack.getItem();
		int amount = 0;

		if (item == Items.BUCKET) {
			amount = 1;
			ItemHandlerHelper.insertItemStacked(this.outputInv, new ItemStack(Items.LAVA_BUCKET), false);
			stack.shrink(1);
		}

		else if (item == ItemInit.alt_bucket) {
			ItemStack stack2 = new ItemStack(ItemInit.alt_bucket_lava);
			SMBucket bucket = (SMBucket) stack2.getItem();
			amount = Math.min(10, this.getLavaAmount());
			bucket.setAmout(bucket.setTag(stack2, stack2.getTagCompound()), amount);
			ItemHandlerHelper.insertItemStacked(this.outputInv, stack2, false);
			stack.shrink(1);
		}

		else if (item == ItemInit.alt_bucket_lava) {
			SMBucket bucket = (SMBucket) stack.getItem();
			amount = Math.min(10, this.getLavaAmount());
    		NBTTagCompound tags = bucket.setTag(stack, stack.getTagCompound());
			bucket.setAmout(bucket.setTag(stack, tags), amount + bucket.getAmount(tags));
			ItemHandlerHelper.insertItemStacked(this.outputInv, stack.copy(), false);
			stack.shrink(1);
		}

		this.lavaValue -= amount * Fluid.BUCKET_VOLUME;
		PacketHandler.sendToClient(new MFGenerationPKT(this.lavaValue, this.pos));
		this.playSound(this.pos, SoundEvents.ITEM_BUCKET_FILL_LAVA, 0.33F, 1F);
	}

	private final CombinedInvWrapper com = new CombinedInvWrapper(this.fuelInv, this.outputInv);
	private final IItemHandlerModifiable output = new WrappedItemHandler(this.com, WrappedItemHandler.WriteMode.IN_OUT);

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.output);
		}

		return super.getCapability(cap, side);
	}

	// インベントリの数
	public int getInvSize() {
		return 1;
	}

	public IItemHandler getOutput() {
		return this.outputInv;
	}
	public ItemStack getOutputItem() {
		return this.getOutput().getStackInSlot(0);
	}

	public IItemHandler getFuel() {
		return this.fuelInv;
	}

	public ItemStack getFuelItem() {
		return this.getFuel().getStackInSlot(0);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("Output", this.outputInv.serializeNBT());
		tags.setTag("Fuel", this.fuelInv.serializeNBT());
		tags.setInteger("lavaValue", this.lavaValue);
		this.lavaTank.writeToNBT(tags);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.outputInv.deserializeNBT(tags.getCompoundTag("Output"));
		this.fuelInv.deserializeNBT(tags.getCompoundTag("Fuel"));
		this.lavaValue = tags.getInteger("lavaValue");
		this.lavaTank.readFromNBT(tags);
	}

	// 次の処理時間の初期化
	public int clearRandTime () {
		return this.world.rand.nextInt(this.randTick);
	}

	public boolean checkTime () {
		return this.tickTime >= (this.randTick + this.randTime);
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

	// 送信するMF量
	@Override
	public int getUseMF () {
		return this.chargeMF;
	}

	public int getLavaAmount() {
		return this.lavaValue;
	}

	public int getMaxLavaAmount() {
		return this.lavaMaxValue;
	}

	public boolean isLavaEmpty() {
		return this.lavaValue <= 0;
	}

	// MFゲージの描画量を計算するためのメソッド
	public int getLavaProgressScaled(int value) {
		return this.isLavaEmpty() ? 0 : Math.min(value, (int) (value * ( (float) this.getLavaAmount() / (float) this.getMaxLavaAmount() )));
	}

	public static class LavaTank extends FluidTank {

		private static final FluidStack MAX_WATER = new FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME);
		private int capacity;
		private final TileMFGeneration tile;

		public LavaTank(int capacity, TileMFGeneration tile) {
			super(capacity);
			this.capacity = capacity;
			this.tile = tile;
		}


		@Override
		public FluidStack getFluid() {
			return super.getFluid();
		}

		@Override
		public int getFluidAmount() {
			return super.getFluidAmount();
		}

		@Override
		public int getCapacity() {
			return this.capacity;
		}

	    public boolean canFill()
	    {
	        return false;
	    }

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain) {

			int amount = resource.amount;
			if (resource.getFluid() == FluidRegistry.LAVA && this.tile.lavaValue >= amount) {
				this.tile.lavaValue -= amount;
				PacketHandler.sendToClient(new MFGenerationPKT(this.tile.lavaValue, this.tile.pos));
				return new FluidStack(FluidRegistry.LAVA, amount);
			}

			return super.drain(resource, true);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			return new FluidStack(FluidRegistry.LAVA, (this.tile.lavaValue > 0) ? Fluid.BUCKET_VOLUME : 0);
		}
	}
}
