package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.api.iitem.IWand;

public class MouseSclorPKT implements IMessage {

	public ItemStack stack;
	public boolean isNext;

	public MouseSclorPKT() {
	}

	public MouseSclorPKT(ItemStack stack, boolean isNext) {
		this.stack = stack;
		this.isNext = isNext;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.stack = ByteBufUtils.readItemStack(buf);
		this.isNext = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeItemStack(buf, this.stack);
		buf.writeBoolean(this.isNext);
	}

	public static class Handler implements IMessageHandler<MouseSclorPKT, IMessage> {

		@Override
		public IMessage onMessage(final MouseSclorPKT msg, final MessageContext ctx) {
			ctx.getServerHandler().player.mcServer.addScheduledTask(new Runnable() {

				@Override
				public void run() {

					EntityPlayerMP player = ctx.getServerHandler().player;
					ItemStack stack = player.getHeldItemMainhand();

					if (stack.isEmpty() || !(stack.getItem() instanceof IWand)) { return; }

					IWand wand = (IWand) stack.getItem();

					if (msg.isNext) {
						wand.nextSlot(player.world, player, stack);
						return;
					}

					else {
						wand.backSlot(player.world, player, stack);
						return;
					}
				}
			});
			return null;
		}
	}
}
