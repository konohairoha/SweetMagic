package sweetmagic.api.recipe.pedal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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

		// ツールリペアラー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.divine_crystal, 2),
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 4), new ItemStack(BlockInit.pillar_stone, 8), new ItemStack(ItemInit.alternative_ingot, 8) },
			new ItemStack[] { new ItemStack(BlockInit.tool_repair) }
		));

		// マギア・リライト
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.deus_crystal),
			new Object[] { new ItemStack(ItemInit.cosmic_crystal_shard, 8), new ItemStack(BlockInit.pillar_stone_w, 8), new ItemStack(ItemInit.alternative_ingot, 16),
					new ItemStack(ItemInit.magicmeal, 8), new ItemStack(ItemInit.mystical_page, 2), new ItemStack(ItemInit.mf_bottle, 4) },
			new ItemStack[] { new ItemStack(BlockInit.magia_rewrite) }
		));

		// エーテルホッパー
		recipe.addRecipe(new PedalRecipes(
			"hopper",
			new Object[] { new OreItems("chestWood", 2), new ItemStack(ItemInit.aether_crystal, 4) },
			new ItemStack[] { new ItemStack(BlockInit.aether_hopper, 2) }
		));

		// パラレル・インターフィアー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Items.BOOK),
			new Object[] { new ItemStack(ItemInit.sugarbell, 4), new ItemStack(ItemInit.blank_page, 2), new ItemStack(ItemInit.divine_crystal, 2)
					, new ItemStack(Blocks.CARPET, 4, 5), new ItemStack(Items.ENDER_EYE, 4) , new ItemStack(ItemInit.mf_bottle, 2)
					, new ItemStack(ItemInit.witch_tears), new ItemStack(Blocks.ENCHANTING_TABLE)},
			new ItemStack[] { new ItemStack(BlockInit.parallel_interfere) }
		));

		// スターダスト・ウィッシュ
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Items.BOOK, 16),
			new Object[] { new ItemStack(ItemInit.cosmic_crystal_shard, 16), new OreItems("chestWood", 64), new ItemStack(ItemInit.pure_crystal, 4)
					, new ItemStack(ItemInit.mystical_page, 4), new ItemStack(Items.ENDER_EYE, 16), new ItemStack(Blocks.ENCHANTING_TABLE) },
			new ItemStack[] { new ItemStack(BlockInit.stardust_wish) }
		));

		// 夜の帳
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.cotton_cloth, 48),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 3), new ItemStack(ItemInit.poison_bottle, 4), new ItemStack(ItemInit.tiny_feather, 4),
					 new ItemStack(ItemInit.unmeltable_ice, 4), new ItemStack(ItemInit.electronic_orb, 4), new ItemStack(ItemInit.mystical_page, 6) },
			new ItemStack[] { new ItemStack(ItemInit.veil_darkness) }
		));

		// エーテルローブ
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.cotton_cloth, 24),
			new Object[] { new ItemStack(ItemInit.aether_crystal, 6), new ItemStack(Items.GOLD_INGOT, 4), new OreItems("slimeball", 16) },
			new ItemStack[] { new ItemStack(ItemInit.magicians_robe) }
		));

		// ポーチ
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Items.LEATHER, 16),
			new Object[] { new OreItems("chestWood"), new ItemStack(ItemInit.aether_crystal, 4), new OreItems("string", 8), new ItemStack(Items.IRON_NUGGET), new ItemStack(Blocks.WOODEN_BUTTON) },
			new ItemStack[] { new ItemStack(ItemInit.magicians_pouch) }
		));

		// ハーネス
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(Items.FEATHER, 8),
			new Object[] { new ItemStack(ItemInit.cotton_cloth, 8), new ItemStack(ItemInit.aether_crystal, 8), new OreItems("string", 8), new ItemStack(ItemInit.mystical_page) },
			new ItemStack[] { new ItemStack(ItemInit.angel_harness) }
		));

		// チョーカー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.aether_crystal, 4),
			new Object[] { new ItemStack(Items.IRON_INGOT, 6), new ItemStack(Items.GOLD_NUGGET, 4), new ItemStack(ItemInit.b_mf_bottle, 2) },
			new ItemStack[] { new ItemStack(ItemInit.aether_choker) }
		));

		// ワープブロック
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.witch_tears),
			new Object[] { new ItemStack(ItemInit.clero_petal, 8), new ItemStack(BlockInit.alt_block) },
			new ItemStack[] { new ItemStack(BlockInit.warp_block, 2) }
		));

		// 杖の台座
		recipe.addRecipe(new PedalRecipes(
			new OreItems("stone", 4),
			new Object[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(Items.IRON_INGOT, 4) },
			new ItemStack[] { new ItemStack(BlockInit.wand_pedal) }
		));

		// 霧雨の勿忘草の花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.dm_flower, 8),
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 8), new ItemStack(BlockInit.mfchanger), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.mfpot) }
		));

		// 黄昏時の夢百合草の花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.mfpot),
			new Object[] { new ItemStack(BlockInit.twilight_alstroemeria), new ItemStack(ItemInit.divine_crystal, 2)
					, new ItemStack(ItemInit.mf_bottle), new ItemStack(BlockInit.mfchanger) },
			new ItemStack[] { new ItemStack(BlockInit.twilightalstroemeria_pot) }
		));

		// スノードロップの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.snowdrop, 4),
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 4), new ItemStack(ItemInit.aether_crystal, 6), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.snowdrop_pot) }
		));

		// トルコキキョウの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.turkey_balloonflower, 16),
			new Object[] { new ItemStack(ItemInit.sannyflower_petal, 16), new ItemStack(ItemInit.moonblossom_petal, 16),
					new ItemStack(ItemInit.pure_crystal, 1), new ItemStack(ItemInit.mf_bottle, 2) },
			new ItemStack[] { new ItemStack(BlockInit.turkey_balloonflower_pot) }
		));

		// 群青の薔薇の花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.ultramarine_rose, 16),
			new Object[] { new ItemStack(ItemInit.sannyflower_petal, 16),
					new ItemStack(ItemInit.pure_crystal, 2), new ItemStack(ItemInit.mf_bottle, 4), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.ultramarine_rose_pot) }
		));

		// ソリッドスターの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.solid_star, 16),
			new Object[] { new ItemStack(ItemInit.divine_crystal), new ItemStack(ItemInit.mf_sbottle, 6), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.solid_star_pot) }
		));

		// ジニアの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.zinnia, 16),
			new Object[] { new ItemStack(ItemInit.divine_crystal), new ItemStack(ItemInit.mf_bottle, 2), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.zinnia_pot) }
		));

		// カーネーションの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.carnation_crayola, 8),
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 4), new ItemStack(ItemInit.cosmic_crystal_shard, 2), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.carnation_crayola_pot) }
		));

		// ハイドラの花瓶
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.hydrangea, 8),
			new Object[] { new ItemStack(ItemInit.divine_crystal), new ItemStack(ItemInit.mf_sbottle, 6), new ItemStack(Items.FLOWER_POT) },
			new ItemStack[] { new ItemStack(BlockInit.hydrangea_pot) }
		));

		// マギア・ドロワー
		recipe.addRecipe(new PedalRecipes(
			"woodChest",
			new Object[] { new ItemStack(ItemInit.mf_sbottle, 6), new ItemStack(ItemInit.mysterious_page, 3), new OreItems("chestWood", 2) },
			new ItemStack[] { new ItemStack(BlockInit.gravity_chest) },
			true
		));

		// 改良型MFテーブル
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.mftable),
			new Object[] { new ItemStack(ItemInit.mysterious_page, 8), new ItemStack(ItemInit.sannyflower_petal, 16), new ItemStack(ItemInit.moonblossom_petal, 16),
					new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.witch_tears) },
			new ItemStack[] { new ItemStack(BlockInit.advanced_mftable) },
			true
		));

		// 改良型MFチェンジャー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.mfchanger),
			new Object[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.magicmeal, 16), new ItemStack(Items.ENDER_PEARL, 4), new ItemStack(ItemInit.witch_tears)},
			new ItemStack[] { new ItemStack(BlockInit.advanced_mfchanger) },
			true
		));

		// 改良型MFタンク
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.mftank),
			new Object[] { new ItemStack(BlockInit.sugarglass, 64), new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.witch_tears)},
			new ItemStack[] { new ItemStack(BlockInit.advanced_mftank) },
			true
		));

		// 改良型エーテル炉
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.aether_furnace_bottom),
			new Object[] { new ItemStack(BlockInit.glow_lamp, 4), new ItemStack(ItemInit.pure_crystal, 4), new ItemStack(Items.GOLD_INGOT, 12)
					, new ItemStack(Blocks.IRON_BARS, 16), new ItemStack(ItemInit.witch_tears)},
			new ItemStack[] { new ItemStack(BlockInit.advanced_aether_furnace_bottom) },
			true
		));

		// 改良型魔法流かまど
		recipe.addRecipe(new PedalRecipes(
			"mffurnace",
			new Object[] { new ItemStack(ItemInit.pure_crystal, 4), new ItemStack(ItemInit.cosmic_crystal_shard, 8), new ItemStack(ItemInit.mf_bottle, 8) },
			new ItemStack[] { new ItemStack(BlockInit.advanced_mffurnace_off) },
			true
		));

		// マスターマギアタンク
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.advanced_mftank),
			new Object[] { new ItemStack(BlockInit.sugarglass, 64), new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.mystical_page, 2)},
			new ItemStack[] { new ItemStack(BlockInit.mm_tank) },
			true
		));

		// マスターマギアテーブル
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(BlockInit.advanced_mftable),
			new Object[] { new ItemStack(BlockInit.prism_log, 64), new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.mystical_page, 2) },
			new ItemStack[] { new ItemStack(BlockInit.mm_table) },
			true
		));

		// ピュアクリスタルチョーカー
		recipe.addRecipe(new PedalRecipes(
			new ItemStack(ItemInit.aether_choker),
			new Object[] { new ItemStack(ItemInit.pure_crystal, 2), new ItemStack(ItemInit.witch_tears, 3), new ItemStack(ItemInit.mf_bottle, 4) },
			new ItemStack[] { new ItemStack(ItemInit.pure_choker) },
			true
		));

		Map<Item, String> aetherRecipe = new HashMap<>();
		aetherRecipe.put(ItemInit.aether_wand_r, "dyeRed");
		aetherRecipe.put(ItemInit.aether_wand_g, "dyeGreen");
		aetherRecipe.put(ItemInit.aether_wand_b, "dyeBlue");
		aetherRecipe.put(ItemInit.aether_wand_y, "dyeYellow");
		aetherRecipe.put(ItemInit.aether_wand_p, "dyePurple");

		for (Entry<Item, String> map : aetherRecipe.entrySet()) {

			// エーテルワンド
			recipe.addRecipe(new PedalRecipes(
				"aetherWand",
				new Object[] { new OreItems(map.getValue(), 2), new ItemStack(ItemInit.magicmeal, 2) },
				new ItemStack[] { new ItemStack(map.getKey()) },
				true
			));
		}

		Map<Item, String> divineRecipe = new HashMap<>();
		divineRecipe.put(ItemInit.divine_wand_r, "dyeRed");
		divineRecipe.put(ItemInit.divine_wand_g, "dyeGreen");
		divineRecipe.put(ItemInit.divine_wand_b, "dyeBlue");
		divineRecipe.put(ItemInit.divine_wand_y, "dyeYellow");
		divineRecipe.put(ItemInit.divine_wand_p, "dyePurple");

		for (Entry<Item, String> map : divineRecipe.entrySet()) {

			// ディバインワンド
			recipe.addRecipe(new PedalRecipes(
				"divineWand",
				new Object[] { new OreItems(map.getValue(), 2), new ItemStack(ItemInit.aether_crystal, 2) },
				new ItemStack[] { new ItemStack(map.getKey()) },
				true
			));
		}

		Map<Item, String> pureRecipe = new HashMap<>();
		pureRecipe.put(ItemInit.purecrystal_wand_r, "dyeRed");
		pureRecipe.put(ItemInit.purecrystal_wand_g, "dyeGreen");
		pureRecipe.put(ItemInit.purecrystal_wand_b, "dyeBlue");
		pureRecipe.put(ItemInit.purecrystal_wand_y, "dyeYellow");
		pureRecipe.put(ItemInit.purecrystal_wand_p, "dyePurple");

		for (Entry<Item, String> map : pureRecipe.entrySet()) {

			// ピュアワンド
			recipe.addRecipe(new PedalRecipes(
				"purecrystalWand",
				new Object[] { new OreItems(map.getValue(), 2), new ItemStack(ItemInit.divine_crystal, 2) },
				new ItemStack[] { new ItemStack(map.getKey()) },
				true
			));
		}

		Map<Item, String> deusRecipe = new HashMap<>();
		deusRecipe.put(ItemInit.deuscrystal_wand_r, "dyeRed");
		deusRecipe.put(ItemInit.deuscrystal_wand_g, "dyeGreen");
		deusRecipe.put(ItemInit.deuscrystal_wand_b, "dyeBlue");
		deusRecipe.put(ItemInit.deuscrystal_wand_y, "dyeYellow");
		deusRecipe.put(ItemInit.deuscrystal_wand_p, "dyePurple");

		for (Entry<Item, String> map : deusRecipe.entrySet()) {

			// デウスワンド
			recipe.addRecipe(new PedalRecipes(
				"deuscrystalWand",
				new Object[] { new OreItems(map.getValue(), 2), new ItemStack(ItemInit.pure_crystal, 2) },
				new ItemStack[] { new ItemStack(map.getKey()) },
				true
			));
		}
	}
}
