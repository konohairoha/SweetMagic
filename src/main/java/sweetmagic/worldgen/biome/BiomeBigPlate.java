package sweetmagic.worldgen.biome;

import java.util.Random;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class BiomeBigPlate extends Biome {

	public static final WorldGenTrees ORK = new WorldGenTrees(false);

	public BiomeBigPlate() {
        super(new BiomeProperties("BigPlate").setTemperature(0.5F).setBaseHeight(0.9F).setHeightVariation(0.01F).setRainfall(0.25F).setSnowEnabled());
        this.setRegistryName("BigPlate");
        this.decorator.treesPerChunk = 0;
        this.decorator.grassPerChunk = 2;
        this.decorator.flowersPerChunk = 0;
        this.spawnableMonsterList.clear();
    }

    // チャンス
	@Override
	public float getSpawningChance() {
		return 0.025F;
	}

	// 木の生成
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
		return (WorldGenAbstractTree) ORK;
	}
}
