package sweetmagic.handlers;

import java.util.Arrays;
import java.util.List;

import defeatedcrow.hac.api.climate.ClimateAPI;
import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.core.climate.ArmorResistantRegister;
import defeatedcrow.hac.core.climate.MobResistantRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import sweetmagic.SweetMagicCore;
import sweetmagic.config.SMConfig;
import sweetmagic.event.AlstroemeriaClickEvent;
import sweetmagic.event.EntitiySpawnEvent;
import sweetmagic.event.EntityItemTossEvent;
import sweetmagic.event.HasItemEvent;
import sweetmagic.event.LivingDethEvent;
import sweetmagic.event.LivingPotionEvent;
import sweetmagic.event.SMHarvestEvent;
import sweetmagic.event.SMLivingDamageEvent;
import sweetmagic.event.SMLoottableEvent;
import sweetmagic.event.XPPickupEvent;
import sweetmagic.init.BiomeInit;
import sweetmagic.init.BlockInit;
import sweetmagic.init.DimensionInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.layer.LayerBarrier;
import sweetmagic.init.entity.layer.LayerEffectBase;
import sweetmagic.init.entity.layer.LayerGlider;
import sweetmagic.init.entity.layer.LayerMagicArray;
import sweetmagic.init.entity.layer.LayerRefresh;
import sweetmagic.init.entity.layer.LayerRegen;
import sweetmagic.init.entity.layer.LayerWandSandryon;
import sweetmagic.init.entity.monster.EntityAncientFairy;
import sweetmagic.init.entity.monster.EntityArchSpider;
import sweetmagic.init.entity.monster.EntityBlackCat;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityBraveSkeleton;
import sweetmagic.init.entity.monster.EntityCreeperCal;
import sweetmagic.init.entity.monster.EntityElectricCube;
import sweetmagic.init.entity.monster.EntityElshariaCurious;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityIfritVerre;
import sweetmagic.init.entity.monster.EntityPhantomZombie;
import sweetmagic.init.entity.monster.EntityPixieVex;
import sweetmagic.init.entity.monster.EntityRepairKitt;
import sweetmagic.init.entity.monster.EntitySandryon;
import sweetmagic.init.entity.monster.EntityShadowGolem;
import sweetmagic.init.entity.monster.EntityShadowHorse;
import sweetmagic.init.entity.monster.EntityShadowWolf;
import sweetmagic.init.entity.monster.EntitySilderGhast;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.init.entity.monster.EntityWindineVerre;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;
import sweetmagic.init.entity.monster.EntityZombieHora;
import sweetmagic.init.entity.monster.EntityZombieKronos;
import sweetmagic.init.tile.chest.TileDisplay;
import sweetmagic.init.tile.chest.TileFruitCrate;
import sweetmagic.init.tile.chest.TileGlassCup;
import sweetmagic.init.tile.chest.TileIronShelf;
import sweetmagic.init.tile.chest.TileMagiaStorage;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.init.tile.chest.TileModenWallRack;
import sweetmagic.init.tile.chest.TileNotePC;
import sweetmagic.init.tile.chest.TilePlateShaped;
import sweetmagic.init.tile.chest.TileRattanBasket;
import sweetmagic.init.tile.chest.TileShocase;
import sweetmagic.init.tile.chest.TileShoeBox;
import sweetmagic.init.tile.chest.TileWandPedal;
import sweetmagic.init.tile.chest.TileWoodChest;
import sweetmagic.init.tile.chest.TileWoodenBox;
import sweetmagic.init.tile.cook.TileFermenter;
import sweetmagic.init.tile.cook.TileFlourMill;
import sweetmagic.init.tile.cook.TileFreezer;
import sweetmagic.init.tile.cook.TileJuiceMaker;
import sweetmagic.init.tile.cook.TilePlate;
import sweetmagic.init.tile.cook.TilePot;
import sweetmagic.init.tile.cook.TileStove;
import sweetmagic.init.tile.cook.TileTray;
import sweetmagic.init.tile.magic.TileAetherCover;
import sweetmagic.init.tile.magic.TileAetherEnchantTable;
import sweetmagic.init.tile.magic.TileAetherFurnace;
import sweetmagic.init.tile.magic.TileAetherHopper;
import sweetmagic.init.tile.magic.TileAltarCreat;
import sweetmagic.init.tile.magic.TileAltarCreationStar;
import sweetmagic.init.tile.magic.TileCleroLanp;
import sweetmagic.init.tile.magic.TileCrystalCore;
import sweetmagic.init.tile.magic.TileFigurineStand;
import sweetmagic.init.tile.magic.TileGravityChest;
import sweetmagic.init.tile.magic.TileMFAccelerator;
import sweetmagic.init.tile.magic.TileMFAetherLanp;
import sweetmagic.init.tile.magic.TileMFArcaneTable;
import sweetmagic.init.tile.magic.TileMFChanger;
import sweetmagic.init.tile.magic.TileMFChangerAdvanced;
import sweetmagic.init.tile.magic.TileMFFisher;
import sweetmagic.init.tile.magic.TileMFForer;
import sweetmagic.init.tile.magic.TileMFFurnace;
import sweetmagic.init.tile.magic.TileMFFurnaceAdvanced;
import sweetmagic.init.tile.magic.TileMFGeneration;
import sweetmagic.init.tile.magic.TileMFHarvester;
import sweetmagic.init.tile.magic.TileMFMMTable;
import sweetmagic.init.tile.magic.TileMFMMTank;
import sweetmagic.init.tile.magic.TileMFPot;
import sweetmagic.init.tile.magic.TileMFSqueezer;
import sweetmagic.init.tile.magic.TileMFSuccessor;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.magic.TileMFTableAdvanced;
import sweetmagic.init.tile.magic.TileMFTank;
import sweetmagic.init.tile.magic.TileMFTankAdvanced;
import sweetmagic.init.tile.magic.TileMFTankCreative;
import sweetmagic.init.tile.magic.TileMagiaFluxCore;
import sweetmagic.init.tile.magic.TileMagiaLantern;
import sweetmagic.init.tile.magic.TileMagiaLight;
import sweetmagic.init.tile.magic.TileMagiaReIncarnation;
import sweetmagic.init.tile.magic.TileMagiaWrite;
import sweetmagic.init.tile.magic.TileMagicBarrier;
import sweetmagic.init.tile.magic.TileParallelInterfere;
import sweetmagic.init.tile.magic.TilePedalCreate;
import sweetmagic.init.tile.magic.TileRegister;
import sweetmagic.init.tile.magic.TileSMSpaner;
import sweetmagic.init.tile.magic.TileSpawnStone;
import sweetmagic.init.tile.magic.TileStardustCrystal;
import sweetmagic.init.tile.magic.TileStardustWish;
import sweetmagic.init.tile.magic.TileToolRepair;
import sweetmagic.init.tile.magic.TileWarpBlock;
import sweetmagic.init.tile.magic.TileWorkbenchStorage;
import sweetmagic.init.tile.plant.TileAlstroemeria;
import sweetmagic.init.tile.plant.TileSannyFlower;
import sweetmagic.worldgen.dungen.map.MapFlugelHouse;
import sweetmagic.worldgen.dungen.map.MapGenCastle;
import sweetmagic.worldgen.dungen.map.MapGenDekaijyu;
import sweetmagic.worldgen.dungen.map.MapGenIdo;
import sweetmagic.worldgen.dungen.map.MapGenMekyu;
import sweetmagic.worldgen.dungen.map.MapGenPyramid;
import sweetmagic.worldgen.dungen.map.MapGenTogijyo;
import sweetmagic.worldgen.dungen.map.MapGenVillager;
import sweetmagic.worldgen.dungen.map.MapWitchHouse;
import sweetmagic.worldgen.dungen.piece.CastlePiece;
import sweetmagic.worldgen.dungen.piece.DekaijyuPiece;
import sweetmagic.worldgen.dungen.piece.FlugelHousePiece;
import sweetmagic.worldgen.dungen.piece.IdoPiece;
import sweetmagic.worldgen.dungen.piece.MekyuPiece;
import sweetmagic.worldgen.dungen.piece.PyramidPiece;
import sweetmagic.worldgen.dungen.piece.TogijyoPiece;
import sweetmagic.worldgen.dungen.piece.VillagerPiece;
import sweetmagic.worldgen.dungen.piece.WitchHousePiece;
import sweetmagic.worldgen.gen.BonusGen;
import sweetmagic.worldgen.gen.CFlowerGen;
import sweetmagic.worldgen.gen.CemeteryGen;
import sweetmagic.worldgen.gen.CledonGen;
import sweetmagic.worldgen.gen.FlowerGardenGen;
import sweetmagic.worldgen.gen.FluitForestFlowerGen;
import sweetmagic.worldgen.gen.MoonGen;
import sweetmagic.worldgen.gen.NetGen;
import sweetmagic.worldgen.gen.SMOreGen;
import sweetmagic.worldgen.gen.SMSkyIslandGen;
import sweetmagic.worldgen.gen.SnowDropGen;
import sweetmagic.worldgen.gen.StickyGen;
import sweetmagic.worldgen.gen.SugarGen;
import sweetmagic.worldgen.gen.WellGen;
import sweetmagic.worldgen.gen.WitchHouseGen;
import sweetmagic.worldgen.gen.WorldVillageGen;

public class RegistryHandler {

	private static final String MODID = SweetMagicCore.MODID;

	public static void Common(FMLPreInitializationEvent event) {

		// レジスター関連をブロック先読み(作物のため)
		BlockInit.init();
		ItemInit.init();
		BlockInit.register();
		ItemInit.register();
		PotionInit.init();
		PotionInit.setList();

		//お飾りお花
		GameRegistry.registerWorldGenerator(new SugarGen(BlockInit.sugarbell_plant, 58), 0);
		GameRegistry.registerWorldGenerator(new CledonGen(BlockInit.clerodendrum), 0);
		GameRegistry.registerWorldGenerator(new StickyGen(BlockInit.sticky_stuff_plant, 128), 0);
		GameRegistry.registerWorldGenerator(new NetGen(BlockInit.whitenet_plant, 11), 0);
		GameRegistry.registerWorldGenerator(new MoonGen(BlockInit.moonblossom_plant, 62), 0);
		GameRegistry.registerWorldGenerator(new MoonGen(BlockInit.dm_plant, 17), 0);
		GameRegistry.registerWorldGenerator(new SugarGen(BlockInit.sannyflower_plant, 21), 1);
		GameRegistry.registerWorldGenerator(new SugarGen(BlockInit.glowflower_plant, 33), 0);
		GameRegistry.registerWorldGenerator(new SugarGen(BlockInit.raspberry_plant, 61, 16), 3);
		GameRegistry.registerWorldGenerator(new FluitForestFlowerGen(), 0);
		GameRegistry.registerWorldGenerator(new FlowerGardenGen(1), 0);
		GameRegistry.registerWorldGenerator(new CFlowerGen(3), 2);
		GameRegistry.registerWorldGenerator(new SnowDropGen(BlockInit.snowdrop, 2), 0);

		GameRegistry.registerWorldGenerator(new CemeteryGen(), 1);	// 墓地生成
		CemeteryGen.initLootTable();									// ルートテーブルInit

		GameRegistry.registerWorldGenerator(new WitchHouseGen(), 1);	// 魔女の家生成
		WitchHouseGen.initLootTable();									// ルートテーブルInit

		GameRegistry.registerWorldGenerator(new WorldVillageGen(), 1);	// 村家生成

		GameRegistry.registerWorldGenerator(new SMSkyIslandGen(), 0);	// 浮島生成

		GameRegistry.registerWorldGenerator(new WellGen(), 0);	// 井戸生成
		WellGen.initLootTable();									// ルートテーブルInit

		GameRegistry.registerWorldGenerator(new BonusGen(), 1);	// ゲッダン生成

		// 鉱石生成
		GameRegistry.registerWorldGenerator(new SMOreGen(), 0);

		// SMディメンション生成
		MapGenStructureIO.registerStructure(MapGenPyramid.Start.class, "pyramid_top");
		PyramidPiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapGenDekaijyu.Start.class, "dekaijyu");
		DekaijyuPiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapGenTogijyo.Start.class, "burassamu");
		TogijyoPiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapGenMekyu.Start.class, "mekyu");
		MekyuPiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapGenIdo.Start.class, "ido");
		IdoPiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapWitchHouse.Start.class, "witchhouse_main");
		WitchHousePiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapGenCastle.Start.class, "castle");
		CastlePiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapGenVillager.Start.class, "villager");
		VillagerPiece.registerIdoPiece();

		MapGenStructureIO.registerStructure(MapFlugelHouse.Start.class, "flugel_house");
		FlugelHousePiece.registerIdoPiece();

		if (Loader.isModLoaded("dcs_lib")) {
			ArmorResistantRegister arr = ArmorResistantRegister.INSTANCE;

			arr.registerMaterial(new ItemStack(ItemInit.aether_choker), 2F, 0.25F);
			arr.registerMaterial(new ItemStack(ItemInit.pure_choker), 3F, 0.375F);
			arr.registerMaterial(new ItemStack(ItemInit.deus_choker), 5F, 0.5F);

			arr.registerMaterial(new ItemStack(ItemInit.magicians_robe), 5F, 5F);
			arr.registerMaterial(new ItemStack(ItemInit.curious_robe), 7.5F, 7.5F);
			arr.registerMaterial(new ItemStack(ItemInit.feary_robe), 7.5F, 7.5F);
			arr.registerMaterial(new ItemStack(ItemInit.ifrite_robe), 7.5F, 7.5F);
			arr.registerMaterial(new ItemStack(ItemInit.kitt_robe), 7.5F, 7.5F);
			arr.registerMaterial(new ItemStack(ItemInit.sandryon_robe), 7.5F, 7.5F);
			arr.registerMaterial(new ItemStack(ItemInit.windine_robe), 7.5F, 7.5F);
			arr.registerMaterial(new ItemStack(ItemInit.witchmadame_robe), 7.5F, 7.5F);
			arr.registerMaterial(new ItemStack(ItemInit.wizard_robe), 7.5F, 7.5F);

			arr.registerMaterial(new ItemStack(ItemInit.magicians_pouch), 2.5F, 2.5F);
			arr.registerMaterial(new ItemStack(ItemInit.master_magia_pouch), 3.5F, 3.5F);

			arr.registerMaterial(new ItemStack(ItemInit.aether_boot), 0.25F, 2.5F);
			arr.registerMaterial(new ItemStack(ItemInit.angel_harness), 0.35F, 5F);

			arr.registerMaterial(new ItemStack(ItemInit.farm_apron), 0.35F, 0.35F);
			arr.registerMaterial(new ItemStack(ItemInit.shop_uniform), 0.35F, 0.35F);
		}
	}

	// tileの登録
	public static void tileHandler () {
		registerTile(TileSannyFlower.class, "ParticleFrower");
		registerTile(TileCleroLanp.class, "CleroLanp");
		registerTile(TileAlstroemeria.class, "Twilight_Alstroemeria");
		registerTile(TileFlourMill.class, "FlourMill");
		registerTile(TileMFChanger.class, "MFChanger");
		registerTile(TileMFTank.class, "MFTank");
		registerTile(TileMFFurnace.class, "MFFurnace");
		registerTile(TileMFPot.class, "MFDM");
		registerTile(TileMFFisher.class, "MFFisher");
		registerTile(TileStove.class, "Stove");
		registerTile(TilePot.class, "Pot");
		registerTile(TileMFTable.class, "MFTable");
		registerTile(TileFermenter.class, "fermenter");
		registerTile(TileSpawnStone.class, "spawnstone");
		registerTile(TileJuiceMaker.class, "juicemaker");
		registerTile(TileFreezer.class, "Freezer");
		registerTile(TilePedalCreate.class, "Pedal");
		registerTile(TileAetherFurnace.class, "AetherFurnace");
		registerTile(TileParallelInterfere.class, "ParallelInterfere");
		registerTile(TileMFChangerAdvanced.class, "MFChangerAdvanced");
		registerTile(TileMFTableAdvanced.class, "MFTableAdvanced");
		registerTile(TileMFTankAdvanced.class, "MFTankAdvanced");
		registerTile(TileModenRack.class, "ModenRack");
		registerTile(TileWandPedal.class, "WandPedal");
		registerTile(TileModenWallRack.class, "ModenWallRack");
		registerTile(TilePlate.class, "PlateRack");
		registerTile(TileMFForer.class, "FlyishForer");
		registerTile(TileStardustCrystal.class, "StardustCrystal");
		registerTile(TileWoodChest.class, "WoodChest");
		registerTile(TileSMSpaner.class, "SMSpaner");
		registerTile(TileRattanBasket.class, "RattanBasket");
		registerTile(TileMagicBarrier.class, "Magicbarrier");
		registerTile(TileWarpBlock.class, "WarpBlock");
		registerTile(TileWandPedal.class, "WandPedal");
		registerTile(TileMFMMTank.class, "MFTankMasterMagia");
		registerTile(TileToolRepair.class, "ToolRepair");
		registerTile(TileGravityChest.class, "GravityChest");
		registerTile(TileMagiaWrite.class, "MagiaWrite");
		registerTile(TileAetherHopper.class, "AetherHopper");
		registerTile(TileMFMMTable.class, "MMTable");
		registerTile(TileStardustWish.class, "StardustWish");
		registerTile(TileMFFurnaceAdvanced.class, "MFFurnaceAdvanced");
		registerTile(TileMFSuccessor.class, "MFSuccessor");
		registerTile(TileMFAetherLanp.class, "MFAetherLanp");
		registerTile(TileMFArcaneTable.class, "MFArcaneTable");
		registerTile(TileMFAccelerator.class, "MFAccelerator");
		registerTile(TileShocase.class, "Showcase");
		registerTile(TileGlassCup.class, "GlassCup");
		registerTile(TileMagiaLight.class, "MagicLight");
		registerTile(TileMFSqueezer.class, "MFSqueezer");
		registerTile(TilePlateShaped.class, "PlateShaped");
		registerTile(TileCrystalCore.class, "CrystalCore");
		registerTile(TileFigurineStand.class, "FigurineStand");
		registerTile(TileMFGeneration.class, "MFGeneration");
		registerTile(TileRegister.class, "SMRegister");
		registerTile(TileWorkbenchStorage.class, "WorkbenchStorage");
		registerTile(TileNotePC.class, "NotePC");
		registerTile(TileShoeBox.class, "ShoeBox");
		registerTile(TileAetherEnchantTable.class, "AetherEnchantTable");
		registerTile(TileTray.class, "SMTray");
		registerTile(TileIronShelf.class, "IronShelf");
		registerTile(TileMagiaLantern.class, "MagiaLantern");
		registerTile(TileMFTankCreative.class, "MFTankCreative");
		registerTile(TileFruitCrate.class, "FruitCrate");
		registerTile(TileWoodenBox.class, "WoodenBox");
		registerTile(TileMagiaStorage.class, "MagiaStorage");
		registerTile(TileAltarCreat.class, "AltarCreat");
		registerTile(TileAltarCreationStar.class, "AltarCreationStar");
		registerTile(TileMagiaFluxCore.class, "MagiaFluxCore");
		registerTile(TileDisplay.class, "Display");
		registerTile(TileMagiaReIncarnation.class, "MagiaReIncarnation");
		registerTile(TileAetherCover.class, "AetherCover");
		registerTile(TileMFHarvester.class, "MFHarvester");
	}

	// 草から種の追加
	public static void addSeed () {
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.rice_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.soybean), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.corn_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.azuki_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.j_radish_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.eggplant_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.lettuce_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.cabbage_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.tomato), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.onion), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.cotton_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.spinach_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.pineapple_seed), 5);
		MinecraftForge.addGrassSeed(new ItemStack(ItemInit.greenpepper_seed), 5);
	}

	// イベント登録
	public static void eventHandler (FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(event);
    	MinecraftForge.EVENT_BUS.register(new SMLoottableEvent());
    	MinecraftForge.EVENT_BUS.register(new SMHarvestEvent());
    	MinecraftForge.EVENT_BUS.register(new LivingPotionEvent());
    	MinecraftForge.EVENT_BUS.register(new SMLivingDamageEvent());
		MinecraftForge.EVENT_BUS.register(new HasItemEvent());
		MinecraftForge.EVENT_BUS.register(new LivingDethEvent());
		MinecraftForge.EVENT_BUS.register(new XPPickupEvent());
		MinecraftForge.EVENT_BUS.register(new EntityItemTossEvent());
		MinecraftForge.EVENT_BUS.register(new EntitiySpawnEvent());
		MinecraftForge.EVENT_BUS.register(new AlstroemeriaClickEvent());
	}

	// レイヤー登録
	public static void layerHandler () {
		if (!SMConfig.isRender) {
			LayerEffectBase.initialiseLayers(LayerBarrier::new);
			LayerEffectBase.initialiseLayers(LayerRefresh::new);
			LayerEffectBase.initialiseLayers(LayerRegen::new);
			LayerEffectBase.initialiseLayers(LayerWandSandryon::new);
			LayerEffectBase.initialiseLayers(LayerMagicArray::new);
			LayerEffectBase.initialiseLayers(LayerGlider::new);
		}
	}

	// ディメンション初期化
	public static void dimentionHandker () {
		DimensionInit.init();
	}

	// バイオーム初期化
	public static void biomeRegster (IForgeRegistry<Biome> registry) {
        BiomeInit.init(registry);
	}

	// スポーンするバイオーム設定
	public static void setSpawnBiome () {

		// えんちちーのスポーン設定
		if (SMConfig.spawn_mob) {

			// スポーン
			for (Biome bio : ForgeRegistries.BIOMES) {

				// 特定のバイオームなら次へ
				if (biomeList.contains(bio)) { continue; }

				EntityRegistry.addSpawn(EntitySkullFrost.class, 25, 1, 3, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityCreeperCal.class, 25, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityEnderShadow.class, 10, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityElectricCube.class, 10, 0, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityBlazeTempest.class, 10, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityArchSpider.class, 10, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityWitchMadameVerre.class, 15, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityWindineVerre.class, 10, 1, 1, EnumCreatureType.MONSTER, bio);
				EntityRegistry.addSpawn(EntityIfritVerre.class, 10, 1, 1, EnumCreatureType.MONSTER, bio);
			}
		}
	}

	// バイオームリスト
	private static final List<Biome> biomeList = Arrays.<Biome> asList(Biomes.MUSHROOM_ISLAND, Biomes.MUSHROOM_ISLAND_SHORE,
			Biomes.HELL, Biomes.VOID, Biomes.SKY);

	public static void registerTile (Class<? extends TileEntity> tileClass, String name) {
		GameRegistry.registerTileEntity(tileClass, new ResourceLocation(MODID, name));
	}

	// 連携周りの登録
	public static void registerPlugin () {

		if (Loader.isModLoaded("dcs_lib")) {

			if (SMConfig.isHard >= 2) {
				ClimateAPI.registerBlock.registerHeatBlock(BlockInit.fire_nasturtium_plant, 3, DCHeatTier.KILN);
			}

			MobResistantRegister mrr = MobResistantRegister.INSTANCE;
			mrr.registerEntityResistant(EntityAncientFairy.class, 20F, 20F);
			mrr.registerEntityResistant(EntityArchSpider.class, 20F, 20F);
			mrr.registerEntityResistant(EntityBlackCat.class, 20F, 20F);
			mrr.registerEntityResistant(EntityBlazeTempest.class, 20F, 20F);
			mrr.registerEntityResistant(EntityBraveSkeleton.class, 20F, 20F);
			mrr.registerEntityResistant(EntityCreeperCal.class, 20F, 20F);
			mrr.registerEntityResistant(EntityElectricCube.class, 20F, 20F);
			mrr.registerEntityResistant(EntityElshariaCurious.class, 20F, 20F);
			mrr.registerEntityResistant(EntityEnderShadow.class, 20F, 20F);
			mrr.registerEntityResistant(EntityIfritVerre.class, 20F, 20F);
			mrr.registerEntityResistant(EntityPhantomZombie.class, 20F, 20F);
			mrr.registerEntityResistant(EntityPixieVex.class, 20F, 20F);
			mrr.registerEntityResistant(EntityRepairKitt.class, 20F, 20F);
			mrr.registerEntityResistant(EntitySandryon.class, 20F, 20F);
			mrr.registerEntityResistant(EntityShadowGolem.class, 20F, 20F);
			mrr.registerEntityResistant(EntityShadowHorse.class, 20F, 20F);
			mrr.registerEntityResistant(EntityShadowWolf.class, 20F, 20F);
			mrr.registerEntityResistant(EntitySilderGhast.class, 20F, 20F);
			mrr.registerEntityResistant(EntitySkullFrost.class, 20F, 20F);
			mrr.registerEntityResistant(EntityWindineVerre.class, 20F, 20F);
			mrr.registerEntityResistant(EntityWitchMadameVerre.class, 20F, 20F);
			mrr.registerEntityResistant(EntityZombieHora.class, 20F, 20F);
			mrr.registerEntityResistant(EntityZombieKronos.class, 20F, 20F);
		}
	}
}
