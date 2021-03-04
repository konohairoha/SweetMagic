package sweetmagic.worldgen.map;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenRavine;

public class SMMapGenRiver extends MapGenRavine {

	public boolean isACStone(IBlockState state){
		return state.getBlock() instanceof BlockStone;
	}

	@Override
	public void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop) {
		Biome biome = world.getBiome(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
		IBlockState state = data.getBlockState(x, y, z);
		IBlockState top = biome.topBlock;
		IBlockState filler = biome.fillerBlock;

		if (this.isACStone(state) || state.getBlock() == top.getBlock() || state.getBlock() == filler.getBlock())
			if (y - 1 < 10)
				data.setBlockState(x, y, z, FLOWING_LAVA);
			else {
				data.setBlockState(x, y, z, AIR);

				if (foundTop && data.getBlockState(x, y - 1, z).getBlock() == filler.getBlock())
					data.setBlockState(x, y - 1, z, top.getBlock().getDefaultState());
			}
	}
}