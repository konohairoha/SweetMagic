package sweetmagic.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.init.PotionInit;

public class TeleportEvent {

	@SubscribeEvent
	public void teleportEvent(EnderTeleportEvent event) {

		if (event.getEntityLiving() == null) { return; }

		// 重力状態以外なら終了
		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.isPotionActive(PotionInit.gravity)) { return; }

		event.setCanceled(true);
	}
}
