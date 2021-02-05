package sweetmagic.util;

import net.minecraft.util.math.BlockPos;

public class GenHelper {

	public final int dimId;
	public final BlockPos pos;

	public GenHelper (int dimId, BlockPos pos) {
		this.dimId = dimId;
		this.pos = pos;
	}

	// ディメンションID取得
	public int getDimId () {
		return this.dimId;
	}

	// 座標取得
	public BlockPos getPos () {
		return this.pos;
	}
}
