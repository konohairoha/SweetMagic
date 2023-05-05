package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.init.tile.magic.TileGravityChest;

public class GravityChestClientPKT implements IMessage {

	private int x;
	private int y;
	private int z;
	private int range;

	public GravityChestClientPKT() { }

	public GravityChestClientPKT(int x, int y, int z, int range) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.range = range;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.range = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.range);
	}

	public static class Handler implements IMessageHandler<GravityChestClientPKT, IMessage> {

		@Override
		public IMessage onMessage(final GravityChestClientPKT pkt, final MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {

					EntityPlayer player = Minecraft.getMinecraft().player;
					if (player == null) { return; }

					World world = player.world;
					TileEntity tile = world.getTileEntity(new BlockPos(pkt.x, pkt.y, pkt.z));
					if (!(tile instanceof TileGravityChest)) { return; }

					TileGravityChest chest = (TileGravityChest) tile;
					chest.range = pkt.range;
				}
			});
			return null;
		}
	}
}
