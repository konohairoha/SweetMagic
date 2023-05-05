package sweetmagic.event;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.projectile.EntityMagicItem;
import sweetmagic.init.item.sm.sweetmagic.SMBookCosmic;
import sweetmagic.init.tile.inventory.InventoryHarvestBag;
import sweetmagic.init.tile.slot.SlotPredicates;

public class EntityItemTossEvent {

	public static final List<Item> cosmicList = Arrays.<Item> asList(
		ItemInit.aether_crystal_shard, ItemInit.aether_crystal
	);

	public static final List<Item> scarletList = Arrays.<Item> asList(
		ItemInit.aether_crystal_shard, ItemInit.aether_crystal, ItemInit.divine_crystal, ItemInit.pure_crystal
	);

	@SubscribeEvent
	public void onEvent(ItemTossEvent event) {

		// エンティティ確認
		EntityItem entity = event.getEntityItem();
		if (entity == null || entity instanceof EntityMagicItem) { return; }

		// エンチャ付いてないなら終了
		ItemStack stack = entity.getItem();
		if (!(this.checkEncha(stack))) { return; }

		// エンティティの置き換え
		World world = entity.world;
		EntityMagicItem item = this.getEntity(world, event.getPlayer(), stack);
		entity.setDead();
		event.setCanceled(true);

		if (!world.isRemote) {
			world.spawnEntity(item);
		}
	}

	// エンティティの取得
	public static EntityMagicItem getEntity (World world, EntityPlayer player, ItemStack stack) {

		double d0 = player.posY - 0.30000001192092896D + (double) player.getEyeHeight();
		EntityMagicItem entity = new EntityMagicItem(world, player.posX, d0, player.posZ, stack);
		entity.setPickupDelay(40);
		entity.setThrower(player.getName());
//		entity.not

        float f2 = 0.3F;
        Random rand = world.rand;
		entity.motionX = (double) (-MathHelper.sin(player.rotationYaw * 0.017453292F) * MathHelper.cos(player.rotationPitch * 0.017453292F) * f2);
		entity.motionZ = (double) (MathHelper.cos(player.rotationYaw * 0.017453292F) * MathHelper.cos(player.rotationPitch * 0.017453292F) * f2);
		entity.motionY = (double) (-MathHelper.sin(player.rotationPitch * 0.017453292F) * f2 + 0.1F);
		float f3 = rand.nextFloat() * ((float) Math.PI * 2F);
		f2 = 0.02F * rand.nextFloat();
		entity.motionX += Math.cos((double) f3) * (double) f2;
		entity.motionY += (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F);
		entity.motionZ += Math.sin((double) f3) * (double) f2;
		entity.setNoDespawn();

        return entity;
	}

	// エンチャがついてないか確認
	public boolean checkEncha (ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(EnchantInit.aetherCharm, stack) > 0;
	}

	@SubscribeEvent
	public void onEntityItemPickupEvent(EntityItemPickupEvent event) {

		// えんちちーの取得してしてるなら終了
		EntityItem entity = event.getItem();
		if (entity.isDead || entity instanceof EntityMagicItem) { return; }

		// エーテルクリスタルかエーテルクリスタルシャード以外なら終了
		ItemStack stack = entity.getItem();
		Item item = stack.getItem();
		if (!scarletList.contains(item) && !SlotPredicates.isSeed(stack) && !SlotPredicates.isSapling(stack)) { return; }

		boolean isCrystal = scarletList.contains(item);
		boolean isSeed = SlotPredicates.isSeed(stack);
		boolean isSapling = SlotPredicates.isSapling(stack);

		// プレイヤー取得
		EntityPlayer player = event.getEntityPlayer();
		NonNullList<ItemStack> pInv = player.inventory.mainInventory;

		// インベントリの数だけ回す
		for (int i = 0; i < pInv.size(); i++) {

			// アイテムスタックを取得し空なら終了
			ItemStack invStack = pInv.get(i);
			if (invStack.isEmpty()) { continue; }

			Item invItem = invStack.getItem();

			if (isCrystal && invStack.getItem() instanceof SMBookCosmic) {

				// アイテムを拾う設定をしてないなら次へ
				SMBookCosmic book = (SMBookCosmic) invItem;
				if (!book.isPickUp(invStack) || book.isMaxMF(invStack) || !book.checkCrystal(item)) { continue; }

				book.insetMF(invStack, book.getItemMF(stack));
				entity.setDead();
		        event.setCanceled(true);
				player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.33F, 1.7F);
		        return;
			}

			else if ( (isSeed && invItem == ItemInit.seed_harvest_bag) ||
						(isSapling && invItem == ItemInit.sapling_harvest_bag)) {

				// インベントリを取得
				InventoryHarvestBag bag = new InventoryHarvestBag(player, invStack);

				for (int k = 0; k < bag.getSlots(); k++) {

					// クラネオインベントリ内のアイテムを取得
					ItemStack st = bag.insertItem(k, stack.copy(), true);
					if (!st.isEmpty()) { continue; }

					bag.insertItem(k, stack.copy(), false);
					bag.writeBack();
					entity.setDead();
			        event.setCanceled(true);
					player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.33F, 1.7F);

					return;
				}
			}
		}
	}
}
