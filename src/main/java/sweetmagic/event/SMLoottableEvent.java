package sweetmagic.event;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.SweetMagicCore;
import sweetmagic.config.SMConfig;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.util.SMUtil;

public class SMLoottableEvent {

	private static final LootFunction[] FUN = new LootFunction[0];
	private static final LootCondition[] CON = new LootCondition[0];
	private static final String MODID = SweetMagicCore.MODID;

	@SubscribeEvent
	public void onEvent(LootTableLoadEvent event) {	//ルートテーブル読み込み用イベント

		//↓2行はルートテーブルへの追加に必須。
		LootPool pool = event.getTable().getPool("main");
		if (pool == null) { return; }

		ResourceLocation src = event.getName();

		// ボーナスチェスト
		if (src.equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST)) {
			Item item = SMConfig.isHard ? Item.getItemFromBlock(BlockInit.sturdust_crystal_bot) : ItemInit.magicianbeginner_book;
			pool.addEntry(new LootEntryItem(item, 100, 0, FUN, CON, MODID + ":magicianbeginner_book"));
		}

		// 村のチェスト
		else if (src.equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH)) {
			pool.addEntry(new LootEntryItem(ItemInit.fortune_ring, 6, 5, FUN, CON, MODID + ":fortune_ring"));
			pool.addEntry(new LootEntryItem(ItemInit.pendulum_necklace, 3, 5, FUN, CON, MODID + ":pendulum_necklace"));
			pool.addEntry(new LootEntryItem(ItemInit.gravity_pendant, 7, 3, FUN, CON, MODID + ":gravity_pendant"));
			pool.addEntry(new LootEntryItem(ItemInit.mf_sbottle, 2, 5, FUN, CON, MODID + ":mf_sbottle"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.twilightlight), 1, 0, FUN, CON, MODID + ":twilightlight"));
			pool.addEntry(new LootEntryItem(ItemInit.eggbag, 3, 0,
					new LootFunction[] { new SetCount(CON, new RandomValueRange(2, 6)) },
					CON, MODID + ":eggbag"));
			pool.addEntry(new LootEntryItem(ItemInit.seedbag, 2, 0,
					new LootFunction[] { new SetCount(CON, new RandomValueRange(2, 10)) },
					CON, MODID + ":seedbag"));
		}

		// ピラミッドのチェスト
		else if (src.equals(LootTableList.CHESTS_DESERT_PYRAMID)) {
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.twilightlight), 3, 0, FUN, CON, MODID + ":twilightlight"));
			pool.addEntry(new LootEntryItem(ItemInit.emelald_pias, 8, 2, FUN, CON, MODID + ":emelald_pias"));
			pool.addEntry(new LootEntryItem(ItemInit.blood_sucking_ring, 10, 3, FUN, CON, MODID + ":blood_sucking_ring"));
			pool.addEntry(new LootEntryItem(ItemInit.gravity_pendant, 12, 4, FUN, CON, MODID + ":gravity_pendant"));
			pool.addEntry(new LootEntryItem(ItemInit.fortune_ring, 8, 5, FUN, CON, MODID + ":fortune_ring"));
			pool.addEntry(new LootEntryItem(ItemInit.pendulum_necklace, 6, 4, FUN, CON, MODID + ":pendulum_necklace"));
		}

		// スポナーのチェスト
		else if (src.equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) {
			pool.addEntry(new LootEntryItem(ItemInit.mysterious_page, 10, 5,
				new LootFunction[] { new SetCount(CON, new RandomValueRange(2, 6)) },
				CON, MODID + ":mysterious_page"));
			pool.addEntry(new LootEntryItem(ItemInit.fortune_ring, 8, 5, FUN, CON, MODID + ":fortune_ring"));
			pool.addEntry(new LootEntryItem(ItemInit.pendulum_necklace, 10, 3, FUN, CON, MODID + ":pendulum_necklace"));
			pool.addEntry(new LootEntryItem(ItemInit.gravity_pendant, 10, 3, FUN, CON, MODID + ":gravity_pendant"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.twilightlight), 5, 0, FUN, CON, MODID + ":twilightlight"));
		}

		// 廃鉱のチェスト
		else if (src.equals(LootTableList.CHESTS_ABANDONED_MINESHAFT)) {
			pool.addEntry(new LootEntryItem(ItemInit.mysterious_page, 8, 2,
				new LootFunction[] { new SetCount(CON, new RandomValueRange(4, 10)) },
				CON, MODID + ":mysterious_page"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.twilightlight), 7, 0, FUN, CON, MODID + ":twilightlight"));
			pool.addEntry(new LootEntryItem(ItemInit.fortune_ring, 12, 5, FUN, CON, MODID + ":fortune_ring"));
			pool.addEntry(new LootEntryItem(ItemInit.pendulum_necklace, 15, 4, FUN, CON, MODID + ":pendulum_necklace"));
			pool.addEntry(new LootEntryItem(ItemInit.gravity_pendant, 15, 4, FUN, CON, MODID + ":gravity_pendant"));
		}

		// 要塞のチェスト
		else if (src.equals(LootTableList.CHESTS_NETHER_BRIDGE)) {
			pool.addEntry(new LootEntryItem(ItemInit.fire_nasturtium_seed, 12, 6, new LootFunction[] {
					new SetCount(CON, new RandomValueRange(1, 5))			//数　Range…(min,max)
			}, CON, MODID + ":fire_nasturtium_seed"));
			pool.addEntry(new LootEntryItem(ItemInit.aether_crystal, 12, 1,
					new LootFunction[] { new SetCount(CON, new RandomValueRange(2, 8)) },
					CON, MODID + ":aether_crystal"));
			pool.addEntry(new LootEntryItem(ItemInit.emelald_pias, 8, 0, FUN, CON, MODID + ":emelald_pias"));
			pool.addEntry(new LootEntryItem(ItemInit.blood_sucking_ring, 10, 1, FUN, CON, MODID + ":blood_sucking_ring"));
			pool.addEntry(new LootEntryItem(ItemInit.fortune_ring, 10, 5, FUN, CON, MODID + ":fortune_ring"));
			pool.addEntry(new LootEntryItem(ItemInit.pendulum_necklace, 12, 4, FUN, CON, MODID + ":pendulum_necklace"));
			pool.addEntry(new LootEntryItem(ItemInit.gravity_pendant, 13, 4, FUN, CON, MODID + ":gravity_pendant"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.twilightlight), 8, 2, FUN, CON, MODID + ":twilightlight"));
			pool.addEntry(new LootEntryItem(ItemInit.accebag, 1, 0, FUN, CON, MODID + ":accebag"));
		}

		// エンドシティのチェスト
		else if (src.equals(LootTableList.CHESTS_END_CITY_TREASURE)) {
			pool.addEntry(new LootEntryItem(ItemInit.aether_crystal, 12, 1,
				new LootFunction[] { new SetCount(CON, new RandomValueRange(1, 8)) },
				CON, MODID + ":aether_crystal"));
			pool.addEntry(new LootEntryItem(ItemInit.divine_crystal, 8, 2,
				new LootFunction[] { new SetCount(CON, new RandomValueRange(0, 6)) },
				CON, MODID + ":divine_crystal"));
			pool.addEntry(new LootEntryItem(ItemInit.pure_crystal, 3, 3,
				new LootFunction[] { new SetCount(CON, new RandomValueRange(0, 4)) },
				CON, MODID + ":pure_crystal"));
			pool.addEntry(new LootEntryItem(ItemInit.accebag, 7, 3, FUN, CON, MODID + ":accebag"));
		}

		// 森の洋館のチェスト
		else if (src.equals(LootTableList.CHESTS_WOODLAND_MANSION)) {
			pool.addEntry(new LootEntryItem(ItemInit.aether_crystal, 12, 0,
				new LootFunction[] { new SetCount(CON, new RandomValueRange(1, 8)) },
				CON, MODID + ":aether_crystal"));
			pool.addEntry(new LootEntryItem(ItemInit.divine_crystal, 6, 1,
				new LootFunction[] { new SetCount(CON, new RandomValueRange(0, 6)) },
				CON, MODID + ":divine_crystal"));
			pool.addEntry(new LootEntryItem(ItemInit.pure_crystal, 3, 2,
				new LootFunction[] { new SetCount(CON, new RandomValueRange(0, 4)) },
				CON, MODID + ":pure_crystal"));
			pool.addEntry(new LootEntryItem(ItemInit.accebag, 5, 2, FUN, CON, MODID + ":accebag"));
		}

		// 図書館
		else if (src.equals(LootTableList.CHESTS_WOODLAND_MANSION)) {
			pool.addEntry(new LootEntryItem(ItemInit.mysterious_page, 8, 4,
					new LootFunction[] { new SetCount(CON, new RandomValueRange(4, 10)) },
					CON, MODID + ":mysterious_page"));
			pool.addEntry(new LootEntryItem(ItemInit.mystical_page, 4, 2, FUN, CON, MODID + ":mystical_page"));
		}

		// 遺跡廊下
		else if (src.equals(LootTableList.CHESTS_STRONGHOLD_CORRIDOR)) {
			pool.addEntry(new LootEntryItem(ItemInit.aether_crystal, 8, 1,
				new LootFunction[] { new SetCount(CON, new RandomValueRange(1, 6)) },
				CON, MODID + ":aether_crystal"));
			pool.addEntry(new LootEntryItem(ItemInit.divine_crystal, 4, 0,
				new LootFunction[] { new SetCount(CON, new RandomValueRange(0, 4)) },
				CON, MODID + ":divine_crystal"));
			pool.addEntry(new LootEntryItem(ItemInit.pure_crystal, 1, 0,
				new LootFunction[] { new SetCount(CON, new RandomValueRange(0, 2)) },
				CON, MODID + ":pure_crystal"));
		}

		// 釣り
		else  if (src.equals(LootTableList.GAMEPLAY_FISHING_FISH)) {
			pool.addEntry(new LootEntryItem(ItemInit.seaweed, 10, 3, FUN, CON, MODID + ":seaweed"));
		}
	}
}
