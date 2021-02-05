package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.api.iblock.IMFBlock;

public class TileMFBlockPKT implements IMessage {

	public int tickTime;
	public int randTime;
	public int mf;
	public int x;
	public int y;
	public int z;

	public TileMFBlockPKT() {
	}

	public TileMFBlockPKT(int tickTime, int randTime, int mf, BlockPos pos) {
		this.tickTime = tickTime;
		this.randTime = randTime;
		this.mf = mf;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.tickTime = buf.readInt();
		this.randTime = buf.readInt();
		this.mf = buf.readInt();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.tickTime);
		buf.writeInt(this.randTime);
		buf.writeInt(this.mf);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}

	public static class Handler implements IMessageHandler<TileMFBlockPKT, IMessage> {

		@Override
		public IMessage onMessage(final TileMFBlockPKT msg, final MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {

					// プレイヤーを取得
					EntityPlayer player = Minecraft.getMinecraft().player;
					if (player == null) { return; }

					BlockPos pos = new BlockPos(msg.x, msg.y, msg.z);
					TileEntity tile = player.world.getTileEntity(pos);
					if (!(tile instanceof IMFBlock)) { return; }

					IMFBlock fisher = (IMFBlock) tile;
					int tickTime = msg.tickTime;
					int mf = msg.mf;
					fisher.setTickTime(tickTime);
					fisher.setMF(mf);
				}
			});
			return null;
		}
	}
}
