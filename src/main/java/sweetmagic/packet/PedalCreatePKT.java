package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.init.tile.magic.TilePedalCreate;

public class PedalCreatePKT implements IMessage {

	public int needChargeTime;
	public int x;
	public int y;
	public int z;

	public PedalCreatePKT() { }

	public PedalCreatePKT(int needChargeTime, BlockPos pos) {
		this.needChargeTime = needChargeTime;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.needChargeTime = buf.readInt();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.needChargeTime);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}

	public static class Handler implements IMessageHandler<PedalCreatePKT, IMessage> {

		@Override
		public IMessage onMessage(final PedalCreatePKT msg, final MessageContext ctx) {

			EntityPlayer player = ctx.getServerHandler().player;
			if (player == null) { return null; }

			TilePedalCreate tile = (TilePedalCreate) player.world.getTileEntity(new BlockPos(msg.x, msg.y, msg.z));
			tile.needChargeTime = msg.needChargeTime;
			return null;
		}
	}
}
