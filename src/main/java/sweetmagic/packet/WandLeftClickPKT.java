package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.api.iitem.IWand;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.util.ItemHelper;
import sweetmagic.util.SoundHelper;

public class WandLeftClickPKT implements IMessage {

	public boolean flag;
	public int slot;

	public WandLeftClickPKT() {
	}

	public WandLeftClickPKT(boolean flag, int slot) {
		this.flag = flag;
		this.slot = slot;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.flag = buf.readBoolean();
		this.slot = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.flag);
		buf.writeInt(this.slot);
	}

	public static class Handler implements IMessageHandler<WandLeftClickPKT, IMessage> {

		@Override
		public IMessage onMessage(final WandLeftClickPKT msg, final MessageContext ctx) {
			ctx.getServerHandler().player.mcServer.addScheduledTask(new Runnable() {

				@Override
				public void run() {

					EntityPlayerMP player = ctx.getServerHandler().player;
					NBTTagCompound tags = ItemHelper.getNBT(player.getHeldItemMainhand());

					tags.setBoolean(IWand.FAVFLAG, msg.flag);
					tags.setInteger(IWand.SLOT , msg.slot);

					PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_NEXT, 1F, 0.33F), player);
				}
			});
			return null;
		}
	}
}
