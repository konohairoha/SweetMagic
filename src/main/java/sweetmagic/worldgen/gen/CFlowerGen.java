     package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import sweetmagic.init.BlockInit;

public class CFlowerGen implements IWorldGenerator {

	//お飾り用お花
	private Block flower;
	private IBlockState state;
	private static Random rand;

	public CFlowerGen(Block flower) {
		this.setGeneratedBlock(flower);
	}

	public void setGeneratedBlock(Block flower) {
		this.flower = flower;
		this.state = flower.getDefaultState();
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator gen, IChunkProvider pro) {

		//ネザー、エンドでは生成しない
		int genDim1 = world.provider.getDimension();
		if ((genDim1 == 1 || genDim1 == -1)) { return; }

		boolean islily = this.flower == BlockInit.lily_valley;

		this.rand = new Random(world.getSeed() + chunkX + chunkZ * (islily ? 3 : 24) );
		int posX = chunkX << 4;
		int posZ = chunkZ << 4;
		posX = posX + 8 + this.rand.nextInt(6) - this.rand.nextInt(6);
		posZ = posZ + 8 + this.rand.nextInt(6) - this.rand.nextInt(6);

		BlockPos p = new BlockPos(posX, 60, posZ);
		Biome biome = world.getBiome(p);
		if (biome == Biomes.SWAMPLAND || biome == Biomes.DESERT ||
				biome == Biomes.MUSHROOM_ISLAND || biome == Biomes.MUSHROOM_ISLAND_SHORE) { return; }

		// チャンス
		for (int k = 0; k < 6; k++) {

			int y = rand.nextInt(20) + 55;
			BlockPos p1 = new BlockPos(posX, y, posZ);
			IBlockState state = world.getBlockState(p1);

			if (!this.checkSetBlock(world, p1, state)) { continue; }

			// 花の塊
			for (int i = 0; i < 6; i++) {

				int pX = rand.nextInt(8) - rand.nextInt(8);
				int pY = rand.nextInt(4) - rand.nextInt(4);
				int pZ = rand.nextInt(8) - rand.nextInt(8);

				BlockPos p2 = p1.add(pX, pY, pZ);
				IBlockState state2 = world.getBlockState(p2);

				if (!this.checkSetBlock(world, p1, state2)) { continue; }

				world.setBlockState(p2.up(), this.state, 3);
			}
		}
	}

	public boolean checkSetBlock (World world, BlockPos p1, IBlockState state) {
		return world.canSeeSky(p1.up()) && state.getMaterial() == Material.GRASS &&
				world.isAirBlock(p1.up()) && ((BlockBush) this.flower).canBlockStay(world, p1, this.state);
	}
}
