package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import sweetmagic.init.base.BaseFlowerGen;

public class SnowDropGen extends BaseFlowerGen {

	public SnowDropGen(Block flower, int seedRand) {
		super(flower);
		this.seedRand = seedRand;
		this.maxY = 31;
	}

	// バイオーム確認
	public boolean checkBiome (Biome biome) {
		return biome.getDefaultTemperature() > 0;
	}

	// 花の生成
	public void genFlower (World world, Random rand, int posX, int posZ, IBlockState state) {

		// チャンス
		for (int k = 0; k < 4; k++) {

			int randX = posX + rand.nextInt(16);
			int y = rand.nextInt(this.maxY) + this.minY;
			int randZ = posZ + rand.nextInt(16);

			// 花の塊
			for (int i = 0; i < 8; i++) {

				int pX = randX + rand.nextInt(8) - rand.nextInt(8);
				int pY = y + rand.nextInt(4) - rand.nextInt(4);
				int pZ = randZ + rand.nextInt(8) - rand.nextInt(8);

				BlockPos pos = new BlockPos (pX, pY, pZ);
				IBlockState state2 = world.getBlockState(pos);
				if (!this.checkSetBlock(world, pos, state2)) { continue; }

				world.setBlockState(pos.up(), this.state, 2);
			}
		}
	}

	// 植えれるかのチェック
	public boolean checkSetBlock (World world, BlockPos pos, IBlockState state) {
		Block block = world.getBlockState(pos.up()).getBlock();
		return world.canSeeSky(pos.up()) && ( block == Blocks.AIR || block == Blocks.SNOW_LAYER) && state.getBlock() == Blocks.GRASS;
	}
}
