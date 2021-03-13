package sweetmagic.init.tile.magic;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeHooks;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.block.magic.MFPot;
import sweetmagic.packet.TileMFBlockPKT;
import sweetmagic.util.ParticleHelper;

public class TileMFPot extends TileMFBase {

	public static final String ISDEAD = "isDead";

	public TileMFPot () {
		super(false);
	}

	@Override
	public void serverUpdate() {

		super.serverUpdate();

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
				PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));
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
				PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));
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
				this.setMF(this.getMF() + 20);
				PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));
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
					PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));
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

        AxisAlignedBB aabb = new AxisAlignedBB(this.pos.add(-12.5, -1, -12.5), this.pos.add(12.5, 4, 12.5));
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
			mfValue += entity.getMaxHealth();
		}

		if (mfValue > 0) {
			this.setMF((this.getMF() + mfValue));
			PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));
			this.spawnParticles();
		}
	}

	// パーティクルスポーン
	public void spawnParticles() {
		ParticleHelper.spawnBoneMeal(this.world, this.pos, EnumParticleTypes.VILLAGER_HAPPY);
	}
}
