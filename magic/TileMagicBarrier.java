package sweetmagic.init.tile.magic;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import sweetmagic.init.BlockInit;
import sweetmagic.init.PotionInit;
import sweetmagic.util.PlayerHelper;

public class TileMagicBarrier extends TileSMBase {

	private static final int RANGE = 64;

	// ロック状態の取得
	public boolean getRock () {
		return this.getBlock(this.pos) == BlockInit.magicbarrier_on;
	}

	@Override
	public void update() {

		if (!this.isSever() || !this.getRock()) { return; }

		this.tickTime++;
		if (this.tickTime % 60 != 0) { return; }

		this.tickTime = 0;
		List<EntityPlayer> entityList = this.getEntityList(EntityPlayer.class, this.getAABB(RANGE, RANGE, RANGE));
		if (entityList.isEmpty()) { return; }

		for (EntityPlayer player : entityList) {

			if (player.isCreative()) { continue; }

			PlayerHelper.addPotion(player, PotionInit.breakblock, 400, 0, true);
		}
	}
}
