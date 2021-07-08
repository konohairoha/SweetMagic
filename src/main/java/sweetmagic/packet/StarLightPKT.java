package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.util.ItemHelper;

public class StarLightPKT implements IMessage {

	public int startX;
	public int startY;
	public int startZ;
	public int endX;
	public int endY;
	public int endZ;

	public StarLightPKT() { }

	public StarLightPKT(int startX, int startY, int startZ, int endX, int endY, int endZ) {
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.endX = endX;
		this.endY = endY;
		this.endZ = endZ;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.startX = buf.readInt();
		this.startY = buf.readInt();
		this.startZ = buf.readInt();
		this.endX = buf.readInt();
		this.endY = buf.readInt();
		this.endZ = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.startX);
		buf.writeInt(this.startY);
		buf.writeInt(this.startZ);
		buf.writeInt(this.endX);
		buf.writeInt(this.endY);
		buf.writeInt(this.endZ);
	}

	public static class Handler implements IMessageHandler<StarLightPKT, IMessage> {

		@Override
		public IMessage onMessage(final StarLightPKT msg, final MessageContext ctx) {
			ctx.getServerHandler().player.mcServer.addScheduledTask(new Runnable() {

				private final static String STARTX = "sX";
				private final static String STARTY = "sY";
				private final static String STARTZ = "sZ";
				private final static String ENDX = "eX";
				private final static String ENDY = "eY";
				private final static String ENDZ = "eZ";

				@Override
				public void run() {

					EntityPlayerMP player = ctx.getServerHandler().player;
					ItemStack stack = player.getHeldItemMainhand();
					NBTTagCompound tags = ItemHelper.getNBT(stack);

					if (msg.startX != 0 && msg.startY != 0 && msg.startZ != 0) {
						tags.setInteger(STARTX, msg.startX);
						tags.setInteger(STARTY, msg.startY);
						tags.setInteger(STARTZ, msg.startZ);
					}

					if (msg.endX != 0 && msg.endY != 0 && msg.endZ != 0) {
						tags.setInteger(ENDX, msg.endX);
						tags.setInteger(ENDY, msg.endY);
						tags.setInteger(ENDZ, msg.endZ);
					}

					player.world.playSound(null, player.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.NEUTRAL, 0.5F, 0.3F);
					player.getCooldownTracker().setCooldown(stack.getItem(), 5);
				}
			});
			return null;
		}
	}
}
