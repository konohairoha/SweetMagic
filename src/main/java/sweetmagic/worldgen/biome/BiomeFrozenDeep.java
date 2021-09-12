package sweetmagic.worldgen.biome;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenIceSpike;

public class BiomeFrozenDeep extends Biome {

    private final WorldGenIceSpike iceSpike = new WorldGenIceSpike();

	public BiomeFrozenDeep() {
        super(new BiomeProperties("FrozenDeep").setTemperature(-0.5F).setBaseHeight(-1.75F).setHeightVariation(0.1F));
        this.setRegistryName("FrozenDeep");
		this.spawnableCreatureList.clear();
        this.decorator.grassPerChunk = 1;
        this.decorator.flowersPerChunk = 1;
//        this.topBlock = Blocks.SNOW.getDefaultState();
	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {

		if (rand.nextFloat() <= 0.33F) {

			for (int i = 0; i < 3; ++i) {
				int x = pos.getX() + rand.nextInt(16) + 8;
				int z = pos.getZ() + rand.nextInt(16) + 8;
				BlockPos p = new BlockPos(x, 64, z);

				if (world.canSeeSky(p.down())) {
					world.setBlockState(p.down(), Blocks.SNOW.getDefaultState(), 3);
					this.iceSpike.generate(world, rand, p);
				}
			}
		}

		super.decorate(world, rand, pos);
	}
}
