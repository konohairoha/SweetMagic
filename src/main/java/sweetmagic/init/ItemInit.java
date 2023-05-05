package sweetmagic.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.item.sm.accessorie.SMAcce;
import sweetmagic.init.item.sm.armor.AetherChoker;
import sweetmagic.init.item.sm.armor.AngelHarness;
import sweetmagic.init.item.sm.armor.FarmClothes;
import sweetmagic.init.item.sm.armor.MagiciansPouch;
import sweetmagic.init.item.sm.armor.MagiciansRobe;
import sweetmagic.init.item.sm.eitem.SMAcceType;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.magic.AetherFlashLight;
import sweetmagic.init.item.sm.magic.AetherHammer;
import sweetmagic.init.item.sm.magic.AirMagic;
import sweetmagic.init.item.sm.magic.CardHeal;
import sweetmagic.init.item.sm.magic.CardMagic;
import sweetmagic.init.item.sm.magic.ChargeMagic;
import sweetmagic.init.item.sm.magic.FieldMagic;
import sweetmagic.init.item.sm.magic.MFItem;
import sweetmagic.init.item.sm.magic.MFStuff;
import sweetmagic.init.item.sm.magic.MFTeleport;
import sweetmagic.init.item.sm.magic.MFTime;
import sweetmagic.init.item.sm.magic.MFWeather;
import sweetmagic.init.item.sm.magic.MagicMeal;
import sweetmagic.init.item.sm.magic.MagicianBeginnerBook;
import sweetmagic.init.item.sm.magic.RunkUpMagic;
import sweetmagic.init.item.sm.magic.ShotMagic;
import sweetmagic.init.item.sm.magic.StarLightWand;
import sweetmagic.init.item.sm.magic.SummonMagic;
import sweetmagic.init.item.sm.magic.TouchMagic;
import sweetmagic.init.item.sm.seed.SMSeed;
import sweetmagic.init.item.sm.seed.SMSeedFood;
import sweetmagic.init.item.sm.sweetmagic.CosmicWand;
import sweetmagic.init.item.sm.sweetmagic.MagicianWand;
import sweetmagic.init.item.sm.sweetmagic.SMAxe;
import sweetmagic.init.item.sm.sweetmagic.SMBook;
import sweetmagic.init.item.sm.sweetmagic.SMBookCosmic;
import sweetmagic.init.item.sm.sweetmagic.SMBucket;
import sweetmagic.init.item.sm.sweetmagic.SMCushion;
import sweetmagic.init.item.sm.sweetmagic.SMDrink;
import sweetmagic.init.item.sm.sweetmagic.SMDropItem;
import sweetmagic.init.item.sm.sweetmagic.SMElementItem;
import sweetmagic.init.item.sm.sweetmagic.SMFood;
import sweetmagic.init.item.sm.sweetmagic.SMFuel;
import sweetmagic.init.item.sm.sweetmagic.SMGuidBook;
import sweetmagic.init.item.sm.sweetmagic.SMHarvestBag;
import sweetmagic.init.item.sm.sweetmagic.SMHoe;
import sweetmagic.init.item.sm.sweetmagic.SMIDoor;
import sweetmagic.init.item.sm.sweetmagic.SMItem;
import sweetmagic.init.item.sm.sweetmagic.SMKey;
import sweetmagic.init.item.sm.sweetmagic.SMLootBag;
import sweetmagic.init.item.sm.sweetmagic.SMPhone;
import sweetmagic.init.item.sm.sweetmagic.SMPick;
import sweetmagic.init.item.sm.sweetmagic.SMShears;
import sweetmagic.init.item.sm.sweetmagic.SMShovel;
import sweetmagic.init.item.sm.sweetmagic.SMSickle;
import sweetmagic.init.item.sm.sweetmagic.SMSword;
import sweetmagic.init.item.sm.sweetmagic.SMWand;

public class ItemInit {

	// 魔法本
	public static Item magic_book, guide_book, magic_book_cosmic, magic_book_scarlet;

	// マジックハウス
	public static Item magicianbeginner_book, smhouse;

    // 天気花びら、種
	public static Item sannyflower_seed, sannyflower_petal;
	public static Item moonblossom_seed, moonblossom_petal;
	public static Item dm_seed, dm_flower;

    // 鉱石、クリスタル
	public static Item alternative_ingot, cosmos_light_ingot, aether_crystal_shard, cosmic_crystal_shard;
	public static Item aether_crystal, divine_crystal, pure_crystal, deus_crystal, cosmic_crystal;

	// エーテルツール
	public static Item alt_axe, alt_pick, alt_shovel, alt_hoe, alt_shears, alt_sword, alt_sickle;
	public static Item alt_bucket, alt_bucket_water, alt_bucket_lava;
	public static Item knife_of_thief, machete;
	public static Item kitt_gadget, kitt_jet, aether_rifle, aether_hammer;
	public static Item aether_flashlight;

	// 袋
	public static Item seedbag, eggbag, accebag, flowerpack;
	public static Item seed_harvest_bag, sapling_harvest_bag;

	public static Item sm_phone, sm_phone_r, sm_phone_p, sm_phone_b, sm_phone_o, sm_phone_l, sm_phone_i;

    // 花びら、種
	public static Item magicmeal, clero_petal, clerodendrum_seed, prizmium;
	public static Item fire_powder, fire_nasturtium_petal, fire_nasturtium_seed;
	public static Item sticky_stuff_petal, sticky_stuff_seed, glowflower_seed;
	public static Item cotton, cotton_seed, quartz_seed;

    // MFボトル
	public static Item b_mf_bottle, b_mf_magiabottle, mf_sbottle, mf_bottle, mf_magiabottle;

	// 魔法素材
	public static Item mysterious_page, ender_shard, stray_soul, electronic_orb, poison_bottle, witch_tears, veil_darkness;

	// 魔法素材（魔法）
	public static Item unmeltable_ice, grav_powder, tiny_feather, blank_page, blank_magic, cotton_cloth, mystical_page;

	// 杖
	public static Item mf_stuff, magician_wand, startlight_wand;
	public static Item aether_wand, divine_wand, purecrystal_wand, deuscrystal_wand, cosmiccrystal_wand, creative_wand;

	// エーテルワンド
	public static Item aether_wand_r, aether_wand_g, aether_wand_b, aether_wand_y, aether_wand_p;

	// ディバインワンド
	public static Item divine_wand_r, divine_wand_g, divine_wand_b, divine_wand_y, divine_wand_p;

	// ピュアクリスタルワンド
	public static Item purecrystal_wand_r, purecrystal_wand_g, purecrystal_wand_b, purecrystal_wand_y, purecrystal_wand_p;

	// デウスクリスタルワンド
	public static Item deuscrystal_wand_r, deuscrystal_wand_g, deuscrystal_wand_b, deuscrystal_wand_y, deuscrystal_wand_p;

	// コズミッククリスタルワンド
	public static Item sacred_meteor_wand, snowdust_wand, flugel_wind_wand, elksi_diafusola_wand, aquarius_sorcerer_wand, phosphorus_wand, judgmentlightninge_wand, sweetpoison_wand, finalexplosion_wand;

	// 装備品
	public static Item warrior_bracelet, witch_scroll, scorching_jewel, mermaid_veil, blood_sucking_ring, emelald_pias, fortune_ring;
	public static Item varrier_pendant, magicians_grobe, magician_quillpen, gravity_pendant, poison_fang, pendulum_necklace, angel_flugel;
	public static Item unyielding_fire, frosted_chain, holly_charm, wind_relief;
	public static Item summon_book, electric_earring, range_glove, prompt_feather, mysterious_fork, extension_ring;

	// ドア
	public static Item black_moderndoor, brown_2paneldoor, brown_5paneldoor, brown_elegantdoor, brown_arch_door, brown_arch_plantdoor;
	public static Item woodgold_3, whitewoodgold_3, simple_door_1, simple_door_2;

	// 料理素材
	public static Item sugarbell, vannila_beans, vannila_essence, olive_oil, whipping_cream;
	public static Item butter, salt, watercup, breadcrumbs, flourpowder, ine, kinako, gelatin;

	// 魔法
	public static Item magic_dig, magic_fire, magic_frost, magic_light, magic_blast;
	public static Item magic_heal, magic_shadow, magic_cyclon, magic_gravity, magic_thunder;
	public static Item magic_regene, magic_barrier, magic_poison, magic_starburst;
	public static Item magic_tornado, magic_ballast, magic_rangebreaker, magic_effectremover;
	public static Item magic_frostspear, magic_flamenova, magic_shadowwolf, magic_rangepoison;
	public static Item magic_elecarmor, magic_refresh, magic_healingwish, magic_lightningbolt;
	public static Item magic_storm, magic_gravitywave, magic_meteor_fall, magic_mining_magia;
	public static Item magic_sacredbuster, magic_growth_aura, magic_growth_effect;
	public static Item magic_burst, magic_vector_boost, magic_vector_halten, magic_magia_destroy;
	public static Item magic_slowtime, magic_deusora, magic_futurevision, magic_frostrain;
	public static Item magic_tempest_gin, magic_gravity_break, magic_shadowgolem;
	public static Item magic_bubleprison, magic_scumefang, magic_regene_shield;
	public static Item magic_deadly_poison, magic_earth_destruction, magic_magia_protection;
	public static Item magic_foamy_hell, magic_shining_flare, magic_growth_verre;
	public static Item magic_bleb_burst, magic_avoid, magic_avoid_2, magic_avoid_3;
	public static Item magic_absolute_zero, magic_blaze_end, magic_healing_hightlow;
	public static Item magic_absorphealth, magic_mind_control, magic_creative;
	public static Item magic_blackhole, magic_invisible, magic_aether_shield, magic_aether_shield2;
	public static Item magic_supernova, magic_mining_enchant, magic_rockblast, magic_hugerock_fall;
	public static Item magic_expand_barrier, magic_shadowhorse, magic_refresh_gift, magic_causality_prediction;
	public static Item magic_aether_force, magic_divine_force, magic_pure_force, magic_deus_force, magic_cosmic_force;
	public static Item magic_growth_collect, magic_blackcat;
	public static Item magic_vector_field, magic_reflesh_field, magic_avoid_tornado;
	public static Item magic_endpoison, magic_thunderforce, magic_earthquake;

    // 簡易魔法
	public static Item card_normal, card_heal;

	// 燃料アイテム
	public static Item plant_chips;

	// 食べ物(作物)
	public static Item lemon, orange, peach, chestnut, coconut, whitenet, coffee_seed;

	// 種
	public static Item sugarbell_seed, whitenet_seed, corn_seed, eggplant_seed;
	public static Item j_radish_seed, lettuce_seed, cabbage_seed, azuki_seed, rice_seed;
	public static Item spinach_seed, pineapple_seed, greenpepper_seed;
	public static Item sweetpotato, eggplant, tomato, onion, soybean, blueberry, raspberry, strawberry;

	// 作物
	public static Item j_radish, lettuce, cabbage, rice, edamame, corn;

	// 食べ物
	public static Item yaki_imo, cooked_corn, butadon, gyuudon, yakinasu, blueberry_muffin, chocolate_muffin;
	public static Item salmon_meuniere, boiled_edamame, potatobutter, cake_dough, lemon_cookie, cheese;
	public static Item tamagoyaki, buridaikon, itigo_daihuku, ogura_toast, caramel_popcorn, nikujaga;
	public static Item roll_cabbage, beefstew, stew, sukiyaki, sauteed_mushrooms, medamayaki, kurikinton, pumpkin_nituke;
	public static Item strawberry_jelly, orange_jelly;
	public static Item kakigori_strawberry, kakigori_milk, kakigori_lemon;
	public static Item cocoamass, cocoabutter, cocoapowder, chocolate, white_chocolate, cocoa;
	public static Item icebox_cookie, mont_blanc, strawberry_tart, youkan, scone, toast;
	public static Item custard, cream_puff, cake_roll, eclair, marshmallow, salt_popcorn;
	public static Item cheegyu, jam_toast, butter_toast, egg_sandwitch, yogurt, pumpkin_soup, pudding;
	public static Item short_cake, cheese_cake, chocolate_cake;
	public static Item croquette, choco_pie, fruit_crepe, sweet_potato, french_toast;
	public static Item banana, yaki_banana, banana_smoothy, mixed_juice, soy_soup, pork_soup;
	public static Item saba_miso, fluit_mix, pizza, gratin, estor_apple, applepie, yaki_apple;
	public static Item choko_cornet, cream_filled_roll, cream_brulee, apple_muffin;
	public static Item donut_choco, donut_plane, donut_strawberrychoco, german_tree_cake, raspberrypie, waffle;
	public static Item salad_mixcorn, salad_mixoil, salad_coleslaw, salad_caprese, salad_potate;
	public static Item coffee, cafe_latte, dry_seaweed, riceball_salmon, riceball_salt, fried_rice, salmondon;
	public static Item talttatan, applecandy, apple_jelly, peach_jelly, kurigohan, spinach;
	public static Item salad_ohitasi, bonito_flakes, steak_hamburg, sushi_egg, sushi_salmon, peach_tart;
	public static Item hamburger, gateau_chocolat, cookie_jam, peach_compote;
	public static Item sandwich, sandwich_fruit, vienna_coffee, spinach_egg;
	public static Item fried_potato, softcream_vannila, softcream_strawberry, softcream_chocolate, cake_chiffon;
	public static Item hotcake, kinakomochi, mochi, zunda, ohagi;
	public static Item riceball_grilled, panna_cotta, salmon_carpaccio;
	public static Item croissant, butter_role, canele, melon_bread, pretzel, salad_fruit;
	public static Item pineapple, greenpepper, madeleine, sweet_and_sour_pork;
	public static Item peppers_stuffed_with_meat, peppers_with_soy_sauce_and_bonito;
	public static Item salad_caesar, pine_smoothy, macaroon;
	public static Item seaplant_soup, coconuts_cookie, icing_cookies, orange_tart, cup_cake;
	public static Item fish_and_chips, milkshake, witch_cake, devil_cake;

	// 飲み物
	public static Item corn_soup, berryorange_juice, strawberrymilk, coconut_juice, milk_pack, fruit_wine;

	//　調味料
	public static Item vannila_pods, olive, miso, salad_dressing, mayonnaise, vinegar, seaweed, soy_sauce;

	// 防具
	public static Item magicians_robe, wizard_robe, feary_robe, kitt_robe, witchmadame_robe, windine_robe, ifrite_robe, curious_robe;
	public static Item magicians_pouch, angel_harness, aether_choker, pure_choker, sandryon_robe;
	public static Item deus_choker, master_magia_pouch, aether_boot;

	public static Item shop_uniform, farm_apron;

	public static Item smcushion_o, smcushion_s, smcushion_y, smcushion_b;

	// ダンジョン用
	public static Item magickey;

    public static List<Item> itemList = new ArrayList();
    public static List<Item> foodList = new ArrayList();
    public static List<Item> magicList = new ArrayList();
    public static List<Item> furniList = new ArrayList();
    public static List<Item> noTabList = new ArrayList();

	// アイテムにデータを入れる
	public static void init() {

		// 魔法本
        guide_book = new SMGuidBook("guide_book");
        magic_book = new SMBook("magic_book", 0);
        magic_book_cosmic = new SMBookCosmic("magic_book_cosmic", 0);
        magic_book_scarlet = new SMBookCosmic("magic_book_scarlet", 1);

        magicianbeginner_book = new MagicianBeginnerBook("magicianbeginner_book", 0);
        smhouse = new MagicianBeginnerBook("smhouse", 1);

        // 天気花びら、種
		sannyflower_petal = new MFTime("sannyflower_petal", 0);
		sannyflower_seed = new SMSeed("sannyflower_seed", BlockInit.sannyflower_plant, 1);
		moonblossom_petal = new MFTime("moonblossom_petal", 14000);
		moonblossom_seed = new SMSeed("moonblossom_seed", BlockInit.moonblossom_plant, 1);
		dm_seed = new SMSeed("drizzly_mysotis_seed", BlockInit.dm_plant, 1);
		dm_flower = new MFWeather("drizzly_mysotis_flower", 0, SMElement.WATER, 12000);

        // 鉱石、クリスタル
		alternative_ingot = new SMItem("alternative_ingot");
		cosmos_light_ingot = new SMItem("cosmos_light_ingot");
		aether_crystal_shard = new MFItem("aether_crystal_shard", 50);
		cosmic_crystal_shard = new MFItem("cosmic_crystal_shard", 1000);
		aether_crystal = new MFItem("aether_crystal", 600);
		divine_crystal = new MFItem("divine_crystal", 8000);
		pure_crystal = new MFItem("pure_crystal", 100000);
		deus_crystal = new MFItem("deus_crystal", 450000);
		cosmic_crystal = new MFItem("cosmic_crystal", 1000000);

		// エーテルツール
		//斧　　：アイテム名　=new アイテムアックス("アイテム名"、採掘レベル、耐久値、効率、攻撃力、エンチャントレベル)、攻撃力、攻撃速度
		//斧以外：アイテム名　=new アイテムピッケル("アイテム名"、採掘レベル、耐久値、効率、攻撃力、エンチャントレベル)
        alt_axe = new SMAxe("alt_axe", EnumHelper.addToolMaterial("alt_axe", 3, 512, 8, 1, 8), 9F, -3.1F);
        alt_pick = new SMPick("alt_pick", EnumHelper.addToolMaterial("alt_pick", 3, 512, 8, 3F, 8));
        alt_shovel = new SMShovel("alt_shovel", EnumHelper.addToolMaterial("alt_shovel", 3, 512, 8, 3F, 8));
        alt_hoe = new SMHoe("alt_hoe", EnumHelper.addToolMaterial("alt_hoe", 3, 800, 8, 0, 8));
        alt_sickle = new SMSickle("alt_sickle", EnumHelper.addToolMaterial("alt_sickle", 3, 800, 8, 0, 8));
        alt_shears = new SMShears("alt_shears");
        alt_bucket = new SMBucket("alt_bucket");
        alt_bucket_water = new SMBucket("alt_bucket_water");
        alt_bucket_lava = new SMBucket("alt_bucket_lava");

		//アイテム名　=new アイテムソード("アイテム名"、採掘レベル、耐久値、効率、攻撃力、エンチャントレベル),攻撃速度(加算)、データ値
        alt_sword = new SMSword("alt_sword", 3, 512, 4F, 3F, 8, -0.6D, 0);
        knife_of_thief = new SMSword("knife_of_thief", 1, 256, 8.5F, 1, 8, 1.4D, 1);
        machete = new SMSword("machete", 1, 512, 2.5F, 1, 0, 0D, 2);

        // 袋
        seedbag = new SMLootBag("seedbag", 0);
        eggbag = new SMLootBag("eggbag", 1);
        accebag = new SMLootBag("accebag", 2);
        flowerpack = new SMLootBag("flowerpack", 3);
        seed_harvest_bag = new SMHarvestBag("seed_harvest_bag", 0);
        sapling_harvest_bag = new SMHarvestBag("sapling_harvest_bag", 1);

        aether_flashlight = new AetherFlashLight("aether_flashlight");

        sm_phone = new SMPhone("sm_phone");
        sm_phone_r = new SMPhone("sm_phone_r");
        sm_phone_p = new SMPhone("sm_phone_p");
        sm_phone_o = new SMPhone("sm_phone_o");
        sm_phone_l = new SMPhone("sm_phone_l");
        sm_phone_i = new SMPhone("sm_phone_i");
        sm_phone_b = new SMPhone("sm_phone_b");

        // 花びら、種
		magicmeal = new MagicMeal("magicmeal");
		sugarbell = new SMElementItem("sugarbell", SMElement.EARTH);
		sugarbell_seed = new SMSeed("sugarbell_seed", BlockInit.sugarbell_plant, 1);
		clero_petal = new MFTeleport("clero_petal", true);
		clerodendrum_seed = new SMSeed("clerodendrum_seed", BlockInit.clerodendrum, 1);
		fire_powder = new SMFuel("fire_powder", 800, magicList);
		fire_nasturtium_petal = new MFWeather("fire_nasturtium_petal", 1600, SMElement.FLAME, 0);
		fire_nasturtium_seed = new SMSeed("fire_nasturtium_seed",BlockInit.fire_nasturtium_plant, 1);
		sticky_stuff_petal = new SMItem("sticky_stuff_petal", magicList);
		sticky_stuff_seed = new SMSeed("sticky_stuff_seed", BlockInit.sticky_stuff_plant, 1);
		glowflower_seed = new SMSeed("glowflower_seed", BlockInit.glowflower_plant, 1);
		cotton = new SMItem("cotton", magicList);
		cotton_seed = new SMSeed("cotton_seed", BlockInit.cotton_plant, 1);
		quartz_seed = new SMSeed("quartz_seed", BlockInit.quartz_plant, 1);

        // MFボトル
		mf_sbottle = new MFItem("mf_sbottle", 1000);
		b_mf_bottle = new SMItem("b_mf_bottle", magicList);
		mf_bottle = new MFItem("mf_bottle", 10000);
		b_mf_magiabottle = new SMItem("b_mf_magiabottle", magicList);
		mf_magiabottle = new MFItem("mf_magiabottle", 100000);

		// 魔法素材
		ender_shard = new SMDropItem("ender_shard");
		stray_soul = new SMElementItem("stray_soul", SMElement.DARK);
		electronic_orb = new SMElementItem("electronic_orb", SMElement.LIGHTNING);
		poison_bottle = new SMElementItem("poison_bottle", SMElement.TOXIC);
		unmeltable_ice = new SMElementItem("unmeltable_ice", SMElement.FROST);
		grav_powder = new SMElementItem("grav_powder", SMElement.GRAVITY);
		tiny_feather = new SMElementItem("tiny_feather", SMElement.CYCLON);
		prizmium = new SMElementItem("prizmium", SMElement.SHINE);
		witch_tears = new SMDropItem("witch_tears");

		// 魔法素材（魔法）
		blank_page = new SMItem("blank_page", magicList);
		blank_magic = new SMItem("blank_magic", magicList);
		mysterious_page = new SMDropItem("mysterious_page");
		mystical_page = new SMItem("mystical_page", magicList);
		cotton_cloth = new SMItem("cotton_cloth", magicList);

		// 杖
		mf_stuff = new MFStuff("magiaflux_stuff");
		magician_wand = new MagicianWand("magician_wand");
		startlight_wand = new StarLightWand("startlight_wand");
		aether_wand = new SMWand("aether_wand", 1, 3000, 3);
		divine_wand = new SMWand("divine_wand", 2, 15000, 6);
		purecrystal_wand = new SMWand("purecrystal_wand", 3, 40000, 9);
		deuscrystal_wand = new SMWand("deuscrystal_wand", 4, 80000, 12);
		cosmiccrystal_wand = new SMWand("cosmiccrystal_wand", 5, 160000, 16);
		creative_wand = new SMWand("creative_wand", 7, 1, 25);

		aether_wand_r = new SMWand("aether_wand_r", 1, 5000, 3);
		aether_wand_g = new SMWand("aether_wand_g", 1, 5000, 3);
		aether_wand_b = new SMWand("aether_wand_b", 1, 5000, 3);
		aether_wand_y = new SMWand("aether_wand_y", 1, 5000, 3);
		aether_wand_p = new SMWand("aether_wand_p", 1, 5000, 3);

		divine_wand_r = new SMWand("divine_wand_r", 2, 20000, 6);
		divine_wand_g = new SMWand("divine_wand_g", 2, 20000, 6);
		divine_wand_b = new SMWand("divine_wand_b", 2, 20000, 6);
		divine_wand_y = new SMWand("divine_wand_y", 2, 20000, 6);
		divine_wand_p = new SMWand("divine_wand_p", 2, 20000, 6);

		purecrystal_wand_r = new SMWand("purecrystal_wand_r", 3, 50000, 9);
		purecrystal_wand_g = new SMWand("purecrystal_wand_g", 3, 50000, 9);
		purecrystal_wand_b = new SMWand("purecrystal_wand_b", 3, 50000, 9);
		purecrystal_wand_y = new SMWand("purecrystal_wand_y", 3, 50000, 9);
		purecrystal_wand_p = new SMWand("purecrystal_wand_p", 3, 50000, 9);

		deuscrystal_wand_r = new SMWand("deuscrystal_wand_r", 4, 100000, 12);
		deuscrystal_wand_g = new SMWand("deuscrystal_wand_g", 4, 100000, 12);
		deuscrystal_wand_b = new SMWand("deuscrystal_wand_b", 4, 100000, 12);
		deuscrystal_wand_y = new SMWand("deuscrystal_wand_y", 4, 100000, 12);
		deuscrystal_wand_p = new SMWand("deuscrystal_wand_p", 4, 100000, 12);

		sacred_meteor_wand = new CosmicWand("sacred_meteor_wand", 5, 200000, 20, SMElement.SHINE);
		snowdust_wand = new CosmicWand("snowdust_wand", 5, 200000, 20, SMElement.FROST);
		flugel_wind_wand = new CosmicWand("flugel_wind_wand", 5, 200000, 20, SMElement.CYCLON);
		elksi_diafusola_wand = new CosmicWand("elksi_diafusola_wand", 5, 200000, 20, SMElement.GRAVITY);
		phosphorus_wand = new CosmicWand("phosphorus_wand", 5, 200000, 20, SMElement.FLAME);
		aquarius_sorcerer_wand = new CosmicWand("aquarius_sorcerer_wand", 5, 200000, 20, SMElement.WATER);
		judgmentlightninge_wand = new CosmicWand("judgmentlightninge_wand", 5, 200000, 20, SMElement.LIGHTNING);
		sweetpoison_wand = new CosmicWand("sweetpoison_wand", 5, 200000, 20, SMElement.TOXIC);
		finalexplosion_wand = new CosmicWand("finalexplosion_wand", 5, 200000, 20, SMElement.BLAST);

		// 防具
		aether_choker = new AetherChoker("aether_choker", ArmorInit.aether_choker, 1, EntityEquipmentSlot.HEAD, 0, 5000);
		pure_choker = new AetherChoker("pure_choker", ArmorInit.pure_choker, 1, EntityEquipmentSlot.HEAD, 0, 40000);
		deus_choker = new AetherChoker("deus_choker", ArmorInit.deus_choker, 1, EntityEquipmentSlot.HEAD, 0, 100000);
		magicians_robe = new MagiciansRobe("magicians_robe", ArmorInit.magicians_robe, 1, EntityEquipmentSlot.CHEST, 0, 10000);
		wizard_robe = new MagiciansRobe("wizard_robe", ArmorInit.wizard_robe, 1, EntityEquipmentSlot.CHEST, 1, 40000);
		feary_robe = new MagiciansRobe("feary_robe", ArmorInit.feary_robe, 1, EntityEquipmentSlot.CHEST, 2, 40000);
		kitt_robe = new MagiciansRobe("kitt_robe", ArmorInit.kitt_robe, 1, EntityEquipmentSlot.CHEST, 8, 40000);
		witchmadame_robe = new MagiciansRobe("witchmadame_robe", ArmorInit.witchmadame_robe, 1, EntityEquipmentSlot.CHEST, 6, 40000);
		windine_robe = new MagiciansRobe("windine_robe", ArmorInit.windine_robe, 1, EntityEquipmentSlot.CHEST, 3, 40000);
		ifrite_robe = new MagiciansRobe("ifrite_robe", ArmorInit.ifrite_robe, 1, EntityEquipmentSlot.CHEST, 4, 40000);
		curious_robe = new MagiciansRobe("curious_robe", ArmorInit.curious_robe, 1, EntityEquipmentSlot.CHEST, 7, 40000);
		sandryon_robe = new MagiciansRobe("sandryon_robe", ArmorInit.sandryon_robe, 1, EntityEquipmentSlot.CHEST, 5, 40000);
		magicians_pouch = new MagiciansPouch("magicians_pouch", ArmorInit.magicians_pouch, 1, EntityEquipmentSlot.LEGS, 0);
		master_magia_pouch = new MagiciansPouch("master_magia_pouch", ArmorInit.master_magia_pouch, 1, EntityEquipmentSlot.LEGS, 1);
		angel_harness = new AngelHarness("angel_harness", ArmorInit.angel_harness, 1, EntityEquipmentSlot.FEET, 0, 20000);
		aether_boot = new AngelHarness("aether_boot", ArmorInit.angel_harness, 1, EntityEquipmentSlot.FEET, 1, 20000);

		shop_uniform = new FarmClothes("shop_uniform", ArmorInit.shop_uniform, 1, EntityEquipmentSlot.CHEST, 0);
		farm_apron = new FarmClothes("farm_apron", ArmorInit.farm_apron, 1, EntityEquipmentSlot.CHEST, 0);

		// 装備品
		range_glove = new SMAcce("range_glove", SMAcceType.UPDATE, false, 21, 1, 2);
		prompt_feather = new SMAcce("prompt_feather", SMAcceType.UPDATE, false, 22, 1, 2);
		mysterious_fork = new SMAcce("mysterious_fork", SMAcceType.UPDATE, false, 23, 1, 2);
		blood_sucking_ring = new SMAcce("blood_sucking_ring", SMAcceType.TERMS, true, 4, 2);
		emelald_pias = new SMAcce("emelald_pias", SMAcceType.TERMS, true, 5, 2);
		warrior_bracelet = new SMAcce("warrior_bracelet", SMAcceType.TERMS, false, 0, 2, 1);
		witch_scroll = new SMAcce("witch_scroll", SMAcceType.TERMS, false, 1, 2, 1);
		scorching_jewel = new SMAcce("scorching_jewel", SMAcceType.UPDATE, true, 2, 2, 4);
		mermaid_veil = new SMAcce("mermaid_veil", SMAcceType.UPDATE, true, 3, 2, 4);
		fortune_ring = new SMAcce("fortune_ring", SMAcceType.UPDATE, false, 6, 2);
		electric_earring = new SMAcce("electric_earring", SMAcceType.TERMS, false, 20, 2);
		summon_book = new SMAcce("summon_book", SMAcceType.TERMS, false, 19, 2);
		veil_darkness = new SMAcce("veil_darkness", SMAcceType.UPDATE, false, 7, 2, 3);
		varrier_pendant = new SMAcce("varrier_pendant", SMAcceType.UPDATE, false, 8, 2, 0);
		magicians_grobe = new SMAcce("magicians_grobe", SMAcceType.TERMS, true, 9, 2);
		magician_quillpen = new SMAcce("magician_quillpen", SMAcceType.TERMS, false, 10, 2, 2);
		gravity_pendant = new SMAcce("gravity_pendant", SMAcceType.TERMS, true, 11, 2);
		poison_fang = new SMAcce("poison_fang", SMAcceType.TERMS, false, 12, 2);
		pendulum_necklace = new SMAcce("pendulum_necklace", SMAcceType.TERMS, true, 13, 2);
		unyielding_fire = new SMAcce("unyielding_fire", SMAcceType.TERMS, false, 14, 3, 2);
		frosted_chain = new SMAcce("frosted_chain", SMAcceType.TERMS, false, 15, 3, 2);
		holly_charm = new SMAcce("holly_charm", SMAcceType.TERMS, false, 16, 3, 2);
		wind_relief = new SMAcce("wind_relief", SMAcceType.TERMS, false, 17, 3, 2);
		angel_flugel = new SMAcce("angel_flugel", SMAcceType.TERMS, false, 18, 3, 1);
		extension_ring = new SMAcce("extension_ring", SMAcceType.TERMS, false, 24, 3, 1);

		// ドア
		black_moderndoor = new SMIDoor("black_moderndoor_i", BlockInit.black_moderndoor);
		brown_2paneldoor = new SMIDoor("brown_2paneldoor_i", BlockInit.brown_2paneldoor);
		brown_5paneldoor = new SMIDoor("brown_5paneldoor_i", BlockInit.brown_5paneldoor);
		brown_elegantdoor = new SMIDoor("brown_elegantdoor_i", BlockInit.brown_elegantdoor);
		brown_arch_door = new SMIDoor("brown_arch_door_i", BlockInit.brown_arch_door);
		brown_arch_plantdoor = new SMIDoor("brown_arch_plantdoor_i", BlockInit.brown_arch_plantdoor);
		woodgold_3 = new SMIDoor("woodgold_3_i", BlockInit.woodgold_3);
		whitewoodgold_3 = new SMIDoor("whitewoodgold_3_i", BlockInit.whitewoodgold_3);
		simple_door_1 = new SMIDoor("simple_door_1_i", BlockInit.simple_door_1);
		simple_door_2 = new SMIDoor("simple_door_2_i", BlockInit.simple_door_2);

		//アイテム燃料…名前、燃焼時間
		plant_chips = new SMFuel("plant_chips", 400);

		// 魔法 (アイテム名、必要経験値量、データ値、属性、tier、クールタイム、必要MF量)
        magic_fire = new ShotMagic("magic_fire", 0, SMElement.FLAME, 1, 40, 30);
        magic_flamenova = new ShotMagic("magic_flamenova", 10, SMElement.FLAME, 2, 120, 150, "magic_fire");
        magic_meteor_fall = new AirMagic("magic_meteor_fall", 9, SMElement.FLAME, 3, 160, 300, "magic_fire");
        magic_blaze_end = new FieldMagic("magic_blaze_end", 2, SMElement.FLAME, 4, 700, 1800, "magic_fire");

        magic_frost = new ShotMagic("magic_frost", 1, SMElement.FROST, 1, 60, 40);
        magic_frostspear = new ShotMagic("magic_frostspear", 9, SMElement.FROST, 2, 100, 120, "magic_frost");
        magic_frostrain = new AirMagic("magic_frostrain", 10, SMElement.FROST, 3, 300, 400, "magic_frost");
        magic_absolute_zero = new FieldMagic("magic_absolute_zero", 3, SMElement.FROST, 4, 700, 1800, "magic_frost");

        magic_effectremover = new AirMagic("magic_effectremover", 5, SMElement.WATER, 1, 600, 80);
        magic_refresh  = new AirMagic("magic_refresh", 7, SMElement.WATER, 2, 1600, 200, "magic_effectremover");
        magic_refresh_gift  = new AirMagic("magic_refresh_gift", 25, SMElement.WATER, 3, 1200, 300, "magic_effectremover");
        magic_reflesh_field  = new FieldMagic("magic_reflesh_field", 1, SMElement.WATER, 4, 2400, 1200, "magic_effectremover", SMElement.SHINE);

        magic_regene = new AirMagic("magic_regene", 2, SMElement.WATER, 1, 240, 100, "magic_heal");
        magic_heal = new AirMagic("magic_heal", 0, SMElement.WATER, 2, 520, 200);
        magic_healingwish  = new AirMagic("magic_healingwish", 8, SMElement.WATER, 3, 2000, 400, "magic_heal");
        magic_healing_hightlow  = new AirMagic("magic_healing_hightlow", 20, SMElement.WATER, 4, 4800, 1600, "magic_heal");

        magic_bubleprison = new ShotMagic("magic_bubleprison", 20, SMElement.WATER, 1, 180, 80);
        magic_scumefang = new ShotMagic("magic_scumefang", 21, SMElement.WATER, 2, 220, 150, "magic_bubleprison");
        magic_foamy_hell = new ShotMagic("magic_foamy_hell", 24, SMElement.WATER, 3, 260, 300, "magic_bubleprison");
        magic_bleb_burst = new ShotMagic("magic_bleb_burst", 26, SMElement.WATER, 4, 100, 1200, "magic_bubleprison", SMElement.BLAST);

        magic_elecarmor = new AirMagic("magic_elecarmor", 6, SMElement.LIGHTNING, 1, 1400, 100, "magic_thunder");
        magic_lightningbolt = new ChargeMagic("magic_lightningbolt", 3, SMElement.LIGHTNING, 2, 300, 200, "magic_thunder");
        magic_thunder = new ChargeMagic("magic_thunder", 2, SMElement.LIGHTNING, 3, 400, 400);
        magic_thunderforce = new ChargeMagic("magic_thunderforce", 11, SMElement.LIGHTNING, 4, 600, 1800, "magic_thunder");

        magic_shadowhorse = new SummonMagic("magic_shadowhorse", 2, SMElement.DARK, 1, 8000, 100, "magic_summon");
        magic_shadowwolf = new SummonMagic("magic_shadowwolf", 0, SMElement.DARK, 2, 6000, 400, "magic_summon");
        magic_shadowgolem = new SummonMagic("magic_shadowgolem", 1, SMElement.DARK, 3, 8000, 600, "magic_summon");
        magic_blackcat = new SummonMagic("magic_blackcat", 3, SMElement.DARK, 4, 7000, 1200, "magic_summon");

        magic_absorphealth = new TouchMagic("magic_absorphealth", 0, SMElement.DARK, 1, 200, 150, "magic_shadow");
        magic_mind_control = new TouchMagic("magic_mind_control", 1, SMElement.DARK, 2, 700, 400, "magic_shadow");
        magic_shadow = new AirMagic("magic_shadow", 1, SMElement.DARK, 3, 2400, 600);

        magic_rangepoison = new ShotMagic("magic_rangepoison", 11, SMElement.TOXIC, 1, 400, 50, "magic_poison");
        magic_poison = new AirMagic("magic_poison", 4, SMElement.TOXIC, 2, 1300, 200);
        magic_deadly_poison = new ShotMagic("magic_deadly_poison", 22, SMElement.TOXIC, 3, 200, 400, "magic_poison");
        magic_endpoison = new ShotMagic("magic_endpoison", 33, SMElement.TOXIC,4, 600, 1200, "magic_poison");

        magic_ballast = new ShotMagic("magic_ballast", 6, SMElement.GRAVITY, 1, 60, 20, "magic_gravity");
        magic_gravitywave = new ShotMagic("magic_gravitywave", 13, SMElement.GRAVITY, 2, 120, 240, "magic_gravity");
        magic_gravity_break = new ShotMagic("magic_gravity_break", 19, SMElement.GRAVITY, 3, 200, 600, "magic_gravity");
        magic_blackhole = new ShotMagic("magic_blackhole", 29, SMElement.GRAVITY, 4, 500, 1800, "magic_gravity");

        magic_vector_boost = new AirMagic("magic_vector_boost", 11, SMElement.GRAVITY, 1, 1200, 200);
        magic_vector_halten = new AirMagic("magic_gravity", 12, SMElement.GRAVITY, 2, 1400, 600, "magic_vector_boost");
        magic_vector_field = new FieldMagic("magic_vector_field", 0, SMElement.GRAVITY, 3, 1400, 800, "magic_vector_boost");

        magic_barrier = new AirMagic("magic_barrier", 3, SMElement.SHINE, 1, 1200, 200);
        magic_regene_shield = new AirMagic("magic_regene_shield", 15, SMElement.WATER, 2, 1800, 400, "magic_barrier");
        magic_magia_protection = new AirMagic("magic_magia_protection", 16, SMElement.WATER, 3, 3600, 800, "magic_barrier");
        magic_expand_barrier = new AirMagic("magic_expand_barrier", 24, SMElement.SHINE, 4, 3000, 1800, "magic_barrier", SMElement.WATER);

        magic_invisible = new AirMagic("magic_invisible", 22, SMElement.SHINE, 1, 1800, 300, "magic_aether_shield");
        magic_aether_shield = new ChargeMagic("magic_aether_shield", 8, SMElement.SHINE, 2, 2200, 400);
        magic_aether_shield2 = new ChargeMagic("magic_aether_shield2", 9, SMElement.SHINE, 3, 2600, 800, "magic_aether_shield");

        magic_light = new ShotMagic("magic_light", 2, SMElement.SHINE, 1, 20, 30);
        magic_starburst = new ShotMagic("magic_starburst", 7, SMElement.SHINE, 2, 40, 80, "magic_light");
        magic_sacredbuster = new ShotMagic("magic_sacredbuster", 16, SMElement.SHINE, 3, 80, 200, "magic_light");
        magic_shining_flare = new ShotMagic("magic_shining_flare", 25, SMElement.SHINE, 4, 300, 1200, "magic_light", SMElement.FLAME);

        magic_tornado = new ShotMagic("magic_tornado", 5, SMElement.CYCLON, 1, 50, 30, "magic_cyclon");
        magic_storm = new ShotMagic("magic_storm", 12, SMElement.CYCLON, 2, 100, 160, "magic_cyclon");
        magic_cyclon = new ShotMagic("magic_cyclon", 18, SMElement.CYCLON, 3, 200, 300);

        magic_avoid = new AirMagic("magic_avoid", 17, SMElement.CYCLON, 1, 1200, 300);
        magic_avoid_2 = new AirMagic("magic_avoid_2", 18, SMElement.CYCLON, 2, 1800, 400, "magic_avoid");
        magic_avoid_3 = new AirMagic("magic_avoid_3", 19, SMElement.CYCLON, 3, 2400, 800, "magic_avoid");
        magic_avoid_tornado = new FieldMagic("magic_avoid_tornado", 4, SMElement.CYCLON, 4, 1600, 1800, "magic_avoid");

        magic_dig = new ShotMagic("magic_dig", 3, SMElement.EARTH, 1, 10, 10);
        magic_rangebreaker = new ShotMagic("magic_rangebreaker", 8, SMElement.EARTH, 2, 20, 30, "magic_dig");
        magic_mining_magia = new ShotMagic("magic_mining_magia", 14, SMElement.EARTH, 3, 40, 50, "magic_dig");
        magic_earth_destruction = new ShotMagic("magic_earth_destruction", 23, SMElement.EARTH, 4, 60, 70, "magic_dig");

        magic_mining_enchant = new AirMagic("magic_mining_enchant", 23, SMElement.EARTH, 1, 1200, 200, "magic_rockblast");
        magic_rockblast = new ShotMagic("magic_rockblast", 31, SMElement.EARTH, 2, 120, 200);
        magic_hugerock_fall = new ShotMagic("magic_hugerock_fall", 32, SMElement.EARTH, 3, 160, 600, "magic_rockblast");
        magic_earthquake = new ChargeMagic("magic_earthquake", 12, SMElement.EARTH, 4, 700, 1800, "magic_rockblast");

        magic_growth_effect = new ChargeMagic("magic_growth_effect", 6, SMElement.EARTH, 1, 200, 200, "magic_growth_aura");
        magic_growth_aura = new ChargeMagic("magic_growth_aura", 5, SMElement.EARTH, 2, 300, 400);
        magic_growth_verre = new ChargeMagic("magic_growth_verre", 7, SMElement.EARTH, 3, 400, 800, "magic_growth_aura");
        magic_growth_collect = new ChargeMagic("magic_growth_collect", 10, SMElement.EARTH, 4, 500, 1800, "magic_growth_aura");

        magic_burst = new ShotMagic("magic_burst", 15, SMElement.BLAST, 1, 160, 100, "magic_tamagotti");
        magic_blast = new ShotMagic("magic_tamagotti", 4, SMElement.BLAST, 2, 120, 200);
        magic_magia_destroy = new ShotMagic("magic_magia_destroy", 17, SMElement.BLAST, 3, 160, 300, "magic_tamagotti");
        magic_supernova = new ShotMagic("magic_supernova", 30, SMElement.BLAST, 4, 280, 1800, "magic_tamagotti");

        magic_slowtime = new ChargeMagic("magic_slowtime", 4, SMElement.TIME, 1, 2400, 100);
        magic_deusora = new AirMagic("magic_deusora", 13, SMElement.TIME, 2, 6000, 200, "magic_slowtime");
        magic_futurevision = new AirMagic("magic_futurevision", 14, SMElement.TIME, 3, 2400, 300, "magic_slowtime");
        magic_causality_prediction = new AirMagic("magic_causality_prediction", 26, SMElement.TIME, 4, 3000, 1200, "magic_slowtime");

        magic_aether_force = new RunkUpMagic("magic_aether_force", 0, SMElement.ALL, 1, 10, 0, "aether_wand");
        magic_divine_force = new RunkUpMagic("magic_divine_force", 1, SMElement.ALL, 2, 10, 0, "divine_wand");
        magic_pure_force = new RunkUpMagic("magic_pure_force", 2, SMElement.ALL, 3, 10, 0, "purecrystal_wand");
        magic_deus_force = new RunkUpMagic("magic_deus_force", 3, SMElement.ALL, 4, 10, 0, "deuscrystal_wand");
        magic_cosmic_force = new RunkUpMagic("magic_cosmic_force", 4, SMElement.ALL, 5, 10, 0, "cosmiccrystal_wand");
        magic_creative = new RunkUpMagic("magic_creative", 5, SMElement.ALL, 0, 10, 0, "creative_wand");

        // 簡易魔法
        card_normal = new CardMagic("card_normal", 50, 0);
        card_heal = new CardHeal("card_heal", 32);

        kitt_gadget = new SMItem("kitt_gadget", noTabList);
        kitt_jet = new SMItem("kitt_jet", noTabList);
        aether_rifle = new SMItem("aether_rifle", noTabList);
        aether_hammer = new AetherHammer("aether_hammer", EnumHelper.addToolMaterial("aether_hammer", 4, 1024, 8, 3F, 8));

        magickey = new SMKey("magickey");

        smcushion_o = new SMCushion("smcushion_o", 0);
        smcushion_s = new SMCushion("smcushion_s", 1);
        smcushion_y = new SMCushion("smcushion_y", 2);
        smcushion_b = new SMCushion("smcushion_b", 3);

		// 種 アイテム名　=new アイテムシードフードクラス(アイテム名、植える作物ブロック、満腹度、隠し満腹度(乗算)、狼が食べるか)
        whitenet_seed = new SMSeed("whitenet_seed",BlockInit.whitenet_plant, 2);
        vannila_pods = new SMSeed("vannila_pods",BlockInit.vannila_plant, 0);
        olive = new SMSeed("olive",BlockInit.olive_plant, 1);
        rice_seed = new SMSeed("rice_seed", BlockInit.rice_plant, 0);
        corn_seed = new SMSeed("corn_seed", BlockInit.corn_plant, 0);
        eggplant_seed = new SMSeed("eggplant_seed", BlockInit.egg_plant, 0);
        j_radish_seed = new SMSeed("j_radish_seed", BlockInit.j_radish_plant, 0);
        lettuce_seed = new SMSeed("lettuce_seed", BlockInit.lettuce_plant, 0);
        cabbage_seed = new SMSeed("cabbage_seed", BlockInit.cabbage_plant, 0);
        azuki_seed = new SMSeed("azuki_seed", BlockInit.azuki_plant, 0);
        spinach_seed = new SMSeed("spinach_seed", BlockInit.spinach_plant, 0);
        coffee_seed = new SMSeed("coffee_seed", BlockInit.coffee_plant, 1);
        pineapple_seed = new SMSeed("pineapple_seed", BlockInit.pineapple_plant, 0);
        greenpepper_seed = new SMSeed("greenpepper_seed", BlockInit.greenpepper_plant, 0);

        // 作物種
		blueberry = new SMSeedFood("blueberry", BlockInit.blueberry_plant, 3, 0.4F, true);
		raspberry = new SMSeedFood("raspberry", BlockInit.raspberry_plant, 4, 0.35F, true);
		strawberry = new SMSeedFood("strawberry",BlockInit.strawberry_plant, 4, 0.3F, false);
		soybean = new SMSeedFood("soybean", BlockInit.soybean_plant, 3, 0.4F, false);
		sweetpotato = new  SMSeedFood("sweetpotato", BlockInit.sweetpotato_plant, 3, 0.4F, false);
		tomato = new  SMSeedFood("tomato", BlockInit.tomato_plant, 3, 0.33F, false);
		onion = new  SMSeedFood("onion", BlockInit.onion_plant, 3, 0.33F, false);

		// 料理素材 アイテム名　=new アイテムクラス("アイテム名")
		vannila_beans = new SMItem("vannila_beans", foodList);
		vannila_essence = new SMItem("vannila_essence", foodList);
		olive_oil = new SMItem("olive_oil", foodList);
		whipping_cream = new SMItem("whipping_cream", foodList);
		butter = new SMItem("butter", foodList);
		salt = new SMItem("salt", foodList);
		watercup = new SMDrink(1, 0.1F, "watercup", 5);
		vinegar = new SMItem("vinegar", foodList);
		mayonnaise = new SMItem("mayonnaise", foodList);
		salad_dressing = new SMItem("salad_dressing", foodList);
		soy_sauce = new SMItem("soy_sauce", foodList);
		breadcrumbs = new SMItem("breadcrumbs", foodList);
		flourpowder = new SMItem("flourpowder", foodList);
		ine = new SMItem("ine", foodList);
		kinako = new SMItem("kinako", foodList);
		gelatin = new SMItem("gelatin", foodList);
		cocoamass = new SMItem("cocoamass", foodList);
		cocoabutter = new SMItem("cocoabutter", foodList);
		cocoapowder = new SMItem("cocoapowder", foodList);
		custard = new SMItem("custard", foodList);
		miso = new SMItem("miso", foodList);
		seaweed = new SMItem("seaweed", foodList);
		bonito_flakes = new SMItem("bonito_flakes", foodList);

		// 食べ物 アイテム名　=new (満腹度回復、隠し満腹度(乗算)、"アイテム名", ポーション効果の整数)
		lemon = new SMFood(3, 0.3F, "lemon", 0);
		orange = new SMFood(3, 0.3F, "orange", 0);
		peach = new SMFood(3, 0.3F, "peach", 0);
		estor_apple = new SMFood(3, 0.5F, "estor_apple", 0);
		chestnut = new SMFood(4, 0.8F, "chestnut", 0);
		coconut = new SMFood(3, 1F, "coconut", 0);
		banana = new SMFood(2, 1.5F, "banana", 0);
		pineapple = new SMFood(3, 0.65F, "pineapple", 0);
		whitenet = new SMFood(4, 0.45F, "whitenet", 0);
		dry_seaweed = new SMFood(1, 0.5F, "dry_seaweed", 1);
		cake_dough = new SMFood(4, 0.45F, "cake_dough", 0);
		cheese = new SMFood(4, 0.3F, "cheese", 0);
		edamame = new SMFood(2, 0.3F, "edamame", 1);
		spinach = new SMFood(2, 0F, "spinach", 0);
		greenpepper = new SMFood(2, 0F, "greenpepper", 0);
		corn = new SMFood(3, 0.3F, "corn", 0);
		j_radish = new SMFood(2, 0.5F, "j_radish", 0);
		eggplant = new SMFood(2, 0.5F, "eggplant", 0);
		lettuce = new SMFood(2, 0.5F, "lettuce", 0);
		cabbage = new SMFood(2, 0.5F, "cabbage", 0);
		yaki_imo = new SMFood(5, 0.5F, "yaki_imo", 1);
		sweet_potato = new SMFood (6, 0.75F, "sweet_potato", 0);
		yaki_banana = new SMFood(6, 0.4F, "yaki_banana", 1);
		yaki_apple = new SMFood(4, 0.55F, "yaki_apple", 0);
		applecandy = new SMFood(5, 0.6F, "applecandy", 0);
		itigo_daihuku = new SMFood(5, 0.5F, "itigo_daihuku", 0);
		cooked_corn = new SMFood(7, 0.15F, "cooked_corn", 0);
		potatobutter = new SMFood(7, 0.5F, "potatobutter", 1);
		boiled_edamame = new SMFood(6, 0.2F, "boiled_edamame", 0);
		peppers_stuffed_with_meat = new SMFood(8, 0.6F, "peppers_stuffed_with_meat", 0);

		// パン類
		toast = new SMFood (4, 0.5F, "toast", 0);
		ogura_toast = new SMFood(9, 0.6F, "ogura_toast", 0);
		butter_toast = new SMFood(7, 0.4F, "butter_toast", 0);
		jam_toast = new SMFood(10, 0.4F, "jam_toast", 0);
		french_toast = new SMFood(10, 0.4F, "french_toast", 0);
		choko_cornet = new SMFood(8, 0.4F, "choko_cornet", 0);
		cream_filled_roll = new SMFood(5, 0.75F, "cream_filled_roll", 0);
		croissant = new SMFood(6, 0.625F, "croissant", 0);
		butter_role = new SMFood(6, 0.4F, "butter_role", 0);
		melon_bread = new SMFood(7, 0.45F, "melon_bread", 0);
		pretzel = new SMFood(5, 0.775F, "pretzel", 0);

		sandwich = new SMFood(7, 0.6F, "sandwich", 0);
		egg_sandwitch = new SMFood(6, 0.75F, "egg_sandwitch", 0);
		sandwich_fruit = new SMFood(5, 0.85F, "sandwich_fruit", 0);
		hamburger = new SMFood(16, 0.625F, "hamburger", 5);

		// 米類
		rice = new SMFood(4, 0.3F, "rice", 0);
		riceball_salt = new SMFood(4, 0.5F, "riceball_salt", 1);
		riceball_salmon = new SMFood(5, 0.6F, "riceball_salmon", 1);
		riceball_grilled = new SMFood(8, 0.4F, "riceball_grilled", 1);
		butadon = new SMFood(8, 0.8F, "butadon", 0);
		gyuudon = new SMFood(10, 0.5F, "gyuudon", 0);
		cheegyu = new SMFood(13, 0.45F, "cheegyu", 0);
		kurigohan = new SMFood(8, 0.65F, "kurigohan", 0);
		fried_rice = new SMFood(10, 0.625F, "fried_rice", 0);
		sushi_egg = new SMFood(5, 0.5F, "sushi_egg", 1);
		sushi_salmon = new SMFood(5, 0.5F, "sushi_salmon", 1);

		// ガッツリ系
		salmondon = new SMFood(8, 0.5F, "salmondon", 0);
		nikujaga = new SMFood(16, 0.45F, "nikujaga", 0);
		salad_mixoil = new SMFood(12, 0.5F, "salad_mixoil", 0);
		salad_mixcorn = new SMFood(8, 0.285F, "salad_mixcorn", 1);
		salad_coleslaw = new SMFood(7, 0.285F, "salad_coleslaw", 1);
		salad_caprese = new SMFood(6, 0.333F, "salad_caprese", 1);
		salad_potate = new SMFood(7, 0.4285F, "salad_potate", 1);
		salad_ohitasi = new SMFood(6, 0.5F, "salad_ohitasi", 1);
		salad_caesar = new SMFood(7, 0.6F, "salad_caesar", 1);
		peppers_with_soy_sauce_and_bonito = new SMFood(6, 0.6F, "peppers_with_soy_sauce_and_bonito", 1);
		salad_fruit = new SMFood(6, 0.6F, "salad_fruit", 1);
		salmon_carpaccio = new SMFood(7, 0.6F, "salmon_carpaccio", 1);
		spinach_egg = new SMFood(6, 0.75F, "spinach_egg", 1);
		yakinasu = new SMFood (5, 0.3F, "yakinasu", 0);
		tamagoyaki = new SMFood (6, 0.5F, "tamagoyaki", 0);
		buridaikon = new SMFood (8, 0.4F, "buridaikon", 0);
		salmon_meuniere = new SMFood(4, 0.6F, "salmon_meuniere", 0);
		saba_miso = new SMFood(8, 0.5F, "saba_miso", 0);
		roll_cabbage = new SMFood (11, 0.75F, "roll_cabbage", 0);
		sweet_and_sour_pork = new SMFood (12, 0.75F, "sweet_and_sour_pork", 6);
		pizza = new SMFood(10, 0.7F, "pizza", 7);
		gratin = new SMFood(8, 0.6F, "gratin", 0);
		beefstew = new SMFood (10, 0.75F, "beefstew", 0);
		stew = new SMFood (9, 0.95F, "stew", 0);
		sukiyaki = new SMFood (11, 0.5F, "sukiyaki", 3);
		steak_hamburg = new SMFood (10, 0.45F, "steak_hamburg", 0);
		sauteed_mushrooms = new SMFood (7, 0.6F, "sauteed_mushrooms", 0);
		medamayaki = new SMFood (6, 0.25F, "medamayaki", 0);
		kurikinton = new SMFood (7, 0.55F, "kurikinton", 0);
		pumpkin_nituke = new SMFood (7, 0.6F, "pumpkin_nituke", 0);
		croquette = new SMFood(7, 0.65F, "croquette", 0);
		fried_potato = new SMFood(6, 0.5F, "fried_potato", 1);
		fish_and_chips = new SMFood(10, 0.75F, "fish_and_chips", 0);
		mochi = new SMFood(5, 0.4F, "mochi", 0);
		kinakomochi = new SMFood(6, 0.5F, "kinakomochi", 0);
		zunda = new SMFood(5, 0.8F, "zunda", 3);
		ohagi = new SMFood(6, 0.7F, "ohagi", 0);

		// 冷蔵庫類
		strawberry_jelly = new SMFood (4, 0.75F, "strawberry_jelly", 2);
		orange_jelly = new SMFood (5, 0.6F, "orange_jelly", 2);
		apple_jelly = new SMFood (4, 0.65F, "apple_jelly", 2);
		peach_jelly = new SMFood (7, 0.3F, "peach_jelly", 2);
		kakigori_lemon = new SMFood (8, 0F, "kakigori_lemon", 2);
		kakigori_strawberry = new SMFood (6, 0.2F, "kakigori_strawberry", 2);
		kakigori_milk = new SMFood (7, 0.1F, "kakigori_milk", 2);
		chocolate = new SMFood (4, 1F, "chocolate", 0);
		white_chocolate = new SMFood (4, 1F, "white_chocolate", 0);
		pudding = new SMFood (7, 0.65F, "pudding", 0);
		salt_popcorn = new SMFood(5, 0.25F, "salt_popcorn", 1);
		caramel_popcorn = new SMFood(6, 0.38F, "caramel_popcorn", 1);
		youkan = new SMFood (4, 1.5F, "youkan", 0);
		fluit_mix = new SMFood(12, 0.1F, "fluit_mix", 0);
		peach_compote = new SMFood(5, 0.6F, "peach_compote", 0);
		softcream_vannila = new SMFood(8, 0.3F, "softcream_vannila", 2);
		softcream_strawberry = new SMFood(6, 0.45F, "softcream_strawberry", 2);
		softcream_chocolate = new SMFood(7, 0.75F, "softcream_chocolate", 2);

		// クッキー
		lemon_cookie = new SMFood(6, 0.5F, "lemon_cookie", 1);
		icebox_cookie = new SMFood (6, 0.5F, "icebox_cookie", 1);
		cookie_jam = new SMFood (6, 0.5F, "cookie_jam", 1);
		coconuts_cookie = new SMFood (6, 0.5F, "coconuts_cookie", 1);
		icing_cookies = new SMFood (6, 0.5F, "icing_cookies", 1);

		// 焼き菓子類
		scone = new SMFood (5, 0.6F, "scone", 1);
		waffle = new SMFood (4, 0.8F, "waffle", 1);
		choco_pie = new SMFood (6, 0.5F, "choco_pie", 1);
		blueberry_muffin = new SMFood(7, 0.5F, "blueberry_muffin", 1);
		chocolate_muffin = new SMFood(8, 0.35F, "chocolate_muffin", 1);
		apple_muffin = new SMFood(5, 0.8F, "apple_muffin", 1);
		cup_cake = new SMFood(6, 0.6F, "cup_cake", 1);
		canele = new SMFood(6, 0.725F, "canele", 1);
		madeleine = new SMFood(5, 0.65F, "madeleine", 1);
		macaroon = new SMFood(6, 0.5F, "macaroon", 1);
		donut_plane = new SMFood(5, 0.7F, "donut_plane", 0);
		donut_choco = new SMFood(6, 0.65F, "donut_choco", 0);
		donut_strawberrychoco = new SMFood(7, 0.55F, "donut_strawberrychoco", 0);
		cream_puff = new SMFood (8, 0.65F, "cream_puff", 0);
		short_cake = new SMFood (6, 1.0F, "short_cake", 0);
		cheese_cake = new SMFood (4, 1.5F, "cheese_cake", 0);
		chocolate_cake = new SMFood (5, 1.1F, "chocolate_cake", 0);
		gateau_chocolat = new SMFood(7, 0.65F, "gateau_chocolat", 0);
		cake_roll = new SMFood (5, 1.2F, "cake_roll", 0);
		applepie = new SMFood(7, 0.65F, "applepie", 0);
		raspberrypie = new SMFood(5, 0.7F, "raspberrypie", 0);
		strawberry_tart = new SMFood (8, 1F, "strawberry_tart", 0);
		peach_tart = new SMFood (6, 0.75F, "peach_tart", 0);
		orange_tart = new SMFood (6, 0.75F, "orange_tart", 0);
		mont_blanc = new SMFood (7, 0.8F, "mont_blanc", 0);
		talttatan = new SMFood(6, 0.75F, "talttatan", 0);
		german_tree_cake = new SMFood(8, 0.5F, "german_tree_cake", 0);
		cake_chiffon = new SMFood(7, 0.6F, "cake_chiffon", 0);
		eclair = new SMFood (6, 0.6F, "eclair", 0);
		hotcake = new SMFood (8, 0.55F, "hotcake", 0);
		fruit_crepe = new SMFood (8, 0.25F, "fruit_crepe", 0);
		cream_brulee = new SMFood (8, 0.4F, "cream_brulee", 0);
		panna_cotta = new SMFood (6, 0.65F, "panna_cotta", 0);
		marshmallow = new SMFood (4, 0.5F, "marshmallow", 1);
		yogurt  = new SMFood(2, 0.5F, "yogurt", 0);

		witch_cake  = new SMFood(8, 0.65F, "witch_cake", 8);
		devil_cake  = new SMFood(6, 0.5F, "devil_cake", 9);

		// 飲み物
		corn_soup = new SMDrink(7, 0.5F, "corn_soup", 0);
		berryorange_juice = new SMDrink(9, 0.25F, "berryorange_juice", 0);
		strawberrymilk = new SMDrink(4, 1.25F, "strawberrymilk", 0);
		coconut_juice = new SMDrink(6, 0.65F, "coconut_juice", 0);
		cocoa = new SMDrink(8, 0.5F, "cocoa", 0);
		pumpkin_soup = new SMDrink(6, 0.8F, "pumpkin_soup", 0);
		banana_smoothy = new SMDrink(6, 0.8F, "banana_smoothy", 0);
		mixed_juice = new SMDrink(6, 0.8F, "mixed_juice", 0);
		milk_pack = new SMDrink(1, 0.5F,"milk_pack", 1);
		soy_soup = new SMDrink(6, 0.3F,"soy_soup", 1);
		pork_soup = new SMDrink(7, 0.6F,"pork_soup", 0);
		coffee = new SMDrink(5, 0F,"coffee", 2);
		cafe_latte = new SMDrink(5, 0.1F,"cafe_latte", 1);
		vienna_coffee = new SMDrink(5, 0.2F,"vienna_coffee", 4);
		fruit_wine = new SMDrink(2, 0.5F,"fruit_wine", 3);
		pine_smoothy = new SMDrink(8, 0.4F,"pine_smoothy", 0);
		seaplant_soup = new SMDrink(7, 0.3F,"seaplant_soup", 0);
		milkshake = new SMDrink(6, 0.4F,"milkshake", 0);

	}

	//アイテムをクリエタブに追加
	public static void register() {

    	// 魔法
		for (Item item : magicList) {
			registerItem(item, 2);
		}

    	// 通常
		for (Item item : itemList) {
			registerItem(item, 0);
		}

    	// 家具
		for (Item item : furniList) {
			registerItem(item, 4);
		}

    	// 食べ物
		for (Item item : foodList) {
			registerItem(item, 1);
		}

		for (Item item : noTabList) {
			registerItem(item, 3);
		}
	}

	public static void registerItem(Item item, int data) {

    	ForgeRegistries.ITEMS.register(item);

    	switch (data) {

    	// 通常
    	case 0:
    		item.setCreativeTab(SweetMagicCore.SMTab);
    		break;

        // 食べ物
    	case 1:
    		item.setCreativeTab(SweetMagicCore.SMFoodTab);
    		break;

    	// 魔法
    	case 2:
    		item.setCreativeTab(SweetMagicCore.SMMagicTab);
    		break;

        // 家具
    	case 4:
    		item.setCreativeTab(SweetMagicCore.SMFurnitureTab);
    		break;
    	}

        if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
        	ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
