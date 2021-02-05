package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import sweetmagic.init.BlockInit;

public class SMOreGen implements IWorldGenerator {

	//ワールド生成用変数
    private WorldGenerator ac_ore;

    public SMOreGen() {

        //たまごっちメモ：ワールド生成用変数 = new WorldGenMinable (ブロック名) , (ブロックの最大生成数) , (置換するブロック)
    	this.ac_ore = new WorldGenMinable(BlockInit.ac_ore.getDefaultState(), 8, BlockMatcher.forBlock(Blocks.STONE));
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		this.runGenerator(ac_ore, world, random, chunkX, chunkZ, 3, 0, 30);
		//備忘メモ：じぇねれーたの後ろの数字はチャンス、一番下の高さ、一番上の高さ
    }

    private void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, int minHeight, int maxHeight) {
        if (minHeight > maxHeight || minHeight < 0 || maxHeight > 256) throw new IllegalArgumentException("Ore generated out of bounds");
        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chance; i++) {
            int x = chunkX * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunkZ * 16 + rand.nextInt(16);
            gen.generate(world, rand, new BlockPos(x, y, z));
        }
    }
}
