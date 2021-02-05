package sweetmagic.api.iblock;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.item.sm.magic.MFItem;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.packet.TileMFBlockPKT;

public interface IMFBlock {

	// MFの取得
	int getMF ();

	// MFの設定
	void setMF (int magiaFlux);

	// 最大MF量を取得
	int getMaxMF ();

	// 最大MF量に達していなかったらtrueを返す
	default boolean canMFChange () {
		return this.getMF() < this.getMaxMF();
	}

	// MFを他のブロックに移し替えるときの処理
	default int outputMF (int mf) {

		// 要求MF量が現在持っているMFよりも多い場合
		if (mf > this.getMF()) {
			mf = this.getMF();
		}
		this.setMF(this.getMF() - mf);
		return mf;
	}

	// アイテムを返す量を計算
	default int outPutItemMF(ItemStack stack) {

		// 残りのMFが入る量の計算
		int mf = this.getMaxMF() - this.getMF();
		int count = 0;

		// 入れる予定のアイテムが残りMFより少なかったら
		if ((mf < this.getItemMF(stack.getItem()) * stack.getCount())) {

			// countにスタック合計MF - 残りMF / アイテム一つのMF
			count = (this.getItemMF(stack.getItem()) * stack.getCount() - mf) / this.getItemMF(stack.getItem());
		}
		return count;
	}

    // APIアイテムかどうかの判定
	default boolean isMFAPIItem (ItemStack stack) {
    	return SlotPredicates.hasMFItem(stack);
    }

	// 吸い込むアイテムのMF量
	default int getItemMF (Item item) {

		int mf = 0;

		// MFアイテムなら
		if (item instanceof MFItem) {
			MFItem mfItem = (MFItem) item;
			mf = mfItem.getMF();

		// APIに登録されたアイテムなら
		} else if (this.isMFAPIItem(new ItemStack(item))) {
			mf = SweetMagicAPI.getMFFromItem(new ItemStack(item));
		}
		return mf;
	}

	// MFが0の時Trueを返す
	default boolean isMfEmpty() {
		return this.getMF() <= 0;
	}

	// MFゲージの描画量を計算するためのメソッド
	default int getMfProgressScaled(int value) {

		int mf = 0;

		if (!this.isMfEmpty()) {
			return Math.min(value, (int) (value * this.getMF() / this.getMaxMF()));
		}

		return mf;
    }

	// 座標リストの取得
	Set<BlockPos> getPosList();

	// 座標リストの追加
	void addPosList(BlockPos pos);

	// 送受信処理
	default void sendRecivehandler () {

		World world = this.getTileWorld();

		for (BlockPos tilePos : this.getPosList()) {

			TileEntity tile = world.getTileEntity(tilePos);
			if (!(tile instanceof IMFBlock)) { continue; }

			IMFBlock mfBase = (IMFBlock) tile;

			// 受け取り側
			if (this.getReceive()) {

				// MFブロックからMFを入れるときの処理
				this.insetMF(this , mfBase, this.getTickTime());
			}
		}
	}

	// MFブロックからMFを入れるときの処理
	default void insetMF (IMFBlock reci, IMFBlock tran, int tickTime) {

		int reciMF = reci.getMF();
		int maxMF = reci.getMaxMF() <= 0 ? 100000 : reci.getMaxMF();
		int tranMF = tran.getMF();
		int useMF = this.getUseMF();

		// 最大MFを超えているまたはMFが空なら終了
		if (reciMF >= maxMF || tranMF <= 0) { return; }

		// 送る側のMFが一定値より少ないなら
		if (useMF > tranMF) {
			reci.setMF(reci.getMF() + tranMF);
			PacketHandler.sendToClient(new TileMFBlockPKT (tickTime, 0, reci.getMF(), reci.getTilePos()));

			tran.setMF(0);
			PacketHandler.sendToClient(new TileMFBlockPKT (tickTime, 0, tran.getMF(), tran.getTilePos()));
		}

		// 一定値より多いなら
		else {

			reci.setMF(reciMF + useMF);
			PacketHandler.sendToClient(new TileMFBlockPKT (tickTime, 0, reci.getMF(), reci.getTilePos()));

			tran.setMF(tranMF - useMF);
			PacketHandler.sendToClient(new TileMFBlockPKT (tickTime, 0, tran.getMF(), tran.getTilePos()));
		}
	}

	default int getUseMF () {
		return 1000;
	}

	// 受け取り側かどうかの取得
	boolean getReceive ();

	// 受け取り側かどうかの設定
	void setReceive (boolean isReceive);

	// List<BlockPos>をnbt保存
	default NBTTagCompound savePosList (NBTTagCompound nbt, Set<BlockPos> posList, String name) {

		// NULLチェックとListの個数を確認
		if (posList != null && !posList.isEmpty()) {

			// nbtのリストを作成
			NBTTagList tagsList = new NBTTagList();

			// リストの分だけ回してNBTに保存
			for (BlockPos pos : posList) {

				if (pos == null) { continue; }

				// 座標をXYZごとに保存
                NBTTagCompound tags = new NBTTagCompound();
                tags.setInteger("X", pos.getX());
                tags.setInteger("Y", pos.getY());
                tags.setInteger("Z", pos.getZ());

                // nbtリストにnbtを入れる
                tagsList.appendTag(tags);
			}

			// nbtを持っているなら
			if (!tagsList.hasNoTags()) {

				// NBTに保存
				nbt.setTag(name, tagsList);
			}
		}

		return nbt;
	}

	// nbtを呼び出してList<BlockPos>に突っ込む
	default Set<BlockPos> loadAllPos(NBTTagCompound nbt, String name) {

		// nbtを受け取りnbtリストを作成
		NBTTagList tagsList = nbt.getTagList(name, 10);
		Set<BlockPos> list = new HashSet<BlockPos>();

		// nbtリスト分だけ回す
		for (int i = 0; i < tagsList.tagCount(); ++i) {

			// nbtリストの中にあるnbtを取り出す
			NBTTagCompound tags = tagsList.getCompoundTagAt(i);

			// 座標リストに入れる
			list.add(new BlockPos(tags.getInteger("X"), tags.getInteger("Y"), tags.getInteger("Z")));
		}

		return list;
	}

	// 座標の取得
	BlockPos getTilePos ();

	World getTileWorld ();

	// 経過時間の取得
	int getTickTime();

	// 経過時間の設定
	void setTickTime(int tickTime);
}
