package sweetmagic.init.tile.magic;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import sweetmagic.init.BlockInit;

public class TileAltarCreat extends TilePedalCreate {

	private static final List<Block> blockList = Arrays.<Block> asList(
		BlockInit.divinecrystal_block, BlockInit.purecrystal_block, BlockInit.cosmos_light_block
	);

	public void spawnParticle () {
		super.spawnParticle();
		float addY = this.nowTick * 0.007F * (this.quickCraft() ? 2F : 1F);
		this.spawnParticleRing(this.world, this.pos.getX() + 0.5F, this.pos.getY() + 1.4F + addY, this.pos.getZ() + 0.5F, 0D, 0D, 0D, 0.75D);
	}

	// ブロックチェック
	public void checkBlock () {
		this.isCrystal = blockList.contains(this.getBlock(this.pos.down()));
		this.isEncha = this.getEnchantPower() >= 15F;
		this.isHaveBlock = this.isCrystal && this.isEncha;

		if (!this.isSever()) {
			this.findPlayer = this.findRangePlayer();
		}
	}

	public Block getCrystal () {
		return BlockInit.divinecrystal_block;
	}

	public Block getCrystalAlpha () {
		return BlockInit.divinecrystal_block_alpha;
	}

	public int getData () {
		return 2;
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return 200000;
	}

    // 受信するMF量
	@Override
    public int getUseMF () {
    	return 50000;
    }
}
