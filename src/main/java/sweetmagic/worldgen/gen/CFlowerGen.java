     package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.config.SMConfig;
import sweetmagic.init.BlockInit;
import sweetmagic.init.DimensionInit;
import sweetmagic.init.base.BaseFlowerGen;

public class CFlowerGen extends BaseFlowerGen {

	public CFlowerGen(int seedRand) {
		this.seedRand = seedRand;
	}

	// IBlockStateの取得
	public IBlockState getState () {

		IBlockState state = null;

		switch (this.rand.nextInt(17)) {
		case 0:
			state = BlockInit.lily_valley.getDefaultState();
			break;
		case 1:
			state = BlockInit.cornflower.getDefaultState();
			break;
		case 2:
			state = BlockInit.cosmos.getDefaultState();
			break;
		case 3:
			state = BlockInit.blackrose.getDefaultState();
			break;
		case 4:
			state = BlockInit.white_clover.getDefaultState();
			break;
		case 5:
			state = BlockInit.foxtail_grass.getDefaultState();
			break;
		case 6:
			state = BlockInit.ultramarine_rose.getDefaultState();
			break;
		case 7:
			state = BlockInit.iberis_umbellata.getDefaultState();
			break;
		case 8:
			state = BlockInit.solid_star.getDefaultState();
			break;
		case 9:
			state = BlockInit.zinnia.getDefaultState();
			break;
		case 10:
			state = BlockInit.campanula.getDefaultState();
			break;
		case 11:
			state = BlockInit.primula_polyansa.getDefaultState();
			break;
		case 12:
			state = BlockInit.hydrangea.getDefaultState();
			break;
		case 13:
			state = BlockInit.carnation_crayola.getDefaultState();
			break;
		case 14:
			state = BlockInit.christmas_rose.getDefaultState();
			break;
		case 15:
			state = BlockInit.turkey_balloonflower.getDefaultState();
			break;
		case 16:
			state = BlockInit.portulaca.getDefaultState();
			break;
		}

		return state;
	}

	// 花の生成
	public void genFlower (World world, Random rand, int posX, int posZ, IBlockState state) {

		// チャンス
		for (int k = 0; k < 8; k++) {

			int randX = posX + rand.nextInt(16);
			int y = rand.nextInt(this.maxY) + this.minY;
			int randZ = posZ + rand.nextInt(16);
			IBlockState state1 = this.getState();

			// 花の塊
			for (int i = 0; i < 16; i++) {

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

	// ディメンションチェック
	public boolean checkDimeintion (int dim) {

		if (!SMConfig.genFlowers) {
			return dim != DimensionInit.dimID;
		}

		else {
			return dim == 1 || dim == -1;
		}
	}

	// 植えれるかのチェック
	public boolean checkSetBlock (World world, BlockPos pos, IBlockState state) {
		Block block = world.getBlockState(pos.up()).getBlock();
		return world.canSeeSky(pos.up()) && ( block == Blocks.AIR || block == Blocks.SNOW_LAYER) && state.getBlock() == Blocks.GRASS;
	}
}
