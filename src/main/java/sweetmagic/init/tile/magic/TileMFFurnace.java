package sweetmagic.init.tile.magic;

import java.util.ArrayList;
import java.util.List;

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

public class TileMFFurnace extends TileMFBase {

	public final ItemStackHandler inputInv = new StackHandler(this, this.getInvSize());
	private final ItemStackHandler outInv = new StackHandler(this, this.getInvSize());
	private final ItemStackHandler fuelInv = new StackHandler(this, 1);
	public int maxMagiaFlux = 200000;
	public int needMF = 20;
	public int maxSmeltTime = 60;
	public int smeltTime = 0;

	private final IItemHandlerModifiable autoInput = new WrappedItemHandler(this.inputInv, WrappedItemHandler.WriteMode.IN) {

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.SMELTABLE.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
		}
	};

	private final IItemHandlerModifiable autoFuel = new WrappedItemHandler(this.fuelInv, WrappedItemHandler.WriteMode.IN) {
		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.MFF_FUEL.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
		}
	};

	private final IItemHandlerModifiable autoOut = new WrappedItemHandler(this.outInv, WrappedItemHandler.WriteMode.OUT);
	private final IItemHandler autoSide = new CombinedInvWrapper(this.autoFuel, this.autoOut);
	private final CombinedInvWrapper join = new CombinedInvWrapper(this.autoInput, this.autoFuel, this.autoOut);

	@Override
	public void update() {

		super.update();

		// 上のチェストから自動でアイテム取り寄せ
		this.pullFromChest();

		// 1秒に一回実行
		if (this.getTime() % 20 == 0 && !this.world.isRemote) {

			// MFが満タンじゃないかつ精錬スロットが空以外ならMFをチャージ
			if (this.canMFChange() && !this.getFuelItem().isEmpty()) {
				this.mfCharge();
			}

//			this.changeBlock();
		}

		// MFがないなら終了
		if (this.isMfEmpty()) { return; }

		// 投入スロットの整理して投入スロットが空なら終了
		ItemHelper.compactInventory(this.inputInv);
		if (this.getInputItem(0).isEmpty()) { return; }

		// 必要MF以上あるなら
		if (this.getMF() >= this.getNeedMF()) {

			// 精錬可能かつ精錬時間が最大に達したら精錬
			if (this.incrementSmeltTime() /*&& !this.world.isRemote*/ && this.getSmeltTime() >= this.getMaxSmeltTime()) {
				this.smeltItem();
			}
		}
	}

	// 上のチェストから自動でアイテム取り寄せ
	public void pullFromChest() {

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
			IItemHandler inv = this.isMFItem(extract) ? this.fuelInv : this.inputInv;
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

	// MFチャージ
	public void mfCharge () {

		// 精錬スロットのアイテムを取得してMFを持ってないなら終了
		ItemStack stack = this.getFuelItem();
		if (!this.isMFItem(stack)) { return; }

		// 燃焼アイテムのMFを取得してMFに加算してクライアント通知
		this.setMF(this.getMF() + this.getItemBurnTime(stack));
		this.sentClient();

		stack.shrink(1);
	}

	// 精錬時間を加算
	public boolean incrementSmeltTime () {

		// 精錬時間を満たしてるなら終了
		if (this.getSmeltTime() >= this.getMaxSmeltTime()) { return true; }

		// 投入アイテムの取得して精錬できるアイテムかを確認
		ItemStack stack = this.getInputItem(0);
		ItemStack smeltStack = FurnaceRecipes.instance().getSmeltingResult(stack).copy();
		if (smeltStack.isEmpty()) { return false; }

		// 精錬時間を加算
		this.setSmeltTime(this.getSmeltTime() + 1);

		return true;
	}

	// アイテムの精錬
	public void smeltItem () {

		// 投入アイテムの取得して精錬できるアイテムかを確認
		ItemStack stack = this.getInputItem(0);
		ItemStack smeltStack = FurnaceRecipes.instance().getSmeltingResult(stack).copy();

		// 鉱石なら倍加
		if (ItemHelper.getOreDictName(stack).startsWith("ore")) {
			this.oreGrow(smeltStack);
		}

		ItemStack resultStack = ItemHandlerHelper.insertItemStacked(this.outInv, smeltStack, false);
		if (!resultStack.isEmpty()) { return; }

		// MFの消費とクライアント通知
		if (!this.world.isRemote) {
			this.setMF(this.getMF() - this.getNeedMF());
			this.sentClient();
		}

		// 精錬時間の初期化と投入アイテムの消費
		this.setSmeltTime(0);
		stack.shrink(1);
		ItemHelper.compactInventory(this.outInv);
	}

	// 鉱石倍化
	public void oreGrow (ItemStack ore) {
		ore.grow(ore.getCount());
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		if (this.getMF() != 0) { tags.setInteger("magiaFlux", this.getMF()); }
		tags.setShort("BurnTime", (short) this.getMF());
		tags.setShort("CookTime", (short) this.smeltTime);
		tags.setTag("Input", this.inputInv.serializeNBT());
		tags.setTag("Output", this.outInv.serializeNBT());
		tags.setTag("Fuel", this.fuelInv.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		this.setMF(tags.getInteger("magiaFlux"));
		this.setMF(tags.getShort("BurnTime"));
		this.smeltTime = tags.getShort("CookTime");
		this.inputInv.deserializeNBT(tags.getCompoundTag("Input"));
		this.outInv.deserializeNBT(tags.getCompoundTag("Output"));
		this.fuelInv.deserializeNBT(tags.getCompoundTag("Fuel"));
	}

	// インベントリの数
	protected int getInvSize() {
		return 9;
	}

	// 投入スロットの取得
	public IItemHandler getFuel() {
		return this.fuelInv;
	}

	// 精錬スロットのアイテム取得
	private ItemStack getFuelItem() {
		return this.fuelInv.getStackInSlot(0);
	}

	// 投入スロットの取得
	public IItemHandler getInput() {
		return this.inputInv;
	}

	// 投入スロットのアイテム取得
	public ItemStack getInputItem(int i) {
		return this.getInput().getStackInSlot(i);
	}

	// 出力スロットの取得
	public IItemHandler getOutput() {
		return this.outInv;
	}

	// 出力スロットのアイテム取得
	public ItemStack getOutputItem(int i) {
		return this.getOutput().getStackInSlot(i);
	}

	// 消費MFの取得
	public int getNeedMF () {
		return this.needMF;
	}

	// MFを持っているかどうか
	public boolean isMFItem (ItemStack stack) {
		return SlotPredicates.isMFItem(stack);
	}

	// ブロックの置換
	public void setBlock (IBlockState state, Block setBlock, MFFurnace face) {
		this.world.setBlockState(this.pos, setBlock.getDefaultState().withProperty(face.FACING, state.getValue(MFFurnace.FACING)), 2);
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

	// 精錬にかかる時間
	public int getMaxSmeltTime () {
		return this.maxSmeltTime;
	}

	// 現在の精錬時間を取得
	public int getSmeltTime () {
		return this.smeltTime;
	}

	// 精錬時間を設定
	public void setSmeltTime (int smeltTime) {
		this.smeltTime = smeltTime;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 5000;
    }

	// ブロックの置き換え
	public void changeBlock () {

		ItemStack stack = this.getInputItem(0);
		IBlockState state = this.getState(this.pos);
		MFFurnace block = (MFFurnace) state.getBlock();
        TileEntity tile = this.world.getTileEntity(this.pos);

        // 精錬スロットに何もないなら火を消す
		if (stack.isEmpty() && block == BlockInit.mffurnace_on) {
			block.setBlock = true;
			this.setBlock(state, BlockInit.mffurnace_off, block);
			block.setBlock = false;
			this.setTile(tile);
		}

		// 精錬スロットに何かあるなら火をつける
		else if (!stack.isEmpty() && block == BlockInit.mffurnace_off) {
			block.setBlock = true;
			this.setBlock(state, BlockInit.mffurnace_on, block);
			block.setBlock = false;
			this.setTile(tile);
		}
	}

	// タイルえんちちーの引き継ぎ
	public void setTile (TileEntity tile) {
		if (tile != null) {
            tile.validate();
            this.world.setTileEntity(this.pos, tile);
        }
	}

	// MFゲージの描画量を計算するためのメソッド
	public int getSmeltTimeScaled(int value) {

		if (this.getSmeltTime() > 0) {
			return Math.min(value, (int) (value * (float) (this.getSmeltTime()) / (float) (this.getMaxSmeltTime())));
		}

		return 0;
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

	@Override
	public List<ItemStack> getList() {

		List<ItemStack> stackList = new ArrayList<>();

		for (int i = 0; i < this.getInvSize(); i++) {
			stackList.add(this.getInputItem(i));
			stackList.add(this.getOutputItem(i));
		}

		stackList.add(this.getFuelItem());
		return stackList;
	}
}
