package sweetmagic.worldgen.dimension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.ChunkGeneratorSettings.Factory;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextOverworld;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;
import sweetmagic.worldgen.dungen.map.MapFlugelHouse;
import sweetmagic.worldgen.dungen.map.MapGenCastle;
import sweetmagic.worldgen.dungen.map.MapGenDekaijyu;
import sweetmagic.worldgen.dungen.map.MapGenIdo;
import sweetmagic.worldgen.dungen.map.MapGenMekyu;
import sweetmagic.worldgen.dungen.map.MapGenPyramid;
import sweetmagic.worldgen.dungen.map.MapGenTogijyo;
import sweetmagic.worldgen.dungen.map.MapGenVillager;
import sweetmagic.worldgen.dungen.map.MapWitchHouse;
import sweetmagic.worldgen.map.SMMapGenCaves;
import sweetmagic.worldgen.map.SMMapGenRiver;

public class SMChunkGen implements IChunkGenerator {

	private static final IBlockState WATER = Blocks.WATER.getDefaultState();
	private static final IBlockState LAVA = Blocks.LAVA.getDefaultState();
    private static final IBlockState ICE = Blocks.ICE.getDefaultState();
    private static final IBlockState SNOW_LAYER = Blocks.SNOW_LAYER.getDefaultState();
    private static final IBlockState STONE = Blocks.STONE.getDefaultState();
    private static final IBlockState DIRT = Blocks.DIRT.getDefaultState();
    private static final IBlockState GRASS = Blocks.GRASS.getDefaultState();

    private final Random rand;
    private final World world;
    private final boolean mapFeaturesEnabled;
    private final double[] heightMap;
    private final float[] biomeWeights;
    public NoiseGeneratorOctaves scaleNoise;
    public NoiseGeneratorOctaves depthNoise;
    public NoiseGeneratorOctaves forestNoise;
    double[] mainNoiseRegion;
    double[] minLimitRegion;
    double[] maxLimitRegion;
    double[] depthRegion;
    private NoiseGeneratorOctaves minLimitPerlinNoise;
    private NoiseGeneratorOctaves maxLimitPerlinNoise;
    private NoiseGeneratorOctaves mainPerlinNoise;
    private NoiseGeneratorPerlin surfaceNoise;
    private ChunkGeneratorSettings settings;
    private IBlockState oceanBlock = WATER;
    private double[] depthBuffer = new double[256];
    private MapGenBase caveGen = new SMMapGenCaves();
    private MapGenBase riverGen = new SMMapGenRiver();
    private MapGenStronghold strongGen= new MapGenStronghold();
    private Biome[] biomesForGen;

	// 追加生成の設定
    private MapGenPyramid pryramid = new MapGenPyramid(this);
    private MapGenDekaijyu dekaijyu = new MapGenDekaijyu(this);
    private MapGenTogijyo togijyo = new MapGenTogijyo(this);
    private MapGenMekyu mekyu = new MapGenMekyu(this);
    private MapGenIdo ido = new MapGenIdo(this);
    private MapWitchHouse witchhouse = new MapWitchHouse(this);
    private MapGenCastle castle = new MapGenCastle(this);
    private MapGenVillager villager = new MapGenVillager(this);
    private MapFlugelHouse flugel = new MapFlugelHouse(this);

	// 追加生成の設定
	Map<MapGenStructure, String> genMap = new HashMap<>();

    public SMChunkGen(World world, long seed, boolean enabled, String option) {
        {
        	this.caveGen = TerrainGen.getModdedMapGen(this.caveGen, InitMapGenEvent.EventType.CAVE);
        	this.riverGen = TerrainGen.getModdedMapGen(this.riverGen, InitMapGenEvent.EventType.RAVINE);
        	this.strongGen = (MapGenStronghold) TerrainGen.getModdedMapGen(this.strongGen, InitMapGenEvent.EventType.STRONGHOLD);

        	this.genMap.put(this.pryramid, "pyramid_top");
        	this.genMap.put(this.dekaijyu, "dekaijyu");
        	this.genMap.put(this.togijyo, "burassamu");
        	this.genMap.put(this.mekyu, "mekyu");
        	this.genMap.put(this.ido, "ido");
        	this.genMap.put(this.witchhouse, "witchhouse_main");
        	this.genMap.put(this.castle, "castle");
        	this.genMap.put(this.villager, "villager");
        	this.genMap.put(this.flugel, "flugel_house");

            if (this.getGen(this.pryramid) instanceof MapGenPyramid) {
            	this.pryramid = (MapGenPyramid) this.getGen(this.pryramid);
            }

            else if (this.getGen(this.dekaijyu) instanceof MapGenDekaijyu) {
            	this.dekaijyu = (MapGenDekaijyu) this.getGen(this.dekaijyu);
            }

            else if (this.getGen(this.togijyo) instanceof MapGenTogijyo) {
            	this.togijyo = (MapGenTogijyo) this.getGen(this.togijyo);
            }

            else if (this.getGen(this.mekyu) instanceof MapGenMekyu) {
            	this.mekyu = (MapGenMekyu) this.getGen(this.mekyu);
            }

            else if (this.getGen(this.ido) instanceof MapGenIdo) {
            	this.ido = (MapGenIdo) this.getGen(this.ido);
            }

            else  if (this.getGen(this.witchhouse) instanceof MapWitchHouse) {
            	this.witchhouse = (MapWitchHouse) this.getGen(this.witchhouse);
            }

            else if (this.getGen(this.castle) instanceof MapGenCastle) {
            	this.castle = (MapGenCastle) this.getGen(this.castle);
            }

            else if (this.getGen(this.villager) instanceof MapGenVillager) {
            	this.villager = (MapGenVillager) this.getGen(this.villager);
            }

            else if (this.getGen(this.flugel) instanceof MapFlugelHouse) {
            	this.flugel = (MapFlugelHouse) this.getGen(this.flugel);
            }
        }

        this.world = world;
        this.mapFeaturesEnabled = enabled;
        this.rand = new Random(seed + 1222);
        this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
        this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
        this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.forestNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.heightMap = new double[825];
        this.biomeWeights = new float[25];

        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                float f = 10.0F / MathHelper.sqrt((float) (i * i + j * j) + 0.2F);
                this.biomeWeights[i + 2 + (j + 2) * 5] = f;
            }
        }

        if (option != null) {
            this.settings = Factory.jsonToFactory(option).build();
            this.oceanBlock = this.settings.useLavaOceans ? LAVA : WATER;
            world.setSeaLevel(this.settings.seaLevel);
        }

        ContextOverworld ctx =
                new ContextOverworld(this.minLimitPerlinNoise, this.maxLimitPerlinNoise, this.mainPerlinNoise, this.surfaceNoise,
                		this.scaleNoise, this.depthNoise, this.forestNoise);
        ctx = TerrainGen.getModdedNoiseGenerators(world, this.rand, ctx);
        this.minLimitPerlinNoise = ctx.getLPerlin1();
        this.maxLimitPerlinNoise = ctx.getLPerlin2();
        this.mainPerlinNoise = ctx.getPerlin();
        this.surfaceNoise = ctx.getHeight();
        this.scaleNoise = ctx.getScale();
        this.depthNoise = ctx.getDepth();
        this.forestNoise = ctx.getForest();
    }

    @Override
    public Chunk generateChunk(int x, int z) {

        this.rand.setSeed((long) x * 341873128334L + (long) z * 132897987334L);
        ChunkPrimer primer = new ChunkPrimer();

        this.setChunkGen(x, z, primer);
        this.biomesForGen = this.world.getBiomeProvider().getBiomes(this.biomesForGen, x * 16, z * 16, 16, 16);
        this.replaceWorldChunkGen(x, z, primer, this.biomesForGen);

        this.caveGen.generate(this.world, x, z, primer);
        this.riverGen.generate(this.world, x, z, primer);
        this.strongGen.generate(this.world, x, z, primer);

    	// 追加生成の設定
		for (Entry<MapGenStructure, String> map : this.genMap.entrySet()) {
			map.getKey().generate(this.world, x, z, primer);
		}

        Chunk chunk = new Chunk(this.world, primer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte) Biome.getIdForBiome(this.biomesForGen[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    public void setChunkGen(int x, int z, ChunkPrimer primer) {

        byte seaLevel = 63;
        this.biomesForGen = this.world.getBiomeProvider().getBiomesForGeneration(this.biomesForGen, x * 4 - 2, z * 4 - 2, 10, 10);
        this.generateHeightmap(x * 4, 0, z * 4);

        for (int k = 0; k < 4; ++k) {

            int l = k * 5;
            int i1 = (k + 1) * 5;
            for (int j1 = 0; j1 < 4; ++j1) {

                int k1 = (l + j1) * 33;
                int l1 = (l + j1 + 1) * 33;
                int i2 = (i1 + j1) * 33;
                int j2 = (i1 + j1 + 1) * 33;

                for (int k2 = 0; k2 < 32; ++k2) {

                    double d0 = 0.125D;
                    double d1 = this.heightMap[k1 + k2];
                    double d2 = this.heightMap[l1 + k2];
                    double d3 = this.heightMap[i2 + k2];
                    double d4 = this.heightMap[j2 + k2];
                    double d5 = (this.heightMap[k1 + k2 + 1] - d1) * d0;
                    double d6 = (this.heightMap[l1 + k2 + 1] - d2) * d0;
                    double d7 = (this.heightMap[i2 + k2 + 1] - d3) * d0;
                    double d8 = (this.heightMap[j2 + k2 + 1] - d4) * d0;

                    for (int l2 = 0; l2 < 8; ++l2) {

                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int i3 = 0; i3 < 4; ++i3) {

                            double d14 = 0.25D;
                            double d16 = (d11 - d10) * d14;
                            double d15 = d10 - d16;

                            for (int k3 = 0; k3 < 4; ++k3) {

                                if ((d15 += d16) > 0D) {
                                    primer.setBlockState(k * 4 + i3, k2 * 8 + l2, j1 * 4 + k3, STONE);
                                }

                                else if (k2 * 8 + l2 < seaLevel) {
                                    primer.setBlockState(k * 4 + i3, k2 * 8 + l2, j1 * 4 + k3, WATER);
                                }
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    public void replaceWorldChunkGen(int x, int z, ChunkPrimer primer, Biome... biomes) {

        if (!ForgeEventFactory.onReplaceBiomeBlocks(this, x, z, primer, this.world)) { return; }

        this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, x * 16, z * 16, 16, 16, 0.0625D, 0.0625D, 1D);

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                Biome biome = biomes[j + i * 16];
                biome.genTerrainBlocks(this.world, this.rand, primer, x * 16 + i, z * 16 + j, this.depthBuffer[j + i * 16]);
            }
        }
    }

    private void generateHeightmap(int x, int zero, int z) {

        this.depthRegion = this.depthNoise.generateNoiseOctaves(this.depthRegion, x, z, 5, 5, 200D, 200D, 0.5D);
        this.mainNoiseRegion = this.mainPerlinNoise.generateNoiseOctaves(this.mainNoiseRegion, x, zero, z, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        this.minLimitRegion = this.minLimitPerlinNoise.generateNoiseOctaves(this.minLimitRegion, x, zero, z, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        this.maxLimitRegion = this.maxLimitPerlinNoise.generateNoiseOctaves(this.maxLimitRegion, x, zero, z, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        int terrainIndex = 0;
        int noiseIndex = 0;

        for (int ax = 0; ax < 5; ++ax) {
            for (int az = 0; az < 5; ++az) {

                float totalVariation = 0.0F;
                float totalHeight = 0.0F;
                float totalFactor = 0.0F;
                byte two = 2;
                Biome biome = this.biomesForGen[ax + 2 + (az + 2) * 10];

                for (int ox = -two; ox <= two; ++ox) {
                    for (int oz = -two; oz <= two; ++oz) {

                        Biome biome1 = this.biomesForGen[ax + ox + 2 + (az + oz + 2) * 10];
                        float rootHeight = biome1.getBaseHeight();
                        float heightVariation = biome1.getHeightVariation();
                        float heightFactor = this.biomeWeights[ox + 2 + (oz + 2) * 5] / (rootHeight + 2.0F);

                        if (biome1.getBaseHeight() > biome.getBaseHeight()) {
                            heightFactor /= 2F;
                        }

                        totalVariation += heightVariation * heightFactor;
                        totalHeight += rootHeight * heightFactor;
                        totalFactor += heightFactor;
                    }
                }

                totalVariation /= totalFactor;
                totalHeight /= totalFactor;
                totalVariation = totalVariation * 0.9F + 0.1F;
                totalHeight = (totalHeight * 4.0F - 1.0F) / 8.0F;
                double terrainNoise = this.depthRegion[noiseIndex] / 8000D;

                if (terrainNoise < 0D) {
                    terrainNoise = -terrainNoise * 0.3D;
                }

                terrainNoise = terrainNoise * 3D - 2D;

                if (terrainNoise < 0D) {

                    terrainNoise /= 2D;

                    if (terrainNoise < -1D) {
                        terrainNoise = -1D;
                    }

                    terrainNoise /= 1.4D;
                    terrainNoise /= 2D;

                }

                else {

                    if (terrainNoise > 1D) {
                        terrainNoise = 1D;
                    }

                    terrainNoise /= 8D;
                }

                ++noiseIndex;
                double heightCalc = (double) totalHeight;
                double variationCalc = (double) totalVariation;
                heightCalc += terrainNoise * 0.2D;
                heightCalc = heightCalc * 8.5D / 8D;
                double d5 = 8.5D + heightCalc * 4D;

                for (int ay = 0; ay < 33; ++ay) {

                    double d6 = ((double) ay - d5) * 12D * 128D / 256D / variationCalc;
                    if (d6 < 0D) { d6 *= 4D; }

                    double d7 = this.minLimitRegion[terrainIndex] / 512D;
                    double d8 = this.maxLimitRegion[terrainIndex] / 512D;
                    double d9 = (this.mainNoiseRegion[terrainIndex] / 10D + 1D) / 2D;
                    double terrainCalc = MathHelper.clampedLerp(d7, d8, d9) - d6;

                    if (ay > 29) {
                        double d11 = (double) ((float) (ay - 29) / 3.0F);
                        terrainCalc = terrainCalc * (1D - d11) + -10D * d11;
                    }
                    this.heightMap[terrainIndex] = terrainCalc;
                    ++terrainIndex;
                }
            }
        }
    }

    @Override
    public void populate(int x, int z) {

        BlockFalling.fallInstantly = true;
        int i = x * 16;
        int j = z * 16;
        BlockPos pos = new BlockPos(i, 0, j);
        Biome biome = this.world.getBiome(pos.add(16, 0, 16));
        this.rand.setSeed(this.world.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long) x * k + (long) z * l ^ this.world.getSeed());
        boolean flag = false;
        ChunkPos chunkpos = new ChunkPos(x, z);
        ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);

        this.strongGen.generateStructure(this.world, this.rand, chunkpos);


    	// 追加生成の設定
		for (Entry<MapGenStructure, String> map : this.genMap.entrySet()) {
			map.getKey().generateStructure(this.world, this.rand, chunkpos);
		}

		if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, EventType.DUNGEON)) {
			for (int j2 = 0; j2 < this.settings.dungeonChance; ++j2) {
				int i3 = this.rand.nextInt(16) + 8;
				int l3 = this.rand.nextInt(256);
				int l1 = this.rand.nextInt(16) + 8;
				(new WorldGenDungeons()).generate(this.world, this.rand, pos.add(i3, l3, l1));
			}
		}

        if (this.settings.useDungeons) {
            if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, EventType.DUNGEON)) {
                for (int j2 = 0; j2 < this.settings.dungeonChance; ++j2) {
                    int i3 = this.rand.nextInt(16) + 8;
                    int l3 = this.rand.nextInt(256);
                    int l1 = this.rand.nextInt(16) + 8;
                    new WorldGenDungeons().generate(this.world, this.rand, pos.add(i3, l3, l1));
                }
            }
        }

        biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));

        if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, EventType.ANIMALS)) {
            WorldEntitySpawner.performWorldGenSpawning(this.world, biome, i + 8, j + 8, 16, 16, this.rand);
        }

        pos = pos.add(8, 0, 8);

        if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, EventType.ICE)) {
            for (int k2 = 0; k2 < 16; ++k2) {
                for (int j3 = 0; j3 < 16; ++j3) {
                    BlockPos blockpos1 = this.world.getPrecipitationHeight(pos.add(k2, 0, j3));
                    BlockPos blockpos2 = blockpos1.down();

                    if (this.world.canBlockFreezeWater(blockpos2)) {
                        this.world.setBlockState(blockpos2, ICE, 2);
                    }

                    if (this.world.canSnowAt(blockpos1, true)) {
                        this.world.setBlockState(blockpos1, SNOW_LAYER, 2);
                    }
                }
            }
        }

        ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, flag);
        BlockFalling.fallInstantly = false;
    }

    @Override
    public boolean generateStructures(Chunk chunk, int x, int z) {
        return false;
    }

    @Override
    public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType type, BlockPos pos) {
        return this.world.getBiome(pos).getSpawnableList(type);
    }

    @Override
    @Nullable
    public BlockPos getNearestStructurePos(World world, String structure, BlockPos pos, boolean findUnexplored) {

        if (!this.mapFeaturesEnabled) { return null; }

		try {
			if ("Stronghold".equals(structure) && this.strongGen != null && world != null) {
				return this.strongGen.getNearestStructurePos(world, pos, findUnexplored);
			}
		}

		catch (Throwable e) {}

        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) { }

    @Override
    public boolean isInsideStructure(World world, String structure, BlockPos pos) {

    	// 追加生成の設定
		for (Entry<MapGenStructure, String> map : this.genMap.entrySet()) {
			if (map.getValue().equals(structure) && map.getKey() != null) {
				return map.getKey().isInsideStructure(pos);
			}
		}

        if (!this.mapFeaturesEnabled) { return false; }

        return false;
    }

    private void replace(BlockPos pos, Block block) {

        if (block == Blocks.DIRT) {
            this.world.setBlockState(pos, DIRT, 2);
        }

        else if (block == Blocks.GRASS) {
            this.world.setBlockState(pos, GRASS, 2);
        }
    }

    private MapGenBase getGen (MapGenBase gen) {
    	return TerrainGen.getModdedMapGen(gen, InitMapGenEvent.EventType.CUSTOM);
    }
}
