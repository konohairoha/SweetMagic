package sweetmagic.api.iitem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.item.sm.magic.MFItem;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.packet.TileMFBlockPKT;

public interface IMFTool {

	// NBT用の変数
	public static final String MF = "mf";					// MF

	// nbt初期化用
	default NBTTagCompound getNBT (ItemStack stack) {

		NBTTagCompound tags = stack.getTagCompound();

		// 初期化
		if (tags == null) {
			stack.setTagCompound(new NBTTagCompound());
			tags = stack.getTagCompound();
		}

		// MFを持っているかどうか
		if (!tags.hasKey(MF)) {
			tags.setInteger(MF, 0);
		}

		return tags;
	}

  	// MFを取得
	default int getMF (ItemStack stack) {
    	return this.getNBT(stack).getInteger(MF);
  	}

  	// MFを設定
	default void setMF (ItemStack stack, int expValue) {
  		this.getNBT(stack).setInteger(MF, Math.max(0, expValue));
  	}

	// 最大MFを取得
	int getMaxMF (ItemStack stack);

	// 最大MFを設定
	void setMaxMF (int maxMF);

	// MFが最大かどうか
	default boolean isMaxMF (ItemStack stack) {
		return this.getMF(stack) >= this.getMaxMF(stack);
	}

	// ゲージ計算取得用
	default int getMfProgressScaled(ItemStack stack, int value) {
		return Math.min(value, (int) (value * this.getMF(stack) / this.getMaxMF(stack)));
	}

	// MFブロックからMFを入れるときの処理
	default void insetMF (ItemStack stack, TileMFTable tile) {

		int mf = this.getMF(stack);
		int useMF = tile.getChargeValue() > tile.getMF() ? tile.getMF() : tile.getChargeValue();
		int sumMF = mf + useMF;

		// 合計MFが最大値より少ない場合
		if (sumMF <= this.getMaxMF(stack)) {
			this.setMF(stack, sumMF);
			tile.setMF(tile.getMF() - useMF);
		}

		// 合計MFが最大値を超える場合
		else {

			int insertMF = this.getMaxMF(stack) - mf;
			this.setMF(stack, mf + insertMF);
			tile.setMF(tile.getMF() - insertMF);
		}

		PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, tile.getMF(), tile.getTilePos()));
	}

	default void insetMF (ItemStack stack, int insertMF) {

		int mf = this.getMF(stack);
		int sumMF = insertMF + mf;

		// 合計MFが最大値より少ない場合
		if (sumMF <= this.getMaxMF(stack)) {
			this.setMF(stack, sumMF);
		}

		// 合計MFが最大値を超える場合
		else {

			int setMF = this.getMaxMF(stack) - mf;
			this.setMF(stack, mf + setMF);
		}
	}

	// MFが空かどうか
	default boolean isEmpty (ItemStack stack) {
		return this.getMF(stack) <= 0;
	}

	// 吸い込むアイテムのMF量
	default int getItemMF (ItemStack stack) {

		int mf = 0;
		int amount = stack.getCount();
		Item item = stack.getItem();

		// MFアイテムなら
		if (item instanceof MFItem) {
			mf = ((MFItem) item).getMF();
		}

		// APIに登録されたアイテムなら
		else if (this.isMFAPIItem(new ItemStack(item))) {
			mf = SweetMagicAPI.getMFFromItem(new ItemStack(item));
		}

		return mf * amount;
	}

    // APIアイテムかどうかの判定
	default boolean isMFAPIItem (ItemStack stack) {
    	return SlotPredicates.hasMFItem(stack);
    }
}
