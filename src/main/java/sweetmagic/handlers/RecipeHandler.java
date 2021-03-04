package sweetmagic.handlers;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import sweetmagic.SweetMagicCore;
import sweetmagic.config.SMConfig;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.recipe.JeiRecipeMFTank;
import sweetmagic.recipe.RecipeNBTExtend;
import sweetmagic.recipe.RecipeRegisterHelper;
import sweetmagic.util.SMUtil;

public class RecipeHandler {

    //レシピ定数List格納
	public static void registerCrafting() {

		String modId = SweetMagicCore.MODID;

		/*草ブロックから粘土*/
		if (SMConfig.help_recipe) {
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, "help_magicmeal"), new ResourceLocation(modId),
				new ItemStack(ItemInit.magicmeal, 4),
				new Object[] {
					" X ",
					"XYX",
					" X ",
					'X', Items.SUGAR,
					'Y', new ItemStack(Items.DYE, 1,15)
				}
			);
		}

		// オルタナティブインゴット
		GameRegistry.addShapelessRecipe(new ResourceLocation(modId, "alternative_ingot"), new ResourceLocation(modId),
			new ItemStack(ItemInit.alternative_ingot, 4),
			new Ingredient[] { Ingredient.fromStacks(new ItemStack(BlockInit.alt_block)) }
		);

		// アルストロメリア
		GameRegistry.addShapedRecipe(new ResourceLocation(modId, "alstroemeria"), new ResourceLocation(modId),
			new ItemStack(BlockInit.twilight_alstroemeria),
			new Object[] {
				"SSS",
				"SCM",
				"MMM",
				'S', ItemInit.sannyflower_petal,
				'M', ItemInit.moonblossom_petal,
				'C', Items.CLOCK
			}
		);

		// オルタナティブアックス
		GameRegistry.addShapedRecipe(new ResourceLocation(modId, "alt_axe"), new ResourceLocation(modId),
			new ItemStack(ItemInit.alt_axe),
			new Object[] {
				"AA ",
				"AS ",
				" S ",
				'A', ItemInit.alternative_ingot,
				'S', "stickWood"
			}
		);

		// オルタナティブバケツ
		GameRegistry.addShapedRecipe(new ResourceLocation(modId, "alt_bucket_2"), new ResourceLocation(modId),
			new ItemStack(ItemInit.alt_bucket),
			new Object[] {
				"A A",
				" A ",
				'A', ItemInit.alternative_ingot
			}
		);

		// オルタナティブバケツ
		GameRegistry.addShapelessRecipe(new ResourceLocation(modId, "alt_bucket_1"), new ResourceLocation(modId),
			new ItemStack(ItemInit.alt_bucket),
			new Ingredient[] { Ingredient.fromStacks(new ItemStack(ItemInit.alt_bucket_lava)) }
		);

		// オルタナティブバケツ
		GameRegistry.addShapelessRecipe(new ResourceLocation(modId, "alt_bucket_0"), new ResourceLocation(modId),
			new ItemStack(ItemInit.alt_bucket),
			new Ingredient[] { Ingredient.fromStacks(new ItemStack(ItemInit.alt_bucket_water)) }
		);

		// オルタナティブクワ
		GameRegistry.addShapedRecipe(new ResourceLocation(modId, "alt_hoe"), new ResourceLocation(modId),
			new ItemStack(ItemInit.alt_hoe),
			new Object[] {
				"AA ",
				" S ",
				" S ",
				'A', ItemInit.alternative_ingot,
				'S', "stickWood"
			}
		);

		// オルタナティブサイス
		GameRegistry.addShapedRecipe(new ResourceLocation(modId, "alt_sickle"), new ResourceLocation(modId),
			new ItemStack(ItemInit.alt_sickle),
			new Object[] {
				"AAA",
				"AS ",
				"S  ",
				'A', ItemInit.alternative_ingot,
				'S', "stickWood"
			}
		);

		// オルタナティブピッケル
		GameRegistry.addShapedRecipe(new ResourceLocation(modId, "alt_pick"), new ResourceLocation(modId),
			new ItemStack(ItemInit.alt_pick),
			new Object[] {
				"AAA",
				" S ",
				" S ",
				'A', ItemInit.alternative_ingot,
				'S', "stickWood"
			}
		);

		// オルタナティブシザー
		GameRegistry.addShapelessRecipe(new ResourceLocation(modId, "alt_shears"), new ResourceLocation(modId),
			new ItemStack(ItemInit.alt_shears),
			new Ingredient[] { Ingredient.fromStacks(new ItemStack(ItemInit.alternative_ingot)), Ingredient.fromStacks(new ItemStack(ItemInit.alternative_ingot)) }
		);

		// オルタナティブシャベル
		GameRegistry.addShapedRecipe(new ResourceLocation(modId, "alt_shovel"), new ResourceLocation(modId),
			new ItemStack(ItemInit.alt_shovel),
			new Object[] {
				" A ",
				" S ",
				" S ",
				'A', ItemInit.alternative_ingot,
				'S', "stickWood"
			}
		);

		// オルタナティブソード
		GameRegistry.addShapedRecipe(new ResourceLocation(modId, "alt_sword"), new ResourceLocation(modId),
			new ItemStack(ItemInit.alt_sword),
			new Object[] {
				" A ",
				" A ",
				" S ",
				'A', ItemInit.alternative_ingot,
				'S', "stickWood"
			}
		);

		// マチェット
		GameRegistry.addShapedRecipe(new ResourceLocation(modId, "machete"), new ResourceLocation(modId),
			new ItemStack(ItemInit.machete),
			new Object[] {
				"  A",
				" A ",
				"S  ",
				'A', ItemInit.alternative_ingot,
				'S', "stickWood"
			}
		);

		// 魔術書
		GameRegistry.addShapedRecipe(new ResourceLocation(modId, "magic_book"), new ResourceLocation(modId),
			new ItemStack(ItemInit.magic_book),
			new Object[] {
				"SCS",
				"PBP",
				"SCS",
				'P', ItemInit.blank_page,
				'B', Items.BOOK,
				'C', ItemInit.aether_crystal,
				'S', ItemInit.sugarbell
			}
		);

		// MFタンク
		GameRegistry.addShapedRecipe(new ResourceLocation(modId, "mftank"), new ResourceLocation(modId),
			new ItemStack(BlockInit.mftank),
			new Object[] {
				"SSS",
				"SBS",
				"SSS",
				'S', BlockInit.sugarglass,
				'B', ItemInit.magic_book
			}
		);

		// MFテーブル
		GameRegistry.addShapelessRecipe(new ResourceLocation(modId, "mftable"), new ResourceLocation(modId),
			new ItemStack(BlockInit.mftable),
			new Ingredient[] {
				Ingredient.fromStacks(new ItemStack(Blocks.CARPET, 1, 14)),
				Ingredient.fromStacks(new ItemStack(Blocks.CRAFTING_TABLE)),
				Ingredient.fromStacks(new ItemStack(ItemInit.magic_book))
			}
		);

		RecipeHandler.addRecipe("wizard_robe",
			new RecipeNBTExtend(new ResourceLocation(modId, "wizard_robe"),
			new ItemStack(ItemInit.wizard_robe),
				"DRD",
				'D', "dyeBlack",
				'R', ItemInit.magicians_robe
			)
		);

		// ブロック、ハーフ、階段をリストに突っ込む
		List<RecipeRegisterHelper> recipeList = new ArrayList<>();
		recipeList.add(new RecipeRegisterHelper(BlockInit.chestnut_planks, BlockInit.chestnut_stairs, BlockInit.chestnut_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.lemon_planks, BlockInit.lemon_stairs, BlockInit.lemon_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.coconut_planks, BlockInit.coconut_stairs, BlockInit.coconut_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.prism_planks, BlockInit.prism_stairs, BlockInit.prism_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.estor_planks, BlockInit.estor_stairs, BlockInit.estor_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.antique_brick_b, BlockInit.antique_brick_stairs_b, BlockInit.antique_brick_slab_b));
		recipeList.add(new RecipeRegisterHelper(BlockInit.flagstone, BlockInit.flagstone_stairs, BlockInit.flagstone_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.flagstone_color, BlockInit.flagstone_color_stairs, BlockInit.flagstone_color_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick, BlockInit.old_brick_stairs, BlockInit.old_brick_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick_r, BlockInit.old_brick_r_stairs, BlockInit.old_brick_r_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick_g, BlockInit.old_brick_g_stairs, BlockInit.old_brick_g_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick_y, BlockInit.old_brick_y_stairs, BlockInit.old_brick_y_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick_l, BlockInit.old_brick_l_stairs, BlockInit.old_brick_l_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick_b, BlockInit.old_brick_b_stairs, BlockInit.old_brick_b_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.old_brick_s, BlockInit.old_brick_s_stairs, BlockInit.old_brick_s_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick, BlockInit.longtile_brick_stairs, BlockInit.longtile_brick_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_b, BlockInit.longtile_brick_b_stairs, BlockInit.longtile_brick_b_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_g, BlockInit.longtile_brick_g_stairs, BlockInit.longtile_brick_g_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_y, BlockInit.longtile_brick_y_stairs, BlockInit.longtile_brick_y_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_r, BlockInit.longtile_brick_r_stairs, BlockInit.longtile_brick_r_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_l, BlockInit.longtile_brick_l_stairs, BlockInit.longtile_brick_l_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_p, BlockInit.longtile_brick_p_stairs, BlockInit.longtile_brick_p_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_o, BlockInit.longtile_brick_o_stairs, BlockInit.longtile_brick_o_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.longtile_brick_w, BlockInit.longtile_brick_w_stairs, BlockInit.longtile_brick_w_slab));

		// リストの分だけ回す
		for (RecipeRegisterHelper recipe : recipeList ) {

			Block planks = recipe.getPlanks();
			Block stairs = recipe.getStairs();
			Block slab = recipe.getSlab();

			Item slabItem = SMUtil.getItemBlock(slab);
			Item stairsItem = SMUtil.getItemBlock(stairs);

			// ハーフ→ブロック
			GameRegistry.addShapelessRecipe(new ResourceLocation(modId, planks.getUnlocalizedName() + "_0"), new ResourceLocation(modId),
				new ItemStack(planks),
				new Ingredient[] {
					Ingredient.fromItems(slabItem),
					Ingredient.fromItems(slabItem)
				}
			);

			// 階段→ブロック
			GameRegistry.addShapelessRecipe(new ResourceLocation(modId, planks.getUnlocalizedName() + "_1"), new ResourceLocation(modId),
				new ItemStack(planks, 3),
				new Ingredient[] {
					Ingredient.fromItems(stairsItem),
					Ingredient.fromItems(stairsItem),
					Ingredient.fromItems(stairsItem),
					Ingredient.fromItems(stairsItem)
				}
			);

			// ブロック→ハーフ
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, slab.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(slab, 6),
				new Object[] {
					"BBB",
					'B', planks
				}
			);

			// ブロック→階段
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, stairs.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(stairs, 8),
				new Object[] {
					"  B",
					" BB",
					"BBB",
					'B', planks
				}
			);
		}

		// ブロック、ハーフ、階段をリストに突っ込む
		List<RecipeRegisterHelper> recipeList2 = new ArrayList<>();
		recipeList2.add(new RecipeRegisterHelper(BlockInit.chestnut_log, BlockInit.chestnut_planks, BlockInit.chestnut_plate, false));
		recipeList2.add(new RecipeRegisterHelper(BlockInit.coconut_log, BlockInit.coconut_planks, BlockInit.coconut_plate, false));
		recipeList2.add(new RecipeRegisterHelper(BlockInit.lemon_log, BlockInit.lemon_planks, BlockInit.lemon_plate, false));
		recipeList2.add(new RecipeRegisterHelper(BlockInit.prism_log, BlockInit.prism_planks, BlockInit.prism_plate, false));
		recipeList2.add(new RecipeRegisterHelper(BlockInit.estor_log, BlockInit.estor_planks, BlockInit.estor_plate, false));

		for (RecipeRegisterHelper recipe : recipeList2) {

			Block log = recipe.getLog();
			Block planks = recipe.getPlanks();
			Block plate = recipe.getPlate();

			// 原木→ブロック
			GameRegistry.addShapelessRecipe(new ResourceLocation(modId, planks.getUnlocalizedName() + "_2"), new ResourceLocation(modId),
				new ItemStack(planks, 4),
				new Ingredient[] { Ingredient.fromStacks(new ItemStack(log)) }
			);

			// ブロック→感圧版
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, plate.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(plate, 4),
				new Object[] {
					"BB",
					'B', planks
				}
			);
		}

		// 完成品、染料をリストに突っ込む
		List<RecipeRegisterHelper> recipeList3 = new ArrayList<>();
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick, "dyeBlue"));
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick_b, "dyeBrown"));
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick_y, "dyeYellow"));
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick_g, "dyeGreen"));
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick_l, "dyeGray"));
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick_r, "dyeRed"));
		recipeList3.add(new RecipeRegisterHelper(BlockInit.old_brick_s, "dyeLightBlue"));

		// 鉱石辞書を取得
		Ingredient cobble = Ingredient.fromStacks(SMUtil.getOreArray("cobblestone"));
		Ingredient recipeBook = Ingredient.fromStacks(SMUtil.getOreArray("recipeBook"));

		for (RecipeRegisterHelper recipe : recipeList3) {

			// ブロックとアイテムの取得
			Block stone = recipe.getStone();
			Ingredient dye = Ingredient.fromStacks(SMUtil.getOreArray(recipe.getOre()));

			// 素材→古びたレンガ
			GameRegistry.addShapelessRecipe(new ResourceLocation(modId, stone.getUnlocalizedName() + "__0"), new ResourceLocation(modId),
				new ItemStack(stone, 9),
				new Ingredient[] {
					cobble,
					cobble,
					cobble,
					cobble,
					cobble,
					cobble,
					cobble,
					dye,
					recipeBook
				}
			);
		}

		// 完成品、染料をリストに突っ込む
		List<RecipeRegisterHelper> recipeList4 = new ArrayList<>();
		recipeList4.add(new RecipeRegisterHelper(BlockInit.sugarglass, BlockInit.sugarglass_pane));
		recipeList4.add(new RecipeRegisterHelper(BlockInit.shading_sugarglass, BlockInit.shading_sugarglass_pane));
		recipeList4.add(new RecipeRegisterHelper(BlockInit.frosted_glass, BlockInit.frosted_glass_pane));
		recipeList4.add(new RecipeRegisterHelper(BlockInit.frosted_glass_line, BlockInit.frosted_glass_line_pane));
		recipeList4.add(new RecipeRegisterHelper(BlockInit.prismglass, BlockInit.prismglass_pane));

		for (RecipeRegisterHelper recipe : recipeList4) {

			// ブロックとアイテムの取得
			Block glass = recipe.getGlass();
			Block pane = recipe.getPane();

			// ガラス→板ガラス
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, pane.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(pane, 16),
				new Object[] {
					"BBB",
					"BBB",
					'B', glass
				}
			);
		}

		// 完成品、染料をリストに突っ込む
		List<RecipeRegisterHelper> recipeList5 = new ArrayList<>();
		recipeList5.add(new RecipeRegisterHelper("antique_brick", BlockInit.antique_brick_0, BlockInit.antique_brick_1, BlockInit.antique_brick_2, BlockInit.antique_brick_stairs, BlockInit.antique_brick_slab));
		recipeList5.add(new RecipeRegisterHelper("antique_brick_white", BlockInit.antique_brick_0w, BlockInit.antique_brick_1w, BlockInit.antique_brick_2w, BlockInit.antique_brick_stairs_w, BlockInit.antique_brick_slab_w));
		recipeList5.add(new RecipeRegisterHelper("antique_brick_brown", BlockInit.antique_brick_0l, BlockInit.antique_brick_1l, BlockInit.antique_brick_2l, BlockInit.antique_brick_stairs_l, BlockInit.antique_brick_slab_l));
		recipeList5.add(new RecipeRegisterHelper("antique_brick_green", BlockInit.antique_brick_0g, BlockInit.antique_brick_1g, BlockInit.antique_brick_2g, BlockInit.antique_brick_stairs_g, BlockInit.antique_brick_slab_g));

		// リストの分だけ回す
		for (RecipeRegisterHelper recipe : recipeList5 ) {

			Ingredient brick = Ingredient.fromStacks(SMUtil.getOreArray(recipe.getBrick()));
			Block brick0 = recipe.getBrick0();
			Block brick1 = recipe.getBrick1();
			Block brick2 = recipe.getBrick2();
			Block stairs = recipe.getStairs();
			Block slab = recipe.getSlab();

			Item slabItem = SMUtil.getItemBlock(slab);
			Item stairsItem = SMUtil.getItemBlock(stairs);

			// ハーフ→ブロック
			GameRegistry.addShapelessRecipe(new ResourceLocation(modId, brick0.getUnlocalizedName() + "_0"), new ResourceLocation(modId),
				new ItemStack(brick0),
				new Ingredient[] {
					Ingredient.fromItems(slabItem),
					Ingredient.fromItems(slabItem)
				}
			);

			// 階段→ブロック
			GameRegistry.addShapelessRecipe(new ResourceLocation(modId, brick0.getUnlocalizedName() + "_1"), new ResourceLocation(modId),
				new ItemStack(brick0, 3),
				new Ingredient[] {
					Ingredient.fromItems(stairsItem),
					Ingredient.fromItems(stairsItem),
					Ingredient.fromItems(stairsItem),
					Ingredient.fromItems(stairsItem)
				}
			);

			// ひび割れ
			GameRegistry.addShapelessRecipe(new ResourceLocation(modId, brick1.getUnlocalizedName() + "_2"), new ResourceLocation(modId),
				new ItemStack(brick1, 2),
				new Ingredient[] {
					brick,
					brick,
					recipeBook
				}
			);

			// コケ付き
			GameRegistry.addShapelessRecipe(new ResourceLocation(modId, brick2.getUnlocalizedName() + "_3"), new ResourceLocation(modId),
				new ItemStack(brick2, 3),
				new Ingredient[] {
					brick,
					brick,
					brick,
					recipeBook
				}
			);

			// 無印
			GameRegistry.addShapelessRecipe(new ResourceLocation(modId, brick0.getUnlocalizedName() + "_4"), new ResourceLocation(modId),
				new ItemStack(brick0, 4),
				new Ingredient[] {
					brick,
					brick,
					brick,
					brick,
					recipeBook
				}
			);

			// ブロック→ハーフ
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, slab.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(slab, 6),
				new Object[] {
					"BBB",
					'B', brick
				}
			);

			// ブロック→階段
			GameRegistry.addShapedRecipe(new ResourceLocation(modId, stairs.getUnlocalizedName()), new ResourceLocation(modId),
				new ItemStack(stairs, 8),
				new Object[] {
					"  B",
					" BB",
					"BBB",
					'B', brick
				}
			);
		}
	}

    public static Multimap<String,IRecipe> recipeMultimap = HashMultimap.create();

    public static void addRecipe(String key, IRecipe value) {
        addRecipe(key, value, false);
    }

    public static void addRecipe(String key, IRecipe value, boolean isDummy) {
        if(!isDummy) {
            if(value.getRegistryName() == null)
				value.setRegistryName(new ResourceLocation(value.getGroup()));
            ForgeRegistries.RECIPES.register(value);
        }
        recipeMultimap.put(key, value);
    }

	public static void registerSmelting() {
		//たまごっちメモ：(精錬前のアイテムまたはブロック)、（精錬後のアイテムまたはブロック）、精錬時に出る経験値
		GameRegistry.addSmelting(ItemInit.watercup, new ItemStack(ItemInit.salt, 16), 0.3F);
		GameRegistry.addSmelting(ItemInit.plant_chips, new ItemStack(Items.DYE, 1, 15), 0.05F);
		GameRegistry.addSmelting(ItemInit.sweetpotato, new ItemStack(ItemInit.yaki_imo), 0.1F);
		GameRegistry.addSmelting(ItemInit.eggplant, new ItemStack(ItemInit.yakinasu), 0.1F);
		GameRegistry.addSmelting(BlockInit.chestnut_log, new ItemStack(Items.COAL), 0.15F);
		GameRegistry.addSmelting(BlockInit.coconut_log, new ItemStack(Items.COAL), 0.15F);
		GameRegistry.addSmelting(BlockInit.lemon_log, new ItemStack(Items.COAL), 0.15F);
		GameRegistry.addSmelting(BlockInit.orange_log, new ItemStack(Items.COAL), 0.15F);
		GameRegistry.addSmelting(BlockInit.prism_log, new ItemStack(Items.COAL), 0.15F);
		GameRegistry.addSmelting(ItemInit.banana, new ItemStack(ItemInit.yaki_banana), 0.1F);
		GameRegistry.addSmelting(ItemInit.estor_apple, new ItemStack(ItemInit.yaki_apple), 0.1F);
	}

	// JEI追加レシピ
	public static void JEIaddRecipe () {
        JeiRecipeMFTank.init();
	}
}
