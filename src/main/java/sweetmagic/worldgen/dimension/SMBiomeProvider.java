package sweetmagic.worldgen.dimension;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.ChunkGeneratorSettings.Factory;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddMushroomIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerDeepOcean;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerEdge.Mode;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerShore;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent.InitBiomeGens;
import sweetmagic.init.BiomeInit;
import sweetmagic.worldgen.layer.GenLayerSMWorld;

public class SMBiomeProvider extends BiomeProvider {

    public static List<Biome> allowedBiomes = Lists.newArrayList(BiomeInit.FLOWERGARDEN);
    private final BiomeCache biomeCache;
    private final List<Biome> biomesToSpawnIn;
    private ChunkGeneratorSettings settings;
    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;

    public SMBiomeProvider(WorldInfo info) {
        this(info.getSeed(), info.getTerrainType(), info.getGeneratorOptions());
    }

    private SMBiomeProvider(long seed, WorldType type, String options) {
        this();

        if (type == WorldType.CUSTOMIZED && !options.isEmpty()) {
            this.settings = Factory.jsonToFactory(options).build();
        }

        GenLayer[] agenlayer = this.initializeAllBiomeGenerators(seed, type, this.settings);
        agenlayer = getModdedBiomeGenerators(type, seed, agenlayer);
        this.genBiomes = agenlayer[0];
        this.biomeIndexLayer = agenlayer[1];
    }

    protected SMBiomeProvider() {
        this.biomeCache = new BiomeCache(this);
        this.biomesToSpawnIn = Lists.newArrayList(allowedBiomes);
    }

    GenLayer[] initializeAllBiomeGenerators(long seed, WorldType type, ChunkGeneratorSettings setting) {

		GenLayer layer = new GenLayerIsland(1L);
		layer = new GenLayerFuzzyZoom(2000L, layer);

		GenLayer addisland = new GenLayerAddIsland(1L, layer);
		GenLayer zoom = new GenLayerZoom(2001L, addisland);
		GenLayer addisland1 = new GenLayerAddIsland(2L, zoom);
		addisland1 = new GenLayerAddIsland(50L, addisland1);
		addisland1 = new GenLayerAddIsland(70L, addisland1);
		GenLayer ocean = new GenLayerRemoveTooMuchOcean(2L, addisland1);
		GenLayer snow = new GenLayerAddSnow(2L, ocean);
		GenLayer island2 = new GenLayerAddIsland(3L, snow);
		GenLayer edge = new GenLayerEdge(2L, island2, Mode.COOL_WARM);
		edge = new GenLayerEdge(2L, edge, Mode.HEAT_ICE);
		edge = new GenLayerEdge(3L, edge, Mode.SPECIAL);
		GenLayer zoom1 = new GenLayerZoom(2002L, edge);
		zoom1 = new GenLayerZoom(2003L, zoom1);
		GenLayer addisland3 = new GenLayerAddIsland(4L, zoom1);
		GenLayer mushroomisland = new GenLayerAddMushroomIsland(5L, addisland3);
		GenLayer deepocean = new GenLayerDeepOcean(4L, mushroomisland);
		GenLayer genlayer4 = GenLayerZoom.magnify(1000L, deepocean, 0);
		int i = 4;
		int j = i;

		if (setting != null) {
			i = setting.biomeSize;
			j = setting.riverSize;
		}

		i = GenLayer.getModdedBiomeSize(type, i);

		GenLayer layer1 = GenLayerZoom.magnify(1000L, genlayer4, 0);
		GenLayer river = new GenLayerRiverInit(100L, layer1);
		GenLayer biomeedge = type.getBiomeLayer(seed, genlayer4, setting);
		GenLayer layer2 = GenLayerZoom.magnify(1000L, river, 2);
		GenLayer hills = new GenLayerHills(1000L, biomeedge, layer2);
		GenLayer layer5 = GenLayerZoom.magnify(1000L, river, 2);
		layer5 = GenLayerZoom.magnify(1000L, layer5, j);
		GenLayer river2 = new GenLayerRiver(1L, layer5);
		GenLayer mooth = new GenLayerSmooth(1000L, river2);
		hills = new GenLayerRareBiome(1001L, hills);

		for (int k = 0; k < i; ++k) {
			hills = new GenLayerZoom((long) (1000 + k), hills);

			if (k == 0) {
				hills = new GenLayerAddIsland(3L, hills);
			}

			if (k == 1 || i == 1) {
				hills = new GenLayerShore(1000L, hills);
			}
		}

		GenLayer mooth1 = new GenLayerSmooth(1000L, hills);
		GenLayer rivermix = new GenLayerRiverMix(100L, mooth1, mooth);
		GenLayer layer3 = new GenLayerVoronoiZoom(10L, rivermix);
		rivermix.initWorldGenSeed(seed);
		layer3.initWorldGenSeed(seed);

		return new GenLayer[] { rivermix, layer3, rivermix };
    }

    @Override
    public List<Biome> getBiomesToSpawnIn() {
        return this.biomesToSpawnIn;
    }

    @Override
    public Biome getBiome(BlockPos pos) {
        return this.getBiome(pos, null);
    }

    @Override
    public Biome getBiome(BlockPos pos, Biome biome) {
        return this.biomeCache.getBiome(pos.getX(), pos.getZ(), biome);
    }

    @Override
    public float getTemperatureAtHeight(float par1, int par2) {
        return par1 * 2;
    }

    @Override
    public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height) {

		IntCache.resetIntCache();
		if (biomes == null || biomes.length < width * height) {
			biomes = new Biome[width * height];
		}

        int[] aint = this.genBiomes.getInts(x, z, width, height);

        try {
            for (int i = 0; i < width * height; ++i) {
                biomes[i] = Biome.getBiome(aint[i], Biomes.DEFAULT);
            }
            return biomes;
        } catch (Throwable throwable) {
            CrashReport report = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory category = report.makeCategory("RawBiomeBlock");
            category.addCrashSection("biomes[] size", biomes.length);
            category.addCrashSection("x", x);
            category.addCrashSection("z", z);
            category.addCrashSection("w", width);
            category.addCrashSection("h", height);
            throw new ReportedException(report);
        }
    }

    @Override
    public Biome[] getBiomes( @Nullable Biome[] biome, int x, int z, int width, int depth) {
        return this.getBiomes(biome, x, z, width, depth, true);
    }

    @Override
    public Biome[] getBiomes( @Nullable Biome[] biome, int x, int z, int width, int length, boolean flag) {
        IntCache.resetIntCache();
        if (biome == null || biome.length < width * length) {
            biome = new Biome[width * length];
        }

        if (flag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0) {
            Biome[] abiome = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(abiome, 0, biome, 0, width * length);
            return biome;
        }

        int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);

        for (int i = 0; i < width * length; ++i) {
            biome[i] = Biome.getBiome(aint[i], Biomes.DEFAULT);
        }

        return biome;
    }

    @Override
    public boolean areBiomesViable(int x, int z, int radius, List<Biome> biomeList) {

        IntCache.resetIntCache();
        int i = x - radius >> 2;
        int j = z - radius >> 2;
        int k = x + radius >> 2;
        int l = z + radius >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        int[] aint = this.genBiomes.getInts(i, j, i1, j1);

		try {
			for (int k1 = 0; k1 < i1 * j1; ++k1) {
				Biome biome = Biome.getBiome(aint[k1]);

                if (!biomeList.contains(biome)) { return false; }
            }

            return true;
        } catch (Throwable throwable) {
            CrashReport report = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory category = report.makeCategory("Layer");
            category.addCrashSection("Layer", this.genBiomes.toString());
            category.addCrashSection("x", x);
            category.addCrashSection("z", z);
            category.addCrashSection("radius", radius);
            category.addCrashSection("allowed", biomeList);
            throw new ReportedException(report);
        }
    }

    @Override
    @Nullable
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomeList, Random rand) {

        IntCache.resetIntCache();
        int i = x - range >> 2;
        int j = z - range >> 2;
        int k = x + range >> 2;
        int l = z + range >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        int[] aint = this.genBiomes.getInts(i, j, i1, j1);
        BlockPos pos = null;
        int k1 = 0;

        for (int l1 = 0; l1 < i1 * j1; ++l1) {

            int i2 = i + l1 % i1 << 2;
            int j2 = j + l1 / i1 << 2;
            Biome biome = Biome.getBiome(aint[l1]);

            if (biomeList.contains(biome) && (pos == null || rand.nextInt(k1 + 1) == 0)) {
                pos = new BlockPos(i2, 0, j2);
                ++k1;
            }
        }

        return pos;
    }

    @Override
    public void cleanupCache() {
        this.biomeCache.cleanupCache();
    }

    @Override
    public GenLayer[] getModdedBiomeGenerators(WorldType type, long seed, GenLayer[] layer) {
        layer = GenLayerSMWorld.initializeAllBiomeGenerators(seed, type);
        InitBiomeGens event = new InitBiomeGens(type, seed, layer);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.getNewBiomeGens();
    }

    @Override
    public boolean isFixedBiome() {
        return this.settings != null && this.settings.fixedBiome >= 0;
    }

    @Override
    public Biome getFixedBiome() {
        return this.settings != null && this.settings.fixedBiome >= 0 ? Biome.getBiomeForId(this.settings.fixedBiome) : null;
    }
}
