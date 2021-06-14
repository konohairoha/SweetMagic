package sweetmagic.proxy;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
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
import sweetmagic.event.WandRenderEvent;
import sweetmagic.handlers.RegistryHandler;
import sweetmagic.init.entity.monster.EntityAncientFairy;
import sweetmagic.init.entity.monster.EntityArchSpider;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityBraveSkeleton;
import sweetmagic.init.entity.monster.EntityCreeperCal;
import sweetmagic.init.entity.monster.EntityElectricCube;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityIfritVerre;
import sweetmagic.init.entity.monster.EntityPhantomZombie;
import sweetmagic.init.entity.monster.EntityPixieVex;
import sweetmagic.init.entity.monster.EntitySandryon;
import sweetmagic.init.entity.monster.EntityShadowGolem;
import sweetmagic.init.entity.monster.EntityShadowWolf;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.init.entity.monster.EntityWindineVerre;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;
import sweetmagic.init.entity.monster.EntityZombieHora;
import sweetmagic.init.entity.monster.EntityZombieKronos;
import sweetmagic.init.entity.projectile.EntityAbsoluteZeroMagic;
import sweetmagic.init.entity.projectile.EntityBabuleMagic;
import sweetmagic.init.entity.projectile.EntityBlackHole;
import sweetmagic.init.entity.projectile.EntityBlazeCyclone;
import sweetmagic.init.entity.projectile.EntityBlazeEndMagic;
import sweetmagic.init.entity.projectile.EntityCyclonMagic;
import sweetmagic.init.entity.projectile.EntityDigMagic;
import sweetmagic.init.entity.projectile.EntityElectricMagic;
import sweetmagic.init.entity.projectile.EntityExplosionMagic;
import sweetmagic.init.entity.projectile.EntityFireMagic;
import sweetmagic.init.entity.projectile.EntityFlameNova;
import sweetmagic.init.entity.projectile.EntityFrostMagic;
import sweetmagic.init.entity.projectile.EntityFrostRain;
import sweetmagic.init.entity.projectile.EntityGravityMagic;
import sweetmagic.init.entity.projectile.EntityLightMagic;
import sweetmagic.init.entity.projectile.EntityMeteorMagic;
import sweetmagic.init.entity.projectile.EntityNomal;
import sweetmagic.init.entity.projectile.EntityPoisonMagic;
import sweetmagic.init.entity.projectile.EntityRockBlast;
import sweetmagic.init.entity.projectile.EntityShinigFlare;
import sweetmagic.init.render.block.RenderFermenter;
import sweetmagic.init.render.block.RenderJuiceMaker;
import sweetmagic.init.render.block.RenderMFArcaneTable;
import sweetmagic.init.render.block.RenderMFFisher;
import sweetmagic.init.render.block.RenderMFSuccessor;
import sweetmagic.init.render.block.RenderMFTable;
import sweetmagic.init.render.block.RenderMagiaWrite;
import sweetmagic.init.render.block.RenderModenRack;
import sweetmagic.init.render.block.RenderParallelInterfere;
import sweetmagic.init.render.block.RenderPedalCreate;
import sweetmagic.init.render.block.RenderPot;
import sweetmagic.init.render.block.RenderSMSpawner;
import sweetmagic.init.render.block.RenderStardustCrystal;
import sweetmagic.init.render.block.RenderTileAlstroemeria;
import sweetmagic.init.render.block.RenderTileMFChager;
import sweetmagic.init.render.block.RenderTileMFTank;
import sweetmagic.init.render.block.RenderToolRepair;
import sweetmagic.init.render.block.RenderWandPedal;
import sweetmagic.init.render.monster.RenderAncientFairy;
import sweetmagic.init.render.monster.RenderArchSpider;
import sweetmagic.init.render.monster.RenderBlazeTempest;
import sweetmagic.init.render.monster.RenderBraveSkeleton;
import sweetmagic.init.render.monster.RenderCreeperCal;
import sweetmagic.init.render.monster.RenderElectricCube;
import sweetmagic.init.render.monster.RenderEnderShadow;
import sweetmagic.init.render.monster.RenderIfritVerre;
import sweetmagic.init.render.monster.RenderPhantomZombie;
import sweetmagic.init.render.monster.RenderPixieVex;
import sweetmagic.init.render.monster.RenderSandryon;
import sweetmagic.init.render.monster.RenderShadowGolem;
import sweetmagic.init.render.monster.RenderShadowWolf;
import sweetmagic.init.render.monster.RenderSkullFrost;
import sweetmagic.init.render.monster.RenderWindineVerre;
import sweetmagic.init.render.monster.RenderWitchMadameVerre;
import sweetmagic.init.render.monster.RenderZombieHora;
import sweetmagic.init.render.monster.RenderZombieKronos;
import sweetmagic.init.render.projectile.RenderAbsoluteZeroMagic;
import sweetmagic.init.render.projectile.RenderBabuleMagic;
import sweetmagic.init.render.projectile.RenderBlackHole;
import sweetmagic.init.render.projectile.RenderBlazeEndMagic;
import sweetmagic.init.render.projectile.RenderCardNormal;
import sweetmagic.init.render.projectile.RenderCyclonMagic;
import sweetmagic.init.render.projectile.RenderCyclone;
import sweetmagic.init.render.projectile.RenderDigMagic;
import sweetmagic.init.render.projectile.RenderElectricMagic;
import sweetmagic.init.render.projectile.RenderFireMagic;
import sweetmagic.init.render.projectile.RenderFrostMagic;
import sweetmagic.init.render.projectile.RenderFrostRain;
import sweetmagic.init.render.projectile.RenderGravityMagic;
import sweetmagic.init.render.projectile.RenderLightMagic;
import sweetmagic.init.render.projectile.RenderMeteorMagic;
import sweetmagic.init.render.projectile.RenderPoisonMagic;
import sweetmagic.init.render.projectile.RenderRockBlast;
import sweetmagic.init.render.projectile.RenderShinigFlare;
import sweetmagic.init.render.projectile.RenderTamagottiMagic;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.init.tile.chest.TileWandPedal;
import sweetmagic.init.tile.cook.TileFermenter;
import sweetmagic.init.tile.cook.TileJuiceMaker;
import sweetmagic.init.tile.cook.TilePot;
import sweetmagic.init.tile.magic.TileMFArcaneTable;
import sweetmagic.init.tile.magic.TileMFChanger;
import sweetmagic.init.tile.magic.TileMFFisher;
import sweetmagic.init.tile.magic.TileMFSuccessor;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.magic.TileMFTank;
import sweetmagic.init.tile.magic.TileMagiaWrite;
import sweetmagic.init.tile.magic.TileParallelInterfere;
import sweetmagic.init.tile.magic.TilePedalCreate;
import sweetmagic.init.tile.magic.TileSMSpaner;
import sweetmagic.init.tile.magic.TileStardustCrystal;
import sweetmagic.init.tile.magic.TileToolRepair;
import sweetmagic.init.tile.plant.TileAlstroemeria;
import sweetmagic.key.ClientKeyHelper;

public class ClientProxy extends CommonProxy {

	public static Configuration CONFIG;
	@Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

	@Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        // ブロックのレンダー
		ClientRegistry.bindTileEntitySpecialRenderer(TileMFTank.class, new RenderTileMFTank());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMFChanger.class, new RenderTileMFChager());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMFFisher.class, new RenderMFFisher());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMFTable.class, new RenderMFTable());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePot.class, new RenderPot());
		ClientRegistry.bindTileEntitySpecialRenderer(TileJuiceMaker.class, new RenderJuiceMaker());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePedalCreate.class, new RenderPedalCreate());
		ClientRegistry.bindTileEntitySpecialRenderer(TileParallelInterfere.class, new RenderParallelInterfere());
		ClientRegistry.bindTileEntitySpecialRenderer(TileFermenter.class, new RenderFermenter());
		ClientRegistry.bindTileEntitySpecialRenderer(TileModenRack.class, new RenderModenRack());
		ClientRegistry.bindTileEntitySpecialRenderer(TileStardustCrystal.class, new RenderStardustCrystal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSMSpaner.class, new RenderSMSpawner());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlstroemeria.class, new RenderTileAlstroemeria());
		ClientRegistry.bindTileEntitySpecialRenderer(TileWandPedal.class, new RenderWandPedal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagiaWrite.class, new RenderMagiaWrite());
		ClientRegistry.bindTileEntitySpecialRenderer(TileToolRepair.class, new RenderToolRepair());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMFSuccessor.class, new RenderMFSuccessor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMFArcaneTable.class, new RenderMFArcaneTable());

		// レイヤー登録
		RegistryHandler.layerHandler();
    	MinecraftForge.EVENT_BUS.register(new WandRenderEvent());
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
		registRender(EntityFrostRain.class, RenderFrostRain.class);
		registRender(EntityRockBlast.class, RenderRockBlast.class);
		registRender(EntityBabuleMagic.class, RenderBabuleMagic.class);
		registRender(EntityShinigFlare.class, RenderShinigFlare.class);
		registRender(EntityAbsoluteZeroMagic.class, RenderAbsoluteZeroMagic.class);
		registRender(EntityBlazeEndMagic.class, RenderBlazeEndMagic.class);
		registRender(EntityBlackHole.class, RenderBlackHole.class);

		registRender(EntityNomal.class, RenderCardNormal.class);

		registRender(EntitySkullFrost.class, RenderSkullFrost.class);
		registRender(EntityCreeperCal.class, RenderCreeperCal.class);
		registRender(EntityBlazeTempest.class, RenderBlazeTempest.class);
		registRender(EntityEnderShadow.class, RenderEnderShadow.class);
		registRender(EntityElectricCube.class, RenderElectricCube.class);
		registRender(EntityArchSpider.class, RenderArchSpider.class);
		registRender(EntityZombieHora.class, RenderZombieHora.class);
		registRender(EntityZombieKronos.class, RenderZombieKronos.class);
		registRender(EntityShadowWolf.class, RenderShadowWolf.class);
		registRender(EntityShadowGolem.class, RenderShadowGolem.class);
		registRender(EntityElectricMagic.class, RenderElectricMagic.class);
		registRender(EntityWitchMadameVerre.class, RenderWitchMadameVerre.class);
		registRender(EntityWindineVerre.class, RenderWindineVerre.class);
		registRender(EntityIfritVerre.class, RenderIfritVerre.class);
		registRender(EntityPhantomZombie.class, RenderPhantomZombie.class);
		registRender(EntityBraveSkeleton.class, RenderBraveSkeleton.class);
		registRender(EntityPixieVex.class, RenderPixieVex.class);
		registRender(EntityAncientFairy.class, RenderAncientFairy.class);
		registRender(EntitySandryon.class, RenderSandryon.class);
	}

	//えんちちのれんだー
	public static void registRender(Class<? extends Entity> cls, final Class<? extends Render> render) {
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

	@Override
	public boolean isJumpPressed() {
		return FMLClientHandler.instance().getClient().gameSettings.keyBindJump.isKeyDown();
	}

	@Override
	public boolean isDushPressed() {
		return FMLClientHandler.instance().getClient().gameSettings.keyBindSprint.isKeyDown();
	}
}