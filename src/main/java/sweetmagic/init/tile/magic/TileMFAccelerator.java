package sweetmagic.init.tile.magic;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;

public class TileMFAccelerator extends TileMFBase {

	public int maxMagiaFlux = 100000; 	// 最大MF量を設定
	public int chargeMF = 10000; 			// 一度に補給できるMF;
	private static final int RANGE = 5;
	public static final int USEMF = 1;

	private static final List<Block> blockList = Arrays.<Block> asList(
		Blocks.AIR, BlockInit.pedestal_creat, BlockInit.magia_accelerator
	);

	@Override
	public void update() {

		super.update();
		if (this.getTime() % 2 != 0 || !this.canUpdate() || !this.isActive(this.world, this.pos)) { return; }

		// 座標リストの取得
		Iterable<BlockPos> posList = BlockPos.getAllInBox(this.pos.add(-RANGE, 0, -RANGE), this.pos.add(RANGE, RANGE, RANGE));
		int sumMF = 0;

		for (BlockPos p : posList) {

			// 空気ブロックかMFブロック以外なら次へ
			Block block = this.getBlock(p);
			if ( blockList.contains(block) || !(block instanceof BaseMFBlock) ) { continue; }

			// MFBase以外のtileかアクセラレータなら次へ
			TileEntity tile = this.world.getTileEntity(p);
			if ( !(tile instanceof TileMFBase) ) { continue; }

			// 送信側なら次へ
			TileMFBase mfBase = (TileMFBase) tile;
			if (!mfBase.getReceive()) { continue; }

			// アップデートの呼び出し
			mfBase.update();
			sumMF += USEMF;

			// MFが足りなくなったら終了
			if (this.getMF() <= sumMF || !this.canUpdate()) { break; }
		}

		// 消費MFが0より大きいなら
		if (this.isSever() && sumMF > 0) {
			this.setMF(this.getMF() - sumMF);
			this.sentClient();
		}
	}

	// MFがあるかどうか
	public boolean canUpdate () {
		return this.getMF() >= USEMF;
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return this.chargeMF;
    }
}
