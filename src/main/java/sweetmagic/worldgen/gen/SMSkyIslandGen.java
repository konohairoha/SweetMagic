package sweetmagic.worldgen.gen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import sweetmagic.config.SMConfig;
import sweetmagic.init.LootTableInit;
import sweetmagic.init.base.BaseWorldGen;
import sweetmagic.util.GenHelper;
import sweetmagic.worldgen.structures.WorldGenStructure;

public class SMSkyIslandGen extends BaseWorldGen {

	private static final WorldGenStructure SM_HOUSE = new WorldGenStructure("skyisland");
	public List<GenHelper> skyList = new ArrayList<>();

    public SMSkyIslandGen() {
		this.maxChance = SMConfig.dungeon_spawnchance * 12;
		this.minChance = 0;
		this.minY = 160;
		this.maxY = 161;
		this.seedRand = 31;
    }

    // コンフィグ確認
    public boolean checkConfig () {
    	return SMConfig.isGenStructure;
    }

    // 生成不可能なバイオーム
    public boolean checkBiome (World world, BlockPos pos, Biome biome) {
    	return false;
    }

    // 生成できる座標であるか
    public boolean checkBlock (World world, IBlockState state, BlockPos pos) {
    	return true;
    }

    // 座標リストの取得
    public List<GenHelper> geGenList () {
    	return this.skyList;
    }

    // 離れてる距離を取得
    public int getDistance () {
    	return 800;
    }

    //生成物の内容
    public void generate(World world, BlockPos pos) {

    	pos = pos.up(world.rand.nextInt(30));

        WorldGenerator gen = this.SM_HOUSE;
    	gen.generate(world, this.rand, pos);

		IBlockState state = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.SOUTH);
		world.setBlockState(pos.add(14, 12, 14), state, 3);
		world.setBlockState(pos.add(15, 12, 14), state, 3);
    	this.setLootTable(world, rand, pos.add(14, 12, 14), LootTableInit.SMFOODS);
    	this.setLootTable(world, rand, pos.add(15, 12, 14), LootTableInit.SMFOODS);
    }
}
