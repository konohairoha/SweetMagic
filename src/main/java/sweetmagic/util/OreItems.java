package sweetmagic.util;

import net.minecraft.item.ItemStack;

public class OreItems {

	private String name;
	private int amount;
	private ItemStack stack;

	public OreItems (String name, int amount) {
		this.name = name;
		this.amount = amount;
	}

	public OreItems (String name) {
		this.name = name;
		this.amount = 1;
	}

	public OreItems (ItemStack stack) {
		this.stack = stack;
		this.amount = this.stack.getCount();
	}

	// 鉱石辞書目の取得
	public String getName () {
		return this.name;
	}

	// アイテム個数の取得
	public int getAmount () {
		return this.amount;
	}

	// ItemStackの取得
	public ItemStack getStack () {
		return this.stack == null || this.stack.isEmpty() ? null : this.stack.copy();
	}
}
