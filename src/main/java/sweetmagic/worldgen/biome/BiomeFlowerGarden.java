package sweetmagic.worldgen.biome;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import sweetmagic.init.BlockInit;
import sweetmagic.util.SweetState;
import sweetmagic.worldgen.gen.WorldGenEstor;

public class BiomeFlowerGarden extends Biome {

	public static final WorldGenerator FLOWER = new WorldGenEstor(true, BlockInit.estor_log, BlockInit.estor_leaves);
	public static final WorldGenBirchTree BIRCH= new WorldGenBirchTree(false, false);
	public static final WorldGenTrees ORK = new WorldGenTrees(false);
    protected static final WorldGenBigTree BIG = new WorldGenBigTree(false);
	public boolean isForest = false;

	public BiomeFlowerGarden(String name, BiomeProperties property) {
        super(property);
        this.decorator.treesPerChunk = 0;
        this.decorator.grassPerChunk = 3;
        this.decorator.flowersPerChunk = 0;
        this.setRegistryName(name);
        this.addDefaultFlowers();
    }

    public BiomeFlowerGarden() {
        super(new BiomeProperties("FlowerGarden").setTemperature(1F).setBaseHeight(0.2F).setHeightVariation(0.2F).setRainfall(0.8F).setSnowEnabled());
        this.decorator.treesPerChunk = 0;
        this.decorator.flowersPerChunk = 0;
        this.setRegistryName("FlowerGarden");
        this.addDefaultFlowers();
    }

    public BiomeFlowerGarden(String name) {
        super(new BiomeProperties(name).setTemperature(1F).setBaseHeight(0.6F).setRainfall(0.8F).setSnowEnabled());
        this.decorator.treesPerChunk = 10;
        this.decorator.flowersPerChunk = 0;
        this.setRegistryName(name);
        this.isForest = true;
    }

    // チャンス
	@Override
	public float getSpawningChance() {
		return 0.025F;
	}

	// 木の生成
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {

		if (!this.isForest) {
			return (WorldGenAbstractTree) FLOWER;
		}

		int chance = rand.nextInt(12);

		switch (chance) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			return (WorldGenAbstractTree) FLOWER;
		case 5:
		case 6:
		case 7:
			return (WorldGenAbstractTree) ORK;
		case 8:
		case 9:
		case 10:
			return (WorldGenAbstractTree) BIRCH;
		case 11:
			return (WorldGenAbstractTree) BIG;
		}

		return (WorldGenAbstractTree) FLOWER;
	}

	@Override
	public void addDefaultFlowers() {

		flowers.clear();
		this.addFlower(BlockInit.lily_valley.getDefaultState(), 10);
		this.addFlower(BlockInit.cornflower.getDefaultState(), 10);
		this.addFlower(BlockInit.blueberry_plant.getDefaultState().withProperty(SweetState.STAGE5, 4), 6);
		this.addFlower(BlockInit.raspberry_plant.getDefaultState().withProperty(SweetState.STAGE6, 5), 6);
		this.addFlower(BlockInit.olive_plant.getDefaultState().withProperty(SweetState.STAGE5, 4), 6);
		this.addFlower(BlockInit.sugarbell_plant.getDefaultState().withProperty(SweetState.STAGE4, 3), 10);
		this.addFlower(BlockInit.sannyflower_plant.getDefaultState().withProperty(SweetState.STAGE4, 3), 2);
		this.addFlower(BlockInit.clerodendrum.getDefaultState().withProperty(SweetState.STAGE4, 3), 15);
		this.addFlower(BlockInit.fire_nasturtium_plant.getDefaultState().withProperty(SweetState.STAGE4, 3), 10);
		this.addFlower(BlockInit.glowflower_plant.getDefaultState().withProperty(SweetState.STAGE4, 3), 20);
	}

	public void plantFlower(World world, Random rand, BlockPos pos) {

		FlowerEntry flower = (FlowerEntry) WeightedRandom.getRandomItem(rand, flowers);
		if (!((BlockBush) flower.state.getBlock()).canBlockStay(world, pos, flower.state)) {
			return;
		}

		world.setBlockState(pos, flower.state, 3);
	}
}
