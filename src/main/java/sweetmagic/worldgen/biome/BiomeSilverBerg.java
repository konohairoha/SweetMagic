package sweetmagic.worldgen.biome;

import java.util.Random;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;

public class BiomeSilverBerg extends Biome {

    protected static final WorldGenBirchTree SUPER_BIRCH_TREE = new WorldGenBirchTree(false, true);

	public BiomeSilverBerg(String name, BiomeProperties property) {
        super(property);
        this.decorator.treesPerChunk = 0;
        this.decorator.grassPerChunk = 2;
        this.setRegistryName(name);
    }

	// 木の生成
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
		return (WorldGenAbstractTree) SUPER_BIRCH_TREE;
	}

    // チャンス
	@Override
	public float getSpawningChance() {
		return 0.075F;
	}
}
