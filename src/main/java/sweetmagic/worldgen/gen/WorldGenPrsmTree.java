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
import sweetmagic.init.block.blocks.SMLeaves;

public class WorldGenPrsmTree extends WorldGenAbstractTree {

	public static final Block LOG = BlockInit.prism_log;
    public static final Block LEAVE = BlockInit.prism_leaves;
    public final IBlockState log;
    public final IBlockState leave;
    public boolean isSmall = false;

    public WorldGenPrsmTree(boolean flag) {
        this(LOG, LEAVE, false);
    }

    public WorldGenPrsmTree(Block log, Block leave, boolean isSmall) {
        super(false);
        this.leave = leave.getDefaultState();
		this.log = log.getDefaultState();
		this.isSmall = isSmall;
    }

    // 生成
	public boolean generate(World world, Random rand, BlockPos pos) {

		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				BlockPos p = pos.add(x, -1, z);
				if (!world.isAirBlock(p) && this.checkBlock(world.getBlockState(p).getBlock())){ return false; }
			}
		}

		for (int y = 1; y <= 8; y++) {
			if (!world.isAirBlock(pos.add(0, y, 0))) {
				if (world.getBlockState(pos.add(0, y, 0)).getMaterial() == Material.PLANTS){ return false; }
			}
		}

		world.setBlockToAir(pos);

		IBlockState log = this.log;
		IBlockState smLeaves = this.leave.withProperty(SMLeaves.CHECK_DECAY, false);

		// 大きい木
		if (!this.isSmall) {
			this.genTree(world, rand, pos, log, smLeaves);
		}

		// 小さき木
		else {
			this.genSmallTree(world, rand, pos, log, smLeaves);
		}

		return true;
    }

	// 草か土かチェック
	public boolean checkBlock (Block block) {
		return block != Blocks.DIRT && block != Blocks.GRASS;
	}

	// 普通の木
    public void genTree (World world, Random rand, BlockPos pos, IBlockState log, IBlockState smLeaves) {

		int maxHeight = rand.nextInt(24) + 24;

		this.subTrunk(world, pos.north(), log);
		this.subTrunk(world, pos.south(), log);
		this.subTrunk(world, pos.west(), log);
		this.subTrunk(world, pos.east(), log);

		for (int y = 0; y < maxHeight; y++) {
			this.setBlock(world, pos.up(y), log);
		}

		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				for (int y = 19; y <= maxHeight - 4; y++) {

					if (rand.nextInt(20) != 0) { continue; }
					this.setLeave(world, pos.add(x, y, z), smLeaves, log);

					boolean isOverX = x == 2 || x == -2;
					boolean isOverZ = z == 2 || z == -2;

					if ( isOverX && isOverZ ) {
						this.setBlock(world, pos.add(x / 2, y - 1, z / 2), log);
					}

					else if (isOverX) {
						this.setBlock(world, pos.add(x / 2, y - 1, z), log);
					}

					else if (isOverZ) {
						this.setBlock(world, pos.add(x, y - 1, z / 2), log);
					}

					continue;
				}
			}
		}

		BlockPos top = pos.add(0, maxHeight - 1, 0);

		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				this.setBlock(world, top.add(x, 0, z), smLeaves);
			}
		}

		this.setAir(world, top.add(-2, 0, -2), smLeaves);
		this.setAir(world, top.add(2, 0, -2), smLeaves);
		this.setAir(world, top.add(-2, 0, 2), smLeaves);
		this.setAir(world, top.add(2, 0, 2), smLeaves);

		this.setBlock(world, top.add(0, 1, -1), smLeaves);
		this.setBlock(world, top.add(-1, 1, 0), smLeaves);
		this.setBlock(world, top.add(0, 1, 1), smLeaves);
		this.setBlock(world, top.add(1, 1, 0), smLeaves);
		this.setBlock(world, top.up(), smLeaves);
		this.setBlock(world, top.up(2), smLeaves);
    }

    // 小さい木
    public void genSmallTree (World world, Random rand, BlockPos pos, IBlockState log, IBlockState smLeaves) {

		this.setBlock(world, pos, log);
		this.setBlock(world, pos.north(), smLeaves);
		this.setBlock(world, pos.south(), smLeaves);
		this.setBlock(world, pos.east(), smLeaves);
		this.setBlock(world, pos.west(), smLeaves);
		this.setBlock(world, pos.up(), smLeaves);
    }

    // 主軸の横
	public void subTrunk (World world, BlockPos pos, IBlockState state) {

		Random rand = world.rand;
		int height = rand.nextInt(6) + 8;

		for (int y = 0; y <= height; y++) {
			this.setBlock(world, pos.up(y), state);
		}
	}

	// 葉っぱ
	public void setLeave (World world, BlockPos pos, IBlockState leave, IBlockState log) {

		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				this.setBlock(world, pos.add(x, 0, z), leave);
				this.setBlock(world, pos.add(x, 1, z), leave);
			}
		}

		this.setAir(world, pos.add(-1, 1, -1), leave);
		this.setAir(world, pos.add(1, 1, -1), leave);
		this.setAir(world, pos.add(-1, 1, 1), leave);
		this.setAir(world, pos.add(1, 1, 1), leave);

		world.setBlockState(pos, log, 3);
	}

	// ブロックの設置
	public void setBlock (World world, BlockPos pos, IBlockState state) {
		if (world.isAirBlock(pos)) {
			world.setBlockState(pos, state, 3);
		}
	}

	// 空気
	public void setAir (World world, BlockPos pos, IBlockState state) {
		if (world.getBlockState(pos).getBlock() == state.getBlock()) {
			world.setBlockToAir(pos);
		}
	}
}
