package sweetmagic.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.recipe.alstroemeria.AlstroemeriaRecipes;
import sweetmagic.api.recipe.alstroemeria.IAlstroemeriaRecipePlugin;
import sweetmagic.api.recipe.alstroemeria.SMAlstroemeriaRecipePlugin;
import sweetmagic.api.recipe.fermenter.FermenterRecipes;
import sweetmagic.api.recipe.fermenter.IFermenterRecipePlugin;
import sweetmagic.api.recipe.fermenter.SMFermenterRecipePlugin;
import sweetmagic.api.recipe.flourmill.FlourMillRecipes;
import sweetmagic.api.recipe.flourmill.IFlourMillRecipePlugin;
import sweetmagic.api.recipe.flourmill.SMFlourMillRecipePlugin;
import sweetmagic.api.recipe.freezer.FreezerRecipes;
import sweetmagic.api.recipe.freezer.IFreezerRecipePlugin;
import sweetmagic.api.recipe.freezer.SMFreezerRecipePlugin;
import sweetmagic.api.recipe.juicemaker.IJuiceMakerRecipePlugin;
import sweetmagic.api.recipe.juicemaker.JuiceMakerRecipes;
import sweetmagic.api.recipe.juicemaker.SMJuiceMakerRecipePlugin;
import sweetmagic.api.recipe.mftable.IMFTableRecipePlugin;
import sweetmagic.api.recipe.mftable.MFTableRecipes;
import sweetmagic.api.recipe.mftable.SMMFTableRecipePlugin;
import sweetmagic.api.recipe.obmagia.IObMagiaRecipePlugin;
import sweetmagic.api.recipe.obmagia.ObMagiaRecipes;
import sweetmagic.api.recipe.obmagia.SMObMagiaRecipePlugin;
import sweetmagic.api.recipe.oven.IOvenRecipePlugin;
import sweetmagic.api.recipe.oven.OvenRecipes;
import sweetmagic.api.recipe.oven.SMOvenRecipePlugin;
import sweetmagic.api.recipe.pan.IPanRecipePlugin;
import sweetmagic.api.recipe.pan.PanRecipes;
import sweetmagic.api.recipe.pan.SMPanRecipePlugin;
import sweetmagic.api.recipe.pedal.IPedalRecipePlugin;
import sweetmagic.api.recipe.pedal.PedalRecipes;
import sweetmagic.api.recipe.pedal.SMPedalRecipePlugin;
import sweetmagic.api.recipe.pot.IPotRecipePlugin;
import sweetmagic.api.recipe.pot.PotRecipes;
import sweetmagic.api.recipe.pot.SMPotRecipePlugin;

public class RecipeManager {

	// Pluginインスタンス管理
	public static Map<EventPriority, List<IAlstroemeriaRecipePlugin>> alsList = new HashMap<>();
	public static Map<EventPriority, List<IPedalRecipePlugin>> pedalList = new HashMap<>();
	public static Map<EventPriority, List<IFlourMillRecipePlugin>> flourList = new HashMap<>();
	public static Map<EventPriority, List<IOvenRecipePlugin>> ovenList = new HashMap<>();
	public static Map<EventPriority, List<IJuiceMakerRecipePlugin>> juiceList = new HashMap<>();
	public static Map<EventPriority, List<IFreezerRecipePlugin>> freezerList = new HashMap<>();
	public static Map<EventPriority, List<IPanRecipePlugin>> panList = new HashMap<>();
	public static Map<EventPriority, List<IPotRecipePlugin>> potList = new HashMap<>();
	public static Map<EventPriority, List<IObMagiaRecipePlugin>> magiaList = new HashMap<>();
	public static Map<EventPriority, List<IMFTableRecipePlugin>> mfTableList = new HashMap<>();
	public static Map<EventPriority, List<IFermenterRecipePlugin>> fermenteList = new HashMap<>();

	// preInitで処理を行う
	public static void preInitRegisterPlugin(FMLPreInitializationEvent event) {

		// クラスを色々取得
		ASMDataTable dataTable = event.getAsmData();

    	// アルストロメリアレシピプラグイン
    	getAsmDataRecipeAlstroemeria(dataTable.getAll(SMAlstroemeriaRecipePlugin.class.getCanonicalName()));

    	// 創造の台座レシピプラグイン
    	getAsmDataRecipePedal(dataTable.getAll(SMPedalRecipePlugin.class.getCanonicalName()));

    	// 製粉機レシピプラグイン
    	getAsmDataRecipeFlourMill(dataTable.getAll(SMFlourMillRecipePlugin.class.getCanonicalName()));

    	// オーブンレシピプラグイン
    	getAsmDataRecipeOven(dataTable.getAll(SMOvenRecipePlugin.class.getCanonicalName()));

    	// ジュースメイカーレシピプラグイン
    	getAsmDataRecipeJuiceMaker(dataTable.getAll(SMJuiceMakerRecipePlugin.class.getCanonicalName()));

    	// 冷蔵庫レシピプラグイン
    	getAsmDataRecipeFreezer(dataTable.getAll(SMFreezerRecipePlugin.class.getCanonicalName()));

    	// フライパンレシピプラグイン
    	getAsmDataRecipePan(dataTable.getAll(SMPanRecipePlugin.class.getCanonicalName()));

    	// オーブンレシピプラグイン
    	getAsmDataRecipePot(dataTable.getAll(SMPotRecipePlugin.class.getCanonicalName()));

    	// オブマギアレシピプラグイン
    	getAsmDataRecipeObMagia(dataTable.getAll(SMObMagiaRecipePlugin.class.getCanonicalName()));

    	// MFテーブルレシピプラグイン
    	getAsmDataRecipeMFTable(dataTable.getAll(SMMFTableRecipePlugin.class.getCanonicalName()));

    	// 発酵機レシピプラグイン
    	getAsmDataRecipeFer(dataTable.getAll(SMFermenterRecipePlugin.class.getCanonicalName()));
	}

	// postInitでレシピを読み込む
	public static void postInitRegisterPlugin(FMLPostInitializationEvent event) {

		//レシピ登録インスタンス
		AlstroemeriaRecipes alstro = new AlstroemeriaRecipes();
		PedalRecipes pedal = new PedalRecipes();
		FlourMillRecipes flour = new FlourMillRecipes();
		OvenRecipes oven = new OvenRecipes();
		JuiceMakerRecipes juiceMaker = new JuiceMakerRecipes();
		PanRecipes pan = new PanRecipes();
		PotRecipes pot = new PotRecipes();
		ObMagiaRecipes obmagia = new ObMagiaRecipes();
		MFTableRecipes mftable = new MFTableRecipes();
		FermenterRecipes fermente = new FermenterRecipes();
		FreezerRecipes freezer = new FreezerRecipes();

		//優先度
		List<EventPriority> priorityList = new ArrayList<>();
		priorityList.add(EventPriority.LOWEST);
		priorityList.add(EventPriority.LOW);
		priorityList.add(EventPriority.NORMAL);
		priorityList.add(EventPriority.HIGH);
		priorityList.add(EventPriority.HIGHEST);

		//優先度順でレシピ登録処理を行う
		for (EventPriority priority : priorityList) {

			// アルストロメリアレシピ
			for (IAlstroemeriaRecipePlugin plugin : alsList.getOrDefault(priority, new ArrayList<>())) {
				plugin.registerAlstroemeriaRecipe(alstro);
			}

			// アルストロメリアレシピ
			for (IPedalRecipePlugin plugin : pedalList.getOrDefault(priority, new ArrayList<>())) {
				plugin.registerPedalRecipe(pedal);
			}

			// 製粉機レシピ
			for (IFlourMillRecipePlugin plugin : flourList.getOrDefault(priority, new ArrayList<>())) {
				plugin.registerFlourMillRecipe(flour);
			}

			// オーブンレシピ
			for (IOvenRecipePlugin plugin : ovenList.getOrDefault(priority, new ArrayList<>())) {
				plugin.registerOvenRecipe(oven);
			}

			// ジュースメイカーレシピ
			for (IJuiceMakerRecipePlugin plugin : juiceList.getOrDefault(priority, new ArrayList<>())) {
				plugin.registerJuiceMakerRecipe(juiceMaker);
			}

			// 冷蔵庫レシピ
			for (IFreezerRecipePlugin plugin : freezerList.getOrDefault(priority, new ArrayList<>())) {
				plugin.registerFreezerRecipe(freezer);
			}

			// フライパンレシピ
			for (IPanRecipePlugin plugin : panList.getOrDefault(priority, new ArrayList<>())) {
				plugin.registerPanRecipe(pan);
			}

			// 鍋レシピ
			for (IPotRecipePlugin plugin : potList.getOrDefault(priority, new ArrayList<>())) {
				plugin.registerPotRecipe(pot);
			}

			// オブマギアレシピ
			for (IObMagiaRecipePlugin plugin : magiaList.getOrDefault(priority, new ArrayList<>())) {
				plugin.registerObMagiaRecipe(obmagia);
			}

			// MF作業台レシピ
			for (IMFTableRecipePlugin plugin : mfTableList.getOrDefault(priority, new ArrayList<>())) {
				plugin.registerMFTableRecipe(mftable);
			}

			// 発酵機レシピ
			for (IFermenterRecipePlugin plugin : fermenteList.getOrDefault(priority, new ArrayList<>())) {
				plugin.registerFermenterRecipe(fermente);
			}
		}

		SweetMagicAPI.unmodifiableList();

		//インスタンス破棄
		priorityList = null;
	}

	// フライパンレシピのプラグインクラスを取得
	public static void getAsmDataRecipePan( Set<ASMData> data ) {

		//対象を取得する
    	for (ASMData asmData : data) {
    		try {

    			//アノテーション情報を取得する
    			Map<String, Object> annoList = asmData.getAnnotationInfo();
    			String modid = (String) annoList.get("modid");
    			ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) annoList.get("priority");
    			EventPriority priority = EventPriority.NORMAL;

    			//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
    			modid = modid != null ? modid : "";
    			priority = holder == null ? priority : EventPriority.valueOf(holder.getValue());

    			//ModIdが指定されている場合はModが読み込まれているか判断
    			if ("".equals(modid) || Loader.isModLoaded(modid)) {

    				Class<?> asmClass = Class.forName(asmData.getClassName());

    				//----レシピ登録----
        			//プラグインクラスを取得
        			Class<? extends IPanRecipePlugin> asmCss = asmClass.asSubclass(IPanRecipePlugin.class);

        			List<IPanRecipePlugin> pluginList = panList.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmCss.newInstance());
        			panList.put(priority, pluginList);
    			}

    		} catch (Exception e) {
    			SweetMagicCore.logger.error("Pan recipe plugin load error " + asmData.getClassName());
			}
    	}
	}

	// 鍋レシピのプラグインクラスを取得
	public static void getAsmDataRecipePot(Set<ASMData> data) {

		//対象を取得する
		for (ASMData asmData : data) {
			try {

				//アノテーション情報を取得する
				Map<String, Object> annoList = asmData.getAnnotationInfo();
				String modid = (String) annoList.get("modid");
				ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) annoList.get("priority");
				EventPriority priority = EventPriority.NORMAL;

				//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
				modid = modid != null ? modid : "";
				priority = holder == null ? priority : EventPriority.valueOf(holder.getValue());

				//ModIdが指定されている場合はModが読み込まれているか判断
				if ("".equals(modid) || Loader.isModLoaded(modid)) {

					Class<?> asmClass = Class.forName(asmData.getClassName());

					//----レシピ登録----
					//プラグインクラスを取得
					Class<? extends IPotRecipePlugin> asmCss = asmClass.asSubclass(IPotRecipePlugin.class);

					List<IPotRecipePlugin> pluginList = potList.getOrDefault(priority, new ArrayList<>());
					pluginList.add(asmCss.newInstance());
					potList.put(priority, pluginList);
				}

    		} catch (Exception e) {
    			SweetMagicCore.logger.error("Pot recipe plugin load error " + asmData.getClassName());
			}
    	}
	}

	// オーブンレシピのプラグインクラスを取得
	public static void getAsmDataRecipeOven( Set<ASMData> data ) {

		//対象を取得する
    	for (ASMData asmData : data) {
    		try {

    			//アノテーション情報を取得する
    			Map<String, Object> annoList = asmData.getAnnotationInfo();
    			String modid = (String) annoList.get("modid");
    			ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) annoList.get("priority");
    			EventPriority priority = EventPriority.NORMAL;

    			//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
    			modid = modid != null ? modid : "";
    			priority = holder == null ? priority : EventPriority.valueOf(holder.getValue());

    			//ModIdが指定されている場合はModが読み込まれているか判断
    			if ("".equals(modid) || Loader.isModLoaded(modid)) {

    				Class<?> asmClass = Class.forName(asmData.getClassName());

    				//----レシピ登録----
        			//プラグインクラスを取得
        			Class<? extends IOvenRecipePlugin> asmCss = asmClass.asSubclass(IOvenRecipePlugin.class);

        			List<IOvenRecipePlugin> pluginList = ovenList.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmCss.newInstance());
        			ovenList.put(priority, pluginList);
    			}

    		} catch (Exception e) {
    			SweetMagicCore.logger.error("Oven recipe plugin load error " + asmData.getClassName());
			}
    	}
	}

	// ジュースメイカーレシピのプラグインクラスを取得
	public static void getAsmDataRecipeJuiceMaker( Set<ASMData> data ) {

		//対象を取得する
    	for (ASMData asmData : data) {
    		try {

    			//アノテーション情報を取得する
    			Map<String, Object> annoList = asmData.getAnnotationInfo();
    			String modid = (String) annoList.get("modid");
    			ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) annoList.get("priority");
    			EventPriority priority = EventPriority.NORMAL;

    			//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
    			modid = modid != null ? modid : "";
    			priority = holder == null ? priority : EventPriority.valueOf(holder.getValue());

    			//ModIdが指定されている場合はModが読み込まれているか判断
    			if ("".equals(modid) || Loader.isModLoaded(modid)) {

    				Class<?> asmClass = Class.forName(asmData.getClassName());

    				//----レシピ登録----
        			//プラグインクラスを取得
        			Class<? extends IJuiceMakerRecipePlugin> asmCss = asmClass.asSubclass(IJuiceMakerRecipePlugin.class);

        			List<IJuiceMakerRecipePlugin> pluginList = juiceList.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmCss.newInstance());
        			juiceList.put(priority, pluginList);
    			}

    		} catch (Exception e) {
    			SweetMagicCore.logger.error("JuiceMaker recipe plugin load error " + asmData.getClassName());
			}
    	}
	}

	// 冷蔵庫レシピのプラグインクラスを取得
	public static void getAsmDataRecipeFreezer( Set<ASMData> data ) {

		//対象を取得する
    	for (ASMData asmData : data) {
    		try {

    			//アノテーション情報を取得する
    			Map<String, Object> annoList = asmData.getAnnotationInfo();
    			String modid = (String) annoList.get("modid");
    			ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) annoList.get("priority");
    			EventPriority priority = EventPriority.NORMAL;

    			//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
    			modid = modid != null ? modid : "";
    			priority = holder == null ? priority : EventPriority.valueOf(holder.getValue());

    			//ModIdが指定されている場合はModが読み込まれているか判断
    			if ("".equals(modid) || Loader.isModLoaded(modid)) {

    				Class<?> asmClass = Class.forName(asmData.getClassName());

    				//----レシピ登録----
        			//プラグインクラスを取得
        			Class<? extends IFreezerRecipePlugin> asmCss = asmClass.asSubclass(IFreezerRecipePlugin.class);

        			List<IFreezerRecipePlugin> pluginList = freezerList.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmCss.newInstance());
        			freezerList.put(priority, pluginList);
    			}

    		} catch (Exception e) {
    			SweetMagicCore.logger.error("Freezer recipe plugin load error " + asmData.getClassName());
			}
    	}
	}

	// アルストロメリアレシピのプラグインクラスを取得
	public static void getAsmDataRecipeAlstroemeria( Set<ASMData> data ) {

		//対象を取得する
    	for (ASMData asmData : data) {
    		try {

    			//アノテーション情報を取得する
    			Map<String, Object> annoList = asmData.getAnnotationInfo();
    			String modid = (String) annoList.get("modid");
    			ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) annoList.get("priority");
    			EventPriority priority = EventPriority.NORMAL;

    			//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
    			modid = modid != null ? modid : "";
    			priority = holder == null ? priority : EventPriority.valueOf(holder.getValue());

    			//ModIdが指定されている場合はModが読み込まれているか判断
    			if ("".equals(modid) || Loader.isModLoaded(modid)) {

    				Class<?> asmClass = Class.forName(asmData.getClassName());
    				//----レシピ登録----
    				//プラグインクラスを取得
    				Class<? extends IAlstroemeriaRecipePlugin> asmCss = asmClass.asSubclass(IAlstroemeriaRecipePlugin.class);

        			List<IAlstroemeriaRecipePlugin> pluginList = alsList.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmCss.newInstance());
        			alsList.put(priority, pluginList);
    			}

    		} catch (Exception e) {
    			SweetMagicCore.logger.error("Twilight Alstroemeria recipe plugin load error " + asmData.getClassName());
			}
    	}
	}


	// アルストロメリアレシピのプラグインクラスを取得
	public static void getAsmDataRecipePedal( Set<ASMData> data ) {

		//対象を取得する
    	for (ASMData asmData : data) {
    		try {

    			//アノテーション情報を取得する
    			Map<String, Object> annoList = asmData.getAnnotationInfo();
    			String modid = (String) annoList.get("modid");
    			ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) annoList.get("priority");
    			EventPriority priority = EventPriority.NORMAL;

    			//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
    			modid = modid != null ? modid : "";
    			priority = holder == null ? priority : EventPriority.valueOf(holder.getValue());

    			//ModIdが指定されている場合はModが読み込まれているか判断
    			if ("".equals(modid) || Loader.isModLoaded(modid)) {

    				Class<?> asmClass = Class.forName(asmData.getClassName());
    				//----レシピ登録----
    				//プラグインクラスを取得
    				Class<? extends IPedalRecipePlugin> asmCss = asmClass.asSubclass(IPedalRecipePlugin.class);

        			List<IPedalRecipePlugin> pluginList = pedalList.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmCss.newInstance());
        			pedalList.put(priority, pluginList);
    			}

    		} catch (Exception e) {
    			SweetMagicCore.logger.error("PedalCreal recipe plugin load error " + asmData.getClassName());
			}
    	}
	}

	// オブマギアレシピのプラグインクラスを取得
	public static void getAsmDataRecipeObMagia( Set<ASMData> data ) {

		//対象を取得する
    	for (ASMData asmData : data) {
    		try {

    			//アノテーション情報を取得する
    			Map<String, Object> annoList = asmData.getAnnotationInfo();
    			String modid = (String) annoList.get("modid");
    			ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) annoList.get("priority");
    			EventPriority priority = EventPriority.NORMAL;

    			//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
    			modid = modid != null ? modid : "";
    			priority = holder == null ? priority : EventPriority.valueOf(holder.getValue());

    			//ModIdが指定されている場合はModが読み込まれているか判断
    			if ("".equals(modid) || Loader.isModLoaded(modid)) {

    				Class<?> asmClass = Class.forName(asmData.getClassName());

    				//----レシピ登録----
    				//プラグインクラスを取得
    				Class<? extends IObMagiaRecipePlugin> asmCss = asmClass.asSubclass(IObMagiaRecipePlugin.class);

        			List<IObMagiaRecipePlugin> pluginList = magiaList.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmCss.newInstance());
        			magiaList.put(priority, pluginList);
    			}

    		} catch (Exception e) {
    			SweetMagicCore.logger.error("ObMagia recipe plugin load error " + asmData.getClassName());
			}
    	}
	}

	// MFテーブルレシピのプラグインクラスを取得
	public static void getAsmDataRecipeMFTable( Set<ASMData> data ) {

		//対象を取得する
    	for (ASMData asmData : data) {
    		try {

    			//アノテーション情報を取得する
    			Map<String, Object> annoList = asmData.getAnnotationInfo();
    			String modid = (String) annoList.get("modid");
    			ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) annoList.get("priority");
    			EventPriority priority = EventPriority.NORMAL;

    			//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
    			modid = modid != null ? modid : "";
    			priority = holder == null ? priority : EventPriority.valueOf(holder.getValue());

    			//ModIdが指定されている場合はModが読み込まれているか判断
    			if ("".equals(modid) || Loader.isModLoaded(modid)) {

    				Class<?> asmClass = Class.forName(asmData.getClassName());

    				//----レシピ登録----
    				//プラグインクラスを取得
    				Class<? extends IMFTableRecipePlugin> asmCss = asmClass.asSubclass(IMFTableRecipePlugin.class);

        			List<IMFTableRecipePlugin> pluginList = mfTableList.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmCss.newInstance());
        			mfTableList.put(priority, pluginList);
    			}

    		} catch (Exception e) {
    			SweetMagicCore.logger.error("MFTable recipe plugin load error " + asmData.getClassName());
			}
    	}
	}

	// 製粉機レシピのプラグインクラスを取得
	public static void getAsmDataRecipeFlourMill( Set<ASMData> data ) {

		//対象を取得する
    	for (ASMData asmData : data) {
    		try {

    			//アノテーション情報を取得する
    			Map<String, Object> annoList = asmData.getAnnotationInfo();
    			String modid = (String) annoList.get("modid");
    			ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) annoList.get("priority");
    			EventPriority priority = EventPriority.NORMAL;

    			//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
    			modid = modid != null ? modid : "";
    			priority = holder == null ? priority : EventPriority.valueOf(holder.getValue());

    			//ModIdが指定されている場合はModが読み込まれているか判断
    			if ("".equals(modid) || Loader.isModLoaded(modid)) {

    				Class<?> asmClass = Class.forName(asmData.getClassName());

    				//----レシピ登録----
    				//プラグインクラスを取得
        			Class<? extends IFlourMillRecipePlugin> asmCss = asmClass.asSubclass(IFlourMillRecipePlugin.class);

        			List<IFlourMillRecipePlugin> pluginList = flourList.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmCss.newInstance());
        			flourList.put(priority, pluginList);
    			}

    		} catch (Exception e) {
    			SweetMagicCore.logger.error("Flour Mill recipe plugin load error " + asmData.getClassName());
			}
    	}
	}

	// 発酵機レシピのプラグインクラスを取得
	public static void getAsmDataRecipeFer( Set<ASMData> data ) {

		//対象を取得する
    	for (ASMData asmData : data) {
    		try {

    			//アノテーション情報を取得する
    			Map<String, Object> annoList = asmData.getAnnotationInfo();
    			String modid = (String) annoList.get("modid");
    			ModAnnotation.EnumHolder holder = (ModAnnotation.EnumHolder) annoList.get("priority");
    			EventPriority priority = EventPriority.NORMAL;

    			//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
    			modid = modid != null ? modid : "";
    			priority = holder == null ? priority : EventPriority.valueOf(holder.getValue());

    			//ModIdが指定されている場合はModが読み込まれているか判断
    			if ("".equals(modid) || Loader.isModLoaded(modid)) {

    				Class<?> asmClass = Class.forName(asmData.getClassName());

    				//----レシピ登録----
    				//プラグインクラスを取得
        			Class<? extends IFermenterRecipePlugin> asmCss = asmClass.asSubclass(IFermenterRecipePlugin.class);

        			List<IFermenterRecipePlugin> pluginList = fermenteList.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmCss.newInstance());
        			fermenteList.put(priority, pluginList);
    			}

    		} catch (Exception e) {
    			SweetMagicCore.logger.error("Flour Mill recipe plugin load error " + asmData.getClassName());
			}
    	}
	}
}
