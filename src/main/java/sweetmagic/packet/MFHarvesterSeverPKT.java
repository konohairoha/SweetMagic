package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.tile.magic.TileMFHarvester;

public class MFHarvesterSeverPKT implements IMessage {

	private int x;
	private int y;
	private int z;
	private int range;
	private boolean isRender;
	private boolean isChangeRange;

	public MFHarvesterSeverPKT() {}

	public MFHarvesterSeverPKT(int x, int y, int z, int range, boolean isRender, boolean isChangeRange) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.range = range;
		this.isRender = isRender;
		this.isChangeRange = isChangeRange;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.range = buf.readInt();
		this.isRender = buf.readBoolean();
		this.isChangeRange = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.range);
		buf.writeBoolean(this.isRender);
		buf.writeBoolean(this.isChangeRange);
	}

	public static class Handler implements IMessageHandler<MFHarvesterSeverPKT, IMessage> {

		@Override
		public IMessage onMessage(final MFHarvesterSeverPKT pkt, final MessageContext ctx) {

			EntityPlayer player = ctx.getServerHandler().player;
			if (player == null) { return null; }

			World world = player.world;
			TileEntity tile = world.getTileEntity(new BlockPos(pkt.x, pkt.y, pkt.z));
			if (!(tile instanceof TileMFHarvester)) { return null; }

			TileMFHarvester chest = (TileMFHarvester) tile;
			chest.range = pkt.range;
			chest.isRangeRender = pkt.isRender;
			chest.resetInfo();

			PacketHandler.sendToClient(new MFHarvesterClientPKT(pkt.x, pkt.y, pkt.z, chest.range, chest.isRangeRender, pkt.isChangeRange));
			return null;
		}
	}
}
