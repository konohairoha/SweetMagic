package sweetmagic.worldgen.biome;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.worldgen.gen.WorldGenFlower;

public class BiomeFlowerGarden extends Biome {

	public static final WorldGenerator FLOWER = new WorldGenFlower();

    public BiomeFlowerGarden() {
        super(new BiomeProperties("FlowerGarden").setTemperature(1F).setBaseHeight(0.2F).setHeightVariation(0.2F).setRainfall(0.8F).setSnowEnabled());
        this.decorator.treesPerChunk = 3;
//        this.decorator.grassPerChunk = 2;
        this.setRegistryName("FlowerGarden");
    }

    // チャンス
	@Override
	public float getSpawningChance() {
		return 0.025F;
	}

	// 木の生成
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
		return (WorldGenAbstractTree) FLOWER;
	}

	// 草の色
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos) {
		int i = super.getGrassColorAtPos(pos);
		return (i & 16711422) + 2634762 >> 1;
    }
}
