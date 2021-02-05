package sweetmagic.init;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.registries.IForgeRegistry;
import sweetmagic.config.SMConfig;
import sweetmagic.worldgen.biome.BiomeFlowerGarden;
import sweetmagic.worldgen.biome.BiomeFruitForest;
import sweetmagic.worldgen.biome.BiomePrismForest;

public class BiomeInit {

    public static Biome FLUITFOREST = new BiomeFruitForest();
    public static Biome PRISMFOREST = new BiomePrismForest();
    public static Biome FLOWERGARDEN = new BiomeFlowerGarden();

    public static void init(IForgeRegistry<Biome> registry) {
        registry.register(FLUITFOREST);
		BiomeManager.addSpawnBiome(FLUITFOREST);
        BiomeDictionary.addTypes(FLUITFOREST, Type.FOREST);

        registry.register(PRISMFOREST);
		BiomeManager.addSpawnBiome(PRISMFOREST);
        BiomeDictionary.addTypes(PRISMFOREST, Type.FOREST);

        registry.register(FLOWERGARDEN);
		BiomeManager.addSpawnBiome(FLOWERGARDEN);
        BiomeDictionary.addTypes(FLOWERGARDEN, Type.PLAINS);

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
