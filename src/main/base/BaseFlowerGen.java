package sweetmagic.init.base;

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

public class BaseFlowerGen implements IWorldGenerator {

	//お飾り用お花
	public IBlockState state;
	public Block flower;
	public Random rand;
	public int seedRand = 10;
	public int minY = 63;
	public int maxY = 16;

	public BaseFlowerGen() {
	}

	public BaseFlowerGen(Block flower) {
		this.flower = flower;
		this.state = flower.getDefaultState();
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator gen, IChunkProvider pro) {

		//ネザー、エンドでは生成しない
		int dimId = world.provider.getDimension();
		if (this.checkDimeintion(dimId)) { return; }

		this.rand = this.getRand(world, chunkX, chunkZ);
		int posX = (chunkX * 16) + 8;
		int posZ = (chunkZ * 16) + 8;
		Biome bio = world.getBiome(new BlockPos(posX, 60, posZ));
		if (this.checkBiome(bio)) { return; }

		this.genFlower(world, this.rand, posX, posZ, this.state);
	}

	// ディメンションチェック
	public boolean checkDimeintion (int dim) {
		return dim == 1 || dim == -1;
	}

	// バイオーム確認
	public boolean checkBiome (Biome biome) {
		return biome == Biomes.SWAMPLAND || biome == Biomes.DESERT ||
				biome == Biomes.MUSHROOM_ISLAND || biome == Biomes.MUSHROOM_ISLAND_SHORE;
	}

	// 乱数チェック
	public Random getRand (World world, int chunkX, int chunkZ) {
		return new Random(world.getSeed() + chunkX + chunkZ * this.seedRand);
	}

	// 花の生成
	public void genFlower (World world, Random rand, int posX, int posZ, IBlockState state) {

		// チャンス
		for (int k = 0; k < 2; k++) {

			int randX = posX + rand.nextInt(16);
			int y = rand.nextInt(this.maxY) + this.minY;
			int randZ = posZ + rand.nextInt(16);

			// 花の塊
			for (int i = 0; i < 16; i++) {

				int pX = randX + rand.nextInt(8) - rand.nextInt(8);
				int pY = y + rand.nextInt(4) - rand.nextInt(4);
				int pZ = randZ + rand.nextInt(8) - rand.nextInt(8);

				BlockPos pos = new BlockPos (pX, pY, pZ);
				IBlockState state2 = world.getBlockState(pos);
				if (!this.checkSetBlock(world, pos, state2)) { continue; }

				world.setBlockState(pos.up(), state, 2);
			}
		}
	}

	public boolean checkSetBlock (World world, BlockPos pos, IBlockState state) {
		return world.canSeeSky(pos.up()) && state.getMaterial() == Material.GRASS &&
				world.isAirBlock(pos.up()) && ((BlockBush) this.flower).canBlockStay(world, pos, this.state);
	}
}
