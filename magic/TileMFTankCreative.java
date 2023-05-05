package sweetmagic.init.tile.magic;

public class TileMFTankCreative extends TileMFTank {

	public int maxMagiaFlux = 2000000000;	// 最大MF量を設定

	// 最大MF量を設定
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

	// MF量を設定
	@Override
	public int getMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 100000;
    }

    // クリエ機能
	@Override
	public boolean isCreative () {
		return true;
	}
}
