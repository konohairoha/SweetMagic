package sweetmagic.init.tile.magic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.magic.MFFurnace;

public class TileMFFurnaceAdvanced extends TileMFFurnace {

	public int needMF = 200;
	public int maxMagiaFlux = 2000000;
	public int maxSmeltTime = 20;
	public int smeltTime = 0;

	// ブロックの置き換え
	@Override
	public void changeBlock () {

		ItemStack stack = this.getInputItem(0);
		IBlockState state = this.getState(this.pos);
		MFFurnace block = (MFFurnace) state.getBlock();
        TileEntity tile = this.world.getTileEntity(this.pos);

        // 精錬スロットに何もないなら火を消す
		if (stack.isEmpty() && block == BlockInit.advanced_mffurnace_on) {
			block.setBlock = true;
			this.setBlock(state, BlockInit.advanced_mffurnace_off, block);
			block.setBlock = false;
			this.setTile(tile);
		}

		// 精錬スロットに何かあるなら火をつける
		else if (!stack.isEmpty() && block == BlockInit.advanced_mffurnace_off) {
			block.setBlock = true;
			this.setBlock(state, BlockInit.advanced_mffurnace_on, block);
			block.setBlock = false;
			this.setTile(tile);
		}
	}

	// 鉱石倍化
	@Override
	public void oreGrow (ItemStack ore) {
		ore.grow(ore.getCount() + 1);
	}

	// 精錬にかかる時間
	@Override
	public int getMaxSmeltTime () {
		return this.maxSmeltTime;
	}

	// 現在の精錬時間を取得
	@Override
	public int getSmeltTime () {
		return this.smeltTime;
	}

	// 精錬時間を設定
	@Override
	public void setSmeltTime (int smeltTime) {
		this.smeltTime = smeltTime;
	}

	// 消費MFの取得
	@Override
	public int getNeedMF () {
		return this.needMF;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 10000;
    }

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}
}
