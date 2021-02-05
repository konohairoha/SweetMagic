package sweetmagic.api.recipe.pedal;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.util.OreItems;

@SMPedalRecipePlugin(priority = EventPriority.LOW)
public class PedalRecipePlugin implements IPedalRecipePlugin {

	@Override
	public void registerPedalRecipe(PedalRecipes recipe) {

		// MFかまど
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Blocks.FURNACE),
			new Object[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.mf_sbottle, 4), new OreItems("stone", 16)},
			new ItemStack[] { new ItemStack(BlockInit.mffurnace_off) }
		));

		// MF釣り機
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.sugarglass, 4),
			new Object[] { new OreItems("ingotIron", 6), new ItemStack(Items.FISHING_ROD), new ItemStack(ItemInit.aether_crystal, 2)},
			new ItemStack[] { new ItemStack(BlockInit.mffisher) }
		));

		// 肉生産機
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.prismglass, 4),
			new Object[] { new OreItems("ingotIron", 6), new ItemStack(ItemInit.machete), new ItemStack(ItemInit.aether_crystal, 2)},
			new ItemStack[] { new ItemStack(BlockInit.flyishforer) }
		));

		// 魔法流の杖
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.ender_shard),
			new Object[] { new OreItems("stickWood", 2), "nuggetIron" },
			new ItemStack[] { new ItemStack(ItemInit.mf_stuff) }
		));

		//エーテルワンド
		recipe.addRecipe(new PedalRecipes(
			"nuggetGold",
			new Object[] { new ItemStack(ItemInit.aether_crystal, 3), new OreItems("stickWood", 2), new OreItems("feather", 2)},
			new ItemStack[] { new ItemStack(ItemInit.aether_wand) }
		));

		// エーテル炉
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.gorgeous_lamp),
			new Object[] { new ItemStack(BlockInit.glow_lamp), new ItemStack(ItemInit.divine_crystal, 4), new OreItems("ingotIron", 12)
					, new ItemStack(Blocks.IRON_BARS, 4)},
			new ItemStack[] { new ItemStack(BlockInit.aether_furnace_bottom) }
		));

		// パラレル・インターフィアー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Items.BOOK),
			new Object[] { new ItemStack(ItemInit.sugarbell, 4), new ItemStack(ItemInit.blank_page, 2), new ItemStack(ItemInit.divine_crystal, 2)
					, new ItemStack(Blocks.CARPET, 4, 5), new ItemStack(Items.ENDER_EYE, 4) , new ItemStack(ItemInit.mf_bottle, 2)
					, new ItemStack(ItemInit.witch_tears), new ItemStack(Blocks.ENCHANTING_TABLE)},
			new ItemStack[] { new ItemStack(BlockInit.parallel_interfere) }
		));

		// 夜の帳
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.cotton_cloth, 48),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 3), new ItemStack(ItemInit.poison_bottle, 4), new ItemStack(ItemInit.tiny_feather, 4),
					 new ItemStack(ItemInit.unmeltable_ice, 4), new ItemStack(ItemInit.electronic_orb, 4), new ItemStack(ItemInit.mystical_page, 6) },
			new ItemStack[] { new ItemStack(ItemInit.veil_darkness) }
		));
	}
}
