package sweetmagic.worldgen.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import sweetmagic.config.SMConfig;
import sweetmagic.init.VillagerInit;
import sweetmagic.init.base.BaseWorldGen;
import sweetmagic.worldgen.structures.WorldGenStructure;

public class WorldVillageGen extends BaseWorldGen {

	public final WorldGenStructure SM_HOUSE = new WorldGenStructure("vill_house");

    public WorldVillageGen() {
		this.maxChance = SMConfig.dungeon_spawnchance * 2;
		this.minChance = 2;
		this.range = 4;
		this.seedRand = 39;
    }

    // コンフィグ確認
    @Override
    public boolean checkConfig () {
    	return SMConfig.isGenStructure;
    }

    // 生成不可能なバイオーム
    @Override
    public boolean checkBiome (World world, BlockPos pos, Biome biome) {
    	return biome != Biomes.PLAINS && biome != Biomes.SAVANNA && biome != Biomes.FOREST;
    }

    // 村チェック
    @Override
    public boolean checkVillage (World world, BlockPos pos) {
    	return world.villageCollection.getNearestVillage(pos, 32) == null;
    }

    // 生成できる座標であるか
    @Override
    public boolean checkBlock (World world, IBlockState state, BlockPos pos) {
    	return world.canSeeSky(pos.up()) && state.getMaterial() == Material.GRASS;
    }

    //生成物の内容
    public void generate(World world, BlockPos pos) {

    	pos = pos.down(2);

    	for (int x = 2; x < 8; x++) {
    		for (int z = 2; z < 12; z++) {
    			Block block = world.getBlockState(pos.add(x + 20, 3, z + 8)).getBlock();
        		if (block != Blocks.AIR && !(block instanceof BlockBush)) {
        			return;
        		}
        	}
    	}

        WorldGenerator gen = this.SM_HOUSE;
    	gen.generate(world, this.rand, pos);

    	if (!world.isRemote) {

    		ForgeRegistry registry = (ForgeRegistry) ForgeRegistries.VILLAGER_PROFESSIONS;
    		int id = registry.getID(world.rand.nextBoolean() ? VillagerInit.cook : VillagerInit.magic_researcher);

			EntityVillager eggv = new EntityVillager(world);
    		eggv.setGrowingAge(0);
    		eggv.setProfession(id);
			eggv.setLocationAndAngles(pos.getX() + 9, pos.getY() + 4, pos.getZ() + 10, 0, 0F);
	        world.spawnEntity(eggv);
    	}
    }
}
