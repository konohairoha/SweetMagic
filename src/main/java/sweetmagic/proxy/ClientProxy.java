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
import sweetmagic.init.entity.monster.EntityArchSpider;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityCreeperCal;
import sweetmagic.init.entity.monster.EntityElectricCube;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityPhantomZombie;
import sweetmagic.init.entity.monster.EntityShadowGolem;
import sweetmagic.init.entity.monster.EntityShadowWolf;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;
import sweetmagic.init.entity.monster.EntityZombieHora;
import sweetmagic.init.entity.monster.EntityZombieKronos;
import sweetmagic.init.entity.projectile.EntityBlazeCyclone;
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
import sweetmagic.init.render.block.RenderFermenter;
import sweetmagic.init.render.block.RenderJuiceMaker;
import sweetmagic.init.render.block.RenderMFFisher;
import sweetmagic.init.render.block.RenderMFTable;
import sweetmagic.init.render.block.RenderModenRack;
import sweetmagic.init.render.block.RenderParallelInterfere;
import sweetmagic.init.render.block.RenderPedalCreate;
import sweetmagic.init.render.block.RenderPot;
import sweetmagic.init.render.block.RenderSMSpawner;
import sweetmagic.init.render.block.RenderStardustCrystal;
import sweetmagic.init.render.block.RenderTileAlstroemeria;
import sweetmagic.init.render.block.RenderTileMFChager;
import sweetmagic.init.render.block.RenderTileMFTank;
import sweetmagic.init.render.monster.RenderArchSpider;
import sweetmagic.init.render.monster.RenderBlazeTempest;
import sweetmagic.init.render.monster.RenderCreeperCal;
import sweetmagic.init.render.monster.RenderElectricCube;
import sweetmagic.init.render.monster.RenderEnderShadow;
import sweetmagic.init.render.monster.RenderPhantomZombie;
import sweetmagic.init.render.monster.RenderShadowGolem;
import sweetmagic.init.render.monster.RenderShadowWolf;
import sweetmagic.init.render.monster.RenderSkullFrost;
import sweetmagic.init.render.monster.RenderWitchMadameVerre;
import sweetmagic.init.render.monster.RenderZombieHora;
import sweetmagic.init.render.monster.RenderZombieKronos;
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
import sweetmagic.init.render.projectile.RenderTamagottiMagic;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.init.tile.cook.TileFermenter;
import sweetmagic.init.tile.cook.TileJuiceMaker;
import sweetmagic.init.tile.cook.TilePot;
import sweetmagic.init.tile.magic.TileMFChanger;
import sweetmagic.init.tile.magic.TileMFFisher;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.magic.TileMFTank;
import sweetmagic.init.tile.magic.TileParallelInterfere;
import sweetmagic.init.tile.magic.TilePedalCreate;
import sweetmagic.init.tile.magic.TileSMSpaner;
import sweetmagic.init.tile.magic.TileStardustCrystal;
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
		registRender(EntityPhantomZombie.class, RenderPhantomZombie.class);
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
}