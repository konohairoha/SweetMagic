package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.init.tile.cook.TileJuiceMaker;

public class TileJMPKT implements IMessage {

	public int x;
	public int y;
	public int z;
	public int tickTime;
	public int cookTime;
	public boolean isCooking;

	public TileJMPKT() {}

	public TileJMPKT(BlockPos pos, int tickTime, int cookTime, boolean isCooking) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.tickTime = tickTime;
		this.cookTime = cookTime;
		this.isCooking = isCooking;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.tickTime = buf.readInt();
		this.cookTime = buf.readInt();
		this.isCooking = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(this.x);
		buf.writeFloat(this.y);
		buf.writeFloat(this.z);
		buf.writeFloat(this.tickTime);
		buf.writeFloat(this.cookTime);
		buf.writeBoolean(this.isCooking);
	}

	public static class Handler implements IMessageHandler<TileJMPKT, IMessage> {

		@Override
		public IMessage onMessage(final TileJMPKT pkt, final MessageContext ctx) {
			ctx.getServerHandler().player.mcServer.addScheduledTask(new Runnable() {

				@Override
				public void run() {

					// プレイヤーを取得
					EntityPlayer player = Minecraft.getMinecraft().player;

					if (player == null) { return; }

					BlockPos pos = new BlockPos(pkt.x, pkt.y, pkt.z);
					TileEntity tile = player.world.getTileEntity(pos);

					if (!(tile instanceof TileJuiceMaker)) { return; }

					TileJuiceMaker table = (TileJuiceMaker) tile;
					table.tickTime = pkt.tickTime;
					table.cookTime = pkt.cookTime;
					table.isCooking = pkt.isCooking;

				}
			});

			return null;
		}
	}
}
