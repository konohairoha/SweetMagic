package sweetmagic.event;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.entity.projectile.EntityMagicItem;

public class EntityItemTossEvent {

	@SubscribeEvent
	public void onEvent(ItemTossEvent event) {

		// エンティティ確認
		EntityItem entity = event.getEntityItem();
		if (entity == null || entity instanceof EntityMagicItem) { return; }

		// MF関連のアイテム以外なら終了
		ItemStack stack = entity.getItem();
		if (!(stack.getItem() instanceof IMFTool)) { return; }

		// エンチャレベルチェック
		int level = EnchantmentHelper.getEnchantmentLevel(EnchantInit.aetherCharm, stack);
		if (level <= 0) { return; }

		// エンティティの置き換え
		World world = entity.world;
		EntityMagicItem item = this.getEntity(world, event.getPlayer(), stack);
		item.setNoDespawn();
		entity.setDead();
		event.setCanceled(true);

		if (!world.isRemote) {
			world.spawnEntity(item);
		}
	}

	// エンティティの取得
	public EntityMagicItem getEntity (World world, EntityPlayer player, ItemStack stack) {

		double d0 = player.posY - 0.30000001192092896D + (double) player.getEyeHeight();
		EntityMagicItem entity = new EntityMagicItem(world, player.posX, d0, player.posZ, stack);
		entity.setPickupDelay(40);
		entity.setThrower(player.getName());

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

        return entity;
	}
}
