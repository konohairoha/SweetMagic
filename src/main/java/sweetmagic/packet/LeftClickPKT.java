package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.util.ItemHelper;
import sweetmagic.util.SoundHelper;

public class LeftClickPKT implements IMessage {

	public boolean isSneak;
	public int face;
	public int x;
	public int y;
	public int z;

	public LeftClickPKT() {
	}

	public LeftClickPKT(boolean isSneak, int face, int x, int y, int z) {
		this.isSneak = isSneak;
		this.face = face;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.isSneak = buf.readBoolean();
		this.face = buf.readInt();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.isSneak);
		buf.writeInt(this.face);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}

	public static class Handler implements IMessageHandler<LeftClickPKT, IMessage> {

		@Override
		public IMessage onMessage(final LeftClickPKT msg, final MessageContext ctx) {
			ctx.getServerHandler().player.mcServer.addScheduledTask(new Runnable() {

				public final static String SNEAK = "sneak";
				public final static String FACING = "facing";
				public final static String X = "posX";
				public final static String Y = "posY";
				public final static String Z = "posZ";

				@Override
				public void run() {

					EntityPlayerMP player = ctx.getServerHandler().player;
					ItemStack stack = player.getHeldItemMainhand();
					NBTTagCompound tags = ItemHelper.getNBT(stack);

					tags.setBoolean(SNEAK, msg.isSneak);
					tags.setInteger(FACING, msg.face);
					tags.setInteger(X, msg.x);
					tags.setInteger(Y, msg.y);
					tags.setInteger(Z, msg.z);

					PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_BUT, 0.5F, 0.33F), player);
					player.getCooldownTracker().setCooldown(stack.getItem(), 5);
				}
			});
			return null;
		}
	}
}
