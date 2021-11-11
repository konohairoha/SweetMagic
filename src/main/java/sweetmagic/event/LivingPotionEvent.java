package sweetmagic.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
}
