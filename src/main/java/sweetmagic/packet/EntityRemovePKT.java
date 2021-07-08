package sweetmagic.packet;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.init.PotionInit;

public class EntityRemovePKT implements IMessage {

	public int x;
	public int y;
	public int z;
	public int data;
	public int level;
	public int time;
	public boolean flag;

	public EntityRemovePKT () {
	}

	public EntityRemovePKT (Entity entity, int data, int time, int level, boolean flag) {
		this.x = (int) entity.posX;
		this.y = (int) entity.posY;
		this.z = (int) entity.posZ;
		this.data = data;
		this.time = time;
		this.level = level;
		this.flag = flag;
	}

	// 変数読み出し
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.data = buf.readInt();
		this.time = buf.readInt();
		this.level = buf.readInt();
		this.flag = buf.readBoolean();
	}

	// 変数書きこみ
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.data);
		buf.writeInt(this.time);
		buf.writeInt(this.level);
		buf.writeBoolean(this.flag);
	}

	public static class Handler implements IMessageHandler<EntityRemovePKT, IMessage> {

		@Override
		public IMessage onMessage(final EntityRemovePKT mdg, final MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {

					World world = Minecraft.getMinecraft().world;
					BlockPos pos = new BlockPos(mdg.x, mdg.y, mdg.z);
		  	        AxisAlignedBB aabb = new AxisAlignedBB(pos.add(-1, -1, -1), pos.add(1, 1, 1));
		  			List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

					for (EntityLivingBase entity : list) {
						this.action(entity);
					}
				}

				public void action(EntityLivingBase entity) {

					switch (mdg.data) {
					case 0:
						entity.removePotionEffect(PotionInit.regene);
						break;
					case 1:
						entity.addPotionEffect(new PotionEffect(PotionInit.babule, mdg.time, mdg.level));
						break;
					case 2:
						if (!entity.isPotionActive(PotionInit.aether_barrier)) { return; }
						entity.removePotionEffect(PotionInit.aether_barrier);

						if (mdg.flag) {
							entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, mdg.time, mdg.level));
						}
						break;
					}
				}
			});
			return null;
		}
	}
}
