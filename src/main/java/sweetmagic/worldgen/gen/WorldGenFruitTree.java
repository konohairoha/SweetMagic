package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.blocks.FruitLeaves;
import sweetmagic.util.SweetState;

public class WorldGenFruitTree extends WorldGenAbstractTree {

    private static final Block LOG = BlockInit.orange_log;
    private static final Block LEAVE = BlockInit.orange_leaves;
    private final IBlockState log;
    private final IBlockState leave;
    public int growValue = 0;

    public WorldGenFruitTree(boolean flag) {
        this(flag, LOG, LEAVE);
    }

    public WorldGenFruitTree(boolean notify, Block log, Block leave) {
        super(true);

        this.growValue = notify ? 0 : 2;

        if (leave instanceof FruitLeaves) {
    		this.leave = leave.getDefaultState().withProperty(SweetState.STAGE3, this.growValue);
        }

        else {
        	this.leave = leave.getDefaultState();
        }

		this.log = log.getDefaultState();
    }

    public boolean generate(World world, Random rand, BlockPos pos) {

		if (this.checkBlock(world.getBlockState(pos.down()).getBlock())) { return false; }

		for (int y = 1; y <= 5; y++) {
			Material mate = world.getBlockState(pos.up(y)).getMaterial();
			if (mate != mate.AIR && mate == Material.PLANTS){ return false; }
		}

		world.setBlockToAir(pos);

		//葉っぱ一段目
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				this.setBlock(world, pos.add(x, 3, z), this.leave);
			}
		}

		//葉っぱ２段目
		for (int x = -1; x <= 1; x++) {
			for (int z = -2; z <= 2; z++) {
				this.setBlock(world, pos.add(x, 4, z), this.leave);
				this.setBlock(world, pos.add(z, 4, x), this.leave);
			}
		}

		//葉っぱ3段目
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				this.setBlock(world, pos.add(x, 5, z), this.leave);
			}
		}

		//葉っぱ最上段
		for (int z = -1; z <= 1; z++) {
			this.setBlock(world, pos.add(0, 6, z), this.leave);
			this.setBlock(world, pos.add(z, 6, 0), this.leave);
		}

		//原木
		for (int y = 0; y <= 5; y++) {
			world.setBlockState(pos.add(0, y, 0), this.log);
		}

		if (this.leave.getBlock() != BlockInit.chestnut_leaves) { return true; }
		IBlockState chestnut = BlockInit.chestnut_plant.getDefaultState().withProperty(SweetState.STAGE3, this.growValue);

		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				if (rand.nextInt(5) != 0 || (x == 0 && z == 0)) { continue; }
				world.setBlockState(pos.add(x, 2, z), chestnut);
				break;
			}
		}

		return true;
    }

	// ブロックの設置
	public void setBlock (World world, BlockPos pos, IBlockState state) {
		if (this.isAir(world, pos)) {
			this.setBlockAndNotifyAdequately(world, pos, state);
		}
	}

	public boolean isAir(World world, BlockPos pos) {
		return world.getBlockState(pos).getMaterial() == Material.AIR;
	}

	// 草か土かチェック
	public boolean checkBlock (Block block) {
		return block != Blocks.DIRT && block != Blocks.GRASS;
	}
}
