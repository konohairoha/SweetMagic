package sweetmagic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;
import sweetmagic.init.entity.ai.EntityAIAnger;

public class SMUtil {

	public static Random rand = new Random();
	public static final UUID TOOL_REACH = UUID.fromString("7f10172d-de69-49d7-81bd-9594286a6827");

	public static boolean isEmpty(ItemStack item) {
		if (item == null) {
			item = ItemStack.EMPTY;
			return true;
		}
		return item.getItem() == null || item.isEmpty();
	}

	// コード短くするとき用、ブロックをアイテムに変換処理
	public static Item getItemBlock(Block block) {
		return Item.getItemFromBlock(block);
	}

	// コード短くするとき用、いちいちItemStackのインスタンス作るのめんどいので
	public static ItemStack getStack(Item item) {
		return new ItemStack(item);
	}

	// オーブンとかの複数個可変にするとき用、一番少ないItemStackの整数を返す
	public static int minObj(List<Object[]> inputs) {
		ItemStack item = (ItemStack)(inputs.get(0)[1]);
		int min = item.getCount();
		for(Object[] input : inputs) {
			ItemStack check = (ItemStack)(input[1]);
			if(min > check.getCount())	min = check.getCount();
		}
		return min;
	}

	// オーブンとかの複数個可変にするとき用、一番少ないItemStackの整数を返す
	public static int minObj(List<Object[]> inputs, ItemStack hand) {
		int min = minObj(inputs);
		return min > hand.getCount() ? hand.getCount() : min;
	}

	// オーブンでプレイヤーのInventoryの可変個数を一気にへらすラクラクメソッド(リスト版)
	public static void decrPInvMinList(EntityPlayer player, int st, List<Object[]> inputs) {
		for(Object[] input : inputs) {
			player.inventory.decrStackSize((Integer) input[0], (int)input[2] * st);
		}
	}

	// オーブンでプレイヤーのInventoryの可変個数を一気にへらすラクラクメソッド
	public static void decrPInvMin(EntityPlayer player, int st, Object... ob) {
		for(int i = 0; i < ob.length; i++) {
			player.inventory.decrStackSize((Integer) ob[i], st);
		}
	}

	// Inventoryから見つけたいItemStack()
	public static List<ItemStack> getShrinkInputsFromInv(NonNullList<ItemStack> inv, List<ItemStack> input, byte minAmount) {
		for(ItemStack stack : input) {
			for (int i = 0; i < inv.size(); i++) {
			ItemStack st = inv.get(i);
				if (!st.isEmpty() && st.getCount() >= minAmount && stack.isItemEqual(st)) { }
			}
		}
		return null;
	}

	/**
	 * Inventoryから見つけたいItemStack()をObject配列で返す　見つからないときはNullで返すので注意
	 * @param inv			プレイヤーのインベントリ
	 * @param itemstack	探索したいアイテム情報(Metadata対応)
	 * @param minAmount	byte レシピ個数　※1～64…指定の数以上あるかを見る　0…アイテムがあるかだけ見る
	 * @return	Object[]	Inventoryのポインタ[0]、アイテム情報(ItemStack)[1]、アイテム数(int)[2]
	 * @see　※minAmountが1～64の場合、戻り値Object[2]のアイテム数は指定したminAmountを返す　minAmountが0の場合、最初に取得できたアイテムの数を返す
	 */
	public static Object[] getStackFromInv(NonNullList<ItemStack> inv, ItemStack itemstack, byte minAmount) {

        Object[] obj = new Object[3];
        for (int i = 0; i < inv.size(); i++) {

            ItemStack stack = inv.get(i);

            // ItemStackが最低個数かどう
            if (!stack.isEmpty() && (stack.getCount() >= minAmount || minAmount == 0)) {
                // ItemStackが鉱石辞書登録してるなら鉱石辞書名で判断
                if (ItemHelper.checkOreName(stack, itemstack) || stack.isItemEqual(itemstack)) {
                    obj[0] = i;
                    obj[1] = stack;
                    obj[2] = minAmount != 0 ? itemstack.getCount() : stack.getCount();
                    return obj;
                }
            }
        }
        return null;
    }

	public static Object[] getStackFromInv(List<ItemStack> inv, ItemStack itemstack, byte minAmount) {

        Object[] obj = new Object[3];
        for (int i = 0; i < inv.size(); i++) {

            ItemStack stack = inv.get(i);

            // ItemStackが最低個数かどう
            if (!stack.isEmpty() && (stack.getCount() >= minAmount || minAmount == 0)) {
                // ItemStackが鉱石辞書登録してるなら鉱石辞書名で判断
                if (ItemHelper.checkOreName(stack, itemstack) || stack.isItemEqual(itemstack)) {
                    obj[0] = i;
                    obj[1] = stack;
                    obj[2] = minAmount != 0 ? itemstack.getCount() : stack.getCount();
                    return obj;
                }
            }
        }
        return null;
    }

	public static Object[] getStackInv(List<ItemStack> inv, ItemStack stack, byte minAmount) {

        Object[] obj = new Object[3];
        for (int i = 0; i < inv.size(); i++) {

            ItemStack invStack = inv.get(i);

			// ItemStackが最低個数かどう
			if (!invStack.isEmpty() && (invStack.getCount() >= minAmount || minAmount == 0)) {

                // ItemStackが鉱石辞書登録してるなら鉱石辞書名で判断
                if (invStack.isItemEqual(stack) || isEqualItem(invStack, stack) ) {
                    obj[0] = i;															// インベントリの位置
                    obj[1] = invStack;													// アイテム情報
                    obj[2] = minAmount != 0 ? stack.getCount() : invStack.getCount();	// アイテムの個数
                    return obj;
                }
            }
        }
        return null;
	}

	public static boolean isEqualItem (ItemStack invStack, ItemStack stack) {
		return stack.getMetadata() == 32767 && stack.getItem().getRegistryName().equals(invStack.getItem().getRegistryName());
	}

	/**
	 * Inventoryからアイテムを探索する　メタデータいらない方		　static参照可能にしといた
	 * @param inv			プレイヤーのInventoryを参照するように設定
	 * @param item			Item
	 * @param minAmount	byte 最低個数　0にすると可変個数を取得するように
	 * @return	Object[]	Inventoryのポインタ[0]、アイテム情報(ItemStack)[1]、スタック数(int)[2]
	 */
	public static Object[] getStackFromPInv(NonNullList<ItemStack> inv, Item item, byte minAmount) {
		Object[] obj = new Object[3];
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.get(i);
			if (!stack.isEmpty() && (stack.getCount() >= minAmount || minAmount == 0) && stack.getItem() == item) {
				obj[0] = i;
				obj[1] = stack;
				obj[2] = stack.getCount();
				return obj;
			}
		}
		return null;
	}

	/**
	 * Inventoryからアイテムを探索する　メタデータもいる方	static参照可能にしといた Byteでちょこっとメモリ節約
	 * @param inv					プレイヤーのInventoryを参照するように設定
	 * @param item					Item
	 * @param meta				データ値
	 * @param minAmount		byte 最低個数　0にすると可変個数を取得するように
	 * @return		Object[]	Inventoryのポインタ[0]、アイテム情報(ItemStack)[1]、スタック数(int)[2]
	 */
	public static Object[] getStackFromInventory(NonNullList<ItemStack> inv, Item item, int meta, byte minAmount) {
		Object[] obj = new Object[3];
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.get(i);
			if (!stack.isEmpty() && stack.getCount() >= minAmount && stack.getItem() == item
					&& stack.getItemDamage() == meta) {
				obj[0] = i;
				obj[1] = stack;
				obj[2] = stack.getCount();
				return obj;
			}
		}
		return null;
	}

	public static boolean isIntegratedItem(ItemStack i1, ItemStack i2, boolean nullable) {
		if (isEmpty(i1) || isEmpty(i2)) { return nullable; }

		if (i1.getItem() == i2.getItem()) {
			if (i1.getItemDamage() == i2.getItemDamage() || i2.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
				NBTTagCompound t1 = i1.getTagCompound();
				NBTTagCompound t2 = i2.getTagCompound();
				if (t1 == null && t2 == null) {
					return true;
				} else if (t1 == null || t2 == null) {
					return false;
				} else {
					return t1.equals(t2);
				}
			}
		}
		return false;
	}

	public static boolean canInsert(ItemStack i1, ItemStack i2) {
		if (!isEmpty(i1) && isEmpty(i2)) {
			return true;
		} else if (isSameItem(i1, i2, false)) {
			return i1.getCount() <= (i2.getMaxStackSize() - i2.getCount());
		}
		return false;
	}

	public static boolean isSameItem(ItemStack i1, ItemStack i2, boolean nullable) {
		if (isEmpty(i1) || isEmpty(i2)) { return nullable; }

		if (i1.getItem() == i2.getItem() && i1.getItemDamage() == i2.getItemDamage()) {
			NBTTagCompound t1 = i1.getTagCompound();
			NBTTagCompound t2 = i2.getTagCompound();
			if (t1 == null && t2 == null) {
				return true;
			} else if (t1 == null || t2 == null) {
				return false;
			} else {
				return t1.equals(t2);
			}
		}
		return false;
	}

	// Obj型から鉱石辞書を元にアイテムに変換
	public static ArrayList<ItemStack> getOreList(Object obj) {

		ArrayList<ItemStack> ret = Lists.newArrayList();
		if (obj == null) { return ret; }

		// String型なら
		if (obj instanceof String) {
			ret.addAll(OreDictionary.getOres((String) obj));
		}

		// List型なら
		else if (obj instanceof List && !((List) obj).isEmpty()) {
			ret.addAll((List<ItemStack>) obj);
		}

		// ItemStack型なら
		else if (obj instanceof ItemStack) {

			ItemStack stack = (ItemStack) obj;
			if (!SMUtil.isEmpty(stack)) {
				ret.add(stack.copy());
			}
		}

		// Item型なら
		else if (obj instanceof Item) {
			ret.add(new ItemStack((Item) obj, 1, 0));
		}

		// Block型なら
		else if (obj instanceof Block) {
			ret.add(new ItemStack((Block) obj, 1, 0));
		}

		// OreObj型なら
		else if (obj instanceof OreItems) {

			OreItems ore = (OreItems) obj;
			int amount = ore.getAmount();

			// ItemStackなら
			if (ore.getStack() != null && !ore.getStack().isEmpty()) {

				// アイテム個数の設定
				ItemStack stack = ore.getStack();
				stack.setCount(amount);
				ret.add(stack.copy());
			}

			// Stringなら
			else if (!ore.getName().equals("")){

				// リストを作ってリストに突っ込む
				ArrayList<ItemStack> oreList = Lists.newArrayList();
				oreList.addAll(OreDictionary.getOres(ore.getName()));

				// リスト分だけ回す
				for (ItemStack oreStack : oreList) {
					ItemStack stack = oreStack.copy();
					stack.setCount(amount);
					ret.add(stack.copy());
				}
			}
		}

		else {
			throw new IllegalArgumentException("Unknown Object passed to recipe!");
		}
		return ret;
	}

	// 配列型で鉱石辞書を返す
	public static ItemStack[] getOreArray (Object obj) {
		List<ItemStack> stackList = getOreList(obj);
		return stackList.toArray(new ItemStack[stackList.size()]);
	}

	public static ItemStack getItemStack(Object obj) {
		return (ItemStack) obj;
	}

	public static Item getItem (Object obj) {
		return getItemStack(obj).getItem();
	}

	// タゲ集め
	public static void tameAIAnger(EntityLiving anger, EntityLivingBase target) {

        boolean isLearning = false;

        for (EntityAITasks.EntityAITaskEntry entry : anger.targetTasks.taskEntries) {

            if (entry.action instanceof EntityAIAnger) {
                EntityAIAnger ai = (EntityAIAnger)entry.action;
                ai.setTarget(target);
                isLearning = true;
                break;
            }
        }

        if (!isLearning) {
            EntityAIAnger ai = new EntityAIAnger(anger, target);
            ai.setTarget(target);
            anger.targetTasks.addTask(0, ai);
        }
    }


	// 必須だがオーバーライドをする必要はない。
	public static float getGrowthChance(Block block, World world, BlockPos pos, float chance) {

		float f = 1.0F;
		BlockPos p0 = pos.down();

		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				float f1 = 0.0F;
				IBlockState state = world.getBlockState(p0.add(i, 0, j));

				if (state.getBlock().canSustainPlant(state, world, p0.add(i, 0, j), EnumFacing.UP, (IPlantable) block)) {
					f1 = 1.0F;

					if (state.getBlock().isFertile(world, p0.add(i, 0, j))) { f1 = 3.0F; }
				}

				if (i != 0 || j != 0) { f1 /= 4.0F; }
				f += f1;
			}
		}

		BlockPos p1 = pos.north();
		BlockPos p2 = pos.south();
		BlockPos p3 = pos.west();
		BlockPos p4 = pos.east();
		boolean flag = block == world.getBlockState(p3).getBlock() || block == world.getBlockState(p4).getBlock();
		boolean flag1 = block == world.getBlockState(p1).getBlock() || block == world.getBlockState(p2).getBlock();

		if (flag && flag1) {
			f /= chance;
		} else {
			boolean flag2 = block == world.getBlockState(p3.north()).getBlock()
					|| block == world.getBlockState(p4.north()).getBlock()
					|| block == world.getBlockState(p4.south()).getBlock()
					|| block == world.getBlockState(p3.south()).getBlock();

			if (flag2) { f /= 2.0F; }
		}

		return f;
	}

	// 範囲のえんちちーを取得
	public static ArrayList getAABB (Class css, World world, Entity entity, double x, double y, double z) {
		return (ArrayList) world.getEntitiesWithinAABB(css, entity.getEntityBoundingBox().grow(x, y, z));
	}

}
