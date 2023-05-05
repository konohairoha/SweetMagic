package sweetmagic.init;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.advanced.AdvancedItemStackTrigger;
import sweetmagic.advanced.AdvancedTrigger;

public class AdvancedInit {

	public static final AdvancedTrigger active_magic = new AdvancedTrigger("active_magic");
	public static final AdvancedTrigger astral_craft = new AdvancedTrigger("astral_craft");
	public static final AdvancedTrigger magic_craft = new AdvancedTrigger("magic_craft");
	public static final AdvancedTrigger witch_craft = new AdvancedTrigger("witch_craft");
	public static final AdvancedTrigger apprentice_magician = new AdvancedTrigger("apprentice_magician");
	public static final AdvancedTrigger intermediate_magician = new AdvancedTrigger("intermediate_magician");
	public static final AdvancedTrigger advanced_magician = new AdvancedTrigger("advanced_magician");
	public static final AdvancedTrigger maestro_magician = new AdvancedTrigger("maestro_magician");
	public static final AdvancedItemStackTrigger item_use = new AdvancedItemStackTrigger( new ResourceLocation(SweetMagicCore.MODID, "item_use"));

	public static void register() {
		CriteriaTriggers.register(active_magic);
		CriteriaTriggers.register(astral_craft);
		CriteriaTriggers.register(magic_craft);
		CriteriaTriggers.register(witch_craft);
		CriteriaTriggers.register(apprentice_magician);
		CriteriaTriggers.register(intermediate_magician);
		CriteriaTriggers.register(advanced_magician);
		CriteriaTriggers.register(maestro_magician);
		CriteriaTriggers.register(item_use);
	}
}
