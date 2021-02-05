package sweetmagic.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.packet.MouseSclorPKT;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = SweetMagicCore.MODID)
public class MouseScrollEvent {

	@SubscribeEvent
	public static void onMouseEvent(MouseEvent event) {

		EntityPlayer player = Minecraft.getMinecraft().player;
		ItemStack stack = player.getHeldItemMainhand();

		// 杖を持っていないなら終了
		if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof IWand)) { return; }

		if (Minecraft.getMinecraft().inGameHasFocus && event.getDwheel() != 0 && player.isSneaking()) {
			event.setCanceled(true);
			int d = event.getDwheel();
			PacketHandler.sendToServer(new MouseSclorPKT(stack, d > 0));
		}
	}
}
