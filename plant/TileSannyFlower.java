package sweetmagic.init.tile.plant;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.crop.MagiaFlower;
import sweetmagic.init.tile.magic.TileSMBase;

public class TileSannyFlower extends TileSMBase {

	public static boolean flagDaytime = false;
	public static boolean flagRaining = false;

	public void update() {

		this.tickTime++;

		// Worldの時間は20分、Tick換算で24000となる。
		if (this.tickTime % 160 != 0) { return; }

		long worldTime = this.world.getWorldTime() % 24000;
		if (worldTime < 12000) { flagDaytime = true; }

		IBlockState state = this.getState(this.pos);
		Block block = state.getBlock();

		// ドリズリィ・ミオソチスの場合
		if (block == BlockInit.dm_plant) {

			// 雨を降っているなら
			if(this.world.isRaining()) {
				flagRaining = true;
				this.spawnParticles(this.pos);
			}

			// 雨が降っていないなら
			else if (!this.world.isRaining() && this.isSever()) {

				flagRaining = false;

				// 花が咲いてる状態なら
				if (MagiaFlower.getNowStateMeta_open(state) == 3) {
					this.world.setBlockState(this.pos, ((MagiaFlower) block).withStage(this.world, state, 2), 2);
				}
			}
		}

		// 昼 だったら
		if (worldTime < 12000) {

			flagDaytime = true;

			// サニーフラワーの花
			if (block == BlockInit.sannyflower_plant) {
				this.spawnParticles(this.pos);
			}

			// ムーンブロッサムの花
			else if (block == BlockInit.moonblossom_plant && this.isSever()) {
				if (MagiaFlower.getNowStateMeta_open(state) == 3 && flagDaytime) {
					this.world.setBlockState(this.pos, ((MagiaFlower) block).withStage(this.world, state, 2), 2);
				}
			}
		}

		// 夜だったら
		else {

			flagDaytime = false;

			// ムーンブロッサムの花
			if (block == BlockInit.moonblossom_plant) {
				this.spawnParticles(this.pos);
			}

			// サニーフラワーの花
			else if (block == BlockInit.sannyflower_plant && this.isSever()) {
				if(MagiaFlower.getNowStateMeta_open(state) == 3 && !flagDaytime) {
					this.world.setBlockState(this.pos, ((MagiaFlower) block).withStage(this.world, state, 2), 2);
				}
			}
		}
		this.tickTime = 0;
    }

	private void spawnParticles(BlockPos pos) {

		Random rand = this.world.rand;
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		for (int i = 0; i < 6; ++i) {
            double d1 = x + rand.nextFloat();
            double d2 = y + rand.nextFloat();
			double d3 = z + rand.nextFloat();
			this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d1, d2, d3, 0.0D, 0.0D, 0.0D);
        }
    }
}
