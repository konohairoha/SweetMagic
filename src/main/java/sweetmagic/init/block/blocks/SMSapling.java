package sweetmagic.init.block.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.EnumPlantType;
import sweetmagic.api.iblock.ISmeltItemBlock;
import sweetmagic.init.BlockInit;
import sweetmagic.worldgen.gen.WorldGenCoconutTree;
import sweetmagic.worldgen.gen.WorldGenEstor;
import sweetmagic.worldgen.gen.WorldGenFruitTree;
import sweetmagic.worldgen.gen.WorldGenPrsmTree;

public class SMSapling extends BlockBush implements IGrowable, ISmeltItemBlock {

	private static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09D, 0D, 0.09D, 0.9D, 0.8D, 0.9D);
	private final int data;

	public SMSapling(String name, int data) {
        super(Material.PLANTS);
        setRegistryName(name);
        setUnlocalizedName(name);
		setSoundType(SoundType.PLANT);
		this.data = data;
		BlockInit.blockList.add(this);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos){
		return EnumPlantType.Plains;
	}

	/**
	 * 0 = オレンジ
	 * 1 = レモン
	 * 2 = 栗
	 * 3 = ヤシ
	 * 4 = プリズム
	 * 5 = バナナ
	 * 6 = エストール
	 * 7 = 桃
	 */

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {

		WorldGenerator gen = null;

		switch (this.data) {

		// オレンジ
		case 0:
			gen = new WorldGenFruitTree(true, this.getLog(), this.getLeave());
			break;

		// レモン
		case 1:
			gen = new WorldGenFruitTree(true, this.getLog(), this.getLeave());
			break;

		// 栗
		case 2:
			gen = new WorldGenFruitTree(true, this.getLog(), this.getLeave());
			break;

		// ヤシ
		case 3:
			gen = new WorldGenCoconutTree(true, this.getLog(), this.getLeave(), BlockInit.coconut_plant, 0);
			break;

		// プリズム
		case 4:
			gen = new WorldGenPrsmTree(this.getLog(), this.getLeave(), false);
			break;

		// バナナ
		case 5:
			gen = new WorldGenCoconutTree(true, this.getLog(), this.getLeave(), BlockInit.banana_plant, 1);
			break;

		// エストール
		case 6:
			gen = new WorldGenEstor(true, this.getLog(), this.getLeave());
			break;

		// 桃
		case 7:
			gen = new WorldGenFruitTree(true, this.getLog(), this.getLeave());
			break;
		}

		if (gen != null) {
	    	gen.generate(world, rand, pos);
		}
	}

	// 原木の取得
	public Block getLog () {

		switch (this.data) {
		case 0: return BlockInit.orange_log;	// オレンジ
		case 1: return BlockInit.lemon_log;	// レモン
		case 2: return BlockInit.chestnut_log;	// 栗
		case 3: return BlockInit.coconut_log;	// ヤシ
		case 4: return BlockInit.prism_log;	// プリズム
		case 5: return Blocks.LOG;				// バナナ
		case 6: return BlockInit.estor_log;	// エストール
		case 7: return BlockInit.peach_log;	// 桃
		}

		return BlockInit.orange_log;
	}

	// 葉っぱの取得
	public Block getLeave () {

		switch (this.data) {
		case 0: return BlockInit.orange_leaves;	// オレンジ
		case 1: return BlockInit.lemon_leaves;		// レモン
		case 2: return BlockInit.chestnut_leaves;	// 栗
		case 3: return BlockInit.coconut_leaves;	// ヤシ
		case 4: return BlockInit.prism_leaves;		// プリズム
		case 5: return BlockInit.banana_leaves;	// バナナ
		case 6: return BlockInit.estor_leaves;		// エストール
		case 7: return BlockInit.peach_leaves;		// 桃
		}

		return BlockInit.orange_leaves;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(state);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return SAPLING_AABB;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (world.isRemote) { return; }

		super.updateTick(world, pos, state, rand);
		if (world.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(6) == 0) {
			this.grow(world, rand, pos, state);
		}
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return (double) world.rand.nextFloat() < 0.45D;
	}

	//土、草、耕地に置いても壊れないようにする
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		Material m = world.getBlockState(pos.down()).getMaterial();
		return (m == Material.SAND && this.data == 3) || m == Material.GROUND || m == Material.GRASS;
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		Material m = world.getBlockState(pos.down()).getMaterial();
		return (m == Material.SAND && this.data == 3) || m == Material.GROUND || m == Material.GRASS;
	}

	// 精錬時間の取得
	@Override
	public int getSmeltTime() {
		return 100;
	}
}
