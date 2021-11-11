package sweetmagic.event;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.inventory.InventoryPouch;

public class XPPickupEvent {

	private static final EntityEquipmentSlot[] ARMORSLOT = new EntityEquipmentSlot[] {
		EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET
	};

	// 経験値取得イベント
	@SubscribeEvent
	public void onBulletRenderEvent(PlayerPickupXpEvent event) {

		// プレイヤーじゃなかったら
		EntityPlayer player = event.getEntityPlayer();
		if (player == null ) { return; }

		EntityXPOrb xp = event.getOrb();

		// ローブの修繕イベント呼び出し
		this.armorRepair(player, xp, xp.getXpValue());
	}

	// ローブの修繕イベント
	public void armorRepair (EntityPlayer player, EntityXPOrb xp, int value) {

		for (EntityEquipmentSlot slot : ARMORSLOT) {

			// 防具の取得
			ItemStack stack = player.getItemStackFromSlot(slot);
			if (stack.isEmpty() || !this.checkEncha(stack)) { continue; }

			Item item = stack.getItem();

			// MFToolなら
			if (item instanceof IMFTool) {
				if(!this.healRepair(player, stack, value)) { continue; }
			}

			// ポーチなら
			else if (item instanceof IPouch) {
				this.emelaldPiasEffect(player, stack, xp, value);
			}

			// 耐久値があるなら
			else if (stack.getMaxDamage() > 0 && stack.getItemDamage() > 0) {
				this.repairTool(player, stack, value);
			}
		}
	}

	// 経験値増加
	public void emelaldPiasEffect (EntityPlayer player, ItemStack stack, EntityXPOrb xp, int value) {

		ItemStack leg = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		if (!(leg.getItem() instanceof IPouch)) { return; }

		// インベントリを取得
		InventoryPouch neo = new InventoryPouch(player);
		IItemHandlerModifiable inv = neo.inventory;
		float addXP = 0;

		// インベントリの分だけ回す
		for (int i = 0; i < inv.getSlots(); i++) {

			// アイテムを取得し空かアクセサリー以外なら次へ
			ItemStack st = inv.getStackInSlot(i);
			if (st.isEmpty() || !(st.getItem() instanceof IAcce)) { continue; }

			Item item = st.getItem();
			IAcce acce = (IAcce) item;

			// エメラルドピアスを持ってるなら経験値増加
			if (item == ItemInit.emelald_pias) {
				addXP += value * 0.25F;

				// 重複不可なら終了
				if (!acce.isDuplication()) { break; }
			}
		}

		xp.xpValue += addXP;
	}

	public boolean healRepair (EntityPlayer player, ItemStack stack, int value) {

		// MF回復エンチャがついていなかったら終了
		IMFTool wand = (IMFTool) stack.getItem();
		int level = this.getEnchantLevel(EnchantInit.mfRecover, stack);

		// エンチャが0以下かMFが最大値なら終了
		if (level <= 0 || wand.isMaxMF(stack)) { return false; }

		// エンチャレベル分増やす
		value = value >= 4 ? value / 3 : value;
		value *= level;

		// 取得した経験値分MFを増やす
		wand.insetMF(stack, value);

		return true;
	}

	public void repairTool (EntityPlayer player, ItemStack stack, int value) {

		// エンチャレベルの取得
		int level = this.getEnchantLevel(EnchantInit.mfRecover, stack);

		// エンチャレベル分増やす
		value = ( value >= 4 ? value / 3 : value ) * level;
		stack.setItemDamage(stack.getItemDamage() - value);

	}

	// エンチャレベル取得
	public int getEnchantLevel (Enchantment enchant, ItemStack stack) {
		return Math.min(EnchantmentHelper.getEnchantmentLevel(enchant, stack), 10);
	}

	// エンチャがついてないか確認
	public boolean checkEncha (ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(EnchantInit.mfRecover, stack) > 0;
	}
}
