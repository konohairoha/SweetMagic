package sweetmagic.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import sweetmagic.init.potion.PotionFrost;
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

	public static List<Potion> buffList = new ArrayList<>();		// バフリスト
	public static List<Potion> deBuffList = new ArrayList<>();	// デバフリスト

	public static void init() {

		frosty = new PotionFrost(false, 0x56CBFD, "frosted", "textures/items/magic_frost.png");
		gravity = new PotionSM(false, 0x56CBFD, "gravity", "textures/items/grav_powder.png", true);
		gravity_accele = new PotionSM(false, 0x56CBFD, "gravity_accele", "textures/items/magic_gravity.png", true);
		cyclone = new PotionSM(false, 0x56CBFD, "cyclon", "textures/items/magic_cyclon.png", true);
		shadow = new PotionSM(false, 0x56CBFD, "shadow", "textures/items/magic_shadow.png", false);
		aether_barrier = new PotionSM(false, 0x56CBFD, "aether_barrier", "textures/items/magic_barrier.png", false);
		deadly_poison = new PotionSM(false, 0x56CBFD, "deadly_poison", "textures/items/poison_bottle.png", false);
		grant_poison = new PotionSM(false, 0x56CBFD, "grant_poison", "textures/items/magic_poison.png", false);
		slow = new PotionSM(false, 0x56CBFD, "slow", "textures/items/sannyflower_petal.png", true);
		timestop = new PotionSM(false, 0x56CBFD, "timestop", "textures/items/magic_slowtime.png", false);
		electric_armor = new PotionSM(false, 0x56CBFD, "electric_armor", "textures/items/magic_thunder.png", false);
		refresh_effect = new PotionSM(false, 0x56CBFD, "refresh_effect", "textures/items/magic_effectremover.png", true);
		flame = new PotionSM(false, 0x56CBFD, "flame", "textures/items/magic_fire.png", false);
		babule = new PotionSM(false, 0x56CBFD, "babule", "textures/items/magic_babule.png", true);
		regene = new PotionSM(false, 0x56CBFD, "regene", "textures/items/magic_heal.png", true);
		breakblock = new PotionSM(false, 0x56CBFD, "breakblock", "textures/items/magic_dig.png", true);
	}

	// リストを設定
	public static void setList () {

		buffList.add(cyclone);
		buffList.add(gravity_accele);
		buffList.add(shadow);
		buffList.add(aether_barrier);
		buffList.add(grant_poison);
		buffList.add(electric_armor);
		buffList.add(regene);
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
