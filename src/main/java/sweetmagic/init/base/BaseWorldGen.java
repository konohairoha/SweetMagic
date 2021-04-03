package sweetmagic.init.base;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.IWorldGenerator;
import sweetmagic.init.tile.chest.TileWoodChest;
import sweetmagic.util.WorldHelper;

public class BaseWorldGen implements IWorldGenerator {

	public Random rand;
	public BlockPos pos;
	public int minChance;
	public int maxChance;
	public int minY;
	public int maxY;
	public int seedRand = 10;
	public String name;

    public BaseWorldGen() {
		this.minChance = 2;
		this.maxChance = 200;
		this.minY = 63;
		this.maxY = 73;
    }

    @Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator gen, IChunkProvider pro) {

    	// コンフィグ確認
    	if (!this.checkConfig() || this.checkFeatures(world) || WorldHelper.isFlat(world) ) { return; }

		//ネザー、エンドでは生成しない
		int dimId = world.provider.getDimension();
		if (this.checkDimension(dimId)) { return; }

		// 生成時のチェックして生成
		if(this.checkGen(rand, chunkX, chunkZ, world)) {
			this.generate(world, this.pos);
		}
	}

    // コンフィグ確認
    public boolean checkConfig () {
    	return true;
    }

    public boolean checkDimension (int dimId) {
    	return dimId == 1 || dimId == -1;
    }

    public boolean checkVillage (World world, BlockPos pos) {
    	return world.villageCollection.getNearestVillage(pos, 6) != null;
    }

    //生成条件
    public boolean checkGen(Random rand, int chunkX, int chunkZ, World world) {

		// 乱数の取得
    	this.rand = this.getRand(chunkX, chunkZ, world);

    	// チャンス少ないなら終了
		if (this.rand.nextInt(this.maxChance) > this.minChance) { return false; }

		// x軸、z軸選定
		int posX = (chunkX << 4) + this.rand.nextInt(8) - this.rand.nextInt(8);
		int posZ = (chunkZ << 4) + this.rand.nextInt(8) - this.rand.nextInt(8);

		// バイオーム判定
		BlockPos pos = new BlockPos(posX, 60, posZ);
		Biome biome = world.getBiomeForCoordsBody(pos);

		// 砂漠以外ならか村の近くなら終了
		if (this.checkBiome(world, pos, biome) || this.checkVillage(world, pos)) { return false; }

		// 高さ判定
		for (int y = this.minY; y < this.maxY; y++) {

			this.pos = new BlockPos(posX, y, posZ);
			IBlockState state = world.getBlockState(this.pos);

		    // 生成できる座標であるか
			if (this.checkBlock(world, state, this.pos)) { return true; }
		}
		return false;
	}

    // 乱数の取得
    public Random getRand (int chunkX, int chunkZ, World world) {
    	return new Random(world.getSeed() + chunkX + chunkZ * 48 + this.seedRand);
    }

    // 生成不可能なバイオーム
    public boolean checkBiome (World world, BlockPos pos, Biome biome) {
    	return biome != Biomes.PLAINS;
    }

    // 生成できる座標であるか
    public boolean checkBlock (World world, IBlockState state, BlockPos pos) {
    	return world.canSeeSky(pos.up()) && state.getMaterial() == Material.GRASS;
    }

    public boolean isReplaceable(World world, IBlockState state, BlockPos pos) {
		return state.getBlock().isAir(state, world, pos) || state.getMaterial().isReplaceable();
	}

    // 生成物の内容
    public void generate(World world, BlockPos pos) {}

	public void setMobSpawner (World world, Random rand, BlockPos pos) {
		world.setBlockToAir(pos);
		world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 3);
		this.setSpawner(world, rand, pos);
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

	public void setChest (World world, Random rand, BlockPos pos, List<ItemStack> list, int chance) {

		//宝箱の生成
		TileEntity chest = world.getTileEntity(pos);
		if (chest != null && chest instanceof TileEntityChest) {
			for (int l = 0; l < 27; l++) {
				int r = this.rand.nextInt(chance);
				if (r < list.size()) {
					ItemStack ret = list.get(r);
					((TileEntityChest) chest).setInventorySlotContents(l, ret);
				}
			}
		}
	}

	// ルートテーブルの設定
	public void setLootTable (World world, Random rand, BlockPos pos, ResourceLocation src) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityChest) {
			((TileEntityChest) tile).setLootTable(src, rand.nextLong());
		}
	}

	// ルートテーブルの設定
	public void setLootTable (World world, Random rand, BlockPos pos, ResourceLocation src, float chance) {

		if (world.isRemote) { return; }

		TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof TileWoodChest)) { return; }

    	TileWoodChest chest = (TileWoodChest) tile;

    	for (int i = 0; i < chest.getInvSize(); i++) {

			if (rand.nextFloat() >= chance || !chest.getChestItem(i).isEmpty()) { continue; }

			// ルートテーブルをリストに入れて取り出してインベントリに入れる
			LootContext.Builder lootcontext = new LootContext.Builder((WorldServer) world);
			LootTable loot =  world.getLootTableManager().getLootTableFromLocation(src);

			List<ItemStack> items =loot.generateLootForPools(rand, lootcontext.build());
			if (items.isEmpty() || items.size() < 0) { continue; }

			ItemStack stack = items.get(rand.nextInt(items.size()));

			chest.chestInv.insertItem(i, stack, false);
    	}
	}

	// 構造物の生成が有効か
	public boolean checkFeatures (World world) {
		return !world.getWorldInfo().isMapFeaturesEnabled();
	}
}
