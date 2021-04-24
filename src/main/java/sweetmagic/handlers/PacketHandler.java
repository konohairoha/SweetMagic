package sweetmagic.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.packet.EntityRemovePKT;
import sweetmagic.packet.KeyPressPKT;
import sweetmagic.packet.LeftClickPKT;
import sweetmagic.packet.MouseSclorPKT;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.packet.RemovePotion;
import sweetmagic.packet.ScrollPagePKT;
import sweetmagic.packet.TileMFBlockPKT;
import sweetmagic.packet.WandLeftClickPKT;

public class PacketHandler {

	private static final SimpleNetworkWrapper HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel(SweetMagicCore.MODID);

	public static void register() {
		int disc = 0;
		HANDLER.registerMessage(KeyPressPKT.Handler.class, KeyPressPKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(PlayerSoundPKT.Handler.class, PlayerSoundPKT.class, disc++, Side.CLIENT);
		HANDLER.registerMessage(TileMFBlockPKT.Handler.class, TileMFBlockPKT.class, disc++, Side.CLIENT);
		HANDLER.registerMessage(RemovePotion.Handler.class, RemovePotion.class, disc++, Side.CLIENT);
		HANDLER.registerMessage(MouseSclorPKT.Handler.class, MouseSclorPKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(ScrollPagePKT.Handler.class, ScrollPagePKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(LeftClickPKT.Handler.class, LeftClickPKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(WandLeftClickPKT.Handler.class, WandLeftClickPKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(TileMFBlockPKT.Handler.class, TileMFBlockPKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(EntityRemovePKT.Handler.class, EntityRemovePKT.class, disc++, Side.CLIENT);
	}

	public static void sendToServer(IMessage msg) {
		HANDLER.sendToServer(msg);
	}

	public static void sendToPlayer(IMessage msg, EntityPlayerMP player) {
		if (!(player instanceof FakePlayer)) {
			HANDLER.sendTo(msg, player);
		}
	}

	public static void sendToClient(IMessage msg) {
		HANDLER.sendToAll(msg);
	}
}
