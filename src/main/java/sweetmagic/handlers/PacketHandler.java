package sweetmagic.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.packet.EntityRemovePKT;
import sweetmagic.packet.FigurineStandPKT;
import sweetmagic.packet.GravityChestClientPKT;
import sweetmagic.packet.GravityChestSeverPKT;
import sweetmagic.packet.KeyPressPKT;
import sweetmagic.packet.LeftClickPKT;
import sweetmagic.packet.MFGenerationPKT;
import sweetmagic.packet.MFHarvesterClientPKT;
import sweetmagic.packet.MFHarvesterSeverPKT;
import sweetmagic.packet.MouseSclorPKT;
import sweetmagic.packet.PacketNotePCButtonClient;
import sweetmagic.packet.PacketNotePCtoClient;
import sweetmagic.packet.PacketNotePCtoSever;
import sweetmagic.packet.PacketUpdateSlot;
import sweetmagic.packet.PedalCreatePKT;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.packet.RemovePotion;
import sweetmagic.packet.SMBucketPKT;
import sweetmagic.packet.ScrollPagePKT;
import sweetmagic.packet.StarLightPKT;
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
//		HANDLER.registerMessage(TileMFBlockPKT.Handler.class, TileMFBlockPKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(EntityRemovePKT.Handler.class, EntityRemovePKT.class, disc++, Side.CLIENT);
		HANDLER.registerMessage(StarLightPKT.Handler.class, StarLightPKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(SMBucketPKT.Handler.class, SMBucketPKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(FigurineStandPKT.Handler.class, FigurineStandPKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(PedalCreatePKT.Handler.class, PedalCreatePKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(MFGenerationPKT.Handler.class, MFGenerationPKT.class, disc++, Side.CLIENT);
		HANDLER.registerMessage(PacketNotePCtoClient.Handler.class, PacketNotePCtoClient.class, disc++, Side.CLIENT);
		HANDLER.registerMessage(PacketNotePCtoSever.Handler.class, PacketNotePCtoSever.class, disc++, Side.SERVER);
		HANDLER.registerMessage(PacketNotePCButtonClient.Handler.class, PacketNotePCButtonClient.class, disc++, Side.CLIENT);
		HANDLER.registerMessage(GravityChestClientPKT.Handler.class, GravityChestClientPKT.class, disc++, Side.CLIENT);
		HANDLER.registerMessage(GravityChestSeverPKT.Handler.class, GravityChestSeverPKT.class, disc++, Side.SERVER);
		HANDLER.registerMessage(PacketUpdateSlot.Handler.class, PacketUpdateSlot.class, disc++, Side.CLIENT);
		HANDLER.registerMessage(MFHarvesterClientPKT.Handler.class, MFHarvesterClientPKT.class, disc++, Side.CLIENT);
		HANDLER.registerMessage(MFHarvesterSeverPKT.Handler.class, MFHarvesterSeverPKT.class, disc++, Side.SERVER);
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
