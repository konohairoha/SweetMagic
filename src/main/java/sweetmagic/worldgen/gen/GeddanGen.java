package sweetmagic.worldgen.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import sweetmagic.config.SMConfig;
import sweetmagic.init.DimensionInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.base.BaseWorldGen;
import sweetmagic.init.entity.monster.EntityArchSpider;
import sweetmagic.init.entity.monster.EntityPhantomZombie;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.worldgen.structures.WorldGenStructure;

public class GeddanGen extends BaseWorldGen {

	public static List<ItemStack> chestA = new ArrayList<>();
	public final WorldGenStructure BOT = new WorldGenStructure("geddan_bot");
	public final WorldGenStructure UP = new WorldGenStructure("geddan_up");

    public GeddanGen() {
		this.maxChance = SMConfig.dungeon_spawnchance * 2;
		this.minChance = 0;
		this.seedRand = 256;
    }

    // ディメンション確認
    public boolean checkDimension (int dimId) {
    	return dimId != DimensionInit.dimID;
    }

    // 生成不可能なバイオーム
	public boolean checkBiome(World world, BlockPos pos, Biome biome) {
		return !BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST);
	}

    //生成物の内容
    public void generate(World world, BlockPos pos) {

    	pos = pos.down(4);

        WorldGenerator gen = this.BOT;
    	gen.generate(world, this.rand, pos);

        WorldGenerator gen1 = this.UP;
    	gen1.generate(world, this.rand, pos.add(10, 35, 8));

		for (BlockPos p : BlockPos.getAllInBox(pos.add(9, 6, 10), pos.add(18, 24, 20))) {

			IBlockState state = world.getBlockState(p);
			Block block = state.getBlock();
			if (block != Blocks.CHEST && block != Blocks.BEDROCK) { continue; }

			boolean isChest = block == Blocks.CHEST;

	    	// チェストなら
			if (isChest) {
		    	this.setChest(world, rand, p, chestA, SMConfig.dungeon_lootchance);
			}

			// スポナーなら
			else {
				world.setBlockState(p, Blocks.MOB_SPAWNER.getDefaultState(), 2);
				this.setSpawner(world, world.rand, p);
			}
		}
    }

	public void setSpawner (World world, Random rand, BlockPos pos) {

		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityMobSpawner) {

			int rnd = rand.nextInt(6);
			MobSpawnerBaseLogic logic = ((TileEntityMobSpawner) tile).getSpawnerBaseLogic();

			switch (rnd) {
			case 0:
				logic.setEntityId(EntityList.getKey(EntitySkeleton.class));
				break;
			case 1:
				logic.setEntityId(EntityList.getKey(EntityZombie.class));
				break;
			case 2:
				logic.setEntityId(EntityList.getKey(EntityPhantomZombie.class));
				break;
			case 3:
				logic.setEntityId(EntityList.getKey(EntitySkullFrost.class));
				break;
			case 4:
				logic.setEntityId(EntityList.getKey(EntitySpider.class));
				break;
			case 5:
				logic.setEntityId(EntityList.getKey(EntityArchSpider.class));
				break;
			}
		}
	}

    //ルートテーブルの内容設定
	public static void setLootChestA() {
		Random rand = new Random();
        chestA.add(new ItemStack(ItemInit.aether_crystal, rand.nextInt(32) + 1));
        chestA.add(new ItemStack(ItemInit.divine_crystal, rand.nextInt(8) + 1));
        chestA.add(new ItemStack(ItemInit.pure_crystal, 1));
        chestA.add(new ItemStack(ItemInit.mf_sbottle, rand.nextInt(16) + 1));
        chestA.add(new ItemStack(ItemInit.mf_bottle, rand.nextInt(6) + 1));
        chestA.add(new ItemStack(ItemInit.mystical_page, rand.nextInt(4) + 1));
        chestA.add(new ItemStack(ItemInit.prizmium, rand.nextInt(8) + 1));
	}
}
