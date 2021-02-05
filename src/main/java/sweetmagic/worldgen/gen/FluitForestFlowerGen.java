package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import sweetmagic.init.BiomeInit;
import sweetmagic.init.BlockInit;
import sweetmagic.util.SweetState;

public class FluitForestFlowerGen implements IWorldGenerator {

	//お飾り用お花
	private Block flower;
	private IBlockState state;
	private static Random rand;

	public FluitForestFlowerGen() {
	}

	// IBlockStateの取得
	public IBlockState getState (Biome biome) {

		IBlockState state = null;

		// フルーツの森
		if (biome == BiomeInit.FLUITFOREST) {

			switch (this.rand.nextInt(5)) {
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
			}
		}

		// プリズムフォレスト
		else if (biome == BiomeInit.PRISMFOREST) {
			switch (this.rand.nextInt(5)) {
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
			}
		}

		return state;
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator gen, IChunkProvider pro) {

		//ネザー、エンドでは生成しない
		int genDim1 = world.provider.getDimension();
		if ((genDim1 == 1 || genDim1 == -1)) { return; }

		this.rand = new Random(world.getSeed() + chunkX + chunkZ * 5);
		int posX = chunkX << 4;
		int posZ = chunkZ << 4;
		posX = posX + 8 + this.rand.nextInt(6) - this.rand.nextInt(6);
		posZ = posZ + 8 + this.rand.nextInt(6) - this.rand.nextInt(6);

		BlockPos p = new BlockPos(posX, 60, posZ);
		Biome biome = world.getBiome(p);
		if (biome != BiomeInit.FLUITFOREST && biome != BiomeInit.PRISMFOREST) { return; }

		this.state = this.getState(biome);

		// チャンス
		for (int k = 0; k < 8; k++) {

			int y = rand.nextInt(20) + 55;
			BlockPos p1 = new BlockPos(posX, y, posZ);
			IBlockState state = world.getBlockState(p1);
			if (!this.checkSetBlock(world, p1, state)) { continue; }

			// 花の塊
			for (int i = 0; i < 12; i++) {

				int pX = rand.nextInt(8) - rand.nextInt(8);
				int pY = rand.nextInt(4) - rand.nextInt(4);
				int pZ = rand.nextInt(8) - rand.nextInt(8);

				BlockPos p2 = p1.add(pX, pY, pZ);
				IBlockState state2 = world.getBlockState(p2);
				if (!this.checkSetBlock(world, p1, state2)) { continue; }

				world.setBlockState(p2.up(), this.state, 2);
			}
		}
	}

	// 植えれるかのチェック
	public boolean checkSetBlock (World world, BlockPos p1, IBlockState state) {
		return world.canSeeSky(p1.up()) && state.getBlock() == Blocks.GRASS;
	}
}