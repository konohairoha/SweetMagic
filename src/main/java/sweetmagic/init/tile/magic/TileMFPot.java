package sweetmagic.init.tile.magic;

import net.minecraft.util.EnumParticleTypes;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.packet.TileMFBlockPKT;
import sweetmagic.util.ParticleHelper;

public class TileMFPot extends TileMFBase {

	public TileMFPot () {
		super(false);
	}

	@Override
	public void serverUpdate() {

		super.serverUpdate();

		if (this.getBlock(this.pos) == BlockInit.mfpot) {

			// 雨が降っていてMFが溜めれる状態なら
			if (this.world.isRaining() && this.canMFChange()) {

				this.setMF(this.getMF() + 1);

				long time = this.getTime();

				if (time % 20 == 0) {
					PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));
				}

				if (time % 100 == 0) {
					this.spawnParticles();
				}
			}
		}

		else {

			long time = this.world.getWorldTime();
			long worldTime = time % 24000;

			if (worldTime >= 11000 && worldTime < 14000) {

				this.setMF(this.getMF() + 8);

				if (time % 20 == 0) {
					PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));
				}

				if (time % 100 == 0) {
					this.spawnParticles();
				}
			}
		}
	}

	// パーティクルスポーン
	public void spawnParticles() {
		ParticleHelper.spawnBoneMeal(this.world, this.pos, EnumParticleTypes.VILLAGER_HAPPY);
	}
}
