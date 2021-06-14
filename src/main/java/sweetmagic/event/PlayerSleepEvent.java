package sweetmagic.event;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.init.DimensionInit;

public class PlayerSleepEvent {

	@SubscribeEvent
	public void onEvent(PlayerWakeUpEvent event) {

		// クライアント側かスイートマジックディメンション以外なら終了
		World world = event.getEntityPlayer().getEntityWorld();
		if (world.isRemote || world.provider.getDimension() != DimensionInit.dimID) { return; }

		// プレイヤーリストの取得
		List<EntityPlayer> playerList = world.playerEntities;
		int playerCount = playerList.size() / 2;
		int sleepCount = 0;

		// リスト分回す
		for (EntityPlayer player : playerList) {

			// 5秒以上寝てないならカウントしない
			if (player.getSleepTimer() < 100) { continue; }

			sleepCount++;

			// 寝てる人が半分以上なら
			if (sleepCount >= playerCount) {
				int dayTime = 24000;
		        WorldServer sever = world.getMinecraftServer().getWorld(0);
		        long day = (sever.getWorldTime() / dayTime) + 1;
		        sever.setWorldTime(day * dayTime);
		        return;
			}
		}
	}
}
