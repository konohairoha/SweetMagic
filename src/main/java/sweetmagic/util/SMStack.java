package sweetmagic.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SMStack {

	private final List<ItemStack> stackList = new ArrayList<>();

	public SMStack (Object... objArray) {

		for (Object obj : objArray) {
			if (obj instanceof Item) {
				this.stackList.add(new ItemStack((Item) obj));
			}

			else if (obj instanceof ItemStack) {
				this.stackList.add((ItemStack) obj);
			}

			else if (obj instanceof Block) {
				this.stackList.add(new ItemStack((Block) obj));
			}
		}
	}

	public List<ItemStack> getStackList () {
		return this.stackList;
	}
}
