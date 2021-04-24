package sweetmagic.init;

import net.minecraft.advancements.CriteriaTriggers;
import sweetmagic.advanced.AdvancedTrigger;

public class AdvancedInit {

	public static final AdvancedTrigger active_magic = new AdvancedTrigger("active_magic");
	public static final AdvancedTrigger astral_craft = new AdvancedTrigger("astral_craft");
	public static final AdvancedTrigger magic_craft = new AdvancedTrigger("magic_craft");

	public static void register() {
		CriteriaTriggers.register(active_magic);
		CriteriaTriggers.register(astral_craft);
		CriteriaTriggers.register(magic_craft);
	}
}
