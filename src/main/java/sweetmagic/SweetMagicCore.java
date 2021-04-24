package sweetmagic;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import sweetmagic.api.magiaflux.MagiaFluxManager;
import sweetmagic.config.SMConfig;
import sweetmagic.handlers.OreDictionaryHandler;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.handlers.RecipeHandler;
import sweetmagic.handlers.RegistryHandler;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.AdvancedInit;
import sweetmagic.proxy.CommonProxy;
import sweetmagic.recipe.RecipeManager;
import sweetmagic.util.SMChunkLoader;

/*
 * modを読み込む用のコアクラス
 * 登録はせずに呼び出しだけを行う
 */
@Mod(modid = SweetMagicCore.MODID, name = SweetMagicCore.NAME, version = SweetMagicCore.VERSION)
public class SweetMagicCore {

    public static final String MODID = "sweetmagic";
    public static final String NAME = "SweetMagic";
    public static final String VERSION = "1.1.5";
    public static final String CLIENTPROXY = "sweetmagic.proxy.ClientProxy";
    public static final String COMMONPROXY = "sweetmagic.proxy.CommonProxy";

    // GUIを考えてMODのインスタンスを作っておく
    @Instance(SweetMagicCore.MODID)
  	public static SweetMagicCore INSTANCE;
  	public static CreativeTabs SMTab = new SMTab("sweetmagicTab"), SMMagicTab = new SMMagicTab("sweetmagicMagicTab"), SMFoodTab = new SMFoodTab("sweetmagicFoodTab");
  	@SidedProxy(clientSide = SweetMagicCore.CLIENTPROXY, serverSide = SweetMagicCore.COMMONPROXY)
  	public static CommonProxy proxy;
  	public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    	this.proxy.preInit(event);

    	// すぐconfigの読み込みをする
    	SMConfig.INSTANCE.load(event.getModConfigurationDirectory());

    	this.logger = event.getModLog();

    	// tileの登録
    	RegistryHandler.tileHandler();

    	this.proxy.registerEntityRender();
    	this.proxy.loadEntity();
		this.proxy.loadVillager();

		// Plugin - レシピクラス読み込み
    	RecipeManager.preInitRegisterPlugin(event);

		// Plugin - MFアイテム登録クラス読み込み
    	MagiaFluxManager.preInitRegisterPlugin(event);

		// パケット
		PacketHandler.register();

		// チャンクローダー
		SMChunkLoader.getInstance().preInit();

		// 進捗読み込み
		AdvancedInit.register();

    }

    @EventHandler
    public void construct(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    	this.proxy.init(event);

    	// イベント登録
    	RegistryHandler.eventHandler(event);

    	// 鉱石辞書登録
    	OreDictionaryHandler.registerOreDictionary();

    	// 種の追加
    	RegistryHandler.addSeed();

    	// GUIの登録
        NetworkRegistry.INSTANCE.registerGuiHandler(SweetMagicCore.INSTANCE, new SMGuiHandler());

        // ディメンションの読み込み
        RegistryHandler.dimentionHandker();

		// 追加精錬読み込み
		RecipeHandler.registerSmelting();

        // JEI追加レシピ
        RecipeHandler.JEIaddRecipe();

        // 追加モブのスポーンするバイオームを設定
        RegistryHandler.setSpawnBiome();
    }

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		this.proxy.postInit(event);

		// Plugin - レシピ登録
		RecipeManager.postInitRegisterPlugin(event);

		// Plugin - アイテムにMF登録
		MagiaFluxManager.postInitRegisterPlugin(event);

        // 追加レシピ
        RecipeHandler.registerCrafting();

	}

	// バイオーム読み込み
    @SubscribeEvent
    public void registerBiomes(RegistryEvent.Register<Biome> event) {
        IForgeRegistry<Biome> registry = event.getRegistry();
        RegistryHandler.biomeRegster(registry);

    }

	public static ResourceLocation prefix(String name) {
		return new ResourceLocation(MODID, name);
	}
}
