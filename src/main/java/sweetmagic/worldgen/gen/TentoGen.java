package sweetmagic.worldgen.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import sweetmagic.config.SMConfig;
import sweetmagic.init.BlockInit;
import sweetmagic.init.DimensionInit;
import sweetmagic.init.base.BaseWorldGen;
import sweetmagic.worldgen.structures.WorldGenStructure;

public class TentoGen extends BaseWorldGen {

	public final WorldGenStructure SM_HOUSE = new WorldGenStructure("tento");

    public TentoGen() {
		this.maxChance = SMConfig.dungeon_spawnchance;
		this.minChance = 0;
		this.minY = 63;
		this.maxY = 70;
		this.seedRand = 60;
    }

    // ディメンション確認
    public boolean checkDimension (int dimId) {
    	return dimId != DimensionInit.dimID;
    }

    // 生成不可能なバイオーム
    public boolean checkBiome (World world, BlockPos pos, Biome biome) {
    	return !BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST);
    }

    //生成物の内容
    public void generate(World world, BlockPos pos) {

		pos = pos.down(1);

    	for (int x = -10; x < 0; x++) {
    		for (int z = 0; z < 10; z++) {
    			Block block = world.getBlockState(pos.add(x, 3, z)).getBlock();
        		if (block != Blocks.AIR && !(block instanceof BlockBush)) {
        			return;
        		}
        	}
    	}

        WorldGenerator gen = this.SM_HOUSE;
    	gen.generate(world, this.rand, pos);

		IBlockState state = BlockInit.lemon_trapdoor.getDefaultState().withProperty(BlockTrapDoor.OPEN, Boolean.valueOf(true)).withProperty(BlockTrapDoor.FACING, EnumFacing.EAST);
		world.setBlockState(pos.add(27, 5, 14), state, 3);
		world.setBlockState(pos.add(27, 5, 18), state, 3);
    }

	// 乱数取得
	public int getRandInt (World world) {
		int rand = world.rand.nextInt(5);
		return rand == 3 ? 0 : rand;
	}
}
