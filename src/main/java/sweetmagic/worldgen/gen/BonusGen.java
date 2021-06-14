package sweetmagic.worldgen.gen;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import sweetmagic.config.SMConfig;
import sweetmagic.init.LootTableInit;
import sweetmagic.init.base.BaseWorldGen;
import sweetmagic.worldgen.structures.WorldGenStructure;

public class BonusGen extends BaseWorldGen {

	private static final WorldGenStructure SM_HOUSE = new WorldGenStructure("bonus");

    public BonusGen() {
		this.maxChance = SMConfig.dungeon_spawnchance + 20;
		this.minChance = 1;
		this.seedRand = 1222;
    }

    // コンフィグ確認
    public boolean checkConfig () {
    	return SMConfig.isGenStructure;
    }

    // ディメンション確認
    public boolean checkDimension (int dimId) {
    	return dimId != 0;
    }

    // 生成不可能なバイオーム
    public boolean checkBiome (World world, BlockPos pos, Biome biome) {
    	return !BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY);
    }

    // 生成できる座標であるか
    public boolean checkBlock (World world, IBlockState state, BlockPos pos) {
    	Material mate = state.getMaterial();
    	return world.canSeeSky(pos.up()) && ( mate == Material.GRASS || mate == Material.SAND );
    }

    //生成物の内容
    public void generate(World world, BlockPos pos) {
        WorldGenerator gen = this.SM_HOUSE;
    	gen.generate(world, this.rand, pos);
    	this.setLootTable(world, this.rand, pos.add(2, 2, 2), LootTableInit.BIGINERCHEST, 0.25F);
    }
}
