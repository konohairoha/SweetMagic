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
import sweetmagic.worldgen.biome.BiomeCoconutDesert;
import sweetmagic.worldgen.biome.BiomeFlowerGarden;
import sweetmagic.worldgen.biome.BiomeFruitForest;
import sweetmagic.worldgen.biome.BiomePrismBerg;
import sweetmagic.worldgen.biome.BiomePrismForest;
import sweetmagic.worldgen.biome.BiomeSilverBerg;

public class BiomeInit {

    public static Biome FLUITFOREST = new BiomeFruitForest();
    public static Biome FLUITFORESTHILL = new BiomeFruitForest("FluitForestHill",
    		new BiomeProperties("FluitForestHill").setTemperature(1F).setBaseHeight(0.1F).setHeightVariation(0.8F).setRainfall(0.6F).setSnowEnabled());

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

    public static void init(IForgeRegistry<Biome> registry) {

        registry.register(FLUITFOREST);
		BiomeManager.addSpawnBiome(FLUITFOREST);
        BiomeDictionary.addTypes(FLUITFOREST, Type.FOREST);

        registry.register(FLUITFORESTHILL);
		BiomeManager.addSpawnBiome(FLUITFORESTHILL);
        BiomeDictionary.addTypes(FLUITFORESTHILL, Type.HILLS);

        registry.register(ESTORFOREST);
		BiomeManager.addSpawnBiome(ESTORFOREST);
        BiomeDictionary.addTypes(ESTORFOREST, Type.FOREST);

        registry.register(PRISMFOREST);
		BiomeManager.addSpawnBiome(PRISMFOREST);
        BiomeDictionary.addTypes(PRISMFOREST, Type.FOREST);

        registry.register(FLOWERGARDEN);
		BiomeManager.addSpawnBiome(FLOWERGARDEN);
        BiomeDictionary.addTypes(FLOWERGARDEN, Type.PLAINS);

        registry.register(FLOWERVALLEY);
		BiomeManager.addSpawnBiome(FLOWERVALLEY);
        BiomeDictionary.addTypes(FLOWERVALLEY, Type.HILLS);

        registry.register(COCONUTBEACH);
		BiomeManager.addSpawnBiome(COCONUTBEACH);
        BiomeDictionary.addTypes(COCONUTBEACH, Type.SANDY);

        registry.register(COCONUTBEACHHILL);
		BiomeManager.addSpawnBiome(COCONUTBEACHHILL);
        BiomeDictionary.addTypes(COCONUTBEACHHILL, Type.SANDY);

        registry.register(PRISMBERG);
		BiomeManager.addSpawnBiome(PRISMBERG);
        BiomeDictionary.addTypes(PRISMBERG, Type.MOUNTAIN);

        registry.register(PRISMHILL);
		BiomeManager.addSpawnBiome(PRISMHILL);
        BiomeDictionary.addTypes(PRISMHILL, Type.MOUNTAIN);

        registry.register(SLIVERBERG);
		BiomeManager.addSpawnBiome(SLIVERBERG);
        BiomeDictionary.addTypes(SLIVERBERG, Type.MOUNTAIN);

        // バイオームを生成するなら
        if (SMConfig.genBiome) {
        	genBiome();
        }
    }

    public static void genBiome () {
		BiomeManager.addBiome(BiomeType.WARM, new BiomeEntry(FLUITFOREST, 7));
		BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(PRISMFOREST, 6));
    }
}
