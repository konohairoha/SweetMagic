package sweetmagic.event;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.api.iitem.ISMArmor;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.EnchantInit;

public class XPPickupEvent {

	// 経験値取得イベント
	@SubscribeEvent
	public void onBulletRenderEvent(PlayerPickupXpEvent event) {

		// プレイヤーじゃなかったら
		if (event.getEntityPlayer() == null || !(event.getEntityPlayer() instanceof EntityPlayer)) { return; }

		// ItemStackの取得
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = player.getHeldItemMainhand();
		int value = event.getOrb().getXpValue();

		// 杖の修繕イベント呼び出し
		this.wandEvent(stack, value);

		// ローブの修繕イベント呼び出し
		this.robeEvent(player, value);

	}

	// 杖の修繕イベント
	public void wandEvent (ItemStack stack, int value) {

		// 杖じゃなかったら終了
		if (stack.isEmpty() || !(stack.getItem() instanceof IWand)) { return; }

		// MF回復エンチャがついていなかったら終了
		IWand wand = IWand.getWand(stack);
		int level = wand.getEnchantLevel(EnchantInit.mfRecover, stack);

		// エンチャが0以下かMFが最大値なら終了
		if (level <= 0 || wand.isMaxMF(stack)) { return; }

		// エンチャレベル分増やす
		value = value >= 4 ? value / 3 : value;
		value *= level;

		// 取得した経験値分MFを増やす
		wand.insetMF(stack, value);
	}

	// ローブの修繕イベント
	public void robeEvent (EntityPlayer player, int value) {

		List<EntityEquipmentSlot> slotList = new ArrayList<>();
//		slotList.add(EntityEquipmentSlot.HEAD);
		slotList.add(EntityEquipmentSlot.CHEST);
//		slotList.add(EntityEquipmentSlot.FEET);
//		slotList.add(EntityEquipmentSlot.LEGS);

		for (EntityEquipmentSlot slot : slotList) {

			// 防具の取得
			ItemStack stack = player.getItemStackFromSlot(slot);
			if (stack.isEmpty() || !(stack.getItem() instanceof ISMArmor)) { continue; }

			// エンチャレベルの取得
			ISMArmor robe = (ISMArmor) stack.getItem();
			int level = robe.getEnchantLevel(EnchantInit.mfRecover, stack);

			// エンチャが0以下かMFが最大値なら終了
			if (level <= 0 || stack.getItemDamage() == 0) { continue; }

			// エンチャレベル分増やす
			value = value >= 4 ? value / 3 : value;
			value *= level;

			// ダメージ回復
			stack.setItemDamage(stack.getItemDamage() - value);
		}
	}
}
