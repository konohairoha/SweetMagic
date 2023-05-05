package sweetmagic.init.tile.magic;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import sweetmagic.client.particle.ParticleOrb;
import sweetmagic.init.BlockInit;

public class TileAltarCreationStar extends TilePedalCreate {

	@Override
	public void spawnParticle () {
		super.spawnParticle();
		this.spawnParticleRing(this.world, this.pos.getX() + 0.5F, this.pos.getY() + 1.4F + this.nowTick * 0.007F, this.pos.getZ() + 0.5F, 0D, 0D, 0D, 0.6D);
	}

	// ブロックチェック
	public void checkBlock () {
		this.isCrystal = this.getBlock(this.pos.down()) == this.getCrystal();
		this.isEncha = this.getEnchantPower() >= 35F;
		this.isHaveBlock = this.isCrystal && this.isEncha;
		if (!this.isSever()) {
			this.findPlayer = this.findRangePlayer();
		}
	}

	public Block getCrystal () {
		return BlockInit.cosmos_light_block;
	}

	public Block getCrystalAlpha () {
		return BlockInit.cosmos_light_block_alpha;
	}

	public int getData () {
		return 3;
	}

	@Override
    public void spawnParticleRing (World world, double x, double y, double z, double vecX, double vecY, double vecZ, double step) {

		Random rand = world.rand;
		double spped = 0.1D;

        for (double degree = 0D; degree < 2D * Math.PI; degree += step) {
			List<Integer> color = this.getRGB(rand);
			Particle effect = ParticleOrb.create(world, x + Math.cos(degree), y, z + Math.sin(degree), -Math.cos(degree) * spped, vecY, -Math.sin(degree) * spped, color.get(0), color.get(1), color.get(2));
			this.getParticle().addEffect(effect);
        }
    }

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return 5000000;
	}

    // 受信するMF量
	@Override
    public int getUseMF () {
    	return 500000;
    }
}
