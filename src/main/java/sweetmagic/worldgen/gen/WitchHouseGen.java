package sweetmagic.worldgen.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import sweetmagic.config.SMConfig;
import sweetmagic.init.BiomeInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.base.BaseWorldGen;
import sweetmagic.worldgen.structures.WorldGenStructure;

public class WitchHouseGen extends BaseWorldGen {

	public static List<ItemStack> chestA = new ArrayList<>();
	public static List<ItemStack> chestB = new ArrayList<>();
	private static final WorldGenStructure SM_HOUSE = new WorldGenStructure("witch_house");

    public WitchHouseGen() {
		this.maxChance = SMConfig.dungeon_spawnchance;
		this.minChance = 0;
		this.seedRand = 20;
    }

    // コンフィグ確認
    public boolean checkConfig () {
    	return SMConfig.isGenStructure;
    }

    // 生成不可能なバイオーム
    public boolean checkBiome (World world, BlockPos pos, Biome biome) {
    	return biome != Biomes.PLAINS && biome != BiomeInit.PRISMFOREST;
    }

    // 生成できる座標であるか
    public boolean checkBlock (World world, IBlockState state, BlockPos pos) {
    	return world.canSeeSky(pos.up()) && state.getMaterial() == Material.GRASS;
    }

    //生成物の内容
    public void generate(World world, BlockPos pos) {

    	pos = new BlockPos (pos.getX(), 63, pos.getZ());

    	for (int x = 0; x < 18; x++) {
    		for (int z = 0; z < 28; z++) {
    			Block block = world.getBlockState(pos.add(x + 1, 4, z + 1)).getBlock();
        		if (block != Blocks.AIR && !(block instanceof BlockBush)) {
        			return;
        		}
        	}
    	}

        WorldGenerator gen = this.SM_HOUSE;
    	gen.generate(world, this.rand, pos);

    	// スポナーの中身を設定
    	this.setMobSpawner(world, rand, pos.add(2, 1, 16));
    	this.setMobSpawner(world, rand, pos.add(7, 1, 16));

    	this.setMobSpawner(world, rand, pos.add(13, 1, 3));
    	this.setMobSpawner(world, rand, pos.add(13, 1, 16));

    	this.setMobSpawner(world, rand, pos.add(5, 8, 10));
    	this.setMobSpawner(world, rand, pos.add(11, 8, 10));

    	// 隠しチェスト
    	this.setChest(world, rand, pos.add(6, 2, 4), chestA, SMConfig.dungeon_lootchance);

    	// 安全地帯
    	this.setChest(world, rand, pos.add(22, 8, 4), chestB, SMConfig.dungeon_lootchance);
    	this.setChest(world, rand, pos.add(22, 8, 5), chestB, SMConfig.dungeon_lootchance);
    }

	public void setSpawner (World world, Random rand, BlockPos pos) {

		TileEntity tile = world.getTileEntity(pos);
		int rnd = rand.nextInt(6);
		if (tile instanceof TileEntityMobSpawner) {

			TileEntityMobSpawner sp = (TileEntityMobSpawner) tile;

			switch (rnd) {
			case 0:
				(sp).getSpawnerBaseLogic()
						.setEntityId(EntityList.getKey(EntitySkeleton.class));
				break;
			case 1:
				(sp).getSpawnerBaseLogic()
						.setEntityId(EntityList.getKey(EntityZombie.class));
				break;
			case 2:
				(sp).getSpawnerBaseLogic()
						.setEntityId(EntityList.getKey(EntityWitch.class));
				break;
			case 3:
				(sp).getSpawnerBaseLogic()
						.setEntityId(EntityList.getKey(EntityHusk.class));
				break;
			case 4:
				(sp).getSpawnerBaseLogic()
						.setEntityId(EntityList.getKey(EntityVindicator.class));
				break;
			case 5:
				(sp).getSpawnerBaseLogic()
						.setEntityId(EntityList.getKey(EntityWitherSkeleton.class));
				break;
			}
		}
	}

	public static void initLootTable () {
		setLootChestA();
		setLootChestB();
	}

    //ルートテーブルの内容設定
	public static void setLootChestA() {
		Random rand = new Random();
        chestA.add(new ItemStack(ItemInit.tiny_feather, rand.nextInt(4) + 1));
        chestA.add(new ItemStack(ItemInit.divine_crystal, rand.nextInt(4) + 1));
        chestA.add(new ItemStack(ItemInit.sannyflower_petal, rand.nextInt(24) + 16));
        chestA.add(new ItemStack(ItemInit.moonblossom_petal, rand.nextInt(24) + 16));
        chestA.add(new ItemStack(ItemInit.mf_bottle, rand.nextInt(4) + 1));
        chestA.add(new ItemStack(ItemInit.mf_sbottle, rand.nextInt(16) + 1));
	}

    //ルートテーブルの内容設定
	public static void setLootChestB() {
		Random rand = new Random();
        chestB.add(new ItemStack(Items.DYE, rand.nextInt(16) + 1, 15));
        chestB.add(new ItemStack(ItemInit.mysterious_page, rand.nextInt(8) + 1));
        chestB.add(new ItemStack(ItemInit.blank_page, rand.nextInt(3) + 1));
        chestA.add(new ItemStack(ItemInit.sannyflower_petal, rand.nextInt(16) + 4));
        chestA.add(new ItemStack(ItemInit.moonblossom_petal, rand.nextInt(16) + 4));
        chestA.add(new ItemStack(ItemInit.divine_crystal, rand.nextInt(2) + 1));
        chestA.add(new ItemStack(ItemInit.aether_crystal, rand.nextInt(16) + 1));
        chestA.add(new ItemStack(ItemInit.aether_crystal_shard, rand.nextInt(42) + 8));
	}
}
