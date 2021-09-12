package sweetmagic.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.items.IItemHandlerModifiable;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.inventory.InventoryPouch;

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

		// インベントリを取得
		InventoryPouch neo = new InventoryPouch(entity);
		IItemHandlerModifiable inv = neo.inventory;
		float addTime = 1F;

		// インベントリの分だけ回す
		for (int i = 0; i < inv.getSlots(); i++) {

			// アイテムを取得し空かアクセサリー以外なら次へ
			ItemStack st = inv.getStackInSlot(i);
			if (st.isEmpty() || !(st.getItem() instanceof IAcce)) { continue; }

			// ペンデュラムネックレスなら
			if (st.getItem() == ItemInit.pendulum_necklace) {
				addTime += 0.1F;
			}
		}

		return (int) (time * addTime);
	}

    public static void addExperience (EntityPlayer player, int amount) {
        final int experience = getExperience(player) + amount;
        player.experienceTotal = experience;
        player.experienceLevel = getLevelForExperience(experience);
        final int expForLevel = getExperienceForLevels(player.experienceLevel);
        player.experience = (float) (experience - expForLevel) / (float) player.xpBarCap();
    }

    public static int getExperience (EntityPlayer player) {
        return (int) (getExperienceForLevels(player.experienceLevel) + player.experience * player.xpBarCap());
    }

    public static int getLevelForExperience (int experience) {

        int level = 0;

        while (getExperienceForLevels(level) <= experience) {
            level++;
        }

        return level - 1;
    }

    public static int getExperienceForLevels (int level) {

        if (level == 0) { return 0; }

        if (level > 0 && level < 17) {
            return level * level + 6 * level;
        }

        else if (level > 16 && level < 32) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        }

        return (int) (4.5 * level * level - 162.5 * level + 2220);
    }
}
