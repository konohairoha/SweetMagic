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

public class PacketNotePCButtonClient implements IMessage {

	private int x;
	private int y;
	private int z;
	private int id;
	private float rate;
	private int addValue;
	private int sp;
	private int dateSP;

	public PacketNotePCButtonClient() {}

	public PacketNotePCButtonClient(int x, int y, int z, int id, float rate, int addValue, int sp, int dateSP) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
		this.rate = rate;
		this.addValue = addValue;
		this.sp = sp;
		this.dateSP = dateSP;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.id = buf.readInt();
		this.rate = buf.readFloat();
		this.addValue = buf.readInt();
		this.sp = buf.readInt();
		this.dateSP = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.id);
		buf.writeFloat(this.rate);
		buf.writeInt(this.addValue);
		buf.writeInt(this.sp);
		buf.writeInt(this.dateSP);
	}

	public static class Handler implements IMessageHandler<PacketNotePCButtonClient, IMessage> {

		@Override
		public IMessage onMessage(final PacketNotePCButtonClient pkt, final MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {

					EntityPlayer player = Minecraft.getMinecraft().player;
					if (player == null) { return; }

					World world = player.world;
					TileEntity tile = world.getTileEntity(new BlockPos(pkt.x, pkt.y, pkt.z));
					if (!(tile instanceof TileNotePC)) { return; }

					TileNotePC note = (TileNotePC) tile;
					int id = pkt.id;

					// アイテム売却ボタン
					if (id == 26) {
						note.sp = pkt.sp;
						note.dateSP = pkt.dateSP;
					}

					else if (id >= 27) {

						int page = id - 26;

						if (note.getPage() != page) {
							note.setPage(page);
						}
					}

					else if (id % 5 == 0) {
						note.sp = pkt.sp;
					}

					else {

						switch(id) {
						case 1:
						case 2:
						case 3:
						case 4:
							note.bagValue = pkt.addValue;
							break;
						case 6:
						case 7:
						case 8:
						case 9:
							note.saplingValue = pkt.addValue;
							break;
						case 11:
						case 12:
						case 13:
						case 14:
							note.nuggetValue = pkt.addValue;
							break;
						case 16:
						case 17:
						case 18:
						case 19:
							note.dropValue = pkt.addValue;
							break;
						case 21:
						case 22:
						case 23:
						case 24:
							note.emeraldValue = pkt.addValue;
							break;
						}
					}
				}
			});
			return null;
		}
	}
}
