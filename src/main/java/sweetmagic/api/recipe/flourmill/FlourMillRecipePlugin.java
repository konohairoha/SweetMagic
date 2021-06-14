package sweetmagic.api.recipe.flourmill;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.ItemInit;

@SMFlourMillRecipePlugin(priority = EventPriority.LOW)
public class FlourMillRecipePlugin implements IFlourMillRecipePlugin {

	@Override
	public void registerFlourMillRecipe(FlourMillRecipes recipe) {

		//レシピ登録方法…ItemStack(hand)、ItemStack[](Output)

		// 砂糖
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(ItemInit.sugarbell),
			new ItemStack[] { new ItemStack(Items.SUGAR, 2), new ItemStack(Items.SUGAR)
		}));

		// 糸
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(ItemInit.cotton),
			new ItemStack[] { new ItemStack(Items.STRING, 2), new ItemStack(Items.STRING)
		}));

		// オリーブ
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(ItemInit.olive),
			new ItemStack[] { new ItemStack(ItemInit.olive_oil, 2), new ItemStack (ItemInit.plant_chips)
		}));

		// パン
		recipe.addRecipe(new FlourMillRecipes(
			"bread",
			new ItemStack[] { new ItemStack(ItemInit.breadcrumbs, 3), new ItemStack(ItemInit.breadcrumbs)
		}));

		// 小麦
		recipe.addRecipe(new FlourMillRecipes(
			"cropWheat",
			new ItemStack[] { new ItemStack(ItemInit.flourpowder, 3), new ItemStack(ItemInit.flourpowder), new ItemStack (ItemInit.plant_chips)
		}));

		// バニラ
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(ItemInit.vannila_pods),
			new ItemStack[] { new ItemStack(ItemInit.vannila_essence, 2), new ItemStack(ItemInit.vannila_essence), new ItemStack(ItemInit.vannila_beans)
		}));

		// 大豆
		recipe.addRecipe(new FlourMillRecipes(
			"cropSoybean",
			new ItemStack[] { new ItemStack(ItemInit.kinako), new ItemStack(ItemInit.kinako)
		}));

		// 米
		recipe.addRecipe(new FlourMillRecipes(
			"seedRice",
			new ItemStack[] { new ItemStack(ItemInit.rice), new ItemStack (ItemInit.plant_chips)
		}));

		// 米
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(ItemInit.ine),
			new ItemStack[] { new ItemStack(ItemInit.rice, 2), new ItemStack (ItemInit.plant_chips)
		}));

		// 土→砂利変換
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(Blocks.DIRT),
			new ItemStack[] { new ItemStack(Blocks.GRAVEL), new ItemStack (Blocks.SAND)
		}));

		// 砂利→砂変換
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(Blocks.GRAVEL),
			new ItemStack[] { new ItemStack(Blocks.SAND), new ItemStack (Items.FLINT)
		}));

		// 糸
		recipe.addRecipe(new FlourMillRecipes(
			"wool",
			new ItemStack[] { new ItemStack(Items.STRING, 4)
		}));

		// 粘土
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(Blocks.CLAY),
			new ItemStack[] { new ItemStack(Items.CLAY_BALL, 4)
		}));

		// カカオマス
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(Items.DYE, 1, 3),
			new ItemStack[] { new ItemStack(ItemInit.cocoamass), new ItemStack (ItemInit.cocoabutter)
		}));

		// カカオバター
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(ItemInit.cocoamass),
			new ItemStack[] { new ItemStack(ItemInit.cocoabutter), new ItemStack(ItemInit.cocoapowder)
		}));

		// ココアパウダー
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(ItemInit.cocoabutter),
			new ItemStack[] { new ItemStack(ItemInit.cocoapowder), new ItemStack(ItemInit.cocoapowder)
		}));

		// カカオ
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(Blocks.LEAVES),
			new ItemStack[] { new ItemStack(Items.DYE, 2, 3), new ItemStack(Blocks.SAPLING, 1, 3)
		}));

		// コーヒー豆
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(Blocks.TALLGRASS, 1, 1),
			new ItemStack[] { new ItemStack(ItemInit.coffee_seed), new ItemStack(ItemInit.coffee_seed)
		}));

		// 火炎の粉
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(ItemInit.fire_nasturtium_petal, 1),
			new ItemStack[] { new ItemStack(ItemInit.fire_powder, 2), new ItemStack(ItemInit.fire_powder)
		}));

		// サツマイモ
		recipe.addRecipe(new FlourMillRecipes(
			new ItemStack(Blocks.DIRT, 1, 2),
			new ItemStack[] { new ItemStack(Blocks.DIRT), new ItemStack(ItemInit.sweetpotato)
		}));

		// 植物くず
		recipe.addRecipe(new FlourMillRecipes(
			"listAllseed",
			new ItemStack[] { new ItemStack(ItemInit.plant_chips, 2)
		}));
	}
}
