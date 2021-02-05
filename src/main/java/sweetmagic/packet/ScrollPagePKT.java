package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.init.tile.container.ContainerParallelInterfere;

public class ScrollPagePKT implements IMessage {

	public int page;

	public ScrollPagePKT() {}

	public ScrollPagePKT(int page) {
		this.page = page;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.page = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.page);
	}

	public static class Handler implements IMessageHandler<ScrollPagePKT, IMessage> {

		@Override
		public IMessage onMessage(final ScrollPagePKT pkt, final MessageContext ctx) {

			EntityPlayer player = ctx.getServerHandler().player;
			ContainerParallelInterfere container = (ContainerParallelInterfere) player.openContainer;
			container.onScrollChanged(pkt.page);

			return null;
		}
	}
}
