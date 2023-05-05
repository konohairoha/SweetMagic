package sweetmagic.api.magiaflux;

import net.minecraft.item.ItemStack;
import sweetmagic.api.SweetMagicAPI;

public class MagiaFluxInfo {

	public ItemStack mItem;
	public int magiaflux;

	// 初期化用
	public MagiaFluxInfo(){
		this.mItem = null;
		this.magiaflux = 0;
	}

	// 削除用
	public MagiaFluxInfo(ItemStack item){
		this.mItem = item;
		this.magiaflux = 0;
	}

	// 追加用
	public MagiaFluxInfo(ItemStack item, int mf){
		this.mItem = item;
		this.magiaflux = mf;
	}

	public ItemStack getItem() {
		return this.mItem;
	}

	public int getMF() {
		return this.magiaflux;
	}

	// アイテムにMFを定義
	public void setMF(MagiaFluxInfo info) {
		if(info.getItem().isEmpty()) { return; }

		for(MagiaFluxInfo list : SweetMagicAPI.mfList) {
			if(list.getItem().isItemEqual(info.getItem())) {
				// すでにそのアイテムが定義されてたら異常
				throw new IllegalArgumentException("The item has already been defined.    item:" + info.getItem() + "    mf:" + info.getMF());
			}
		}

		// 対象のMFが1を下回ったら異常
		if(info.getMF() < 1) {
			throw new IllegalArgumentException("The target Magia Flux is an invalid value.    item:" + info.getItem() + "    mf:" + info.getMF());
		}

		//チェック終了後、リストに入れる
		SweetMagicAPI.mfList.add(info);
	}

	// MFを定義したアイテム情報を削除
	public void deleteMF(MagiaFluxInfo info) {

		if(info.getItem().isEmpty()) { return; }

		int i = 0;
		boolean deleteFlg = false;

		for(MagiaFluxInfo list : SweetMagicAPI.mfList) {

			if(list.getItem().isItemEqual(info.getItem())) {
				deleteFlg = true;
				break;
			}

			else {
				i++;
			}
		}

		// チェック終了後、削除できるなら要素削除
		if(deleteFlg) {
			SweetMagicAPI.mfList.remove(i);
		}
	}
}
