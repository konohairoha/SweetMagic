package sweetmagic.init.tile.slot;

import java.util.function.Predicate;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.iitem.ISMItem;
import sweetmagic.api.iitem.IWand;
import sweetmagic.api.magiaflux.MagiaFluxInfo;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.eitem.SMType;
import sweetmagic.init.item.sm.magic.MFItem;
import sweetmagic.util.ItemHelper;

public final class SlotPredicates {

	public static ItemStack stack;

	// 燃焼アイテムか
    public static final Predicate<ItemStack> FURNACE_FUEL = input -> !input.isEmpty() && TileEntityFurnace.isItemFuel(input);

	// MFアイテムか
    public static final Predicate<ItemStack> MFF_FUEL = input -> !input.isEmpty() && doesItemHaveEmc(input);

    // かまどレシピまたは鉄インゴットか
    public static final Predicate<ItemStack> SMELTABLE = input -> !input.isEmpty() && !FurnaceRecipes.instance().getSmeltingResult(input).isEmpty();

    // MF空ビンだけが入るように
    public static final Predicate<ItemStack> MFBOTTLE = input -> !input.isEmpty() && (input.getItem() == Items.GLASS_BOTTLE || input.getItem() == ItemInit.b_mf_bottle);

    // MF空ビンだけが入るように
    public static final Predicate<ItemStack> ALLITEM = input -> !input.isEmpty();

    // 属性アイテムを持っているなら
    public static final Predicate<ItemStack> SMELEMENT = input -> canInputSMItem(input);

    // 杖なら
    public static final Predicate<ItemStack> SMWAND = input -> !input.isEmpty() && input.getItem() instanceof IWand;

    // 水なら
    public static final Predicate<ItemStack> WATERCUP = input -> !input.isEmpty() && isWaterCup(input);

    // 射撃タイプなら
    public static final Predicate<ItemStack> ISHOTER = input -> !input.isEmpty() && isShotMagic(input);

    // 射撃タイプなら
    public static final Predicate<ItemStack> ISFOOD = input -> !input.isEmpty() && input.getItem() instanceof ItemFood;

	// クリスタル以外か
    public static final Predicate<ItemStack> ISNOTCRYSTAL = input -> !input.isEmpty() && doesItemHaveEmc(input) && isNotCrystal(input);

    // MFを持っているかどうか
	public static boolean doesItemHaveEmc(ItemStack stack) {

		if (stack.isEmpty()) {
			return false;
		}

		return stack.getItem() instanceof MFItem || hasMFItem(stack);
	}

	// APIに登録したアイテムかどうか
	public static boolean hasMFItem (ItemStack stack) {

		MagiaFluxInfo info = null;
		for(MagiaFluxInfo list : SweetMagicAPI.mfList) {
			if(list.getItem().isItemEqual(stack)) {
				info = list;
				break;
			}
		}
		return info != null;
	}

	// 鉱石辞書にwaterCupかどうか
	public static boolean isWaterCup (ItemStack stack) {
		return ItemHelper.checkOreName(stack, "waterBucket");
	}

	// 杖にアイテムを入れれるかどうか
	public static boolean canInputSMItem (ItemStack input) {

		if (input.isEmpty() || !(input.getItem() instanceof ISMItem)) { return false;}

		ISMItem smItem = (ISMItem) input.getItem();
		int smTier = smItem.getTier();

		IWand wand = (IWand) stack.getItem();
		int wandTier = wand.getTier();

		return wandTier >= smTier;
	}

	// 射撃タイプかどうか
	public static boolean isShotMagic (ItemStack input) {

		ISMItem smItem = (ISMItem) input.getItem();

		return canInputSMItem(input) && smItem.getType() == SMType.SHOTTER;
	}

	// クリスタルじゃないかどうか
	public static boolean isNotCrystal(ItemStack stack) {
		Item item = stack.getItem();
		return item != ItemInit.aether_crystal && item != ItemInit.divine_crystal && item != ItemInit.pure_crystal;
	}
}
