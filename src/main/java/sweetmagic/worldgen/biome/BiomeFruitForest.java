package sweetmagic.worldgen.biome;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.worldgen.gen.WorldGenEstor;
import sweetmagic.worldgen.gen.WorldGenFruitTree;
import sweetmagic.worldgen.gen.WorldGenPrsmTree;

public class BiomeFruitForest extends Biome {

	private static final WorldGenerator ORANGE = new WorldGenFruitTree(false, BlockInit.orange_log, BlockInit.orange_leaves);
	private static final WorldGenerator CHESTNUT = new WorldGenFruitTree(false, BlockInit.chestnut_log, BlockInit.chestnut_leaves);
	private static final WorldGenerator LEMON = new WorldGenFruitTree(false, BlockInit.lemon_log, BlockInit.lemon_leaves);
	private static final WorldGenerator ESTOR = new WorldGenEstor(false, BlockInit.estor_log, BlockInit.estor_leaves);
	private static final WorldGenerator PEACH = new WorldGenFruitTree(false, BlockInit.peach_log, BlockInit.peach_leaves);
	private static final WorldGenBirchTree BIRCH= new WorldGenBirchTree(false, false);
	private static final WorldGenTrees ORK = new WorldGenTrees(false);
	private boolean isTall = false;

	private static final WorldGenerator ORANGE_TALL = new WorldGenPrsmTree(BlockInit.orange_log, BlockInit.orange_leaves, false);
	private static final WorldGenerator CHESTNUT_TALL = new WorldGenPrsmTree(BlockInit.chestnut_log, BlockInit.chestnut_leaves, false);
	private static final WorldGenerator LEMON_TALL = new WorldGenPrsmTree(BlockInit.lemon_log, BlockInit.lemon_leaves, false);
	private static final WorldGenerator ESTOR_TALL = new WorldGenPrsmTree(BlockInit.estor_log, BlockInit.estor_leaves, false);
	private static final WorldGenerator PEACH_TALL = new WorldGenPrsmTree(BlockInit.peach_log, BlockInit.peach_leaves, false);

	public BiomeFruitForest(String name, BiomeProperties property, boolean isTall) {
        super(property);
        this.decorator.treesPerChunk = 3;
        this.decorator.grassPerChunk = 2;
        this.setRegistryName(name);
        this.isTall = isTall;
    }

    public BiomeFruitForest() {
        super(new BiomeProperties("FluitForest").setTemperature(1F).setBaseHeight(0.2F).setHeightVariation(0.2F).setRainfall(0.8F).setSnowEnabled());
        this.decorator.treesPerChunk = 2;
        this.decorator.grassPerChunk = 2;
        this.setRegistryName("FluitForest");
    }

    // チャンス
	@Override
	public float getSpawningChance() {
		return 0.075F;
	}

	// 木の生成
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {

		if (this.isTall) {

			switch (rand.nextInt(5)) {
			case 0:
				return (WorldGenAbstractTree) ORANGE_TALL;
			case 1:
				return (WorldGenAbstractTree) CHESTNUT_TALL;
			case 2:
				return (WorldGenAbstractTree) LEMON_TALL;
			case 3:
				return (WorldGenAbstractTree) ESTOR_TALL;
			case 4:
				return (WorldGenAbstractTree) PEACH_TALL;
			}
		}

		else {
			switch (rand.nextInt(7)) {
			case 0:
				return (WorldGenAbstractTree) ORANGE;
			case 1:
				return (WorldGenAbstractTree) CHESTNUT;
			case 2:
				return (WorldGenAbstractTree) LEMON;
			case 3:
				return (WorldGenAbstractTree) BIRCH;
			case 4:
				return (WorldGenAbstractTree) ORK;
			case 5:
				return (WorldGenAbstractTree) ESTOR;
			case 6:
				return (WorldGenAbstractTree) PEACH;
			}
		}

		return (WorldGenAbstractTree) ORANGE;
	}

	// 草の色
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos) {
		int i = super.getGrassColorAtPos(pos);
		return this.isTall ? i : (i & 16711422) + 2634762 >> 1;
    }
}
