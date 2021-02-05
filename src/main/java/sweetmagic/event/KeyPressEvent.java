package sweetmagic.event;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.key.ClientKeyHelper;
import sweetmagic.packet.KeyPressPKT;

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
}
