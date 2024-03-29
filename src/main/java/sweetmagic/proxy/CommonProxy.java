package sweetmagic.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.RegistryHandler;
import sweetmagic.init.BookInit;
import sweetmagic.init.VillagerInit;
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
import sweetmagic.init.entity.projectile.EntityMagicItem;
import sweetmagic.init.entity.projectile.EntityMeteorMagic;
import sweetmagic.init.entity.projectile.EntityNomal;
import sweetmagic.init.entity.projectile.EntityPoisonMagic;
import sweetmagic.init.entity.projectile.EntityRockBlast;
import sweetmagic.init.entity.projectile.EntityShinigFlare;
import sweetmagic.init.entity.projectile.EntitySittableBlock;
import sweetmagic.init.entity.projectile.EntitySuperNova;

public class CommonProxy {

	public static int count = 0;
	public static VillagerProfession cook;

    public void preInit(FMLPreInitializationEvent event) {
    	RegistryHandler.Common(event);
    }

	public void init(FMLInitializationEvent event) {}
    public void construct(FMLConstructionEvent event) {}
	public void registerEntityRender() {}
	public boolean isJumpPressed() { return false; }
	public boolean isDushPressed() { return false; }

	public void postInit(FMLPostInitializationEvent event) {
		BookInit.init();
	}

	public void loadEntity() {

		arrowEntity(EntityLightMagic.class, "lightmagic");
		arrowEntity(EntityFireMagic.class, "firemagic");
		arrowEntity(EntityFrostMagic.class, "frostmagic");
		arrowEntity(EntityDigMagic.class, "digmagic");
		arrowEntity(EntityCyclonMagic.class, "cyclonagic");
		arrowEntity(EntityGravityMagic.class, "gravitymagic");
		arrowEntity(EntityElectricMagic.class, "electricmagic");
		arrowEntity(EntityExplosionMagic.class, "tamagottimagic");
		arrowEntity(EntityBlazeCyclone.class, "cyclone");
		arrowEntity(EntityNomal.class, "cardnormal");
		arrowEntity(EntityFlameNova.class, "flamenova");
		arrowEntity(EntityShadowWolf.class, "shadowwolf");
		arrowEntity(EntityShadowGolem.class, "shadowgolem");
		arrowEntity(EntityShadowHorse.class, "shadowhorse");
		arrowEntity(EntityBlackCat.class, "blackcat");
		arrowEntity(EntityTamedKit.class, "tamedkit");
		arrowEntity(EntityPoisonMagic.class, "poisonmagic");
		arrowEntity(EntityMeteorMagic.class, "meteormagic");
		arrowEntity(EntityRockBlast.class, "rockblast");
		arrowEntity(EntityBlackHole.class, "blackhole");
		arrowEntity(EntityBabuleMagic.class, "babulemagic");
		arrowEntity(EntityShinigFlare.class, "shiningflare");
		arrowEntity(EntitySuperNova.class, "supernova");
		arrowEntity(EntitySittableBlock.class, "sittable");
		arrowEntity(EntityMagicItem.class, "magicitem");
		arrowEntity(EntityBlastBomb.class, "blastbomb");
		arrowEntity(EntityCuriousCrystal.class, "curiouscrystal");
		arrowEntity(EntityCushion.class, "smcushion");
		arrowEntity(EntityMagicCycle.class, "magiccycle");
		arrowEntity(EntityLockBullet.class, "lockbullet");
		arrowEntity(EntityDragonBullet.class, "dragonbullet");

		eggEntity(EntitySkullFrost.class, "skullfrost");
		eggEntity(EntityCreeperCal.class, "creepercalamity");
		eggEntity(EntityBlazeTempest.class, "blazetempest");
		eggEntity(EntityEnderShadow.class, "endershadow");
		eggEntity(EntityElectricCube.class, "electriccube");
		eggEntity(EntityArchSpider.class, "archspider");
		eggEntity(EntityPixieVex.class, "pixievex");
		eggEntity(EntitySilderGhast.class, "silder_ghast");
		eggEntity(EntityPhantomZombie.class, "phantomzombie");
		eggEntity(EntityEvilCrystal.class, "evilcrystal");
		eggEntity(EntityWitchMadameVerre.class, "witchmadameverre");
		eggEntity(EntityWindineVerre.class, "windine");
		eggEntity(EntityIfritVerre.class, "ifrite");
		eggEntity(EntityBraveSkeleton.class, "braveskeleton");
		eggEntity(EntitySandryon.class, "sandryon");
		eggEntity(EntityZombieHora.class, "zombiehora");
		eggEntity(EntityZombieKronos.class, "zombiekronos");
		eggEntity(EntityFairySkyDwarfKronos.class, "fairyskydwarfkronos");
		eggEntity(EntityAncientFairy.class, "ancientfairy");
		eggEntity(EntityRepairKitt.class, "repairkitt");
		eggEntity(EntityElshariaCurious.class, "elsharia_curious");

	}

	//スポーンエッグを出す
	public static void eggEntity(Class<? extends Entity> css, String name) {
		EntityRegistry.registerModEntity(new ResourceLocation(SweetMagicCore.MODID, name), css, name, count, SweetMagicCore.INSTANCE, 128, 1, true, 1000, 255);
		count++;
	}

	//スポーンエッグを出さない
	public static void arrowEntity(Class<? extends Entity> css, String name) {
		EntityRegistry.registerModEntity(new ResourceLocation(SweetMagicCore.MODID, name), css, name, count, SweetMagicCore.INSTANCE, 128, 1, true);
		count++;
	}

	// 村人読み込み
	public void loadVillager() {
		VillagerInit.init();
		VillagerInit.regsterTrade();
	}
}
