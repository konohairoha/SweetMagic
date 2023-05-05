package sweetmagic.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import sweetmagic.api.magiaflux.MagiaFluxInfo;
import sweetmagic.api.recipe.alstroemeria.AlstroemeriaRecipeInfo;
import sweetmagic.api.recipe.alstroemeria.AlstroemeriaRecipes;
import sweetmagic.api.recipe.fermenter.FermenterRecipeInfo;
import sweetmagic.api.recipe.fermenter.FermenterRecipes;
import sweetmagic.api.recipe.flourmill.FlourMillRecipeInfo;
import sweetmagic.api.recipe.flourmill.FlourMillRecipes;
import sweetmagic.api.recipe.freezer.FreezerRecipeInfo;
import sweetmagic.api.recipe.freezer.FreezerRecipes;
import sweetmagic.api.recipe.juicemaker.JuiceMakerRecipeInfo;
import sweetmagic.api.recipe.juicemaker.JuiceMakerRecipes;
import sweetmagic.api.recipe.mftable.MFTableRecipeInfo;
import sweetmagic.api.recipe.mftable.MFTableRecipes;
import sweetmagic.api.recipe.obmagia.ObMagiaRecipeInfo;
import sweetmagic.api.recipe.obmagia.ObMagiaRecipes;
import sweetmagic.api.recipe.oven.OvenRecipeInfo;
import sweetmagic.api.recipe.oven.OvenRecipes;
import sweetmagic.api.recipe.pan.PanRecipeInfo;
import sweetmagic.api.recipe.pan.PanRecipes;
import sweetmagic.api.recipe.pedal.PedalRecipeInfo;
import sweetmagic.api.recipe.pedal.PedalRecipes;
import sweetmagic.api.recipe.pot.PotRecipeInfo;
import sweetmagic.api.recipe.pot.PotRecipes;
import sweetmagic.util.RecipeHelper;

public class SweetMagicAPI {

	// 保有MFアイテム情報リスト
	public static List<MagiaFluxInfo> mfList = new ArrayList<MagiaFluxInfo>();

	// 保有MFを取得
	public static int getMFFromItem(ItemStack item) {

		MagiaFluxInfo info = null;

		for(MagiaFluxInfo list : mfList) {
			if(list.getItem().isItemEqual(item)) {
				info = list;
				break;
			}
		}

		return info != null ? info.getMF() : -1;
	}

	// アルストロメリアレシピリスト
	public static List<AlstroemeriaRecipes> alsRecipe = new ArrayList<AlstroemeriaRecipes>();

	// アルストロメリアレシピリスト
	public static List<PedalRecipes> pedalRecipe = new ArrayList<PedalRecipes>();

	// 製粉機レシピリスト
	public static List<FlourMillRecipes> millRecipe = new ArrayList<FlourMillRecipes>();

	// オーブンレシピリスト
	public static List<OvenRecipes> ovenRecipe = new ArrayList<OvenRecipes>();

	// 鍋レシピリスト
	public static List<PotRecipes> potRecipe = new ArrayList<PotRecipes>();

	// フライパンレシピリスト
	public static List<PanRecipes> panRecipe = new ArrayList<PanRecipes>();

	// オブマギアレシピリスト
	public static List<ObMagiaRecipes> magiaRecipe = new ArrayList<ObMagiaRecipes>();

	// MFテーブルレシピリスト
	public static List<MFTableRecipes> mfTableRecipe = new ArrayList<MFTableRecipes>();

	// 発酵機レシピリスト
	public static List<FermenterRecipes> fermenterRecipe = new ArrayList<FermenterRecipes>();

	// ジュースメイカーレシピリスト
	public static List<JuiceMakerRecipes> juiceRecipe = new ArrayList<JuiceMakerRecipes>();

	// 冷蔵庫レシピリスト
	public static List<FreezerRecipes> freezRecipe = new ArrayList<FreezerRecipes>();

	// この処理以降のリストには要素を追加できないので注意！
	public static void unmodifiableList(){

		ovenRecipe = Collections.unmodifiableList(ovenRecipe);
		millRecipe = Collections.unmodifiableList(millRecipe);
		alsRecipe = Collections.unmodifiableList(alsRecipe);
		pedalRecipe = Collections.unmodifiableList(pedalRecipe);
		potRecipe = Collections.unmodifiableList(potRecipe);
		panRecipe = Collections.unmodifiableList(panRecipe);
		magiaRecipe = Collections.unmodifiableList(magiaRecipe);
		mfTableRecipe = Collections.unmodifiableList(mfTableRecipe);
		fermenterRecipe = Collections.unmodifiableList(fermenterRecipe);
		juiceRecipe = Collections.unmodifiableList(juiceRecipe);
		freezRecipe = Collections.unmodifiableList(freezRecipe);

	}

	/*
	 * ======================================
	 * 			以下がチェックレシピ用
	 * ======================================
	 */

	// アルストロメリアのレシピチェック用
	public static AlstroemeriaRecipeInfo getAlstroemeriaRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {
		return RecipeHelper.getAlstroemeriaRecipeInfo(hand, inv);
	}

	// 創造の台座のレシピチェック用
	public static PedalRecipeInfo getPedalRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {
		return RecipeHelper.getPedalRecipeInfo(hand, inv);
	}

	// 製粉機のレシピチェック用
	public static FlourMillRecipeInfo getFlourMillRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {
		return RecipeHelper.getFlourMillRecipeInfo(hand, inv);
	}

	// オーブンのレシピチェック用
	public static OvenRecipeInfo getOvenRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {
		return RecipeHelper.getOvenRecipeInfo(hand, inv);
	}

	// 鍋のレシピチェック用
	public static PotRecipeInfo getPotRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {
		return RecipeHelper.getPotRecipeInfo(hand, inv);
	}

	// フライパンのレシピチェック用
	public static PanRecipeInfo getPanRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {
		return RecipeHelper.getPanRecipeInfo(hand, inv);
	}

	// オブマギアのレシピチェック用
	public static ObMagiaRecipeInfo getObMagiaRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {
		return RecipeHelper.getObMagiaRecipeInfo(hand, inv);
	}

	// MFテーブルのレシピチェック用
	public static MFTableRecipeInfo getMFTableRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {
		return RecipeHelper.getmftableRecipeInfo(hand, inv);
	}

	// 発酵機のレシピチェック用
	public static FermenterRecipeInfo getFermenterRecipeInfo(ItemStack hand, NonNullList<ItemStack> inv) {
		return RecipeHelper.getFermenterRecipeInfo(hand, inv);
	}

	// ジュースメーカーのレシピチェック用
	public static JuiceMakerRecipeInfo getJuiceMakerRecipeInfo(ItemStack hand, List<ItemStack> inv) {
		return RecipeHelper.getJuiceMakerRecipeInfo(hand, inv);
	}

	// 冷蔵庫のレシピチェック用
	public static FreezerRecipeInfo getFreezRecipeInfo(ItemStack hand, List<ItemStack> inv) {
		return RecipeHelper.getFreezRecipeInfo(hand, inv);
	}
}

