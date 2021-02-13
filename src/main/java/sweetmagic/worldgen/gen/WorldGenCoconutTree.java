package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.blocks.SMLeaves;
import sweetmagic.util.SweetState;

public class WorldGenCoconutTree extends WorldGenAbstractTree {

    private static final Block LOG = BlockInit.coconut_log;
    private static final Block LEAVE = BlockInit.coconut_leaves;
    private static final Block PLANTE = BlockInit.coconut_plant;
    private IBlockState log;
    private final IBlockState leave;
    private final IBlockState plante;
    public final int data;
    public int growValue = 0;

    public WorldGenCoconutTree(boolean flag) {
        this(flag, LOG, LEAVE, PLANTE, 0);
    }

    public WorldGenCoconutTree(boolean notify, Block log, Block leave, Block plante, int data) {
        super(false);
        this.growValue = notify ? 0 : 2;
        this.leave = leave.getDefaultState().withProperty(SMLeaves.CHECK_DECAY, false).withProperty(SMLeaves.DECAYABLE, false);
		this.log = log.getDefaultState();
		this.plante = plante.getDefaultState().withProperty(SweetState.STAGE3, this.growValue);
		this.data = data;
    }

    public boolean generate(World world, Random rand, BlockPos pos) {

		if (this.checkBlock(world.getBlockState(pos.down()).getBlock())) { return false; }

		for (int y = 1; y <= 8; y++) {
			if (!world.isAirBlock(pos.add(0, y, 0))) {
				if (world.getBlockState(pos.add(0, y, 0)).getMaterial() == Material.PLANTS){ return false; }
			}
		}

		world.setBlockToAir(pos);
		IBlockState AIR = Blocks.AIR.getDefaultState();

		if (this.data == 0) {
			this.genCoconut(world, rand, pos, this.leave, AIR);
		}

		else {
			this.genBanana(world, rand, pos, this.leave, AIR);
		}

		return true;
    }

    public void genCoconut (World world, Random rand, BlockPos pos, IBlockState smLeave, IBlockState AIR) {

		// 葉っぱ一段目
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				for (int y = 7; y <= 9; y++) {
					this.setBlock(world, pos.add(x, y, z), smLeave);
				}
			}
		}

		for (int x = -4; x <= 4; x++) {
			this.setBlock(world, pos.add(x, 9, 0), smLeave);
			this.setBlock(world, pos.add(0, 9, x), smLeave);
		}

		world.setBlockState(pos.add(-1, 7, -1), AIR);
		world.setBlockState(pos.add(-1, 7, 1), AIR);
		world.setBlockState(pos.add(1, 7, -1), AIR);
		world.setBlockState(pos.add(1, 7, 1), AIR);

		for (int y = 7; y <= 8; y++) {
			this.setBlock(world, pos.add(5, y, 0), smLeave);
			this.setBlock(world, pos.add(0, y, 5), smLeave);
			this.setBlock(world, pos.add(-5, y, 0), smLeave);
			this.setBlock(world, pos.add(0, y, -5), smLeave);
			this.setBlock(world, pos.add(-4, y, -4), smLeave);
			this.setBlock(world, pos.add(-4, y, 4), smLeave);
			this.setBlock(world, pos.add(4, y, -4), smLeave);
			this.setBlock(world, pos.add(4, y, 4), smLeave);
		}

		for (int x = -1; x <= 1; x++) {
			this.setBlock(world, pos.add(x, 6, 0), this.plante);
			this.setBlock(world, pos.add(x, 7, 0), smLeave);
			this.setBlock(world, pos.add(x, 10, 0), smLeave);
			this.setBlock(world, pos.add(0, 6, x), this.plante);
			this.setBlock(world, pos.add(0, 7, x), smLeave);
			this.setBlock(world, pos.add(0, 10, x), smLeave);
		}

		this.setBlock(world, pos.add(-2, 9, -2), smLeave);
		this.setBlock(world, pos.add(-2, 9, 2), smLeave);
		this.setBlock(world, pos.add(2, 9, -2), smLeave);
		this.setBlock(world, pos.add(2, 9, 2), smLeave);

		this.setBlock(world, pos.add(-3, 9, -3), smLeave);
		this.setBlock(world, pos.add(-3, 9, 3), smLeave);
		this.setBlock(world, pos.add(3, 9, -3), smLeave);
		this.setBlock(world, pos.add(3, 9, 3), smLeave);

		//原木
		for (int y = 0; y <= 8; y++) {
			world.setBlockState(pos.add(0, y, 0), this.log, 3);
		}
    }

    public void genBanana (World world, Random rand, BlockPos pos, IBlockState smLeave, IBlockState AIR) {

    	this.log = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);

    	for (int x = -2; x <= 2; x++) {
			this.setBlock(world, pos.add(x, 4, 0), smLeave);
			this.setBlock(world, pos.add(0, 4, x), smLeave);
    	}

		this.setBlock(world, pos.add(0, 3, 3), smLeave);
		this.setBlock(world, pos.add(0, 3, -3), smLeave);
		this.setBlock(world, pos.add(3, 3, 0), smLeave);
		this.setBlock(world, pos.add(-3, 3, 0), smLeave);

		this.setBlock(world, pos.add(0, 5, 0), smLeave);
		this.setBlock(world, pos.add(1, 5, 1), smLeave);
		this.setBlock(world, pos.add(1, 5, -1), smLeave);
		this.setBlock(world, pos.add(-1, 5, 1), smLeave);
		this.setBlock(world, pos.add(-1, 5, -1), smLeave);

		this.setBlock(world, pos.add(2, 4, 2), smLeave);
		this.setBlock(world, pos.add(2, 4, -2), smLeave);
		this.setBlock(world, pos.add(-2, 4, 2), smLeave);
		this.setBlock(world, pos.add(-2, 4, -2), smLeave);

		this.setBlock(world, pos.add(0, 3, 1), this.plante);
		this.setBlock(world, pos.add(0, 3, -1), this.plante);
		this.setBlock(world, pos.add(1, 3, 0), this.plante);
		this.setBlock(world, pos.add(-1, 3, 0), this.plante);

		//原木
		for (int y = 0; y <= 4; y++) {
			world.setBlockState(pos.add(0, y, 0), this.log, 3);
		}
    }

	// ブロックの設置
	public void setBlock (World world, BlockPos pos, IBlockState state) {
		if (world.isAirBlock(pos)) {
			world.setBlockState(pos, state, 3);
		}
	}

	// 草か土かチェック
	public boolean checkBlock (Block block) {
		return block != Blocks.DIRT && block != Blocks.GRASS;
	}
}
