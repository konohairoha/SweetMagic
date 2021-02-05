package sweetmagic.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PlayerHelper {

	// クリエイティブかどうか
	public static boolean isCleative (EntityPlayer player) {
		return player.capabilities.isCreativeMode;
	}

	// プレイヤーかどうか
	public static boolean isPlayer (EntityLivingBase entity) {
		return entity != null && entity instanceof EntityPlayer;
	}

	// EntityBaseMagicShot用
	public static boolean isThowerPlayer (EntityLivingBase attecker, EntityLivingBase target) {
		return isPlayer(attecker) && isPlayer(target);
	}

	// EntityBaseMagicShot用
	public static boolean isNotThowerPlayer (EntityLivingBase attecker, EntityLivingBase target) {
		return !isPlayer(attecker) && !isPlayer(target);
	}

	// ポーション付加
	public static void addPotion (EntityLivingBase entiy, Potion potion, int time, int level) {
		entiy.addPotionEffect(new PotionEffect(potion, time, level));
	}

	// ポーション付加
	public static void addPotion (EntityLivingBase entiy, Potion potion, int time, int level, boolean flag) {
		entiy.addPotionEffect(new PotionEffect(potion, time, level, true, false));
	}
}
