package sweetmagic.worldgen.biome;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.worldgen.gen.WorldGenEstor;
import sweetmagic.worldgen.gen.WorldGenFruitTree;

public class BiomeDryFruitForest extends Biome {

	private static final WorldGenerator ORANGE = new WorldGenFruitTree(false, BlockInit.orange_log, Blocks.AIR);
	private static final WorldGenerator CHESTNUT = new WorldGenFruitTree(false, BlockInit.chestnut_log, Blocks.AIR);
	private static final WorldGenerator LEMON = new WorldGenFruitTree(false, BlockInit.lemon_log, Blocks.AIR);
	private static final WorldGenerator ESTOR = new WorldGenEstor(false, BlockInit.estor_log, Blocks.AIR);
	private static final WorldGenerator PEACH = new WorldGenFruitTree(false, BlockInit.peach_log, Blocks.AIR);

    public BiomeDryFruitForest() {
        super(new BiomeProperties("DryFruitForest").setTemperature(1F).setBaseHeight(0.2F).setHeightVariation(0.2F).setRainfall(0.8F).setSnowEnabled());
        this.decorator.treesPerChunk = 2;
        this.decorator.grassPerChunk = 0;
        this.setRegistryName("DryFruitForest");
        this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.spawnableCreatureList.clear();
    }

	// 木の生成
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {

		switch (rand.nextInt(5)) {
		case 0:
			return (WorldGenAbstractTree) ORANGE;
		case 1:
			return (WorldGenAbstractTree) CHESTNUT;
		case 2:
			return (WorldGenAbstractTree) LEMON;
		case 3:
			return (WorldGenAbstractTree) ESTOR;
		case 4:
			return (WorldGenAbstractTree) PEACH;
		}

		return (WorldGenAbstractTree) ORANGE;
	}

	// 草の色
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos) {
		int i = super.getGrassColorAtPos(pos);
		return (i & 16711422) + 2634762 >> 1;
    }
}
