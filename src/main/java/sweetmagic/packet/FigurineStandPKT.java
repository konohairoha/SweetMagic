package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.init.block.blocks.FigurineStand;
import sweetmagic.init.entity.monster.ISMMob;
import sweetmagic.init.tile.magic.TileFigurineStand;

public class FigurineStandPKT implements IMessage {

	public boolean isSpecial;
//	public boolean isSub;
	public int x;
	public int y;
	public int z;

	public FigurineStandPKT() { }

	public FigurineStandPKT(boolean isSpecial, BlockPos pos) {
		this.isSpecial = isSpecial;
//		this.isSub = isSub;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.isSpecial = buf.readBoolean();
//		this.isSub = buf.readBoolean();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.isSpecial);
//		buf.writeBoolean(this.isSub);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}

	public static class Handler implements IMessageHandler<FigurineStandPKT, IMessage> {

		@Override
		public IMessage onMessage(final FigurineStandPKT msg, final MessageContext ctx) {


			EntityPlayer player = ctx.getServerHandler().player;
			if (player == null) { return null; }

			BlockPos pos = new BlockPos(msg.x, msg.y, msg.z);
			TileEntity tile = player.world.getTileEntity(pos);
			if (!(tile instanceof TileFigurineStand)) { return null; }

			TileFigurineStand fs = (TileFigurineStand) tile;
			fs.setSpecial(msg.isSpecial);

			EntityLivingBase entity = fs.getEntity();
			ISMMob sm = (ISMMob) entity;
			boolean isSpe = sm.getSpecial();

			if (fs.getDate() == 3) {

				EntityLivingBase sub = fs.getSubEntity();

				// 騎乗していなければ馬の生成
				if (sub == null) {
					FigurineStand block = (FigurineStand) player.world.getBlockState(pos).getBlock();
					fs.setSubEntity(block.getSub(player.world, pos));
				}

				// 騎乗解除して馬を削除
				else {
					fs.setSubEntity(null);
					entity.dismountRidingEntity();
				}
			}

			sm.setSpecial(!isSpe);

//			fs.setSub(msg.isSub);
			return null;
		}
	}
}
