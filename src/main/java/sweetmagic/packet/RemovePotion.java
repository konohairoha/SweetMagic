package sweetmagic.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sweetmagic.init.PotionInit;

public class RemovePotion implements IMessage {

	public int potion;

	public RemovePotion () {
	}

	public RemovePotion (int potion) {
		this.potion = potion;
	}

	/**
	 * 1 = 幻影
	 * 2 = 全デバフ解除
	 */

	// 変数読み出し
	@Override
	public void fromBytes(ByteBuf buf) {
		this.potion = buf.readInt();
	}

	// 変数書きこみ
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.potion);
	}

	public static class Handler implements IMessageHandler<RemovePotion, IMessage> {

		@Override
		public IMessage onMessage(final RemovePotion mdg, final MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {

					EntityPlayer player = Minecraft.getMinecraft().player;
					int potion = mdg.potion;

					switch (potion) {
					case 1:
						player.removeActivePotionEffect(this.getPotion(potion));
						break;
					case 2:
						this.reflash(player);
						break;
					}
				}

				// ポーションの取得
				public Potion getPotion (int potion) {

					Potion pot = null;

					switch (potion) {
					case 1:
						pot = PotionInit.shadow;
						break;
					}

					return pot;
				}

				// デバフ解除
				public void reflash (EntityPlayer player) {
					for (Potion potion : PotionInit.getDeBuffPotionList()) {
						player.removeActivePotionEffect(potion);
					}
				}
			});
			return null;
		}
	}
}
