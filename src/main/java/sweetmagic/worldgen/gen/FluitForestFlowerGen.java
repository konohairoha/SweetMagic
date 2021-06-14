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
import sweetmagic.util.SweetState;

public class FluitForestFlowerGen extends BaseFlowerGen {

	public FluitForestFlowerGen() {
	}

	// バイオーム確認
	public boolean checkBiome (Biome biome) {
		return biome != BiomeInit.FLUITFOREST && biome != BiomeInit.PRISMFOREST && biome != BiomeInit.SLIVERBERG;
	}

	// IBlockStateの取得
	public IBlockState getState (Biome biome) {

		IBlockState state = null;

		// フルーツの森
		if (biome == BiomeInit.FLUITFOREST) {

			this.maxY = 16;

			switch (this.rand.nextInt(7)) {
			case 0:
				state = BlockInit.lily_valley.getDefaultState();
				break;
			case 1:
				state = BlockInit.cornflower.getDefaultState();
				break;
			case 2:
				state = BlockInit.blueberry_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
				break;
			case 3:
				state = BlockInit.raspberry_plant.getDefaultState().withProperty(SweetState.STAGE6, 5);
				break;
			case 4:
				state = BlockInit.olive_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
				break;
			case 5:
				state = BlockInit.blackrose.getDefaultState();
				break;
			case 6:
				state = BlockInit.cosmos.getDefaultState();
				break;
			}
		}

		// プリズムフォレスト
		else if (biome == BiomeInit.PRISMFOREST) {

			this.maxY = 16;
			switch (this.rand.nextInt(6)) {
			case 0:
				state = BlockInit.sugarbell_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
				break;
			case 1:
				state = BlockInit.sannyflower_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
				break;
			case 2:
				state = BlockInit.clerodendrum.getDefaultState().withProperty(SweetState.STAGE4, 3);
				break;
			case 3:
				state = BlockInit.fire_nasturtium_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
				break;
			case 4:
				state = BlockInit.glowflower_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
				break;
			case 5:
				state = BlockInit.goldcrest.getDefaultState();
				break;
			}
		}

		else if (biome == BiomeInit.SLIVERBERG) {
			this.maxY = 40;
			state = BlockInit.goldcrest.getDefaultState();
		}

		return state;
	}

	// 花の生成
	public void genFlower (World world, Random rand, int posX, int posZ, IBlockState state) {

		IBlockState state1 = this.getState(world.getBiome(new BlockPos(posX, 60, posZ)));

		// チャンス
		for (int k = 0; k < 2; k++) {

			int randX = posX + rand.nextInt(16);
			int y = rand.nextInt(this.maxY) + this.minY;
			int randZ = posZ + rand.nextInt(16);

			// 花の塊
			for (int i = 0; i < 24; i++) {

				int pX = randX + rand.nextInt(8) - rand.nextInt(8);
				int pY = y + rand.nextInt(4) - rand.nextInt(4);
				int pZ = randZ + rand.nextInt(8) - rand.nextInt(8);

				BlockPos pos = new BlockPos (pX, pY, pZ);
				IBlockState state2 = world.getBlockState(pos);
				if (!this.checkSetBlock(world, pos, state2)) { continue; }

				world.setBlockState(pos.up(), state1, 2);

				if (state1 == BlockInit.goldcrest.getDefaultState()) {
					world.setBlockState(pos.up(2), state1, 2);
				}
			}
		}
	}

	// 植えれるかのチェック
	public boolean checkSetBlock (World world, BlockPos pos, IBlockState state) {
		Block block = world.getBlockState(pos.up()).getBlock();
		return world.canSeeSky(pos.up()) && ( block == Blocks.AIR || block == Blocks.SNOW_LAYER) && state.getBlock() == Blocks.GRASS;
	}
}