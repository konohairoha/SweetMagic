package sweetmagic.init.item.sm.magic;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import sweetmagic.api.iitem.IElementItem;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;

public class MFTime extends MFSlotItem implements IElementItem {

	public int time;

	public MFTime (String name, int time) {
		super(name, SMType.AIR, SMElement.TIME, 1, 40, 10, true);
		this.setTime(time);
	}

	// ツールチップ
	public List<String> magicToolTip (List<String> toolTip) {

		switch (this.time) {
		case 0:
			toolTip.add("tip.magic_sun.name");
			break;
		case 14000:
			toolTip.add("tip.magic_moon.name");
			break;
		}

		return toolTip;
	}

	// 変更時間を設定
	public void setTime (int time) {
		this.time = time;
	}

	// 変更時間を取得
	public int getTime () {
		return this.time;
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, ItemStack stack, Item slotItem) {

		// 時間設定
//		world.setWorldTime(this.getTime());
		world.playSound(null, new BlockPos(player), SMSoundEvent.CHANGETIME, SoundCategory.VOICE, 0.35F, 1.0F);

		if (world instanceof WorldServer) {

			int dayTime = 24000;
			World seWorld = player.getEntityWorld();
            WorldServer sever = seWorld.getMinecraftServer().getWorld(0);
            long day = (sever.getWorldTime() / dayTime) + 1;
            sever.setWorldTime(this.getTime() + (day * dayTime));
		}

		return true;
	}
}
