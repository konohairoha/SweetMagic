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
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityBraveSkeleton;
import sweetmagic.init.entity.monster.EntityCreeperCal;
import sweetmagic.init.entity.monster.EntityElectricCube;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityIfritVerre;
import sweetmagic.init.entity.monster.EntityPhantomZombie;
import sweetmagic.init.entity.monster.EntityPixieVex;
import sweetmagic.init.entity.monster.EntityShadowGolem;
import sweetmagic.init.entity.monster.EntityShadowWolf;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.init.entity.monster.EntityWindineVerre;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;
import sweetmagic.init.entity.monster.EntityZombieHora;
import sweetmagic.init.entity.monster.EntityZombieKronos;
import sweetmagic.init.entity.projectile.EntityBabuleMagic;
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
import sweetmagic.init.entity.projectile.EntityMagicItem;
import sweetmagic.init.entity.projectile.EntityMeteorMagic;
import sweetmagic.init.entity.projectile.EntityNomal;
import sweetmagic.init.entity.projectile.EntityPoisonMagic;
import sweetmagic.init.entity.projectile.EntityRockBlast;
import sweetmagic.init.entity.projectile.EntityShinigFlare;
import sweetmagic.init.entity.projectile.EntitySittableBlock;

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
		arrowEntity(EntityPoisonMagic.class, "poisonmagic");
		arrowEntity(EntityMeteorMagic.class, "meteormagic");
		arrowEntity(EntityFrostRain.class, "frostrain");
		arrowEntity(EntityRockBlast.class, "rockblast");
		arrowEntity(EntityBabuleMagic.class, "babulemagic");
		arrowEntity(EntityShinigFlare.class, "shiningflare");
		arrowEntity(EntitySittableBlock.class, "sittable");
		arrowEntity(EntityMagicItem.class, "magicitem");

		eggEntity(EntitySkullFrost.class, "skullfrost");
		eggEntity(EntityCreeperCal.class, "creepercalamity");
		eggEntity(EntityBlazeTempest.class, "blazetempest");
		eggEntity(EntityEnderShadow.class, "endershadow");
		eggEntity(EntityElectricCube.class, "electriccube");
		eggEntity(EntityArchSpider.class, "archspider");
		eggEntity(EntityPhantomZombie.class, "phantomzombie");
		eggEntity(EntityWitchMadameVerre.class, "witchmadameverre");
		eggEntity(EntityWindineVerre.class, "windine");
		eggEntity(EntityIfritVerre.class, "ifrite");
		eggEntity(EntityPixieVex.class, "pixievex");
		eggEntity(EntityZombieHora.class, "zombiehora");
		arrowEntity(EntityZombieKronos.class, "zombiekronos");
		eggEntity(EntityBraveSkeleton.class, "braveskeleton");
		eggEntity(EntityAncientFairy.class, "ancientfairy");

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
