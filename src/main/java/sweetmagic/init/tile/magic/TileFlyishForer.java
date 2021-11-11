package sweetmagic.init.tile.magic;

import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.block.magic.MFPot;
import sweetmagic.packet.TileMFBlockPKT;
import sweetmagic.util.SMUtil;

public class TileFlyishForer extends TileMFFisher {

	public int needMF = 1000;

	// 釣りのルートテーブルから引き出す
	public boolean onFishing () {

		this.setMF(this.getMF() - this.needMF);
		PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));

		if (this.isSever()) {

			// ルートテーブルをリストに入れる
			List<ItemStack> meat = SMUtil.getOreList("listAllmeatraw");
			ItemStack stack = meat.get(this.rand.nextInt(meat.size()));

			// アイテムをスポーン
			ItemHandlerHelper.insertItemStacked(this.outputInv, stack.copy(), false);

			// 30%の確率で
			if (this.rand.nextFloat() <= 0.3F) {
				ItemHandlerHelper.insertItemStacked(this.outputInv, new ItemStack(Items.BEEF), false);
			}

			// 30%の確率で
			if (this.rand.nextFloat() <= 0.3F) {
				ItemHandlerHelper.insertItemStacked(this.outputInv, new ItemStack(Items.PORKCHOP), false);
			}
		}

		this.playSound(this.pos, SoundEvents.ENTITY_SHEEP_SHEAR, 0.5F, 1F);
		this.randTick = this.getBlock(this.pos.up()) instanceof MFPot ? 150 : 300;
		return false;
	}
}
