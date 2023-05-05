package sweetmagic.init.tile.magic;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;

public class TileMFAetherLanp extends TileMFBase {

	// 最大MF量を設定
	public int maxMagiaFlux = 10000;
	public boolean findPlayer = false;

	@Override
	public void serverUpdate() {

		super.serverUpdate();

		// 一定時間経つと送受信をする
		if (this.getTime() % 60 == 0) {
			this.roundMFRecive();
		}
	}

	// 周囲のMF回収
	public void roundMFRecive () {

		// 範囲の座標を取得
		int area = 12;
		Iterable<BlockPos> posList = BlockPos.getAllInBox(this.pos.add(-area, 0, -area), this.pos.add(area, area / 2, area));

		// リスト分まわす
		for (BlockPos p : posList) {

			// MFブロック以外なら終了
			IBlockState state = this.getState(p);
			Block block = state.getBlock();
			if ( !(block instanceof BaseMFBlock) || !block.hasTileEntity() ) { continue; }

			// 受信側なら終了
			TileMFBase tile = (TileMFBase) this.getTile(p);
			if (tile.getReceive()) { continue; }

			// MFブロックからMFを入れるときの処理
			this.insetMF(this , tile, this.getTickTime());

			// MFを貯めれなくなったら終了
			if (!this.canMFChange()) { return; }
		}
	}

	@Override
	public void clientUpdate() {

		if (this.getTime() % 30 == 0 && this.canMFChange()) {

			this.checkRangePlayer();
			if (!this.findPlayer) { return; }

			Random rand = this.world.rand;
			int area = 12;
			Iterable<BlockPos> posArray = BlockPos.getAllInBox(this.pos.add(-area, 0, -area), this.pos.add(area, area / 2, area));

			// リスト分まわす
			for (BlockPos p : posArray) {

				// MFブロック以外なら終了
				Block block = this.getState(p).getBlock();
				if ( !(block instanceof BaseMFBlock) || block == BlockInit.aether_lanp || !block.hasTileEntity()) { continue; }

				// 受信側なら終了
				TileMFBase tile = (TileMFBase) this.getTile(p);
				if (tile.getReceive() || tile.isMfEmpty()) { continue; }

				float pX = this.pos.getX() - p.getX();
				float pY = this.pos.getY() - p.getY();
				float pZ = this.pos.getZ() - p.getZ();

				for (int i = 0; i < 2; i++) {

					float randX = (rand.nextFloat() - rand.nextFloat()) * 0.5F;
					float randY = (rand.nextFloat() - rand.nextFloat()) * 0.5F;
					float randZ = (rand.nextFloat() - rand.nextFloat()) * 0.5F;

					float x = p.getX() + 0.5F + randX;
					float y = p.getY() + 0.525F + randY;
					float z = p.getZ() + 0.5F + randZ;
					float xSpeed = pX * 0.1175F;
					float ySpeed = pY * 0.1175F;
					float zSpeed = pZ * 0.1175F;

					Particle effect = ParticleNomal.create(this.world, x, y, z, xSpeed, ySpeed, zSpeed);
					this.getParticle().addEffect(effect);
				}
			}
		}

		super.clientUpdate();
	}

	public void checkRangePlayer () {
		this.findPlayer = this.findRangePlayer(32D, 16D, 32D);
	}

	// 最大MF量を設定
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
		return 5000;
    }

}
