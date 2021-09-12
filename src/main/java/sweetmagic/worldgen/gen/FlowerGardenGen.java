package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import sweetmagic.init.BiomeInit;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFlowerGen;

public class FlowerGardenGen extends BaseFlowerGen {

	public FlowerGardenGen(int seedRand) {
		this.seedRand = seedRand;
	}

	// バイオーム確認
	public boolean checkBiome (Biome biome) {
		return biome != BiomeInit.FLOWERGARDEN && biome != BiomeInit.FLOWERVALLEY;
	}

	// IBlockStateの取得
	public IBlockState getState () {

		this.maxY = 31;

		switch (this.rand.nextInt(20)) {
		case 0:
			return BlockInit.lily_valley.getDefaultState();
		case 1:
			return BlockInit.cornflower.getDefaultState();
		case 2:
			return BlockInit.cosmos.getDefaultState();
		case 3:
			return BlockInit.blackrose.getDefaultState();
		case 4:
			return BlockInit.white_clover.getDefaultState();
		case 5:
			return BlockInit.foxtail_grass.getDefaultState();
		case 6:
			return BlockInit.ultramarine_rose.getDefaultState();
		case 7:
			return BlockInit.iberis_umbellata.getDefaultState();
		case 8:
			return BlockInit.solid_star.getDefaultState();
		case 9:
			return BlockInit.zinnia.getDefaultState();
		case 10:
			return BlockInit.campanula.getDefaultState();
		case 11:
			return BlockInit.primula_polyansa.getDefaultState();
		case 12:
			return BlockInit.hydrangea.getDefaultState();
		case 13:
			return BlockInit.carnation_crayola.getDefaultState();
		case 14:
			return BlockInit.christmas_rose.getDefaultState();
		case 15:
			return BlockInit.turkey_balloonflower.getDefaultState();
		case 16:
			return BlockInit.portulaca.getDefaultState();
		case 17:
			return BlockInit.surfinia.getDefaultState();
		case 18:
			return BlockInit.pansy_yellowmazenta.getDefaultState();
		case 19:
			return BlockInit.pansy_blue.getDefaultState();
		}

		return BlockInit.lily_valley.getDefaultState();
	}

	// 花の生成
	public void genFlower (World world, Random rand, int posX, int posZ, IBlockState state) {

		// チャンス
		for (int k = 0; k < 6; k++) {

			int randX = posX + rand.nextInt(16);
			int y = rand.nextInt(this.maxY) + this.minY;
			int randZ = posZ + rand.nextInt(16);
			IBlockState state1 = this.getState();

			// 花の塊
			for (int i = 0; i < 32; i++) {

				int pX = randX + rand.nextInt(8) - rand.nextInt(8);
				int pY = y + rand.nextInt(4) - rand.nextInt(4);
				int pZ = randZ + rand.nextInt(8) - rand.nextInt(8);

				BlockPos pos = new BlockPos (pX, pY, pZ);
				IBlockState state2 = world.getBlockState(pos);
				if (!this.checkSetBlock(world, pos, state2)) { continue; }

				world.setBlockState(pos.up(), state1, 2);
			}
		}
	}

	// 植えれるかのチェック
	public boolean checkSetBlock (World world, BlockPos pos, IBlockState state) {
		Block block = world.getBlockState(pos.up()).getBlock();
		return world.canSeeSky(pos.up()) && ( block == Blocks.AIR || block == Blocks.SNOW_LAYER) && state.getBlock() == Blocks.GRASS;
	}
}
