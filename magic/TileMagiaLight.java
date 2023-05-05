package sweetmagic.init.tile.magic;

import sweetmagic.config.SMConfig;
import sweetmagic.util.WorldHelper;

public class TileMagiaLight extends TileMFBase {

	private final int maxMagiaFlux = 1000000; 	// 最大MF量を設定
	private static final int SHRINK_MF = SMConfig.magiaLightRange / 4;

	@Override
	public void serverUpdate() {

		super.serverUpdate();

		// 一定時間経てば処理
		if (this.getTime() % 20 != 0 || this.isMfEmpty() || !this.isActive(this.pos) || WorldHelper.isPeaceful(this.world)) { return; }

		this.setMF(this.getMF() - this.getShrinkMF());
		this.sentClient();
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 10000;
    }

	// 一度に消費するMF
	public int getShrinkMF () {
		return SHRINK_MF;
	}

	public int getRange () {
		return SMConfig.magiaLightRange;
	}
}
