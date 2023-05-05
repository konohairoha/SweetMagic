package sweetmagic.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.enchant.EnchantWand;

@ObjectHolder(SweetMagicCore.MODID)
@Mod.EventBusSubscriber
public class EnchantInit {

	public static Enchantment mfCostDown, mfCoolTimeDown;
	public static Enchantment wandAddPower;
	public static Enchantment maxMFUP;
	public static Enchantment mfRecover;
	public static Enchantment aetherCharm;
	public static Enchantment elementBonus;

	public static List<Enchantment> enchantList = new ArrayList<>();

	public static void init () {
		mfCostDown = new EnchantWand(Enchantment.Rarity.COMMON, "mfcostdown", 5);
		mfCoolTimeDown = new EnchantWand(Enchantment.Rarity.RARE, "mfcooltimedown", 5);
		maxMFUP = new EnchantWand(Enchantment.Rarity.UNCOMMON, "maxmfup", 5);
		wandAddPower = new EnchantWand(Enchantment.Rarity.RARE, "wandaddpower", 5);
		mfRecover = new EnchantWand(Enchantment.Rarity.UNCOMMON, "mfrecover", 3);
		aetherCharm = new EnchantWand(Enchantment.Rarity.UNCOMMON, "aethercharm", 1);
		elementBonus = new EnchantWand(Enchantment.Rarity.VERY_RARE, "elementbonus", 5);
	}

	public static void register(IForgeRegistry<Enchantment> register) {
		for (Enchantment enchant : enchantList) {
			register.register(enchant);
		}
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Enchantment> event) {
		EnchantInit.init();
		EnchantInit.register(event.getRegistry());
	}
}
