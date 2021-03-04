package sweetmagic.worldgen.biome;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import sweetmagic.init.BlockInit;
import sweetmagic.worldgen.gen.WorldGenCoconutTree;

public class BiomeCoconutDesert extends Biome {

	public static final WorldGenerator FLOWER =  new WorldGenCoconutTree(true, BlockInit.coconut_log, BlockInit.coconut_leaves, BlockInit.coconut_plant, 0);

	public BiomeCoconutDesert(String name, BiomeProperties property) {
        super(property);
        this.decorator.treesPerChunk = 0;
      this.decorator.grassPerChunk = 2;
        this.setRegistryName(name);
        this.topBlock = Blocks.SAND.getDefaultState();
        this.fillerBlock = Blocks.SAND.getDefaultState();
    }

    public BiomeCoconutDesert() {
        super(new BiomeProperties("CoconutDesert").setTemperature(1.25F).setBaseHeight(0.2F).setHeightVariation(0.2F).setRainfall(0.8F).setSnowEnabled());
        this.decorator.treesPerChunk = 0;
//        this.decorator.grassPerChunk = 2;
        this.setRegistryName("CoconutDesert");
        this.topBlock = Blocks.SAND.getDefaultState();
        this.fillerBlock = Blocks.SAND.getDefaultState();
    }

    // チャンス
	@Override
	public float getSpawningChance() {
		return 0.025F;
	}

	// 木の生成
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
		return (WorldGenAbstractTree) FLOWER;
	}
}
