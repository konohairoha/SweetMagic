package sweetmagic.init;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.registries.IForgeRegistry;
import sweetmagic.config.SMConfig;
import sweetmagic.worldgen.biome.BiomeBigPlate;
import sweetmagic.worldgen.biome.BiomeCoconutDesert;
import sweetmagic.worldgen.biome.BiomeDryFruitForest;
import sweetmagic.worldgen.biome.BiomeFlowerGarden;
import sweetmagic.worldgen.biome.BiomeFrozenDeep;
import sweetmagic.worldgen.biome.BiomeFrozenForest;
import sweetmagic.worldgen.biome.BiomeFruitForest;
import sweetmagic.worldgen.biome.BiomePrismBerg;
import sweetmagic.worldgen.biome.BiomePrismForest;
import sweetmagic.worldgen.biome.BiomeSilverBerg;

public class BiomeInit {

    public static Biome FLUITFOREST = new BiomeFruitForest();
    public static Biome FLUITFORESTHILL = new BiomeFruitForest("FluitForestHill",
    		new BiomeProperties("FluitForestHill").setTemperature(1F).setBaseHeight(0.1F).setHeightVariation(0.8F).setRainfall(0.6F).setSnowEnabled(), false);
    public static Biome FLUITTOWERFOREST = new BiomeFruitForest("FluitTowerForest",
    		new BiomeProperties("FluitTowerForest").setTemperature(1F).setBaseHeight(0.1F).setHeightVariation(0.4F).setRainfall(0.2F).setSnowEnabled(), true);

    public static Biome FLOWERGARDEN = new BiomeFlowerGarden();
    public static Biome FLOWERVALLEY = new BiomeFlowerGarden("FlowerValley",
    		new BiomeProperties("FlowerValley").setTemperature(1F).setBaseHeight(0.0F).setHeightVariation(0.9F).setRainfall(0.6F).setSnowEnabled());

    public static Biome ESTORFOREST = new BiomeFlowerGarden("EstorForest");

    public static Biome COCONUTBEACH = new BiomeCoconutDesert();
    public static Biome COCONUTBEACHHILL = new BiomeCoconutDesert("CoconutDesertHill",
    		new BiomeProperties("CoconutDesertHill").setTemperature(1.25F).setBaseHeight(0.3F).setHeightVariation(1F).setRainfall(0.6F).setSnowEnabled());

    public static Biome PRISMFOREST = new BiomePrismForest();
    public static Biome PRISMBERG = new BiomePrismBerg("PrismBerg",
    		new BiomeProperties("PrismBerg").setTemperature(-1F).setBaseHeight(0.4F).setHeightVariation(1.3F).setRainfall(0.6F));
    public static Biome PRISMHILL = new BiomePrismBerg("PrismHill",
    		new BiomeProperties("PrismHill").setTemperature(-1F).setBaseHeight(0.2F).setHeightVariation(1.8F).setRainfall(0.6F));

    public static Biome SLIVERBERG = new BiomeSilverBerg("SilverBerg",
    		new BiomeProperties("SilverBerg").setTemperature(1F).setBaseHeight(0.1F).setHeightVariation(1.2F).setRainfall(0.6F));

    public static Biome BIGPLATE = new BiomeBigPlate();

    public static Biome DRYLAND = new BiomeDryFruitForest();

    public static Biome FROZENFOREST = new BiomeFrozenForest();
    public static Biome FROZENFORESTHILL = new BiomeFrozenForest("FrozenForestHill",
    		new BiomeProperties("FrozenForestHill").setTemperature(-0.1F).setBaseHeight(0.4F).setHeightVariation(0.9F).setRainfall(0.6F));

    public static Biome FROZENDEEP = new BiomeFrozenDeep();

    public static void init(IForgeRegistry<Biome> registry) {
        registerBiome(registry, FLUITFOREST, Type.FOREST);
        registerBiome(registry, FLUITFORESTHILL, Type.HILLS);
        registerBiome(registry, FLUITTOWERFOREST, Type.FOREST);
        registerBiome(registry, ESTORFOREST, Type.FOREST);
        registerBiome(registry, PRISMFOREST, Type.FOREST);
        registerBiome(registry, FLOWERGARDEN, Type.PLAINS);
        registerBiome(registry, FLOWERVALLEY, Type.HILLS);
        registerBiome(registry, COCONUTBEACH, Type.SANDY);
        registerBiome(registry, COCONUTBEACHHILL, Type.SANDY);
        registerBiome(registry, PRISMBERG, Type.MOUNTAIN);
        registerBiome(registry, PRISMHILL, Type.MOUNTAIN);
        registerBiome(registry, SLIVERBERG, Type.MOUNTAIN);
        registerBiome(registry, BIGPLATE, Type.PLAINS);
        registerBiome(registry, DRYLAND, Type.FOREST);
        registerBiome(registry, FROZENFOREST, Type.FOREST);
        registerBiome(registry, FROZENFORESTHILL, Type.FOREST);
        registerBiome(registry, FROZENDEEP, Type.OCEAN);

        // バイオームを生成するなら
        if (SMConfig.genBiome) {
        	genBiome();
        }
    }

    public static void registerBiome (IForgeRegistry<Biome> registry, Biome biome, Type type) {
        registry.register(biome);
		BiomeManager.addSpawnBiome(biome);
        BiomeDictionary.addTypes(biome, type);
    }

    public static void genBiome () {
		BiomeManager.addBiome(BiomeType.WARM, new BiomeEntry(FLUITFOREST, 7));
		BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(PRISMFOREST, 6));
    }
}
