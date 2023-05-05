package sweetmagic.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import sweetmagic.SweetMagicCore;

public class LootTableInit {

	// チェスト類
	public static final ResourceLocation PYM = new ResourceLocation(SweetMagicCore.MODID, "pyramid_boss");
	public static final ResourceLocation SMBOUNUS = new ResourceLocation(SweetMagicCore.MODID, "smbonus_chest");
	public static final ResourceLocation SMFOODS = new ResourceLocation(SweetMagicCore.MODID, "food_chest");
	public static final ResourceLocation MOBCHEST = new ResourceLocation(SweetMagicCore.MODID, "mobdrop_cehst");
	public static final ResourceLocation MEQCHEST = new ResourceLocation(SweetMagicCore.MODID, "mekyu_chest");
	public static final ResourceLocation BIGINERCHEST = new ResourceLocation(SweetMagicCore.MODID, "biginer_chest");
	public static final ResourceLocation IDOCHEST = new ResourceLocation(SweetMagicCore.MODID, "ido_chest");
	public static final ResourceLocation CASTLECHEST = new ResourceLocation(SweetMagicCore.MODID, "castle_chest");
	public static final ResourceLocation SKYLANDOLD = new ResourceLocation(SweetMagicCore.MODID, "skyland_old");
	public static final ResourceLocation FLUGELCHEST = new ResourceLocation(SweetMagicCore.MODID, "flugel_chest");

	// モブドロップ
	public static final ResourceLocation SKULLFROST = new ResourceLocation(SweetMagicCore.MODID, "entity/skullfrost");
	public static final ResourceLocation CREEPERCALAMITY = new ResourceLocation(SweetMagicCore.MODID, "entity/creepercalamity");
	public static final ResourceLocation BLAZETEMPEST = new ResourceLocation(SweetMagicCore.MODID, "entity/blazetempest");
	public static final ResourceLocation ENDERSHADOW = new ResourceLocation(SweetMagicCore.MODID, "entity/endershadow");
	public static final ResourceLocation ELECTRICCUBE = new ResourceLocation(SweetMagicCore.MODID, "entity/electriccube");
	public static final ResourceLocation ARCHSPIDER = new ResourceLocation(SweetMagicCore.MODID, "entity/archspider");
	public static final ResourceLocation PIXIEVEX = new ResourceLocation(SweetMagicCore.MODID, "entity/pixievex");
	public static final ResourceLocation SILDER_GHAST = new ResourceLocation(SweetMagicCore.MODID, "entity/silder_ghast");
	public static final ResourceLocation PHANTOMZOMBIE = new ResourceLocation(SweetMagicCore.MODID, "entity/phantomzombie");
	public static final ResourceLocation WITCHMADAMEVERRE = new ResourceLocation(SweetMagicCore.MODID, "entity/witchmadameverre");

	public static void regsterLoottable () {
		LootTableList.register(SKULLFROST);
		LootTableList.register(CREEPERCALAMITY);
		LootTableList.register(BLAZETEMPEST);
		LootTableList.register(ENDERSHADOW);
		LootTableList.register(ELECTRICCUBE);
		LootTableList.register(ARCHSPIDER);
		LootTableList.register(PIXIEVEX);
		LootTableList.register(SILDER_GHAST);
		LootTableList.register(PHANTOMZOMBIE);
		LootTableList.register(WITCHMADAMEVERRE);
	}
}
