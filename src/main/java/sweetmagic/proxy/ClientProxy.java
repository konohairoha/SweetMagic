package sweetmagic.proxy;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import sweetmagic.event.PorchRenderEvent;
import sweetmagic.event.WandRenderEvent;
import sweetmagic.handlers.RegistryHandler;
import sweetmagic.init.entity.monster.EntityAncientFairy;
import sweetmagic.init.entity.monster.EntityArchSpider;
import sweetmagic.init.entity.monster.EntityBlackCat;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityBraveSkeleton;
import sweetmagic.init.entity.monster.EntityCreeperCal;
import sweetmagic.init.entity.monster.EntityElectricCube;
import sweetmagic.init.entity.monster.EntityElshariaCurious;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityEvilCrystal;
import sweetmagic.init.entity.monster.EntityFairySkyDwarfKronos;
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
import sweetmagic.init.entity.monster.EntityTamedKit;
import sweetmagic.init.entity.monster.EntityWindineVerre;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;
import sweetmagic.init.entity.monster.EntityZombieHora;
import sweetmagic.init.entity.monster.EntityZombieKronos;
import sweetmagic.init.entity.projectile.EntityBabuleMagic;
import sweetmagic.init.entity.projectile.EntityBlackHole;
import sweetmagic.init.entity.projectile.EntityBlastBomb;
import sweetmagic.init.entity.projectile.EntityBlazeCyclone;
import sweetmagic.init.entity.projectile.EntityCuriousCrystal;
import sweetmagic.init.entity.projectile.EntityCushion;
import sweetmagic.init.entity.projectile.EntityCyclonMagic;
import sweetmagic.init.entity.projectile.EntityDigMagic;
import sweetmagic.init.entity.projectile.EntityDragonBullet;
import sweetmagic.init.entity.projectile.EntityElectricMagic;
import sweetmagic.init.entity.projectile.EntityExplosionMagic;
import sweetmagic.init.entity.projectile.EntityFireMagic;
import sweetmagic.init.entity.projectile.EntityFlameNova;
import sweetmagic.init.entity.projectile.EntityFrostMagic;
import sweetmagic.init.entity.projectile.EntityGravityMagic;
import sweetmagic.init.entity.projectile.EntityLightMagic;
import sweetmagic.init.entity.projectile.EntityLockBullet;
import sweetmagic.init.entity.projectile.EntityMagicCycle;
import sweetmagic.init.entity.projectile.EntityMeteorMagic;
import sweetmagic.init.entity.projectile.EntityNomal;
import sweetmagic.init.entity.projectile.EntityPoisonMagic;
import sweetmagic.init.entity.projectile.EntityRockBlast;
import sweetmagic.init.entity.projectile.EntityShinigFlare;
import sweetmagic.init.entity.projectile.EntitySittableBlock;
import sweetmagic.init.render.block.RenderAetherCover;
import sweetmagic.init.render.block.RenderAetherEnchantTable;
import sweetmagic.init.render.block.RenderAlstroemeria;
import sweetmagic.init.render.block.RenderCrystalCore;
import sweetmagic.init.render.block.RenderFermenter;
import sweetmagic.init.render.block.RenderFigurineStand;
import sweetmagic.init.render.block.RenderGlassCup;
import sweetmagic.init.render.block.RenderJuiceMaker;
import sweetmagic.init.render.block.RenderMFArcaneTable;
import sweetmagic.init.render.block.RenderMFChager;
import sweetmagic.init.render.block.RenderMFFisher;
import sweetmagic.init.render.block.RenderMFHarvester;
import sweetmagic.init.render.block.RenderMFSuccessor;
import sweetmagic.init.render.block.RenderMFTable;
import sweetmagic.init.render.block.RenderMFTank;
import sweetmagic.init.render.block.RenderMagiaStorage;
import sweetmagic.init.render.block.RenderMagiaWrite;
import sweetmagic.init.render.block.RenderMagicFluxCore;
import sweetmagic.init.render.block.RenderModenRack;
import sweetmagic.init.render.block.RenderOven;
import sweetmagic.init.render.block.RenderParallelInterfere;
import sweetmagic.init.render.block.RenderPedalCreate;
import sweetmagic.init.render.block.RenderPot;
import sweetmagic.init.render.block.RenderSMSpawner;
import sweetmagic.init.render.block.RenderStardustCrystal;
import sweetmagic.init.render.block.RenderToolRepair;
import sweetmagic.init.render.block.RenderWandPedal;
import sweetmagic.init.render.block.RenderWorkbenchStorage;
import sweetmagic.init.render.monster.RenderAncientFairy;
import sweetmagic.init.render.monster.RenderArchSpider;
import sweetmagic.init.render.monster.RenderBlackCat;
import sweetmagic.init.render.monster.RenderBlazeTempest;
import sweetmagic.init.render.monster.RenderBraveSkeleton;
import sweetmagic.init.render.monster.RenderCreeperCal;
import sweetmagic.init.render.monster.RenderElectricCube;
import sweetmagic.init.render.monster.RenderElshariaCurious;
import sweetmagic.init.render.monster.RenderEnderShadow;
import sweetmagic.init.render.monster.RenderEvilCrystal;
import sweetmagic.init.render.monster.RenderFairySkyDwarfKronos;
import sweetmagic.init.render.monster.RenderIfritVerre;
import sweetmagic.init.render.monster.RenderPhantomZombie;
import sweetmagic.init.render.monster.RenderPixieVex;
import sweetmagic.init.render.monster.RenderRepairKitt;
import sweetmagic.init.render.monster.RenderSandryon;
import sweetmagic.init.render.monster.RenderShadowGolem;
import sweetmagic.init.render.monster.RenderShadowHorse;
import sweetmagic.init.render.monster.RenderShadowWolf;
import sweetmagic.init.render.monster.RenderSilderGhast;
import sweetmagic.init.render.monster.RenderSkullFrost;
import sweetmagic.init.render.monster.RenderTamedKit;
import sweetmagic.init.render.monster.RenderWindineVerre;
import sweetmagic.init.render.monster.RenderWitchMadameVerre;
import sweetmagic.init.render.monster.RenderZombieHora;
import sweetmagic.init.render.monster.RenderZombieKronos;
import sweetmagic.init.render.projectile.RenderBabuleMagic;
import sweetmagic.init.render.projectile.RenderBlackHole;
import sweetmagic.init.render.projectile.RenderBlastBomb;
import sweetmagic.init.render.projectile.RenderCardNormal;
import sweetmagic.init.render.projectile.RenderCuriousCrystal;
import sweetmagic.init.render.projectile.RenderCushion;
import sweetmagic.init.render.projectile.RenderCyclonMagic;
import sweetmagic.init.render.projectile.RenderCyclone;
import sweetmagic.init.render.projectile.RenderDigMagic;
import sweetmagic.init.render.projectile.RenderDragonBullet;
import sweetmagic.init.render.projectile.RenderElectricMagic;
import sweetmagic.init.render.projectile.RenderFireMagic;
import sweetmagic.init.render.projectile.RenderFrostMagic;
import sweetmagic.init.render.projectile.RenderGravityMagic;
import sweetmagic.init.render.projectile.RenderLightMagic;
import sweetmagic.init.render.projectile.RenderLockBullet;
import sweetmagic.init.render.projectile.RenderMagicCycle;
import sweetmagic.init.render.projectile.RenderMeteorMagic;
import sweetmagic.init.render.projectile.RenderPoisonMagic;
import sweetmagic.init.render.projectile.RenderRockBlast;
import sweetmagic.init.render.projectile.RenderShinigFlare;
import sweetmagic.init.render.projectile.RenderTamagottiMagic;
import sweetmagic.init.tile.chest.TileGlassCup;
import sweetmagic.init.tile.chest.TileMagiaStorage;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.init.tile.chest.TileWandPedal;
import sweetmagic.init.tile.cook.TileFermenter;
import sweetmagic.init.tile.cook.TileFlourMill;
import sweetmagic.init.tile.cook.TileJuiceMaker;
import sweetmagic.init.tile.cook.TilePot;
import sweetmagic.init.tile.magic.TileAetherCover;
import sweetmagic.init.tile.magic.TileAetherEnchantTable;
import sweetmagic.init.tile.magic.TileCrystalCore;
import sweetmagic.init.tile.magic.TileFigurineStand;
import sweetmagic.init.tile.magic.TileMFArcaneTable;
import sweetmagic.init.tile.magic.TileMFChanger;
import sweetmagic.init.tile.magic.TileMFFisher;
import sweetmagic.init.tile.magic.TileMFHarvester;
import sweetmagic.init.tile.magic.TileMFSuccessor;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.magic.TileMFTank;
import sweetmagic.init.tile.magic.TileMagiaFluxCore;
import sweetmagic.init.tile.magic.TileMagiaWrite;
import sweetmagic.init.tile.magic.TileParallelInterfere;
import sweetmagic.init.tile.magic.TilePedalCreate;
import sweetmagic.init.tile.magic.TileSMSpaner;
import sweetmagic.init.tile.magic.TileStardustCrystal;
import sweetmagic.init.tile.magic.TileToolRepair;
import sweetmagic.init.tile.magic.TileWorkbenchStorage;
import sweetmagic.init.tile.plant.TileAlstroemeria;
import sweetmagic.key.ClientKeyHelper;

public class ClientProxy extends CommonProxy {

	public static Configuration CONFIG;
	@Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
		registRender(TileMFHarvester.class, new RenderMFHarvester());
    }

	@Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        // ブロックのレンダー
		registRender(TileMFTank.class, new RenderMFTank());
		registRender(TileMFChanger.class, new RenderMFChager());
		registRender(TileMFFisher.class, new RenderMFFisher());
		registRender(TileMFTable.class, new RenderMFTable());
		registRender(TilePot.class, new RenderPot());
		registRender(TileJuiceMaker.class, new RenderJuiceMaker());
		registRender(TilePedalCreate.class, new RenderPedalCreate());
		registRender(TileParallelInterfere.class, new RenderParallelInterfere());
		registRender(TileFermenter.class, new RenderFermenter());
		registRender(TileModenRack.class, new RenderModenRack());
		registRender(TileStardustCrystal.class, new RenderStardustCrystal());
		registRender(TileSMSpaner.class, new RenderSMSpawner());
		registRender(TileAlstroemeria.class, new RenderAlstroemeria());
		registRender(TileWandPedal.class, new RenderWandPedal());
		registRender(TileMagiaWrite.class, new RenderMagiaWrite());
		registRender(TileToolRepair.class, new RenderToolRepair());
		registRender(TileMFSuccessor.class, new RenderMFSuccessor());
		registRender(TileMFArcaneTable.class, new RenderMFArcaneTable());
		registRender(TileGlassCup.class, new RenderGlassCup());
		registRender(TileCrystalCore.class, new RenderCrystalCore());
		registRender(TileCrystalCore.class, new RenderCrystalCore());
		registRender(TileFigurineStand.class, new RenderFigurineStand());
		registRender(TileFlourMill.class, new RenderOven());
		registRender(TileWorkbenchStorage.class, new RenderWorkbenchStorage());
		registRender(TileAetherEnchantTable.class, new RenderAetherEnchantTable());
		registRender(TileMagiaStorage.class, new RenderMagiaStorage());
		registRender(TileMagiaFluxCore.class, new RenderMagicFluxCore());
		registRender(TileAetherCover.class, new RenderAetherCover());

		// レイヤー登録
		RegistryHandler.layerHandler();
    	MinecraftForge.EVENT_BUS.register(new WandRenderEvent());
    	MinecraftForge.EVENT_BUS.register(new PorchRenderEvent());
    }

	@Override
	public void construct(FMLConstructionEvent event) {
	}

	@Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

	//えんちちの読込み
	@Override
	public void loadEntity() {
		super.loadEntity();
		ClientKeyHelper.registerMCBindings();
		registRender(EntityLightMagic.class, RenderLightMagic.class);
		registRender(EntityFireMagic.class, RenderFireMagic.class);
		registRender(EntityFlameNova.class, RenderFireMagic.class);
		registRender(EntityFrostMagic.class, RenderFrostMagic.class);
		registRender(EntityDigMagic.class, RenderDigMagic.class);
		registRender(EntityCyclonMagic.class, RenderCyclonMagic.class);
		registRender(EntityGravityMagic.class, RenderGravityMagic.class);
		registRender(EntityExplosionMagic.class, RenderTamagottiMagic.class);
		registRender(EntityPoisonMagic.class, RenderPoisonMagic.class);
		registRender(EntityBlazeCyclone.class, RenderCyclone.class);
		registRender(EntityMeteorMagic.class, RenderMeteorMagic.class);
		registRender(EntityRockBlast.class, RenderRockBlast.class);
		registRender(EntityBabuleMagic.class, RenderBabuleMagic.class);
		registRender(EntityShinigFlare.class, RenderShinigFlare.class);
		registRender(EntityBlackHole.class, RenderBlackHole.class);
		registRender(EntitySittableBlock.class, RenderCyclone.class);
		registRender(EntityBlastBomb.class, RenderBlastBomb.class);
		registRender(EntityCuriousCrystal.class, RenderCuriousCrystal.class);
		registRender(EntityCushion.class, RenderCushion.class);
		registRender(EntityMagicCycle.class, RenderMagicCycle.class);
		registRender(EntityLockBullet.class, RenderLockBullet.class);
		registRender(EntityDragonBullet.class, RenderDragonBullet.class);

		registRender(EntityNomal.class, RenderCardNormal.class);

		registRender(EntitySkullFrost.class, RenderSkullFrost.class);
		registRender(EntityCreeperCal.class, RenderCreeperCal.class);
		registRender(EntityBlazeTempest.class, RenderBlazeTempest.class);
		registRender(EntityEnderShadow.class, RenderEnderShadow.class);
		registRender(EntityElectricCube.class, RenderElectricCube.class);
		registRender(EntityArchSpider.class, RenderArchSpider.class);
		registRender(EntityZombieHora.class, RenderZombieHora.class);
		registRender(EntityZombieKronos.class, RenderZombieKronos.class);
		registRender(EntityFairySkyDwarfKronos.class, RenderFairySkyDwarfKronos.class);
		registRender(EntityShadowWolf.class, RenderShadowWolf.class);
		registRender(EntityShadowGolem.class, RenderShadowGolem.class);
		registRender(EntityShadowHorse.class, RenderShadowHorse.class);
		registRender(EntityBlackCat.class, RenderBlackCat.class);
		registRender(EntityTamedKit.class, RenderTamedKit.class);
		registRender(EntityElectricMagic.class, RenderElectricMagic.class);
		registRender(EntityWitchMadameVerre.class, RenderWitchMadameVerre.class);
		registRender(EntityWindineVerre.class, RenderWindineVerre.class);
		registRender(EntityIfritVerre.class, RenderIfritVerre.class);
		registRender(EntityPhantomZombie.class, RenderPhantomZombie.class);
		registRender(EntityEvilCrystal.class, RenderEvilCrystal.class);
		registRender(EntityBraveSkeleton.class, RenderBraveSkeleton.class);
		registRender(EntityPixieVex.class, RenderPixieVex.class);
		registRender(EntityAncientFairy.class, RenderAncientFairy.class);
		registRender(EntitySandryon.class, RenderSandryon.class);
		registRender(EntitySilderGhast.class, RenderSilderGhast.class);
		registRender(EntityElshariaCurious.class, RenderElshariaCurious.class);
		registRender(EntityRepairKitt.class, RenderRepairKitt.class);
	}

	//えんちちのれんだー
	private static void registRender(Class<? extends Entity> cls, final Class<? extends Render> render) {
		RenderingRegistry.registerEntityRenderingHandler(cls, new IRenderFactory() {
			@Override
			public Render createRenderFor(RenderManager manager) {
				try {
					return render.getConstructor(manager.getClass()).newInstance(manager);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	// たいるえんちちーのれんだー
	private static void registRender (Class<? extends TileEntity> tile, TileEntitySpecialRenderer render) {
		ClientRegistry.bindTileEntitySpecialRenderer(tile, render);
	}

	@Override
	public boolean isJumpPressed() {
		return FMLClientHandler.instance().getClient().gameSettings.keyBindJump.isKeyDown();
	}

	@Override
	public boolean isDushPressed() {
		return FMLClientHandler.instance().getClient().gameSettings.keyBindSprint.isKeyDown();
	}
}
