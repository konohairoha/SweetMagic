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
import sweetmagic.init.block.blocks.PlantPot;
import sweetmagic.util.SweetState;

public class WorldGenEstor extends WorldGenAbstractTree {

	public static final Block LOG = BlockInit.prism_log;
    public static final Block LEAVE = BlockInit.prism_leaves;
    public final IBlockState log;
    public final IBlockState leave;
    public int growValue = 0;

    public WorldGenEstor(boolean notify) {
        this(notify, LOG, LEAVE);
    }

    public WorldGenEstor(boolean notify, Block log, Block leave) {
        super(false);
        this.log = log.getDefaultState();

        this.growValue = notify ? 0 : 2;

        if (leave instanceof FruitLeaves) {
    		this.leave = leave.getDefaultState().withProperty(SweetState.STAGE3, this.growValue);
        }

        else {
        	this.leave = leave.getDefaultState();
        }
    }

    // 生成
	public boolean generate(World world, Random rand, BlockPos pos) {

		if (this.checkBlock(world.getBlockState(pos.down()).getBlock())) { return false; }

		for (int y = 1; y <= 5; y++) {
			Material mate = world.getBlockState(pos.up(y)).getMaterial();
			if (mate != mate.AIR && mate == Material.PLANTS){ return false; }
		}

		world.setBlockToAir(pos);

		for (int x = -1; x <= 1; x++) {
			this.setBlock(world, pos.add(x, 2, 0), this.leave);
			this.setBlock(world, pos.add(x, 6, 0), this.leave);
			this.setBlock(world, pos.add(x, 7, 0), this.leave);
			this.setBlock(world, pos.add(0, 2, x), this.leave);
			this.setBlock(world, pos.add(0, 6, x), this.leave);
			this.setBlock(world, pos.add(0, 7, x), this.leave);
		}

		for (int x = -2; x <= 2; x++) {
			for (int z = -1; z <= 1; z++) {
				this.setBlock(world, pos.add(x, 3, z), this.leave);
				this.setBlock(world, pos.add(x, 4, z), this.leave);
				this.setBlock(world, pos.add(x, 5, z), this.leave);
				this.setBlock(world, pos.add(z, 3, x), this.leave);
				this.setBlock(world, pos.add(z, 4, x), this.leave);
				this.setBlock(world, pos.add(z, 5, x), this.leave);
			}
		}

		for (int y = 0; y <= 5; y++) {
			world.setBlockState(pos.add(0, y, 0), this.log, 2);
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
		return block != Blocks.DIRT && block != Blocks.GRASS && !(block instanceof PlantPot);
	}
}
