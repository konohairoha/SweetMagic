package sweetmagic.init.tile.magic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.init.tile.slot.StackHandler;

public class TileMFTable extends TileMFBase {

	public int maxMagiaFlux = 100000; 	// 最大MF量を設定
	public int useMF = 5000; 			// 一度に補給できるMF;
	public BlockPos viewPos = this.pos;
	public int slotSize = 1;

	// 杖スロット
	public final ItemStackHandler wandInventory = new StackHandler(this, this.getInvSize()) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

    // MFアイテムのスロット
	public final ItemStackHandler inputInventory = new StackHandler(this, 1) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

	@Override
	public void serverUpdate() {

		super.serverUpdate();

		// 一定時間経てば処理
		if (this.getTime() % 20 != 0) { return; }

		// 最大値に達していないなら
		if (this.canMFChange()) {
			this.smeltItem();
		}

		// MFが空ではないなら杖にMFを入れる
		if (!this.isMfEmpty()) {
			this.wandChargeMF();
		}

		this.markDirty();
	}

	// インプットスロットのMFを取得
	public boolean smeltItem () {

		ItemStack stack = this.getInputItem();
		if (stack.isEmpty()) { return false; }

		// 燃焼アイテムのMFを取得してMFに加算する
		this.setMF(this.getMF() + this.getItemBurnTime(stack));
		stack.shrink(1);
		this.sentClient();

		return true;
	}

	// 杖スロットにある杖にMFを入れる
	public void wandChargeMF () {

		for (int i = 0; i < this.getWand().getSlots(); i++) {

			// 杖スロットが空以外なら次へ
			ItemStack stack = this.getWandItem(i);
			if(stack.isEmpty() || this.wandMaxMF(stack) || this.isMfEmpty()){ continue; }

			// 杖にMFを補給
			IMFTool wand = (IMFTool) stack.getItem();
			wand.insetMF(stack, this);

			// MFが最大になったときに通知
			if (this.wandMaxMF(stack)) {
				if (!this.world.isRemote) {
					this.world.playEvent(2003, new BlockPos(this.pos.add(0, 1.75, 0)), 0);
				}
				this.playSound(this.pos, SoundEvents.ENTITY_PLAYER_LEVELUP, 1F, 1F);
			}
		}
	}

	// 杖の杖が最大値を超えているか
	public boolean wandMaxMF (ItemStack stack) {
		IMFTool wand = (IMFTool) stack.getItem();
		return wand.getMF(stack) >= wand.getMaxMF(stack);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("wand", this.wandInventory.serializeNBT());
		tags.setTag("Input", this.inputInventory.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.wandInventory.deserializeNBT(tags.getCompoundTag("wand"));
		this.inputInventory.deserializeNBT(tags.getCompoundTag("Input"));
	}

	// インベントリの数
	public int getInvSize() {
		return this.slotSize;
	}

	// MFスロットの取得
	public IItemHandler getInput() {
		return this.inputInventory;
	}

	// 杖スロットの取得
	public IItemHandler getWand() {
		return this.wandInventory;
	}

	// インプットスロットのアイテムを取得
	public ItemStack getInputItem() {
		return this.getInput().getStackInSlot(0);
	}

	// 杖スロットのアイテムを取得
	public  ItemStack getWandItem(int i) {
		return this.getWand().getStackInSlot(i);
	}

	// 杖に入れるMF量の取得
	public int getChargeValue () {
		return this.useMF;
	}

	@Override
	public List<ItemStack> getList() {

		List<ItemStack> stackList = new ArrayList<>();

		for (int i = 0; i < this.getInvSize(); i++) {
			stackList.add(this.getWandItem(i));
		}

		stackList.add(this.getInputItem());
		return stackList;
	}
}
