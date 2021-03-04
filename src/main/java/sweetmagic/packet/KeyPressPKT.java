package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.api.iitem.IRobe;
import sweetmagic.api.iitem.IWand;
import sweetmagic.key.SMKeybind;

public class KeyPressPKT implements IMessage {

	public SMKeybind key;

	public KeyPressPKT() {
	}

	public KeyPressPKT(SMKeybind key) {
		this.key = key;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.key = SMKeybind.values()[buf.readInt()];
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.key.ordinal());
	}

	public static class Handler implements IMessageHandler<KeyPressPKT, IMessage> {

		@Override
		public IMessage onMessage(final KeyPressPKT message, final MessageContext ctx) {
			ctx.getServerHandler().player.mcServer.addScheduledTask(new Runnable() {

				@Override
				public void run() {

					EntityPlayerMP player = ctx.getServerHandler().player;
					ItemStack stack = player.getHeldItemMainhand();

					Item item = stack.getItem();

					// 押したキーで処理
					switch (message.key) {
					case OPEN:

						// 杖を持っていたら
						if (item instanceof IWand) {
							IWand wand = (IWand) item;
							wand.openGui(player.world, player, stack);
							return;
						}

						// それ以外
						else {

							ItemStack armor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

							// ローブを着ていたら
							if (!armor.isEmpty() && armor.getItem() instanceof IRobe) {
								IRobe robe = (IRobe) armor.getItem();
								robe.openGUI(player.world, player, armor);
								return;
							}
						}

						return;
					case NEXT:
						if (item instanceof IWand) {
							IWand wand = (IWand) item;
							wand.nextSlot(player.world, player, stack);
						}
						return;
					case BACK:
						if (item instanceof IWand) {
							IWand wand = (IWand) item;
							wand.backSlot(player.world, player, stack);
						}
						return;

					// ポーチ
					case POUCH:

						ItemStack legs = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);

						// ポーチ
						if (!legs.isEmpty() && legs.getItem() instanceof IPouch) {
							IPouch robe = (IPouch) legs.getItem();
							robe.openGUI(player.world, player, legs);
						}
						return;

					default:
						break;
					}
				}
			});
			return null;
		}
	}
}
