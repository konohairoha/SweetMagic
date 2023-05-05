package sweetmagic.init.villager;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import sweetmagic.init.ItemInit;
import sweetmagic.init.villager.SMTradeInit.TradeType;

public class SMTradeRecipes {

	public static final SMTradeRecipes INSTANCE = new SMTradeRecipes();

	// 料理交換
	public class CookTrade implements ITradeList {

		private final List<SMTradeInit> data;
		private final PriceInfo info;

		public CookTrade(List<SMTradeInit> list, PriceInfo priceInfo) {
			this.data = list;
			this.info = priceInfo;
		}

		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipe, Random rand) {

			if (this.data != null && !this.data.isEmpty()) {

				int amount = this.info != null ? this.info.getPrice(rand) : 1;

				int listSize = this.data.size();
				SMTradeInit trade = this.data.get(rand.nextInt(listSize));
				ItemStack stack = trade.stack;
				amount += trade.price;
				ItemStack emerald = new ItemStack(Items.EMERALD, amount);

				if (trade.type == TradeType.BUY) {
					int value = emerald.getCount();
					emerald.setCount(value > 2 ? value / 2 : value);
					stack.setCount(emerald.getCount() > 4 ? 1 : stack.getCount() * emerald.getCount());

					recipe.add(new MerchantRecipe(emerald, stack));
				}

				else {
					int value = stack.getCount() * emerald.getCount();
					stack.setCount(value >= 64 ? 32 : value);

					if (value > 4) {
						emerald.setCount(1);
					}

					recipe.add(new MerchantRecipe(stack, emerald));
				}
			}
		}
	}

	// 魔法交換
	public class MagicTrade implements ITradeList {

		@Nullable
		private final PriceInfo info;
		private final List<SMTradeInit> data;

		public MagicTrade(List<SMTradeInit> list, @Nullable PriceInfo priceInfo) {
			this.info = priceInfo;
			this.data = list;
		}

		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipe, Random rand) {

			if (this.data != null && !this.data.isEmpty()) {

				int amount = this.info != null ? this.info.getPrice(rand) : 1;

				int listSize = this.data.size();
				SMTradeInit trade = this.data.get(rand.nextInt(listSize));
				ItemStack stack = trade.stack;
				amount += trade.price;
				ItemStack emerald = new ItemStack(ItemInit.mf_sbottle, amount);

				if (trade.type == TradeType.BUY) {
					recipe.add(new MerchantRecipe(emerald, stack));
				}

				else {
					recipe.add(new MerchantRecipe(stack, emerald));
				}
			}
		}
	}

	// 魔法交換
	public class MagicRareTrade implements ITradeList {

		@Nullable
		private final PriceInfo info;
		private final List<SMTradeInit> data;

		public MagicRareTrade(List<SMTradeInit> list, @Nullable PriceInfo priceInfo) {
			this.info = priceInfo;
			this.data = list;
		}

		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipe, Random rand) {

			if (this.data != null && !this.data.isEmpty()) {

				int amount = this.info != null ? this.info.getPrice(rand) : 1;

				int listSize = this.data.size();
				SMTradeInit trade = this.data.get(rand.nextInt(listSize));
				ItemStack stack = trade.stack;
				amount += trade.price;
				ItemStack emerald = new ItemStack(ItemInit.mf_magiabottle, amount);

				if (trade.type == TradeType.BUY) {
					recipe.add(new MerchantRecipe(emerald, stack));
				}

				else {
					recipe.add(new MerchantRecipe(stack, emerald));
				}
			}
		}
	}
}
