package sweetmagic.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.recipe.NormalRecipeInfo;
import sweetmagic.api.recipe.alstroemeria.AlstroemeriaRecipeInfo;
import sweetmagic.api.recipe.fermenter.FermenterRecipeInfo;
import sweetmagic.api.recipe.flourmill.FlourMillRecipeInfo;
import sweetmagic.api.recipe.freezer.FreezerRecipeInfo;
import sweetmagic.api.recipe.juicemaker.JuiceMakerRecipeInfo;
import sweetmagic.api.recipe.mftable.MFTableRecipeInfo;
import sweetmagic.api.recipe.obmagia.ObMagiaRecipeInfo;
import sweetmagic.api.recipe.oven.OvenRecipeInfo;
import sweetmagic.api.recipe.pan.PanRecipeInfo;
import sweetmagic.api.recipe.pedal.PedalRecipeInfo;
import sweetmagic.api.recipe.pot.PotRecipeInfo;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.sweetmagic.SMBucket;

public class RecipeHelper {

	/**
	 * アルストロメリアレシピを満たしていたらアイテム情報リストが返ってくる欲張りセットメソッド
	 * handでフィルタをかけ、inv内のアイテムを探索する
	 * @param hand	手に持っているアイテム
	 * @param inv		プレイヤーのインベントリ
	 * @return
	 */

	public static AlstroemeriaRecipeInfo getAlstroemeriaRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {

		// アイテム情報リスト
		List<Object[]> itemInfos = new ArrayList<Object[]>();
		//レシピ情報
		AlstroemeriaRecipeInfo recipeInfo = new AlstroemeriaRecipeInfo();

		/**
		 * まどメモ
		 * stream().filter(T -> ( 条件 ) )
		 * または
		 * stream().forEach( T -> { ( 処理 ) } )
		 * このTにはListのクラス自体(インスタンス？)が返ってきます
		 *
		 * ■List.stream().filter().forEach();　がまるごと一つのメソッドとして内部で動いちゃいます！
		 * ※forEachはcontinue、breakが使えない、Listの次または前みたいな操作ができないなどの制約があるのでFor文とは違います　気を付けましょう
		 *
		 */

		// 大分類
		SweetMagicAPI.alsRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe -> {

			// レシピ検索が終わってたら終了
			if (recipeInfo.canComplete) { return; }

			// List<List<ItemStack>> から取り出す（中分類）
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 中分類を回すたびにレシピチェック成功フラグの初期化
				boolean successFlg = false;

				// 鉱石辞書にある分取り出す（小分類）
				for (ItemStack stack : oreList) {

					// アイテム情報をインベントリから取り出せなかったら次の小分類へ
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());
					if (itemInfo == null) { continue; }

					// レシピチェック成功
					successFlg = true;

					// レシピアイテムと保存できたアイテム情報の数が一致しなければ次の中分類へ
					itemInfos.add(itemInfo);
					if (itemInfos.size() != recipe.getInputList().size()) { break; }

					// 数が揃ったら中分類から抜け出す
					recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
					recipeInfo.canComplete = true;
					return;

				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック（大分類）処理に移行
				if(itemInfos.isEmpty() || !successFlg) {
					itemInfos.clear();
					return;
				}
			}
		});

		// アイテムリストが残ってたらアイテムリストを返す　なかったらcanComplete = Falseで返す
		return !itemInfos.isEmpty() ? recipeInfo : new AlstroemeriaRecipeInfo();
	}

	// 創造の台座
	public static PedalRecipeInfo getPedalRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {

		// アイテム情報リスト
		List<Object[]> itemInfos = new ArrayList<Object[]>();
		//レシピ情報
		PedalRecipeInfo recipeInfo = new PedalRecipeInfo();

		// 大分類
		SweetMagicAPI.pedalRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe -> {

			// レシピ検索が終わってたら終了
			if (recipeInfo.canComplete) { return; }

			// List<List<ItemStack>> から取り出す（中分類）
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 中分類を回すたびにレシピチェック成功フラグの初期化
				boolean successFlg = false;

				// 鉱石辞書にある分取り出す（小分類）
				for (ItemStack stack : oreList) {

					// アイテム情報をインベントリから取り出せなかったら次の小分類へ
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());
					if (itemInfo == null) { continue; }

					// レシピチェック成功
					successFlg = true;

					// レシピアイテムと保存できたアイテム情報の数が一致しなければ次の中分類へ
					itemInfos.add(itemInfo);
					if (itemInfos.size() != recipe.getInputList().size()) { break; }

					// 数が揃ったら中分類から抜け出す
					recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack(), recipe.keepTag);
					recipeInfo.canComplete = true;
					return;

				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック（大分類）処理に移行
				if(itemInfos.isEmpty() || !successFlg) {
					itemInfos.clear();
					return;
				}
			}
		});

		// アイテムリストが残ってたらアイテムリストを返す　なかったらcanComplete = Falseで返す
		return !itemInfos.isEmpty() ? recipeInfo : new PedalRecipeInfo();
	}

	public static FlourMillRecipeInfo getFlourMillRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {

		//レシピ情報
		FlourMillRecipeInfo recipeInfo = new FlourMillRecipeInfo();

		SweetMagicAPI.millRecipe.stream().filter(recipe -> ItemHelper.checkHandItem(recipe.getHandList(), hand)).forEach(recipe ->
		{

			//手に持ってるアイテムで引っかかったらレシピ情報を
			recipeInfo.setRecipeInfo(recipe.getHandList(), recipe.getOutputItemStack());
			recipeInfo.canComplete = true;
		});

		// レシピリストが残ってたらアイテムリストを返す　なかったらcanComplete = Falseで返す
		return recipeInfo.canComplete ? recipeInfo : new FlourMillRecipeInfo();
	}

	public static OvenRecipeInfo getOvenRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {

		// アイテム情報リスト
		List<Object[]> itemInfos = new ArrayList<Object[]>();
		//レシピ情報
		OvenRecipeInfo recipeInfo = new OvenRecipeInfo();

		// 大分類
		SweetMagicAPI.ovenRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe -> {

			// レシピ検索が終わってたら終了
			if (recipeInfo.canComplete) { return; }

			// List<List<ItemStack>> から取り出す（中分類）
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 中分類を回すたびにレシピチェック成功フラグの初期化
				boolean successFlg = false;

				// 鉱石辞書にある分取り出す（小分類）
				for (ItemStack stack : oreList) {

					// アイテム情報をインベントリから取り出せなかったら次の小分類へ
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());
					if (itemInfo == null) { continue; }

					// レシピチェック成功
					successFlg = true;

					// レシピアイテムと保存できたアイテム情報の数が一致しなければ次の中分類へ
					itemInfos.add(itemInfo);
					if (itemInfos.size() != recipe.getInputList().size()) { break; }

					// 数が揃ったら中分類から抜け出す
					recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
					recipeInfo.canComplete = true;
					return;

				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック（大分類）処理に移行
				if(itemInfos.isEmpty() || !successFlg) {
					itemInfos.clear();
					return;
				}
			}
		});

		// アイテムリストが残ってたらアイテムリストを返す　なかったらcanComplete = Falseで返す
		return !itemInfos.isEmpty() ? recipeInfo : new OvenRecipeInfo();
	}

	public static FermenterRecipeInfo getFermenterRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {

		// アイテム情報リスト
		List<Object[]> itemInfos = new ArrayList<Object[]>();
		//レシピ情報
		FermenterRecipeInfo recipeInfo = new FermenterRecipeInfo();

		// 大分類
		SweetMagicAPI.fermenterRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe -> {

			// レシピ検索が終わってたら終了
			if (recipeInfo.canComplete) { return; }

			// List<List<ItemStack>> から取り出す（中分類）
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 中分類を回すたびにレシピチェック成功フラグの初期化
				boolean successFlg = false;

				// 鉱石辞書にある分取り出す（小分類）
				for (ItemStack stack : oreList) {

					// アイテム情報をインベントリから取り出せなかったら次の小分類へ
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());
					if (itemInfo == null) { continue; }

					// レシピチェック成功
					successFlg = true;

					// レシピアイテムと保存できたアイテム情報の数が一致しなければ次の中分類へ
					itemInfos.add(itemInfo);
					if (itemInfos.size() != recipe.getInputList().size()) { break; }

					// 数が揃ったら中分類から抜け出す
					recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
					recipeInfo.canComplete = true;
					return;

				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック（大分類）処理に移行
				if(itemInfos.isEmpty() || !successFlg) {
					itemInfos.clear();
					return;
				}
			}
		});

		// アイテムリストが残ってたらアイテムリストを返す　なかったらcanComplete = Falseで返す
		return !itemInfos.isEmpty() ? recipeInfo : new FermenterRecipeInfo();
	}

	public static JuiceMakerRecipeInfo getJuiceMakerRecipeInfo(ItemStack hand, List<ItemStack> inv) {

		// アイテム情報リスト
		List<Object[]> itemInfos = new ArrayList<Object[]>();
		//レシピ情報
		JuiceMakerRecipeInfo recipeInfo = new JuiceMakerRecipeInfo();

		// 大分類
		SweetMagicAPI.juiceRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe -> {

			// レシピ検索が終わってたら終了
			if (recipeInfo.canComplete) { return; }

			// List<List<ItemStack>> から取り出す（中分類）
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 中分類を回すたびにレシピチェック成功フラグの初期化
				boolean successFlg = false;

				// 鉱石辞書にある分取り出す（小分類）
				for (ItemStack stack : oreList) {

					// アイテム情報をインベントリから取り出せなかったら次の小分類へ
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());
					if (itemInfo == null) { continue; }

					// レシピチェック成功
					successFlg = true;

					// レシピアイテムと保存できたアイテム情報の数が一致しなければ次の中分類へ
					itemInfos.add(itemInfo);
					if (itemInfos.size() != recipe.getInputList().size()) { break; }

					// 数が揃ったら中分類から抜け出す
					recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
					recipeInfo.canComplete = true;
					return;

				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック（大分類）処理に移行
				if(itemInfos.isEmpty() || !successFlg) {
					itemInfos.clear();
					return;
				}
			}
		});

		// アイテムリストが残ってたらアイテムリストを返す　なかったらcanComplete = Falseで返す
		return !itemInfos.isEmpty() ? recipeInfo : new JuiceMakerRecipeInfo();
	}

	public static FreezerRecipeInfo getFreezRecipeInfo(ItemStack hand, List<ItemStack> inv) {

		// アイテム情報リスト
		List<Object[]> itemInfos = new ArrayList<Object[]>();
		//レシピ情報
		FreezerRecipeInfo recipeInfo = new FreezerRecipeInfo();

		// 大分類
		SweetMagicAPI.freezRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe -> {

			// レシピ検索が終わってたら終了
			if (recipeInfo.canComplete) { return; }

			// List<List<ItemStack>> から取り出す（中分類）
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 中分類を回すたびにレシピチェック成功フラグの初期化
				boolean successFlg = false;

				// 鉱石辞書にある分取り出す（小分類）
				for (ItemStack stack : oreList) {

					// アイテム情報をインベントリから取り出せなかったら次の小分類へ
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());
					if (itemInfo == null) { continue; }

					// レシピチェック成功
					successFlg = true;

					// レシピアイテムと保存できたアイテム情報の数が一致しなければ次の中分類へ
					itemInfos.add(itemInfo);
					if (itemInfos.size() != recipe.getInputList().size()) { break; }

					// 数が揃ったら中分類から抜け出す
					recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
					recipeInfo.canComplete = true;
					return;

				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック（大分類）処理に移行
				if(itemInfos.isEmpty() || !successFlg) {
					itemInfos.clear();
					return;
				}
			}
		});

		// アイテムリストが残ってたらアイテムリストを返す　なかったらcanComplete = Falseで返す
		return !itemInfos.isEmpty() ? recipeInfo : new FreezerRecipeInfo();
	}

	public static PotRecipeInfo getPotRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {

		// アイテム情報リスト
		List<Object[]> itemInfos = new ArrayList<Object[]>();
		//レシピ情報
		PotRecipeInfo recipeInfo = new PotRecipeInfo();

		SweetMagicAPI.potRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe -> {

			// レシピ検索が終わってたら終了
			if (recipeInfo.canComplete) { return; }

			// List<List<ItemStack>> から取り出す（中分類）
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 中分類を回すたびにレシピチェック成功フラグの初期化
				boolean successFlg = false;

				// 鉱石辞書にある分取り出す（小分類）
				for (ItemStack stack : oreList) {

					// アイテム情報をインベントリから取り出せなかったら次の小分類へ
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());
					if (itemInfo == null) { continue; }

					// レシピチェック成功
					successFlg = true;

					// レシピアイテムと保存できたアイテム情報の数が一致しなければ次の中分類へ
					itemInfos.add(itemInfo);
					if (itemInfos.size() != recipe.getInputList().size()) { break; }

					// 数が揃ったら中分類から抜け出す
					recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
					recipeInfo.canComplete = true;
					return;

				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック（大分類）処理に移行
				if(itemInfos.isEmpty() || !successFlg) {
					itemInfos.clear();
					return;
				}
			}
		});

		// アイテムリストが残ってたらアイテムリストを返す　なかったらcanComplete = Falseで返す
		return !itemInfos.isEmpty() ? recipeInfo : new PotRecipeInfo();
	}

	public static PanRecipeInfo getPanRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {

		// アイテム情報リスト
		List<Object[]> itemInfos = new ArrayList<Object[]>();
		//レシピ情報
		PanRecipeInfo recipeInfo = new PanRecipeInfo();

		SweetMagicAPI.panRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe -> {

			// レシピ検索が終わってたら終了
			if (recipeInfo.canComplete) { return; }

			// List<List<ItemStack>> から取り出す（中分類）
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 中分類を回すたびにレシピチェック成功フラグの初期化
				boolean successFlg = false;

				// 鉱石辞書にある分取り出す（小分類）
				for (ItemStack stack : oreList) {

					// アイテム情報をインベントリから取り出せなかったら次の小分類へ
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());
					if (itemInfo == null) { continue; }

					// レシピチェック成功
					successFlg = true;

					// レシピアイテムと保存できたアイテム情報の数が一致しなければ次の中分類へ
					itemInfos.add(itemInfo);
					if (itemInfos.size() != recipe.getInputList().size()) { break; }

					// 数が揃ったら中分類から抜け出す
					recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
					recipeInfo.canComplete = true;
					return;

				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック（大分類）処理に移行
				if(itemInfos.isEmpty() || !successFlg) {
					itemInfos.clear();
					return;
				}
			}
		});

		// アイテムリストが残ってたらアイテムリストを返す　なかったらcanComplete = Falseで返す
		return !itemInfos.isEmpty() ? recipeInfo : new PanRecipeInfo();
	}

	public static ObMagiaRecipeInfo getObMagiaRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {

		// アイテム情報リスト
		List<Object[]> itemInfos = new ArrayList<Object[]>();
		//レシピ情報
		ObMagiaRecipeInfo recipeInfo = new ObMagiaRecipeInfo();


		SweetMagicAPI.magiaRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe -> {

			// レシピ検索が終わってたら終了
			if (recipeInfo.canComplete) { return; }

			// List<List<ItemStack>> から取り出す（中分類）
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 中分類を回すたびにレシピチェック成功フラグの初期化
				boolean successFlg = false;

				// 鉱石辞書にある分取り出す（小分類）
				for (ItemStack stack : oreList) {

					// アイテム情報をインベントリから取り出せなかったら次の小分類へ
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());
					if (itemInfo == null) { continue; }

					// レシピチェック成功
					successFlg = true;

					// レシピアイテムと保存できたアイテム情報の数が一致しなければ次の中分類へ
					itemInfos.add(itemInfo);
					if (itemInfos.size() != recipe.getInputList().size()) { break; }

					// 数が揃ったら中分類から抜け出す
					recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
					recipeInfo.canComplete = true;
					return;

				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック（大分類）処理に移行
				if(itemInfos.isEmpty() || !successFlg) {
					itemInfos.clear();
					return;
				}
			}
		});

		// アイテムリストが残ってたらアイテムリストを返す　なかったらcanComplete = Falseで返す
		return !itemInfos.isEmpty() ? recipeInfo : new ObMagiaRecipeInfo();
	}

	public static MFTableRecipeInfo getmftableRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {

		// アイテム情報リスト
		List<Object[]> itemInfos = new ArrayList<Object[]>();
		//レシピ情報
		MFTableRecipeInfo recipeInfo = new MFTableRecipeInfo();

		SweetMagicAPI.mfTableRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe -> {

			// レシピ検索が終わってたら終了
			if (recipeInfo.canComplete) { return; }

			// List<List<ItemStack>> から取り出す（中分類）
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 中分類を回すたびにレシピチェック成功フラグの初期化
				boolean successFlg = false;

				// 鉱石辞書にある分取り出す（小分類）
				for (ItemStack stack : oreList) {

					// アイテム情報をインベントリから取り出せなかったら次の小分類へ
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());
					if (itemInfo == null) { continue; }

					// レシピチェック成功
					successFlg = true;

					// レシピアイテムと保存できたアイテム情報の数が一致しなければ次の中分類へ
					itemInfos.add(itemInfo);
					if (itemInfos.size() != recipe.getInputList().size()) { break; }

					// 数が揃ったら中分類から抜け出す
					recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
					recipeInfo.canComplete = true;
					return;

				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック（大分類）処理に移行
				if(itemInfos.isEmpty() || !successFlg) {
					itemInfos.clear();
					return;
				}
			}
		});

		// アイテムリストが残ってたらアイテムリストを返す　なかったらNullで返す
		return !itemInfos.isEmpty() ? recipeInfo : new MFTableRecipeInfo();
	}

	// リストにあるアイテムが一致かつ個数が超えてる
	public static boolean canCraft (List<ItemStack> stackList, ItemStack hand) {
		return ItemHelper.checkHandItem(stackList, hand) && stackList.get(0).getCount() <= hand.getCount();
	}

	// クラフト処理
	public static RecipeUtil recipeAllCraft (NormalRecipeInfo recipeInfo, EntityPlayer player, ItemStack hand) {

		// 完成品はリスト
		List<ItemStack> inputs = new ArrayList<ItemStack>();
		List<ItemStack> results = new ArrayList<ItemStack>();
		ItemStack copy = hand.copy();
		Item handItem = hand.getItem();

		// バケツチェック
		if (isBucket(copy.getItem())) {
			results.add(new ItemStack(Items.BUCKET, copy.getCount()));
		}

		// 水入りバケツ
		else if (handItem == ItemInit.alt_bucket_water) {
			results.add(((SMBucket) handItem).useBucket(hand));
		}

		// レシピに登録されてるメインアイテムの要求数
		int reHandAmount = recipeInfo.getHandList().get(0).getCount();

		// 手に持ってるアイテムの個数
		int shrinkAmount = getRequestAmount(reHandAmount, hand.getCount());

		// クラフトで使うアイテムの取得
		for (Object[] input : recipeInfo.getinputs()) {

			// レシピに登録されてるサブアイテムの要求数
			int reInputAmount = (int)input[2];

			// インベントリの個数
			int inputAmount = player.inventory.mainInventory.get((int) input[0]).getCount();
			int inputShrink = getRequestAmount(reInputAmount, inputAmount);

			// インベントリのほうが消費個数が少ないなら消費個数を減らす
			shrinkAmount = shrinkAmount > inputShrink ? inputShrink : shrinkAmount;
		}

		// クラフトで使うアイテムの取得
		for (Object[] recipe : recipeInfo.getinputs()) {

			// ItemStackの取得して個数設定 + リスト追加
			ItemStack send = ((ItemStack) recipe[1]).copy();
			Item sendItem = send.getItem();
			send.setCount(shrinkAmount * (int) recipe[2]);
			inputs.add(send);

			// バケツチェック
			if (isBucket(sendItem)) {
				results.add(new ItemStack(Items.BUCKET));
			}

			// 水入りバケツ
			else if (sendItem == ItemInit.alt_bucket_water) {
				results.add(((SMBucket) sendItem).useBucket(send));
			}
		}

		// クラフトで出来たアイテムの取得
		for (Object st : recipeInfo.getOutputItems()) {

			// ItemStackの取得して個数設定 + リスト追加
			ItemStack send = ((ItemStack) st).copy();
			int amount = shrinkAmount * send.getCount();

			// 数が0になるまで繰り返し
			while (amount > 0) {
				int input = Math.min(amount, 64);
				amount -= input;
				ItemStack resultItems = send.copy();
				resultItems.setCount(input);
				results.add(resultItems);
			}
		}

		// 減らす数分インベントリから減らす
		SMUtil.decrPInvMinList(player, shrinkAmount, recipeInfo.getinputs());

		//プレイヤーのインベントリに結果を通知
		player.inventoryContainer.detectAndSendChanges();

		// メインハンドの消費量取得
		int shrinkHand = reHandAmount * shrinkAmount;

		// 手に持ってるアイテムも減らす
		hand.shrink(shrinkHand);
		copy.setCount(shrinkHand);

		return new RecipeUtil(recipeInfo, copy, inputs, results);
	}

	// クラフト処理
	public static RecipeUtil recipeSingleCraft (NormalRecipeInfo recipeInfo, EntityPlayer player, ItemStack hand) {

		ItemStack handitem = recipeInfo.getHandList().get(0);
		Item handStack = handitem.getItem();
		ItemStack copy = hand.copy();
		List<ItemStack> inputs = new ArrayList<ItemStack>();
		List<ItemStack> results = new ArrayList<ItemStack>();

		// バケツチェック
		if (isBucket(handStack)) {
			results.add(new ItemStack(Items.BUCKET, handitem.getCount()));
		}

		// 上位魔術書
		else if (handStack == ItemInit.magic_book_cosmic){
			ItemStack book = new ItemStack(ItemInit.magic_book_cosmic);
			book.setTagCompound(ItemHelper.getNBT(handitem));
			results.add(book);
		}

		// 水入りバケツ
		else if (handStack == ItemInit.alt_bucket_water) {
			results.add(((SMBucket) handStack).useBucket(hand));
		}

		// 手に持っているアイテムを処理する
		hand.shrink(handitem.getCount());

		// クラフトで使うアイテムの取得
		for (Object[] recipe : recipeInfo.getinputs()) {

			// ItemStackの取得して個数設定 + リスト追加
			ItemStack send = ((ItemStack) recipe[1]).copy();
			Item sendItem = send.getItem();
			send.setCount((int) recipe[2]);
			inputs.add(send);

			// バケツチェック
			if (isBucket(sendItem)) {
				results.add(new ItemStack(Items.BUCKET, send.getCount()));
			}

			// 上位魔術書
			else if (sendItem == ItemInit.magic_book_cosmic){
				ItemStack book = new ItemStack(ItemInit.magic_book_cosmic);
				book.setTagCompound(ItemHelper.getNBT(send));
				results.add(book);
			}

			// 水入りバケツ
			else if (sendItem == ItemInit.alt_bucket_water) {
				results.add(((SMBucket) sendItem).useBucket(send));
			}
		}

		// クラフトで出来たアイテムの取得
		for (Object st : recipeInfo.getOutputItems()) {

			// ItemStackの取得して個数設定 + リスト追加
			ItemStack send = ((ItemStack) st).copy();
			int amount = send.getCount();

			// 数が0になるまで繰り返し
			while (amount > 0) {
				int input = Math.min(amount, 64);
				amount -= input;
				ItemStack resultItems = send.copy();
				resultItems.setCount(input);
				results.add(resultItems);
			}
		}

		// 減らす数分インベントリから減らす
		SMUtil.decrPInvMinList(player, 1, recipeInfo.getinputs());

		//プレイヤーのインベントリに結果を通知
		player.inventoryContainer.detectAndSendChanges();

		return new RecipeUtil(recipeInfo, copy, inputs, results);
	}

	// レシピの消費数計算
	public static int getRequestAmount (int recipeAmount, int invAmount) {
		return invAmount / recipeAmount;
	}

	// バケツチェック
	public static boolean isBucket (Item item){
		return item == Items.WATER_BUCKET || item == Items.MILK_BUCKET;
	}
}
