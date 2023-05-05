package sweetmagic.init.tile.magic;

import java.util.Random;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.config.SMConfig;
import sweetmagic.util.WorldHelper;

public class TileMagiaLantern extends TileMagiaLight {

	private final int maxMagiaFlux = 50000; 	// 最大MF量を設定
	private static final int SHRINK_MF = SMConfig.magiaLightRange / 4;

	@Override
	public void clientUpdate() {

		super.clientUpdate();

		// 一定時間経てば処理
		if (this.getTime() % 20 != 0 || this.isMfEmpty() || !this.isActive(this.pos) || WorldHelper.isPeaceful(this.world)) { return; }

		Random rand = this.world.rand;
		BlockPos pos = this.pos;

		float randX = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
		float randY = (rand.nextFloat() - rand.nextFloat()) * 0.15F;
		float randZ = (rand.nextFloat() - rand.nextFloat()) * 0.1F;

		float x = pos.getX() + 0.5F + randX;
		float y = pos.getY() + 0.3F + randY;
		float z = pos.getZ() + 0.5F + randZ;

		int red = rand.nextInt(123) + 122;
		int blue = rand.nextInt(123) + 122;
		int green = rand.nextInt(123) + 122;

		Particle effect = ParticleNomal.create(this.world, x, y, z, 0, 0, 0, red, blue, green);
		this.getParticle().addEffect(effect);
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 2500;
    }

	// 一度に消費するMF
	public int getShrinkMF () {
		return SHRINK_MF;
	}

	public int getRange () {
		return SMConfig.magiaLightRange / 4;
	}
}
