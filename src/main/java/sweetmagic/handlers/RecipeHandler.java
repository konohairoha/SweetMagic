package sweetmagic.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	public static final String MODID =  SweetMagicCore.MODID;
	public static final ResourceLocation MODSRC =  new ResourceLocation(MODID);

    //レシピ定数List格納
	public static void registerCrafting() {

		String MODID = SweetMagicCore.MODID;
		Ingredient recipeBook = Ingredient.fromStacks(SMUtil.getOreArray("recipeBook"));
		Ingredient alt_ingot = Ingredient.fromStacks(new ItemStack(ItemInit.alternative_ingot));
		Ingredient cobble = Ingredient.fromStacks(SMUtil.getOreArray("cobblestone"));
		Ingredient slabWood = Ingredient.fromStacks(SMUtil.getOreArray("slabWood"));
		Ingredient stickWood = Ingredient.fromStacks(SMUtil.getOreArray("stickWood"));
		Ingredient foodRice = Ingredient.fromStacks(SMUtil.getOreArray("foodRice"));
		Ingredient dustSalt = Ingredient.fromStacks(SMUtil.getOreArray("dustSalt"));
		Ingredient dry_seaweed = Ingredient.fromStacks(new ItemStack(ItemInit.dry_seaweed));
		Ingredient compost_drit = Ingredient.fromStacks(new ItemStack(BlockInit.compost_drit));
		Ingredient treeSapling = Ingredient.fromStacks(SMUtil.getOreArray("treeSapling"));
		Ingredient iron_bars = Ingredient.fromStacks(new ItemStack(Blocks.IRON_BARS));
		Ingredient ironNugget = Ingredient.fromStacks(new ItemStack(Items.IRON_NUGGET));

		/*草ブロックから粘土*/
		if (SMConfig.help_recipe) {
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "help_magicmeal"), MODSRC,
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
		GameRegistry.addShapelessRecipe(new ResourceLocation(MODID, "alternative_ingot"), MODSRC,
			new ItemStack(ItemInit.alternative_ingot, 4),
			new Ingredient[] { Ingredient.fromStacks(new ItemStack(BlockInit.alt_block)) }
		);

		// アルストロメリア
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "alstroemeria"), MODSRC,
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
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "alt_axe"), MODSRC,
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
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "alt_bucket_2"), MODSRC,
			new ItemStack(ItemInit.alt_bucket),
			new Object[] {
				"A A",
				" A ",
				'A', ItemInit.alternative_ingot
			}
		);

		// オルタナティブバケツ
		GameRegistry.addShapelessRecipe(new ResourceLocation(MODID, "alt_bucket_1"), MODSRC,
			new ItemStack(ItemInit.alt_bucket),
			new Ingredient[] { Ingredient.fromStacks(new ItemStack(ItemInit.alt_bucket_lava)) }
		);

		// オルタナティブバケツ
		GameRegistry.addShapelessRecipe(new ResourceLocation(MODID, "alt_bucket_0"), MODSRC,
			new ItemStack(ItemInit.alt_bucket),
			new Ingredient[] { Ingredient.fromStacks(new ItemStack(ItemInit.alt_bucket_water)) }
		);

		// オルタナティブクワ
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "alt_hoe"), MODSRC,
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
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "alt_sickle"), MODSRC,
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
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "alt_pick"), MODSRC,
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
		GameRegistry.addShapelessRecipe(new ResourceLocation(MODID, "alt_shears"), MODSRC,
			new ItemStack(ItemInit.alt_shears),
			new Ingredient[] { alt_ingot, alt_ingot }
		);

		// オルタナティブシャベル
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "alt_shovel"), MODSRC,
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
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "alt_sword"), MODSRC,
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
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "machete"), MODSRC,
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
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "magic_book"), MODSRC,
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
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "mftank"), MODSRC,
			new ItemStack(BlockInit.mftank),
			new Object[] {
				"SSS",
				"SBS",
				"SSS",
				'S', BlockInit.sugarglass,
				'B', ItemInit.magic_book
			}
		);

		// カフェテーブル
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "cafe_table"), MODSRC,
			new ItemStack(BlockInit.cafe_table, 6),
			new Object[] {
				"WWW",
				" I ",
				"III",
				'W', "smPlanks",
				'I', Items.IRON_INGOT
			}
		);

		// カフェチェア
		GameRegistry.addShapedRecipe(new ResourceLocation(MODID, "cafe_chair"), MODSRC,
			new ItemStack(BlockInit.cafe_chair, 6),
			new Object[] {
				"W  ",
				"WWW",
				"I I",
				'W', "smPlanks",
				'I', Items.IRON_INGOT
			}
		);

		// MFテーブル
		addShapelessRecipe( "mftable",
			new Ingredient[] {
				Ingredient.fromStacks(new ItemStack(Blocks.CARPET, 1, 14)),
				Ingredient.fromStacks(new ItemStack(Blocks.CRAFTING_TABLE)),
				Ingredient.fromStacks(new ItemStack(ItemInit.magic_book))
			},
			new ItemStack(BlockInit.mftable)
		);

		// 柱
		addShapelessRecipe( "pillar_stone",
			new Ingredient[] { Ingredient.fromStacks(new ItemStack(Blocks.STONEBRICK, 1, 3)), recipeBook },
			new ItemStack(BlockInit.pillar_stone, 4)
		);

        // 柱
		addShapelessRecipe( "pillar_white",
			new Ingredient[] { Ingredient.fromStacks(new ItemStack(Blocks.QUARTZ_BLOCK, 1, 32767)), recipeBook },
			new ItemStack(BlockInit.pillar_stone_w, 4)
		);

		// 料理皿
		addShapelessRecipe( "plate",
			new Ingredient[] { iron_bars, iron_bars, recipeBook },
			new ItemStack(BlockInit.plate, 4)
		);

		// 木皿
		addShapelessRecipe( "wood_plate",
			new Ingredient[] { slabWood, slabWood, recipeBook },
			new ItemStack(BlockInit.wood_plate, 4)
		);

		// 木皿
		addShapelessRecipe( "iron_plate",
			new Ingredient[] { ironNugget, ironNugget, recipeBook },
			new ItemStack(BlockInit.iron_plate, 4)
		);

		// ローブ
		Map<Item, String> mapRecipe = new HashMap<>();
		mapRecipe.put(ItemInit.wizard_robe, "dyeBlack");
		mapRecipe.put(ItemInit.feary_robe, "dyeGreen");
		mapRecipe.put(ItemInit.windine_robe, "dyeBlue");
		mapRecipe.put(ItemInit.ifrite_robe, "dyeRed");
		mapRecipe.put(ItemInit.sandryon_robe, "dyeYellow");

		for (Entry<Item, String> map : mapRecipe.entrySet()) {

			// ローブ
			Item robe = map.getKey();

			RecipeHandler.addRecipe(robe.getUnlocalizedName(),
				new RecipeNBTExtend(new ResourceLocation(MODID, robe.getUnlocalizedName()),
				new ItemStack(robe),
					"APA",
					"DRD",
					"APA",
					'D', map.getValue(),
					'P', ItemInit.pure_crystal,
					'A', "magicAccessori",
					'R', "magicians_grobe"
				)
			);
		}

		// カフェキッチンシンク
		RecipeHandler.addRecipe("cafe_kitchen_sink",
			new RecipeNBTExtend(new ResourceLocation(MODID, "cafe_kitchen_sink"),
			new ItemStack(BlockInit.cafe_kitchen_sink),
				"III",
				" C ",
				'I', Items.IRON_NUGGET,
				'C', BlockInit.cafe_kitchen_table
			)
		);

		// シュガーベルの種
		addShapelessRecipe( "sugarbell_seed",
			new Ingredient[] {
				Ingredient.fromStacks(SMUtil.getOreArray("listAllseed")),
				Ingredient.fromStacks(SMUtil.getOreArray("dirt"))
			},
			new ItemStack(ItemInit.sugarbell_seed, 4)
		);

		// 鮭おにぎり
		addShapelessRecipe( "riceball_salmon",
			new Ingredient[] {
				foodRice,
				foodRice,
				dry_seaweed,
				Ingredient.fromStacks(new ItemStack(Items.COOKED_FISH, 1, 1))
			},
			new ItemStack(ItemInit.riceball_salmon, 3)
		);

		// 塩おにぎり
		addShapelessRecipe( "foodRice",
			new Ingredient[] {
				foodRice,
				foodRice,
				dry_seaweed,
				dustSalt
			},
			new ItemStack(ItemInit.riceball_salt, 3)
		);

		// ブロック、ハーフ、階段をリストに突っ込む
		List<RecipeRegisterHelper> recipeList = new ArrayList<>();
		recipeList.add(new RecipeRegisterHelper(BlockInit.chestnut_planks, BlockInit.chestnut_stairs, BlockInit.chestnut_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.lemon_planks, BlockInit.lemon_stairs, BlockInit.lemon_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.coconut_planks, BlockInit.coconut_stairs, BlockInit.coconut_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.prism_planks, BlockInit.prism_stairs, BlockInit.prism_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.estor_planks, BlockInit.estor_stairs, BlockInit.estor_slab));
		recipeList.add(new RecipeRegisterHelper(BlockInit.peach_planks, BlockInit.peach_stairs, BlockInit.peach_slab));
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

			Ingredient slabItem = Ingredient.fromItems(SMUtil.getItemBlock(slab));
			Ingredient stairsItem = Ingredient.fromItems(SMUtil.getItemBlock(stairs));

			// ハーフ→ブロック
			addShapelessRecipe( planks.getUnlocalizedName() + "_0",
				new Ingredient[] {
					slabItem, slabItem
				},
				new ItemStack(planks)
			);

			// 階段→ブロック
			addShapelessRecipe( planks.getUnlocalizedName() + "_1",
				new Ingredient[] {
					stairsItem, stairsItem, stairsItem, stairsItem
				},
				new ItemStack(planks)
			);

			// ブロック→ハーフ
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, slab.getUnlocalizedName()), MODSRC,
				new ItemStack(slab, 6),
				new Object[] {
					"BBB",
					'B', planks
				}
			);

			// ブロック→階段
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, stairs.getUnlocalizedName()), MODSRC,
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
		recipeList2.add(new RecipeRegisterHelper(BlockInit.peach_log, BlockInit.peach_planks, BlockInit.peach_plate, false));

		for (RecipeRegisterHelper recipe : recipeList2) {

			Block log = recipe.getLog();
			Block planks = recipe.getPlanks();
			Block plate = recipe.getPlate();

			// 原木→ブロック
			addShapelessRecipe( planks.getUnlocalizedName() + "_2",
				new Ingredient[] { Ingredient.fromStacks(new ItemStack(log)) },
				new ItemStack(planks, 4)
			);

			// ブロック→感圧版
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, plate.getUnlocalizedName()), MODSRC,
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

		for (RecipeRegisterHelper recipe : recipeList3) {

			// ブロックとアイテムの取得
			Block stone = recipe.getStone();
			Ingredient dye = Ingredient.fromStacks(SMUtil.getOreArray(recipe.getOre()));

			// 素材→古びたレンガ
			addShapelessRecipe( stone.getUnlocalizedName() + "__0",
				new Ingredient[] {
					cobble, cobble, cobble, cobble, cobble, cobble, cobble,
					dye,
					recipeBook
				},
				new ItemStack(stone, 9)
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
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, pane.getUnlocalizedName()), MODSRC,
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

			Ingredient slabItem = Ingredient.fromItems(SMUtil.getItemBlock(slab));
			Ingredient stairsItem = Ingredient.fromItems(SMUtil.getItemBlock(stairs));

			// ハーフ→ブロック
			addShapelessRecipe( brick0.getUnlocalizedName() + "_0",
				new Ingredient[] {
					slabItem, slabItem
				},
				new ItemStack(brick0)
			);

			// 階段→ブロック
			addShapelessRecipe( brick0.getUnlocalizedName() + "_0",
				new Ingredient[] {
					stairsItem, stairsItem, stairsItem, stairsItem
				},
				new ItemStack(brick0, 3)
			);

			// ひび割れ
			addShapelessRecipe( brick1.getUnlocalizedName() + "_2",
				new Ingredient[] {
					brick, brick,
					recipeBook
				},
				new ItemStack(brick1, 2)
			);

			// コケ付き
			addShapelessRecipe( brick2.getUnlocalizedName() + "_3",
				new Ingredient[] {
					brick, brick, brick,
					recipeBook
				},
				new ItemStack(brick2, 3)
			);

			// 無印
			addShapelessRecipe( brick0.getUnlocalizedName() + "_4",
				new Ingredient[] {
					brick, brick, brick, brick,
					recipeBook
				},
				new ItemStack(brick0, 4)
			);

			// ブロック→ハーフ
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, slab.getUnlocalizedName()), MODSRC,
				new ItemStack(slab, 6),
				new Object[] {
					"BBB",
					'B', brick
				}
			);

			// ブロック→階段
			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, stairs.getUnlocalizedName()), MODSRC,
				new ItemStack(stairs, 8),
				new Object[] {
					"  B",
					" BB",
					"BBB",
					'B', brick
				}
			);
		}

		Map<Block, Block> busketRecipe = new HashMap<>();
		busketRecipe.put(BlockInit.iberis_umbellata, BlockInit.iberis_umbellata_basket);
		busketRecipe.put(BlockInit.campanula, BlockInit.campanula_basket);
		busketRecipe.put(BlockInit.primula_polyansa, BlockInit.primula_polyansa_basket);
		busketRecipe.put(BlockInit.christmas_rose, BlockInit.christmas_rose_basket);
		busketRecipe.put(BlockInit.portulaca, BlockInit.portulaca_basket);

		for (Entry<Block, Block> map : busketRecipe.entrySet()) {

			Block flower = map.getKey();
			Block basket = map.getValue();

			addShapelessRecipe( basket.getUnlocalizedName(),
				new Ingredient[] {
					Ingredient.fromStacks(new ItemStack(flower)),
					stickWood,
					stickWood,
					stickWood
				},
				new ItemStack(basket, 4)
			);
		}

		// ウォールラック
		Map<Block, String> wallRecipe = new HashMap<>();
		wallRecipe.put(BlockInit.moden_wallrack, "dyeBrown");
		wallRecipe.put(BlockInit.moden_wallrack_b, "dyeBlack");
		wallRecipe.put(BlockInit.moden_wallrack_l, "dyeWhite");
		wallRecipe.put(BlockInit.cafe_wallrack, "recipeBook");

		for (Entry<Block, String> map : wallRecipe.entrySet()) {

			Block block = map.getKey();

			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, block.getUnlocalizedName()), MODSRC,
				new ItemStack(block, 4),
				new Object[] {
					"PSP",
					"SDS",
					'P', slabWood,
					'S', stickWood,
					'D', map.getValue()
				}
			);
		}

		// 植木鉢
		Map<Ingredient, Block> potRecipe = new HashMap<>();
		potRecipe.put(Ingredient.fromStacks(SMUtil.getOreArray("orange_planks_w")), BlockInit.orange_planks_pot_w);
		potRecipe.put(Ingredient.fromStacks(SMUtil.getOreArray("orange_planks")), BlockInit.orange_planks_pot);
		potRecipe.put(Ingredient.fromStacks(SMUtil.getOreArray("antique_brick")), BlockInit.antique_brick_pot_r);
		potRecipe.put(Ingredient.fromStacks(new ItemStack(BlockInit.estor_planks)), BlockInit.estor_planks_pot);
		potRecipe.put(Ingredient.fromStacks(new ItemStack(BlockInit.longtile_brick_l)), BlockInit.longtile_brick_pot_l);
		potRecipe.put(Ingredient.fromStacks(new ItemStack(BlockInit.longtile_brick_o)), BlockInit.longtile_brick_pot_o);

		for (Entry<Ingredient, Block> map : potRecipe.entrySet()) {

			Block block = map.getValue();

			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, block.getUnlocalizedName()), MODSRC,
				new ItemStack(block, 4),
				new Object[] {
					"BDB",
					" B ",
					'B', map.getKey(),
					'D', compost_drit
				}
			);
		}

		// トラップドア
		Map<Ingredient, Block> trapdoorRecipe = new HashMap<>();
		trapdoorRecipe.put(Ingredient.fromStacks(SMUtil.getOreArray("antique_brick")), BlockInit.antique_tdoor_0);
		trapdoorRecipe.put(Ingredient.fromStacks(SMUtil.getOreArray("antique_brick_white")), BlockInit.antique_tdoor_0w);
		trapdoorRecipe.put(Ingredient.fromStacks(SMUtil.getOreArray("antique_brick_black")), BlockInit.antique_tdoor_b);
		trapdoorRecipe.put(Ingredient.fromStacks(SMUtil.getOreArray("antique_brick_brown")), BlockInit.antique_tdoor_l);
		trapdoorRecipe.put(Ingredient.fromStacks(new ItemStack(BlockInit.coconut_planks)), BlockInit.coconut_trapdoor);
		trapdoorRecipe.put(Ingredient.fromStacks(new ItemStack(BlockInit.longtile_brick_p)), BlockInit.longtile_brick_p_tdoor);
		trapdoorRecipe.put(Ingredient.fromStacks(new ItemStack(BlockInit.longtile_brick_r)), BlockInit.longtile_brick_r_tdoor);

		for (Entry<Ingredient, Block> map : trapdoorRecipe.entrySet()) {

			Block block = map.getValue();

			GameRegistry.addShapedRecipe(new ResourceLocation(MODID, block.getUnlocalizedName()), MODSRC,
				new ItemStack(block, 8),
				new Object[] {
					"BBB",
					"BBB",
					'B', map.getKey()
				}
			);
		}

		// 植え込み
		Map<Block, Block> plantRecipe = new HashMap<>();
		plantRecipe.put(BlockInit.chestnut_leaves, BlockInit.chestnut_planting);
		plantRecipe.put(BlockInit.orange_leaves, BlockInit.orange_planting);
		plantRecipe.put(BlockInit.estor_leaves, BlockInit.estor_planting);
		plantRecipe.put(BlockInit.peach_leaves, BlockInit.peach_planting);

		for (Entry<Block, Block> map : plantRecipe.entrySet()) {

			Ingredient plant = Ingredient.fromStacks(new ItemStack(map.getKey()));
			Block leave = map.getValue();

			addShapelessRecipe( leave.getUnlocalizedName(),
				new Ingredient[] {
					plant,
					plant,
					treeSapling
				},
				new ItemStack(leave, 8)
			);
		}

		// 単体クラフト
		singleCraft(new ItemStack(BlockInit.magicbook), new ItemStack(Items.BOOK, 3));
		singleCraft(new ItemStack(ItemInit.whipping_cream), new ItemStack(ItemInit.butter, 2));
		singleCraft(new ItemStack(ItemInit.cabbage), new ItemStack(ItemInit.cabbage_seed));
		singleCraft(new ItemStack(ItemInit.clero_petal), new ItemStack(ItemInit.clero_petal));
		singleCraft(new ItemStack(ItemInit.corn), new ItemStack(ItemInit.corn_seed));
		singleCraft(new ItemStack(BlockInit.cornflower), new ItemStack(Items.DYE, 2 , 6));
		singleCraft(new ItemStack(ItemInit.eggplant), new ItemStack(ItemInit.eggplant_seed));
		singleCraft(new ItemStack(ItemInit.j_radish), new ItemStack(ItemInit.j_radish_seed));
		singleCraft(new ItemStack(ItemInit.spinach), new ItemStack(ItemInit.spinach_seed));
		singleCraft(new ItemStack(BlockInit.lemon_trapdoor_n), new ItemStack(BlockInit.lemon_trapdoor));
		singleCraft(new ItemStack(ItemInit.lettuce), new ItemStack(ItemInit.lettuce_seed));
		singleCraft(new ItemStack(ItemInit.ine), new ItemStack(ItemInit.rice_seed, 2));
		singleCraft(new ItemStack(ItemInit.cotton), new ItemStack(Items.STRING, 2));
		singleCraft(new ItemStack(ItemInit.sugarbell), new ItemStack(Items.SUGAR, 2), "sugar_0");
		singleCraft(new ItemStack(ItemInit.coconut), new ItemStack(Items.SUGAR, 4), "sugar_1");
		singleCraft(new ItemStack(Items.WATER_BUCKET), new ItemStack(ItemInit.watercup, 8));
		singleCraft(new ItemStack(Items.MILK_BUCKET), new ItemStack(ItemInit.milk_pack, 8));
		singleCraft(new ItemStack(ItemInit.milk_pack), new ItemStack(ItemInit.whipping_cream, 2));
		singleCraft(new ItemStack(BlockInit.white_ironfence), new ItemStack(BlockInit.black_ironfence));
		singleCraft(new ItemStack(BlockInit.black_ironfence), new ItemStack(BlockInit.white_ironfence));
		singleCraft(new ItemStack(BlockInit.orange_log), new ItemStack(BlockInit.orange_planks, 4));
		singleCraft(new ItemStack(BlockInit.lemon_trapdoor), new ItemStack(BlockInit.lemon_trapdoor_n));
	}

    public static Multimap<String,IRecipe> recipeMultimap = HashMultimap.create();

    // MBT引継ぎクラフト用
    public static void addRecipe(String key, IRecipe value) {

        if(value.getRegistryName() == null) {
			value.setRegistryName(new ResourceLocation(value.getGroup()));
        }

        ForgeRegistries.RECIPES.register(value);
        recipeMultimap.put(key, value);
    }

    // 複数不定レシピ
    public static void addShapelessRecipe (String name, Ingredient[] ing, ItemStack resalt) {
		GameRegistry.addShapelessRecipe(new ResourceLocation(MODID, name), MODSRC, resalt, ing );
    }

    // 単体クラフト用
    public static void singleCraft (ItemStack useStack, ItemStack resaltStack) {
		GameRegistry.addShapelessRecipe(new ResourceLocation(MODID, resaltStack.getUnlocalizedName()), MODSRC,
				resaltStack,
			new Ingredient[] { Ingredient.fromStacks(useStack) }
		);
    }

    // 単体クラフト用
    public static void singleCraft (ItemStack useStack, ItemStack resaltStack, String name) {
		GameRegistry.addShapelessRecipe(new ResourceLocation(MODID, name), MODSRC,
				resaltStack,
			new Ingredient[] { Ingredient.fromStacks(useStack) }
		);
    }

	public static void registerSmelting() {
		//たまごっちメモ：(精錬前のアイテムまたはブロック)、（精錬後のアイテムまたはブロック）、精錬時に出る経験値
		GameRegistry.addSmelting(ItemInit.watercup, new ItemStack(ItemInit.salt, 16), 0.3F);
		GameRegistry.addSmelting(ItemInit.plant_chips, new ItemStack(Items.DYE, 1, 15), 0.05F);
		GameRegistry.addSmelting(ItemInit.sweetpotato, new ItemStack(ItemInit.yaki_imo), 0.1F);
		GameRegistry.addSmelting(ItemInit.eggplant, new ItemStack(ItemInit.yakinasu), 0.1F);
		GameRegistry.addSmelting(BlockInit.chestnut_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(BlockInit.coconut_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(BlockInit.lemon_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(BlockInit.orange_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(BlockInit.prism_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(BlockInit.estor_log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(ItemInit.banana, new ItemStack(ItemInit.yaki_banana), 0.1F);
		GameRegistry.addSmelting(ItemInit.estor_apple, new ItemStack(ItemInit.yaki_apple), 0.1F);
	}

	// JEI追加レシピ
	public static void JEIaddRecipe () {
        JeiRecipeMFTank.init();
	}
}
