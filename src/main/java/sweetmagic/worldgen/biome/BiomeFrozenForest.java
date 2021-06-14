package sweetmagic.worldgen.biome;

import java.util.Random;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;

public class BiomeFrozenForest extends Biome {

	public static final WorldGenMegaPineTree ORK = new WorldGenMegaPineTree(false, true);

	public BiomeFrozenForest() {
        super(new BiomeProperties("FrozenForest").setTemperature(-0.1F).setBaseHeight(0.7F).setHeightVariation(0.1F).setRainfall(1F));
        this.setRegistryName("FrozenForest");
        this.decorator.treesPerChunk = 3;
        this.decorator.grassPerChunk = 2;
        this.decorator.flowersPerChunk = 0;
        this.spawnableMonsterList.clear();
    }

	public BiomeFrozenForest(String name, BiomeProperties property) {
		super(property);
		this.setRegistryName(name);
		this.decorator.treesPerChunk = 2;
		this.decorator.grassPerChunk = 2;
        this.decorator.flowersPerChunk = 0;
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
