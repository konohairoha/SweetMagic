package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.init.tile.magic.TileMFGeneration;

public class MFGenerationPKT implements IMessage {

	public int lava;
	public int x;
	public int y;
	public int z;

	public MFGenerationPKT() { }

	public MFGenerationPKT(int lava, BlockPos pos) {
		this.lava = lava;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.lava = buf.readInt();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.lava);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}

	public static class Handler implements IMessageHandler<MFGenerationPKT, IMessage> {

		@Override
		public IMessage onMessage(final MFGenerationPKT msg, final MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {

					// プレイヤーを取得
					EntityPlayer player = Minecraft.getMinecraft().player;
					if (player == null) { return; }

					BlockPos pos = new BlockPos(msg.x, msg.y, msg.z);
					TileEntity tile = player.world.getTileEntity(pos);
					if (!(tile instanceof TileMFGeneration)) { return; }

					TileMFGeneration fisher = (TileMFGeneration) tile;
					fisher.lavaValue = msg.lava;
				}
			});
			return null;
		}
	}
}
