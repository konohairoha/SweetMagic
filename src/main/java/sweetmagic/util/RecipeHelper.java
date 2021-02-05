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

		SweetMagicAPI.alsRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe ->
		{

			// レシピチェック失敗フラグ
			boolean nextRecipeFlg = false;
			// レシピチェック成功フラグ
			boolean successFlg = false;

			// List<List<ItemStack>> から取り出す
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 鉱石辞書にある分取り出す
				for (ItemStack stack : oreList) {

					// レシピチェック成功フラグが立っていたらチェック終了
					if(successFlg) { break; }

					// アイテム情報をインベントリから取り出す
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());

					// 取り出したかどうか
					if (itemInfo != null) {

						// アイテム情報をリストに追加する
						itemInfos.add(itemInfo);

						// レシピアイテムと保存できたアイテム情報の数が一致したら
						if (itemInfos.size() == recipe.getInputList().size()) {
							recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
							recipeInfo.canComplete = true;
							successFlg = true;
							break;
						}

						// 次の要求アイテムへ
						break;
					}
				}

				if (itemInfos.isEmpty()) {
					nextRecipeFlg = true;
				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
				if(nextRecipeFlg) {
					nextRecipeFlg = false;
					itemInfos.clear();
					break;
				}

				// レシピチェック成功フラグが立っていたらチェック終了
				if(successFlg) { break; }
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

		SweetMagicAPI.pedalRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe ->
		{

			// レシピチェック失敗フラグ
			boolean nextRecipeFlg = false;
			// レシピチェック成功フラグ
			boolean successFlg = false;

			// List<List<ItemStack>> から取り出す
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 鉱石辞書にある分取り出す
				for (ItemStack stack : oreList) {

					// レシピチェック成功フラグが立っていたらチェック終了
					if(successFlg) { break; }

					// アイテム情報をインベントリから取り出す
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());

					// 取り出したかどうか
					if (itemInfo != null) {

						// アイテム情報をリストに追加する
						itemInfos.add(itemInfo);

						// レシピアイテムと保存できたアイテム情報の数が一致したら
						if (itemInfos.size() == recipe.getInputList().size()) {
							recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
							recipeInfo.canComplete = true;
							successFlg = true;
							break;
						}

						// 次の要求アイテムへ
						break;
					}
				}

				if (itemInfos.isEmpty()) {
					nextRecipeFlg = true;
				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
				if(nextRecipeFlg) {
					nextRecipeFlg = false;
					itemInfos.clear();
					break;
				}

				// レシピチェック成功フラグが立っていたらチェック終了
				if(successFlg) { break; }
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

		SweetMagicAPI.ovenRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe ->
		{

			// レシピチェック失敗フラグ
			boolean nextRecipeFlg = false;
			// レシピチェック成功フラグ
			boolean successFlg = false;

			// List<List<ItemStack>> から取り出す
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 鉱石辞書にある分取り出す
				for (ItemStack stack : oreList) {

					// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
					if(nextRecipeFlg) {
						nextRecipeFlg = false;
						itemInfos.clear();
						break;
					}

					// レシピチェック成功フラグが立っていたらチェック終了
					if(successFlg) { break; }

					// アイテム情報をインベントリから取り出す
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());

					// 取り出したかどうか
					if(itemInfo != null) {

						// アイテム情報をリストに追加する
						itemInfos.add(itemInfo);

						System.out.println("==========" + itemInfo);

						// レシピアイテムと保存できたアイテム情報の数が一致したら
						if(itemInfos.size() == recipe.inputList.size()) {
							recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
							recipeInfo.canComplete = true;
							successFlg = true;
						}

						// 次の要求アイテムへ
						break;
					}
				}

				if (itemInfos.isEmpty()) {
					nextRecipeFlg = true;
				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
				if(nextRecipeFlg) {
					nextRecipeFlg = false;
					itemInfos.clear();
					break;
				}

				// レシピチェック成功フラグが立っていたらチェック終了
				if(successFlg) { break; }
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

		SweetMagicAPI.fermenterRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe ->
		{

			// レシピチェック失敗フラグ
			boolean nextRecipeFlg = false;
			// レシピチェック成功フラグ
			boolean successFlg = false;

			// List<List<ItemStack>> から取り出す
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 鉱石辞書にある分取り出す
				for (ItemStack stack : oreList) {

					// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
					if(nextRecipeFlg) {
						nextRecipeFlg = false;
						itemInfos.clear();
						break;
					}

					// レシピチェック成功フラグが立っていたらチェック終了
					if(successFlg) { break; }

					// アイテム情報をインベントリから取り出す
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());

					// 取り出したかどうか
					if(itemInfo != null) {

						// アイテム情報をリストに追加する
						itemInfos.add(itemInfo);

						// レシピアイテムと保存できたアイテム情報の数が一致したら
						if(itemInfos.size() == recipe.inputList.size()) {
							recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
							recipeInfo.canComplete = true;
							successFlg = true;
						}

						// 次の要求アイテムへ
						break;
					}
				}

				if (itemInfos.isEmpty()) {
					nextRecipeFlg = true;
				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
				if(nextRecipeFlg) {
					nextRecipeFlg = false;
					itemInfos.clear();
					break;
				}

				// レシピチェック成功フラグが立っていたらチェック終了
				if(successFlg) { break; }
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

		SweetMagicAPI.juiceRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe ->
		{

			// レシピチェック失敗フラグ
			boolean nextRecipeFlg = false;
			// レシピチェック成功フラグ
			boolean successFlg = false;

			// List<List<ItemStack>> から取り出す
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 鉱石辞書にある分取り出す
				for (ItemStack stack : oreList) {

					// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
					if(nextRecipeFlg) {
						nextRecipeFlg = false;
						itemInfos.clear();
						break;
					}

					// レシピチェック成功フラグが立っていたらチェック終了
					if(successFlg) { break; }

					// アイテム情報をインベントリから取り出す
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) 1);

					// 取り出したかどうか
					if(itemInfo != null) {

						// アイテム情報をリストに追加する
						itemInfos.add(itemInfo);

						// レシピアイテムと保存できたアイテム情報の数が一致したら
						if(itemInfos.size() == recipe.inputList.size()) {
							recipeInfo.setRecipeInfo(recipe.getHandList(), recipe.getOutputItemStack());
							recipeInfo.canComplete = true;
							successFlg = true;
						}

						// 次の要求アイテムへ
						break;
					}
				}

				if (itemInfos.isEmpty()) {
					nextRecipeFlg = true;
				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
				if(nextRecipeFlg) {
					nextRecipeFlg = false;
					itemInfos.clear();
					break;
				}

				// レシピチェック成功フラグが立っていたらチェック終了
				if(successFlg) { break; }
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

		SweetMagicAPI.freezRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe ->
		{

			// レシピチェック失敗フラグ
			boolean nextRecipeFlg = false;
			// レシピチェック成功フラグ
			boolean successFlg = false;

			// List<List<ItemStack>> から取り出す
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 鉱石辞書にある分取り出す
				for (ItemStack stack : oreList) {

					// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
					if(nextRecipeFlg) {
						nextRecipeFlg = false;
						itemInfos.clear();
						break;
					}

					// レシピチェック成功フラグが立っていたらチェック終了
					if(successFlg) { break; }

					// アイテム情報をインベントリから取り出す
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) 1);

					// 取り出したかどうか
					if(itemInfo != null) {

						// アイテム情報をリストに追加する
						itemInfos.add(itemInfo);

						// レシピアイテムと保存できたアイテム情報の数が一致したら
						if(itemInfos.size() == recipe.inputList.size()) {
							recipeInfo.setRecipeInfo(recipe.getHandList(), recipe.getOutputItemStack());
							recipeInfo.canComplete = true;
							successFlg = true;
						}

						// 次の要求アイテムへ
						break;
					}
				}

				if (itemInfos.isEmpty()) {
					nextRecipeFlg = true;
				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
				if(nextRecipeFlg) {
					nextRecipeFlg = false;
					itemInfos.clear();
					break;
				}

				// レシピチェック成功フラグが立っていたらチェック終了
				if(successFlg) { break; }
			}
		});

		// アイテムリストが残ってたらアイテムリストを返す　なかったらcanComplete = Falseで返す
		return !itemInfos.isEmpty() ? recipeInfo : new FreezerRecipeInfo();
	}

	// 冷蔵庫レシピを返す
	public static FreezerRecipeInfo returnFreezerInfo (FreezerRecipeInfo info, List<Object[]> listInfo) {
		return !listInfo.isEmpty() ? info : new FreezerRecipeInfo();
	}


	public static PotRecipeInfo getPotRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {

		// アイテム情報リスト
		List<Object[]> itemInfos = new ArrayList<Object[]>();
		//レシピ情報
		PotRecipeInfo recipeInfo = new PotRecipeInfo();

		SweetMagicAPI.potRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe ->
		{

			// レシピチェック失敗フラグ
			boolean nextRecipeFlg = false;
			// レシピチェック成功フラグ
			boolean successFlg = false;

			// List<List<ItemStack>> から取り出す
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 鉱石辞書にある分取り出す
				for (ItemStack stack : oreList) {

					// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
					if(nextRecipeFlg) {
						nextRecipeFlg = false;
						itemInfos.clear();
						break;
					}

					// レシピチェック成功フラグが立っていたらチェック終了
					if(successFlg) { break; }

					// アイテム情報をインベントリから取り出す
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());

					// 取り出したかどうか
					if(itemInfo != null) {

						// アイテム情報をリストに追加する
						itemInfos.add(itemInfo);

						// レシピアイテムと保存できたアイテム情報の数が一致したら
						if(itemInfos.size() == recipe.inputList.size()) {
							recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
							recipeInfo.canComplete = true;
							successFlg = true;
						}

						// 次の要求アイテムへ
						break;
					}
				}

				if (itemInfos.isEmpty()) {
					nextRecipeFlg = true;
				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
				if(nextRecipeFlg) {
					nextRecipeFlg = false;
					itemInfos.clear();
					break;
				}

				// レシピチェック成功フラグが立っていたらチェック終了
				if(successFlg) { break; }
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

		SweetMagicAPI.panRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe ->
		{

			// レシピチェック失敗フラグ
			boolean nextRecipeFlg = false;
			// レシピチェック成功フラグ
			boolean successFlg = false;

			// List<List<ItemStack>> から取り出す
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 鉱石辞書にある分取り出す
				for (ItemStack stack : oreList) {

					// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
					if(nextRecipeFlg) {
						nextRecipeFlg = false;
						itemInfos.clear();
						break;
					}

					// レシピチェック成功フラグが立っていたらチェック終了
					if(successFlg) { break; }

					// アイテム情報をインベントリから取り出す
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());

					// 取り出したかどうか
					if(itemInfo != null) {

						// アイテム情報をリストに追加する
						itemInfos.add(itemInfo);

						// レシピアイテムと保存できたアイテム情報の数が一致したら
						if(itemInfos.size() == recipe.inputList.size()) {
							recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
							recipeInfo.canComplete = true;
							successFlg = true;
						}

						// 次の要求アイテムへ
						break;
					}
				}

				if (itemInfos.isEmpty()) {
					nextRecipeFlg = true;
				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
				if(nextRecipeFlg) {
					nextRecipeFlg = false;
					itemInfos.clear();
					break;
				}

				// レシピチェック成功フラグが立っていたらチェック終了
				if(successFlg) { break; }
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


		SweetMagicAPI.magiaRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe ->
		{

			// レシピチェック失敗フラグ
			boolean nextRecipeFlg = false;
			// レシピチェック成功フラグ
			boolean successFlg = false;

			// List<List<ItemStack>> から取り出す
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 鉱石辞書にある分取り出す
				for (ItemStack stack : oreList) {

					// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
					if(nextRecipeFlg) {
						nextRecipeFlg = false;
						itemInfos.clear();
						break;
					}

					// レシピチェック成功フラグが立っていたらチェック終了
					if(successFlg) { break; }

					// アイテム情報をインベントリから取り出す
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());

					// 取り出したかどうか
					if(itemInfo != null) {

						// アイテム情報をリストに追加する
						itemInfos.add(itemInfo);

						// レシピアイテムと保存できたアイテム情報の数が一致したら
						if(itemInfos.size() == recipe.inputList.size()) {
							recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
							recipeInfo.canComplete = true;
							successFlg = true;
						}

						// 次の要求アイテムへ
						break;
					}
				}

				if (itemInfos.isEmpty()) {
					nextRecipeFlg = true;
				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
				if(nextRecipeFlg) {
					nextRecipeFlg = false;
					itemInfos.clear();
					break;
				}

				// レシピチェック成功フラグが立っていたらチェック終了
				if(successFlg) { break; }
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

		SweetMagicAPI.mfTableRecipe.stream().filter(recipe -> canCraft(recipe.getHandList(), hand)).forEach(recipe ->
		{

			// レシピチェック失敗フラグ
			boolean nextRecipeFlg = false;
			// レシピチェック成功フラグ
			boolean successFlg = false;

			// List<List<ItemStack>> から取り出す
			for (List<ItemStack> oreList : recipe.getInputList()) {

				// 鉱石辞書にある分取り出す
				for (ItemStack stack : oreList) {

					// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
					if(nextRecipeFlg) {
						nextRecipeFlg = false;
						itemInfos.clear();
						break;
					}

					// レシピチェック成功フラグが立っていたらチェック終了
					if(successFlg) { break; }

					// アイテム情報をインベントリから取り出す
					Object[] itemInfo = SMUtil.getStackInv(inv, stack, (byte) stack.getCount());

					// 取り出したかどうか
					if(itemInfo != null) {

						// アイテム情報をリストに追加する
						itemInfos.add(itemInfo);

						// レシピアイテムと保存できたアイテム情報の数が一致したら
						if(itemInfos.size() == recipe.getInputList().size()) {
//							System.out.println("=======" + recipe.getOutputItemStack());
							recipeInfo.setRecipeInfo(recipe.getHandList(), itemInfos, recipe.getOutputItemStack());
							recipeInfo.canComplete = true;
							successFlg = true;
						}

						// 次の要求アイテムへ
						break;

						// レシピチェック失敗にする
					} /*else {
						nextRecipeFlg = true;
						break;
					}*/
				}

				if (itemInfos.isEmpty()) {
					nextRecipeFlg = true;
				}

				// レシピチェックではじかれたら初期化しながら次のレシピチェック処理に移行
				if(nextRecipeFlg) {
					nextRecipeFlg = false;
					itemInfos.clear();
					break;
				}

				// レシピチェック成功フラグが立っていたらチェック終了
				if(successFlg) { break; }
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
			send.setCount(shrinkAmount * (int) recipe[2]);
			inputs.add(send);

			// バケツチェック
			if (isBucket(send.getItem())) {
				results.add(new ItemStack(Items.BUCKET));
			}
		}

		// クラフトで出来たアイテムの取得
		for (Object st : recipeInfo.getOutputItems()) {

			// ItemStackの取得して個数設定 + リスト追加
			ItemStack send = ((ItemStack) st).copy();
			int amount = shrinkAmount * send.getCount();

			// 数が0になるまで繰り返し
			while (amount > 0) {
				int input = amount >= 64 ? 64 : amount;
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
		ItemStack copy = hand.copy();
		List<ItemStack> inputs = new ArrayList<ItemStack>();
		List<ItemStack> results = new ArrayList<ItemStack>();

		// 手に持っているアイテムを処理する
		hand.shrink(handitem.getCount());

		// クラフトで使うアイテムの取得
		for (Object[] recipe : recipeInfo.getinputs()) {

			// ItemStackの取得して個数設定 + リスト追加
			ItemStack send = ((ItemStack) recipe[1]).copy();
			send.setCount((int) recipe[2]);
			inputs.add(send);

			// バケツチェック
			if (isBucket(send.getItem())) {
				results.add(new ItemStack(Items.BUCKET, send.getCount()));
			}
		}

		// クラフトで出来たアイテムの取得
		for (Object st : recipeInfo.getOutputItems()) {

			// ItemStackの取得して個数設定 + リスト追加
			ItemStack send = ((ItemStack) st).copy();
			int amount = send.getCount();

			// 数が0になるまで繰り返し
			while (amount > 0) {
				int input = amount >= 64 ? 64 : amount;
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
