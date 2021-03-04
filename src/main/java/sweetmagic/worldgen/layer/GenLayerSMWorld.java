package sweetmagic.worldgen.layer;

import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

public abstract  class GenLayerSMWorld extends GenLayer {

    public GenLayerSMWorld(long seed) {
        super(seed);
    }

    public static GenLayer[] initializeAllBiomeGenerators(long seed, WorldType type) {

		int size = getModdedBiomeSize(type, 5);

		GenLayer layer = new GenLayerIsland(1L);
		layer = new GenLayerFuzzyZoom(2000L, layer);
		layer = new GenLayerSMBiome(100L, layer);
		layer = GenLayerZoom.magnify(2000L, layer, 1);
		layer = GenLayerZoom.magnify(2100L, layer, size);

		GenLayer indexLayer = new GenLayerVoronoiZoom(10L, layer);
		indexLayer.initWorldGenSeed(seed);
		layer.initWorldGenSeed(seed);

		return new GenLayer[] { layer, indexLayer, layer };
    }
}
