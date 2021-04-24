package sweetmagic.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.PotionInit;
import sweetmagic.util.ParticleHelper;

public class LivingDethEvent {

	public final Map<UUID, InventoryPlayer> playerKeepsMap = new HashMap<>();

	// 死亡時
	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {

		if (event.getEntityLiving() == null) { return; }

		EntityLivingBase living = event.getEntityLiving();
		boolean isCanel = false;

		// ポーションがアクティブじゃないなら
		if (living.isPotionActive(PotionInit.refresh_effect)) {
			isCanel = this.refreshEffect(event, living);
		}

		// 死亡キャンセルしてないなら
		if (!isCanel && living instanceof EntityPlayer) {
			this.keepInv(event, living);
		}
	}

	// リフレッシュエフェクト
	public boolean refreshEffect (LivingDeathEvent event, EntityLivingBase living) {

		// ポーションレベルが0なら終了
		int power = living.getActivePotionEffect(PotionInit.refresh_effect).getAmplifier();
		if (power == 0) { return false; }

		// 死亡キャンセル
		event.setCanceled(true);

		if (living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;
			if (player.capabilities.isCreativeMode && player.getName().equals("Konohairoha")) {
				living.setHealth(living.getMaxHealth());
				return true;
			}
		}

		living.setHealth(living.getMaxHealth() / 2);
		living.removePotionEffect(PotionInit.refresh_effect);

		// パーティクルスポーン
		World world = living.world;
		ParticleHelper.spawnHeal(living, EnumParticleTypes.VILLAGER_HAPPY, 16, 1, 4);
		ParticleHelper.spawnHeal(living, EnumParticleTypes.FIREWORKS_SPARK, 64, world.rand.nextGaussian() * 0.1, 4);
		world.playSound(null, new BlockPos(living), SoundEvents.ITEM_TOTEM_USE, SoundCategory.NEUTRAL, 0.25F, 1F);
		return true;
	}

	// インベントリをキープ
	public boolean keepInv (LivingDeathEvent event, EntityLivingBase living) {

		EntityPlayer player = (EntityPlayer) living;
		InventoryPlayer pInv = player.inventory;
		int mainInv = pInv.mainInventory.size();
		InventoryPlayer keepInv= new InventoryPlayer(null);
		UUID pID = player.getUniqueID();
		boolean isKeep = false;
		NonNullList<ItemStack> armorInv = pInv.armorInventory;
		NonNullList<ItemStack> offHandInv = pInv.offHandInventory;

		// プレイヤーのインベントリ分回す
		for (int inv = 0; inv < mainInv; inv++) {

			// エーテルチャームが付いてないなら次へ
			ItemStack stack = pInv.getStackInSlot(inv);
			if (!this.checkEnchant(stack)) { continue; }

			// コピーをインベントに入れて本物を消す
			keepInv.mainInventory.set(inv, stack.copy());
			pInv.mainInventory.set(inv, ItemStack.EMPTY);
			isKeep = true;
		}

		// アーマー分回す
		for (int i = 0; i < armorInv.size(); i++) {

			// エーテルチャームが付いてないなら次へ
			ItemStack stack = armorInv.get(i);
			if (!this.checkEnchant(stack)) { continue; }

			// keepInvに入れる
			keepInv.armorInventory.set(i, stack.copy());
			armorInv.set(i, ItemStack.EMPTY);
			isKeep = true;
		}

		// オフハンド分回す
		for (int i = 0; i < offHandInv.size(); i++) {

			// エーテルチャームが付いてないなら次へ
			ItemStack stack = offHandInv.get(i);
			if (!this.checkEnchant(stack)) { continue; }

			keepInv.offHandInventory.set(i, stack.copy());
			offHandInv.set(i, ItemStack.EMPTY);
			isKeep = true;
		}

		// 突っ込んだ情報をmapに入れる
		if (isKeep) {
			playerKeepsMap.put(pID, keepInv);
		}

		return isKeep;
	}

	// リスポーン時
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {

		EntityPlayer player = event.player;
		InventoryPlayer keepInv = playerKeepsMap.remove(player.getUniqueID());
		if (keepInv  == null) { return; }

		InventoryPlayer pInv = player.inventory;
		NonNullList<ItemStack> displaced = NonNullList.create();
		NonNullList<ItemStack> mainInv = pInv.mainInventory;
		NonNullList<ItemStack> offInv = pInv.offHandInventory;
		NonNullList<ItemStack> armorInv = pInv.armorInventory;

		// アーマー
		for (int i = 0; i < armorInv.size(); i++) {

			ItemStack stack = keepInv.armorInventory.get(i);
			if (stack.isEmpty()) { continue; }

			ItemStack armor = armorInv.set(i, stack);
			if (!armor.isEmpty()) { continue; }

			displaced.add(armor);
		}

		// オフハンド
		for (int i = 0; i < offInv.size(); i++){

			ItemStack stack = keepInv.offHandInventory.get(i);
			if (stack.isEmpty()) { continue; }

			ItemStack off = offInv.set(i, stack);
			if (!off.isEmpty()) { continue; }

			displaced.add(off);
		}

		// メインハンド
		for (int i = 0; i < mainInv.size(); i++) {

			ItemStack stack = keepInv.mainInventory.get(i);
			if (stack.isEmpty()) { continue; }

			ItemStack main = mainInv.set(i, stack);
			if (!main.isEmpty()) { continue; }

			displaced.add(main);
		}
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event) {

		EntityPlayer player = event.player;
		InventoryPlayer keepInv = playerKeepsMap.remove(player.getUniqueID());
		if (keepInv == null) {  return; }

		keepInv.player = player;
		keepInv.dropAllItems();
	}

	// エンチャントチェック
	public boolean checkEnchant (ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(EnchantInit.aetherCharm, stack) > 0;
	}
}
