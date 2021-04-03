package sweetmagic.worldgen.gen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.BiomeDictionary;
import sweetmagic.config.SMConfig;
import sweetmagic.init.BlockInit;
import sweetmagic.init.DimensionInit;
import sweetmagic.init.base.BaseWorldGen;
import sweetmagic.init.tile.magic.TileSpawnStone;
import sweetmagic.worldgen.structures.WorldGenStructure;

public class MoatGen extends BaseWorldGen {

	public final WorldGenStructure SM_HOUSE = new WorldGenStructure("moat");

    public MoatGen() {
		this.maxChance = SMConfig.dungeon_spawnchance;
		this.minChance = 0;
		this.minY = 63;
		this.maxY = 70;
		this.seedRand = 99;
    }

    // ディメンション確認
    public boolean checkDimension (int dimId) {
    	return dimId != DimensionInit.dimID;
    }

    // 生成不可能なバイオーム
    public boolean checkBiome (World world, BlockPos pos, Biome biome) {
    	return !BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS);
    }

    //生成物の内容
    public void generate(World world, BlockPos pos) {

        WorldGenerator gen = this.SM_HOUSE;
    	gen.generate(world, this.rand, pos);

    	// 隠しチェスト
    	this.setLootTable(world, this.rand, pos.add(12, 2, 1), LootTableList.CHESTS_END_CITY_TREASURE);

		IBlockState dirt = Blocks.DIRT.getDefaultState();
		for (int x = 0; x < 41; x++) {
			for (int z = 0; z < 42; z++) {
				BlockPos p = pos.add(x, 0, z);
				this.setBlock(world, p.down(1), dirt);
				this.setBlock(world, p.down(2), dirt);
				this.setBlock(world, p.down(3), dirt);
			}
		}

    	this.setSpaner(world, pos.add(21, 2, 23), 3);
    	this.setLootTable(world, this.rand, pos.add(14, 6, 16), LootTableList.CHESTS_END_CITY_TREASURE);
    }

	public void setBlock (World world, BlockPos pos, IBlockState state) {
		world.setBlockState(pos, state, 2);
	}

	public void setSpaner (World world, BlockPos pos, int rand) {
		world.setBlockState(pos, BlockInit.spawn_stone.getDefaultState(), 3);
		TileSpawnStone tile = (TileSpawnStone) world.getTileEntity(pos);
		tile.data = rand;
		tile.isRand = true;
	}

	// 乱数取得
	public int getRandInt (World world) {
		int rand = world.rand.nextInt(5);
		return rand == 3 ? 0 : rand;
	}
}
