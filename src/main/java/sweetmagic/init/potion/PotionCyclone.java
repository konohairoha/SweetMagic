package sweetmagic.init.potion;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import sweetmagic.SweetMagicCore;

public class PotionCyclone extends PotionBase {

	public static final UUID MODIFIER_UUID = UUID.fromString("CE9DBC2A-EE3F-43F5-1222-F7F1EE4915A9");

	public PotionCyclone (boolean effect, int color, String name, String dir) {
		super(effect, color, name, dir);
		ForgeRegistries.POTIONS.register(this.setRegistryName(SweetMagicCore.MODID, name)
				.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, MODIFIER_UUID.toString(), 0.15000000596046448D, 2));
	}
}
