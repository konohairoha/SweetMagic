package sweetmagic.init.potion;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import sweetmagic.SweetMagicCore;

public class PotionResitanceBlow extends PotionBase {

	private static final UUID MODIFIER_UUID = UUID.fromString("BD6D692F-B923-4221-B2A8-3500022D2A9C");

	public PotionResitanceBlow (boolean effect, int color, String name, String dir) {
		super(effect, color, name, dir);
		ForgeRegistries.POTIONS.register(this.setRegistryName(SweetMagicCore.MODID, name)
				.registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, MODIFIER_UUID.toString(), 0.5D, 0));
	}
}
