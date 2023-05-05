package sweetmagic.worldgen.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySkeleton;
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

public class CemeteryGen extends BaseWorldGen {

	public static List<ItemStack> chestA = new ArrayList<>();
	public static List<ItemStack> chestB = new ArrayList<>();
	public static List<ItemStack> chestC = new ArrayList<>();
	private static final WorldGenStructure SM_HOUSE = new WorldGenStructure("tomb");

    public CemeteryGen() {
		this.maxChance = SMConfig.dungeon_spawnchance;
		this.minChance = 1;
		this.seedRand = 10;
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
    	return biome != Biomes.PLAINS && biome != BiomeInit.FLUITFOREST;
    }

    // 生成できる座標であるか
    public boolean checkBlock (World world, IBlockState state, BlockPos pos) {
    	return world.canSeeSky(pos.up()) && state.getMaterial() == Material.GRASS;
    }

    //生成物の内容
    public void generate(World world, BlockPos pos) {

    	pos = pos.down(17);

    	for (int x = 2; x < 8; x++) {
    		for (int z = 2; z < 12; z++) {
    			Block block = world.getBlockState(pos.add(x + 20, 17, z + 8)).getBlock();
        		if (block != Blocks.AIR && !(block instanceof BlockBush)) {
        			return;
        		}
        	}
    	}

        WorldGenerator gen = this.SM_HOUSE;
    	gen.generate(world, this.rand, pos);

    	// スポナーとその中身を設定
    	this.setMobSpawner(world, rand, pos.add(4, 0, 8));
    	this.setMobSpawner(world, rand, pos.add(7, 7, 3));
    	this.setMobSpawner(world, rand, pos.add(15, 7, 19));
    	this.setMobSpawner(world, rand, pos.add(25, 7, 4));

    	// 隠しチェスト
    	this.setChest(world, rand, pos.add(12, 2, 1), chestA, SMConfig.dungeon_lootchance);
    	this.setChest(world, rand, pos.add(13, 2, 1), chestA, SMConfig.dungeon_lootchance);

    	// 通常チェスト
    	this.setChest(world, rand, pos.add(15, 7, 3), chestB, SMConfig.dungeon_lootchance);

    	// ご褒美
    	this.setChest(world, rand, pos.add(25, 3, 20), chestC, SMConfig.dungeon_lootchance);
    	this.setChest(world, rand, pos.add(26, 3, 20), chestC, SMConfig.dungeon_lootchance);
    }

	public void setSpawner (World world, Random rand, BlockPos pos) {

		TileEntity tile = world.getTileEntity(pos);
		int rnd = rand.nextInt(2);
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
			}
		}
	}

	public static void initLootTable () {
		setLootChestA();
		setLootChestB();
		setLootChestC();
	}

    //ルートテーブルの内容設定
	public static void setLootChestA() {
		Random rand = new Random();
        chestA.add(new ItemStack(Items.BOOK, rand.nextInt(4) + 1));
        chestA.add(new ItemStack(Items.ENDER_PEARL, rand.nextInt(3) + 1));
        chestA.add(new ItemStack(Items.BLAZE_ROD, rand.nextInt(4) + 1));
        chestA.add(new ItemStack(ItemInit.alternative_ingot, rand.nextInt(2) + 1));
        chestA.add(new ItemStack(ItemInit.clerodendrum_seed, rand.nextInt(2) + 1));
        chestA.add(new ItemStack(ItemInit.fire_nasturtium_seed, rand.nextInt(2) + 1));
        chestA.add(new ItemStack(ItemInit.sticky_stuff_seed, rand.nextInt(2) + 1));
        chestA.add(new ItemStack(ItemInit.dm_seed, rand.nextInt(2) + 1));
        chestA.add(new ItemStack(ItemInit.b_mf_bottle, rand.nextInt(4) + 1));
	}

    //ルートテーブルの内容設定
	public static void setLootChestB() {
		Random rand = new Random();
        chestB.add(new ItemStack(Blocks.WEB));
        chestB.add(new ItemStack(ItemInit.magicmeal, rand.nextInt(2) + 1));
        chestB.add(new ItemStack(ItemInit.moonblossom_seed, rand.nextInt(2) + 1));
        chestB.add(new ItemStack(ItemInit.glowflower_seed, rand.nextInt(2) + 1));
        chestB.add(new ItemStack(ItemInit.sannyflower_seed, rand.nextInt(2) + 1));
        chestB.add(new ItemStack(ItemInit.ender_shard, rand.nextInt(2) + 1));
        chestB.add(new ItemStack(ItemInit.blank_page, rand.nextInt(2) + 1));
        chestA.add(new ItemStack(Items.GLASS_BOTTLE, rand.nextInt(8) + 1));
	}

    //ルートテーブルの内容設定
	public static void setLootChestC() {
		Random rand = new Random();
        chestC.add(new ItemStack(ItemInit.aether_crystal, rand.nextInt(2) + 1));
        chestC.add(new ItemStack(Items.IRON_INGOT, rand.nextInt(2) + 1));
        chestC.add(new ItemStack(Items.GOLD_INGOT, rand.nextInt(2) + 1));
        chestC.add(new ItemStack(ItemInit.mysterious_page, rand.nextInt(2) + 1));
        chestC.add(new ItemStack(ItemInit.mf_sbottle, rand.nextInt(2) + 1));
        chestC.add(new ItemStack(ItemInit.magic_book));
        chestC.add(new ItemStack(Items.EMERALD));
        chestC.add(new ItemStack(Items.GOLDEN_APPLE));
        chestC.add(new ItemStack(Items.DIAMOND));
        chestC.add(new ItemStack(Items.GHAST_TEAR));
        chestA.add(new ItemStack(Items.GLASS_BOTTLE, rand.nextInt(4) + 1));
	}
}
