package sweetmagic.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHelper {

	public static boolean hasSpace(NonNullList<ItemStack> inv, ItemStack stack) {
		for (ItemStack invStack : inv) {
			if (invStack.isEmpty()) { return true; }
			if (areItemStacksEqual(stack, invStack) && invStack.getCount() < invStack.getMaxStackSize()) {
				return true;
			}
		}
		return false;
	}

	public static boolean areItemStacksEqual(ItemStack stack1, ItemStack stack2) {
		return ItemStack.areItemStacksEqual(getNormalizedStack(stack1), getNormalizedStack(stack2));
	}

	public static ItemStack getNormalizedStack(ItemStack stack) {
		ItemStack result = stack.copy();
		result.setCount(1);
		return result;
	}

	public static ItemStack stateToStack(IBlockState state, int stackSize) {
		return new ItemStack(state.getBlock(), stackSize, state.getBlock().getMetaFromState(state));
	}

	public static NBTTagCompound getTag(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		return stack.getTagCompound();
	}

	public static void compactItemListNoStacksize(List<ItemStack> list) {

		for (int x = 0; x < list.size(); x++) {

			ItemStack stack = list.get(x);
			if (stack.isEmpty()) { continue; }

			for (int y = x + 1; y < list.size(); y++) {

				ItemStack stack2 = list.get(y);
				if (ItemHandlerHelper.canItemStacksStack(stack, stack2)) {
					stack.grow(stack2.getCount());
					list.set(y, ItemStack.EMPTY);
				}
			}
		}
		list.removeIf(ItemStack::isEmpty);
		list.sort(ItemHelper.ITEMSTACK_ASCENDING);
	}

	public static boolean isOre(IBlockState state) {

		if (state.getBlock() == Blocks.LIT_REDSTONE_ORE) { return true; }
		if (Item.getItemFromBlock(state.getBlock()) == Items.AIR) { return false; }

		String oreDictName = getOreDictName(stateToStack(state, 1));
		return oreDictName.startsWith("ore") || oreDictName.startsWith("denseore");
	}

	public static String getOreDictName(ItemStack stack) {

		if (stack.isEmpty()) { return "Unknown"; }

		int[] oreIds = OreDictionary.getOreIDs(stack);
		if (oreIds.length == 0) { return "Unknown"; }

		return OreDictionary.getOreName(oreIds[0]);
	}


	// 2つのアイテムスタックを比較して一致すればtrueを返す
	public static boolean checkOreName (ItemStack recipeStack, ItemStack inputStack) {

		// ItemStackが空じゃないことをチェック
		if (recipeStack.isEmpty() || inputStack.isEmpty()) { return false; }

		// 鉱石辞書のID取得
		int[] recipeId = OreDictionary.getOreIDs(recipeStack);
		int[] inputId = OreDictionary.getOreIDs(inputStack);

		// 鉱石辞書に登録してるかどうかのチェック
		if (recipeId.length == 0 || inputId.length == 0) { return false; }

		// recipeStackの鉱石辞書登録数分回す
		for (int i = 0; i < recipeId.length; i++) {
			String recipe = OreDictionary.getOreName(recipeId[i]);

			// inputStackの鉱石辞書登録数分回す
			for (int k = 0; k < inputId.length; k++) {
				String input = OreDictionary.getOreName(inputId[k]);

				// 一致してるかどうか
				if (recipe.equals(input)) { return true; }
			}
		}
		return false;
	}

	// アイテムが一致しているかのチェック
	public static boolean checkHandItem (List<ItemStack> stackList, ItemStack stack) {

		for (ItemStack s : stackList) {
			if(s.isItemEqual(stack)) { return true; }
		}

		return false;
	}

	// 2つのアイテムスタックを比較して一致すればtrueを返す
	public static boolean checkOreName (ItemStack recipeStack, String name) {

		// ItemStackが空じゃないことをチェック
		if (recipeStack.isEmpty()) { return false; }

		// 鉱石辞書のID取得
		int[] recipeId = OreDictionary.getOreIDs(recipeStack);

		// 鉱石辞書に登録してるかどうかのチェック
		if (recipeId.length == 0) { return false; }

		// recipeStackの鉱石辞書登録数分回す
		for (int i = 0; i < recipeId.length; i++) {
			String recipe = OreDictionary.getOreName(recipeId[i]);

			// 一致してるかどうか
			if (recipe.equals(name)) { return true; }
		}
		return false;
	}

	public final static Comparator<ItemStack> ITEMSTACK_ASCENDING = (o1, o2) -> {

		if ((o1.isEmpty() && o2.isEmpty())) { return 0; }
		if (o1.isEmpty()) { return 1; }
		if (o2.isEmpty()) { return -1; }

		else {
			if (o1.getItem() != o2.getItem()) {
				return Item.getIdFromItem(o1.getItem()) - Item.getIdFromItem(o2.getItem());
			} else {
				return o1.getItemDamage() - o2.getItemDamage();
			}
		}
	};

	public static void compactInventory(IItemHandlerModifiable inventory) {

		List<ItemStack> temp = new ArrayList<>();
		for (int i = 0; i < inventory.getSlots(); i++) {
			if (!inventory.getStackInSlot(i).isEmpty()) {
				temp.add(inventory.getStackInSlot(i));
				inventory.setStackInSlot(i, ItemStack.EMPTY);
			}
		}

		for (ItemStack s : temp) {
			ItemHandlerHelper.insertItemStacked(inventory, s, false);
		}
	}

	public static NBTTagCompound getNBT(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		return stack.getTagCompound();
	}
}
