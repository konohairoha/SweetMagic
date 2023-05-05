package sweetmagic.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import sweetmagic.init.potion.PotionCyclone;
import sweetmagic.init.potion.PotionFrost;
import sweetmagic.init.potion.PotionResitanceBlow;
import sweetmagic.init.potion.PotionSM;

public class PotionInit {

	public static Potion frosty, flame;
	public static Potion gravity, gravity_accele;
	public static Potion cyclone;
	public static Potion shadow;
	public static Potion aether_barrier;
	public static Potion deadly_poison, grant_poison;
	public static Potion slow, timestop;
	public static Potion electric_armor;
	public static Potion refresh_effect;
	public static Potion babule, regene;
	public static Potion breakblock;
	public static Potion mf_down;
	public static Potion mind_control;
	public static Potion resistance_blow;
	public static Potion aether_shield;
	public static Potion magic_array;
	public static Potion holly_charm;
	public static Potion wind_relief;
	public static Potion causality_prediction;
	public static Potion glider;
	public static Potion increased_cool_time;
	public static Potion range_glove;
	public static Potion prompt_feather;
	public static Potion armor_break;
	public static Potion drop_increase;

	public static List<Potion> buffList = new ArrayList<>();		// バフリスト
	public static List<Potion> deBuffList = new ArrayList<>();		// デバフリスト

	public static void init() {

		frosty = new PotionFrost(true, 0, "frosted", "textures/items/magic_frost.png");
		gravity = new PotionSM(true, 0, "gravity", "textures/items/grav_powder.png", true);
		gravity_accele = new PotionSM(false, 0, "gravity_accele", "textures/items/magic_gravity.png", true);
		cyclone = new PotionCyclone(false, 0, "cyclon", "textures/items/magic_avoid.png");
		shadow = new PotionSM(false, 0, "shadow", "textures/items/magic_shadow.png", false);
		aether_barrier = new PotionSM(false, 0, "aether_barrier", "textures/items/magic_barrier.png", false);
		deadly_poison = new PotionSM(true, 0, "deadly_poison", "textures/items/poison_bottle.png", false);
		grant_poison = new PotionSM(false, 0, "grant_poison", "textures/items/magic_poison.png", false);
		slow = new PotionSM(true, 0, "slow", "textures/items/sannyflower_petal.png", true);
		timestop = new PotionSM(false, 0, "timestop", "textures/items/magic_slowtime.png", false);
		electric_armor = new PotionSM(false, 0, "electric_armor", "textures/items/magic_thunder.png", false);
		refresh_effect = new PotionSM(false, 0, "refresh_effect", "textures/items/magic_effectremover.png", true);
		flame = new PotionSM(true, 0, "flame", "textures/items/magic_fire.png", false);
		babule = new PotionSM(true, 0, "babule", "textures/particle/magic_bubble.png", true);
		regene = new PotionSM(false, 0, "regene", "textures/items/magic_heal.png", true);
		breakblock = new PotionSM(false, 0, "breakblock", "textures/items/magic_dig.png", true);
		mf_down = new PotionSM(false, 0, "mf_down", "textures/items/mystical_page.png", true);
		mind_control = new PotionSM(false, 0, "mind_control", "textures/items/stray_soul.png", true);
		resistance_blow = new PotionResitanceBlow(false, 0, "resistance_blow", "textures/items/magic_heal.png");
		aether_shield = new PotionSM(false, 0, "aether_shield", "textures/items/magic_aether_shield.png", false);
		magic_array = new PotionSM(false, 0, "magic_array", "textures/block/mahoujin.png", false);
		holly_charm = new PotionSM(false, 0, "holly_charm", "textures/items/holly_charm.png", false);
		wind_relief = new PotionSM(false, 0, "wind_relief", "textures/items/wind_relief.png", false);
		causality_prediction = new PotionSM(false, 0, "causality_prediction", "textures/items/magician_quillpen.png", false);
		glider = new PotionSM(false, 0, "glider", "textures/entity/angel_wing.png", true);
		increased_cool_time = new PotionSM(false, 0, "increased_cool_time", "textures/items/pendulum_necklace.png", false);
		range_glove = new PotionSM(false, 0, "range_glove", "textures/items/range_glove.png", false);
		prompt_feather = new PotionSM(false, 0, "prompt_feather", "textures/items/prompt_feather.png", false);
		armor_break = new PotionSM(false, 0, "armor_break", "textures/items/magicians_robe.png", false);
		drop_increase = new PotionSM(false, 0, "drop_increase", "textures/items/accebag.png", false);
	}

	// リストを設定
	public static void setList () {

		buffList.add(cyclone);
		buffList.add(gravity_accele);
		buffList.add(shadow);
		buffList.add(aether_barrier);
		buffList.add(grant_poison);
		buffList.add(electric_armor);
		buffList.add(refresh_effect);
		buffList.add(regene);
		buffList.add(mf_down);
		buffList.add(resistance_blow);
		buffList.add(aether_shield);
		buffList.add(magic_array);
		buffList.add(holly_charm);
		buffList.add(wind_relief);
		buffList.add(causality_prediction);
		buffList.add(glider);
		buffList.add(range_glove);
		buffList.add(prompt_feather);
		buffList.add(drop_increase);
		buffList.add(MobEffects.SPEED);
		buffList.add(MobEffects.STRENGTH);
		buffList.add(MobEffects.INSTANT_HEALTH);
		buffList.add(MobEffects.JUMP_BOOST);
		buffList.add(MobEffects.REGENERATION);
		buffList.add(MobEffects.RESISTANCE);
		buffList.add(MobEffects.FIRE_RESISTANCE);
		buffList.add(MobEffects.WATER_BREATHING);
		buffList.add(MobEffects.INVISIBILITY);
		buffList.add(MobEffects.NIGHT_VISION);
		buffList.add(MobEffects.HEALTH_BOOST);
		buffList.add(MobEffects.GLOWING);
		buffList.add(MobEffects.LUCK);
		buffList.add(MobEffects.ABSORPTION);
		buffList.add(MobEffects.SATURATION);

		deBuffList.add(flame);
		deBuffList.add(frosty);
		deBuffList.add(gravity);
		deBuffList.add(deadly_poison);
		deBuffList.add(slow);
		deBuffList.add(timestop);
		deBuffList.add(babule);
		deBuffList.add(mind_control);
		deBuffList.add(armor_break);
		deBuffList.add(MobEffects.HUNGER);
		deBuffList.add(MobEffects.POISON);
		deBuffList.add(MobEffects.WITHER);
		deBuffList.add(MobEffects.SLOWNESS);
		deBuffList.add(MobEffects.MINING_FATIGUE);
		deBuffList.add(MobEffects.NAUSEA);
		deBuffList.add(MobEffects.BLINDNESS);
		deBuffList.add(MobEffects.WEAKNESS);
		deBuffList.add(MobEffects.HASTE);
		deBuffList.add(MobEffects.INSTANT_DAMAGE);
		deBuffList.add(MobEffects.UNLUCK);
	}

	// バフリストの取得
	public static List<Potion> getBuffPotionList () {
		return PotionInit.buffList;
	}

	// デバフリストの取得
	public static List<Potion> getDeBuffPotionList () {
		return PotionInit.deBuffList;
	}
}
