package sweetmagic.init.potion;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import sweetmagic.SweetMagicCore;
import sweetmagic.util.SMDamage;

public class PotionFrost extends PotionBase {

	public static final UUID MODIFIER_UUID = UUID.fromString("CE9DBC2A-EE3F-43F5-9DF7-F7F1EE4915A9");

	public PotionFrost (boolean effect, int color, String name, String dir) {
		super(effect, color, name, dir);
		ForgeRegistries.POTIONS.register(this.setRegistryName(SweetMagicCore.MODID, name)
				.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, MODIFIER_UUID.toString(), -0.15000000596046448D, 2));
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {

		if (amplifier < 4) { return; }

		DamageSource src = SMDamage.flostDamage;
		entity.attackEntityFrom(src, 1F);
		entity.hurtResistantTime = 0;
	}

	public boolean isReady(int duration, int amplifier) {
		return duration % 10 == 0;
	}
}
