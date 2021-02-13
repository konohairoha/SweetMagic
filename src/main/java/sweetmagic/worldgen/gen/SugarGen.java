package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import sweetmagic.init.base.BaseFlowerGen;
import sweetmagic.util.SweetState;

public class SugarGen extends BaseFlowerGen {

	public int chance = 8;

	public SugarGen(Block flower, int seedRand) {
		super(flower);
		this.seedRand = seedRand;
	}

	public SugarGen(Block flower, int seedRand, int chance) {
		super(flower);
		this.seedRand = seedRand;
		this.chance = chance;
	}

	// バイオーム確認
	public boolean checkBiome (Biome biome) {
		return !BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS);
	}

	// 花の生成
	public void genFlower (World world, Random rand, int posX, int posZ, IBlockState state) {

		boolean isSun = this.seedRand == 16;
		if (!isSun && world.rand.nextInt(3) != 0) { return; }

		boolean isRas = this.chance != 8;
		int size = isSun || isRas ? 16 : 8;
		IBlockState state1 = isRas ? state.withProperty(SweetState.STAGE6, 5) : state.withProperty(SweetState.STAGE4, 3);

		// チャンス
		for (int k = 0; k < 2; k++) {

			int randX = posX + rand.nextInt(16);
			int y = rand.nextInt(this.maxY) + this.minY;
			int randZ = posZ + rand.nextInt(16);

			// 花の塊
			for (int i = 0; i < size; i++) {

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
	public boolean checkSetBlock (World world, BlockPos p1, IBlockState state) {
		return world.canSeeSky(p1.up()) && state.getBlock() == Blocks.GRASS;
	}
}
