package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import sweetmagic.init.BlockInit;
import sweetmagic.util.SweetState;

public class WorldGenFlower extends WorldGenAbstractTree {

    public WorldGenFlower() {
        super(false);
    }

    public boolean generate(World world, Random rand, BlockPos pos) {
    	this.setBlock(world, pos, this.getState(world));
		return true;
    }

	// ブロックの設置
	public void setBlock (World world, BlockPos pos, IBlockState state) {
		if (world.isAirBlock(pos)) {
			world.setBlockState(pos, state, 3);
		}
	}


	// IBlockStateの取得
	public IBlockState getState (World world) {

		IBlockState state = null;

		switch (world.rand.nextInt(5)) {
		case 0:
			state = BlockInit.lily_valley.getDefaultState();
			break;
		case 1:
			state = BlockInit.cornflower.getDefaultState();
			break;
		case 2:
			state = BlockInit.blueberry_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
			break;
		case 3:
			state = BlockInit.raspberry_plant.getDefaultState().withProperty(SweetState.STAGE6, 5);
			break;
		case 4:
			state = BlockInit.olive_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
			break;
		case 5:
			state = BlockInit.sugarbell_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
			break;
		case 6:
			state = BlockInit.sannyflower_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
			break;
		case 7:
			state = BlockInit.clerodendrum.getDefaultState().withProperty(SweetState.STAGE4, 3);
			break;
		case 8:
			state = BlockInit.fire_nasturtium_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
			break;
		case 9:
			state = BlockInit.glowflower_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
			break;
		}

		return state;
	}
}
