package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.CooldownTracker;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.init.item.sm.sweetmagic.SMBucket;
import sweetmagic.util.ItemHelper;

public class SMBucketPKT implements IMessage {

	public int amount;

	public SMBucketPKT() {}

	public SMBucketPKT(int amount) {
		this.amount = amount;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.amount = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.amount);
	}

	public static class Handler implements IMessageHandler<SMBucketPKT, IMessage> {

		@Override
		public IMessage onMessage(final SMBucketPKT pkt, final MessageContext ctx) {

			EntityPlayer player = ctx.getServerHandler().player;
			ItemStack stack = player.getHeldItemMainhand();
			Item item = stack.getItem();
			CooldownTracker cool = player.getCooldownTracker();
			if (!(item instanceof SMBucket) || cool.hasCooldown(item)) { return null; }

			SMBucket bucket = (SMBucket) item;
			NBTTagCompound tags = ItemHelper.getNBT(stack);
			bucket.setAmout(tags, pkt.amount);
			cool.setCooldown(item, 1);

			return null;
		}
	}
}
