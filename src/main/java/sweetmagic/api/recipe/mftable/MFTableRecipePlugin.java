package sweetmagic.api.recipe.mftable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.ItemInit;
import sweetmagic.util.SMUtil;

@SMMFTableRecipePlugin(priority = EventPriority.LOW)
public class MFTableRecipePlugin implements IMFTableRecipePlugin {

	@Override
	public void registerMFTableRecipe(MFTableRecipes recipe) {

		//レシピ登録方法…ItemStack(hand)、ItemStack[](Input)、ItemStack[](Output)、必要杖レベル

		// ディバインクリスタル
		recipe.addRecipe(new MFTableRecipes(
			SMUtil.getStack(ItemInit.aether_wand),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal), new ItemStack(Items.BLAZE_ROD, 2), new ItemStack(ItemInit.tiny_feather, 2) },
			new ItemStack[] { new ItemStack(ItemInit.divine_wand) },
			1
		));

		// ピュアクリスタルワンド
		recipe.addRecipe(new MFTableRecipes(
			SMUtil.getStack(ItemInit.divine_wand),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal, 3), new ItemStack(Items.GOLD_INGOT, 4), new ItemStack(ItemInit.electronic_orb, 2) },
			new ItemStack[] { new ItemStack(ItemInit.purecrystal_wand) },
			1
		));

		// デウスクリスタルワンド
		recipe.addRecipe(new MFTableRecipes(
			SMUtil.getStack(ItemInit.purecrystal_wand),
			new ItemStack[] { new ItemStack(ItemInit.deus_crystal), new ItemStack(Items.DIAMOND, 8), new ItemStack(ItemInit.witch_tears, 2) },
			new ItemStack[] { new ItemStack(ItemInit.deuscrystal_wand) },
			1
		));
	}
}
