package sweetmagic.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.potion.RenderEffect;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = SweetMagicCore.MODID, value = Side.CLIENT)
public class SMClientEvent {

	@SubscribeEvent
	public static void renderLivingPost(RenderLivingEvent.Post<EntityLivingBase> event) {
		for (RenderEffect effect : RenderEffect.VALUES) {
			if (effect.shouldRender(event.getEntity(), false)) {
				effect.render(event.getEntity(), event.getRenderer(), event.getX(), event.getY(), event.getZ(), event.getPartialRenderTick(), false);
			}
		}
	}
}
