package sweetmagic.init.block.blocks;

import java.util.Random;

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

	protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.099D, 0.0D, 0.099D, 0.89D, 0.8D, 0.89D);
	public final int data;

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
	 */

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {

		WorldGenerator gen = null;

		switch (this.data) {

		// オレンジ
		case 0:
			gen = new WorldGenFruitTree(true, BlockInit.orange_log, BlockInit.orange_leaves);
			break;

		// レモン
		case 1:
			gen = new WorldGenFruitTree(true, BlockInit.lemon_log, BlockInit.lemon_leaves);
			break;

		// 栗
		case 2:
			gen = new WorldGenFruitTree(true, BlockInit.chestnut_log, BlockInit.chestnut_leaves);
			break;

		// ヤシ
		case 3:
			gen = new WorldGenCoconutTree(true, BlockInit.coconut_log, BlockInit.coconut_leaves, BlockInit.coconut_plant, 0);
			break;

		// プリズム
		case 4:
			gen = new WorldGenPrsmTree(BlockInit.prism_log, BlockInit.prism_leaves, false);
			break;

		// バナナ
		case 5:
			gen = new WorldGenCoconutTree(true, Blocks.LOG, BlockInit.banana_leaves, BlockInit.banana_plant, 1);
			break;

		// エストール
		case 6:
			gen = new WorldGenEstor(true, BlockInit.estor_log, BlockInit.estor_leaves);
			break;
		}

		if (gen != null) {
	    	gen.generate(world, rand, pos);
		}
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
