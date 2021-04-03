package sweetmagic.init.tile.magic;

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
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.init.tile.slot.WrappedItemHandler;
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
	public int furnaceCookTime;
	public int useMF;

	// １個の燃焼時間、効率
	public TileMFF() {
		this(65);
	}

	protected TileMFF(int ticksBeforeSmelt) {
		super();
		this.ticksBeforeSmelt = ticksBeforeSmelt;
	}

	@Override
	public void update() {

		super.update();

		// 上のチェストから自動でアイテム取り寄せとインベントリの整理
		this.pullFromInventories();
		ItemHelper.compactInventory(this.inputInv);

		// 精錬可能なアイテムなら実行
		if (this.canSmelt()) {

			// MFが最大量じゃなかったら燃料をひたすら減らす
			if (this.getMF() < this.getMaxMF()) {
				this.chargeMF();
			}

			// MFが空でないなら精錬
			if (!this.isMfEmpty() && this.getMF() >= this.getUseMF()) {

				// 精錬時間を進める
				this.furnaceCookTime++;
				this.markDirty();

				// 精錬
				this.setMF(this.getMF() - this.getUseMF());
				this.sentClient();

				// 精錬時間が超えたら精錬
				if (this.furnaceCookTime >= this.ticksBeforeSmelt) {
					this.furnaceCookTime = 0;
					this.smeltItem();
				}
			}
		}

		// 1秒に一回チェック
		if (this.tickTime % 20 != 0) { return; }

		// 精錬後スロット整理
		ItemHelper.compactInventory(this.outInv);

		this.changeBlock();
	}

	// ブロックの置換
	public void setBlock (IBlockState state, Block setBlock, MFFurnace face) {
		this.world.setBlockState(this.pos, setBlock.getDefaultState().withProperty(face.FACING, state.getValue(MFFurnace.FACING)), 2);
	}

	// 燃焼アイテムかどうか
	public boolean isItemFuel(ItemStack stack) {
		return this.getItemBurnTime(stack) > 0;
	}

	// 上のチェストから自動でアイテム取り寄せ
	public void pullFromInventories() {

		// タイルえんちちーを持たないかホッパーなら終了
		TileEntity tile = this.getTile(this.pos.up());
		if (tile == null || tile instanceof TileEntityHopper || tile instanceof TileEntityDropper) { return; }

		// ブロックの取得
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

		// 上のチェストのスロット分回す
		for (int i = 0; i < handler.getSlots(); i++) {

			// 空なら終了
			ItemStack extract = handler.extractItem(i, Integer.MAX_VALUE, true);
			if (extract.isEmpty()) { continue; }

			// 燃料なら燃料スロットにそれ以外は精錬スロットへ
			IItemHandler inv = this.isItemFuel(extract) ? this.fuelInv : this.inputInv;
			ItemStack remainder = ItemHandlerHelper.insertItemStacked(inv, extract, true);
			int success = extract.getCount() - remainder.getCount();

			// 数が1以上なら突っ込む
			if (success > 0) {
				ItemStack insert = handler.extractItem(i, success, false);
				ItemStack result = ItemHandlerHelper.insertItemStacked(inv, insert, false);
				assert result.isEmpty();
			}
		}
	}

	// MFを貯める
	public void chargeMF () {

		// 燃料スロットのアイテムをMFに変換
		ItemStack fuelStack = this.getFuelItem();
		int chargeMF = this.getItemBurnTime(fuelStack);
		if (chargeMF <= 0) { return; }

		// MFを貯める
		this.setMF(this.getMF() + chargeMF);
		this.sentClient();
		this.markDirty();

		// アイテムを減らす
		ItemStack copy = fuelStack.copy();
		fuelStack.shrink(1);

		// 減らしてアイテムが1以上なら個数の再設定
		if (this.getFuelItem().isEmpty()) {
			this.fuelInv.setStackInSlot(0, copy.getItem().getContainerItem(copy));
		}
	}

	// 精錬処理
	public void smeltItem() {

		// 精錬アイテムと精錬後のアイテムを取得
		ItemStack smelt = this.inputInv.getStackInSlot(0);
		ItemStack result = FurnaceRecipes.instance().getSmeltingResult(smelt).copy();

		// 鉱石なら倍化
		if (ItemHelper.getOreDictName(smelt).startsWith("ore")) {
			result.grow(result.getCount());
		}

		// スロットに入れる
		ItemHandlerHelper.insertItemStacked(this.outInv, result, false);
		smelt.shrink(1);
	}

	// 精錬可能か
	public boolean canSmelt() {

		// 精錬スロットが空なら終了
		ItemStack toSmelt = this.inputInv.getStackInSlot(0);
		if (toSmelt.isEmpty()) { return false; }

		// 精錬できないなら終了
		ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(toSmelt);
		if (smeltResult.isEmpty()) { return false; }

		// 精錬後のスロットに入れれるか
		ItemStack currentSmelt = this.outInv.getStackInSlot(this.outInv.getSlots() - 1);
		if (currentSmelt.isEmpty()) { return true; }
		if (!smeltResult.isItemEqual(currentSmelt)) { return false; }

		int result = currentSmelt.getCount() + smeltResult.getCount();
		return result <= currentSmelt.getMaxStackSize();
	}

	// 精錬の進捗ゲージ
	public int getCookProgressScaled(int value) {
		return (this.furnaceCookTime + (!this.isMfEmpty() && this.canSmelt() ? 1 : 0)) * value / this.ticksBeforeSmelt;
	}

	// 鉱石なら倍化
	public void oreGrow (ItemStack ore) {
		ore.grow(ore.getCount());
	}

	// ブロックの置き換え
	public void changeBlock () {

		ItemStack stack = this.inputInv.getStackInSlot(0);
		IBlockState state = this.getState(this.pos);
		MFFurnace block = (MFFurnace) state.getBlock();
        TileEntity tile = this;

        // 精錬スロットに何もないなら火を消す
		if (stack.isEmpty() && block == BlockInit.mffurnace_on) {
			block.setBlock = true;
			this.setBlock(state, BlockInit.mffurnace_off, block);
			block.setBlock = false;
		}

		// 精錬スロットに何かあるなら火をつける
		else if (!stack.isEmpty() && block == BlockInit.mffurnace_off) {
			block.setBlock = true;
			this.setBlock(state, BlockInit.mffurnace_on, block);
			block.setBlock = false;
		}

		// タイルえんちちーの引き継ぎ
		if (tile != null) {
            tile.validate();
            this.world.setTileEntity(this.pos, tile);
        }
	}

//	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int value) {
		return this.getMF() * value / this.ticksBeforeSmelt;
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
	}

	// インベントリの数
	protected int getInvSize() {
		return 9;
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

	// 消費MFの取得
	public int getUseMF () {
		return this.useMF;
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
