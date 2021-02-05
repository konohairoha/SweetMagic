package sweetmagic.api.magiaflux;

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

public class MagiaFluxManager {

	// Pluginインスタンス管理
	public static Map<EventPriority, List<IMagiaFluxItemListPlugin>> mfPlug = new HashMap<>();

	// preInitで処理を行う
	public static void preInitRegisterPlugin(FMLPreInitializationEvent event) {

		//クラスを色々取得
		ASMDataTable dataTable = event.getAsmData();

    	//ソーサラーレシピプラグインを取得する
    	getAsmData(dataTable.getAll(SMMagiaFluxItemListPlugin.class.getCanonicalName()));

	}

	// ソーサラーレシピのプラグインクラスを取得
	public static void getAsmData( Set<ASMData> data ) {

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
    				Class<? extends IMagiaFluxItemListPlugin> asmCss = asmClass.asSubclass(IMagiaFluxItemListPlugin.class);

        			List<IMagiaFluxItemListPlugin> pluginList = mfPlug.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmCss.newInstance());
        			mfPlug.put(priority, pluginList);

    			}
    		} catch (Exception e) {
    			SweetMagicCore.logger.error("MagiaFluxList plugin load error " + asmData.getClassName());
			}
    	}
	}

	// postInitでMF定義を読み込む
	public static void postInitRegisterPlugin(FMLPostInitializationEvent event) {

		//登録インスタンス
		MagiaFluxInfo info = new MagiaFluxInfo();

		//優先度
		List<EventPriority> priorityList = new ArrayList<>();
		priorityList.add(EventPriority.LOWEST);
		priorityList.add(EventPriority.LOW);
		priorityList.add(EventPriority.NORMAL);
		priorityList.add(EventPriority.HIGH);
		priorityList.add(EventPriority.HIGHEST);

		//優先度順で登録処理を行う
		for (EventPriority priority : priorityList) {

			//リスト作って優先度順に登録していく
			for (IMagiaFluxItemListPlugin plugin : mfPlug.getOrDefault(priority, new ArrayList<>())) {
				//MF定義
				plugin.setMF(info);
				//MF情報削除
				plugin.deleteMF(info);
			}
		}
		//インスタンス破棄
		priorityList = null;
	}
}
