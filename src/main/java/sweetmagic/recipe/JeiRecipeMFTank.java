package sweetmagic.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import sweetmagic.init.ItemInit;

public class JeiRecipeMFTank {

	// レシピリスト
	public static List<JeiRecipeMFTank> recipes = new ArrayList<JeiRecipeMFTank>();
	protected Object inputStack;		// 材料（鉱石辞書対応）
	protected ItemStack outputStack;	// 結果

	// レシピの初期化
	public static void init() {
		JeiRecipeMFTank.register(new ItemStack(Items.GLASS_BOTTLE, 1), new ItemStack(ItemInit.mf_sbottle, 1));
		JeiRecipeMFTank.register(new ItemStack(ItemInit.b_mf_bottle, 1), new ItemStack(ItemInit.mf_bottle, 1));

	}

	// 外向け用レシピ登録メソッド
	public static void register(ItemStack inputStack, ItemStack outputStack) {
		commonRegister(inputStack, outputStack);
	}

	// 通常レシピと鉱石辞書でのレシピ登録を行う
	private static void commonRegister(Object inputStack, ItemStack outputStack/*, int burnTime*/) {
		recipes.add(new JeiRecipeMFTank( inputStack, outputStack ));
	}

	// レシピに一致するかの判断を行う
	public static JeiRecipeMFTank getRecipe() {
		return (JeiRecipeMFTank) recipes;
	}

	public ItemStack getInput() {
		ItemStack ipt = ItemStack.EMPTY;
		//通常のItemStack
		if (this.inputStack instanceof ItemStack) {
			ipt = ((ItemStack) this.inputStack).copy();
		}
		//鉱石辞書
		else if (this.inputStack instanceof String) {
			NonNullList<ItemStack> oreDict = OreDictionary.getOres((String) this.inputStack);
			for (ItemStack stack : oreDict) {
				ipt = (stack.copy());
			}
		}
		return ipt;
	}

	public ItemStack getOutput() {
		return this.outputStack.copy();
	}

	// コンストラクタ(外部から直接呼び出さないようにする)
	private JeiRecipeMFTank(Object inputStack, ItemStack outputStack) {
		this.inputStack = inputStack;
		this.outputStack = outputStack;
	}
}
