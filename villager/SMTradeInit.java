package sweetmagic.init.villager;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import sweetmagic.init.ItemInit;

public class SMTradeInit {

	public final int price;
	public final ItemStack stack;
	public final TradeType type;
	public static Random rand = new Random();

	public SMTradeInit(TradeType type, ItemStack stack, int price) {
		this.price = price;
		this.stack = stack;
		this.type = type;
	}

	public static enum TradeType {
		BUY,
		SELL;
	}

	public static final List<SMTradeInit> COOK0 = Lists.newArrayList();		// 売り：卵
	public static final List<SMTradeInit> COOK1 = Lists.newArrayList();		// 売り：種　買い：料理素材
	public static final List<SMTradeInit> COOK2 = Lists.newArrayList();		// 売り：中間素材　買い：中間素材
	public static final List<SMTradeInit> COOK3 = Lists.newArrayList();		// 売り：料理　買い：料理

	public static final List<SMTradeInit> MAGIC0 = Lists.newArrayList();	// 売り：サニー、ムーンの花弁
	public static final List<SMTradeInit> MAGIC1 = Lists.newArrayList();	// 売り：魔法の種
	public static final List<SMTradeInit> MAGIC2 = Lists.newArrayList();	// 売り：花弁
	public static final List<SMTradeInit> MAGIC3 = Lists.newArrayList();	// 売り：魔法道具
	public static final List<SMTradeInit> MAGIC4 = Lists.newArrayList();	// 売り：モブドロップ

	public static void init() {

		COOK0.add(new SMTradeInit(TradeType.BUY, new ItemStack(ItemInit.eggbag, 4), 0));

		for (int i = 0; i < 8; i++) {
			COOK1.add(new SMTradeInit(TradeType.BUY, TradeList.getSeed(), 2));
			COOK1.add(new SMTradeInit(TradeType.SELL, TradeList.getCookMaterial(), 0));
			COOK1.add(new SMTradeInit(TradeType.SELL, TradeList.getCookMaterial(), 1));
			COOK2.add(new SMTradeInit(TradeType.BUY, TradeList.getInterMaterial(), 0));
			COOK2.add(new SMTradeInit(TradeType.BUY, TradeList.getInterMaterial(), 1));
			COOK2.add(new SMTradeInit(TradeType.SELL, TradeList.getInterMaterial(), 0));
			COOK2.add(new SMTradeInit(TradeType.SELL, TradeList.getInterMaterial(), 1));
			COOK3.add(new SMTradeInit(TradeType.BUY, TradeList.cookItem(), 3));
			COOK3.add(new SMTradeInit(TradeType.BUY, TradeList.cookItem(), 5));
			COOK3.add(new SMTradeInit(TradeType.SELL, TradeList.cookItem(), 0));
			COOK3.add(new SMTradeInit(TradeType.SELL, TradeList.cookItem(), 0));
		}

		for (int i = 0; i < 8; i++) {
			MAGIC0.add(new SMTradeInit(TradeType.BUY, TradeList.getSanyMoonItem(), 0));
			MAGIC1.add(new SMTradeInit(TradeType.BUY, TradeList.getMagicSeed(), 0));
			MAGIC1.add(new SMTradeInit(TradeType.BUY, TradeList.getMagicPetal(), 0));
			MAGIC2.add(new SMTradeInit(TradeType.BUY, TradeList.getMagicPetal(), 0));
			MAGIC2.add(new SMTradeInit(TradeType.BUY, TradeList.getMagicItem(), 0));
			MAGIC3.add(new SMTradeInit(TradeType.BUY, TradeList.getMagicItem(), 0));
			MAGIC3.add(new SMTradeInit(TradeType.BUY, TradeList.getMagicDrop(), 1));
			MAGIC4.add(new SMTradeInit(TradeType.BUY, TradeList.getRateItem(), 0));
			MAGIC4.add(new SMTradeInit(TradeType.BUY, TradeList.getRateItem(), 1));
		}
	}
}
