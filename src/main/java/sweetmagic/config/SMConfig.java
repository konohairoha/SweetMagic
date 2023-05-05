package sweetmagic.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

//@Mod.EventBusSubscriber(modid = SweetMagicCore.MODID)
public class SMConfig {

	private SMConfig() {}

	public static final SMConfig INSTANCE = new SMConfig();

	// コンフィググループ
	private final static String ITEMADD = "Item";
	private final static String ITEMADD_COME = "Settings for Items and Recipes";
	private final static String RENDER = "Render";
	private final static String RENDER_COME = "Render Settings.";
	private final static String STRUCTURE = "Structure";
	private final static String STRUCTURE_COME = "Structure Setup";
	private final static String BIOME = "Biome";
	private final static String BIOME_COME = "Biome settings.";
	private final static String DIM = "Dimension";
	private final static String DIM_COME = "Set the Dimension ID.";
	private final static String DIFFICUL = "Difficulty";
	private final static String DIFFICUL_COME = "Combat Difficulty Settings.";
	private final static String MFBLOCK = "Magic Blocks";
	private final static String MFBLOCK_COME = "Settings for Magic Blocks.";
	private final static String PLANT = "Crop Related";
	private final static String PLANT_COME = "Set number of Crops and Growth Rate.";

	// ヘルプレシピ
	public static boolean help_recipe = false;
	private static final String help_recipe_name = "Add Help Recipes";
	private static final String help_recipe_come = "Add Some Help Recipes.  Default = false";

	// 追加バニラレシピ
	public static boolean addVanilaRecipe = true;
	private static final String addVanilaRecipe_name = "Add Vanila Recipes";
	private static final String addVanilaRecipe_come = "Add Some Vanila Recipes.  Default = true";

	// クリスタルドロップ
	public static boolean mobdrop_crystal = false;
	private static final String mobdrop_crystal_name = "[Aether_Crystal] Drop";
	private static final String mobdrop_crystal_come = "[Aether Crystal] drop when mobs are defeated at [Knife of Pirates].  Default = false";

	// ダンジョン生成
	public static boolean isGenStructure = true;
	private static final String structure_name = "Generate Structure";
	private static final String structure_come = "Generate Structure in the World.  Default = true";

	// 家の生成チャンス
	public static int house_spawnchance = 360;
	private static final String house_spawnchance_name = "Generating Magical House Rate";
	private static final String house_spawnchance_come = "Set the frequency of Magical House Generation.  Default = 360";

	// 家ルートテーブル
	public static int house_lootchance = 60;
	private static final String house_lootchance_name = "Magical House Loot Change";
	private static final String house_lootchance_come = "You can change the Magical House Loot.  Default = 60";

	// ダンジョン生成チャンス
	public static int dungeon_spawnchance = 480;
	private static final String dungeon_spawnchance_name = "Dungeon Generate Change Rate";
	private static final String dungeon_spawnchance_come = "Set the frequency of Dungeon Generation.  Default = 480";

	// ダンジョンルートテーブル
	public static int dungeon_lootchance = 32;
	private static final String dungeon_lootchance_name = "Dungeons Loot Change";
	private static final String dungeon_lootchance_come = "You can change the Dungeons Loot.  Default = 32";

	// モブスポーン
	public static boolean spawn_mob = true;
	private static final String spawn_mob_name = "Mob Spawn";
	private static final String spawn_mob_come = "SweetMagic's mob spawn settings.  Default = true";

	// レンダー
	public static boolean isRender = false;
	private static final String render_name = "Render Effect";
	private static final String render_come = "Lighten some Renders.  Default = false";

	// 杖GUI
	public static boolean isLeftSide = true;
	private static final String renderSide_name = "Wand GUI Settings";
	private static final String renderSide_come = "Pull the magic Wand GUI Left.  Default = true";

	// 杖GUI3表示
	public static boolean isSneakRender = true;
	private static final String sneakRender_name = "Multiple magic display during sneak";
	private static final String sneakRender_come = "true: 3  false: 1   Default = true";

	// 杖GUI
	public static boolean isPorchRenderGUI = true;
	private static final String renderProchGui_name = "Porch GUI Settings";
	private static final String renderProchGui_come = "Display of items with cool time.  Default = true";

	// バイオーム
	public static boolean genBiome = true;
	private static final String biome_name = "Generate Biome";
	private static final String biome_come = "Generate Biome in the Overworld.  Default = true";

	// 花
	public static boolean genFlowers = true;
	private static final String flower_name = "Generate Flowers";
	private static final String flower_come = "Generating Flowers in the Overworld.  Default = true";

	// ディメンション
	public static int dimId = 1222;
	private static final String dim_name = "Dimension ID";
	private static final String dim_come = "Set the Dimension ID.  Default = 1222";

	// 難易度
	public static int isHard = 1;
	private static final String hard_name = "Enemy Mob Strength";
	private static final String hard_come = "0 = Sweet, 1 = Normal, 2 = Bitter, 3 = Verry Bitter.  Default = 1";

	// ダメージレート
	public static double damageRate = 1D;
	private static final String damageRate_name = "Damage Rate";
	private static final String damageRate_come = "Magic Damage Rate Setting. Default = 1.0";

	// 最大エンチャレベル
	public static int maxLevel = 10;
	private static final String maxLevel_name= "Max Enchantment Level";
	private static final String maxLevel_come= "Setting the Max Enchantment Level.  Default = 10";

	// マギアライトの範囲設定
	public static int magiaLightRange = 128;
	private static final String magiaLightRange_name= "Magia Light Range";
	private static final String magiaLightRange_come= "Range Where Mobs are not Spawned.  Default = 128";

	// 敵モブスポーン日数設定
	public static int spawnDay = 3;
	private static final String spawnDay_name= "Days to Spawn";
	private static final String spawnDay_come= "Number of Days until Enemy Mobs Spawn.  Default = 3";

	// 作説成長速度
	public static int glowthRate = 0;
	private static final String glowthRate_name= "Crop Growth Rate";
	private static final String glowthRate_come= "The larger the number, the harder it is to grow. (Minimum value: 0)  Default = 0";

	// 作説入手個数
	public static int glowthValue = 0;
	private static final String glowthValue_name= "Number of Crops Dropped";
	private static final String glowthValue_come= "Increases or decreases the number of crop drops from -2 to 2.  Default = 0";

	public void load(File file) {
		File cfgFile = new File(file, "sweetmagic.cfg");
		SMConfig.INSTANCE.load(new Configuration(cfgFile));
	}

	public void load(Configuration cfg) {

		try {

			// 初期読み込み
			cfg.load();

			// カテゴリーコメント
			cfg.setCategoryComment(ITEMADD, ITEMADD_COME);
			cfg.setCategoryComment(STRUCTURE, STRUCTURE_COME);
			cfg.setCategoryComment(RENDER, RENDER_COME);
			cfg.setCategoryComment(BIOME, BIOME_COME);
			cfg.setCategoryComment(DIM, DIM_COME);
			cfg.setCategoryComment(DIFFICUL, DIFFICUL_COME);
			cfg.setCategoryComment(MFBLOCK, MFBLOCK_COME);
			cfg.setCategoryComment(PLANT, PLANT_COME);

			//　Property　別変数　=　cfg.get("小さいカテゴリでまとめるための文字列", "Key(例：add bettyutool=true　みたいな)", コンフィグ参照用変数)
			Property look_hrcp = cfg.get(ITEMADD, this.help_recipe_name, this.help_recipe, this.help_recipe_come);
			Property look_vanilacp = cfg.get(ITEMADD, this.addVanilaRecipe_name, this.addVanilaRecipe, this.addVanilaRecipe_come);
			Property look_mdropC = cfg.get(ITEMADD, this.mobdrop_crystal_name, this.mobdrop_crystal, this.mobdrop_crystal_come);
			Property look_spawn_house = cfg.get(STRUCTURE, this.structure_name, this.isGenStructure, this.structure_come);
			Property look_houseChance = cfg.get(STRUCTURE, this.house_spawnchance_name, this.house_spawnchance, this.house_spawnchance_come);
			Property look_spawn_houseloot = cfg.get(STRUCTURE, this.house_lootchance_name, this.house_lootchance, this.house_lootchance_come);
			Property look_dungeonChance = cfg.get(STRUCTURE, this.dungeon_spawnchance_name, this.dungeon_spawnchance, this.dungeon_spawnchance_come);
			Property look_spawn_dungeonloot = cfg.get(STRUCTURE, this.dungeon_lootchance_name, this.dungeon_lootchance, this.dungeon_lootchance_come);
			Property look_spawn_mob = cfg.get(DIFFICUL, this.spawn_mob_name, this.spawn_mob, this.spawn_mob_come);
			Property look_render = cfg.get(RENDER, this.render_name, this.isRender, this.render_come);
			Property look_renderSide = cfg.get(RENDER, this.renderSide_name, this.isLeftSide, this.renderSide_come);
			Property look_sneakRender = cfg.get(RENDER, this.sneakRender_name, this.isSneakRender, this.sneakRender_come);
			Property look_renderPorchGUI = cfg.get(RENDER, this.renderProchGui_name, this.isPorchRenderGUI, this.renderProchGui_come);
			Property look_biome = cfg.get(BIOME, this.biome_name, this.genBiome, this.biome_come);
			Property look_FLW = cfg.get(STRUCTURE, this.flower_name, this.genFlowers, this.flower_come);
			Property look_dim = cfg.get(DIM, this.dim_name, this.dimId, this.dim_come);
			Property look_hard = cfg.get(DIFFICUL, this.hard_name, this.isHard, this.hard_come);
			Property look_dmrate = cfg.get(DIFFICUL, this.damageRate_name, this.damageRate, this.damageRate_come);
			Property look_maxlevel = cfg.get(DIFFICUL, this.maxLevel_name, this.maxLevel, this.maxLevel_come);
			Property look_spawnDay = cfg.get(DIFFICUL, this.spawnDay_name, this.spawnDay, this.spawnDay_come);
			Property look_magiaLightRange = cfg.get(MFBLOCK, this.magiaLightRange_name, this.magiaLightRange, this.magiaLightRange_come);
			Property look_glowRate = cfg.get(PLANT, this.glowthRate_name, this.glowthRate, this.glowthRate_come);
			Property look_glowValue = cfg.get(PLANT, this.glowthValue_name, this.glowthValue, this.glowthValue_come);

			// look_○○　で　読み込んだ変数をStatic参照できる変数に代入
			this.help_recipe = look_hrcp.getBoolean();
			this.addVanilaRecipe = look_vanilacp.getBoolean();
			this.mobdrop_crystal = look_mdropC.getBoolean();
			this.isGenStructure = look_spawn_house.getBoolean();
			this.spawn_mob = look_spawn_mob.getBoolean();
			this.isRender = look_render.getBoolean();
			this.isLeftSide = look_renderSide.getBoolean();
			this.isSneakRender = look_sneakRender.getBoolean();
			this.isPorchRenderGUI = look_renderPorchGUI.getBoolean();
			this.genBiome = look_biome.getBoolean();
			this.genFlowers = look_FLW.getBoolean();
			this.dimId = look_dim.getInt();
			this.isHard = look_hard.getInt();
			this.damageRate = look_dmrate.getDouble();
			this.maxLevel = look_maxlevel.getInt();
			this.spawnDay = look_spawnDay.getInt();
			this.magiaLightRange = look_magiaLightRange.getInt();
			this.glowthRate = look_glowRate.getInt();
			this.glowthValue = look_glowValue.getInt();

			// int　0以下が設定されていたらデフォルトを設定
			this.house_spawnchance = look_houseChance.getInt();
			if(house_spawnchance < 1) { house_spawnchance = 240; }
			this.house_lootchance = look_spawn_houseloot.getInt();
			if(house_lootchance < 1) { house_lootchance = 60; }

			// int　0以下が設定されていたらデフォルトを設定
			this.dungeon_spawnchance = look_dungeonChance.getInt();
			if(dungeon_spawnchance < 1) { dungeon_spawnchance = 160; }
			this.dungeon_lootchance = look_spawn_dungeonloot.getInt();
			if(this.dungeon_lootchance < 1) { this.dungeon_lootchance = 32; }

			// int 0以下が設定
			if(this.damageRate <= 0) { this.damageRate = 1D; }
			if (this.maxLevel < 1) { this.maxLevel = 10; }
			if (this.house_lootchance < 1) { this.house_lootchance = 128; }

			this.glowthRate = Math.max(0, this.glowthRate);
			this.glowthValue = (this.glowthValue < -2 || this.glowthValue > 2) ? 0 : this.glowthValue;
		}

		// これはなんかのエラー用に置いておくっぽい。
		catch (Exception e) {
			e.printStackTrace();
		}

		// コンフィグで書き換えられている内容を保存するためにfinallyブロックで呼び出す。
		finally {
			cfg.save();
		}
	}
}
