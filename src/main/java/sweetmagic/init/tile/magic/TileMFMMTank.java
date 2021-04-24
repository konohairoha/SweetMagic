package sweetmagic.init.tile.magic;

public class TileMFMMTank extends TileMFTank {

	public int maxMagiaFlux = 100000000;	// 最大MF量を設定

	// 最大MF量を設定
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 20000;
    }
}
