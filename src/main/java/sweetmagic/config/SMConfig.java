package sweetmagic.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

//@Mod.EventBusSubscriber(modid = SweetMagicCore.MODID)
public class SMConfig {

	private SMConfig() {}

	public static final SMConfig INSTANCE = new SMConfig();

	// コンフィググループ
	public final static String ITEMADD = "item_add_config";
	public final static String ITEMADD_COME = "If those flag is set to true , those items(or some recipes) are added.";
	public final static String MISC = "misc_config";
	public final static String MISC_COME = "If those flag is set to true , you can use the little features.";
	public final static String MOB = "mob_config";
	public final static String MOB_COME = "If these flags are set to true, the mob will spawn.";
	public final static String RENDER = "render_config";
	public final static String RENDER_COME = "If these flags are set to true, some renders will not take place.";
	public final static String STRUCTURE_BOOL = "generate_structure_config";
	public final static String STRUCTURE_BOOL_BOOL_COME = "If these flags are set to true, Structure will be generate";
	public final static String BIOME = "generate_biome_config";
	public final static String BIOME_COME = "If these flags are set to true, we will generate a biome.";
	public final static String DIM = "dimension_Id_config";
	public final static String DIM_COME = "Set the dimension ID.";
	public final static String DIFFICUL = "difficulty config";
	public final static String DIFFICUL_COME = "Setting the difficulty level.";

	// ヘルプレシピ
	public static boolean help_recipe = false;
	public static String help_recipe_name = "add help recipes";
	public static String help_recipe_come = "When this flag is set to true, some help recipe are added. When this set to false, recipe is not added. default = false";

	// ナイフ
	public static boolean help_knifedrop = false;
	public static String help_knifedrop_name = "help knife of thief";
	public static String help_knifedrop_come = "When this flag is set to true, if entity is killed by player's Item[knife of thief], this some mod's item drop rarely . When this set to false, to no drop. default = false";

	// クリスタルドロップ
	public static boolean mobdrop_crystal = false;
	public static String mobdrop_crystal_name = "isMobDrop [aether_crystal]";
	public static String mobdrop_crystal_come = "When this flag is set to true, if entity is killed by player's Item[knife of thief], aether_crystal is drop at mob. When this set to false, to no drop. default = false";

	// ダンジョン生成
	public static boolean isGenStructure = true;
	public static String structure_name = "Generate Structure.";
	public static String structure_come = "If these flags are set to true, Structure will be generate";

	// 家の生成チャンス
	public static int house_spawnchance = 360;
	public static String house_spawnchance_name = "magical house spawn chance";
	public static String house_spawnchance_come = "Set up a chance to spawn magical home. default = 360";

	// 家ルートテーブル
	public static int house_lootchance = 60;
	public static String house_lootchance_name = "magical house loot chance";
	public static String house_lootchance_come = "Set up a loot chance to chest in magical home. default = 60";

	// ダンジョン生成チャンス
	public static int dungeon_spawnchance = 480;
	public static String dungeon_spawnchance_name = "dungeon generate chance";
	public static String dungeon_spawnchance_come = "Set up a chance to dungeon generate. default = 480";

	// ダンジョンルートテーブル
	public static int dungeon_lootchance = 32;
	public static String dungeon_lootchance_name = "dungeons loot chance";
	public static String dungeon_lootchance_come = "Set up a loot chance to chest in dungeons. default = 32";

	// モブスポーン
	public static boolean spawn_mob = true;
	public static String spawn_mob_name = "Enable mob spawn.";
	public static String spawn_mob_come = "If this setting is set to true, the mob may spawn.";

	// レンダー
	public static boolean isRender = true;
	public static String render_name = "Render Effect.";
	public static String render_come = "If these flags are set to true, some renders will not take place.";

	// 杖GUI
	public static boolean isLeftSide = true;
	public static String renderSide_name = "Position the wand GUI.";
	public static String renderSide_come = "If true, the wand's GUI will be displayed on the left side; if false, the GUI will be displayed on the right side.";

	// バイオーム
	public static boolean genBiome = true;
	public static String biome_name = "Generate Biome.";
	public static String biome_come = "If these flags are set to true, we will generate a biome.";

	// 花
	public static boolean genFlowers = true;
	public static String flower_name = "Generate Flowers.";
	public static String flower_come = "If these flags are set to true, we will generate flowers.";

	// ディメンション
	public static int dimId = 1222;
	public static String dim_name = "Set Dimension Id.";
	public static String dim_come = "Set the dimension ID. default = 1222";

	// 難易度
	public static boolean isHard = false;
	public static String hard_name = "High difficulty mode settings.";
	public static String hard_come = "By setting it to true, you can get a Stardust crystal in the bonus chest and various difficulty levels!";

	// ダメージレート
	public static double damageRate = 1D;
	public static String damageRate_name = "Damage rate for magic attack power.";
	public static String damageRate_come = "Set the damage rate for magic attack power. The higher the number, the greater the damage. Default is 1.0.";

	// 最大エンチャレベル
	public static int maxLevel = 10;
	public static String maxLevel_name= "Maximum level of Wand Add Power enchantment";
	public static String maxLevel_come= "Sets the maximum level of magic firepower increase enchantments. Normally only effective up to level 10, but can be changed.";


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
			cfg.setCategoryComment(STRUCTURE_BOOL, STRUCTURE_BOOL_BOOL_COME);
			cfg.setCategoryComment(MISC, MISC_COME);
			cfg.setCategoryComment(MOB, MOB_COME);
			cfg.setCategoryComment(RENDER, RENDER_COME);
			cfg.setCategoryComment(BIOME, BIOME_COME);
			cfg.setCategoryComment(DIM, DIM_COME);
			cfg.setCategoryComment(DIFFICUL, DIFFICUL_COME);

			//　Property　別変数　=　cfg.get("小さいカテゴリでまとめるための文字列", "Key(例：add bettyutool=true　みたいな)", コンフィグ参照用変数)
			Property look_hrcp = cfg.get(ITEMADD, this.help_recipe_name, this.help_recipe, this.help_recipe_come);
			Property look_knife = cfg.get(ITEMADD, this.help_knifedrop_name, this.help_knifedrop, this.help_knifedrop_come);
			Property look_mdropC = cfg.get(ITEMADD, this.mobdrop_crystal_name, this.mobdrop_crystal, this.mobdrop_crystal_come);
			Property look_spawn_house = cfg.get(STRUCTURE_BOOL, this.structure_name, this.isGenStructure, this.structure_come);
			Property look_houseChance = cfg.get(MISC, this.house_spawnchance_name, this.house_spawnchance, this.house_spawnchance_come);
			Property look_spawn_houseloot = cfg.get(MISC, this.house_lootchance_name, this.house_lootchance, this.house_lootchance_come);
			Property look_dungeonChance = cfg.get(MISC, this.dungeon_spawnchance_name, this.dungeon_spawnchance, this.dungeon_spawnchance_come);
			Property look_spawn_dungeonloot = cfg.get(MISC, this.dungeon_lootchance_name, this.dungeon_lootchance, this.dungeon_lootchance_come);
			Property look_spawn_mob = cfg.get(MOB, this.spawn_mob_name, this.spawn_mob, this.spawn_mob_come);
			Property look_render = cfg.get(RENDER, this.render_name, this.isRender, this.render_come);
			Property look_renderSide = cfg.get(RENDER, this.renderSide_name, this.isLeftSide, this.renderSide_come);
			Property look_biome = cfg.get(BIOME, this.biome_name, this.genBiome, this.biome_come);
			Property look_FLW = cfg.get(STRUCTURE_BOOL, this.flower_name, this.genFlowers, this.flower_come);
			Property look_dim = cfg.get(DIM, this.dim_name, this.dimId, this.dim_come);
			Property look_hard = cfg.get(DIFFICUL, this.hard_name, this.isHard, this.hard_come);
			Property look_dmrate = cfg.get(DIFFICUL, this.damageRate_name, this.damageRate, this.damageRate_come);
			Property look_maxlevel = cfg.get(DIFFICUL, this.maxLevel_name, this.maxLevel, this.maxLevel_come);

			// look_○○　で　読み込んだ変数をStatic参照できる変数に代入
			this.help_recipe = look_hrcp.getBoolean();
			this.help_knifedrop = look_knife.getBoolean();
			this.mobdrop_crystal = look_mdropC.getBoolean();
			this.isGenStructure = look_spawn_house.getBoolean();
			this.spawn_mob = look_spawn_mob.getBoolean();
			this.isRender = look_render.getBoolean();
			this.isLeftSide = look_renderSide.getBoolean();
			this.genBiome = look_biome.getBoolean();
			this.genFlowers = look_FLW.getBoolean();
			this.dimId = look_dim.getInt();
			this.isHard = look_hard.getBoolean();
			this.damageRate = look_dmrate.getDouble();
			this.maxLevel = look_maxlevel.getInt();

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
