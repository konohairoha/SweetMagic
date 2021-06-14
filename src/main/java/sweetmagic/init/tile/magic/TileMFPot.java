package sweetmagic.init.tile.magic;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeHooks;
import sweetmagic.init.block.magic.MFPot;
import sweetmagic.util.ParticleHelper;

public class TileMFPot extends TileMFBase {

	public static final String ISDEAD = "isDead";
	public int maxMagiaFlux = 200000;	// 最大MF量を設定

	public TileMFPot () {
		super(false);
	}

	@Override
	public void serverUpdate() {

		super.serverUpdate();
		if (!this.canMFChange()) { return; }

		MFPot mfpot = (MFPot) this.getBlock(this.pos);

		switch (mfpot.data) {
		case 0:
			// ドリズリィ
			this.dmPot();
			break;
		case 1:
			// 黄昏時
			this.twilightPot();
			break;
		case 2:
			// スノードロップ
			this.snowdropPot();
			break;
		case 5:
			// ソリッドスター
			this.solidstarPot();
			break;
		case 6:
			// ジニア
			this.zinniaPot();
			break;
		case 7:
			// ハイドら
			this.hydrangeaPot();
			break;
		}
	}

	// ドリズリィ
	public void dmPot () {

		// 雨が降っていてMFが溜めれる状態なら
		if (this.world.isRaining() && this.canMFChange()) {

			this.setMF(this.getMF() + 1);

			long time = this.getTime();

			if (time % 20 == 0) {
				this.sentClient();
			}

			if (time % 100 == 0) {
				this.spawnParticles();
			}
		}
	}

	// 黄昏時の夢百合草
	public void twilightPot () {

		long time = this.world.getWorldTime();
		long worldTime = time % 24000;

		if (worldTime >= 11000 && worldTime < 14000) {

			this.setMF(this.getMF() + 8);

			if (time % 20 == 0) {
				this.sentClient();
			}

			if (time % 100 == 0) {
				this.spawnParticles();
			}
		}
	}

	// スノードロップ
	public void snowdropPot () {

		Biome biome = this.world.getBiomeForCoordsBody(this.pos);
		if(biome.getDefaultTemperature() <= 0) {

			long time = this.getTime();

			if (time % 20 == 0) {
				this.setMF(this.getMF() + 10);
				this.sentClient();
			}

			if (time % 100 == 0) {
				this.spawnParticles();
			}
		}
	}

	// ソリッドスター
	public void solidstarPot () {

		long time = this.getTime();
		if (time % 40 != 0) { return; }

		boolean isCharge = true;

		for (int x = -2; x <= 2; ++x) {
			for (int z = -2; z <= 2; ++z) {

				if (x > -2 && x < 2 && z == -1) { z = 2; }

				for (int y = 0; y <= 1; ++y) {

					BlockPos bpos = pos.add(x, y, z);
					float power = ForgeHooks.getEnchantPower(this.world, bpos);
					if (power <= 0) { continue; }

					if (!this.world.isAirBlock(this.pos.add(x / 2, 0, z / 2))) { break; }

					isCharge = true;
					this.setMF((int) (this.getMF() + power));
					this.sentClient();
				}
			}
		}

		if (isCharge) {
			this.spawnParticles();
		}
	}

	// ジニア
	public void zinniaPot () {

		long time = this.getTime();
		if (time % 40 != 0) { return; }

		boolean isCharge = true;

		for (int x = -2; x <= 2; ++x) {
			for (int z = -2; z <= 2; ++z) {

				if (x > -2 && x < 2 && z == -1) { z = 2; }

				for (int y = 0; y <= 1; ++y) {

					BlockPos bpos = this.pos.add(x, y, z);
					IBlockState state = this.getState(bpos);
					Block block = state.getBlock();
					if (block == Blocks.AIR) { continue; }

					float power = block.getLightValue(state) * 0.25F;
					if (power <= 0) { continue; }

					if (!this.world.isAirBlock(this.pos.add(x / 2, 0, z / 2))) { break; }

					isCharge = true;
					this.setMF((int) (this.getMF() + power));
					this.sentClient();
				}
			}
		}

		if (isCharge) {
			this.spawnParticles();
		}
	}

	// ハイドラ
	public void hydrangeaPot () {

		long time = this.getTime();
		if (time % 10 != 0) { return; }

        AxisAlignedBB aabb = new AxisAlignedBB(this.pos.add(-12.5, -4, -12.5), this.pos.add(12.5, 4, 12.5));
		List<EntityLivingBase> entityList = this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		if (entityList.isEmpty()) { return; }

		int mfValue = 0;

		for (EntityLivingBase entity : entityList) {

			// プレイヤーか死んでないモブなら次へ
			if (entity instanceof EntityPlayer || entity.isEntityAlive()) { continue; }

			// 死亡したNBTを持ってるなら次へ
			NBTTagCompound tags = entity.getEntityData();
			if (tags == null || tags.hasKey(ISDEAD)) { continue; }

			tags.setBoolean(ISDEAD, true);
			mfValue += entity.getMaxHealth() * 5;
		}

		if (mfValue > 0) {
			this.setMF((this.getMF() + mfValue));
			this.sentClient();
			this.spawnParticles();
		}
	}

	// パーティクルスポーン
	public void spawnParticles() {
		ParticleHelper.spawnBoneMeal(this.world, this.pos, EnumParticleTypes.VILLAGER_HAPPY);
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}
}
