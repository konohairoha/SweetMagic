package sweetmagic.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LivingPotionEvent {

	@SubscribeEvent
	public void livingEvent (LivingEvent event) {

		EntityLivingBase entity = event.getEntityLiving();
		if (entity == null) { return; }

		// 飛躍上昇なら
		if (entity.isPotionActive(MobEffects.JUMP_BOOST)) {
			entity.fallDistance = 0;
		}
	}
}
