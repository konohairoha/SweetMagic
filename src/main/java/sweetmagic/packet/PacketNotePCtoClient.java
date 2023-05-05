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
import sweetmagic.init.tile.chest.TileNotePC;

public class PacketNotePCtoClient implements IMessage {

	private int x;
	private int y;
	private int z;
	private int id;
	private int rand;
	private int listId;
	private float rate;

	public PacketNotePCtoClient() {}

	public PacketNotePCtoClient(int x, int y, int z, int id, int rand, int listId, float rate) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
		this.rand = rand;
		this.listId = listId;
		this.rate = rate;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.id = buf.readInt();
		this.rand = buf.readInt();
		this.listId = buf.readInt();
		this.rate = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.id);
		buf.writeInt(this.rand);
		buf.writeInt(this.listId);
		buf.writeFloat(this.rate);
	}

	public static class Handler implements IMessageHandler<PacketNotePCtoClient, IMessage> {

		@Override
		public IMessage onMessage(final PacketNotePCtoClient pkt, final MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {

					EntityPlayer player = Minecraft.getMinecraft().player;
					if (player == null) { return; }

					World world = player.world;
					TileEntity tile = world.getTileEntity(new BlockPos(pkt.x, pkt.y, pkt.z));
					if (!(tile instanceof TileNotePC)) { return; }

					TileNotePC note = (TileNotePC) tile;

					if (pkt.id != 100) {


						if (pkt.listId >= 27) {

							int page = pkt.listId - 26;

							if (note.getPage() != page) {
								note.setPage(page);
							}
						}
					}

					else if (pkt.listId != 100) {

						switch (pkt.listId) {
						case 0:
							note.setBagStack(pkt.rand);
							break;
						case 1:
							note.setSaplingStack(pkt.rand);
							break;
						case 2:
							note.setNuggetStack(pkt.rand);
							break;
						case 3:
							note.setDropStack(pkt.rand);
							break;
						case 4:
							note.setCrystalStack(pkt.rand);
							break;
						}
					}

					else {
						note.rate = pkt.rate;
						note.dateSP = 0;
					}
				}
			});
			return null;
		}
	}
}
