package sweetmagic.init.tile.magic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEnchantedBook;
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
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileAetherEnchantTable extends TileMFBase {

	public int maxMagiaFlux = 100000; 	// 最大MF量を設定
	public int needMF = 15000;
	public final int needTick = 90;
	public int nowTick_L = 0;
	public int nowTick_C = 0;
	public int nowTick_R = 0;

	// 杖スロット
	public final ItemStackHandler wandInv = new InventoryWoodChest(this, this.getInvSize()) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

	// 出力スロット
	public final ItemStackHandler outInv = new InventoryWoodChest(this, 27) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

	private final IItemHandlerModifiable output = new WrappedItemHandler(this.outInv, WrappedItemHandler.WriteMode.OUT);
	private final IItemHandler side = new CombinedInvWrapper(this.wandInv);
	private final CombinedInvWrapper join = new CombinedInvWrapper(this.wandInv);

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
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.output);
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.side);
				}
			}
		}

		return super.getCapability(cap, side);
	}

	@Override
	public void update() {

		super.update();

		// 0.5秒に１度だけ更新
		if (this.getTime() % 10 != 0 || !this.canEnchant() || !this.isActive(this.pos)) { return; }

		// アイテムが空でないなら
		if (!this.getWandItem(0).isEmpty()) {

			ItemStack stack = this.getWandItem(0);
			if (!this.checkEncha(stack)) { return; }

			this.nowTick_L++;

			// 必要な時間を満たしたらアイテムのエンチャント
			if (this.nowTick_L >= this.needTick) {
				this.nowTick_L = this.enchantItem(stack, 0, this.nowTick_L);
			}
		}

		// アイテムが空でないなら
		if (!this.getWandItem(1).isEmpty()) {

			ItemStack stack = this.getWandItem(1);
			if (!this.checkEncha(stack)) { return; }

			this.nowTick_C++;

			// 必要な時間を満たしたらアイテムのエンチャント
			if (this.nowTick_C >= this.needTick) {
				this.nowTick_C = this.enchantItem(stack, 1, this.nowTick_C);
			}
		}

		// アイテムが空でないなら
		if (!this.getWandItem(2).isEmpty()) {

			ItemStack stack = this.getWandItem(2);
			if (!this.checkEncha(stack)) { return; }

			this.nowTick_R++;

			// 必要な時間を満たしたらアイテムのエンチャント
			if (this.nowTick_R >= this.needTick && !stack.isEmpty()) {
				this.nowTick_R = this.enchantItem(stack, 2, this.nowTick_R);
			}
		}
	}

	// 必要MF以上あるかどうか
	public boolean canEnchant () {
		return this.getMF() >= this.needMF;
	}

	public boolean checkEncha (ItemStack stack) {
		return stack.getItem() == Items.BOOK || stack.isItemEnchantable();
	}

	public int enchantItem (ItemStack stack, int slot, int nowTick) {

		Random rand = this.world.rand;
        List<EnchantmentData> enchaList = EnchantmentHelper.buildEnchantmentList(rand, stack, 30, false);

		if (enchaList.isEmpty() || !this.canEnchant()) { return nowTick; }

        boolean flag = stack.getItem() == Items.BOOK;

		if (flag) {

			ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
			ItemStack out = ItemHandlerHelper.insertItemStacked(this.outInv, book, true);
			if (!out.isEmpty()) { return nowTick; }

			if (this.isSever()) {
				stack.shrink(1);
				stack = book;
				ItemHandlerHelper.insertItemStacked(this.outInv, book, false);
			}
		}

		// 本以外なら
		else {
			ItemStack out = ItemHandlerHelper.insertItemStacked(this.outInv, stack, true);
			if (!out.isEmpty()) { return nowTick; }

			if (this.isSever()) {
				ItemStack copy = stack.copy();
				ItemHandlerHelper.insertItemStacked(this.outInv, copy, false);
				stack.shrink(1);
				stack = copy;
			}
		}

		if (!this.isSever()) { return 0; }

		for (EnchantmentData encha : enchaList) {

			if (flag) {
				ItemEnchantedBook.addEnchantment(stack, encha);
			}

			else {
				stack.addEnchantment(encha.enchantment, encha.enchantmentLevel);
			}
		}

		this.setMF(this.getMF() - this.needMF);
		this.sentClient();
		this.playSound(this.pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, rand.nextFloat() * 0.1F + 0.9F);

		return 0;
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("wand", this.wandInv.serializeNBT());
		tags.setTag("outInv", this.outInv.serializeNBT());
		tags.setInteger("nowTick_L", this.nowTick_L);
		tags.setInteger("nowTick_C", this.nowTick_C);
		tags.setInteger("nowTick_R", this.nowTick_R);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.wandInv.deserializeNBT(tags.getCompoundTag("wand"));
		this.outInv.deserializeNBT(tags.getCompoundTag("outInv"));
		this.nowTick_L = tags.getInteger("nowTick_L");
		this.nowTick_C = tags.getInteger("nowTick_C");
		this.nowTick_R = tags.getInteger("nowTick_R");
	}

	// インベントリの数
	public int getInvSize() {
		return 3;
	}

	// 杖スロットの取得
	public IItemHandler getWand() {
		return this.wandInv;
	}

	// 杖スロットのアイテムを取得
	public  ItemStack getWandItem(int i) {
		return this.getWand().getStackInSlot(i);
	}

	// 出力スロットの取得
	public IItemHandler getOut() {
		return this.outInv;
	}

	// 出力スロットのアイテムを取得
	public ItemStack getOutItem(int i) {
		return this.getOut().getStackInSlot(i);
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 10000;
    }

	// MFゲージの描画量を計算するためのメソッド
	public int getProgressScaled(int value, int now, int max) {
		return Math.min(value, (int) (value * (float) (now) / (float) (max)));
    }
}
