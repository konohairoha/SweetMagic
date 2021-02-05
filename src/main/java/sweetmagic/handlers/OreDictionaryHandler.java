package sweetmagic.handlers;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;

public class OreDictionaryHandler {

	//鉱石辞書にある素材名と自分のMODの素材を紐付けする
	public static void registerOreDictionary() {
		OreDictionary.registerOre("slimeball", ItemInit.sticky_stuff_petal);
		OreDictionary.registerOre("plankWood", BlockInit.chestnut_planks);
		OreDictionary.registerOre("slabWood", BlockInit.chestnut_slab);
		OreDictionary.registerOre("logWood", BlockInit.chestnut_log);
        OreDictionary.registerOre("treeLeaves",BlockInit.chestnut_leaves);
        OreDictionary.registerOre("treeSapling",BlockInit.chestnut_sapling);
		OreDictionary.registerOre("plankWood", BlockInit.lemon_planks);
		OreDictionary.registerOre("slabWood", BlockInit.lemon_slab);
		OreDictionary.registerOre("logWood", BlockInit.lemon_log);
        OreDictionary.registerOre("treeLeaves",BlockInit.lemon_leaves);
        OreDictionary.registerOre("treeSapling",BlockInit.lemon_sapling);
		OreDictionary.registerOre("plankWood", BlockInit.orange_planks);
		OreDictionary.registerOre("slabWood", BlockInit.lemon_slab);
		OreDictionary.registerOre("logWood", BlockInit.orange_log);
        OreDictionary.registerOre("treeLeaves",BlockInit.orange_leaves);
        OreDictionary.registerOre("treeSapling",BlockInit.orange_sapling);
		OreDictionary.registerOre("plankWood", BlockInit.coconut_planks);
		OreDictionary.registerOre("slabWood", BlockInit.coconut_slab);
        OreDictionary.registerOre("treeLeaves",BlockInit.coconut_leaves);
        OreDictionary.registerOre("treeSapling",BlockInit.coconut_sapling);
		OreDictionary.registerOre("plankWood", BlockInit.coconut_planks);
		OreDictionary.registerOre("logWood", BlockInit.coconut_log);
        OreDictionary.registerOre("treeLeaves",BlockInit.coconut_leaves);
        OreDictionary.registerOre("treeSapling",BlockInit.coconut_sapling);
		OreDictionary.registerOre("logWood", BlockInit.prism_log);
        OreDictionary.registerOre("treeLeaves",BlockInit.prism_leaves);
        OreDictionary.registerOre("treeSapling",BlockInit.prism_sapling);
		OreDictionary.registerOre("plankWood", BlockInit.prism_planks);
		OreDictionary.registerOre("slabWood", BlockInit.prism_slab);
        OreDictionary.registerOre("treeLeaves",BlockInit.banana_leaves);
        OreDictionary.registerOre("treeSapling",BlockInit.banana_sapling);
        OreDictionary.registerOre("doorWood",ItemInit.black_moderndoor);
        OreDictionary.registerOre("doorWood",ItemInit.brown_2paneldoor);
        OreDictionary.registerOre("doorWood",ItemInit.brown_5paneldoor);
        OreDictionary.registerOre("doorWood",ItemInit.brown_arch_plantdoor);
        OreDictionary.registerOre("doorWood",ItemInit.brown_arch_door);
        OreDictionary.registerOre("doorWood",ItemInit.woodgold_3);
        OreDictionary.registerOre("doorWood",ItemInit.whitewoodgold_3);

		OreDictionary.registerOre("smLog", BlockInit.chestnut_log);
		OreDictionary.registerOre("smLog", BlockInit.coconut_log);
		OreDictionary.registerOre("smLog", BlockInit.lemon_log);
		OreDictionary.registerOre("smLog", BlockInit.orange_log);
		OreDictionary.registerOre("smLog", BlockInit.prism_log);

		OreDictionary.registerOre("dyeBlue", BlockInit.cornflower);
		OreDictionary.registerOre("dyeWhite", BlockInit.lily_valley);
		OreDictionary.registerOre("dyeRed", ItemInit.strawberry);
		OreDictionary.registerOre("dyeBrown", ItemInit.chestnut);
		OreDictionary.registerOre("dyeYellow", ItemInit.lemon);
		OreDictionary.registerOre("dyeOrange", ItemInit.orange);
		OreDictionary.registerOre("dyePurple", ItemInit.blueberry);
		OreDictionary.registerOre("dyeGreen", ItemInit.vannila_pods);

		OreDictionary.registerOre("antique_brick", BlockInit.antique_brick_0);
		OreDictionary.registerOre("antique_brick", BlockInit.antique_brick_1);
		OreDictionary.registerOre("antique_brick", BlockInit.antique_brick_2);




		OreDictionary.registerOre("orange_planks", BlockInit.orange_planks);
		OreDictionary.registerOre("slabWood", BlockInit.orange_slab);
		OreDictionary.registerOre("orange_planks", BlockInit.orange_planks_huti1);
		OreDictionary.registerOre("orange_planks", BlockInit.orange_planks_huti3);
		OreDictionary.registerOre("orange_planks", BlockInit.orange_planks_mossy);

		OreDictionary.registerOre("orange_planks_w", BlockInit.orange_planks_w);
		OreDictionary.registerOre("slabWood", BlockInit.orange_wslab);
		OreDictionary.registerOre("orange_planks_w", BlockInit.orange_planks_w_huti1);
		OreDictionary.registerOre("orange_planks_w", BlockInit.orange_planks_w_huti3);
		OreDictionary.registerOre("orange_planks_w", BlockInit.orange_planks_wdamage);
		OreDictionary.registerOre("orange_planks_w", BlockInit.orange_planks_wmossy);

		OreDictionary.registerOre("blockGlass", BlockInit.sugarglass);
		OreDictionary.registerOre("blockGlass", BlockInit.shading_sugarglass);
		OreDictionary.registerOre("blockGlass", BlockInit.frosted_glass);
		OreDictionary.registerOre("blockGlass", BlockInit.frosted_glass_line);
		OreDictionary.registerOre("blockGlass", BlockInit.prismglass);

		OreDictionary.registerOre("stone", BlockInit.old_brick);
		OreDictionary.registerOre("stone", BlockInit.old_brick_b);
		OreDictionary.registerOre("stone", BlockInit.old_brick_g);
		OreDictionary.registerOre("stone", BlockInit.old_brick_l);
		OreDictionary.registerOre("stone", BlockInit.old_brick_r);
		OreDictionary.registerOre("stone", BlockInit.old_brick_y);

		//たまごっち連携用
		OreDictionary.registerOre("hopper", Blocks.HOPPER);
		OreDictionary.registerOre("recipeBook", ItemInit.magic_book);
		OreDictionary.registerOre("recipeBook", ItemInit.guide_book);
		OreDictionary.registerOre("riceSeed", ItemInit.rice_seed);
		OreDictionary.registerOre("flourPowder", ItemInit.flourpowder);
		OreDictionary.registerOre("flourPowder", Items.WHEAT);
		OreDictionary.registerOre("milkBucket", ItemInit.milk_pack);
		OreDictionary.registerOre("milkBucket", Items.MILK_BUCKET);
		OreDictionary.registerOre("breadCrumbs", ItemInit.breadcrumbs);
		OreDictionary.registerOre("cheese", ItemInit.cheese);
		OreDictionary.registerOre("butter", ItemInit.butter);
		OreDictionary.registerOre("strawberry", ItemInit.strawberry);
		OreDictionary.registerOre("oil", ItemInit.olive_oil);
		OreDictionary.registerOre("whippingCream", ItemInit.whipping_cream);
		OreDictionary.registerOre("soyBean", ItemInit.soybean);
		OreDictionary.registerOre("edamame", ItemInit.edamame);
		OreDictionary.registerOre("rice", ItemInit.rice);
		OreDictionary.registerOre("kinako", ItemInit.kinako);
		OreDictionary.registerOre("ine", ItemInit.ine);
		OreDictionary.registerOre("glass", BlockInit.shading_sugarglass);
		OreDictionary.registerOre("glass", BlockInit.sugarglass);
		OreDictionary.registerOre("mfbottle", ItemInit.mf_bottle);
		OreDictionary.registerOre("salt", ItemInit.salt);
		OreDictionary.registerOre("treeSapling", BlockInit.orange_sapling);
		OreDictionary.registerOre("treeSapling", BlockInit.lemon_sapling);
		OreDictionary.registerOre("treeSapling", BlockInit.chestnut_sapling);
		OreDictionary.registerOre("tomato", ItemInit.tomato);
		OreDictionary.registerOre("eggplant", ItemInit.eggplant);
		OreDictionary.registerOre("lettuce", ItemInit.lettuce);
		OreDictionary.registerOre("j_radish", ItemInit.j_radish);
		OreDictionary.registerOre("cabbage", ItemInit.cabbage);
		OreDictionary.registerOre("onion", ItemInit.onion);
		OreDictionary.registerOre("azuki", ItemInit.azuki_seed);
		OreDictionary.registerOre("waterBucket", ItemInit.watercup);
		OreDictionary.registerOre("waterBucket", Items.WATER_BUCKET);
		OreDictionary.registerOre("wool", ItemInit.cotton_cloth);
		OreDictionary.registerOre("blockWool", ItemInit.cotton_cloth);
		OreDictionary.registerOre("itemCloth", ItemInit.cotton_cloth);
		OreDictionary.registerOre("cropCotton", ItemInit.cotton);

		OreDictionary.registerOre("glowstone", BlockInit.gorgeous_lamp);
		OreDictionary.registerOre("glowstone", BlockInit.glow_lamp);
		OreDictionary.registerOre("glowstone", BlockInit.glow_light);
		OreDictionary.registerOre("bucketWater", ItemInit.watercup);
		OreDictionary.registerOre("cropStrawberry", ItemInit.strawberry);
		OreDictionary.registerOre("dustWood", ItemInit.plant_chips);
		OreDictionary.registerOre("dustFlour", ItemInit.flourpowder);
		OreDictionary.registerOre("foodFlour", ItemInit.flourpowder);
		OreDictionary.registerOre("dustBread", ItemInit.breadcrumbs);
		OreDictionary.registerOre("foodButter", ItemInit.butter);
		OreDictionary.registerOre("seedRice", ItemInit.rice_seed);
		OreDictionary.registerOre("cropRice", ItemInit.rice);
		OreDictionary.registerOre("cropLemon", ItemInit.lemon);
		OreDictionary.registerOre("cropTomato", ItemInit.tomato);
		OreDictionary.registerOre("cropOnion", ItemInit.onion);
		OreDictionary.registerOre("cropRadish", ItemInit.j_radish);
		OreDictionary.registerOre("cropEggplant", ItemInit.eggplant);
		OreDictionary.registerOre("cropCabbage", ItemInit.cabbage);
		OreDictionary.registerOre("cropCorn", ItemInit.corn);
		OreDictionary.registerOre("cropChestnut", ItemInit.chestnut);
		OreDictionary.registerOre("cropBlueberry", ItemInit.blueberry);
		OreDictionary.registerOre("cropGreenSoybeans", ItemInit.edamame);
		OreDictionary.registerOre("cropSoybean", ItemInit.soybean);
		OreDictionary.registerOre("cropSoy", ItemInit.soybean);
		OreDictionary.registerOre("cropLettuce", ItemInit.lettuce);
		OreDictionary.registerOre("seedSoybean", ItemInit.soybean);
		OreDictionary.registerOre("listAllwater", ItemInit.watercup);
		OreDictionary.registerOre("cropBean", ItemInit.azuki_seed);
		OreDictionary.registerOre("foodOil", ItemInit.olive_oil);
		OreDictionary.registerOre("foodCheese", ItemInit.cheese);
		OreDictionary.registerOre("bucketMilk", ItemInit.milk_pack);
		OreDictionary.registerOre("foodCream", ItemInit.whipping_cream);
		OreDictionary.registerOre("bread", ItemInit.toast);
		OreDictionary.registerOre("listAllberry", ItemInit.blueberry);
		OreDictionary.registerOre("listAllberry", ItemInit.raspberry);
		OreDictionary.registerOre("foodDough", ItemInit.cake_dough);
		OreDictionary.registerOre("foodCustard", ItemInit.custard);
		OreDictionary.registerOre("listAllsuggar", ItemInit.vannila_essence);
		OreDictionary.registerOre("foodGelatine", ItemInit.gelatin);
		OreDictionary.registerOre("foodMarshmellows", ItemInit.marshmallow);
		OreDictionary.registerOre("dustSalt", ItemInit.salt);
		OreDictionary.registerOre("dustSugar", Items.SUGAR);
		OreDictionary.registerOre("cropMelon", Items.MELON);
		OreDictionary.registerOre("listAllmushroom", Blocks.BROWN_MUSHROOM);
		OreDictionary.registerOre("listAllmushroom", Blocks.RED_MUSHROOM);
		OreDictionary.registerOre("listAllmushroom", ItemInit.whitenet);
		OreDictionary.registerOre("listAllbeefraw", Items.BEEF);
		OreDictionary.registerOre("listAllmeatraw", Items.BEEF);
		OreDictionary.registerOre("listAllmeatraw", Items.CHICKEN);
		OreDictionary.registerOre("listAllchikenraw", Items.CHICKEN);
		OreDictionary.registerOre("listAllmeatraw", Items.PORKCHOP);
		OreDictionary.registerOre("listAllporkraw", Items.PORKCHOP);
		OreDictionary.registerOre("listAllfruit", ItemInit.banana);
		OreDictionary.registerOre("listAllfruit", ItemInit.strawberry);
		OreDictionary.registerOre("listAllfruit", ItemInit.blueberry);
		OreDictionary.registerOre("listAllfruit", ItemInit.chestnut);
		OreDictionary.registerOre("listAllfruit", ItemInit.lemon);
		OreDictionary.registerOre("listAllfruit", ItemInit.orange);
		OreDictionary.registerOre("listAllveggie", ItemInit.onion);
		OreDictionary.registerOre("listAllveggie", ItemInit.corn);
		OreDictionary.registerOre("listAllveggie", ItemInit.tomato);
		OreDictionary.registerOre("listAllveggie", ItemInit.j_radish);
		OreDictionary.registerOre("listAllveggie", ItemInit.eggplant);
		OreDictionary.registerOre("listAllveggie", ItemInit.cabbage);
		OreDictionary.registerOre("listAllveggie", ItemInit.lettuce);
		OreDictionary.registerOre("listAllseed", ItemInit.sannyflower_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.moonblossom_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.dm_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.sugarbell_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.clerodendrum_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.fire_nasturtium_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.sticky_stuff_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.glowflower_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.cotton_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.whitenet_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.rice_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.corn_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.eggplant_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.j_radish_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.lettuce_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.cabbage_seed);
		OreDictionary.registerOre("listAllseed", ItemInit.azuki_seed);
	}
}