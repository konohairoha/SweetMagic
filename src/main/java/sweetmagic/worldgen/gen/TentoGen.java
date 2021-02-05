package sweetmagic.worldgen.gen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
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
import sweetmagic.init.tile.magic.TileSMSpaner;
import sweetmagic.util.GenHelper;
import sweetmagic.worldgen.structures.WorldGenStructure;

public class TentoGen extends BaseWorldGen {

	public final WorldGenStructure SM_HOUSE = new WorldGenStructure("tento");
	public List<GenHelper> skyList = new ArrayList<>();

    public TentoGen() {
		this.maxChance = SMConfig.dungeon_spawnchance;
		this.minChance = 0;
		this.minY = 63;
		this.maxY = 70;
		this.range = 4;
		this.seedRand = 60;
    }

    // コンフィグ確認
    public boolean checkConfig () {
    	return SMConfig.isGenStructure;
    }

    // ディメンション確認
    public boolean checkDimension (int dimId) {
    	return dimId != DimensionInit.dimID;
    }

    // 生成不可能なバイオーム
    public boolean checkBiome (World world, BlockPos pos, Biome biome) {
    	return !BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST);
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
    	this.addGen(world, pos);
    }

	public void addGen(World world, BlockPos pos) {

		pos = pos.up();

		world.setBlockState(pos.add(7, 0, 4), BlockInit.smspaner.getDefaultState(), 3);
		world.setBlockState(pos.add(11, 0, 4), BlockInit.smspaner.getDefaultState(), 3);

		TileSMSpaner ti1 = (TileSMSpaner) world.getTileEntity(pos.add(7, 0, 4));
		ti1.data = this.getRandInt(world);
		TileSMSpaner ti2 = (TileSMSpaner) world.getTileEntity(pos.add(11, 0, 4));
		ti2.data = this.getRandInt(world);

		IBlockState state = BlockInit.lemon_trapdoor.getDefaultState().withProperty(BlockTrapDoor.OPEN, Boolean.valueOf(true)).withProperty(BlockTrapDoor.FACING, EnumFacing.EAST);
		world.setBlockState(pos.add(27, 3, 14), state, 3);
		world.setBlockState(pos.add(27, 3, 18), state, 3);
	}

	// 乱数取得
	public int getRandInt (World world) {
		int rand = world.rand.nextInt(5);
		return rand == 3 ? 0 : rand;
	}

	public void SetMob (World world, MobSpawnerBaseLogic mob) {

		switch (world.rand.nextInt(6)) {
		case 0:
			mob.setEntityId(EntityList.getKey(EntityZombieVillager.class));
			break;
		case 1:
			mob.setEntityId(EntityList.getKey(EntityWitherSkeleton.class));
			break;
		case 2:
			mob.setEntityId(EntityList.getKey(EntityHusk.class));
			break;
		case 3:
			mob.setEntityId(EntityList.getKey(EntityCaveSpider.class));
			break;
		case 4:
			mob.setEntityId(EntityList.getKey(EntityEvoker.class));
			break;
		case 5:
			mob.setEntityId(EntityList.getKey(EntitySilverfish.class));
			break;
		}
	}
}
