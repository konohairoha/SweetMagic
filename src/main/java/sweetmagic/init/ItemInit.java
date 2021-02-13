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
import sweetmagic.init.item.sm.armor.MagiciansPouch;
import sweetmagic.init.item.sm.armor.MagiciansRobe;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.magic.AirMagic;
import sweetmagic.init.item.sm.magic.CardHeal;
import sweetmagic.init.item.sm.magic.CardMagic;
import sweetmagic.init.item.sm.magic.ChargeMagic;
import sweetmagic.init.item.sm.magic.GrandMagic;
import sweetmagic.init.item.sm.magic.MFItem;
import sweetmagic.init.item.sm.magic.MFStuff;
import sweetmagic.init.item.sm.magic.MFTeleport;
import sweetmagic.init.item.sm.magic.MFTime;
import sweetmagic.init.item.sm.magic.MFWeather;
import sweetmagic.init.item.sm.magic.MagicianBeginnerBook;
import sweetmagic.init.item.sm.magic.ShotMagic;
import sweetmagic.init.item.sm.seed.MagicMeal;
import sweetmagic.init.item.sm.seed.SMSeed;
import sweetmagic.init.item.sm.seed.SMSeedFood;
import sweetmagic.init.item.sm.sweetmagic.CosmicWand;
import sweetmagic.init.item.sm.sweetmagic.SMAxe;
import sweetmagic.init.item.sm.sweetmagic.SMBook;
import sweetmagic.init.item.sm.sweetmagic.SMBucket;
import sweetmagic.init.item.sm.sweetmagic.SMDrink;
import sweetmagic.init.item.sm.sweetmagic.SMDropItem;
import sweetmagic.init.item.sm.sweetmagic.SMElementItem;
import sweetmagic.init.item.sm.sweetmagic.SMFood;
import sweetmagic.init.item.sm.sweetmagic.SMFoodItem;
import sweetmagic.init.item.sm.sweetmagic.SMFuel;
import sweetmagic.init.item.sm.sweetmagic.SMGuidBook;
import sweetmagic.init.item.sm.sweetmagic.SMHoe;
import sweetmagic.init.item.sm.sweetmagic.SMIDoor;
import sweetmagic.init.item.sm.sweetmagic.SMItem;
import sweetmagic.init.item.sm.sweetmagic.SMPick;
import sweetmagic.init.item.sm.sweetmagic.SMShears;
import sweetmagic.init.item.sm.sweetmagic.SMShovel;
import sweetmagic.init.item.sm.sweetmagic.SMSickle;
import sweetmagic.init.item.sm.sweetmagic.SMSummon;
import sweetmagic.init.item.sm.sweetmagic.SMSword;
import sweetmagic.init.item.sm.sweetmagic.SMWand;

public class ItemInit {

	// 魔法本
	public static Item magic_book, guide_book;

	// マジックハウス
	public static Item magicianbeginner_book;

    // 天気花びら、種
	public static Item sannyflower_seed, sannyflower_petal;
	public static Item moonblossom_seed, moonblossom_petal;
	public static Item dm_seed, dm_flower;

    // 鉱石、クリスタル
	public static Item alternative_ingot, aether_crystal_shard, aether_crystal, divine_crystal, pure_crystal, deus_crystal;

	// エーテルツール
	public static Item alt_axe, alt_pick, alt_shovel, alt_hoe, alt_shears, alt_sword, alt_sickle;
	public static Item alt_bucket, alt_bucket_water, alt_bucket_lava;
	public static Item knife_of_thief, machete;

    // 花びら、種
	public static Item magicmeal, clero_petal, clerodendrum_seed, prizmium;
	public static Item fire_powder, fire_nasturtium_petal, fire_nasturtium_seed;
	public static Item sticky_stuff_petal, sticky_stuff_seed, glowflower_seed;
	public static Item cotton, cotton_seed;

    // MFボトル
	public static Item b_mf_bottle, mf_sbottle, mf_bottle;

	// 魔法素材
	public static Item mysterious_page, ender_shard, stray_soul, electronic_orb, poison_bottle, witch_tears, veil_darkness;

	// 魔法素材（魔法）
	public static Item unmeltable_ice, grav_powder, tiny_feather, blank_page, blank_magic, cotton_cloth, mystical_page;

	// 杖
	public static Item mf_stuff, aether_wand, divine_wand, purecrystal_wand, deuscrystal_wand, creative_wand;

	// ドア
	public static Item black_moderndoor, brown_2paneldoor, brown_5paneldoor, brown_elegantdoor, brown_arch_door, brown_arch_plantdoor;
	public static Item woodgold_3, whitewoodgold_3;

	// 料理素材
	public static Item sugarbell, vannila_beans, vannila_essence, olive_oil, whipping_cream;
	public static Item  butter, salt, watercup, breadcrumbs, flourpowder, ine, kinako, gelatin;

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

    // 簡易魔法
	public static Item card_normal, card_heal;

	// 燃料アイテム
	public static Item plant_chips;

	// 食べ物(作物)
	public static Item lemon, orange, chestnut, coconut, whitenet;

	// 種
	public static Item sugarbell_seed, whitenet_seed, corn_seed, eggplant_seed;
	public static Item j_radish_seed, lettuce_seed, cabbage_seed, azuki_seed, rice_seed;
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
	public static Item saba_miso, fluit_mix, pizza, gratin, estor_apple, applepie;

	// 飲み物
	public static Item corn_soup, berryorange_juice, strawberrymilk, coconut_juice, milk_pack;

	//　調味料
	public static Item vannila_pods, olive, miso;

	// 防具
	public static Item magicians_robe, wizard_robe, magicians_pouch;

    public static List<Item> itemList = new ArrayList();
    public static List<Item> foodList = new ArrayList();
    public static List<Item> noTabList = new ArrayList();

	// アイテムにデータを入れる
	public static void init() {

		// 魔法本
        magic_book = new SMBook("magic_book");
        guide_book = new SMGuidBook("guide_book");

        magicianbeginner_book = new MagicianBeginnerBook("magicianbeginner_book", 0);

        // 天気花びら、種
		sannyflower_petal = new MFTime("sannyflower_petal", 0);
		sannyflower_seed = new SMSeed("sannyflower_seed", BlockInit.sannyflower_plant, 1);
		moonblossom_petal = new MFTime("moonblossom_petal", 14000);
        moonblossom_seed = new SMSeed("moonblossom_seed", BlockInit.moonblossom_plant, 1);
        dm_seed = new SMSeed("drizzly_mysotis_seed", BlockInit.dm_plant, 1);
		dm_flower = new MFWeather("drizzly_mysotis_flower", 0, SMElement.WATER, 12000);

        // 鉱石、クリスタル
		alternative_ingot = new SMItem("alternative_ingot");
		aether_crystal_shard = new MFItem("aether_crystal_shard", 50);
		aether_crystal = new MFItem("aether_crystal", 600);
		divine_crystal = new MFItem("divine_crystal", 8000);
		pure_crystal = new MFItem("pure_crystal", 90000);
		deus_crystal = new MFItem("deus_crystal", 450000);

		// エーテルツール
		//斧　　：アイテム名　=new アイテムアックス("アイテム名"、採掘レベル、耐久値、効率、攻撃力、エンチャントレベル)、攻撃力、攻撃速度
		//斧以外：アイテム名　=new アイテムピッケル("アイテム名"、採掘レベル、耐久値、効率、攻撃力、エンチャントレベル)
        alt_axe = new SMAxe("alt_axe", EnumHelper.addToolMaterial("alt_axe", 3, 512, 8, 1, 8), 9F, -3.1f);
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
        machete = new SMSword("machete", 1, 512, 1F, -3, 0, 0D, 2);

        // 花びら、種
		magicmeal = new MagicMeal("magicmeal");
		sugarbell = new SMElementItem("sugarbell", SMElement.EARTH);
        sugarbell_seed = new SMSeed("sugarbell_seed", BlockInit.sugarbell_plant, 1);
		clero_petal = new MFTeleport("clero_petal", true);
		clerodendrum_seed = new SMSeed("clerodendrum_seed", BlockInit.clerodendrum, 1);
		fire_powder = new SMItem("fire_powder");
		fire_nasturtium_petal = new MFWeather("fire_nasturtium_petal", 1600, SMElement.FLAME, 0);
        fire_nasturtium_seed = new SMSeed("fire_nasturtium_seed",BlockInit.fire_nasturtium_plant, 1);
		sticky_stuff_petal = new SMItem("sticky_stuff_petal");
        sticky_stuff_seed = new SMSeed("sticky_stuff_seed", BlockInit.sticky_stuff_plant, 1);
        glowflower_seed = new SMSeed("glowflower_seed", BlockInit.glowflower_plant, 1);
        cotton = new SMItem("cotton");
        cotton_seed = new SMSeed("cotton_seed", BlockInit.cotton_plant, 1);

        // MFボトル
		mf_sbottle = new MFItem("mf_sbottle", 1000);
		b_mf_bottle = new SMItem("b_mf_bottle");
		mf_bottle = new MFItem("mf_bottle", 10000);

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
		blank_page = new SMItem("blank_page");
		blank_magic = new SMItem("blank_magic");
		mysterious_page = new SMDropItem("mysterious_page");
		mystical_page = new SMItem("mystical_page");
		cotton_cloth = new SMItem("cotton_cloth");
		veil_darkness = new SMSummon("veil_darkness");

		magicians_robe = new MagiciansRobe("magicians_robe", ArmorInit.magicians_robe, 1, EntityEquipmentSlot.CHEST, 1);
		wizard_robe = new MagiciansRobe("wizard_robe", ArmorInit.wizard_robe, 1, EntityEquipmentSlot.CHEST, 1);
		magicians_pouch = new MagiciansPouch("magicians_pouch", ArmorInit.magicians_pouch, 1, EntityEquipmentSlot.LEGS, 1);

		// 杖
		mf_stuff = new MFStuff("magiaflux_stuff");
		aether_wand = new SMWand("aether_wand", 1, 3000, 3);
		divine_wand = new SMWand("divine_wand", 2, 15000, 6);
		purecrystal_wand = new CosmicWand("purecrystal_wand", 3, 40000, 9);
		deuscrystal_wand = new CosmicWand("deuscrystal_wand", 4, 80000, 12);
		creative_wand = new SMWand("creative_wand", 7, 1, 16);

		// ドア
		black_moderndoor = new SMIDoor("black_moderndoor_i", BlockInit.black_moderndoor);
		brown_2paneldoor = new SMIDoor("brown_2paneldoor_i", BlockInit.brown_2paneldoor);
		brown_5paneldoor = new SMIDoor("brown_5paneldoor_i", BlockInit.brown_5paneldoor);
		brown_elegantdoor = new SMIDoor("brown_elegantdoor_i", BlockInit.brown_elegantdoor);
		brown_arch_door = new SMIDoor("brown_arch_door_i", BlockInit.brown_arch_door);
		brown_arch_plantdoor = new SMIDoor("brown_arch_plantdoor_i", BlockInit.brown_arch_plantdoor);
		woodgold_3 = new SMIDoor("woodgold_3_i", BlockInit.woodgold_3);
		whitewoodgold_3 = new SMIDoor("whitewoodgold_3_i", BlockInit.whitewoodgold_3);

		//アイテム燃料…名前、燃焼時間
		plant_chips = new SMFuel("plant_chips", 400);

		// 料理素材 アイテム名　=new アイテムクラス("アイテム名")
		vannila_beans = new SMFoodItem("vannila_beans");
		vannila_essence = new SMFoodItem("vannila_essence");
		olive_oil = new SMFoodItem("olive_oil");
		whipping_cream = new SMFoodItem("whipping_cream");
		butter = new SMFoodItem("butter");
		salt = new SMFoodItem("salt");
		watercup = new SMFoodItem("watercup");
		breadcrumbs = new SMFoodItem("breadcrumbs");
		flourpowder = new SMFoodItem("flourpowder");
		ine = new SMFoodItem("ine");
		kinako = new SMFoodItem("kinako");
		gelatin = new SMFoodItem("gelatin");
		cocoamass = new SMFoodItem("cocoamass");
		cocoabutter = new SMFoodItem("cocoabutter");
		cocoapowder = new SMFoodItem("cocoapowder");
		custard = new SMFoodItem("custard");
		miso = new SMFoodItem("miso");

		// 魔法 (アイテム名、必要経験値量、データ値、属性、tier、クールタイム、必要MF量)
        magic_fire = new ShotMagic("magic_fire", 0, SMElement.FLAME, 1, 40, 30);
        magic_flamenova = new ShotMagic("magic_flamenova", 10, SMElement.FLAME, 2, 120, 150, "magic_fire");
        magic_meteor_fall = new AirMagic("magic_meteor_fall", 9, SMElement.FLAME, 3, 160, 300, "magic_fire");

        magic_frost = new ShotMagic("magic_frost", 1, SMElement.FROST, 1, 60, 40);
        magic_frostspear = new ShotMagic("magic_frostspear", 9, SMElement.FROST, 2, 100, 120, "magic_frost");
        magic_frostrain = new AirMagic("magic_frostrain", 10, SMElement.FROST, 3, 300, 400, "magic_frost");

        magic_effectremover = new AirMagic("magic_effectremover", 5, SMElement.WATER, 1, 1000, 100);
        magic_refresh  = new AirMagic("magic_refresh", 7, SMElement.WATER, 2, 1600, 200, "magic_effectremover");

        magic_regene = new AirMagic("magic_regene", 2, SMElement.WATER, 1, 400, 100, "magic_heal");
        magic_heal = new AirMagic("magic_heal", 0, SMElement.WATER, 2, 800, 200);
        magic_healingwish  = new AirMagic("magic_healingwish", 8, SMElement.WATER, 3, 1600, 400, "magic_heal");

        magic_elecarmor = new AirMagic("magic_elecarmor", 6, SMElement.LIGHTNING, 1, 1400, 100, "magic_thunder");
        magic_lightningbolt = new ChargeMagic("magic_lightningbolt", 3, SMElement.LIGHTNING, 2, 300, 200, "magic_thunder");
        magic_thunder = new ChargeMagic("magic_thunder", 2, SMElement.LIGHTNING, 3, 200, 300);

        magic_shadowwolf = new GrandMagic("magic_shadowwolf", 0, SMElement.DARK, 1, 6000, 100, "magic_shadow");
        magic_shadowgolem = new GrandMagic("magic_shadowgolem", 1, SMElement.DARK, 2, 8000, 300, "magic_shadow");
        magic_shadow = new AirMagic("magic_shadow", 1, SMElement.DARK, 3, 1200, 300);

        magic_rangepoison = new ShotMagic("magic_rangepoison", 11, SMElement.TOXIC, 1, 400, 50, "magic_poison");
        magic_poison = new AirMagic("magic_poison", 4, SMElement.TOXIC, 2, 1300, 200);

        magic_ballast = new ShotMagic("magic_ballast", 6, SMElement.GRAVITY, 1, 60, 20, "magic_gravity");
        magic_gravitywave = new ShotMagic("magic_gravitywave", 13, SMElement.GRAVITY, 2, 120, 140, "magic_gravity");
        magic_gravity_break = new ShotMagic("magic_gravity_break", 19, SMElement.GRAVITY, 3, 140, 400, "magic_gravity");

        magic_vector_boost = new AirMagic("magic_vector_boost", 11, SMElement.GRAVITY, 1, 1200, 100);
        magic_vector_halten = new AirMagic("magic_gravity", 12, SMElement.GRAVITY, 2, 1400, 200, "magic_vector_boost");

        magic_barrier = new AirMagic("magic_barrier", 3, SMElement.SHINE, 1, 1200, 200);

        magic_light = new ShotMagic("magic_light", 2, SMElement.SHINE, 1, 30, 30);
        magic_starburst = new ShotMagic("magic_starburst", 7, SMElement.SHINE, 2, 80, 80, "magic_light");
        magic_sacredbuster = new ShotMagic("magic_sacredbuster", 16, SMElement.SHINE, 3, 200, 200, "magic_light");

        magic_tornado = new ShotMagic("magic_tornado", 5, SMElement.CYCLON, 1, 50, 30, "magic_cyclon");
        magic_storm = new ShotMagic("magic_storm", 12, SMElement.CYCLON, 2, 100, 160, "magic_cyclon");
        magic_cyclon = new ShotMagic("magic_cyclon", 18, SMElement.CYCLON, 3, 133, 300);

        magic_dig = new ShotMagic("magic_dig", 3, SMElement.EARTH, 1, 10, 10);
        magic_rangebreaker = new ShotMagic("magic_rangebreaker", 8, SMElement.EARTH, 2, 30, 20, "magic_dig");
        magic_mining_magia = new ShotMagic("magic_mining_magia", 14, SMElement.EARTH, 3, 50, 30, "magic_dig");

        magic_growth_effect = new ChargeMagic("magic_growth_effect", 6, SMElement.EARTH, 1, 300, 100, "magic_growth_aura");
        magic_growth_aura = new ChargeMagic("magic_growth_aura", 5, SMElement.EARTH, 2, 100, 500);

        magic_burst = new ShotMagic("magic_burst", 15, SMElement.BLAST, 1, 160, 100, "magic_tamagotti");
        magic_blast = new ShotMagic("magic_tamagotti", 4, SMElement.BLAST, 2, 120, 200);
        magic_magia_destroy = new ShotMagic("magic_magia_destroy", 17, SMElement.BLAST, 3, 80, 300, "magic_tamagotti");

        magic_slowtime = new ChargeMagic("magic_slowtime", 4, SMElement.TIME, 1, 1500, 100);
        magic_deusora = new AirMagic("magic_deusora", 13, SMElement.TIME, 2, 3600, 200, "magic_slowtime");
        magic_futurevision = new AirMagic("magic_futurevision", 14, SMElement.TIME, 3, 2400, 300, "magic_slowtime");

        // 簡易魔法
        card_normal = new CardMagic("card_normal", 50, 0);
        card_heal = new CardHeal("card_heal", 32);

		// 食べ物 アイテム名　=new (満腹度回復、隠し満腹度(乗算)、"アイテム名", ポーション効果の整数)
		lemon = new SMFood(3, 0.3F, "lemon", 0);
		orange = new SMFood(3, 0.3F, "orange", 0);
		estor_apple = new SMFood(3, 0.5F, "estor_apple", 0);
		chestnut = new SMFood(4, 0.8F, "chestnut", 0);
		coconut = new SMFood(3, 1F, "coconut", 0);
		cheese = new SMFood(4, 0.3F, "cheese",0);
		banana = new SMFood(2, 1.5F, "banana",0);
		whitenet = new SMFood(4, 0.45F, "whitenet",0);
		rice = new SMFood(4, 0.3F, "rice", 0);
		edamame = new SMFood(2, 0.3F, "edamame", 1);
		cake_dough = new SMFood(4, 0.45F, "cake_dough", 0);
		corn = new SMFood(3, 0.3F, "corn", 0);
		j_radish = new SMFood(2, 0.5F, "j_radish", 0);
		eggplant = new SMFood(2, 0.5F, "eggplant", 0);
		lettuce = new SMFood(2, 0.5F, "lettuce", 0);
		cabbage = new SMFood(2, 0.5F, "cabbage", 0);
		yaki_imo = new SMFood(5, 0.5F, "yaki_imo", 1);
		sweet_potato = new SMFood (6, 0.75F, "sweet_potato", 0);
		yaki_banana = new SMFood(6, 0.4F, "yaki_banana", 1);
		itigo_daihuku = new SMFood(5, 0.5F, "itigo_daihuku", 0);
		cooked_corn = new SMFood(8, 0.25F, "cooked_corn", 0);
		potatobutter = new SMFood(7, 0.5F, "potatobutter", 1);
		boiled_edamame = new SMFood(6, 0.2F, "boiled_edamame", 0);
		ogura_toast = new SMFood(9, 0.6F, "ogura_toast", 0);
		butter_toast = new SMFood(7, 0.4F, "butter_toast", 0);
		jam_toast = new SMFood(10, 0.4F, "jam_toast", 0);
		french_toast = new SMFood(10, 0.4F, "french_toast", 0);
		egg_sandwitch = new SMFood(6, 0.75F, "egg_sandwitch", 0);
		butadon = new SMFood(8, 0.8F, "butadon", 0);
		gyuudon = new SMFood(10, 0.5F, "gyuudon", 0);
		cheegyu = new SMFood(13, 0.45F, "cheegyu", 0);
		nikujaga = new SMFood(16, 0.45F, "nikujaga", 0);
		yakinasu = new SMFood (5, 0.3F, "yakinasu", 0);
		tamagoyaki = new SMFood (6, 0.5F, "tamagoyaki", 0);
		buridaikon = new SMFood (8, 0.4F, "buridaikon", 0);
		salmon_meuniere = new SMFood(4, 0.6F, "salmon_meuniere", 0);
		saba_miso = new SMFood(8, 0.5F, "saba_miso", 0);
		roll_cabbage = new SMFood (11, 0.75F, "roll_cabbage", 0);
		beefstew = new SMFood (10, 0.75F, "beefstew", 0);
		stew = new SMFood (9, 0.95F, "stew", 0);
		sukiyaki = new SMFood (11, 0.5F, "sukiyaki", 3);
		sauteed_mushrooms = new SMFood (7, 0.6F, "sauteed_mushrooms", 0);
		medamayaki = new SMFood (6, 0.75F, "medamayaki", 0);
		kurikinton = new SMFood (7, 0.55F, "kurikinton", 0);
		pumpkin_nituke = new SMFood (7, 0.6F, "pumpkin_nituke", 0);
		strawberry_jelly = new SMFood (4, 0.75F, "strawberry_jelly", 2);
		orange_jelly = new SMFood (5, 0.6F, "orange_jelly", 2);
		kakigori_lemon = new SMFood (8, 0F, "kakigori_lemon", 2);
		kakigori_strawberry = new SMFood (6, 0.2F, "kakigori_strawberry", 2);
		kakigori_milk = new SMFood (7, 0.1F, "kakigori_milk", 2);
		chocolate = new SMFood (8, 0.25F, "chocolate", 0);
		white_chocolate = new SMFood (4, 1F, "white_chocolate", 0);
		choco_pie = new SMFood (6, 0.5F, "choco_pie", 1);
		pudding = new SMFood (7, 0.65F, "pudding", 0);
		salt_popcorn = new SMFood(5, 0.25F, "salt_popcorn", 1);
		caramel_popcorn = new SMFood(6, 0.38F, "caramel_popcorn", 1);
		lemon_cookie = new SMFood(6, 0.5F, "lemon_cookie", 1);
		icebox_cookie = new SMFood (6, 0.5F, "icebox_cookie", 1);
		strawberry_tart = new SMFood (8, 1F, "strawberry_tart", 0);
		mont_blanc = new SMFood (7, 0.8F, "mont_blanc", 0);
		fruit_crepe = new SMFood (8, 0.25F, "fruit_crepe", 0);
		youkan = new SMFood (4, 1.5F, "youkan", 0);
		scone = new SMFood (5, 0.6F, "scone", 1);
		blueberry_muffin = new SMFood(7, 0.5F, "blueberry_muffin", 1);
		chocolate_muffin = new SMFood(8, 0.35F, "chocolate_muffin", 1);
		toast = new SMFood (4, 0.5F, "toast", 0);
		cream_puff = new SMFood (8, 0.65F, "cream_puff", 0);
		short_cake = new SMFood (6, 1.0F, "short_cake", 0);
		cheese_cake = new SMFood (4, 1.5F, "cheese_cake", 0);
		chocolate_cake = new SMFood (5, 1.1F, "chocolate_cake", 0);
		cake_roll = new SMFood (5, 1.2F, "cake_roll", 0);
		eclair = new SMFood (8, 0.4F, "eclair", 0);
		marshmallow = new SMFood (4, 0.5F, "marshmallow", 1);
		yogurt  = new SMFood(2, 0.5F, "yogurt", 0);
		croquette  = new SMFood(7, 0.65F, "croquette", 0);
		fluit_mix = new SMFood(12, 0.1F, "fluit_mix", 0);
		pizza = new SMFood(10, 0.7F, "pizza", 0);
		gratin = new SMFood(8, 0.6F, "gratin", 0);
		applepie = new SMFood(7, 0.65F, "applepie", 0);

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

        // 作物種
		blueberry = new SMSeedFood("blueberry", BlockInit.blueberry_plant, 3, 0.4F, true);
		raspberry = new SMSeedFood("raspberry", BlockInit.raspberry_plant, 4, 0.35F, true);
		strawberry = new SMSeedFood("strawberry",BlockInit.strawberry_plant, 4, 0.3F, false);
		soybean = new SMSeedFood("soybean", BlockInit.soybean_plant, 3, 0.4F, false);
		sweetpotato = new  SMSeedFood("sweetpotato", BlockInit.sweetpotato_plant, 3, 0.4F, false);
		tomato = new  SMSeedFood("tomato", BlockInit.tomato_plant, 3, 0.33F, false);
		onion = new  SMSeedFood("onion", BlockInit.onion_plant, 3, 0.33F, false);

	}

	//アイテムをクリエタブに追加
	public static void register() {

		for (Item item : itemList) {
			registerItem(item, 0);
		}

		for (Item item : foodList) {
			registerItem(item, 1);
		}

		for (Item item : noTabList) {
			registerItem(item, 2);
		}
	}

	public static void registerItem(Item item, int data) {
    	ForgeRegistries.ITEMS.register(item);
    	if (data == 0) {
    		item.setCreativeTab(SweetMagicCore.SMTab);
    	} else if (data == 1) {
    		item.setCreativeTab(SweetMagicCore.SMFoodTab);
    	}
        if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
        	ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
