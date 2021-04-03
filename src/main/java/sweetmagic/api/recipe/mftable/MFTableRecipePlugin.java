package sweetmagic.api.recipe.mftable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;

@SMMFTableRecipePlugin(priority = EventPriority.LOW)
public class MFTableRecipePlugin implements IMFTableRecipePlugin {

	@Override
	public void registerMFTableRecipe(MFTableRecipes recipe) {

		//レシピ登録方法…ItemStack(hand)、ItemStack[](Input)、ItemStack[](Output)、必要杖レベル

		// ディバインクリスタル
		recipe.addRecipe(new MFTableRecipes(
			"aetherWand",
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal), new ItemStack(Items.BLAZE_ROD, 2), new ItemStack(Items.FEATHER, 4) },
			new ItemStack[] { new ItemStack(ItemInit.divine_wand) },
			1
		));

		// ピュアクリスタルワンド
		recipe.addRecipe(new MFTableRecipes(
			"divineWand",
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal, 3), new ItemStack(Items.GOLD_INGOT, 4), new ItemStack(ItemInit.mf_sbottle, 4) },
			new ItemStack[] { new ItemStack(ItemInit.purecrystal_wand) },
			1
		));

		// デウスクリスタルワンド
		recipe.addRecipe(new MFTableRecipes(
			"purecrystalWand",
			new ItemStack[] { new ItemStack(ItemInit.deus_crystal), new ItemStack(Items.DIAMOND, 8), new ItemStack(ItemInit.witch_tears, 2) },
			new ItemStack[] { new ItemStack(ItemInit.deuscrystal_wand) },
			1
		));

		// コズミッククリスタルワンド
		recipe.addRecipe(new MFTableRecipes(
			"deuscrystalWand",
			new ItemStack[] { new ItemStack(ItemInit.cosmic_crystal), new ItemStack(Items.DIAMOND, 16), new ItemStack(ItemInit.mystical_page, 4) },
			new ItemStack[] { new ItemStack(ItemInit.cosmiccrystal_wand) },
			1
		));

		// セイクリッドメテオールワンド
		recipe.addRecipe(new MFTableRecipes(
			"cosmiccrystalWand",
			new ItemStack[] { new ItemStack(ItemInit.cosmic_crystal, 2), new ItemStack(ItemInit.cosmic_crystal_shard, 8), new ItemStack(ItemInit.prizmium, 64), new ItemStack(BlockInit.twilightlight, 2) },
			new ItemStack[] { new ItemStack(ItemInit.sacred_meteor_wand) },
			1
		));

		// スノーダストワンド
		recipe.addRecipe(new MFTableRecipes(
			"cosmiccrystalWand",
			new ItemStack[] { new ItemStack(ItemInit.cosmic_crystal, 2), new ItemStack(ItemInit.cosmic_crystal_shard, 8), new ItemStack(ItemInit.unmeltable_ice, 64), new ItemStack(BlockInit.twilightlight, 2) },
			new ItemStack[] { new ItemStack(ItemInit.snowdust_wand) },
			1
		));
	}
}
