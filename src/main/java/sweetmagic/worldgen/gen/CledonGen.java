package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.init.base.BaseFlowerGen;
import sweetmagic.util.SweetState;

public class CledonGen extends BaseFlowerGen {

	public CledonGen(Block block) {
		super(block);
		this.minY = 75;
		this.maxY = 100;
	}

	// 花の生成
	public void genFlower (World world, Random rand, int posX, int posZ, IBlockState state) {

		// 高度選定
		for (int y = this.minY; y < this.maxY; y++) {

			//生成頻度
			if (rand.nextInt(4) != 0) { continue; }

			BlockPos pos = new BlockPos(posX + rand.nextInt(16) - rand.nextInt(8), y, posZ + rand.nextInt(16) - rand.nextInt(8));
			IBlockState state2 = world.getBlockState(pos);
			if (!this.checkSetBlock(world, pos, state2)) { continue; }
			world.setBlockState(pos.up(), this.state.withProperty(SweetState.STAGE4, 3), 2);
			return;
		}
	}
}
