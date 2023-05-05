package sweetmagic.init.tile.magic;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeHooks;
import sweetmagic.config.SMConfig;
import sweetmagic.init.block.magic.MFPot;
import sweetmagic.util.ParticleHelper;

public class TileMFPot extends TileMFBase {

	public static final String ISDEAD = "isDead";
	public int maxMagiaFlux = 200000;	// 最大MF量を設定
	private EnumFacing face = EnumFacing.NORTH;

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
		case 9:
			// クリスマスローズエリックスミシィ
			this.ericsmithii();
			break;
		}
	}

	// ドリズリィ
	public void dmPot () {

		// 雨が降っていてMFが溜めれる状態なら
		if (this.world.isRaining() && this.canMFChange()) {

			long time = this.getTime();

			if (time % 20 == 0) {
				int mf = SMConfig.isHard == 3 ? 15 : 20;
				this.setMF(this.getMF() + mf);
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

		if (worldTime >= 9500 && worldTime <= 15500) {

			int mf = SMConfig.isHard == 3 ? 4 : 10;
			this.setMF(this.getMF() + mf);

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
		if(biome.getDefaultTemperature() > 0 && this.pos.getY() <= 120) { return; }

		long time = this.getTime();

		if (time % 20 == 0) {
			int mf = SMConfig.isHard == 3 ? 8 : 12;
			this.setMF(this.getMF() + mf);
			this.sentClient();
		}

		if (time % 100 == 0) {
			this.spawnParticles();
		}
	}

	// ソリッドスター
	public void solidstarPot () {

		long time = this.getTime();
		if (time % 40 != 0) { return; }

		boolean isCharge = true;
		float sumEnchapower = 0F;

		for (int x = -2; x <= 2; ++x) {
			for (int z = -2; z <= 2; ++z) {

				if (x > -2 && x < 2 && z == -1) { z = 2; }

				for (int y = 0; y <= 1; ++y) {

					BlockPos bpos = this.pos.add(x, y, z);
					float power = ForgeHooks.getEnchantPower(this.world, bpos);
					if (power <= 0) { continue; }

					if (!this.world.isAirBlock(this.pos.add(x / 2, 0, z / 2))) { break; }

					isCharge = true;
					sumEnchapower += power;
				}
			}
		}

		if (isCharge) {
			sumEnchapower *= SMConfig.isHard == 3 ? 0.25F : 1F;
			this.setMF((int) (this.getMF() + sumEnchapower));
			this.sentClient();
			this.spawnParticles();
		}
	}

	// ジニア
	public void zinniaPot () {

		long time = this.getTime();
		if (time % 40 != 0) { return; }

		boolean isCharge = true;
		float sumLightValue = 0F;

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
					sumLightValue += power;
				}
			}
		}

		if (isCharge) {
			sumLightValue *= SMConfig.isHard == 3 ? 0.35F : 1F;
			this.setMF((int) (this.getMF() + sumLightValue));
			this.sentClient();
			this.spawnParticles();
		}
	}

	// ハイドラ
	public void hydrangeaPot () {

		long time = this.getTime();
		if (time % 10 != 0) { return; }

		List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, this.getAABB(12.5D, 4D, 12.5D));
		if (entityList.isEmpty()) { return; }

		int mfValue = 0;

		for (EntityLivingBase entity : entityList) {

			// プレイヤーか死んでないモブなら次へ
			if (entity instanceof EntityPlayer || entity.isEntityAlive()) { continue; }

			// 死亡したNBTを持ってるなら次へ
			NBTTagCompound tags = entity.getEntityData();
			if (tags == null || tags.hasKey(ISDEAD)) { continue; }

			tags.setBoolean(ISDEAD, true);
			mfValue += entity.getMaxHealth() * 7;
		}

		if (mfValue > 0) {
			this.setMF((this.getMF() + mfValue));
			this.sentClient();
			this.spawnParticles();
		}
	}

	// エリックスミシィ
	public void ericsmithii () {

		long time = this.getTime();
		if (time % 7 != 0) { return; }

		BlockPos pos = this.pos.offset(this.face);
		Block block = this.getBlock(pos);

		if (block instanceof BlockSnow) {
			this.breakBlock(pos, false);
			this.setMF((this.getMF() + 7));
			this.sentClient();
		}

		this.face = this.face.rotateYCCW();


	}

	// パーティクルスポーン
	public void spawnParticles() {
		ParticleHelper.spawnParticle(this.world, this.pos, EnumParticleTypes.VILLAGER_HAPPY);
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}
}
