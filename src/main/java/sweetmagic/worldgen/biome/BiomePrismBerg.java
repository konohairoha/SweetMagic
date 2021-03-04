package sweetmagic.worldgen.biome;

import java.util.Random;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import sweetmagic.init.BlockInit;
import sweetmagic.worldgen.gen.WorldGenPrsmTree;

public class BiomePrismBerg extends Biome {

	public static final WorldGenerator PRISM = new WorldGenPrsmTree(BlockInit.prism_log, BlockInit.prism_leaves, false);

	public BiomePrismBerg(String name, BiomeProperties property) {
        super(property);
        this.decorator.treesPerChunk = 0;
        this.decorator.grassPerChunk = 2;
        this.setRegistryName(name);
    }

    public BiomePrismBerg(String name) {
        super(new BiomeProperties(name).setTemperature(-1F).setBaseHeight(0.4F).setHeightVariation(1.3F).setRainfall(0.6F));
        this.decorator.treesPerChunk = 0;
        this.decorator.grassPerChunk = 2;
        this.setRegistryName(name);
    }

    // チャンス
	@Override
	public float getSpawningChance() {
		return 0.005F;
	}

	// 木の生成
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
		return (WorldGenAbstractTree) PRISM;
	}

//	// 草の色
//	@Override
//	@SideOnly(Side.CLIENT)
//	public int getGrassColorAtPos(BlockPos pos) {
//		return 0xafd8f7;
//    }
}
