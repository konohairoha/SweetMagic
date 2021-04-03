package sweetmagic.event;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.util.SMUtil;

public class SMLoottableEvent {

	@SubscribeEvent
	public void onEvent(LootTableLoadEvent event) {	//ルートテーブル読み込み用イベント

		//↓2行はルートテーブルへの追加に必須。
		LootPool pool = event.getTable().getPool("main");
		if (pool == null) { return; }

		ResourceLocation src = event.getName();


		// ボーナスチェスト
		if (src.equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST)) {
			pool.addEntry(new LootEntryItem(ItemInit.magicianbeginner_book, 100, 0, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":magicianbeginner_book"));
		}

		// 村のチェスト
		else if (src.equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH)) {
			pool.addEntry(new LootEntryItem(ItemInit.blueberry, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":blueberry"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.chestnut_sapling), 10, 6, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":chestnut_sapling"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.lemon_sapling), 10, 6, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":lemon_sapling"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.orange_sapling), 10, 6, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":orange_sapling"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.coconut_sapling), 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":coconut_sapling"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.prism_sapling), 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":prism_sapling"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.banana_sapling), 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":banana_sapling"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.estor_sapling), 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":estor_sapling"));
			pool.addEntry(new LootEntryItem(ItemInit.mf_sbottle, 5, 5, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":mf_sbottle"));
		}

		// ピラミッドのチェスト
		else if (src.equals(LootTableList.CHESTS_DESERT_PYRAMID)) {
			pool.addEntry(new LootEntryItem(ItemInit.sannyflower_seed, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":sannyflower"));
			pool.addEntry(new LootEntryItem(ItemInit.moonblossom_seed, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":moonblossom"));
			pool.addEntry(new LootEntryItem(ItemInit.dm_seed, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":dm_seed"));
		}

		// スポナーのチェスト
		else if (src.equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) {
			pool.addEntry(new LootEntryItem(ItemInit.mysterious_page, 7, 5, new LootFunction[] {
					new SetMetadata(new LootCondition[0], new RandomValueRange(0,0)),	//メタデータ
					new SetCount(new LootCondition[0], new RandomValueRange(1, 3))			//数　Range…(min,max)
			}, new LootCondition[0], SweetMagicCore.MODID + ":mysterious_page"));
			pool.addEntry(new LootEntryItem(ItemInit.sannyflower_seed, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":sannyflower"));
			pool.addEntry(new LootEntryItem(ItemInit.moonblossom_seed, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":moonblossom"));
			pool.addEntry(new LootEntryItem(ItemInit.dm_seed, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":dm_seed"));
			pool.addEntry(new LootEntryItem(ItemInit.fortune_ring, 8, 2, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":fortune_ring"));
		}
		// 廃鉱のチェスト
		if (src.equals(LootTableList.CHESTS_ABANDONED_MINESHAFT)) {
			pool.addEntry(new LootEntryItem(ItemInit.mysterious_page, 4, 2,new LootFunction[] {
					new SetMetadata(new LootCondition[0], new RandomValueRange(0,0)),	//メタデータ
					new SetCount(new LootCondition[0], new RandomValueRange(1, 3))			//数　Range…(min,max)
			}, new LootCondition[0], SweetMagicCore.MODID + ":mysterious_page"));
			pool.addEntry(new LootEntryItem(ItemInit.sannyflower_seed, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":sannyflower"));
			pool.addEntry(new LootEntryItem(ItemInit.moonblossom_seed, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":moonblossom"));
			pool.addEntry(new LootEntryItem(ItemInit.dm_seed, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":dm_seed"));
			pool.addEntry(new LootEntryItem(ItemInit.blueberry, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":blueberry"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.chestnut_sapling), 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":chestnut_sapling"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.lemon_sapling), 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":lemon_sapling"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.orange_sapling), 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":orange_sapling"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.coconut_sapling), 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":coconut_sapling"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.prism_sapling), 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":prism_sapling"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.banana_sapling), 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":banana_sapling"));
			pool.addEntry(new LootEntryItem(SMUtil.getItemBlock(BlockInit.estor_sapling), 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":estor_sapling"));
			pool.addEntry(new LootEntryItem(ItemInit.fortune_ring, 4, 2, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":fortune_ring"));
		}

		// 要塞のチェスト
		else if (src.equals(LootTableList.CHESTS_NETHER_BRIDGE)) {
			pool.addEntry(new LootEntryItem(ItemInit.fire_nasturtium_seed, 12, 6,new LootFunction[] {
					new SetMetadata(new LootCondition[0], new RandomValueRange(0,0)),	//メタデータ
					new SetCount(new LootCondition[0], new RandomValueRange(1, 5))			//数　Range…(min,max)
			}, new LootCondition[0], SweetMagicCore.MODID + ":fire_nasturtium_seed"));
			pool.addEntry(new LootEntryItem(ItemInit.sannyflower_petal, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":sannyflower"));
			pool.addEntry(new LootEntryItem(ItemInit.moonblossom_petal, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":moonblossom"));
			pool.addEntry(new LootEntryItem(ItemInit.dm_flower, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":dm_seed"));
			pool.addEntry(new LootEntryItem(ItemInit.mysterious_page, 20, 8, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":mysterious_page"));
		}

		// エンドシティのチェスト
		else if (src.equals(LootTableList.CHESTS_END_CITY_TREASURE)) {
			pool.addEntry(new LootEntryItem(ItemInit.aether_crystal, 4, 0, new LootFunction[] {
					new SetCount(new LootCondition[0], new RandomValueRange(1, 8))
			}, new LootCondition[0], SweetMagicCore.MODID + ":aether_crystal"));
			pool.addEntry(new LootEntryItem(ItemInit.divine_crystal, 3, 0, new LootFunction[] {
					new SetCount(new LootCondition[0], new RandomValueRange(0, 6))
			}, new LootCondition[0], SweetMagicCore.MODID + ":divine_crystal"));
			pool.addEntry(new LootEntryItem(ItemInit.pure_crystal, 2, 0, new LootFunction[] {
					new SetCount(new LootCondition[0], new RandomValueRange(0, 4))
			}, new LootCondition[0], SweetMagicCore.MODID + ":pure_crystal"));
			pool.addEntry(new LootEntryItem(ItemInit.emelald_pias, 2, 1, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":emelald_pias"));
			pool.addEntry(new LootEntryItem(ItemInit.blood_sucking_ring, 2, 1, new LootFunction[0], new LootCondition[0], SweetMagicCore.MODID + ":blood_sucking_ring"));
		}
	}
}
