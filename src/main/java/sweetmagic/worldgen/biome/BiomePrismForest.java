package sweetmagic.worldgen.biome;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.worldgen.gen.WorldGenPrsmTree;

public class BiomePrismForest extends Biome {

	public static final WorldGenerator PRISM = new WorldGenPrsmTree(BlockInit.prism_log, BlockInit.prism_leaves, false);
	public static final WorldGenerator PRISM_Mini = new WorldGenPrsmTree(BlockInit.prism_log, BlockInit.prism_leaves, true);

    public BiomePrismForest() {
        super(new BiomeProperties("PrismForest").setTemperature(-0.5F).setBaseHeight(0.2F).setHeightVariation(0.1F).setRainfall(0.6F));
        this.decorator.treesPerChunk = 2;
        this.decorator.grassPerChunk = 2;
        this.setRegistryName("PrismForest");
    }

    // チャンス
	@Override
	public float getSpawningChance() {
		return 0.05F;
	}

	// 木の生成
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {

		switch (rand.nextInt(4)) {
		case 0:
		case 1:
		case 2:
			return (WorldGenAbstractTree) PRISM;
		case 3:
			return (WorldGenAbstractTree) PRISM_Mini;
		}

		return (WorldGenAbstractTree) PRISM;
	}

	// 草の色
	@Override
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos) {
		return 0xafd8f7;
    }
}
