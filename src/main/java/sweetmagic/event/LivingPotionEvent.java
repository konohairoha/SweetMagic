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

//		// 盲目ならタゲをnullに書き換え
//		if (this.tickTime % 20 == 0 && entity.isPotionActive(MobEffects.BLINDNESS) && entity instanceof EntityLiving) {
//			SMUtil.tameAIAnger((EntityLiving) entity, null);
//		}

		// 飛躍上昇なら
		if (entity.isPotionActive(MobEffects.JUMP_BOOST)) {
			entity.fallDistance = 0;
		}
	}
}
