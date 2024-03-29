package sweetmagic.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.key.ClientKeyHelper;
import sweetmagic.packet.KeyPressPKT;
import sweetmagic.packet.MouseSclorPKT;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = SweetMagicCore.MODID)
public class KeyPressEvent {

	// キー入力イベント
	@SubscribeEvent
	public static void keyPress(KeyInputEvent event) {
		for (KeyBinding k : ClientKeyHelper.mcToPe.keySet()) {
			if (k.isPressed()) {
				PacketHandler.sendToServer(new KeyPressPKT(ClientKeyHelper.mcToPe.get(k)));
			}
		}
	}

	// マウススクロールイベント
	@SubscribeEvent
	public static void onMouseEvent(MouseEvent event) {

		// スペクターモードなら終了
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player.isSpectator()) { return; }

		// 杖を持っていないなら終了
		ItemStack stack = player.getHeldItemMainhand();
		if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof IWand)) { return; }

		if (Minecraft.getMinecraft().inGameHasFocus && event.getDwheel() != 0 && player.isSneaking()) {
			event.setCanceled(true);
			PacketHandler.sendToServer(new MouseSclorPKT(stack, event.getDwheel() > 0));
		}
	}
}
