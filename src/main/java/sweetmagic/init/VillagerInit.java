package sweetmagic.init;

import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import sweetmagic.init.villager.SMTradeInit;
import sweetmagic.init.villager.SMTradeRecipes;

public class VillagerInit {

	public static VillagerProfession cook;
	public static VillagerProfession magic_researcher;

	// 初期化
	public static void init () {

		// 料理人
		cook = new VillagerProfession("sweetmagic:cook",
				"sweetmagic:textures/entity/hora_cook.png",
				"textures/entity/zombie_villager/zombie_butcher.png");
		ForgeRegistries.VILLAGER_PROFESSIONS.register(cook);

		// 魔術師
		magic_researcher = new VillagerProfession("sweetmagic:magic_researcher",
				"sweetmagic:textures/entity/kronos_maguic.png",
				"textures/entity/zombie_villager/zombie_butcher.png");
		ForgeRegistries.VILLAGER_PROFESSIONS.register(magic_researcher);
	}

	// 交易リストの追加
	public static void regsterTrade () {

		// 交換アイテムの読み込み
		SMTradeInit.init();

		// 料理人
		VillagerCareer cookList = new VillagerCareer(cook, "cook");

		cookList.addTrade(1, new ITradeList[] {
			SMTradeRecipes.INSTANCE.new CookTrade(SMTradeInit.COOK0, new PriceInfo(1, 1)),
			SMTradeRecipes.INSTANCE.new CookTrade(SMTradeInit.COOK1, new PriceInfo(1, 2))
		});

		cookList.addTrade(2, new ITradeList[] {
			SMTradeRecipes.INSTANCE.new CookTrade(SMTradeInit.COOK1, new PriceInfo(1, 3)),
			SMTradeRecipes.INSTANCE.new CookTrade(SMTradeInit.COOK1, new PriceInfo(2, 2))
		});

		cookList.addTrade(3, new ITradeList[] {
			SMTradeRecipes.INSTANCE.new CookTrade(SMTradeInit.COOK1, new PriceInfo(1, 3)),
			SMTradeRecipes.INSTANCE.new CookTrade(SMTradeInit.COOK2, new PriceInfo(1, 1))
		});

		cookList.addTrade(4, new ITradeList[] {
			SMTradeRecipes.INSTANCE.new CookTrade(SMTradeInit.COOK2, new PriceInfo(1, 2)),
			SMTradeRecipes.INSTANCE.new CookTrade(SMTradeInit.COOK2, new PriceInfo(1, 3))
		});

		cookList.addTrade(5, new ITradeList[] {
			SMTradeRecipes.INSTANCE.new CookTrade(SMTradeInit.COOK3, new PriceInfo(8, 12)),
			SMTradeRecipes.INSTANCE.new CookTrade(SMTradeInit.COOK3, new PriceInfo(7, 13))
		});

		cookList.addTrade(6, new ITradeList[] {
			SMTradeRecipes.INSTANCE.new CookTrade(SMTradeInit.COOK3, new PriceInfo(10, 14)),
			SMTradeRecipes.INSTANCE.new CookTrade(SMTradeInit.COOK3, new PriceInfo(8, 16))
		});

		// 魔術師
		VillagerCareer magicList = new VillagerCareer(magic_researcher, "magic_researcher");

		magicList.addTrade(1, new ITradeList[] {
			SMTradeRecipes.INSTANCE.new MagicTrade(SMTradeInit.MAGIC1, new PriceInfo(1, 1)),
			SMTradeRecipes.INSTANCE.new MagicTrade(SMTradeInit.MAGIC1, new PriceInfo(1, 1))
		});

		magicList.addTrade(2, new ITradeList[] {
			SMTradeRecipes.INSTANCE.new MagicTrade(SMTradeInit.MAGIC1, new PriceInfo(1, 1)),
			SMTradeRecipes.INSTANCE.new MagicTrade(SMTradeInit.MAGIC2, new PriceInfo(1, 1))
		});

		magicList.addTrade(3, new ITradeList[] {
			SMTradeRecipes.INSTANCE.new MagicTrade(SMTradeInit.MAGIC2, new PriceInfo(1, 1)),
			SMTradeRecipes.INSTANCE.new MagicTrade(SMTradeInit.MAGIC2, new PriceInfo(1, 1))
		});

		magicList.addTrade(4, new ITradeList[] {
			SMTradeRecipes.INSTANCE.new MagicTrade(SMTradeInit.MAGIC3, new PriceInfo(1, 2)),
			SMTradeRecipes.INSTANCE.new MagicTrade(SMTradeInit.MAGIC3, new PriceInfo(1, 2))
		});

		magicList.addTrade(5, new ITradeList[] {
			SMTradeRecipes.INSTANCE.new MagicTrade(SMTradeInit.MAGIC4, new PriceInfo(2, 2)),
			SMTradeRecipes.INSTANCE.new MagicTrade(SMTradeInit.MAGIC4, new PriceInfo(2, 2))
		});

		magicList.addTrade(6, new ITradeList[] {
			SMTradeRecipes.INSTANCE.new MagicTrade(SMTradeInit.MAGIC4, new PriceInfo(2, 3)),
			SMTradeRecipes.INSTANCE.new MagicTrade(SMTradeInit.MAGIC4, new PriceInfo(2, 3))
		});
	}
}
