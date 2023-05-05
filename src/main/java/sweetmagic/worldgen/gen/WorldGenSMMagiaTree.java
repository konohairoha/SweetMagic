package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.blocks.PlantPot;
import sweetmagic.init.block.blocks.SMLeaves;
import sweetmagic.init.block.blocks.SMLog;

public class WorldGenSMMagiaTree extends WorldGenAbstractTree {

	public static final Block LOG = BlockInit.prism_log;
    public static final Block LEAVE = BlockInit.prism_leaves;
    public final IBlockState log;
    public final IBlockState leave;
    public int height = 10;
    public int randHeight = 1;

    public WorldGenSMMagiaTree(boolean flag) {
        this(LOG, LEAVE);
    }

    public WorldGenSMMagiaTree(Block log, Block leave) {
        super(true);
        this.leave = leave.getDefaultState();
		this.log = log.getDefaultState();
    }

    // 生成
	public boolean generate(World world, Random rand, BlockPos pos) {

		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (this.checkBlock(world.getBlockState(pos.add(x, -1, z)).getBlock())) { return false; }
			}
		}

		for (int y = 1; y <= 8; y++) {
			Material mate = world.getBlockState(pos.up(y)).getMaterial();
			if (mate != mate.AIR && mate == Material.PLANTS){ return false; }
		}

		world.setBlockToAir(pos);

		IBlockState log = this.log;
		IBlockState smLeaves = this.getLeaveState();

		this.genTree(world, rand, pos, log, smLeaves);

		return true;
    }

	// 草か土かチェック
	public boolean checkBlock (Block block) {
		return block != Blocks.DIRT && block != Blocks.GRASS && !(block instanceof PlantPot);
	}

	// 葉っぱのIBlockStateを取得
	public IBlockState getLeaveState () {
		return this.leave.getBlock() instanceof SMLeaves ? this.leave.withProperty(SMLeaves.CHECK_DECAY, false) : this.leave;
	}

	// 普通の木
    public void genTree (World world, Random rand, BlockPos pos, IBlockState log, IBlockState smLeaves) {

		int maxHeight = rand.nextInt(this.randHeight) + this.height;

		this.subTrunk(world, pos.north(), log, EnumFacing.NORTH);
		this.subTrunk(world, pos.south(), log, EnumFacing.SOUTH);
		this.subTrunk(world, pos.west(), log, EnumFacing.WEST);
		this.subTrunk(world, pos.east(), log, EnumFacing.EAST);

		for (int y = 0; y < maxHeight; y++) {
			this.setBlock(world, pos.up(y), log);
		}

		// 原木最上段
		this.setBlock(world, pos.add(0, maxHeight, -1), log.withProperty(SMLog.LOG_AXIS, SMLog.EnumAxis.Z));
		this.setBlock(world, pos.add(0, maxHeight, 1), log.withProperty(SMLog.LOG_AXIS, SMLog.EnumAxis.Z));
		this.setBlock(world, pos.add(-1, maxHeight, 0), log.withProperty(SMLog.LOG_AXIS, SMLog.EnumAxis.X));
		this.setBlock(world, pos.add(1, maxHeight, 0), log.withProperty(SMLog.LOG_AXIS, SMLog.EnumAxis.X));

		BlockPos top = pos.add(0, maxHeight - 4, 0);

		// 葉っぱ1段目
		for (int i = -2; i <= 2; i++) {
			for (int k = -2; k <= 2; k++) {
				this.setBlock(world, top.add(i, 0, k), smLeaves);
			}
		}

		this.setSubLeave(world, top, 3, smLeaves);

		// 葉っぱ2段目
		for (int i = -3; i <= 3; i++) {
			for (int k = -2; k <= 2; k++) {
				this.setBlock(world, top.add(i, 1, k), smLeaves);
				this.setBlock(world, top.add(k, 1, i), smLeaves);
			}
		}

		this.setSubLeave(world, top.up(1), 4, smLeaves);

		// 葉っぱ3段目
		for (int i = -3; i <= 3; i++) {
			for (int k = -1; k <= 1; k++) {
				this.setBlock(world, top.add(i, 2, k), smLeaves);
				this.setBlock(world, top.add(k, 2, i), smLeaves);
			}
		}

		// 葉っぱ3、4段目
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				for (int y = 0; y <= 1; y++) {
					this.setBlock(world, top.add(x, y + 2, z), smLeaves);
				}
			}
		}

		this.setSubLeave(world, top.up(3), 3, smLeaves);

		// 葉っぱ5段目
		for (int i = -2; i <= 2; i++) {
			for (int k = -1; k <= 1; k++) {
				this.setBlock(world, top.add(i, 4, k), smLeaves);
				this.setBlock(world, top.add(k, 4, i), smLeaves);
			}
		}

		// 葉っぱ6段目
		for (int i = -1; i <= 1; i++) {
			for (int k = -1; k <= 1; k++) {
				this.setBlock(world, top.add(i, 5, k), smLeaves);
			}
		}

		this.setSubLeave(world, top.up(5), 2, smLeaves);

		// 葉っぱ7段目
		for (int i = -1; i <= 1; i++) {
			this.setBlock(world, top.add(i, 6, 0), smLeaves);
			this.setBlock(world, top.add(0, 6, i), smLeaves);
		}
    }

    // 主軸の横
	public void subTrunk (World world, BlockPos pos, IBlockState state, EnumFacing face) {

		Random rand = world.rand;
		int height = rand.nextInt(3) + 2;

		for (int y = 0; y < height; y++) {
			this.setBlock(world, pos.up(y), state);
		}

		int horizon = rand.nextInt(2) + 1;
		for (int i = 0; i < horizon; i++) {

			BlockPos pos2 = pos;
			IBlockState state2 = state;

			switch (face) {
			case NORTH:
				pos2 = pos2.north(i + 1);
				state2 = state2.withProperty(SMLog.LOG_AXIS, SMLog.EnumAxis.Z);
				break;
			case SOUTH:
				pos2 = pos2.south(i + 1);
				state2 = state2.withProperty(SMLog.LOG_AXIS, SMLog.EnumAxis.Z);
				break;
			case WEST:
				pos2 = pos2.west(i + 1);
				state2 = state2.withProperty(SMLog.LOG_AXIS, SMLog.EnumAxis.X);
				break;
			case EAST:
				pos2 = pos2.east(i + 1);
				state2 = state2.withProperty(SMLog.LOG_AXIS, SMLog.EnumAxis.X);
				break;
			}

			this.setBlock(world, pos2, state2);
		}
	}

	// 葉っぱ
	public void setSubLeave (World world, BlockPos pos, int scale, IBlockState smLeaves) {

		Random rand = world.rand;
		float chance = 0.45F;

		if (rand.nextFloat() <= chance) {
			this.setBlock(world, pos.add(0, 0, scale), smLeaves);
		}

		if (rand.nextFloat() <= chance) {
			this.setBlock(world, pos.add(scale, 0, 0), smLeaves);
		}

		if (rand.nextFloat() <= chance) {
			this.setBlock(world, pos.add(0, 0, -scale), smLeaves);
		}

		if (rand.nextFloat() <= chance) {
			this.setBlock(world, pos.add(-scale, 0, 0), smLeaves);
		}
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

	// 空気
	public void setAir (World world, BlockPos pos, IBlockState state) {
		if (world.getBlockState(pos).getBlock() == state.getBlock()) {
			world.setBlockToAir(pos);
		}
	}
}
