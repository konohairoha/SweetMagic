package sweetmagic.event;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.SweetMagicCore;
import sweetmagic.config.SMConfig;
import sweetmagic.init.PotionInit;

public class LivingPotionEvent {

	// 落下イベント
	@SubscribeEvent
	public void livingEvent (LivingFallEvent event) {

		// 飛躍上昇なら
		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.isPotionActive(MobEffects.JUMP_BOOST)) { return; }

		entity.fallDistance = 0;
	}

	// テレポートイベント
	@SubscribeEvent
	public void teleportEvent(EnderTeleportEvent event) {

		// 重力、泡状態以外なら終了
		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.isPotionActive(PotionInit.gravity) && !entity.isPotionActive(PotionInit.babule)) { return; }

		event.setCanceled(true);
	}

	@SubscribeEvent
	public void onEvent(PotionAddedEvent event) {

		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.isPotionActive(PotionInit.refresh_effect)) { return; }

		Potion potion = event.getPotionEffect().getPotion();
		if (!potion.isBadEffect()) { return; }

		entity.removePotionEffect(potion);
	}

	@SubscribeEvent
	public void onEvent(LivingEvent.LivingUpdateEvent event) {

		EntityLivingBase entity = event.getEntityLiving();
		if(entity == null || !(entity instanceof EntityPlayer)  ||
				!entity.isPotionActive(PotionInit.glider) || entity.isPotionActive(PotionInit.breakblock)) { return; }

		entity.fallDistance = 0;

		if (SweetMagicCore.proxy.isJumpPressed()) {
			entity.motionY += 0.05F;
		}

		if (entity.onGround) { return; }

		if (entity.motionY <= 0) {
			entity.motionY *= entity.isSneaking() ? 0.975F : 0.6F;
		}

		if (entity.world.isRemote && entity.ticksExisted % 5 == 0) {

			NBTTagCompound tags = entity.getEntityData();
			float lastRotYaw = tags.hasKey("lastRotYaw") ? tags.getFloat("lastRotYaw") : 0F;

			if (lastRotYaw != 0 && (lastRotYaw > entity.prevRenderYawOffset + 5F || lastRotYaw + 5F < entity.prevRenderYawOffset) ) {
				float down = 1F - Math.min(1F, ( 1.375F * Math.abs(lastRotYaw - entity.prevRenderYawOffset) / 180F));
				entity.motionX *= down;
				entity.motionZ *= down;
			}

			tags.setFloat("lastRotYaw", entity.prevRenderYawOffset);
		}

		if (entity.moveForward > 0) {

			if (Math.abs(entity.motionX) <= 2.25F && Math.abs(entity.motionZ) <= 2.25F) {
				float moXZ = 1.125F;
				entity.motionX *= moXZ;
				entity.motionZ *= moXZ;
			}
		}

		else if (entity.moveForward < 0) {
			entity.motionX *= 0.85F;
			entity.motionZ *= 0.85F;
		}

		// パーティクルスポーン
		if (entity.world.isRemote && !SMConfig.isRender) {
			this.spawnParticle(entity.world, entity, 1);
		}
	}

	public void spawnParticle (World world, EntityLivingBase entity, int value) {

		Random rand = world.rand;
		float mY = (float) (entity.motionY <= 0F ? 1F : entity.motionY);

		for (int i = 0; i < value; ++i) {
			float f1 = (float) (entity.posX - 0.5F + rand.nextFloat() + entity.motionX * i / 4.0F);
			float f2 = (float) (entity.posY + 0.5F + rand.nextFloat() * 0.5F + mY * i / 4F);
			float f3 = (float) (entity.posZ - 0.5F + rand.nextFloat() + entity.motionZ * i / 4.0D);
			world.spawnParticle(EnumParticleTypes.END_ROD, f1, f2, f3, 0, 0, 0);
		}
	}
}
