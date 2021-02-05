package sweetmagic.init.tile.furnace;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.magic.MFFurnace;
import sweetmagic.init.tile.magic.TileMFBase;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.util.ItemHelper;

public class TileMFF extends TileMFBase {

	private final ItemStackHandler inputInv = new StackHandler(this, this.getInvSize());
	private final ItemStackHandler outInv = new StackHandler(this, this.getInvSize());
	private final ItemStackHandler fuelInv = new StackHandler(this, 1);

	private final IItemHandlerModifiable autoInput = new WrappedItemHandler(this.inputInv, WrappedItemHandler.WriteMode.IN) {

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.SMELTABLE.test(stack)
					? super.insertItem(slot, stack, simulate)
					: stack;
		}
	};

	private final IItemHandlerModifiable autoFuel = new WrappedItemHandler(this.fuelInv,
			WrappedItemHandler.WriteMode.IN) {
		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.MFF_FUEL.test(stack)
					? super.insertItem(slot, stack, simulate)
					: stack;
		}
	};

	private final IItemHandlerModifiable autoOut = new WrappedItemHandler(this.outInv, WrappedItemHandler.WriteMode.OUT);
	private final IItemHandler autoSide = new CombinedInvWrapper(this.autoFuel, this.autoOut);
	private final CombinedInvWrapper join = new CombinedInvWrapper(this.autoInput, this.autoFuel, this.autoOut);
	protected final int ticksBeforeSmelt;
	private final int efficiencyBonus;
//	public int furnaceBurnTime;
	public int currentItemBurnTime;
	public int furnaceCookTime;

	// １個の燃焼時間、効率
	public TileMFF() {
		this(65, 1);
	}

	protected TileMFF(int ticksBeforeSmelt, int efficiencyBonus) {
		super();
		this.ticksBeforeSmelt = ticksBeforeSmelt;
		this.efficiencyBonus = efficiencyBonus;
	}

	@Override
	public void update() {

		super.update();
		boolean notEmpty = !this.isMfEmpty();

		// 精錬アイテムが入ってないと燃焼時間が減らない
		if (notEmpty && this.canSmelt()) {
			this.setMF(this.getMF() - 3);
		}

		this.pullFromInventories();
		ItemHelper.compactInventory(this.inputInv);

		// MFが最大量じゃなかったら燃料をひたすら減らす
		if (this.getMF() < this.getMaxMF() && this.canSmelt()) {

			this.setMF(this.getMF() + this.getItemBurnTime(this.getFuelItem()));
			this.currentItemBurnTime += this.getItemBurnTime(this.getFuelItem());

			if (this.getMF() > 0) {
				this.markDirty();

				if (!this.getFuelItem().isEmpty()) {
					ItemStack copy = this.getFuelItem().copy();

					this.getFuelItem().shrink(1);

					if (this.getFuelItem().isEmpty()) {
						this.fuelInv.setStackInSlot(0, copy.getItem().getContainerItem(copy));
					}
				}
			}
		}

		// 精錬出来るアイテムがあると精錬
		if (this.getMF() > 0 && this.canSmelt()) {

			this.furnaceCookTime++;
			this.markDirty();

			if (this.furnaceCookTime == this.ticksBeforeSmelt) {
				this.furnaceCookTime = 0;
				this.smeltItem();
			}
		}

		ItemHelper.compactInventory(this.outInv);

		if (this.tickTime % 20 == 0) {

			ItemStack stack = this.inputInv.getStackInSlot(0);
			IBlockState state = this.getState(this.pos);
			MFFurnace block = (MFFurnace) state.getBlock();
	        TileEntity tile = this;

			if (stack.isEmpty() && block == BlockInit.mffurnace_on) {
				block.setBlock = true;
				this.setBlock(state, BlockInit.mffurnace_off, block);
				block.setBlock = false;
			}

			else if (!stack.isEmpty() && block == BlockInit.mffurnace_off) {
				block.setBlock = true;
				this.setBlock(state, BlockInit.mffurnace_on, block);
				block.setBlock = false;
			}

	        if (tile != null){
	            tile.validate();
	            this.world.setTileEntity(this.pos, tile);
	        }
		}
	}

	public void setBlock (IBlockState state, Block setBlock, MFFurnace face) {
		this.world.setBlockState(this.pos, setBlock.getDefaultState().withProperty(face.FACING, state.getValue(MFFurnace.FACING)), 2);
	}

	public boolean isItemFuel(ItemStack stack) {
		return this.getItemBurnTime(stack) > 0;
	}

	public boolean isBurning() {
		return this.getMF() > 0;
	}

	private void pullFromInventories() {

		TileEntity tile = this.getTile(this.pos.up());

		if (tile == null || tile instanceof TileEntityHopper || tile instanceof TileEntityDropper) {
			return;
		}

		IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

		if (handler == null) {
			if (tile instanceof ISidedInventory) {
				handler = new SidedInvWrapper((ISidedInventory) tile, EnumFacing.DOWN);
			} else if (tile instanceof IInventory) {
				handler = new InvWrapper((IInventory) tile);
			} else {
				return;
			}
		}

		for (int i = 0; i < handler.getSlots(); i++) {

			ItemStack extract = handler.extractItem(i, Integer.MAX_VALUE, true);
			if (extract.isEmpty()) { continue; }

			IItemHandler inv = TileEntityFurnace.isItemFuel(extract) ? this.fuelInv : this.inputInv;
			ItemStack remainder = ItemHandlerHelper.insertItemStacked(inv, extract, true);
			int success = extract.getCount() - remainder.getCount();

			if (success > 0) {
				ItemStack insert = handler.extractItem(i, success, false);
				ItemStack result = ItemHandlerHelper.insertItemStacked(inv, insert, false);
				assert result.isEmpty();
			}
		}
	}

	private void smeltItem() {

		ItemStack smelt = this.inputInv.getStackInSlot(0);
		ItemStack result = FurnaceRecipes.instance().getSmeltingResult(smelt).copy();

		if (this.world.rand.nextFloat() < this.getOreDoubleChance()
				&& ItemHelper.getOreDictName(smelt).startsWith("ore")) {
			result.grow(result.getCount());
		}

		ItemHandlerHelper.insertItemStacked(this.outInv, result, false);
		smelt.shrink(1);
	}

	public boolean canSmelt() {

		ItemStack toSmelt = this.inputInv.getStackInSlot(0);
		if (toSmelt.isEmpty()) { return false; }

		ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(toSmelt);
		if (smeltResult.isEmpty()) { return false; }

		ItemStack currentSmelt = this.outInv.getStackInSlot(this.outInv.getSlots() - 1);
		if (currentSmelt.isEmpty()) { return true; }
		if (!smeltResult.isItemEqual(currentSmelt)) { return false; }

		int result = currentSmelt.getCount() + smeltResult.getCount();
		return result <= currentSmelt.getMaxStackSize();
	}

	public int getCookProgressScaled(int value) {
		return (this.furnaceCookTime + (isBurning() && canSmelt() ? 1 : 0)) * value / this.ticksBeforeSmelt;
	}



//	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int value) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = this.ticksBeforeSmelt;
		}
		return this.getMF() * value / this.currentItemBurnTime;
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		if (this.getMF() != 0) { tags.setInteger("magiaFlux", this.getMF()); }
		tags.setShort("BurnTime", (short) this.getMF());
		tags.setShort("CookTime", (short) this.furnaceCookTime);
		tags.setTag("Input", this.inputInv.serializeNBT());
		tags.setTag("Output", this.outInv.serializeNBT());
		tags.setTag("Fuel", this.fuelInv.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		this.setMF(tags.getInteger("magiaFlux"));
		this.setMF(tags.getShort("BurnTime"));
		this.furnaceCookTime = tags.getShort("CookTime");
		this.inputInv.deserializeNBT(tags.getCompoundTag("Input"));
		this.outInv.deserializeNBT(tags.getCompoundTag("Output"));
		this.fuelInv.deserializeNBT(tags.getCompoundTag("Fuel"));
		this.currentItemBurnTime = this.getItemBurnTime(this.getFuelItem());
	}

	// インベントリの数
	protected int getInvSize() {
		return 9;
	}

	protected float getOreDoubleChance() {
		return 1F;
	}

	public IItemHandler getFuel() {
		return this.fuelInv;
	}

	private ItemStack getFuelItem() {
		return this.fuelInv.getStackInSlot(0);
	}

	public IItemHandler getInput() {
		return this.inputInv;
	}

	public IItemHandler getOutput() {
		return this.outInv;
	}

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
				case UP:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoInput);
				case DOWN:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoOut);
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoSide);
				}
			}
		}

		return super.getCapability(cap, side);
	}
}
