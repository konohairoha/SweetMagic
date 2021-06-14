package sweetmagic.worldgen.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import sweetmagic.config.SMConfig;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.base.BaseWorldGen;
import sweetmagic.init.tile.magic.TileSpawnStone;
import sweetmagic.worldgen.structures.WorldGenStructure;

public class WellGen extends BaseWorldGen {

	public static List<ItemStack> chestA = new ArrayList<>();
	public static List<ItemStack> chestB = new ArrayList<>();
	private static final WorldGenStructure SM_HOUSE = new WorldGenStructure("ido");

    public WellGen() {
		this.maxChance = SMConfig.dungeon_spawnchance * 3;
		this.minChance = 0;
		this.seedRand = 20;
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
    	return !BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST);
    }

    // 生成できる座標であるか
    public boolean checkBlock (World world, IBlockState state, BlockPos pos) {
    	return world.canSeeSky(pos.up()) && state.getMaterial() == Material.GRASS;
    }

    //生成物の内容
    public void generate(World world, BlockPos pos) {

		BlockPos p = pos.down(21);
        WorldGenerator gen = this.SM_HOUSE;
    	gen.generate(world, this.rand, p);

    	// スポナーの中身を設定
    	this.setSpaner(world, p.add(16, 2, 8), 1);
    	this.setSpaner(world, p.add(16, 9, 8), 0);
    	this.setSpaner(world, p.add(16, 9, 41), 2);
    	this.setSpaner(world, p.add(16, 2, 41), 5);

    	// チェストの設定
    	this.setChest(world, this.rand, p.add(33, 8, 20), chestA, SMConfig.dungeon_lootchance);
    	this.setChest(world, this.rand, p.add(32, 8, 20), chestA, SMConfig.dungeon_lootchance);
    	this.setChest(world, this.rand, p.add(33, 8, 29), chestB, SMConfig.dungeon_lootchance);
    	this.setChest(world, this.rand, p.add(32, 8, 29), chestB, SMConfig.dungeon_lootchance);

    	// チェストの中身を確定
		TileEntity chest = world.getTileEntity(p.add(32, 8, 29));
		((TileEntityChest) chest).setInventorySlotContents(0, new ItemStack(ItemInit.veil_darkness));
    }

	public void setSpaner (World world, BlockPos pos, int rand) {
		world.setBlockState(pos, BlockInit.spawn_stone.getDefaultState(), 3);
		TileSpawnStone tile = (TileSpawnStone) world.getTileEntity(pos);
		tile.data = rand;
		tile.isRand = true;
	}

	public static void initLootTable () {
		setLootChestA();
		setLootChestB();
	}

    //ルートテーブルの内容設定
	public static void setLootChestA() {
		Random rand = new Random();
        chestA.add(new ItemStack(ItemInit.dm_flower, rand.nextInt(8) + 1));
        chestA.add(new ItemStack(ItemInit.fire_nasturtium_petal, rand.nextInt(8) + 1));
        chestA.add(new ItemStack(ItemInit.magicmeal, rand.nextInt(8) + 1));
        chestA.add(new ItemStack(ItemInit.stray_soul, rand.nextInt(8) + 1));
        chestA.add(new ItemStack(ItemInit.electronic_orb, rand.nextInt(8) + 1));
        chestA.add(new ItemStack(ItemInit.poison_bottle, rand.nextInt(8) + 1));
        chestA.add(new ItemStack(ItemInit.unmeltable_ice, rand.nextInt(8) + 1));
        chestA.add(new ItemStack(ItemInit.grav_powder, rand.nextInt(8) + 1));
        chestA.add(new ItemStack(ItemInit.tiny_feather, rand.nextInt(8) + 1));
        chestA.add(new ItemStack(ItemInit.prizmium, rand.nextInt(8) + 1));
	}

    //ルートテーブルの内容設定
	public static void setLootChestB() {
		Random rand = new Random();
        chestB.add(new ItemStack(ItemInit.aether_crystal, rand.nextInt(10) + 6));
        chestB.add(new ItemStack(ItemInit.aether_crystal, rand.nextInt(15) + 1));
        chestB.add(new ItemStack(ItemInit.divine_crystal, rand.nextInt(4) + 1));
        chestB.add(new ItemStack(ItemInit.mf_sbottle, rand.nextInt(10) + 1));
        chestB.add(new ItemStack(ItemInit.mf_sbottle, rand.nextInt(6) + 4));
        chestB.add(new ItemStack(ItemInit.mf_bottle, rand.nextInt(4) + 1));
	}
}
