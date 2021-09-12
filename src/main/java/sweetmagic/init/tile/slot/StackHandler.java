package sweetmagic.init.tile.slot;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.init.tile.magic.TileSMBase;

public class StackHandler extends ItemStackHandler {

	private TileSMBase tile;

	public StackHandler (TileSMBase tile, int size) {
		super(size);
		this.tile = tile;
	}

	@Override
	public void onContentsChanged(int slot) {
		this.tile.markDirty();
	}

	// リストの取得
	public NonNullList<ItemStack> getList () {
		return this.stacks;
	}

	// リストが空かのチェック
	public boolean incIsEmpty () {
		return this.getList().isEmpty();
	}
}
