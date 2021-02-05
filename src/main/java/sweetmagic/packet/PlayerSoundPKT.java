package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.util.SoundHelper;

public class PlayerSoundPKT implements IMessage {

	public static int sound;
	public static float pitch;
	public static float vol;

	public PlayerSoundPKT () {
	}

	public PlayerSoundPKT (int sound, float pitch, float volume) {
		this.sound = sound;
		this.pitch = pitch;
		this.vol = volume;
	}

	// 変数読み出し
	@Override
	public void fromBytes(ByteBuf buf) {
		this.sound = buf.readInt();
		this.pitch = buf.readFloat();
		this.vol = buf.readFloat();
	}

	// 変数書きこみ
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(sound);
		buf.writeFloat(pitch);
		buf.writeFloat(vol);
	}

	public static class Handler implements IMessageHandler<PlayerSoundPKT, IMessage> {

		@Override
		public IMessage onMessage(final PlayerSoundPKT mdg, final MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					SoundHelper.PlaySoundToPlayer(sound, pitch, vol);
				}
			});
			return null;
		}
	}
}
