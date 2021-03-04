package sweetmagic.event;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.api.iitem.IRobe;
import sweetmagic.api.iitem.ISMArmor;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.inventory.InventoryPouch;

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
		slotList.add(EntityEquipmentSlot.CHEST);
		slotList.add(EntityEquipmentSlot.LEGS);

		for (EntityEquipmentSlot slot : slotList) {

			// 防具の取得
			ItemStack stack = player.getItemStackFromSlot(slot);
			if (stack.isEmpty() || !(stack.getItem() instanceof ISMArmor)) { continue; }

			Item item = stack.getItem();

			// ローブなら修繕処理呼び出し
			if (item instanceof IRobe) {
				if(!this.robeRegene(player, stack, value)) { continue; }
			}

			// ポーチなら
			else if (item instanceof IPouch) {
				this.emelaldPiasEffect(player, stack, value);
			}
		}
	}

	// ローブ修繕
	public boolean robeRegene (EntityPlayer player, ItemStack stack, int value) {

		// エンチャレベルの取得
		ISMArmor robe = (ISMArmor) stack.getItem();
		int level = robe.getEnchantLevel(EnchantInit.mfRecover, stack);

		// エンチャが0以下かMFが最大値なら終了
		if (level <= 0 || stack.getItemDamage() == 0) { return false; }

		// エンチャレベル分増やす
		value = value >= 4 ? value / 3 : value;
		value *= level;

		// ダメージ回復
		stack.setItemDamage(stack.getItemDamage() - value);

		return true;
	}

	// 経験値増加
	public void emelaldPiasEffect (EntityPlayer player, ItemStack stack, int value) {

		ItemStack leg = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		if (!(leg.getItem() instanceof IPouch)) { return; }

		// インベントリを取得
		InventoryPouch neo = new InventoryPouch(player);
		IItemHandlerModifiable inv = neo.inventory;

		// インベントリの分だけ回す
		for (int i = 0; i < inv.getSlots(); i++) {

			// アイテムを取得し空かアクセサリー以外なら次へ
			ItemStack st = inv.getStackInSlot(i);
			if (st.isEmpty() || !(st.getItem() instanceof IAcce)) { continue; }

			Item item = st.getItem();
			IAcce acce = (IAcce) item;

			// エメラルドピアスを持ってるなら経験値増加
			if (item == ItemInit.emelald_pias) {
				player.experience += value * 0.01F;

				// 重複不可なら終了
				if (!acce.isDuplication()) { return; }
			}
		}
	}
}
