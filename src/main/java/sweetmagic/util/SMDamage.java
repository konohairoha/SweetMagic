package sweetmagic.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class SMDamage extends DamageSource {

	public SMDamage(String name) {
		super("SweetMagicDamage");
	}

	public static DamageSource MagicDamage(Entity par1, Entity par2) {
		return (new EntityDamageSourceIndirect("magic", par1, par2)).setProjectile();
	}
}
