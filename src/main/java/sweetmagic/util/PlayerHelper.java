package sweetmagic.util;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.inventory.InventoryPouch;

public class PlayerHelper {

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
		entiy.removePotionEffect(potion);
		entiy.addPotionEffect(new PotionEffect(potion, time, level));
	}

	// ポーション付加
	public static void addPotion (EntityLivingBase entity, Potion potion, int time, int level, boolean notDamage) {

		// プレイヤーかつバフなら
		if (entity instanceof EntityPlayer && !potion.isBadEffect() && notDamage) {
			time = addBuffTime((EntityPlayer) entity, time);
		}

		entity.removePotionEffect(potion);
		entity.addPotionEffect(new PotionEffect(potion, time, level, true, true));
	}

	// 追加するバフ時間
	public static int addBuffTime (EntityPlayer entity, int time) {

		// 防具の取得
		ItemStack stack = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		if (stack.isEmpty() || !(stack.getItem() instanceof IPouch) ) { return time; }

		float addTime = 1F;

		// インベントリを取得
		InventoryPouch neo = new InventoryPouch(entity);
		List<ItemStack> stackList = neo.getStackList();

		// インベントリの分だけ回す
		for (ItemStack st : stackList) {

			// ペンデュラムネックレスなら
			if (st.getItem() == ItemInit.pendulum_necklace) {
				addTime += 0.175F;
			}
		}

		return (int) (time * addTime);
	}

    public static void addExp (EntityPlayer player, int amount) {
        final int exp = getExpValue(player) + amount;
        player.experienceTotal = exp;
        player.experienceLevel = getLevelForExp(exp);
        final int expForLevel = getExpForLevel(player.experienceLevel);
        player.experience = (float) (exp - expForLevel) / (float) player.xpBarCap();
    }

    public static int getExpValue (EntityPlayer player) {
        return (int) (getExpForLevel(player.experienceLevel) + player.experience * player.xpBarCap());
    }

    public static int getLevelForExp (int exp) {

        int level = 0;

        while (getExpForLevel(level) <= exp) {
            level++;
        }

        return level - 1;
    }

    public static int getExpForLevel (int level) {

        if (level == 0) { return 0; }

        if (level > 0 && level < 17) {
            return level * level + 6 * level;
        }

        else if (level > 16 && level < 32) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        }

        return (int) (4.5 * level * level - 162.5 * level + 2220);
    }

    public static ItemStack getLeg (EntityPlayer player) {
    	return player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
    }

    public static Item getLegItem (EntityPlayer player) {
    	return getLeg(player).getItem();
    }
}
