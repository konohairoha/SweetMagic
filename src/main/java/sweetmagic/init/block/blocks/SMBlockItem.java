package sweetmagic.init.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SMBlockItem extends ItemBlock {

	public final int amount;

	public SMBlockItem (Block block, int amount) {
		super(block);
		this.amount = amount;
	}

	// 燃焼時間の取得
	@Override
	public int getItemBurnTime(ItemStack stack) {
		return this.amount;
	}
}
