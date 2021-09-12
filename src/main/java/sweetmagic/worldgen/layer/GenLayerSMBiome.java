package sweetmagic.worldgen.layer;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import sweetmagic.init.BiomeInit;

public class GenLayerSMBiome extends GenLayerSMWorld {

	public final WeightedList<BiomeEntry> biomesToGenerate = new WeightedList<>();
	public final int totalWeight;

    public GenLayerSMBiome(long seed, GenLayer layer) {
        super(seed);
        this.biomesToGenerate.add(new BiomeEntry(BiomeInit.FLOWERGARDEN, 10));
        this.biomesToGenerate.add(new BiomeEntry(BiomeInit.FLOWERVALLEY, 3));
        this.biomesToGenerate.add(new BiomeEntry(BiomeInit.FLUITFOREST, 10));
        this.biomesToGenerate.add(new BiomeEntry(BiomeInit.FLUITFORESTHILL, 3));
        this.biomesToGenerate.add(new BiomeEntry(BiomeInit.COCONUTBEACH, 10));
        this.biomesToGenerate.add(new BiomeEntry(BiomeInit.COCONUTBEACHHILL, 2));
        this.biomesToGenerate.add(new BiomeEntry(BiomeInit.ESTORFOREST, 8));
        this.biomesToGenerate.add(new BiomeEntry(BiomeInit.PRISMFOREST, 10));
        this.biomesToGenerate.add(new BiomeEntry(BiomeInit.PRISMBERG, 2));
		this.biomesToGenerate.add(new BiomeEntry(BiomeInit.PRISMHILL, 3));
		this.biomesToGenerate.add(new BiomeEntry(BiomeInit.SLIVERBERG, 4));
		this.biomesToGenerate.add(new BiomeEntry(BiomeInit.BIGPLATE, 5));
		this.biomesToGenerate.add(new BiomeEntry(BiomeInit.DRYLAND, 5));
		this.biomesToGenerate.add(new BiomeEntry(BiomeInit.FROZENFOREST, 7));
		this.biomesToGenerate.add(new BiomeEntry(BiomeInit.FROZENFORESTHILL, 3));
		this.biomesToGenerate.add(new BiomeEntry(BiomeInit.FROZENDEEP, 2));
		this.biomesToGenerate.add(new BiomeEntry(BiomeInit.FLUITTOWERFOREST, 3));
        this.biomesToGenerate.add(new BiomeEntry(Biomes.RIVER, 7));
        this.biomesToGenerate.add(new BiomeEntry(Biomes.OCEAN, 5));
        this.biomesToGenerate.add(new BiomeEntry(Biomes.BEACH, 2));
        this.totalWeight = this.biomesToGenerate.getTotalWeight();
        this.parent = layer;
    }

    @Override
    public int[] getInts(int x, int z, int sizeX, int sizeZ) {

        this.parent.getInts(x, z, sizeX, sizeZ);
        int[] ints = IntCache.getIntCache(sizeX * sizeZ);

        for (int zz = 0; zz < sizeZ; ++zz) {
            for (int xx = 0; xx < sizeX; ++xx) {
                this.initChunkSeed(xx + x, zz + z);
                ints[xx + zz * sizeX] = Biome.getIdForBiome(this.biomesToGenerate.getRandomItem(this.nextInt(this.totalWeight)).biome);
            }
        }

        return ints;
    }

    public class WeightedList<T extends BiomeEntry> extends ArrayList<T> {

    	public int totalWeight;

        @Override
		public boolean add(T obj) {
			boolean b = super.add(obj);
			this.recalculateWeight();
            return b;
        }

        @Override
		public T remove(int index) {
			T is = super.remove(index);
			this.recalculateWeight();
            return is;
        }

        @Override
        public boolean remove(Object o) {
            boolean b = super.remove(o);
            this.recalculateWeight();
            return b;
        }

        public void recalculateWeight() {
        	this.totalWeight = 0;
            for (T obj : this) {
            	this.totalWeight += obj.itemWeight;
            }
        }

        public int getTotalWeight() {
            return this.totalWeight;
        }

        public T getRandomItem(Random rand) {

            if (this.totalWeight == 0) { return null; }

            int i = rand.nextInt(this.totalWeight);

            for (T obj : this) {
                i -= obj.itemWeight;

                if (i < 0) { return obj; }
            }
            return null;
        }

        public T getRandomItem(int weight) {

            if (this.totalWeight == 0) { return null; }

            for (T obj : this) {
                weight -= obj.itemWeight;

                if (weight < 0) { return obj; }
            }
            return null;
        }
    }
}
