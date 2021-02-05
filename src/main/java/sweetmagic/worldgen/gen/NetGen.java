package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import sweetmagic.util.SweetState;

public class NetGen implements IWorldGenerator {

	private Block flower;
	private IBlockState state;
	private static Random rand;

	public NetGen(Block flower) {
		this.setGeneratedBlock(flower);
	}

	public void setGeneratedBlock(Block flower) {
		this.flower = flower;
		this.state = flower.getDefaultState();
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator gen, IChunkProvider pro) {

		this.rand = new Random(world.getSeed() + chunkX + chunkZ * 16);

		int posX = chunkX << 4;
		int posZ = chunkZ << 4;
		posX = posX + 8 + this.rand.nextInt(8) - this.rand.nextInt(8);
		posZ = posZ + 8 + this.rand.nextInt(8) - this.rand.nextInt(8);

		// 高度選定
		for (int y = 20; y < 45; y++) {

			BlockPos p1 = new BlockPos(posX, y, posZ);
			IBlockState state = world.getBlockState(p1);
			Material material = state.getMaterial();

			if (( material == Material.ROCK || material == Material.GROUND ) &&
					world.isAirBlock(p1.up()) && ((BlockBush) this.flower).canBlockStay(world, p1, this.state)) {

				//生成頻度
				if (rand.nextInt(2) != 0)  { continue; }
				world.setBlockState(p1.up(), this.state.withProperty(SweetState.STAGE4, 3), 3);
				return;
			}
		}
	}
}
