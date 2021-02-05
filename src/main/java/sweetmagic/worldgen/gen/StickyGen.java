package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import sweetmagic.util.SweetState;

public class StickyGen implements IWorldGenerator {

	private Block flower;
	private IBlockState state;
	private static Random rand;

	public StickyGen(Block troom) {
		this.setGeneratedBlock(troom);
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

		this.rand = new Random(world.getSeed() + chunkX + chunkZ * 16);
		int posX = chunkX << 4;
		int posZ = chunkZ << 4;
		posX = posX + 8 + this.rand.nextInt(8) - this.rand.nextInt(8);
		posZ = posZ + 8 + this.rand.nextInt(8) - this.rand.nextInt(8);

		BlockPos p = new BlockPos(posX, 60, posZ);
		if (world.getBiome(p) != Biomes.SWAMPLAND) { return; }

		// 高度選定
		for (int y = 63; y < 72; y++) {

			BlockPos p1 = new BlockPos(posX, y, posZ);
			IBlockState state = world.getBlockState(p1);

			if (world.canSeeSky(p1.up()) && state.getMaterial() == Material.GRASS &&
					world.isAirBlock(p1.up()) && ((BlockBush) this.flower).canBlockStay(world, p1, this.state)) {

				if (rand.nextInt(2) != 0) { continue; }
				world.setBlockState(p1.up(), this.state.withProperty(SweetState.STAGE5, 4), 3);
				return;
			}
		}
	}
}
